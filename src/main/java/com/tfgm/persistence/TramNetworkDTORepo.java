package com.tfgm.persistence;

import com.tfgm.models.TramNetworkDTO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TramNetworkDTORepo extends MongoRepository<TramNetworkDTO, String> {}
