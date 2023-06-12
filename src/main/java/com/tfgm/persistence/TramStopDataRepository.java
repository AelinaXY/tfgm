package com.tfgm.persistence;

import com.tfgm.models.TramStopData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TramStopDataRepository extends MongoRepository<TramStopData, String> {}
