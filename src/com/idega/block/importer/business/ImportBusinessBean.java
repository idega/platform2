package com.idega.block.importer.business;

import java.io.File;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.block.importer.data.ImportFile;
import com.idega.block.importer.data.ImportFileClass;
import com.idega.block.importer.data.ImportFileClassHome;
import com.idega.block.importer.data.ImportHandler;
import com.idega.block.importer.data.ImportHandlerHome;
import com.idega.business.IBOServiceBean;
import com.idega.business.IBOSession;
import com.idega.idegaweb.IWUserContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.user.business.GroupBusiness;


/**
 * <p>Title: IdegaWeb classes</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Idega Software</p>
 * @author <a href="mailto:eiki@idega.is"> Eirikur Sveinn Hrafnsson</a>
 * @version 1.0
 */

public class ImportBusinessBean extends IBOServiceBean implements ImportBusiness{

  public ImportBusinessBean() {
  }

/**
 * @see com.idega.block.importer.business.ImportBusiness#getImportHandlers()
 */
  public Collection getImportHandlers() throws RemoteException{
  	
  	Collection col = null;
	try {
		col = ((ImportHandlerHome) this.getIDOHome(ImportHandler.class)).findAllImportHandlers();
	}
	catch (FinderException e) {
	}
  	
    return  col;
  }

/**
 * @see com.idega.block.importer.business.ImportBusiness#getImportFileTypes()
 */
  public Collection getImportFileTypes() throws RemoteException {
  	Collection col = null;
	try {
		col = ((ImportFileClassHome) this.getIDOHome(ImportFileClass.class)).findAllImportFileClasses();
		
	}
	catch (FinderException e) {
	}
  	
    return  col;
  }
  


	/**
	 * @see com.idega.block.importer.business.ImportBusiness#importRecords(String, String, String, Integer)
	 */
	public boolean importRecords(String handlerClass,String fileClass,String filePath,Integer groupId, IWUserContext iwuc)throws RemoteException {
	    try{
	      boolean status = false;
	      	
	      ImportFileHandler handler = this.getImportFileHandler(handlerClass,iwuc);
	      ImportFile file = this.getImportFile(fileClass);
	      
	      file.setFile(new File(filePath));
	      	      
	      handler.setImportFile(file);
	      handler.setRootGroup(getGroupBusiness().getGroupByGroupID(groupId.intValue()));
	  	      
	      status = handler.handleRecords();
		
	      return status;
	    }
	    catch(NoRecordsException ex){
	     ex.printStackTrace();
	     return false;
	    }
	    catch(Exception ex){
	     ex.printStackTrace();
	     return false;
	    }
	}

	/**
	 * @see com.idega.block.importer.business.ImportBusiness#importRecords(String, String, String)
	 */
	public boolean importRecords(String handlerClass,String fileClass,String filePath, IWUserContext iwuc)throws RemoteException {
		try{
	      boolean status = false;
	      ImportFileHandler handler = this.getImportFileHandler(handlerClass,iwuc);
	      ImportFile file = this.getImportFile(fileClass);
	      
	      file.setFile(new File(filePath));
	      	      
	      handler.setImportFile(file);
	  	      
	      status = handler.handleRecords();
	      	
	      return status;
	    }
	    catch(NoRecordsException ex){
	     ex.printStackTrace();
	     return false;
	    }
	    catch(Exception ex){
	     ex.printStackTrace();
	     return false;
	    }
	}
	
	public GroupBusiness getGroupBusiness() throws Exception{
    	return (GroupBusiness) this.getServiceInstance(GroupBusiness.class);
  	}
  	
  	public ImportFileHandler getImportFileHandler(String handlerClass, IWUserContext iwuc) throws Exception{
  		Class importHandlerInterfaceClass = Class.forName(handlerClass);
			Class[] interfaces = importHandlerInterfaceClass.getInterfaces();
			boolean isSessionBean = false;
			for (int i = 0; i < interfaces.length; i++) {
				Class class1 = interfaces[i];
				if( class1.equals(IBOSession.class)){
					isSessionBean = true;
					break;
				}				
			}
			
			ImportFileHandler handler;
			
  		if( isSessionBean ){
				handler = (ImportFileHandler)  getSessionInstance(iwuc,importHandlerInterfaceClass);
  		}
  		else{
				handler = (ImportFileHandler)  getServiceInstance(importHandlerInterfaceClass);
  		}
 
	    return handler;
  	}
 
  	public ImportFile getImportFile(String fileClass) throws Exception{
	      return (ImportFile)Class.forName(fileClass).newInstance();
  	}

	public DropdownMenu getImportHandlers(IWContext iwc, String name) throws RemoteException {
		DropdownMenu menu = new DropdownMenu(name);
		Collection col = getImportHandlers();
		Iterator iter = col.iterator();
		while (iter.hasNext()) {
			ImportHandler element = (ImportHandler) iter.next();
			menu.addMenuElement(element.getClassName(), element.getName());
		}
		return menu;
	}
	public DropdownMenu getImportFileClasses(IWContext iwc, String name) throws RemoteException {
		DropdownMenu menu = new DropdownMenu(name);
		Collection col = getImportFileTypes();
		Iterator iter = col.iterator();
		while (iter.hasNext()) {
			ImportFileClass element = (ImportFileClass) iter.next();
			menu.addMenuElement(element.getClassName(), element.getName());
		}
		return menu;
	}
	
}