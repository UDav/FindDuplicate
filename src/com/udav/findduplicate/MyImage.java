package com.udav.findduplicate;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.*;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Mode;

public class MyImage {
	private final int SIZE = 20;
	private final int TRESHOLD = 60;
	private int data[][];
	private String path;

	/**
	 * Create item of class and load picture
	 * @param path
	 */
	public MyImage(String path) {
		this.path = path;
		load();
	}	
	
	/**
	 * Load image
	 */
	public void load(){
		try {
			BufferedImage img = ImageIO.read(new File(path));
			if (img != null)
				img = Scalr.resize(img, Mode.FIT_EXACT, 20, 20, Scalr.OP_ANTIALIAS);
			else System.out.println("Can't open " + path);
			
			data = new int[img.getWidth()][img.getHeight()];
			
			for (int i=0; i<img.getWidth(); i++){
				for (int j=0; j<img.getHeight(); j++){
					Color c = new Color(img.getRGB(i, j));
					data[i][j] = c.getBlue()+c.getGreen()+c.getRed();
				}
			}
			
			//ImageIO.write(img,"jpg", new File("/home/udav/pict/resize.jpg"));		
			img.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Compare two picture
	 * @param otherData
	 * @return distance 
	 */
	public int compare(int otherData[][]){
		int distance=0;
		for (int i=0; i<SIZE; i++){
			for (int j=0; j<SIZE; j++) {
				if (Math.abs(data[i][j]-otherData[i][j]) > TRESHOLD) {
					distance++;
				}
			}
		}
		return distance;
	}
	
	public int[][] getData() {
		return data;
	}
	
}
