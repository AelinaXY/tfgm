package com.tfgm.persistence;

import com.tfgm.models.TramNetworkDTO;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TramNetworkDTORepo extends JpaRepository<TramNetworkDTO, UUID> {
    TramNetworkDTO findByTimestamp(Long timestamp);
}
