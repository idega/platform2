package com.idega.block.importer.business;

import java.io.File;
import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.importer.data.*;
import com.idega.business.IBOServiceBean;
import com.idega.user.business.GroupBusiness;
import com.idega.user.data.Group;


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
	public boolean importRecords(String handlerClass,String fileClass,String filePath,Integer groupId)throws RemoteException {
	    try{
	      boolean status = false;
	      
	      	
	      ImportFileHandler handler = this.getImportFileHandler(handlerClass);
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
	public boolean importRecords(String handlerClass,String fileClass,String filePath)throws RemoteException {
		try{
	      boolean status = false;
	      ImportFileHandler handler = this.getImportFileHandler(handlerClass);
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
  	
  	public ImportFileHandler getImportFileHandler(String handlerClass) throws Exception{
    	ImportFileHandler handler = (ImportFileHandler)  this.getServiceInstance(Class.forName(handlerClass));
	    return handler;
  	}
 
  	public ImportFile getImportFile(String fileClass) throws Exception{
	      return (ImportFile)Class.forName(fileClass).newInstance();
  	}

  	
  	
  	
  	
  	

}