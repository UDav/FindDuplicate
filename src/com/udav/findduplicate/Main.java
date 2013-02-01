package com.udav.findduplicate;

import javax.swing.JFrame;

public class Main {
	public static EventObserverManager eventObserverManager;
	/**
	 * @param args
	 */
	public static void main(String[] args) {		
		//MainFrame mf = new MainFrame();
		eventObserverManager = new EventObserverManagerImpl();
		new MainFrame(Main.eventObserverManager);
		//new Finder();

	}

}
