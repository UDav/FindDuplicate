package com.udav.findduplicate;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

public class FormManager {
	private static FormManager formManager;
	
	private MainFrame mainFrame;
	private ImgFrame imgFrame;
	
	private FormManager() {
		
	}
	
	public static FormManager getFormManager() {
		if (formManager == null)
			formManager = new FormManager();
		return formManager; 
	}
	
	public void createMainFrame() {
		mainFrame = new MainFrame(Main.eventObserverManager);
	}
	
	public void createImgFrame(final ArrayList<File> fileArray, final int pos) {
		//запускать в отдельном потоке
		imgFrame = new ImgFrame(fileArray, pos, Main.eventObserverManager);		
	}
}
