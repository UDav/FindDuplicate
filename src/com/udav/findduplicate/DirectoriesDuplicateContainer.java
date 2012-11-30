package com.udav.findduplicate;

import java.io.File;
import java.util.ArrayList;

public class DirectoriesDuplicateContainer {
	private ArrayList<File> arrayDup = new ArrayList<File>();
	private long size;
	
	public DirectoriesDuplicateContainer(ArrayList<File> arrayDup, long size) {
		this.arrayDup = arrayDup;
		this.size = size;
	}
	
	public long getSize() {
		return size;
	}
	
	public ArrayList<File> getDuplicateArray() {
		return arrayDup;
	}
}
