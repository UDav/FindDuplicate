package com.udav.findduplicate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.swing.SwingWorker;

public class Finder extends SwingWorker<Integer, Object>{
	
	private ArrayList<File> fileArray = new ArrayList<File>();
	private ArrayList<File> directoryArray = new ArrayList<File>();
	private ArrayList<ArrayList<File>> resultFilesArray = new ArrayList<ArrayList<File>>();
	private ArrayList<DirectoriesDuplicateContainer> resultDirectoriesArray = new ArrayList<DirectoriesDuplicateContainer>();
	private boolean notFirst = false;
	private String extensions[];
	private File pathArray[];

	public Finder(File pathArray[], String extension) {
		this.pathArray = pathArray;
		this.extensions = extension.split(";");
	}
	
	// global progress
	private int progress = 0;
	/**
	 * 
	 * @param currenPos - current position in array
	 * @param arraySize - size array
	 * @param note - description operation
	 */
	private void calculataAndPublishProgress(int currenPos, int arraySize, String note) {
		int currentProgress = (int)(((float)(currenPos+1)/(float)arraySize)*100);
		if (currentProgress > progress){
			progress = currentProgress;
			publish(note);
			setProgress(progress);
		}
	}
		
	/**
	 * Search all files and add to array 
	 * @param f - selected directory
	 */
	private void find(File f) {
		ArrayList<File> tmp = new ArrayList<File>();
		File fileList[] = f.listFiles();
		for (int i=0; i<fileList.length; i++) {
			if ( (fileList[i].isFile()) && (fileList[i].canRead()) && (!fileList[i].isHidden()) ) {
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
			if ( (fileList[i].isDirectory()) && (!fileList[i].isHidden()) && (fileList[i].canExecute()) && (fileList[i].canRead()) ) {
				tmp.add(fileList[i]);
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
		for (int i=0; i<pathArray.length; i++) {
			if (path.equals(pathArray[i].getAbsolutePath())) return true;
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
		progress = 0;
		long start = System.currentTimeMillis();
		// create array and fill him directories size
		ArrayList<Long> sizes = new ArrayList<Long>();
		for (int i=0; i<directoryArray.size(); i++) {
			calculataAndPublishProgress(i, directoryArray.size(), "State 2 of 4: Calculate directories size! ");
			sizes.add(getDirectorySize(directoryArray.get(i).getAbsolutePath()));
		}
		progress = 0;
		for (int i=0; i<directoryArray.size(); i++) {
			calculataAndPublishProgress(i, directoryArray.size(), "State 3 of 4: Find duplicate directories! ");
			ArrayList<File> duplicateDirectory = new ArrayList<File>();
			for (int j=(i+1); j<directoryArray.size(); j++) {
				if ( 
						(sizes.get(i).equals(sizes.get(j)))
						&& (sizes.get(i) > 0)
						&& (compateDirectoriesContent(directoryArray.get(i), directoryArray.get(j)))
						&& (!isAlreadyAdded(directoryArray.get(i)))
						) {
					if (!notFirst) {
						duplicateDirectory.add(directoryArray.get(i));
						duplicateDirectory.add(directoryArray.get(j));
						directoryArray.remove(j); j--;
						notFirst = true;
					} else {
						duplicateDirectory.add(directoryArray.get(j));
						directoryArray.remove(j); j--;
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
	 * Compare content two file
	 * @param first
	 * @param second
	 * @return true - if files equals; false - if file not equals
	 */
	private boolean compareContentFile(File first, File second) {
		int size = 1024;
		if (first.length() < size) size = (int)first.length(); 
		byte firstArray[] = new byte[size];
		byte secondArray[] = new byte[size];

		try{
			InputStream isFirst = new FileInputStream(first);
			InputStream isSecond = new FileInputStream(second);
			while ((isFirst.read(firstArray)) > 0 
					&& (isSecond.read(secondArray) > 0)){
				//if (firstArray.length == secondArray.length)
					for (int i=0; i<firstArray.length; i++) {
						if (firstArray[i] != secondArray[i]) return false;
					}
				//else return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Compare name and size all files
	 * If found duplicate add to resultArray
	 */
	private void compareFiles() {
		progress = 0;
		//calculataAndPublishProgress(0, 0, "State 4 of 4: Find duplicate files! ");
		long start = System.currentTimeMillis();
		for (int i=0; i<fileArray.size(); i++) {
			calculataAndPublishProgress(i, fileArray.size(), "State 4 of 4: Find duplicate files! ");
			ArrayList<File> duplicateFileArray = new ArrayList<File>();		
			for (int j=(i+1); j<fileArray.size(); j++) {
				if (
						(fileArray.get(i).getName().equals(fileArray.get(j).getName()))
						&& (fileArray.get(i).length() == fileArray.get(j).length())
						&& (!isAlreadyAdded(fileArray.get(i)))
						&& (compareContentFile(fileArray.get(i), fileArray.get(j)))
						) {
					if (!notFirst) {
						duplicateFileArray.add(fileArray.get(i));
						duplicateFileArray.add(fileArray.get(j));
						fileArray.remove(j); j--;
						notFirst = true;
					} else {
						duplicateFileArray.add(fileArray.get(j));
						fileArray.remove(j); j--;
					}
				}
			}
			notFirst = false;
			if (duplicateFileArray.size()>0)
				resultFilesArray.add(duplicateFileArray);
			
		}
		System.out.println("2 "+(System.currentTimeMillis() - start));
	}
	
	public ArrayList<ArrayList<File>> getFileDuplicateArray() {
		return resultFilesArray;
	}
	
	public ArrayList<DirectoriesDuplicateContainer> getDirectoriesDuplicateArray() {
		return resultDirectoriesArray;
	}


	public String getStatistic() {
		String result = "";
		
		int filesCounter = 0;
		for (int i=0; i<resultFilesArray.size(); i++) {
			filesCounter += resultFilesArray.get(i).size()-1;
		}
		result += "Дубликатов фалов: "+filesCounter;
		
		int directoriesCounter = 0;
		for (int i=0; i<resultDirectoriesArray.size(); i++) {
			directoriesCounter += resultDirectoriesArray.get(i).getDuplicateArray().size()-1;
		}
		result += "\r\nДубликатов директорий: "+directoriesCounter;
		return result;
	}
	
	@Override
	protected Integer doInBackground() throws Exception {
		publish("State 1 of 4: Collect files and directories! ");

		for (int i=0; i<pathArray.length; i++){
			find(pathArray[i]);
		}

		compareDirectories();

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
