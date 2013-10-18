package de.uni_potsdam.hpi.metanome.algorithm_loading;

import java.io.File;
import java.io.FilenameFilter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedList;

/**
 * 
 */
public class InputDataFinder {

	public String[] getAvailableCsvs() throws UnsupportedEncodingException {
		LinkedList<String> availableCsvs = new LinkedList<String>();
		String pathToFolder = ClassLoader.getSystemResource("inputData").getPath();
		File[] csvFiles = retrieveCsvFiles(pathToFolder);
		for (File csvFile : csvFiles){
			availableCsvs.add(csvFile.getName());
		}
		
		String[] stringArray = new String[availableCsvs.size()];
		return availableCsvs.toArray(stringArray);
	}

	/**
	 * retrieves all CSV Files located directly in the given directory
	 * TODO consider sharing code with algorithm finding methods
	 * 
	 * @param pathToFolder	path to the folder to be searched in 
	 * @return names of all CSV files located directly in the given directory (no subfolders)
	 * 
	 * @throws UnsupportedEncodingException 
	 */
	protected File[] retrieveCsvFiles(String pathToFolder) throws UnsupportedEncodingException {
		File folder = new File(URLDecoder.decode(pathToFolder, "utf-8"));
		File[] csvs = folder.listFiles(new FilenameFilter() {
		    @Override
		    public boolean accept(File file, String name) {
		        return name.endsWith(".csv");
		    }
		});
		
		if (csvs == null) 
			csvs = new File[0];
		return csvs;
	}

}
