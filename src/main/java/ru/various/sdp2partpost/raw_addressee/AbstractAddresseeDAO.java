package ru.various.sdp2partpost.raw_addressee;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public abstract class AbstractAddresseeDAO implements AddresseeDAO {
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;

	/**
	 * This is the method to be used to initialize
	 * database resources ie. connection.
	 *
	 * @param dataSource
	 */
	@Override
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		jdbcTemplate = new JdbcTemplate(dataSource, true);
		jdbcTemplate.execute("SELECT 1 FROM RDB$DATABASE");
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
}
