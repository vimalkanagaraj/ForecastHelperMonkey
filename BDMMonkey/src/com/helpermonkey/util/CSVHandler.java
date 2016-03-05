package com.helpermonkey.util;

import java.io.FileReader;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

/**
 * @author Vimal (Vimalasekar Rajendran; 131807)
 *
 */
public class CSVHandler {

	public CSVHandler() throws Exception {
	}
	
//	public Iterable<CSVRecord> readFromRevenueFile() throws Exception{
//		return readFromCSV(MonkeyConstants.REVENUE_CSV_FILE);
//	}
//	
//	public Iterable<CSVRecord> readFromResourceFile() throws Exception{
//		return readFromCSV(MonkeyConstants.RESOURCE_CSV_FILE);
//	}
//
//	public Iterable<CSVRecord> readFromCalculatedDataFile() throws Exception{
//		return readFromCSV(MonkeyConstants.CALCULATED_CSV_FILE);
//	}
	
	public Iterable<CSVRecord> readFromCSV(String fileNameWithPath) throws Exception{

		Reader in = new FileReader(fileNameWithPath);
		
		Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().parse(in);
		
		return records;
	}
	
}
