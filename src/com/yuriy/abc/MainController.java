package com.yuriy.abc;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Observable;

import javax.swing.JOptionPane;

public class MainController implements MainWindow.Listener {
	
	ReportProcessor reportProcessor;
	MainViewManager mainViewManager;
	
	MainController(ReportProcessor reportProcessor, MainViewManager mainViewManager)
	{
		this.reportProcessor = reportProcessor;
		this.mainViewManager = mainViewManager;
		this.mainViewManager.addMainWindowListener(this);
		this.reportProcessor.addObserver(this.mainViewManager.getMainWindow());
	}

	@Override
	public void onStartButtonClick() {
		new Thread(new Runnable() {
			public void run() {
			   try {
					reportProcessor.process();
				} catch (Exception e) {
					//e.printStackTrace();
					mainViewManager.showException(getStackTrace(e));
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