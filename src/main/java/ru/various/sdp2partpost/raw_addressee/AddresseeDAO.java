package ru.various.sdp2partpost.raw_addressee;

import ru.various.sdp2partpost.addressee.Addressee;

import javax.sql.DataSource;
import java.util.List;
import java.util.Properties;

public interface AddresseeDAO {
    /**
     * This is the method to be used to initialize
     * database resources ie. connection.
     */
    public void setDataSource(DataSource ds);

    /**
     *
     */
    public List<RawAddressee> findRawAddr(Properties properties);

	/**
	 *
	 * @param properties
	 * @return
	 */
	public List<Addressee> findAddr(Properties properties);

	/**
	 *
	 * @param addresses
	 */
	public int insert(List<Addressee> addresses);
}
