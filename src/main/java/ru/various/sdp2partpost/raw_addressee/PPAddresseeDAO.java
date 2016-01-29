package ru.various.sdp2partpost.raw_addressee;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.SqlParameter;
import ru.various.sdp2partpost.LoggingCallback;
import ru.various.sdp2partpost.addressee.Addressee;

import java.sql.*;
import java.util.*;


public class PPAddresseeDAO extends AbstractAddresseeDAO {
	public static final int ADDR_FLAG = 1;

	/**
	 * @param properties
	 */
	@Override
	public List<RawAddressee> findRawAddr(Properties properties) {

		String query = "SELECT family name, indexto zipcode, adres address, street dup_address, '+м+' gender FROM Z_ADRESAT";


		return getJdbcTemplate().query(query, new RawPPAddresseeMapper());
	}

	@Override
	public List<Addressee> findAddr(Properties properties) {
		return null;
	}

	/**
	 * @param addresses
	 */
	@Override
	public int insert(List<Addressee> addresses, LoggingCallback<Addressee> callback) {

		if (addresses.size() == 0)
			return 0;

		int counter = 0;
		List<SqlParameter> params = new ArrayList<>();
		params.add(new SqlParameter("indexto", 12));

		Iterator<Addressee> iterator = addresses.iterator();
		for (;iterator.hasNext();) {
			Addressee addressee = iterator.next();
			String insertSql = "INSERT INTO z_adresat (id_adresat, family, indexto, region, area, city, adres, adr_flag) " +
					"VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

			// address components map resolved by zipcode
			Map<String, Object> result = getJdbcTemplate().call(
					getRenewedCallableStatementCreator(addressee.getAddress().getZipcode()),
					params
			);

			Map<String, Object> index2address = ((ArrayList<Map<String, Object>>) result.get("#result-set-1")).get(0);
			String region = (String) index2address.get("region");
			String area = (String) index2address.get("area");
			String city = (String) index2address.get("city");

			if (region == null && area == null && city == null) {
					iterator.remove();
                    callback.inform(addressee, false, "Индекс не найден в базе");
					continue;
			}

			getJdbcTemplate().update(
					insertSql,
					getNextAddresseeId(), addressee.getName(), addressee.getAddress().getZipcode(),
					region, area, city,
					addressee.getAddress().getPartialAddress(), ADDR_FLAG
			);
            callback.inform(addressee, true, "");

			counter++;
		}

		return counter;
	}

	private CallableStatementCreator getRenewedCallableStatementCreator(final int zipcode) {
		return new CallableStatementCreator() {

			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				CallableStatement statement = con.prepareCall("{call MR_FULLDEPNAME(?)}");
				statement.setString(1, String.valueOf(zipcode));
				return statement;
			}
		};
	}

	private Long getNextAddresseeId() {
		return (Long) getJdbcTemplate().execute(
				new PreparedStatementCreator() {
					@Override
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
						return connection.prepareStatement("SELECT GEN_ID(z_id_adresat, 1) FROM RDB$DATABASE;");
					}
				},
				new PreparedStatementCallback() {
					@Override
					public Object doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {
						ResultSet rs = preparedStatement.executeQuery();
						Long value = null;
						while(rs.next()) {
							value = rs.getLong(1);
						}
						return value;
					}
				});
	}
}
