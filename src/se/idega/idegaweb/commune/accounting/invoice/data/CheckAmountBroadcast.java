package se.idega.idegaweb.commune.accounting.invoice.data;

import com.idega.block.school.data.SchoolCategory;
import com.idega.data.IDOEntity;
import java.rmi.RemoteException;
import java.sql.Timestamp;

/**
 * Last modified: $Date: 2004/03/22 13:01:14 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.1 $
 */
public interface CheckAmountBroadcast extends IDOEntity {
	String getSchoolCategoryId () throws RemoteException;
	SchoolCategory getSchoolCategory () throws RemoteException;
	Timestamp getStartTime () throws RemoteException;
	Timestamp getEndTime () throws RemoteException;
	int getSchoolCount () throws RemoteException;
	void setSchoolCategoryId (String id) throws RemoteException;
	void setSchoolCategory (SchoolCategory schoolCategory)
		throws RemoteException;
	void setStartTime (Timestamp startTime) throws RemoteException;
	void setEndTime (Timestamp endTime) throws RemoteException;
	void setSchoolCount (int count) throws RemoteException;
}
