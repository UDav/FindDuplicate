package com.udav.findduplicate;

import java.io.File;
import java.util.ArrayList;

public class Finder {
	private ArrayList<Item> itemArray = new ArrayList<Item>();

	public Finder() {
		find("/media/Hitachi/Рефераты");
		/*for (int i=0; i<itemArray.size(); i++) {
			System.out.println(itemArray.get(i).nameFile+" "+itemArray.get(i).sizeFile);
		 }
		 */
		compareFile();
			

		
	}
	
	
	//search all files and add to array 
	private void find(String path) {
		ArrayList<String> tmp = new ArrayList<String>();
		File fileList[] = new File(path).listFiles();
		for (int i=0; i<fileList.length; i++) {
			if (fileList[i].isFile()) itemArray.add(new Item(fileList[i].getName(), fileList[i].getAbsolutePath(), fileList[i].length()));
			if (fileList[i].isDirectory()) tmp.add(fileList[i].getAbsolutePath());
		}
		for (int i=0; i<tmp.size(); i++) {
			find(tmp.get(i));
		}
	}
	
	private void compareFile() {
		for (int i=0; i<itemArray.size(); i++) {
			for (int j=0; j<itemArray.size(); j++) {
				if ((itemArray.get(i).nameFile.equals(itemArray.get(j).nameFile)) 
						&& (itemArray.get(i).sizeFile == itemArray.get(j).sizeFile) && (j!=i)) {
					System.out.println(itemArray.get(i).nameFile+" "+itemArray.get(i).pathToFile+"---"+itemArray.get(j).pathToFile);
				}
			}
		}
	}
	
}
