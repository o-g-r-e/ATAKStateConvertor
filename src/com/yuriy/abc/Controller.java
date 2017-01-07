package com.yuriy.abc;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Observable;

import javax.swing.JOptionPane;

public class Controller extends Observable implements ATAKStateWindow.Listener {
	
	ReportProcessor rp;
	
	Controller(ReportProcessor rp, ATAKStateWindow window)
	{
		this.rp = rp;
		window.addListener(this);
		this.rp.addObserver(window);
	}

	@Override
	public void onStartButtonClick() {
		new Thread(new Runnable() {
			public void run() {
			   try {
					rp.doIt();
				} catch (Exception e) {
					//e.printStackTrace();
					JOptionPane.showMessageDialog(null, getStackTrace(e), "Exception", JOptionPane.ERROR_MESSAGE);
				}
			}
		}).start();
	}

	@Override
	public void onFlieLoaded(String fileName) {
		rp.setSourceFileName(fileName);
	}
	
	private String getStackTrace(Throwable throwable) {
	     StringWriter sw = new StringWriter();
	     PrintWriter pw = new PrintWriter(sw, true);
	     throwable.printStackTrace(pw);
	     return sw.getBuffer().toString();
	}
}