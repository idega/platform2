package se.idega.idegaweb.commune.accounting.invoice.data;

import com.idega.data.IDOHome;
import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.FinderException;

/**
 * Last modified: $Date: 2004/03/22 13:01:14 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.1 $
 */
public interface CheckAmountReceivingSchoolHome extends IDOHome {
	CheckAmountReceivingSchool create () throws CreateException, RemoteException;
	CheckAmountReceivingSchool findByPrimaryKey (Object primaryKey)
		throws FinderException, RemoteException;
	Collection findEmailedProvidersByCheckAmountBroadcast
		(CheckAmountBroadcast broadcastInfo) throws FinderException,
																								RemoteException;
	Collection findPaperMailedProvidersByCheckAmountBroadcast
		(CheckAmountBroadcast broadcastInfo) throws FinderException,
																								RemoteException;
	Collection findIgnoredProvidersByCheckAmountBroadcast
		(CheckAmountBroadcast broadcastInfo) throws FinderException,
																								RemoteException;
}
