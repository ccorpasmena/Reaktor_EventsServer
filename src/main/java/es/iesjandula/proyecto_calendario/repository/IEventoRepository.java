package es.iesjandula.proyecto_calendario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.iesjandula.proyecto_calendario.models.Evento;

public interface IEventoRepository extends JpaRepository<Evento, String>
{

}
