package com.tfgm.persistence;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.tfgm.models.TramNetworkDTO;
import com.tfgm.models.TramStop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 */
@Repository
public interface TramNetworkDTORepoBasic extends JpaRepository<TramNetworkDTO, UUID> {
    TramNetworkDTO findByTimestamp(Long timestamp);
    List<TramNetworkDTO> findAll();

}
