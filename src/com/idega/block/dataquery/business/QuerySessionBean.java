/*
 * Created on May 27, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package com.idega.block.dataquery.business;

import java.rmi.RemoteException;

import com.idega.business.IBOSessionBean;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author aron 
 * @version 1.0
 */

public class QuerySessionBean extends IBOSessionBean implements QuerySession{
	private QueryHelper query = null;
	private int xmlFileID = -1;
	
	public QueryService getQueryService() throws RemoteException{
		return (QueryService)this.getServiceInstance(QueryService.class);
	}
	
	public void createNewQuery() throws RemoteException{
		query = getQueryService().getQueryHelper();
	}
	
	public void createQuery(int XMLFileID)throws RemoteException{
		query = getQueryService().getQueryHelper( XMLFileID);
	}

	
	public QueryHelper getQueryHelper(){
		if(query==null){
			try {
				if(xmlFileID>0)
					createQuery(xmlFileID);
				else
					createNewQuery();
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return query;
	}
	/**
	 * @param i
	 */
	public void setXmlFileID(int i) {
		xmlFileID = i;
	}

}
