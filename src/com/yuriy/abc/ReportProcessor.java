package com.yuriy.abc;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.yuriy.abc.MainWindow.ViewCommand;

public class ReportProcessor extends Observable
{
	private String sourceFileName;
	private HSSFWorkbook workBook;
	private static final String OUTPUT_SHEET_NAME = "Постановки-Снятия";
	
	public void setSourceFileName(String fileName)
	{
		this.sourceFileName = fileName;
	}
	
	private static int getSecondNameIndex(String value)
	{
		int startIndex = 0;
		if(value.startsWith("Снятие с охраны"))
			startIndex = "Снятие с охраны".length();
		
		if(value.startsWith("Постановка на охрану"))
			startIndex = "Постановка на охрану".length();
		
		int index = -1;
		for (int i = startIndex; i < value.length(); i++) {
			if(Character.isUpperCase(value.charAt(i)))
			{
				index = i;
				break;
			}
		}
		
		return index;
	}
	
	private Row createRow(Sheet sheet, CellStyle style, String[] data, int rowIndex)
	{
		Row row = sheet.createRow(rowIndex);
		
		for (int i = 0; i < data.length; i++)
		{
			row.createCell(i);
			
			if(style != null)
				row.getCell(i).setCellStyle(style);
			
			if(data != null && data.length > 0 && i < data.length)
				row.getCell(i).setCellValue(data[i]);
		}
		
		return row;
	}
	
	private Row createRow(Sheet sheet, CellStyle style, int rowLength, int rowIndex)
	{
		Row row = sheet.createRow(rowIndex);
		
		for (int i = 0; i < rowLength; i++)
		{
			row.createCell(i);
			
			if(style != null)
				row.getCell(i).setCellStyle(style);
		}
		
		return row;
	}
	
	private void setRowData(Row row, String[] data)
	{
		if(row == null || row.getLastCellNum() <= 0 || data == null || data.length <= 0 || row.getLastCellNum() < data.length)
			return;
		
		for (int i = 0; i < data.length; i++) {
			row.getCell(i).setCellValue(data[i]);
		}
	}
	
