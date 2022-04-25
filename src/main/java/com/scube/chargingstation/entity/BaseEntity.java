package com.scube.chargingstation.entity;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.Instant;


@MappedSuperclass
@Getter @Setter @ToString 
public abstract class BaseEntity {

	@Id
	@GeneratedValue(generator = "custom-generator", strategy = GenerationType.IDENTITY)
	@GenericGenerator(
			name = "custom-generator",
			strategy = "com.scube.chargingstation.model.id.generator.BaseIdentifierGenerator")
	protected String id;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false, nullable = false)
	protected Instant createdAt;

	@UpdateTimestamp
	@Column(name = "modified_at")
	protected Instant modifiedAt;

	@Column
	@Version
	protected int version;
	
	@Column(name = "is_deleted")
	protected String isdeleted;

	/**
	 * Indicates whether the object has already been persisted or not
	 *
	 * @return true if the object has not yet been persisted
	 */
	public boolean isNew() {
		return getId()==null;
	}
}