package is.idega.idegaweb.member.isi.block.accounting.netbokhald.business;

import is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntry;
import is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryHome;
import is.idega.idegaweb.member.isi.block.accounting.netbokhald.data.NetbokhaldAccountingKeys;
import is.idega.idegaweb.member.isi.block.accounting.netbokhald.data.NetbokhaldAccountingKeysHome;
import is.idega.idegaweb.member.isi.block.accounting.netbokhald.data.NetbokhaldSetup;
import is.idega.idegaweb.member.isi.block.accounting.netbokhald.data.NetbokhaldSetupHome;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.ejb.FinderException;

import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.util.IWTimestamp;

public class NetbokhaldBusinessBean extends IBOServiceBean implements NetbokhaldBusiness {
	public Collection getFinanceEntries(String companyNumber, Date dateFrom) {
		Collection col = null;
		try {
			NetbokhaldSetup setup = getNetbokhaldSetupHome().findByPrimaryKey(companyNumber);
			col = getFinanceEntryHome().findAllByClubAndDivisionAndGroupAndDate(setup.getClub(), setup.getDivision(), setup.getGroup(), new IWTimestamp(dateFrom));
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
				
		return col;
	}

	public Collection getFinanceEntries(String companyNumber, String fromSerialNumber) {
		Collection col = null;
		try {
			NetbokhaldSetup setup = getNetbokhaldSetupHome().findByPrimaryKey(companyNumber);
			col = getFinanceEntryHome().findAllByClubAndDivisionAndGroupAndSerial(setup.getClub(), setup.getDivision(), setup.getGroup(), Integer.parseInt(fromSerialNumber));
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
				
		return col;
	}

	public Map getAccountingKeys(String companyNumber) {
		Map map = null;
		
		try {
			NetbokhaldSetup setup = getNetbokhaldSetupHome().findByPrimaryKey(companyNumber);
			Collection col = getNetbokhaldAccountingKeysHome().findAllBySetupID(setup);
			
			if (col != null && !col.isEmpty()) {
				map = new HashMap();
				Iterator it = col.iterator();
				while (it.hasNext()) {
					NetbokhaldAccountingKeys key = (NetbokhaldAccountingKeys) it.next();
					Map innerMap = (Map) map.get(key.getType());
					if (innerMap == null) {
						innerMap = new HashMap();
					}
					innerMap.put(new Integer(key.getKey()), key);
					
					map.put(key.getType(), innerMap);
				}
			}
			
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		
		return map;
	}
	
	private FinanceEntryHome getFinanceEntryHome() throws IDOLookupException {
		return (FinanceEntryHome) IDOLookup.getHome(FinanceEntry.class);
	}
	
	private NetbokhaldSetupHome getNetbokhaldSetupHome() throws IDOLookupException {
		return (NetbokhaldSetupHome) IDOLookup.getHome(NetbokhaldSetup.class);
	}
	
	private NetbokhaldAccountingKeysHome getNetbokhaldAccountingKeysHome() throws IDOLookupException {
		return (NetbokhaldAccountingKeysHome) IDOLookup.getHome(NetbokhaldAccountingKeys.class);
	}
}