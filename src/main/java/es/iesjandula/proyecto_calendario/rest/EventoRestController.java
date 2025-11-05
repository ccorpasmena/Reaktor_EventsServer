package es.iesjandula.proyecto_calendario.rest;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.iesjandula.proyecto_calendario.dto.EventoRequestDto;
import es.iesjandula.proyecto_calendario.models.Categoria;
import es.iesjandula.proyecto_calendario.models.Evento;
import es.iesjandula.proyecto_calendario.models.Usuario;
import es.iesjandula.proyecto_calendario.repository.ICategoriaRepository;
import es.iesjandula.proyecto_calendario.repository.IEventoRepository;
import es.iesjandula.proyecto_calendario.repository.IUsuarioRepository;
import es.iesjandula.proyecto_calendario.utils.CalendarioException;
import es.iesjandula.proyecto_calendario.utils.Constants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/evento")
@RestController
public class EventoRestController
{
    @Autowired
    private IEventoRepository eventoRepository;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private ICategoriaRepository categoriaRepository;

    @PostMapping(value = "/", consumes = "application/json")
    public ResponseEntity<?> crearEvento(@RequestBody EventoRequestDto eventoRequestDto)
    {
        try
        {
            if (eventoRequestDto.getTitulo() == null || eventoRequestDto.getTitulo().isEmpty())
            {
                log.error(Constants.ERR_EVENTO_TITULO_NULO_VACIO);
                throw new CalendarioException(Constants.ERR_EVENTO_CODE, Constants.ERR_EVENTO_TITULO_NULO_VACIO);
            }

            if (eventoRepository.existsById(eventoRequestDto.getTitulo()))
            {
                log.error(Constants.ERR_EVENTO_EXISTE);
                throw new CalendarioException(Constants.ERR_EVENTO_CODE, Constants.ERR_EVENTO_EXISTE);
            }
            Optional<Usuario> usuarioOpt = this.usuarioRepository.findById(eventoRequestDto.getUsuarioId());
            if (!usuarioOpt.isPresent())
            {
                log.error(Constants.ERR_USUARIO_NO_EXISTE);
                throw new CalendarioException(Constants.ERR_USUARIO_CODE, Constants.ERR_USUARIO_NO_EXISTE);
            }
            Usuario usuario = usuarioOpt.get();
            
            Optional<Categoria> categoriaOpt = this.categoriaRepository.findById(eventoRequestDto.getCategoriaId());
            if (!categoriaOpt.isPresent())
            {
                log.error(Constants.ERR_CATEGORIA_NO_EXISTE);
                throw new CalendarioException(Constants.ERR_CATEGORIA_CODE, Constants.ERR_CATEGORIA_NO_EXISTE);
            }
            Categoria categoria = categoriaOpt.get();

            Evento evento = new Evento();
            evento.setTitulo(eventoRequestDto.getTitulo());
            evento.setFechaInicio(eventoRequestDto.getFechaInicio());
            evento.setFechaFin(eventoRequestDto.getFechaFin());
            evento.setUsuario(usuario);
            evento.setCategoria(categoria);

            eventoRepository.saveAndFlush(evento);
            log.info(Constants.ELEMENTO_AGREGADO);
            return ResponseEntity.ok().body(Constants.ELEMENTO_AGREGADO);
        }
        catch (CalendarioException e)
        {
            return ResponseEntity.badRequest().body(e);
        }
    }

   @PutMapping(value = "/", consumes = "application/json")
   public ResponseEntity<?> modificarEvento(@RequestBody EventoRequestDto eventoRequestDto)
   {
       try
       {
           if (eventoRequestDto.getTitulo() == null || eventoRequestDto.getTitulo().isEmpty())
           {
               log.error(Constants.ERR_EVENTO_TITULO_NULO_VACIO);
               throw new CalendarioException(Constants.ERR_EVENTO_CODE, Constants.ERR_EVENTO_TITULO_NULO_VACIO);
           }

           Optional<Evento> eventoOpt = eventoRepository.findById(eventoRequestDto.getTitulo());
           if (!eventoOpt.isPresent())
           {
               log.error(Constants.ERR_EVENTO_NO_EXISTE);
               throw new CalendarioException(Constants.ERR_EVENTO_CODE, Constants.ERR_EVENTO_NO_EXISTE);
           }

           Evento evento = eventoOpt.get();

           if (eventoRequestDto.getFechaFin().before(eventoRequestDto.getFechaInicio()))
           {
               log.error(Constants.ERR_EVENTO_FECHAS_INVALIDAS);
               throw new CalendarioException(Constants.ERR_EVENTO_CODE, Constants.ERR_EVENTO_FECHAS_INVALIDAS);
           }

           Optional<Usuario> usuarioOpt = usuarioRepository.findById(eventoRequestDto.getUsuarioId());
           if (!usuarioOpt.isPresent())
           {
               log.error(Constants.ERR_USUARIO_NO_EXISTE);
               throw new CalendarioException(Constants.ERR_USUARIO_CODE, Constants.ERR_USUARIO_NO_EXISTE);
           }
           Usuario usuario = usuarioOpt.get();

           Categoria categoria = null;
           if (eventoRequestDto.getCategoriaId() != null)
           {
               Optional<Categoria> categoriaOpt = categoriaRepository.findById(eventoRequestDto.getCategoriaId());
               if (!categoriaOpt.isPresent())
               {
                   log.error(Constants.ERR_CATEGORIA_NO_EXISTE);
                   throw new CalendarioException(Constants.ERR_CATEGORIA_CODE, Constants.ERR_CATEGORIA_NO_EXISTE);
               }
               categoria = categoriaOpt.get();
           }

           evento.setTitulo(eventoRequestDto.getTitulo());
           evento.setFechaInicio(eventoRequestDto.getFechaInicio());
           evento.setFechaFin(eventoRequestDto.getFechaFin());
           evento.setUsuario(usuario);
           evento.setCategoria(categoria);

           eventoRepository.saveAndFlush(evento);
           log.info(Constants.ELEMENTO_MODIFICADO);
           return ResponseEntity.ok().body(Constants.ELEMENTO_MODIFICADO);
       }
       catch (CalendarioException e)
       {
           return ResponseEntity.badRequest().body(e);
       }
   }

    @DeleteMapping(value="/{idEvento}")
    public ResponseEntity<?> eliminarEvento(@PathVariable String idEvento)
    {
        try
        {
            if (!eventoRepository.existsById(idEvento))
            {
                log.error(Constants.ERR_EVENTO_NO_EXISTE);
                throw new CalendarioException(Constants.ERR_EVENTO_CODE, Constants.ERR_EVENTO_NO_EXISTE);
            }

            eventoRepository.deleteById(idEvento);
            log.info(Constants.ELEMENTO_ELIMINADO);
            return ResponseEntity.ok().body(Constants.ELEMENTO_ELIMINADO);
        }
        catch (CalendarioException e)
        {
            return ResponseEntity.badRequest().body(e);
        }
    }

    @GetMapping(value="/")
    public ResponseEntity<?> listarEventos()
    {
        return ResponseEntity.ok(eventoRepository.findAll());
    }
}
