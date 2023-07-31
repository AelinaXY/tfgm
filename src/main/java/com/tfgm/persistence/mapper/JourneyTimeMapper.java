package com.tfgm.persistence.mapper;

import com.tfgm.models.JourneyTime;
import com.tfgm.models.Person;
import com.tfgm.models.TFGMResponse;
import com.tfgm.typehandler.UUIDTypeHandler;
import java.util.List;
import java.util.UUID;
import org.apache.ibatis.annotations.*;

@Mapper
public interface JourneyTimeMapper {
    @Insert(
        "INSERT INTO journeytime(uuid,origin,destination,time,averagecount) VALUES (#{uuid, javaType=java.util.UUID, jdbcType=OTHER, typeHandler=UUIDTypeHandler},#{origin}, #{destination},#{time}, #{averageCount})"
            + " ON CONFLICT (uuid) DO UPDATE SET origin = excluded.origin, destination = excluded.destination, time = excluded.time, averagecount = excluded.averagecount;")
    void saveJourneyTime(JourneyTime journeyTime);

    @Results(id = "journeyTimeResult")
    @ConstructorArgs({
        @Arg(column = "uuid", javaType = UUID.class, typeHandler = UUIDTypeHandler.class, id = true),
        @Arg(column = "origin", javaType = String.class),
        @Arg(column = "destination", javaType = String.class),
        @Arg(column = "time", javaType = Long.class),
        @Arg(column = "averagecount", javaType = Long.class),
    })
    @Select("SELECT * FROM journeytime")
    List<JourneyTime> getAll();


    @ResultMap("journeyTimeResult")
    @Select("SELECT * FROM journeytime where destination = #{destination}")
    List<JourneyTime> getDestination(String destination);
}
