package com.max2.fileformat;

import com.max2.pojo.Record;
import play.Logger;

public class RecordFormat {
	
	/*
	 * Check for format 2 and create record
	 */
	public Record getRecordInFormat2(String[] row) {
		String names[]=row[0].split(" ");
		if(ColumnChecker.isName(row[1]) && ColumnChecker.isName(names[0]) && ColumnChecker.isName(names[1]) &&
			ColumnChecker.isZipCode(row[2]) && ColumnChecker.isPhoneNumber(row[3])){
			Logger.info("Format 2 is satisfied");
			Record record=new Record();
			record.setFirstName(names[0]);
			record.setLastName(names[1]);
			record.setColor(row[1]);
			record.setZipcode(row[2]);
			record.setPhoneNumber(row[3]);
			return record;
		}
		return null;
	}
	
	/*
	 * Check for format 4 and create record
	 */
	public Record getRecordInFormat4(String[] row) {
		String names[]=row[0].split(" ");
		if(ColumnChecker.isName(names[0]) && ColumnChecker.isName(names[1]) && ColumnChecker.isAddress(row[1])
		&& ColumnChecker.isZipCode(row[2]) && ColumnChecker.isPhoneNumber(row[3]) && ColumnChecker.isName(row[4])){
			Logger.info("Format 4 is satisfied");
			Record record=new Record();
			record.setFirstName(names[0]);
			record.setLastName(names[1]);
			record.setAddress(row[1]);
			record.setZipcode(row[2]);
			record.setPhoneNumber(row[3]);
			record.setColor(row[4]);
			return record;
		}
		return null;
	}
	
	/*
	 * Check for format 1 and create record
	 */
	public Record getRecordInFormat1(String[] row) {
		if(ColumnChecker.isName(row[0]) && ColumnChecker.isName(row[1]) && ColumnChecker.isPhoneNumber(row[2]) &&
			ColumnChecker.isName(row[3]) && ColumnChecker.isZipCode(row[4])){
			Logger.info("Format 1 is satisfied");
			Record record=new Record();
			record.setFirstName(row[0]);
			record.setLastName(row[1]);
			record.setPhoneNumber(row[2]);
			record.setColor(row[3]);
			record.setZipcode(row[4]);
			return record;
		}
		return null;
	}
	
	/*
	 * Check for format 3 and create record
	 */
	public Record getRecordInFormat3(String[] row) {
		if(ColumnChecker.isName(row[0]) && ColumnChecker.isName(row[1]) && ColumnChecker.isZipCode(row[2]) &&
				ColumnChecker.isPhoneNumber(row[3]) && ColumnChecker.isName(row[4])){
			Logger.info("Format 3 is satisfied");
			Record record=new Record();
			record.setFirstName(row[0]);
			record.setLastName(row[1]);
			record.setZipcode(row[2]);
			record.setPhoneNumber(row[3]);
			record.setColor(row[4]);
			return record;
		}
		return null;
	}

}
