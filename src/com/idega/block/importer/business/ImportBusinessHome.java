/*
 * Created on Sep 5, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.idega.block.importer.business;



import com.idega.business.IBOHome;

/**
 * @author IBM
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface ImportBusinessHome extends IBOHome {
    public ImportBusiness create() throws javax.ejb.CreateException,
            java.rmi.RemoteException;

}
