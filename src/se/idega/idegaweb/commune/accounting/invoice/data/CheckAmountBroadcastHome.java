package se.idega.idegaweb.commune.accounting.invoice.data;

import com.idega.data.IDOHome;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.FinderException;

/**
 * Last modified: $Date: 2004/03/23 14:04:06 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.2 $
 */
public interface CheckAmountBroadcastHome extends IDOHome {
	CheckAmountBroadcast create () throws CreateException, RemoteException;
	CheckAmountBroadcast findByPrimaryKey (Object primaryKey)
		throws FinderException, RemoteException;
	CheckAmountBroadcast findLatestBySchoolCategoryId (String schoolCategoryId)
		throws FinderException, RemoteException;
	Collection findOlderByTimestamp (String schoolCategoryPK, Timestamp timestamp)
		throws FinderException, RemoteException;
}
