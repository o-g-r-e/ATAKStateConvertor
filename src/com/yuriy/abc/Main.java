package com.yuriy.abc;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Main {
	
	public static void main(String[] args) throws FileNotFoundException, IOException
	{
		/*String srcFileName = null;
		String defaultFileName = "atak.xls";
		
		if(args.length == 0 || args[0].length() == 0)
		{
			File f = new File(defaultFileName);
			if(f.exists() && !f.isDirectory()) { 
				srcFileName = "atak.xls";
			}
		}
		else
		{
			srcFileName = args[0];
		}
		
		if(srcFileName != null)
			new ReportProcessor(srcFileName).doIt();*/
		
		new MainController(new ReportProcessor(), new MainViewManager(new MainWindow()));
	}
}