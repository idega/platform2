/*
 * Created on May 27, 2003
 *
 */
package com.idega.block.dataquery.business;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLException;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.dataquery.data.UserQuery;
import com.idega.block.dataquery.data.xml.QueryHelper;
import com.idega.block.media.business.MediaBusiness;
import com.idega.business.IBOSessionBean;
import com.idega.core.file.data.ICFile;
import com.idega.core.file.data.ICFileHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDOStoreException;
import com.idega.presentation.IWContext;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author aron 
 * @version 1.0
 */
public class QuerySessionBean extends IBOSessionBean implements QuerySession   {
	
	
	private QueryHelper helper = null;
	private int userQueryID = -1;
	
	public QueryService getQueryService() throws RemoteException {
		return (QueryService) getServiceInstance(QueryService.class);
	}
	public void createNewQuery() throws RemoteException {
		helper = getQueryService().getQueryHelper();
	}
	public void createQuery(int userQueryID, IWContext iwc) throws RemoteException, FinderException {
		helper = getQueryService().getQueryHelper(userQueryID, iwc);
		this.userQueryID = userQueryID;
	}
	public QueryHelper getQueryHelper(IWContext iwc) throws RemoteException, FinderException {
		if (helper == null) {
			if (userQueryID > 0) {
				createQuery(userQueryID, iwc);
			}
			else {
				createNewQuery();
			}
		}
		return helper;
	}
	/**
	 * @param i
	 */
	public void setUserQueryID(int i){
		userQueryID = i;
	}
	
	public UserQuery storeQuery(String name,boolean isPrivate, boolean overwriteQuery)  throws IDOStoreException, RemoteException, IOException, CreateException, SQLException, FinderException {
		UserQuery userQuery = getQueryService().storeOrUpdateQuery(name, helper, isPrivate, overwriteQuery,  getUserContext());
		// add id to current id and render the document from it
		//createQuery(((Integer)query.getPrimaryKey()).intValue());
		// temporary
		int fileId = ((Integer) userQuery.getSource().getPrimaryKey()).intValue();
		MediaBusiness.moveMedia(fileId, getFile("query").getNodeID());
		return userQuery;
	}
	
			
	

	
	
	private ICFile getFile(String name)	{
  	try {
      ICFileHome home = (ICFileHome) IDOLookup.getHome(ICFile.class);
      ICFile file = home.findByFileName(name);
      return file;
    }
    catch(RemoteException ex){
      throw new RuntimeException("[ReportBusiness]: Message was: " + ex.getMessage());
    }
    catch (FinderException ex) {
			return null;
		}
  }	

	
	public ICFile getXMLFile(int id)throws RemoteException{
		try {
			return ((ICFileHome) this.getIDOHome(ICFile.class)).findByPrimaryKey(new Integer(id));
		}
		catch (FinderException e) {
			throw new RemoteException(e.getMessage());
		}
	}
	
	
}
