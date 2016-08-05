package com.max2.filereader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.max2.fileformat.ColumnChecker;
import com.max2.fileformat.RecordFormat;
import com.max2.pojo.ColorDetailWithNames;
import com.max2.pojo.ColorDetails;
import com.max2.pojo.Record;
import com.max2.pojo.VenueNames;

import play.Logger;

public class CSVFileReader extends AbstractFileReader {
	private static final String UNICODE_FORMAT = "UTF-8";
	private static final String LINE_SEPARATOR = "\\n";
	private static final int FOUR = 4;
	private static final int FIVE = 5;
	private static final String SPACE = " ";
	private static final int FOURTEEN = 14;
	private HashMap<String,ArrayList<Record>> colorDetails;
	private HashMap<ColorDetails,ArrayList<String>> colorInfoForApi;
	private static CSVFileReader csvReader;
	
	private CSVFileReader(){
		
	}
	
	public static CSVFileReader getReaderInstance(){
		if(csvReader==null){
			csvReader=new CSVFileReader();
		}
		return csvReader;
	}
	
	@Override
	public List<Object> parseFile(InputStream inputStream) {
		
		Logger.info("In Parsing function");
		BufferedReader reader;
		List<Object> recordList = null;
		
		try {
			colorDetails = new HashMap<String,ArrayList<Record>>();
			recordList=new ArrayList<Object>();
			reader= new BufferedReader(new InputStreamReader(inputStream));
			String line = null;
		    while ((line = reader.readLine()) != null) {
		    	Logger.info("Reading line");
		    	String[] row=line.split(",");
		        Record record = getRecord(row);
		        if(record!=null){
		        	recordList.add(record);
		        	ArrayList<Record> tempList;
		        	if(colorDetails.containsKey(record.getColor())){
		        		tempList=colorDetails.get(record.getColor());
		        		tempList.add(record);
		        	}else{
		        		tempList=new ArrayList<Record>();
		        		tempList.add(record);
		        	}
		        	colorDetails.put(record.getColor(), tempList);
		        }
		    }
		} catch (UnsupportedEncodingException e) {
			Logger.error("Exception occured"+e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Logger.error("Exception occured"+e.getMessage());
			e.printStackTrace();
		}
		storeDetailsInHashMap();
		return recordList;
	}

	public Record getRecord(String[] row) {
		Logger.info("Getting values from record array");
		System.out.println(Arrays.toString(row));
		System.out.println("Lenght:"+row.length);
		RecordFormat format=new RecordFormat();
		Record record = null;
		if(row.length==FOUR){
			 record=format.getRecordInFormat2(row);
		}else if(row.length==FIVE){
			if(row[0].contains(SPACE)){
				 record=format.getRecordInFormat4(row);
			}else if(ColumnChecker.isPhoneNumber(row[2])){
				 record=format.getRecordInFormat1(row);
			}else if(ColumnChecker.isZipCode(row[2])){
				record=format.getRecordInFormat3(row);
			}else{
				Logger.info("Error in record details");
				Logger.info(Arrays.toString(row));
			}
		}else{
			Logger.info("Error in record details");
			Logger.info(Arrays.toString(row));
		}
		return record;
	}

	public HashMap<String, ArrayList<Record>> getColorDetails() {
		return colorDetails;
	}

	public void printDetailsByColor() {
		Logger.info("Printing Details By Color");
		for(String color:colorDetails.keySet()){
			System.out.println(color+"  "+colorDetails.get(color).size());
		}
		
	}

	public void printNameDetailsByColor() {
		Logger.info("Printing Name Details By Color");
		for(String color:colorDetails.keySet()){
			System.out.print(color+"  "+colorDetails.get(color).size());
			for(Record record:colorDetails.get(color)){
				System.out.print(record.getFirstName()+" "+record.getLastName()+" ");
			}
			System.out.println();
		}
		
	}
	
	public Set<ColorDetails> getDetailsOfColor(){
		Logger.info("Getting JSON for color count");
		try{
		    return colorInfoForApi.keySet();
		}catch(Exception e){
			Logger.error("Color information is empty");
			return new HashSet<ColorDetails>();
		}
	}
	
	public List<ColorDetailWithNames> getDetialsOfColorWithNames(){
		List<ColorDetailWithNames> list=new ArrayList<ColorDetailWithNames>();
		Logger.info("Getting JSON for color count with names");
		for(ColorDetails c:colorInfoForApi.keySet()){
			ColorDetailWithNames detail=new ColorDetailWithNames();
			detail.setColor(c.getColor());
			detail.setCount(c.getCount());
			detail.setFullName(colorInfoForApi.get(c));
			list.add(detail);
		}
		return list;
	}

	private void storeDetailsInHashMap() {
		colorInfoForApi = new HashMap<ColorDetails,ArrayList<String>>();
		Logger.info("Storing details in MAP");
		for(String color:colorDetails.keySet()){
			System.out.println(color);
			System.out.println(colorDetails.get(color).size());
		}
		if(colorInfoForApi==null){
			System.out.println("Is NULL");
		}else{
			System.out.println("Not Null");
		}
		for(String color:colorDetails.keySet()){
			ColorDetails colorDetail=new ColorDetails();
			colorDetail.setColor(color);
			colorDetail.setCount(colorDetails.get(color).size());
			ArrayList<String> nameList=new ArrayList<String>();
			for(Record record:colorDetails.get(color)){
				nameList.add(record.getFirstName()+" "+record.getLastName());
			}
			colorInfoForApi.put(colorDetail, nameList);
		}
		
	}
	
	public VenueNames getVenueNames(String city, String state) {
		ArrayList<String> venueNames = new ArrayList<String>();
    	Properties prop = new Properties();
    	InputStream input = null;
    	VenueNames names=new VenueNames();
    	try{
    		input = new FileInputStream("key.properties");
    		prop.load(input);
    		String DATE_FORMAT = "yyyyMMdd";
    	    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
    	    Calendar c1 = Calendar.getInstance(); // today
    	URL url = new URL("https://api.foursquare.com/v2/venues/search?near="+city+","+state
    			+ "&query=coffee"
    			+ "&v="+sdf.format(c1.getTime())+"&m=foursquare"
    			+ "&client_secret="+prop.getProperty("client_secret")
    			+ "&client_id="+prop.getProperty("client_id"));
    	URLConnection conn1= url.openConnection();
    	BufferedReader streamReader = new BufferedReader(new InputStreamReader(conn1.getInputStream()));
    	StringBuilder responseStrBuilder = new StringBuilder();
		String inputStr;
		while ((inputStr = streamReader.readLine()) != null)
		    responseStrBuilder.append(inputStr);
    	
		JsonFactory factory = new JsonFactory();
		JsonParser  parser  = factory.createParser(responseStrBuilder.toString());
		String name=new String();
		while(!parser.isClosed()){
		    JsonToken jsonToken = parser.nextToken();
		    
		    //System.out.println("jsonToken = " + jsonToken);
		    if(JsonToken.FIELD_NAME.equals(jsonToken)){
		        String fieldName = parser.getCurrentName();
		        //System.out.println(fieldName);
		        jsonToken = parser.nextToken();
		        if("name".equals(fieldName)){
		        	name=parser.getValueAsString();
		        }
		        if("location".equals(fieldName)){
		        	venueNames.add(name);
		        }
		    }
		}
		names.setPlaces(venueNames);
    	}
    	catch(Exception e){
    		
    	}
		return names;
	}

}
