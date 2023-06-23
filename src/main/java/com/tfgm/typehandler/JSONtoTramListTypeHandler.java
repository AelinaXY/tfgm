package com.tfgm.typehandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfgm.models.Tram;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.postgresql.util.PGobject;

@MappedJdbcTypes(JdbcType.OTHER)
@MappedTypes(List.class)
public class JSONtoTramListTypeHandler extends BaseTypeHandler<List<Tram>> {
    private static final TypeReference<Tram> TRAM_TYPE_REFERENCE = new TypeReference<>() {};


    @Override
  public void setNonNullParameter(
      final PreparedStatement statement,
      final int columnIndex,
      final List<Tram> list,
      final JdbcType jdbcType)
      throws SQLException {
    final PGobject[] jsonValues = new PGobject[list.size()];

    try {
      int i = 0;

      for (final Tram tram : list) {
        final PGobject jsonObject = new PGobject();

        jsonObject.setType("jsonb");
        jsonObject.setValue(new ObjectMapper().writeValueAsString(tram));
        jsonValues[i] = jsonObject;
        i++;
      }
    } catch (final JsonProcessingException jpe) {
      throw new SQLException("Error converting List to JSON.", jpe);
    }
    statement.setArray(columnIndex, statement.getConnection().createArrayOf("jsonb", jsonValues));
  }

    @Override
  public List<Tram> getNullableResult(
      final ResultSet resultSet, final String columnName) throws SQLException {
    final Array outputArray = resultSet.getArray(columnName);

    if (!resultSet.wasNull()) {
      return readTramList((String[]) outputArray.getArray());
    }

    return null;
  }

  @Override
  public List<Tram> getNullableResult(
      final ResultSet resultSet, final int columnIndex) throws SQLException {
    final Array outputArray = resultSet.getArray(columnIndex);

    if (!resultSet.wasNull()) {
      return readTramList((String[]) outputArray.getArray());
    }

    return null;
  }

  @Override
  public List<Tram> getNullableResult(
      final CallableStatement callableStatement, final int columnIndex) throws SQLException {
    final Array outputArray = callableStatement.getArray(columnIndex);

    if (!callableStatement.wasNull()) {
      return readTramList((String[]) outputArray.getArray());
    }

    return null;
  }

  private List<Tram> readTramList(final String[] array) throws SQLException {

    final List<Tram> tramList = new ArrayList<>(array.length);

    try {
      for (final String tram : array) {
        tramList.add(new ObjectMapper().readValue(tram, TRAM_TYPE_REFERENCE));
      }
    } catch (final IOException ioe) {
      throw new SQLException("Error reading UriLink.", ioe);
    }

    return tramList;
  }
}
