package com.udav.findduplicate;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.imgscalr.Scalr;

public class ImgPanel extends JPanel {
	private JLabel imgLabel;
	private JLabel pathLabel;
	
	public ImgPanel(final File file) {
		this.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
    	this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setVisible(true);
    	
		BufferedImage image = null;
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		image = Scalr.resize(image, 150, Scalr.OP_ANTIALIAS);
		
		imgLabel = new JLabel(new ImageIcon(image));
		pathLabel = new JLabel(file.getName());
		
		this.add(imgLabel);
		this.add(pathLabel);
		
		addMouseListener(new MouseListener() {
		
		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			new ImgFrame(file);
			System.out.println(file.getName());
			
		}
	});
	}

	
}
