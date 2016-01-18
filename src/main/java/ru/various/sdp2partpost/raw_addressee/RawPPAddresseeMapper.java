package ru.various.sdp2partpost.raw_addressee;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

class RawPPAddresseeMapper implements RowMapper<RawAddressee> {
	@Override
	public RawAddressee mapRow(ResultSet resultSet, int i) throws SQLException {
        String address = resultSet.getString("address") != null
		        ? resultSet.getString("address")
		        : resultSet.getString("dup_address");

		return new RawAddressee(
				resultSet.getString("name"),
				resultSet.getString("zipcode") + ", " + address,
				resultSet.getString("gender"));
	}
}
