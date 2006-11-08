package is.idega.idegaweb.member.isi.block.accounting.webservice.netbokhald.business;

import is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntry;
import is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryHome;

import java.util.Collection;
import java.util.Date;

import javax.ejb.FinderException;

import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;

public class NetbokhaldBusinessBean extends IBOServiceBean implements NetbokhaldBusiness {
	public Collection getFinanceEntries(String companyNumber, Date dateFrom) {
		Collection col = null;
		try {
			col = getFinanceEntryHome().findAllByClubId(330185);
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
				
		return col;
	}
	
	private FinanceEntryHome getFinanceEntryHome() throws IDOLookupException {
		return (FinanceEntryHome) IDOLookup.getHome(FinanceEntry.class);
	}
}
