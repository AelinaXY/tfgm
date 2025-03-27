package com.tfgm.persistence.mapper;

import com.tfgm.models.HistoryStatus;
import com.tfgm.models.Journey;
import com.tfgm.models.TramHistory;
import com.tfgm.typehandler.UUIDTypeHandler;
import org.apache.ibatis.annotations.*;

import java.util.Set;
import java.util.UUID;

@Mapper
public interface TramHistoryMapper {
    @Insert(
        "INSERT INTO tramhistory(tramhistoryid,tramid,origin,destination,timeatorigin,timeatdestination,status) VALUES (#{tramHistoryId, javaType=java.util.UUID, jdbcType=OTHER, typeHandler=UUIDTypeHandler}, #{tramId, javaType=java.util.UUID, jdbcType=OTHER, typeHandler=UUIDTypeHandler}, #{origin}, #{destination},  #{timeAtOrigin},  #{timeAtDestination},  #{status});")
    void create(TramHistory journey);

    @Results(id = "historyResult")
    @ConstructorArgs({
        @Arg(column = "tramhistoryid", javaType = UUID.class, typeHandler = UUIDTypeHandler.class, id = true),
        @Arg(column = "tramid", javaType = UUID.class, typeHandler = UUIDTypeHandler.class),
        @Arg(column = "origin", javaType = String.class),
        @Arg(column = "destination", javaType = String.class),
        @Arg(column = "timeatorigin", javaType = Long.class),
        @Arg(column = "timeatdestination", javaType = Long.class),
        @Arg(column = "status", javaType = String.class),
    })
    @Select("select * from tramhistory where status= 'QUEUED' order by timeatorigin asc limit 200")
    Set<TramHistory> getNextTramHistorySet();

    @Update("update tramhistory set status = #{status} where tramhistoryid = #{tramHistoryId}")
    void updateTramHistoryStatus(UUID tramHistoryId, String status);
}
