package com.scube.chargingstation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scube.chargingstation.entity.ConnectorStatusEntity;

@Repository
public interface ConnectorStatusRepository  extends JpaRepository<ConnectorStatusEntity, String>  {

}