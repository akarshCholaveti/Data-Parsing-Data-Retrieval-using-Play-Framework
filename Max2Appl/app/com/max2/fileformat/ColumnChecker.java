package com.max2.fileformat;

public class ColumnChecker {
	
	/*
	 * Check for alphabets in String 
	 */
	public static boolean isName(String name){
		return name.matches( "[A-Z][a-zA-Z]*" );
	}
	
	/*
	 * Check for a Phone Number
	 */
	public static boolean isPhoneNumber(String phoneNumber){
		if(phoneNumber.matches("\\d{3}[-\\s]\\d{3}[-\\s]\\d{4}")){
			return true;
		}else if(phoneNumber.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")){
			return true;
		}
		return false;
	}
	
	/*
	 * Check for the zip code
	 */
	public static boolean isZipCode(String zipCode){
		return zipCode.matches("^[0-9]{5}(?:-[0-9]{4})?$");
	}
	
	/*
	 * Check for address
	 */
	public static boolean isAddress(String address){
		return address.matches("\\d+\\s+([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z]+)" );
	}
	
}
