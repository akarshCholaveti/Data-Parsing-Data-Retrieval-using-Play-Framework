package controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.Normalizer.Form;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.max2.filereader.CSVFileReader;
import com.max2.pojo.ColorDetailWithNames;
import com.max2.pojo.ColorDetails;
import com.max2.pojo.Record;
import com.max2.pojo.Venue;
import com.max2.pojo.VenueNames;

import play.mvc.*;
import views.html.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import static play.libs.Json.toJson;
import play.data.DynamicForm;
import play.data.Form.*;
import play.mvc.Http.Request;
import play.db.ebean.Model;
import play.Logger;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
    	Logger.debug("Home Page");
        return ok(index.render("Hello Akarsh."));
    }
    
    public Result uploadFile(){
//    	
    	MultipartFormData body = request().body().asMultipartFormData();
        FilePart<File> picture = body.getFile("csvFile");  
        Logger.debug("File upload funtion is called");
    	
        if (picture != null && picture.getFilename().contains(".csv")) {
            String fileName = picture.getFilename();
            CSVFileReader reader=CSVFileReader.getReaderInstance();
            try{
            File file=picture.getFile();
            Logger.debug("Streaming the file");
            InputStream inputStream=reader.loadFile(file);
            Logger.debug("Parsing the file");
            reader.parseFile(inputStream);
            System.out.println(reader.getColorDetails());
            }catch(Exception e){
            	Logger.debug("Exception Occcured:"+e.getMessage());
            }
            return ok(apichoice.render("File uploaded"));
        } else {
            //flash("error", "Missing file");
        	Logger.debug("Wrong File uploaded");
            return ok(index.render("Wrong File Uploaded"));    
        }
    	
    }
    
    public Result enterData(){
    	CSVFileReader reader=CSVFileReader.getReaderInstance();
    	final Map<String,String[]> entries = request().body().asFormUrlEncoded();
    	if(entries==null){
    		System.out.println("It is Null");
    	}else{
    		System.out.println("Not NUll");
    	}
    	String data =null;
    	for(String key:entries.keySet()){
    		if(key.equalsIgnoreCase("data")){
    			data=Arrays.toString(entries.get(key));
    		}
    	}
    	data=data.substring(1, data.length()-1);
    	String records[]=data.split(",");
    	System.out.println(data);
    	System.out.println("Records");
    	for(int i=0;i<records.length;i++){
    		records[i]=records[i].trim();
    	}
    	Record record=reader.getRecord(records);
    	System.out.println(record);
    	record.save();
    	return ok(toJson(record.getPhoneNumber()));
    }
    
    public Result getColorDetails(){
    	CSVFileReader reader=CSVFileReader.getReaderInstance();
    	Set<ColorDetails> details=reader.getDetailsOfColor();
    	List<ColorDetails> list = new ArrayList<ColorDetails>(details);
    	return ok(toJson(list));
    }
    
    public Result getColorDetailsWithNames(){
    	CSVFileReader reader=CSVFileReader.getReaderInstance();
    	List<ColorDetailWithNames> details = reader.getDetialsOfColorWithNames();
    	return ok(toJson(details));
    }
    
    public Result getRecords(){
    	List<Record> recordList = new Model.Finder(String.class,Record.class).all();
    	return ok(toJson(recordList));
    }
    
    public Result getVenueNames(){

    	final Set<Map.Entry<String,String[]>> entries = request().queryString().entrySet();
    	String city =null;
    	String state=null;
    	for (Map.Entry<String,String[]> entry : entries) {
    		String key = entry.getKey();
    		if(key.equalsIgnoreCase("city")){
    			city=Arrays.toString(entry.getValue());
    		}else if(key.equalsIgnoreCase("state")){
    			state=Arrays.toString(entry.getValue());
    		}
    	}
    	CSVFileReader reader=CSVFileReader.getReaderInstance();
    	VenueNames venueNames = reader.getVenueNames(city, state);
		return ok(toJson(venueNames));
    }

	
}
