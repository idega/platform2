/*
 * Created on May 27, 2003
 *
 */
package com.idega.block.dataquery.business;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ejb.FinderException;

import com.idega.block.dataquery.data.xml.*;
import com.idega.block.media.business.MediaBusiness;
import com.idega.business.IBOLookup;
import com.idega.business.IBOSessionBean;
import com.idega.core.file.data.ICFile;
import com.idega.core.file.data.ICFileHome;
import com.idega.data.IDOLookup;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.xml.XMLData;
/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author aron 
 * @version 1.0
 */
public class QuerySessionBean extends IBOSessionBean implements QuerySession {
	private QueryHelper helper = null;
	private int xmlFileID = -1;
	public QueryService getQueryService() throws RemoteException {
		return (QueryService) this.getServiceInstance(QueryService.class);
	}
	public void createNewQuery() throws RemoteException {
		helper = getQueryService().getQueryHelper();
	}
	public void createQuery(int XMLFileID) throws RemoteException {
		helper = getQueryService().getQueryHelper(XMLFileID);
		this.xmlFileID = XMLFileID;
	}
	public QueryHelper getQueryHelper() {
		if (helper == null) {
			try {
				if (xmlFileID > 0)
					createQuery(xmlFileID);
				else
					createNewQuery();
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return helper;
	}
	/**
	 * @param i
	 */
	public void setXmlFileID(int i){
		xmlFileID = i;
	}
	public ICFile storeQuery(String name,int folderID, boolean isPrivate)  throws IOException {
		XMLData data = XMLData.getInstanceWithoutExistingFile();
		if(xmlFileID>0){
			data.setXmlFileId(xmlFileID);
		}
		// name must be stored within the document because it is the identifier when using the query as input for
		// another query
		helper.setName(name);
		data.setDocument(helper.createDocument());
		data.setName(name);
		ICFile query =  data.store();
		User currentUser = getUserContext().getCurrentUser();
		UserBusiness userBusiness = getUserBusiness();
		// TODO: thi solve problem with group types
		String[] groupTypes = 
			{ "iwme_federation", "iwme_union", "iwme_regional_union",  "iwme_league", "iwme_club", "iwme_club_division"};
		Group group = userBusiness.getUsersHighestTopGroupNode(currentUser, Arrays.asList(groupTypes), getUserContext());
		if (group == null) {
			List groupType = new ArrayList();
			groupType.add("general");
			group = userBusiness.getUsersHighestTopGroupNode(currentUser, groupType, getUserContext());
		}
		String suffix  = (isPrivate) ? "_private" : "_public";
		String groupName = group.getPrimaryKey().toString();
		if(folderID>0 && query !=null) {
			String folderName = new StringBuffer(groupName).append(suffix).toString();
			ICFile subFolder = getFile(folderName);
			if (subFolder == null) {
				try {
					subFolder = MediaBusiness.createSubFolder(folderID, folderName);
				}
				catch (Exception ex)	{
					//TODO: thi solve this exception problem in a right way
					throw new IOException("Subfolder couldn't be created");
				}
			}
			int subFolderInt = Integer.parseInt(subFolder.getPrimaryKey().toString());
			MediaBusiness.moveMedia(((Integer)query.getPrimaryKey()).intValue(), subFolderInt);
		}			
		// add id to current id and render the document from it
		createQuery(((Integer)query.getPrimaryKey()).intValue());
		return query;
	
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
	
// unused method	
//  private ICFile getNewXMLFile()  {
//    try {
//      ICFileHome home = (ICFileHome) IDOLookup.getHome(ICFile.class);
//      ICFile xmlFile = (ICFile) home.create();
//      return xmlFile;
//    }
//    // FinderException, RemoteException
//    catch (Exception ex)  {
//      throw new RuntimeException("[XMLData]: Message was: " + ex.getMessage());
//    }
//    
//  }
	
	
	
	public UserBusiness getUserBusiness()	{
		try {
			return (UserBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), UserBusiness.class);
		}
		catch (RemoteException ex)	{
      System.err.println("[ReportOverview]: Can't retrieve UserBusiness. Message is: " + ex.getMessage());
      throw new RuntimeException("[ReportOverview]: Can't retrieve UserBusiness");
		}
	}
	
}
