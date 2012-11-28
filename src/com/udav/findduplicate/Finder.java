package com.udav.findduplicate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingWorker;

public class Finder extends SwingWorker<Integer, Object>{
	
	private ArrayList<File> itemArray = new ArrayList<File>();
	private ArrayList<File> directoryArray = new ArrayList<File>();
	private ArrayList<ArrayList<File>> resultArray = new ArrayList<ArrayList<File>>();	
	private boolean notFirst = false;
	private String pathToFolder;
	private String extensions[];

	public Finder(String pathToFolder, String extension) {
		this.pathToFolder = pathToFolder;
		this.extensions = extension.split(";");
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
				if (extensions[0].equals("*")) {
					// collect all files
					itemArray.add(fileList[i]);
				} else {
					// collect files where extension match
					String splitFileName[] = fileList[i].getName().split("\\.");
					String fileExtension = ""; 
					if (splitFileName.length > 1) fileExtension = splitFileName[1];
					for (int j=0; j<extensions.length; j++) {
						if (fileExtension.equalsIgnoreCase(extensions[j])) {
							itemArray.add(fileList[i]);
						}
					}
				} 
			}
			if (fileList[i].isDirectory() && !fileList[i].isHidden() && fileList[i].canExecute() && fileList[i].canRead()) {
				tmp.add(fileList[i].getAbsolutePath());
				//collect directories
				directoryArray.add(fileList[i]);
			}
		}
		for (int i=0; i<tmp.size(); i++) {
			find(tmp.get(i));
		}
	}
	
	
	/**
	 * Calculate size of directory
	 * 
	 * @param absolutePath - path to current directory
	 * @return result - resulting size of the directory
	 */
	private long getDirectorySize(String absolutePath) {
		long result = 0;
		
		File currentFilesList[] = new File(absolutePath).listFiles();
		for (int i=0; i<currentFilesList.length; i++) {
			if (currentFilesList[i].isFile()) result += currentFilesList[i].length();
			if (currentFilesList[i].isDirectory()) result += getDirectorySize(currentFilesList[i].getAbsolutePath()); 
		}
		return result;
	}
	/**
	 * Compare size and content directory
	 */
	private void compareDirectories() {
		ArrayList<Long> sizes = new ArrayList<Long>();
		for (int i=0; i<directoryArray.size(); i++) {
			sizes.add(getDirectorySize(directoryArray.get(i).getAbsolutePath()));
		}
		
		for (int i=0; i<directoryArray.size(); i++) {
			ArrayList<File> duplicateDirectory = new ArrayList<File>();
			for (int j=0; j<directoryArray.size(); j++) {
				if ( (i!=j) && (sizes.get(i).equals(sizes.get(j))) ) {
					if (!notFirst) {
						System.out.println(directoryArray.get(i).getAbsolutePath()+" "+sizes.get(i));
						System.out.println(directoryArray.get(j).getAbsolutePath()+" "+sizes.get(j));
						duplicateDirectory.add(directoryArray.get(i));
						duplicateDirectory.add(directoryArray.get(j));
						directoryArray.remove(j); sizes.remove(j); j--;
						notFirst = true;
					} else {
						System.out.println(directoryArray.get(j).getAbsolutePath()+" "+sizes.get(j));
						duplicateDirectory.add(directoryArray.get(j));
						directoryArray.remove(j); sizes.remove(j); j--;
					}
				}
			}
			notFirst = false;
			System.out.println("------------------------------------------");
			if (duplicateDirectory.size() > 0) resultArray.add(duplicateDirectory);
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
						itemArray.remove(j); j--;
						notFirst = true;
					} else {
						duplicateFileArray.add(itemArray.get(j));
						itemArray.remove(j); j--;
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


	@Override
	protected Integer doInBackground() throws Exception {
		String tmp[] = pathToFolder.split(";");
		
		publish("State 1 of 2: Collect files!");
		for (int i=0; i<tmp.length; i++)
			find(tmp[i]);
		
		publish("State 2 of 2: Find duplicate files!");
		compareDirectories();
		//compareFiles();	
		// Output duplicates
		/*		for (int i=0; i<resultArray.size(); i++) {
					for (int j=0; j<resultArray.get(i).size(); j++) {
						System.out.println(resultArray.get(i).get(j).getAbsolutePath());
					}
					System.out.println("-------------------------------");
				}
		*/
		
		return 1;
	}
	
}
