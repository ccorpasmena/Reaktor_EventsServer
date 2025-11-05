package es.iesjandula.proyecto_calendario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.iesjandula.proyecto_calendario.models.Categoria;

public interface ICategoriaRepository extends JpaRepository<Categoria, String>
{

}
