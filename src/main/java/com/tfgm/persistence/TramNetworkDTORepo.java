package com.tfgm.persistence;

import com.tfgm.models.TramNetworkDTO;
import com.tfgm.models.TramStop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;

/**
 *
 */
@Repository
public interface TramNetworkDTORepo extends TramNetworkDTORepoBasic, TramNetworkDTORepoCustom {
}
