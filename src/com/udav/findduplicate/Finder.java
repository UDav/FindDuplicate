package com.udav.findduplicate;

import java.io.File;
import java.util.ArrayList;

public class Finder {
	private ArrayList<File> itemArray = new ArrayList<File>();
	private ArrayList<ArrayList<File>> resultArray = new ArrayList<ArrayList<File>>();	
	private boolean notFirst = false;

	public Finder(String pathToFolder, String extension) {
		find(pathToFolder);
		compareFiles();	
		
		// Output duplicates
		/*for (int i=0; i<resultArray.size(); i++) {
			for (int j=0; j<resultArray.get(i).size(); j++) {
				System.out.println(resultArray.get(i).get(j).getAbsolutePath());
			}
			System.out.println("-------------------------------");
		}*/
		
	}
	
	
	/**
	 * Search all files and add to array 
	 * @param path - selected directory
	 */
	private void find(String path) {
		ArrayList<String> tmp = new ArrayList<String>();
		File fileList[] = new File(path).listFiles();
		for (int i=0; i<fileList.length; i++) {
			if (fileList[i].isFile()) {
				itemArray.add(fileList[i]);
			}
			if (fileList[i].isDirectory() && !fileList[i].isHidden() && fileList[i].canExecute() && fileList[i].canRead()) 
				tmp.add(fileList[i].getAbsolutePath());
		}
		for (int i=0; i<tmp.size(); i++) {
			find(tmp.get(i));
		}
	}
	
	/**
	 * Compare name and size all files
	 * If found duplicate add to resultArray
	 */
	private void compareFiles() {
		for (int i=0; i<itemArray.size(); i++) {
			ArrayList<File> duplicateFileArray = new ArrayList<File>();
			
			for (int j=0; j<itemArray.size(); j++) {
				if ((itemArray.get(i).getName().equals(itemArray.get(j).getName()) 
						&& (itemArray.get(i).length() == itemArray.get(j).length()) 
						&& (j!=i))) {
					if (!notFirst) {
						duplicateFileArray.add(itemArray.get(i));
						duplicateFileArray.add(itemArray.get(j));
						itemArray.remove(j);
						notFirst = true;
					} else {
						duplicateFileArray.add(itemArray.get(j));
						itemArray.remove(j);
					}
				}
			}
			notFirst = false;
			if (duplicateFileArray.size()>0)
				resultArray.add(duplicateFileArray);
		}
	}
	
	public ArrayList<ArrayList<File>> getFileDuplicateArray() {
		return resultArray;
	}
	
}
