package es.iesjandula.proyecto_calendario.models;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categorias")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Categoria
{
    @Id
    @Column(length = 100)
    private String nombreCategoria;

    @Column(length = 10)
    private String colorCategoria;

    @OneToMany(mappedBy = "categoria")
    private List<Evento> eventos;
}
