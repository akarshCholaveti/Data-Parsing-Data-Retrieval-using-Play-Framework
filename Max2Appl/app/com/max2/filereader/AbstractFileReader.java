package com.max2.filereader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public abstract class AbstractFileReader {
	
	public InputStream loadFile(File file) throws FileNotFoundException{
		InputStream inputStream= new FileInputStream(file);
		return inputStream;
	}
	
	abstract public List<Object> parseFile(InputStream inputStream);
}
