package es.iesjandula.proyecto_calendario.models;

import es.iesjandula.proyecto_calendario.models.ids.EventoId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "eventos")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Evento
{
	@EmbeddedId
    private EventoId id;

	@ManyToOne
    @JoinColumn(name = "usuario_correo", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "categoria_nombre")
    private Categoria categoria;
}
