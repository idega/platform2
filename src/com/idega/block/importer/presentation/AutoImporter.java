package com.idega.block.importer.presentation;

import java.io.File;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.block.importer.business.ImportBusiness;
import com.idega.block.importer.data.ImportFileClass;
import com.idega.block.importer.data.ImportFileClassHome;
import com.idega.block.importer.data.ImportHandler;
import com.idega.block.importer.data.ImportHandlerHome;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Paragraph;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;

/**
 * Lets the user see and configure the automatic import configurations
 * Copyright:    Copyright (c) 2004
 * Company:      idega software
 * @author Joakim@idega.is
 */
public class AutoImporter extends com.idega.presentation.Block {
	private static final String PARAMETER_IMPORT_HANDLER = "im_imh";
	private static final String PARAMETER_IMPORT_FILE = "im_imf";
	private static final String PARAMETER_IMPORT_FOLDER = "im_fo";

	private static String PARAM_UPDATE="im_up";
	private static String PARAM_DELETE="im_de";
	private IWContext _iwc;
	private IWResourceBundle iwrb;
	private String errorMessage = "";

	public void main(IWContext iwc) throws Exception{
		_iwc = iwc;
		iwrb = this.getResourceBundle(iwc);
		errorMessage = "";
		
		handleAction(iwc);
		
		Form form = new Form();
		Table table = new Table(3, 3);

		//Create the config area, to set the handler, file type and folder
		try {
			Text importHandlerText = new Text(iwrb.getLocalizedString("importer.import.handler", "Import handler : "));
			importHandlerText.setBold();
			table.add(importHandlerText, 1, 1);
			table.add(getImportBusiness(iwc).getImportHandlers(iwc, PARAMETER_IMPORT_HANDLER), 2, 1);

			Text fileType = new Text(iwrb.getLocalizedString("importer.import.filetype", "File type : "));
			fileType.setBold();
			table.add(fileType, 1, 2);
			table.add(getImportBusiness(iwc).getImportFileClasses(iwc, PARAMETER_IMPORT_FILE), 2, 2);

			Text importFolder = new Text(iwrb.getLocalizedString("importer.import.folder", "Import folder : "));
			importFolder.setBold();
			table.add(importFolder, 1, 3);
			
			TextInput importFolderInput = new TextInput(PARAMETER_IMPORT_FOLDER);
			//TODO (JJ) add logic to fetch folder info
			table.add(importFolderInput,2,3);

			
			SubmitButton update = new SubmitButton(PARAM_UPDATE,iwrb.getLocalizedString("importer.update", "Update"));
			update.setAsImageButton(true);
			table.add(update, 3, 3);
		} catch (RemoteException e) {
			add(new ExceptionWrapper(e, this));
			e.printStackTrace();
		}
		form.add(table);
		form.add(new Text(errorMessage));
		form.add(new Paragraph());
		form.add(getAllAutomaticUpdates(iwc));
		add(form);
	}

	/**
	 * Returns a table with all automatic updates listed in it
	 * 
	 * @param iwc
	 * @return
	 */
	private Table getAllAutomaticUpdates(IWContext iwc) {
		ImportHandlerHome importHandlerHome;
		Table table = null;
		try {
			importHandlerHome = (ImportHandlerHome)IDOLookup.getHome(ImportHandler.class);
			ImportFileClassHome importFileClassHome = (ImportFileClassHome)IDOLookup.getHome(ImportFileClass.class);
			Collection coll = importHandlerHome.findAllAutomaticUpdates();
			
			//If we have some automatic poller, display them
			if(coll.size()>0){
				int row = 2;
				table = new Table(4,coll.size()+1);
				
				//Create the headers
				Text importHandlerText = new Text(iwrb.getLocalizedString("importer.import.handler", "Import handler : "));
				importHandlerText.setBold();
				table.add(importHandlerText, 1, 1);

				Text fileType = new Text(iwrb.getLocalizedString("importer.import.filetype", "File type : "));
				fileType.setBold();
				table.add(fileType, 2, 1);

				Text importFolder = new Text(iwrb.getLocalizedString("importer.import.folder", "Import folder : "));
				importFolder.setBold();
				table.add(importFolder, 3, 1);

				Iterator iter = coll.iterator();
				//The list of all pollers
				while(iter.hasNext()){
					ImportHandler importHandler = (ImportHandler)iter.next();

					table.add(importHandler.getName(),1,row);
					ImportFileClass importFileClass = importFileClassHome.findByClassName(importHandler.getAutoImpFileType());
					table.add(importFileClass.getName(),2,row);
					table.add(importHandler.getAutoImpFolder(),3,row);
					SubmitButton delButton = new SubmitButton(
							iwrb.getLocalizedString("importer.delete", "Delete"),PARAM_DELETE,importHandler.getClassName());
					delButton.setAsImageButton(true);
					table.add(delButton,4,row);
					row++;
				}
			}else{
				table = new Table(1,1);
				table.add(new Text("No automatic imports specified"));
			}
		} catch (IDOLookupException e) {
			add(new ExceptionWrapper(e, this));
			e.printStackTrace();
		} catch (FinderException e) {
			add(new ExceptionWrapper(e, this));
			e.printStackTrace();
		}
		
		return table;
	}
	
