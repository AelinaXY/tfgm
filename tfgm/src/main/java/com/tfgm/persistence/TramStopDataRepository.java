package com.tfgm.persistence;

import com.tfgm.models.TramStopDTO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TramStopDataRepository extends MongoRepository<TramStopDTO, String> {}
