package se.idega.idegaweb.commune.accounting.invoice.data;

import com.idega.block.school.data.School;
import com.idega.data.IDOEntity;
import java.rmi.RemoteException;

/**
 * Last modified: $Date: 2004/03/22 13:01:14 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.1 $
 */
public interface CheckAmountReceivingSchool extends IDOEntity {
	int getSchoolId () throws RemoteException;
	School getSchool () throws RemoteException;
	int getCheckAmountBroadcastId () throws RemoteException;
	CheckAmountBroadcast getCheckAmountBroadcast () throws RemoteException;
	int getPaymentRecordCount () throws RemoteException;
	boolean isByEmail () throws RemoteException;
	void setSchoolId (int id) throws RemoteException;
	void setSchool (School school) throws RemoteException;
	void setCheckAmountBroadcastId (int id) throws RemoteException;
	void setCheckAmountBroadcast (CheckAmountBroadcast checkAmountBroadcast)
		throws RemoteException;
	void setPaymentRecordCount (int count) throws RemoteException;
	void setIsByEmail (boolean isByEmail) throws RemoteException;
}
