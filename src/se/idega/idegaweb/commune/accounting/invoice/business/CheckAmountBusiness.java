package se.idega.idegaweb.commune.accounting.invoice.business;

import com.idega.io.MemoryFileBuffer;
import com.idega.user.data.User;
import com.lowagie.text.DocumentException;
import java.rmi.RemoteException;
import java.sql.Date;
import javax.ejb.FinderException;
import javax.ejb.CreateException;

public interface CheckAmountBusiness extends com.idega.business.IBOService
{
	void sendCheckAmountLists(User p0, String schoolCategoryPK) throws RemoteException, CreateException, FinderException;
	MemoryFileBuffer getInternalCheckAmountListBuffer (String schoolCategoryId, Integer providerId, Date startPeriod, Date endPeriod, boolean isShowPosting)	throws RemoteException, DocumentException, FinderException;
}
