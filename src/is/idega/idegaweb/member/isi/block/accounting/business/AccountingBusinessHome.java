/*
 * Created on Sep 10, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package is.idega.idegaweb.member.isi.block.accounting.business;

import com.idega.business.IBOHome;

/**
 * @author IBM
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface AccountingBusinessHome extends IBOHome {
    public AccountingBusiness create() throws javax.ejb.CreateException,
            java.rmi.RemoteException;

}
