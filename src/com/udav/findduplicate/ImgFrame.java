package com.udav.findduplicate;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.imgscalr.Scalr;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;

public class ImgFrame extends JFrame implements ActionListener{
	private JButton buttonLast;
	private JButton buttonNext;
	private JButton buttonDelete;
	private JPanel imgPanel;
	private JLabel imgLabel = new JLabel(); 
	private ArrayList<File> fileArray;
	private int position;
	
	private void drawImg(int pos) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(fileArray.get(pos));
			image = Scalr.resize(image, 500, Scalr.OP_ANTIALIAS);
		} catch (IOException e) {
			e.printStackTrace();
		}
		imgLabel.setIcon(new ImageIcon(image));
		
		this.setTitle(fileArray.get(position).getAbsolutePath());
		imgLabel.repaint();
		imgPanel.repaint();
		imgPanel.revalidate();
		this.repaint();
	}
	
	public ImgFrame(ArrayList<File> fileArray, int position) {
		this.position = position;
		this.setBounds(100, 100, 700, 600);
		this.fileArray = fileArray;
		
		imgPanel = new JPanel();
		imgPanel.add(imgLabel);
		drawImg(position);
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
			if (position > 0) drawImg(--position);
		} else 
		if (e.getSource() == buttonNext){
			if (position < fileArray.size()-1){
				position++;
				drawImg(position);
			}
		} else
		if (e.getSource() == buttonDelete) {
			
		}
		
	}
}
