package com.udav.findduplicate;

import java.io.File;
import java.util.ArrayList;

import javax.swing.SwingWorker;

public class Finder extends SwingWorker<Integer, Object>{
	
	private ArrayList<File> fileArray = new ArrayList<File>();
	private ArrayList<File> directoryArray = new ArrayList<File>();
	private ArrayList<ArrayList<File>> resultArray = new ArrayList<ArrayList<File>>();
	private ArrayList<DirectoriesDuplicateContainer> resultDirectoriesArray = new ArrayList<DirectoriesDuplicateContainer>();
	private boolean notFirst = false;
	private String pathList[];
	private String extensions[];

	public Finder(String pathToFolder, String extension) {
		pathList = pathToFolder.split(";");
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
					fileArray.add(fileList[i]);
				} else {
					// collect files where extension match
					String splitFileName[] = fileList[i].getName().split("\\.");
					String fileExtension = ""; 
					if (splitFileName.length > 1) fileExtension = splitFileName[1];
					for (int j=0; j<extensions.length; j++) {
						if (fileExtension.equalsIgnoreCase(extensions[j])) {
							fileArray.add(fileList[i]);
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
	 * Compare filenames into two directories
	 * @param firstDir
	 * @param secondDir
	 * @return
	 */
	private boolean compateDirectoriesContent(File firstDir, File secondDir){
		String firstList[] = firstDir.list();
		String secondList[] = secondDir.list();
		if (firstList.length != secondList.length) return false;
		for (int i=0; i<firstList.length; i++) {
			if (!firstList[i].equals(secondList[i])) return false;
		}
		return true;
	}
	/**
	 * Compare path to current in isAllredyAdded 
	 * with all path in array pathList
	 * @param path
	 * @return
	 */
	private boolean comparePath(String path) {
		for (int i=0; i<pathList.length; i++) {
			if (path.equals(pathList[i])) return true;
		}
		return false;
	}
	
	/**
	 * Added or not file or directory
	 * @param current - file or directory
	 * @return true - file or directories added
	 * 			false - file or directories not added
	 */
	private boolean isAlreadyAdded(File current) {
		for (int i=0; i<resultDirectoriesArray.size(); i++){
			ArrayList<File> dirDupArray = resultDirectoriesArray.get(i).getDuplicateArray();
			for (int j=0; j<dirDupArray.size(); j++) {
				File tmp = current;
				while (!comparePath(current.getAbsolutePath())) {
					if (current.getAbsolutePath().equals(dirDupArray.get(j).getAbsolutePath())){
						return true;
					}
					current = current.getParentFile();
				}
				current = tmp;
			}
		}
		return false;
	}
	
	/**
	 * Compare size and content directory
	 */
	private void compareDirectories() {
		long start = System.currentTimeMillis();
		// create array and fill him directories size
		ArrayList<Long> sizes = new ArrayList<Long>();
		for (int i=0; i<directoryArray.size(); i++) {
			sizes.add(getDirectorySize(directoryArray.get(i).getAbsolutePath()));
		}
		
		for (int i=0; i<directoryArray.size(); i++) {
			ArrayList<File> duplicateDirectory = new ArrayList<File>();
			for (int j=(i+1); j<directoryArray.size(); j++) {
				if ( 
						/*(i!=j) 
						&&*/ (sizes.get(i).equals(sizes.get(j)))
						&& (sizes.get(i) > 0)
						&& (compateDirectoriesContent(directoryArray.get(i), directoryArray.get(j)))
						&& (!isAlreadyAdded(directoryArray.get(i)))
						) {
					if (!notFirst) {
						duplicateDirectory.add(directoryArray.get(i));
						duplicateDirectory.add(directoryArray.get(j));
						//directoryArray.remove(j); sizes.remove(j); j--;
						notFirst = true;
					} else {
						duplicateDirectory.add(directoryArray.get(j));
						//directoryArray.remove(j); sizes.remove(j); j--;
					}
				}
			}
			notFirst = false;
			if (duplicateDirectory.size() > 0){
				resultDirectoriesArray.add(new DirectoriesDuplicateContainer(duplicateDirectory, sizes.get(i)));
			}
		}
		System.out.println("1 "+(System.currentTimeMillis() - start));
	}
	
	/**
	 * Compare name and size all files
	 * If found duplicate add to resultArray
	 */
	private void compareFiles() {
		long start = System.currentTimeMillis();
		for (int i=0; i<fileArray.size(); i++) {
			ArrayList<File> duplicateFileArray = new ArrayList<File>();		
			for (int j=(i+1); j<fileArray.size(); j++) {
				if (
						/*(j!=i)
						&& */
						(fileArray.get(i).getName().equals(fileArray.get(j).getName()))
						&& (fileArray.get(i).length() == fileArray.get(j).length())
						&& (!isAlreadyAdded(fileArray.get(i)))
						) {
					if (!notFirst) {
						duplicateFileArray.add(fileArray.get(i));
						duplicateFileArray.add(fileArray.get(j));
						//fileArray.remove(j); j--;
						notFirst = true;
					} else {
						duplicateFileArray.add(fileArray.get(j));
						//fileArray.remove(j); j--;
					}
				}
			}
			notFirst = false;
			if (duplicateFileArray.size()>0)
				resultArray.add(duplicateFileArray);
		}
		System.out.println("2 "+(System.currentTimeMillis() - start));
	}
	
	public ArrayList<ArrayList<File>> getFileDuplicateArray() {
		return resultArray;
	}
	
	public ArrayList<DirectoriesDuplicateContainer> getDirectoriesDuplicateArray() {
		return resultDirectoriesArray;
	}


	@Override
	protected Integer doInBackground() throws Exception {
		publish("State 1 of 3: Collect files and directories!");
		for (int i=0; i<pathList.length; i++)
			find(pathList[i]);
		
		publish("State 2 of 3: Find duplicate directories!");
		compareDirectories();
		
		publish("State 3 of 3: Find duplicate files!");
		compareFiles();	
		
		// Output duplicates files
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
