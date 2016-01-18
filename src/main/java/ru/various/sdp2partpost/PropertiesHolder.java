package ru.various.sdp2partpost;

import ru.various.sdp2partpost.enums.Source;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.prefs.Preferences;

/**
 * Hold all properties, corresponding to work with database context.
 * Especially should hold {@code path}, {@code username} and {@code password} properties.
 */
public class PropertiesHolder {
    public static final String PATH = "path";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String LC_CTYPE = "lc_ctype";
	public static final String SOURCE = "source";

	private Map<Source, Properties> propertiesMap = new HashMap<>();

    public PropertiesHolder() {

    }

    /**
     * Set property correspond to {@code type} and {@code key}. Invoke properties base method.
     *
     * @param type shows where store property
     * @param key the key to be placed into this property list
     * @param value the value corresponding to {@code key}
     * @return previous value corresponding to {@code key}
     */
    public Object setProperty(Source type, String key, String value) {
        return getAppropriateProperties(type).setProperty(key, value);
    }

    /**
     * Get property correspond to {@code type} and {@code key}. Invoke properties base method.
     *
     * @param type determine which property get
     * @param key the property key
     * @return value corresponding to {@code key}
     */
    public String getProperty(Source type, String key) {
        return getAppropriateProperties(type).getProperty(key);
    }

    /**
     * Get property correspond to {@code type} and {@code key}. Invoke properties base method.
     *
     * @param type determine which property get
     * @param key the property key
     * @return value corresponding to {@code key} or {@code defaultValue}
     */
    public String getProperty(Source type, String key, String defaultValue) {

        return getAppropriateProperties(type).getProperty(key, defaultValue);
    }

    /**
     * Get properties corresponding to {@code type}.
     *
     * @param type determine which properties should be returned
     * @return properties according to {@code type}
     */
    public Properties getProperties(Source type) {
        return getAppropriateProperties(type);
    }

    /**
     * Emits an XML document representing all of the properties contained in this table.
     *
     * @param type determine which properties store
     * @param os the output stream on which to emit the XML document
     * @param comments description to {@code property}
     * @throws IOException
     */
    public void storeToXML(Source type, OutputStream os, String comments) throws IOException {
        getAppropriateProperties(type).storeToXML(os, comments);
    }

    /**
     * Emits an XML document representing all of the properties contained in this table.
     * @param type determine what properties store
     * @param os the output stream on which to emit the XML document
     * @param comments description to {@code property}
     * @param encoding the name of a supported {@see character encoding}
     * @throws IOException
     */
    public void storeToXML(Source type, OutputStream os, String comments, String encoding) throws IOException {
        getAppropriateProperties(type).storeToXML(os, comments, encoding);
    }

    /**
     * Store choosen by {@code type} properties to a registry
     *
     * @param type determine what properties store
     */
    public void storeToRegistry(Source type) {
        Properties properties = getAppropriateProperties(type);
        Preferences preferences = Preferences.userNodeForPackage(PropertiesHolder.class).node(type.toString());
        preferences.put(PATH, properties.getProperty(PATH, ""));
        preferences.put(USERNAME, properties.getProperty(USERNAME, ""));
        preferences.put(PASSWORD, properties.getProperty(PASSWORD, ""));
        preferences.put(LC_CTYPE, properties.getProperty(LC_CTYPE, "WIN1251"));
//		preferences.flush();
    }

    public void loadFromRegistry(Source type) {
        Properties properties = getAppropriateProperties(type);
        Preferences preferences = Preferences.userNodeForPackage(PropertiesHolder.class).node(type.toString());

        properties.put(PATH, preferences.get(PATH, ""));
        properties.put(USERNAME, preferences.get(USERNAME, ""));
        properties.put(PASSWORD, preferences.get(PASSWORD, ""));
    }

    /**
     * Method check properties according to {@code type} to eligibility
     * which means that mandatory properties {@code path}, {@code username}
     * and {@code password} notnull.
     *
     * @param type determines which properties check
     * @return true if all mandatory properties notnull.
     */
    public boolean isEligible(Source type) {
        Properties properties = getAppropriateProperties(type);
        String path = properties.getProperty(PATH, "");
        String username = properties.getProperty(USERNAME, "");
        String password = properties.getProperty(PASSWORD, "");
        String lc_ctype = properties.getProperty(LC_CTYPE, "");

        return !(path.isEmpty() | username.isEmpty() | password.isEmpty() | lc_ctype.isEmpty());
    }

    private Properties getAppropriateProperties(Source type) {
		Properties properties = null;

		if (!propertiesMap.containsKey(type)) {
			properties = new Properties();
			properties.setProperty(LC_CTYPE, "WIN1251");
			properties.setProperty(SOURCE, type.toString());

			propertiesMap.put(type, properties);
			loadFromRegistry(type);
		}
		else
			properties = propertiesMap.get(type);

		return properties;
    }


}
