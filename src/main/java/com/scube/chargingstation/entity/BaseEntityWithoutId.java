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
public abstract class BaseEntityWithoutId {

	@CreationTimestamp
	@Column(name = "created_at", updatable = false, nullable = false)
	protected Instant createdAt;

	@UpdateTimestamp
	@Column(name = "modified_at")
	protected Instant modifiedAt;

	@Column
	@Version
	protected int version;

}