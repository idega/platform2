package se.idega.idegaweb.commune.accounting.invoice.business;

import com.idega.io.MemoryFileBuffer;
import com.idega.user.data.User;
import com.lowagie.text.DocumentException;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.Map;
import javax.ejb.FinderException;

public interface CheckAmountBusiness extends com.idega.business.IBOService
{
	Map sendCheckAmountLists(User p0, String p2) throws RemoteException;
	MemoryFileBuffer getInternalCheckAmountListBuffer (String schoolCategoryId, Integer providerId, Date startPeriod, Date endPeriod, boolean isShowPosting)	throws RemoteException, DocumentException, FinderException;
}
