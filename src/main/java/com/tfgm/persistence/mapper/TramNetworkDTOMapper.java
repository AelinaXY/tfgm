package com.tfgm.persistence.mapper;

import com.tfgm.models.Tram;
import com.tfgm.models.TramNetworkDTO;
import com.tfgm.typehandler.JSONtoTramListTypeHandler;
import com.tfgm.typehandler.UUIDTypeHandler;
import java.util.List;
import java.util.UUID;
import org.apache.ibatis.annotations.*;

@Mapper
public interface TramNetworkDTOMapper {

  @Results(id = "TNResult")
  @ConstructorArgs({
    @Arg(column = "uuid", javaType = UUID.class, typeHandler = UUIDTypeHandler.class, id = true),
    @Arg(column = "timestamp", javaType = Long.class),
    @Arg(column = "tramJson", javaType = List.class, typeHandler = JSONtoTramListTypeHandler.class),
  })
  @Select("SELECT * FROM tramnetwork")
  List<TramNetworkDTO> getAll();

  @Select("SELECT timestamp FROM tramnetwork ORDER BY timestamp ASC")
  List<Long> getAllTimestamps();

  @ResultMap("TNResult")
  @Select("SELECT * FROM tramnetwork WHERE timestamp = #{timestamp} LIMIT 1")
  TramNetworkDTO getByTimestamp(@Param("timestamp") Long timestamp);

  @Insert(
      "INSERT INTO tramnetwork(uuid,timestamp,tramjson) VALUES (#{uuid, javaType=java.util.UUID, jdbcType=OTHER, typeHandler=UUIDTypeHandler}, #{timestamp}, #{tramArrayList, javaType=java.util.List, jdbcType=OTHER, typeHandler=JSONtoTramListTypeHandler})")
  void create(TramNetworkDTO tramNetworkDTO);

}
