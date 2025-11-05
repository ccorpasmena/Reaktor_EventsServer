package es.iesjandula.proyecto_calendario.rest;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import es.iesjandula.proyecto_calendario.dto.CategoriaRequestDto;
import es.iesjandula.proyecto_calendario.models.Categoria;
import es.iesjandula.proyecto_calendario.repository.ICategoriaRepository;
import es.iesjandula.proyecto_calendario.utils.CalendarioException;
import es.iesjandula.proyecto_calendario.utils.Constants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/categoria")
@RestController
public class CategoriaRestController
{
    @Autowired
    private ICategoriaRepository categoriaRepository;

    @PostMapping(value = "/", consumes = "application/json")
    public ResponseEntity<?> crearCategoria(@RequestBody CategoriaRequestDto categoriaRequestDto)
    {
        try
        {
            if (categoriaRequestDto.getNombre() == null || categoriaRequestDto.getNombre().isEmpty())
            {
                log.error(Constants.ERR_CATEGORIA_NOMBRE_NULO_VACIO);
                throw new CalendarioException(Constants.ERR_CATEGORIA_CODE, Constants.ERR_CATEGORIA_NOMBRE_NULO_VACIO);
            }

            if (categoriaRepository.existsById(categoriaRequestDto.getNombre()))
            {
                log.error(Constants.ERR_CATEGORIA_EXISTE);
                throw new CalendarioException(Constants.ERR_CATEGORIA_CODE, Constants.ERR_CATEGORIA_EXISTE);
            }

            Categoria categoria = new Categoria();
            categoria.setNombre(categoriaRequestDto.getNombre());
            categoria.setColor(categoriaRequestDto.getColor());

            categoriaRepository.saveAndFlush(categoria);
            log.info(Constants.ELEMENTO_AGREGADO);
            return ResponseEntity.ok().body(Constants.ELEMENTO_AGREGADO);
        }
        catch (CalendarioException e)
        {
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PutMapping(value = "/", consumes = "application/json")
    public ResponseEntity<?> modificarCategoria(@RequestBody CategoriaRequestDto categoriaRequestDto)
    {
        try
        {
            Optional<Categoria> optionalCategoria = categoriaRepository.findById(categoriaRequestDto.getNombre());
            if (!optionalCategoria.isPresent())
            {
                log.error(Constants.ERR_CATEGORIA_NO_EXISTE);
                throw new CalendarioException(Constants.ERR_CATEGORIA_CODE, Constants.ERR_CATEGORIA_NO_EXISTE);
            }

            Categoria categoria = optionalCategoria.get();
            categoria.setNombre(categoriaRequestDto.getNombre());
            categoria.setColor(categoriaRequestDto.getColor());

            categoriaRepository.saveAndFlush(categoria);
            log.info(Constants.ELEMENTO_MODIFICADO);
            return ResponseEntity.ok().body(Constants.ELEMENTO_MODIFICADO);
        }
        catch (CalendarioException e)
        {
            return ResponseEntity.badRequest().body(e);
        }
    }

    @DeleteMapping(value="/{idCategoria}")
    public ResponseEntity<?> eliminarCategoria(@PathVariable String idCategoria)
    {
        try
        {
            if (!categoriaRepository.existsById(idCategoria))
            {
                log.error(Constants.ERR_CATEGORIA_NO_EXISTE);
                throw new CalendarioException(Constants.ERR_CATEGORIA_CODE, Constants.ERR_CATEGORIA_NO_EXISTE);
            }

            categoriaRepository.deleteById(idCategoria);
            log.info(Constants.ELEMENTO_ELIMINADO);
            return ResponseEntity.ok().body(Constants.ELEMENTO_ELIMINADO);
        }
        catch (CalendarioException e)
        {
            return ResponseEntity.badRequest().body(e);
        }
    }

    @GetMapping(value="/")
    public ResponseEntity<?> listarCategorias()
    {
        return ResponseEntity.ok(categoriaRepository.findAll());
    }
}
