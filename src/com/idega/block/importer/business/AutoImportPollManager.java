package com.idega.block.importer.business;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;

import org.sadun.util.polling.BasePollManager;
import org.sadun.util.polling.FileFoundEvent;
import org.sadun.util.polling.FileSetFoundEvent;

import com.idega.block.importer.data.ImportFile;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBOService;
import com.idega.idegaweb.IWMainApplication;

/**
 * AutoImportPollManager handles the action when file(s) are found in folders for automatic imports
 * One AutoImportPollManager is created for each folder to be polled from.
 * It basically calls the specified importer class with the files found in the folder.
 * 
 * Copyright:    Copyright (c) 2004
 * Company:      idega software
 * @author Joakim@idega.is
 */
public class AutoImportPollManager extends BasePollManager {
	private String fileClass;
	private ImportFileHandler handler;

	public AutoImportPollManager(String importerClass, String fc) throws IBOLookupException, ClassNotFoundException {
		fileClass = fc;
		handler = getImportFileHandler(importerClass);
	}
	
	/**
	 * Implemented interface (callback)
	 * @see org.sadun.util.polling.BasePollManager
	 */
	public void fileFound(FileFoundEvent evt){
		File file = evt.getFile();
		processFile(file);
		
	}
	
	/**
	 * Implemented interface (callback)
	 * @see org.sadun.util.polling.BasePollManager
	 */
	public void fileSetFound(FileSetFoundEvent evt){
		File[] files = evt.getFiles();
		for(int i=0;i<files.length;i++){
			processFile(files[i]);
		}
	}

	/**
	 * Calls the import and creates a report file if needed for the 
	 * @param filePath
	 */
	private void processFile(File filePath) {
		try {
			ImportFile file = (ImportFile) Class.forName(fileClass).newInstance();
			file.setFile(filePath);
			handler.setImportFile(file);

			handler.handleRecords();
			createReport(handler, filePath);
			filePath.delete();
		} catch (RemoteException e) {
			System.out.println("Warning Automatic import of " + filePath + " did not succeed");
			e.printStackTrace();
		} catch (NoRecordsException e) {
			System.out.println("Warning Automatic import of " + filePath + " did not succeed");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Warning Automatic import of " + filePath + " did not succeed");
			e.printStackTrace();
		}
	}

	/**
	 * Creates a report under ../Reports/ with the same name as the import file
	 * The report contains all the lines that did not execute OK
	 * @param handler
	 * @param path
	 */
	private void createReport(ImportFileHandler handler, File path) {
		try {
			List failedRecords = handler.getFailedRecords();
			if (failedRecords.size() > 0) {
				String pathString = path.toString();
				int folderPointer = pathString.lastIndexOf('/');
				folderPointer = Math.max(folderPointer, pathString.lastIndexOf('\\'));
				String fileName = pathString.substring(folderPointer+1);
				pathString = pathString.substring(0, folderPointer);
				folderPointer = pathString.lastIndexOf('/') + 1;
				folderPointer = Math.max(folderPointer, pathString.lastIndexOf('\\') + 1);
				
				System.out.println("folderPointer = " + folderPointer);
				String reportPathString = pathString
						.substring(0, folderPointer)
						+ "Reports/";
				System.out.println("reportPathString = " + reportPathString);
				String filePath = reportPathString + fileName;
				File reportPath = new File(reportPathString);
				if (!reportPath.exists()) {
					System.out.println("ReportPath not existing, trying to create");
					if(!reportPath.mkdir()){
						System.out.println("Could not create the report folder. No import reports can be created!");
						return;
					}
				}
				System.out.println("pathString = " + filePath);
				try {
					BufferedWriter out = new BufferedWriter(new FileWriter(filePath));

					Iterator iter = failedRecords.iterator();
					while (iter.hasNext()) {
						String line = (String) iter.next();
						out.write(line + '\n');
					}
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (IndexOutOfBoundsException e){
					e.printStackTrace();
				}
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
  	public ImportFileHandler getImportFileHandler(String handlerClass) throws ClassNotFoundException, IBOLookupException {
  		Class importHandlerInterfaceClass = Class.forName(handlerClass);
		ImportFileHandler handler = (ImportFileHandler) getServiceInstance(importHandlerInterfaceClass);
	    return handler;
  	}

    /**
     * Get an instance of the service bean specified by serviceClass
     */
	protected IBOService getServiceInstance(Class serviceClass)throws IBOLookupException{
		return IBOLookup.getServiceInstance(IWMainApplication.getDefaultIWMainApplication().getIWApplicationContext(), serviceClass);
    }
}
