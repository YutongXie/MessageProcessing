package com.xyt.msgprocess.util;

/**
 * limit to use external jar files, can't use Apache commons libs
 *
 */
public class StringUtils {

	public static boolean isBlank(String input) {
		if (input == null)
			return true;

		if ("".equals(input.trim()))
			return true;

		return false;
	}

	public static boolean isNotBlank(String input) {
		return !isBlank(input);
	}

	public static String trimToEmpty(String input) {

		if (isBlank(input))
			return "";

		return input.trim();
	}

}