	/**
	 * @param iwc
	 */
	private void handleAction(IWContext iwc) {
//		printParams(iwc);
		if(iwc.isParameterSet(PARAM_UPDATE)){
			doUpdate(iwc);
		} else if(iwc.isParameterSet(PARAM_DELETE)){
			System.out.println("Would delete "+iwc.getParameter(PARAM_DELETE));
			doDelete(iwc.getParameter(PARAM_DELETE));
		}
	}
	
//	private void printParams(IWContext iwc){
//		Enumeration enum = iwc.getParameterNames();
//		while(enum.hasMoreElements()){
//			String param = (String)enum.nextElement();
//			System.out.println("Parameter "+param+" "+iwc.getParameter(param));
//		}
//	}
	
	private void doDelete(String importHandlerString){
//		String importHandlerString = iwc.getParameter(PARAM_DELETE);
		com.idega.block.importer.IWBundleStarter.shutdown(importHandlerString);
		ImportHandlerHome ImportHandlerHome;
		try {
			ImportHandlerHome = (ImportHandlerHome)IDOLookup.getHome(ImportHandler.class);
			ImportHandler importHandler = ImportHandlerHome.findByClassName(importHandlerString);

			importHandler.setAutoImpFileType(null);
			importHandler.setAutoImpFolder(null);
			importHandler.store();
		} catch (IDOLookupException e) {
			add(new ExceptionWrapper(e, this));
			e.printStackTrace();
		} catch (FinderException e) {
			add(new ExceptionWrapper(e, this));
			e.printStackTrace();
		}
	}
	
	/**
	 * @param iwc
	 */
	private void doUpdate(IWContext iwc) {
		if(iwc.isParameterSet(PARAMETER_IMPORT_HANDLER)){
			String importHandlerString = iwc.getParameter(PARAMETER_IMPORT_HANDLER);
			String importFolderString = iwc.getParameter(PARAMETER_IMPORT_FOLDER);
			if(!new File(importFolderString).exists()){
				errorMessage = iwrb.getLocalizedString("importer.FolderNotFoundPleaseSpecifyValidFolder", "Folder not found. Please specify valid folder");
				return;
			}
			//Could move to business maybe
			doDelete(importHandlerString);
			ImportHandlerHome importHandlerHome;
			try {
				importHandlerHome = (ImportHandlerHome)IDOLookup.getHome(ImportHandler.class);
				ImportHandler importHandler = importHandlerHome.findByClassName(importHandlerString);

				importHandler.setAutoImpFileType(iwc.getParameter(PARAMETER_IMPORT_FILE));
				importHandler.setAutoImpFolder(importFolderString);
				importHandler.store();
				com.idega.block.importer.IWBundleStarter.addPoller(importHandler);
			} catch (IDOLookupException e) {
				add(new ExceptionWrapper(e, this));
				e.printStackTrace();
			} catch (FinderException e) {
				add(new ExceptionWrapper(e, this));
				e.printStackTrace();
			} catch (IBOLookupException e) {
				add(new ExceptionWrapper(e, this));
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				add(new ExceptionWrapper(e, this));
				e.printStackTrace();
			}
		}
	}

	public ImportBusiness getImportBusiness(IWContext iwc) throws RemoteException {
		return (ImportBusiness) IBOLookup.getServiceInstance(iwc, ImportBusiness.class);
	}
}
