package ru.various.sdp2partpost.raw_addressee;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

class RawSDPAddresseeMapper implements RowMapper<RawAddressee> {
    @Override
    public RawAddressee mapRow(ResultSet resultSet, int i) throws SQLException {

        return new RawAddressee(
                resultSet.getString("name"),
                resultSet.getString("address"),
                resultSet.getString("gender"));
    }
}
