package com.tfgm.persistence.mapper;

import com.tfgm.models.Journey;
import com.tfgm.models.Person;
import com.tfgm.models.Tram;
import com.tfgm.typehandler.JSONMapLongHandler;
import com.tfgm.typehandler.UUIDTypeHandler;
import org.apache.ibatis.annotations.*;

import java.util.Map;
import java.util.UUID;

@Mapper
public interface PersonMapper {
  @Insert(
      "INSERT INTO people(uuid,name,tapintime,tapinstop,tapouttime,tapoutstop) VALUES (#{uuid, javaType=java.util.UUID, jdbcType=OTHER, typeHandler=UUIDTypeHandler}, #{name}, #{tapInTime}, #{tapInStop}, #{tapOutTime}, #{tapOutStop});")
  void create(Person person);

    @Results(id = "personResult")
    @ConstructorArgs({
        @Arg(column = "uuid", javaType = UUID.class, typeHandler = UUIDTypeHandler.class, id = true),
        @Arg(column = "name", javaType = String.class),
        @Arg(column = "tapintime", javaType = Long.class),
        @Arg(column = "tapinstop", javaType = String.class),
        @Arg(column = "tapouttime", javaType = Long.class),
        @Arg(column = "tapoutstop", javaType = String.class)
    })
    @Select("SELECT * FROM people WHERE uuid = #{uuid, javaType=java.util.UUID, jdbcType=OTHER, typeHandler=UUIDTypeHandler}")
    Person get(@Param("uuid") UUID uuid);
}
