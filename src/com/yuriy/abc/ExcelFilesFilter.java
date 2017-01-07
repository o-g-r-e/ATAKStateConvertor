package com.yuriy.abc;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class ExcelFilesFilter extends FileFilter {

	@Override
	public boolean accept(File file) {
		
		return file.getAbsolutePath().endsWith(".xls") || file.getAbsolutePath().endsWith(".xlsx") || file.isDirectory();
	}

	@Override
	public String getDescription() {
		
		return "Excel documents (*.xls, *.xlsx)";
	}

}
