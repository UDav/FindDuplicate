package com.udav.findduplicate;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.imgscalr.Scalr;

public class ImgFrame extends JFrame{
	public ImgFrame(File file) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(file);
			image = Scalr.resize(image, 500, Scalr.OP_ANTIALIAS);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setBounds(100, 100, image.getWidth(), image.getHeight());
		JLabel imgLabel = new JLabel(new ImageIcon(image));
		this.add(imgLabel);
		imgLabel.setVisible(true);
		this.setVisible(true);
	}
}
