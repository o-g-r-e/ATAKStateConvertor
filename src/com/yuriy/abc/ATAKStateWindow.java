package com.yuriy.abc;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

public class ATAKStateWindow implements Observer
{	
	private File srcFile;
	private JLabel chooseFileLabel;
	private JTextField fileTextField;
	private JButton chooseFileButton;
	//private JProgressBar progressBar;
	private JTextArea progressTextArea;
	private JScrollPane progressScrollPane;
	private JButton startButton;
	private JFrame frame;
	private Listener listener;
	
	interface Listener
	{
		public void onStartButtonClick();
		public void onFlieLoaded(String fileName);
	}
	
	public void addListener(Listener listener)
	{
		this.listener = listener;
	}
	
	public ATAKStateWindow()
	{
		frame = new JFrame("Отчет по постановкам и снятиям АТАК");
		frame.setBounds(100,100,500,250);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container container = frame.getContentPane();
		
		container.setLayout(null);
		
		chooseFileLabel = new JLabel("Выберите файл:");
		chooseFileLabel.setBounds(101,25,150,30);
		
		container.add(chooseFileLabel);
		
		fileTextField = new JTextField(65535);
		fileTextField.setBounds(101,50,260,20);
		
		container.add(fileTextField);
		
		progressTextArea = new JTextArea(0,0);
		progressScrollPane = new JScrollPane(progressTextArea);
		progressScrollPane.setBounds(0,150,495,123);
		progressScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		progressScrollPane.setVisible(false);
		/*progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setMinimum(0);
		progressBar.setMaximum(100);
		progressBar.setValue(0);
		progressBar.setBounds(10,180,470,15);*/
		container.add(progressScrollPane);
		
		startButton = createButton("Начать", 160,100,150,25, false);
		startButton.setEnabled(false);
		
		startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(srcFile != null)
                {
                	startProcess();
                	listener.onStartButtonClick();
                }
            }
        });
		
		container.add(startButton);
		
		chooseFileButton = createButton("...", 360, 50, 19, 19, true);
		
		chooseFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileopen = new JFileChooser();
                fileopen.setFileFilter(new ExcelFilesFilter());
                int ret = fileopen.showDialog(null, "Открыть файл");                
                if (ret == JFileChooser.APPROVE_OPTION) {
                    fileLoaded(fileopen.getSelectedFile());
                    listener.onFlieLoaded(srcFile.getAbsolutePath());
                }
            }
        });
		
		container.add(chooseFileButton);
		
		frame.setVisible(true);
	}
	
	private void fileLoaded(File file)
	{
		srcFile = file;
		fileTextField.setText(srcFile.getAbsolutePath());
        startButton.setEnabled(true);
	}
	
	private void startProcess()
	{
		frame.setBounds(100,100,500,300);
    	fileTextField.setText("");
    	fileTextField.setEnabled(false);
    	progressScrollPane.setVisible(true);
    	srcFile = null;
    	startButton.setEnabled(false);
    	chooseFileButton.setEnabled(false);
	}
	
	private JButton createButton(String title, int x, int y, int width, int height, boolean enable)
	{
		JButton button = new JButton(title);
		button.setBounds(x, y, width, height);
		button.setEnabled(enable);
		
		return button;
	}

	@Override
	public void update(Observable arg0, Object message) {
		String sMessage = (String)message;
		
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	progressTextArea.setText(sMessage);
            }
        });
	}
}