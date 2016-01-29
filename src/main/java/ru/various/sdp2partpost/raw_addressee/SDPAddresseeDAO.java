package ru.various.sdp2partpost.raw_addressee;

import org.apache.commons.lang3.text.StrSubstitutor;
import ru.various.sdp2partpost.LoggingCallback;
import ru.various.sdp2partpost.addressee.Addressee;

import java.util.List;
import java.util.Properties;

public class SDPAddresseeDAO extends AbstractAddresseeDAO {

    @Override
    public List<RawAddressee> findRawAddr(Properties properties) {

        String queryStr = "SELECT parts.${name} name, parts.${address} address, comments gender " +
                "FROM ${parts_table} parts " +
                "INNER JOIN ${case_table} casee " +
                "ON parts.${case_id} = casee.${id} " +
                "WHERE parts.${case_id} " +
                "IN " +
                "(SELECT casee.${id} " +
                "FROM ${case_table} casee " +
                "WHERE casee.${full_number} IN ${cases})";

        return
                getJdbcTemplate().query(StrSubstitutor.replace(queryStr, properties), new RawSDPAddresseeMapper());
    }

	@Override
	public List<Addressee> findAddr(Properties properties) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param addresses
	 */
	@Override
	public int insert(List<Addressee> addresses, LoggingCallback<Addressee> callback) {
		throw new UnsupportedOperationException();
	}

}