	public void process() throws IOException
	{
		workBook = new HSSFWorkbook(new FileInputStream(sourceFileName));
		
		Sheet srcSheet = workBook.getSheetAt(0);
		
		Sheet destSheet = workBook.createSheet(OUTPUT_SHEET_NAME);
		
		String objName = "";
		int newRowsNumber = 1;
		DataFormatter formatter = new DataFormatter();
		
		CellStyle style = workBook.createCellStyle();
	    Font font = workBook.createFont();
	    font.setBoldweight(Font.BOLDWEIGHT_BOLD);
	    style.setFont(font);
	    
		createRow(destSheet, style, new String[]{"Название", "Дата", "Время", "Код", "Событие", "ШП", "Описание", "Субъект", "Канал"}, 0);
		
		for (int i = 0; i < srcSheet.getLastRowNum(); i++) {
			Row currentSrcRow = srcSheet.getRow(i);
			
			if(currentSrcRow == null)
				continue;
			
			if(currentSrcRow.getCell(0) != null && currentSrcRow.getCell(0).getStringCellValue().contains("Дата / Время"))
				continue;
			
			if(currentSrcRow.getCell(2) != null && currentSrcRow.getCell(2).getStringCellValue().contains("События за период"))
				continue;
			
			if(currentSrcRow.getCell(6) != null && currentSrcRow.getCell(6).getStringCellValue().contains("Исключение"))
				continue;
			
			Cell srcDateTimeCell = currentSrcRow.getCell(0);
			
			String srcDateTimeValue = formatter.formatCellValue(srcDateTimeCell);
			if(srcDateTimeValue.startsWith("Объект"))
			{
				objName = srcDateTimeValue.substring(srcDateTimeValue.indexOf("\""), /*srcDateTimeValue.indexOf("(")*/srcDateTimeValue.indexOf("\"")+11);
				continue;
			}
			else if(srcDateTimeValue.startsWith("Всего событий") || srcDateTimeValue.equals(""))
			{
				continue;
			}
			
			String srcDate = srcDateTimeValue;
			String srcTime = srcDate;
			if(srcDateTimeValue.length() > 9)
			{
				srcDate = srcDateTimeValue.substring(0, 9);
				srcTime = srcDateTimeValue.substring(9, srcDateTimeValue.length());
			}
			
			Cell srcCodeCell  = currentSrcRow.getCell(1);
			Cell srcEventCell = currentSrcRow.getCell(3);
			Cell srcHPCell    = currentSrcRow.getCell(5);
			Cell srcDescCell  = currentSrcRow.getCell(6);
			Cell srcChannelCell  = currentSrcRow.getCell(11);
			
			if(srcCodeCell == null || srcDescCell == null)
				continue;
			
			String srcCode  = srcCodeCell. getStringCellValue();
			String srcEvent = formatter.formatCellValue(srcEventCell);
			String srcHP    = formatter.formatCellValue(srcHPCell);
			String srcDesc  = srcDescCell. getStringCellValue();
			String srcChannel = srcChannelCell==null?"":srcChannelCell. getStringCellValue();
			
			if(srcChannel.equals("Gpr"))
				srcChannel = "Gprs7";
			
			if("".equals(srcCode) && "".equals(srcEvent) && "".equals(srcHP) && "".equals(srcDesc))
				continue;
			
			Row currentDestRow = createRow(destSheet, null, 9, newRowsNumber);
			newRowsNumber++;
			
			/*Cell objNameCell = currentDestRow.getCell(0);
			Cell dateCell    = currentDestRow.getCell(1);
			Cell timeCell    = currentDestRow.getCell(2);
			Cell codeCell    = currentDestRow.getCell(3);
			Cell eventCell   = currentDestRow.getCell(4);
			Cell hpCell      = currentDestRow.getCell(5);
			Cell descCell    = currentDestRow.getCell(6);
			Cell clientSecondNameCell = currentDestRow.getCell(7);
			Cell channelCell = currentDestRow.getCell(8);
			
			objNameCell.setCellValue(objName);
			dateCell.   setCellValue(srcDate);
			timeCell.   setCellValue(srcTime);
			codeCell.   setCellValue(srcCode);
			eventCell.  setCellValue(srcEvent);
			hpCell.     setCellValue(srcHP);*/
			
			String resultDesc = "";
			String resultClientSecondName = "";
			int secondNameIndex = getSecondNameIndex(srcDesc);
			if(secondNameIndex >= 0)
			{
				//descCell.setCellValue(srcDesc.substring(0, secondNameIndex));
				resultDesc = srcDesc.substring(0, secondNameIndex);
				//clientSecondNameCell.setCellValue(srcDesc.substring(secondNameIndex, srcDesc.length()));
				resultClientSecondName = srcDesc.substring(secondNameIndex, srcDesc.length());
			}
			else
			{
				//descCell.setCellValue(srcDesc);
				resultDesc = srcDesc;
			}
			//channelCell.setCellValue(srcChannel);
			
			setRowData(currentDestRow, new String[]{objName, srcDate, srcTime, srcCode, srcEvent, srcHP, resultDesc, resultClientSecondName, srcChannel});
			
			//System.out.print(writeProgress("\rProcess...", i, srcSheet.getLastRowNum()));
			updateInterface(new ViewCommand(0, writeProgress("Process...", i, srcSheet.getLastRowNum())));
		}
		
		//System.out.println("");
		
		for (int j = 0; j < destSheet.getRow(0).getLastCellNum(); j++) {
			destSheet.autoSizeColumn(j);
			//System.out.print(writeProgress("\rResize...", j, destSheet.getRow(0).getLastCellNum()-1));
			updateInterface(new ViewCommand(1, writeProgress("Resize...", j, destSheet.getRow(0).getLastCellNum()-1)));
		}
		updateInterface(new ViewCommand(2, "Complete!"));
		
		workBook.removeSheetAt(0);
		workBook.setSheetOrder(OUTPUT_SHEET_NAME, 0);
		
		FileOutputStream out = new FileOutputStream(sourceFileName);
		workBook.write(out);
		out.close();
		
		Desktop.getDesktop().open(new File(sourceFileName).getParentFile());
	}
	
	private String writeProgress(String word, int n, int max)
	{
		int p = Math.round(((float)n*100.0f)/(float)max);
		return word+p+"%";
	}
	
	private void updateInterface(ViewCommand viewCommand)
	{
		notifyObservers(viewCommand);
		setChanged();
	}
}