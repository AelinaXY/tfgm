package com.tfgm.persistence.mapper;

import com.tfgm.models.Journey;
import com.tfgm.models.TramHistory;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TramHistoryMapper {
    @Insert(
        "INSERT INTO tramhistory(tramhistoryid,tramid,origin,destination,timeatorigin,timeatdestination,status) VALUES (#{tramHistoryId, javaType=java.util.UUID, jdbcType=OTHER, typeHandler=UUIDTypeHandler}, #{tramId, javaType=java.util.UUID, jdbcType=OTHER, typeHandler=UUIDTypeHandler}, #{origin}, #{destination},  #{timeAtOrigin},  #{timeAtDestination},  #{status});")
    void create(TramHistory journey);
}
