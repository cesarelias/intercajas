package py.edu.uca.intercajas.shared.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import com.sun.istack.internal.NotNull;

/**
 * EntityBase, base entity for all entities.
 */
@MappedSuperclass
public abstract class EntityBase implements Serializable {

	private static final long serialVersionUID = 8834347344694007534L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	protected Long id;

	@NotNull @Version
	private Long version;

	public Long getId() {
		return id;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

}