package com.tfgm.typehandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.Reader;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import org.apache.ibatis.type.*;
import org.postgresql.util.PGobject;

/** Maps a map to a flat postgres JSONB field. */
@SuppressWarnings("DuplicatedCode")
@MappedJdbcTypes(JdbcType.OTHER)
@MappedTypes(Map.class)
@Alias(value = "JSONMapLongHandler")
public class JSONMapLongHandler extends BaseTypeHandler<Map<String, Long>> {
  private static final TypeReference<Map<String, Long>> MAP_TYPE = new TypeReference<>() {};

  @Override
  public void setNonNullParameter(
      final PreparedStatement statement,
      final int columnIndex,
      final Map<String, Long> parameter,
      final JdbcType jdbcType)
      throws SQLException {
    final PGobject jsonObject = new PGobject();

    jsonObject.setType("jsonb");

    try {
      jsonObject.setValue(new ObjectMapper().writeValueAsString(parameter));
    } catch (final JsonProcessingException jpe) {
      throw new SQLException("Error converting map to JSON.", jpe);
    }

    statement.setObject(columnIndex, jsonObject);
  }

  @Override
  public Map<String, Long> getNullableResult(final ResultSet resultSet, final String columnName)
      throws SQLException {
    final Reader result = resultSet.getCharacterStream(columnName);

    if (!resultSet.wasNull()) {
      try {
        return new ObjectMapper().readValue(result, MAP_TYPE);
      } catch (final IOException ioe) {
        throw new SQLException("Error reading JSON map.", ioe);
      }
    }

    return null;
  }

  @Override
  public Map<String, Long> getNullableResult(final ResultSet resultSet, final int columnIndex)
      throws SQLException {
    final Reader result = resultSet.getCharacterStream(columnIndex);

    if (!resultSet.wasNull()) {
      try {
        return new ObjectMapper().readValue(result, MAP_TYPE);
      } catch (final IOException ioe) {
        throw new SQLException("Error reading JSON map.", ioe);
      }
    }

    return null;
  }

  @Override
  public Map<String, Long> getNullableResult(
      final CallableStatement statement, final int columnIndex) throws SQLException {
    final Reader result = statement.getCharacterStream(columnIndex);

    if (!statement.wasNull()) {
      try {
        return new ObjectMapper().readValue(result, MAP_TYPE);
      } catch (final IOException ioe) {
        throw new SQLException("Error reading JSON map.", ioe);
      }
    }

    return null;
  }
}
