/*
 * Created on May 27, 2003
 *
 */
package com.idega.block.dataquery.business;
import java.io.IOException;
import java.rmi.RemoteException;

import javax.ejb.FinderException;

//import com.idega.block.dataquery.data.Query;
import com.idega.block.media.business.MediaBusiness;
import com.idega.business.IBOSessionBean;
import com.idega.core.data.ICFile;
import com.idega.core.data.ICFileHome;
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
	public ICFile storeQuery(String name,int folderID)  throws IOException{
		XMLData data = XMLData.getInstanceWithoutExistingFile();
		if(xmlFileID>0){
			data.setXmlFileId(xmlFileID);
		}
		data.setDocument(helper.createDocument());
		data.setName(name);
		ICFile query =  data.store(getIWApplicationContext());
		if(folderID>0 && query !=null)
			MediaBusiness.moveMedia(((Integer)query.getPrimaryKey()).intValue(),folderID);
			
		// add id to current id and render the document from it
		createQuery(((Integer)query.getPrimaryKey()).intValue());
		return query;
	
	}
	
	public ICFile getXMLFile(int id)throws RemoteException{
		try {
			return ((ICFileHome) this.getIDOHome(ICFile.class)).findByPrimaryKey(id);
		}
		catch (FinderException e) {
			throw new RemoteException(e.getMessage());
		}
	}
}
