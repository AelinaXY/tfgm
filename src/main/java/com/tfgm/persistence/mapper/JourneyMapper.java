package com.tfgm.persistence.mapper;

import com.tfgm.models.Journey;
import org.apache.ibatis.annotations.*;

@Mapper
public interface JourneyMapper {
  @Insert(
      "INSERT INTO journeys(uuid,getontime,getofftime,tramuuid,getonstop,getoffstop,personuuid) VALUES (#{uuid, javaType=java.util.UUID, jdbcType=OTHER, typeHandler=UUIDTypeHandler}, #{getOnTime}, #{getOffTime}, #{tramUUID, javaType=java.util.UUID, jdbcType=OTHER, typeHandler=UUIDTypeHandler}, #{getOnStop}, #{getOffStop},#{personUUID, javaType=java.util.UUID, jdbcType=OTHER, typeHandler=UUIDTypeHandler});")
  void create(Journey journey);
}
