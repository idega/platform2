package se.idega.idegaweb.commune.accounting.invoice.business;

import com.idega.io.MediaWritable;
import com.idega.user.data.User;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.Map;
import javax.ejb.FinderException;

public interface CheckAmountBusiness extends com.idega.business.IBOService
{
	Map sendCheckAmountLists(User p0, String p2) throws RemoteException;
	int createInternalCheckAmountList (String schoolCategoryId, Integer providerId, Date startPeriod, Date endPeriod) throws FinderException, RemoteException;
	MediaWritable getInternalCheckAmountListStream (String schoolCategoryId, Integer providerId, Date startPeriod, Date endPeriod) throws RemoteException;
}
