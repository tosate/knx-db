package de.devtom.java.utils;

public class ServiceUtils {
	private ServiceUtils() {
		throw new IllegalStateException("Utility class");
	}
	
	public static boolean compare(String str1, String str2) {
		return (str1 == null ? str2 == null :str1.equals(str2));
	}
}
