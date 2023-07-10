package com.tfgm.persistence.mapper;

import com.tfgm.models.Journey;
import org.apache.ibatis.annotations.*;

import java.util.UUID;

@Mapper
public interface JourneyMapper {
  @Insert(
      "INSERT INTO journeys(uuid,getontime,getofftime,tramuuid,getonstop,getoffstop,personuuid) VALUES (#{uuid, javaType=java.util.UUID, jdbcType=OTHER, typeHandler=UUIDTypeHandler}, #{getOnTime}, #{getOffTime}, #{tramUUID, javaType=java.util.UUID, jdbcType=OTHER, typeHandler=UUIDTypeHandler}, #{getOnStop}, #{getOffStop},#{personUUID, javaType=java.util.UUID, jdbcType=OTHER, typeHandler=UUIDTypeHandler});")
  void create(Journey journey);

  @Select("select count (uuid) from journeys where tramuuid = #{uuid, javaType=java.util.UUID, jdbcType=OTHER, typeHandler=UUIDTypeHandler} and getontime < #{timestamp} and getofftime > #{timestamp}; ")
  Long countPassengers(@Param("uuid") UUID uuid, @Param("timestamp")Long timestamp);
}
