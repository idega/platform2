package com.idega.block.importer;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.ejb.FinderException;

import org.sadun.util.polling.DirectoryPoller;

import com.idega.block.importer.business.AutoImportPollManager;
import com.idega.block.importer.data.ImportHandler;
import com.idega.block.importer.data.ImportHandlerHome;
import com.idega.business.IBOLookupException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;


/**
 * Activats pollers for automatic imports configured by the user (AutoImporter).
 * Copyright:    Copyright (c) 2004
 * Company:      idega software
 * @author Joakim@idega.is
 * @see com.idega.block.importer.presentation.AutoImporter
 */
public class IWBundleStarter implements IWBundleStartable {

	private static HashMap pollers = new HashMap();
	private static DirectoryPoller poller;
	/**
	 * Starts all the pollers for automatic imports
	 * @see com.idega.idegaweb.IWBundleStartable#start(com.idega.idegaweb.IWBundle)
	 */
	public void start(IWBundle starterBundle) {
		System.out.println("Activating pollers for automatic imports");
		try {
			Collection coll = ((ImportHandlerHome)IDOLookup.getHome(ImportHandler.class)).findAllAutomaticUpdates();
			Iterator iter = coll.iterator();
			while(iter.hasNext()){
				ImportHandler importHandler = (ImportHandler)iter.next();
				addPoller(importHandler);
			}
		} catch (IDOLookupException e) {
			System.out.println("WARNING: Could not start the pollers for automatic imports");
			e.printStackTrace();
		} catch (FinderException e) {
			System.out.println("WARNING: Could not start the pollers for automatic imports");
			e.printStackTrace();
		} catch (IBOLookupException e) {
			System.out.println("WARNING: Could not start the pollers for automatic imports");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("WARNING: Could not start the pollers for automatic imports");
			e.printStackTrace();
		}
	}

	/**
	 * Stops all the pollers for automatic imports
	 * @see com.idega.idegaweb.IWBundleStartable#stop(com.idega.idegaweb.IWBundle)
	 */
	public void stop(IWBundle starterBundle) {
		System.out.println("Shutting down pollers");
		Iterator iter = pollers.values().iterator();
		while(iter.hasNext()){
			DirectoryPoller poller = (DirectoryPoller)iter.next();
			poller.shutdown();
		}
	}
	
	/**
	 * Helper function to stop a specific poller
	 * @param handlerClassName 
	 */
	public static void shutdown(String handlerClassName){
		System.out.println("Shutting down poller:"+handlerClassName);
		DirectoryPoller poller = (DirectoryPoller)pollers.get(handlerClassName);
		if(null!=poller){
			poller.shutdown();
			pollers.remove(handlerClassName);
		} else {
			System.out.println("WARNING: Could not find the specified poller");
		}
	}

	/**
	 * Adds a new poller
	 * @param importHandler
	 * @throws ClassNotFoundException
	 * @throws IBOLookupException
	 */
	public static void addPoller(ImportHandler importHandler) throws IBOLookupException, ClassNotFoundException{
		DirectoryPoller poller = new DirectoryPoller(new File(importHandler.getAutoImpFolder()));
		poller.setAutoMove(true);		//Moves the files to a subfolder before handling
		poller.addPollManager(new AutoImportPollManager(importHandler.getClassName(),importHandler.getAutoImpFileType()));
		poller.start();
		pollers.put(importHandler.getClassName(),poller);
		System.out.println("Starting automatic import poller: "+importHandler.getName() +" for folder "+ importHandler.getAutoImpFolder());
	}
}
