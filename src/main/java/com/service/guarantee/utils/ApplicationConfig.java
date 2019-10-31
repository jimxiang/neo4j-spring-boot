package com.service.guarantee.utils;

import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;
import org.apache.log4j.Logger;

public class ApplicationConfig {

	private static final Logger logger = Logger.getLogger(ApplicationConfig.class);
	private static final Properties props = new Properties();

	static {
		InputStream is = null;
		try {
			is = ApplicationConfig.class.getResourceAsStream("/application.properties");
			props.load(is);
		} catch (Throwable th) {
			logger.error("Exception when loading application config.." + CommonFunction.GetErrorStack(th));

		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Throwable e) {
					logger.error("Exception when closing application config.." + CommonFunction.GetErrorStack(e));
				}
			}
		}
	}

	public static String get(String key) {
		if (!props.containsKey(key)) {
			logger.info(String.format("====== There is no %s parameter in /config.properties, so no use neo4j monitor.",
					key));
			return null;
		} else {
			return props.getProperty(key);
		}
	}

	/**
	 *
	 * @param key
	 * @param defaultValue
	 * @param clz
	 * @return
	 */
	public static <T> T getOrDefaultValue(String key, T defaultValue, Class<T> clz) {

		if (!props.containsKey(key)) {
			return defaultValue;
		}

		String value = props.getProperty(key);

		Object object = null;
		try {
			if (Objects.equals(clz, Boolean.class)) {
				object = false;
				return clz.cast(Boolean.valueOf(value));
			} else if (Objects.equals(clz, Integer.class)) {
				object = 0;
				return clz.cast(Integer.valueOf(value));
			} else if (Objects.equals(clz, Float.class)) {
				object = 0.0f;
				return clz.cast(Float.valueOf(value));
			} else if (Objects.equals(clz, Double.class)) {
				object = 0.0;
				return clz.cast(Double.valueOf(value));
			} else {
				return clz.cast(value);
			}
		} catch (Exception e) {
			return clz.cast(object);
		}
	}

	public static String getValue(String key, String defaultValue) {
		if (props.containsKey(key)) {
			return props.getProperty(key).trim();
		}else {
			logger.info(String.format("There is no %s parameter in /config.properties, so use default: %s.", key, defaultValue));
			return defaultValue;
		}
	}

	public static int getValue(String key, int defaultValue) {
		String value = getValue(key, String.valueOf(defaultValue));
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			logger.error(String.format("NumberFormatException: The Value in .config is \"%s\", Use Default Value: %s", value, String.valueOf(defaultValue)));
			return defaultValue;
		}
	}

	public static float getValue(String key, float defaultValue) {
		String value = getValue(key, String.valueOf(defaultValue));
		try {
			return Float.parseFloat(value);
		} catch (NumberFormatException e) {
			logger.error(String.format("NumberFormatException: The Value in .config is \"%s\", Use Default Value: %s", value, String.valueOf(defaultValue)));
			return defaultValue;
		}
	}

	public static boolean getValue(String key, boolean defaultValue) {
		String value = getValue(key, String.valueOf(defaultValue));

		boolean returnValue = Boolean.parseBoolean(value);
		if (value.equals(String.valueOf(returnValue))) {
			return returnValue;
		}else {
			logger.error(String.format("Parse Error, The Value in .config is \"%s\", Use Default Value: %s", value, String.valueOf(defaultValue)));
			return defaultValue;
		}
	}

	public static void main(String[] args) {
	}
}