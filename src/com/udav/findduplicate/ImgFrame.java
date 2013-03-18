package com.udav.findduplicate;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import org.imgscalr.Scalr;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;

public class ImgFrame extends JDialog implements ActionListener{
	private JButton buttonLast;
	private JButton buttonNext;
	public JButton buttonDelete;
	private JPanel imgPanel;
	private JLabel imgLabel = new JLabel(); 
	private ArrayList<File> fileArray;
	private int position;
	private EventObserverManager eventObserverManager;
	
	private void drawImg(int pos, int width, int height) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(fileArray.get(pos));
			image = Scalr.resize(image, width, height, Scalr.OP_ANTIALIAS);
		} catch (IOException e) {
			e.printStackTrace();
		}
		imgLabel.setIcon(new ImageIcon(image));
		
		this.setTitle(fileArray.get(position).getAbsolutePath());
		this.repaint();
	}
	
	public ImgFrame(ArrayList<File> fileArray, int pos, EventObserverManager eventObserverManager) {
		this.eventObserverManager = eventObserverManager;
		this.setModal(true);
		this.position = pos;
		this.setBounds(100, 100, 700, 600);
		this.fileArray = fileArray;
		
		imgPanel = new JPanel();
		imgPanel.add(imgLabel);
		
		imgPanel.addComponentListener(new ComponentListener() {
			@Override
			public void componentShown(ComponentEvent arg0) {}
			@Override
			public void componentResized(ComponentEvent arg0) {
				drawImg(position, imgPanel.getWidth(), imgPanel.getHeight());
			}
			@Override
			public void componentMoved(ComponentEvent arg0) {}
			@Override
			public void componentHidden(ComponentEvent arg0) {}
		});
		getContentPane().add(imgPanel, BorderLayout.NORTH);
		
		JPanel controlPanel = new JPanel();
		getContentPane().add(controlPanel, BorderLayout.SOUTH);
		
		buttonLast = new JButton("<<");
		controlPanel.add(buttonLast);
		buttonLast.addActionListener(this);
		
		buttonNext = new JButton(">>");
		controlPanel.add(buttonNext);
		buttonNext.addActionListener(this);
		
		buttonDelete = new JButton("del");
		controlPanel.add(buttonDelete);
		buttonDelete.addActionListener(this);
		
		imgLabel.setVisible(true);
		this.setVisible(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == buttonLast) {
			if (position > 0) drawImg(--position, imgPanel.getWidth(), imgPanel.getHeight());
		} else 
		if (e.getSource() == buttonNext){
			if (position < fileArray.size()-1){
				position++;
				drawImg(position, imgPanel.getWidth(), imgPanel.getHeight());
			}
		} else
		if (e.getSource() == buttonDelete) {
			eventObserverManager.notifyAll(ImgFrame.class, position);
		}
		
	}
}
