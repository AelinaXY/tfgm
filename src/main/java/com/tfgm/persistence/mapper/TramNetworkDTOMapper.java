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

//    void create(TramNetworkDTO );
}
