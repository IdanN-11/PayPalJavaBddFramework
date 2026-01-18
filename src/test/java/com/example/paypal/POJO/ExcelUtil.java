package com.example.paypal.POJO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Files;

public class ExcelUtil {
	
	public PayloadBase ReadPayloadFromFile(String path) {
	try {
	File file = new File(path);
	FileInputStream fis=new FileInputStream(file);
	
	ObjectMapper mapper = new ObjectMapper();
	PayloadBase payload = mapper.readValue(fis, PayloadBase.class);
	System.out.println(payload);
	return payload;
	}catch(Exception e) {
		System.out.println(e);
		
		return null;
		
	}
	
	}
	public static void main(String args[]) {
		ExcelUtil vv=new ExcelUtil();
		String path = System.getProperty("user.dir")+"\\src\\test\\resources\\paypal_multi_order_with_items.xlsx";
		System.out.println(path);
		
		HashMap<String,HashMap<String,String>>  payload =vv.readOrdersData(path);
		System.out.println(payload);
		List<HashMap<String,String>>  payload1 =vv.readItemData(path);
		System.out.println(payload1);
	}
	
	
	
	
	
	
	public HashMap<String,HashMap<String,String>> readOrdersData(String path) {
		
		File file = new File(path);
		HashMap<String,HashMap<String,String>> mainMap = new HashMap<>();
		try {
			FileInputStream fis =new FileInputStream(file);
			Workbook wb = new XSSFWorkbook(fis);
			
			Sheet orders= wb.getSheet("Orders");
			//header Row
			Row headerRow = orders.getRow(0);
			//Row Count
			Integer rows = orders.getLastRowNum();
			//Column Count
			Integer columns = (int) headerRow.getLastCellNum();
			for(int i=0;i<=rows;i++) {
						
				Row row = orders.getRow(i);
				String keyMain= row.getCell(0).toString();
				HashMap<String,String> map = new HashMap<>();	
				for(int j=1;j<columns;j++) {
					Cell cell=row.getCell(j,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
					String key= headerRow.getCell(j,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString();
					String val = row.getCell(j,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString();
					
					map.put(key, val);
				}
				mainMap.put(keyMain, map);
				
			}
			
			
			
			
			
			
			return mainMap;
			
			
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		
			
			
		
	}
	public List<HashMap<String,String>> readItemData(String path) {
		
		File file = new File(path);
		List<HashMap<String,String>> list= new ArrayList<> ();
		try {
			FileInputStream fis =new FileInputStream(file);
			Workbook wb = new XSSFWorkbook(fis);
			
			Sheet orders= wb.getSheet("Items");
			//header Row
			Row headerRow = orders.getRow(0);
			//Row Count
			Integer rows = orders.getLastRowNum();
			//Column Count
			Integer columns = (int) headerRow.getLastCellNum();
			for(int i=1;i<=rows;i++) {
						
				Row row = orders.getRow(i);
				String keyMain= row.getCell(0).toString();
				HashMap<String,String> map = new HashMap<>();	
				for(int j=0;j<columns;j++) {
					Cell cell=row.getCell(j,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
					String key= headerRow.getCell(j,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString();
					String val = row.getCell(j,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString();
					
					map.put(key, val);
				}
				list.add( map);
				
			}
			
			
			
			
			
			
			return list;
			
			
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
