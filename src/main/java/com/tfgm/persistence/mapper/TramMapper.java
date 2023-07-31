package com.tfgm.persistence.mapper;

import com.tfgm.models.Tram;
import com.tfgm.typehandler.JSONMapHandler;
import com.tfgm.typehandler.JSONMapLongHandler;
import com.tfgm.typehandler.UUIDTypeHandler;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mapper
public interface TramMapper {
  @Insert(
      "INSERT INTO trams(uuid,destination,origin,endofline,tramhistory,lastupdated,removed) VALUES (#{uuid, javaType=java.util.UUID, jdbcType=OTHER, typeHandler=UUIDTypeHandler}, #{destination}, #{origin}, #{endOfLine}, #{tramHistory, javaType=java.util.Map, jdbcType=OTHER, typeHandler=JSONMapHandler}, #{lastUpdated},#{toRemove})"
       + " ON CONFLICT (uuid) DO UPDATE SET destination = excluded.destination, origin = excluded.origin,"
          + " endofline = excluded.endofline, tramhistory = excluded.tramhistory, lastupdated = excluded.lastupdated, removed=excluded.removed;")
  void create(Tram tram);


    @Results(id = "tramResult")
    @ConstructorArgs({
        @Arg(column = "uuid", javaType = UUID.class, typeHandler = UUIDTypeHandler.class, id = true),
        @Arg(column = "endofline", javaType = String.class),
        @Arg(column = "destination", javaType = String.class),
        @Arg(column = "origin", javaType = String.class),
        @Arg(column = "lastupdated", javaType = Long.class),
        @Arg(column = "tramhistory", javaType = Map.class, typeHandler = JSONMapLongHandler.class)
    })
    @Select("DELETE FROM trams WHERE uuid = #{uuid, javaType=java.util.UUID, jdbcType=OTHER, typeHandler=UUIDTypeHandler} RETURNING *")
    Tram delete(@Param("uuid") UUID uuid);

    @Delete("DELETE FROM trams")
    int deleteAll();

  @ResultMap("tramResult")
  @Select(
      "select * from trams where (tramhistory ->> #{stopName})::INTEGER > #{timestamp} and (tramhistory ->> #{stopName})::INTEGER < #{timestamp}+7200 ORDER BY (tramhistory ->> #{stopName})::INTEGER;")
  List<Tram> getAfterTS(@Param("timestamp") Long timestamp, @Param("stopName") String stopName);

  @ResultMap("tramResult")
  @Select("select * from trams")
  List<Tram> getAll();
}
