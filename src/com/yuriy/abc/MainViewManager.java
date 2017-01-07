package com.yuriy.abc;

import javax.swing.JOptionPane;

public class MainViewManager {

	private MainWindow mainWindow;
	
	MainViewManager(MainWindow mainWindow)
	{
		this.mainWindow = mainWindow;
	}
	
	public void showException(String exceptionMessage)
	{
		JOptionPane.showMessageDialog(null, exceptionMessage, "Exception", JOptionPane.ERROR_MESSAGE);
	}
	
	public void addMainWindowListener(MainWindow.Listener listener)
	{
		mainWindow.addListener(listener);
	}
	
	public MainWindow getMainWindow()
	{
		return mainWindow;
	}
}
