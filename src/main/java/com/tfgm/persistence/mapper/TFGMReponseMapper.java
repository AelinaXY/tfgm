package com.tfgm.persistence.mapper;

import com.tfgm.models.TFGMResponse;
import com.tfgm.typehandler.JSONObjectTypeHandler;
import java.util.List;
import org.apache.ibatis.annotations.*;
import org.json.JSONObject;

@Mapper
public interface TFGMReponseMapper {
  @Insert(
      "INSERT INTO tramdata(timestamp,response) VALUES (#{timestamp}, #{response, javaType=org.json.JSONObject, jdbcType=OTHER, typeHandler=JSONObjectTypeHandler})"
          + " ON CONFLICT (timestamp) DO UPDATE SET timestamp = excluded.timestamp, response = excluded.response;")
  void saveResponse(TFGMResponse response);

  @Results(id = "tramDataResponse")
  @ConstructorArgs({
    @Arg(column = "timestamp", javaType = Long.class),
    @Arg(
        column = "response",
        javaType = JSONObject.class,
        typeHandler = JSONObjectTypeHandler.class),
  })
  @Select("SELECT *  FROM tramdata ORDER BY timestamp ASC")
  List<TFGMResponse> getAll();
}
