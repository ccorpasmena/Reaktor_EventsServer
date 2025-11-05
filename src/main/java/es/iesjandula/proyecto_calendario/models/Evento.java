package es.iesjandula.proyecto_calendario.models;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "eventos")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Evento
{
    @Id
    @Column(length = 150)
    private String titulo;
    @Id
    @Column(nullable = false)
    private Date fechaInicio;
    @Id
    @Column(nullable = false)
    private Date fechaFin;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

}
