package com.yuriy.abc;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ViewProcessor implements MainWindow.WindowEventsListener {
	
	ReportProcessor reportProcessor;
	MainWindow mainWindow;
	
	ViewProcessor(ReportProcessor reportProcessor, MainWindow mainWindow)
	{
		this.reportProcessor = reportProcessor;
		this.mainWindow = mainWindow;
		this.mainWindow.addListener(this);
		this.reportProcessor.addObserver(this.mainWindow);
	}

	@Override
	public void onStartButtonClick() {
		new Thread(new Runnable() {
			public void run() {
			   try {
				   reportProcessor.process();
			   } catch (Exception e) {
				   //e.printStackTrace();
				   mainWindow.showException(getStackTrace(e));
			   } finally
			   {
					System.exit(0);
			   }
			}
		}).start();
	}

	@Override
	public void onFlieLoaded(String fileName) {
		reportProcessor.setSourceFileName(fileName);
	}
	
	private String getStackTrace(Throwable throwable) {
	     StringWriter sw = new StringWriter();
	     PrintWriter pw = new PrintWriter(sw, true);
	     throwable.printStackTrace(pw);
	     return sw.getBuffer().toString();
	}
}