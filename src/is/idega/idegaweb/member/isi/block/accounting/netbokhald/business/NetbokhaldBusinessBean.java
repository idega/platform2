package is.idega.idegaweb.member.isi.block.accounting.netbokhald.business;

import is.idega.idegaweb.member.isi.block.accounting.data.DiscountEntry;
import is.idega.idegaweb.member.isi.block.accounting.data.DiscountEntryHome;
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

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.user.data.Group;
import com.idega.util.IWTimestamp;

public class NetbokhaldBusinessBean extends IBOServiceBean implements
		NetbokhaldBusiness {
	public Collection getFinanceEntries(String companyNumber, Date dateFrom) {
		Collection col = null;
		try {
			NetbokhaldSetup setup = getNetbokhaldSetupHome().findByPrimaryKey(
					companyNumber);
			
			if (setup.getDeleted()) {
				return null;
			}

			col = getFinanceEntryHome()
					.findAllByClubAndDivisionAndGroupAndDate(setup.getClub(),
							setup.getDivision(), setup.getGroup(),
							new IWTimestamp(dateFrom));
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}

		return col;
	}

	public Collection getFinanceEntries(String companyNumber,
			String fromSerialNumber) {
		Collection col = null;
		try {
			NetbokhaldSetup setup = getNetbokhaldSetupHome().findByPrimaryKey(
					companyNumber);
			
			if (setup.getDeleted()) {
				return null;
			}
			
			col = getFinanceEntryHome()
					.findAllByClubAndDivisionAndGroupAndSerial(setup.getClub(),
							setup.getDivision(), setup.getGroup(),
							Integer.parseInt(fromSerialNumber));
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}

		return col;
	}

	public Collection getDiscountEntries(String companyNumber, Date dateFrom) {
		Collection col = null;
		try {
			NetbokhaldSetup setup = getNetbokhaldSetupHome().findByPrimaryKey(
					companyNumber);

			if (setup.getDeleted()) {
				return null;
			}

			col = getDiscountEntryHome()
					.findAllByClubAndDivisionAndGroupAndDate(setup.getClub(),
							setup.getDivision(), setup.getGroup(),
							new IWTimestamp(dateFrom));
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}

		return col;
	}

	public Collection getDiscountEntries(String companyNumber,
			String fromSerialNumber) {
		Collection col = null;
		try {
			NetbokhaldSetup setup = getNetbokhaldSetupHome().findByPrimaryKey(
					companyNumber);

			if (setup.getDeleted()) {
				return null;
			}

			col = getDiscountEntryHome()
					.findAllByClubAndDivisionAndGroupAndSerial(setup.getClub(),
							setup.getDivision(), setup.getGroup(),
							Integer.parseInt(fromSerialNumber));
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
			NetbokhaldSetup setup = getNetbokhaldSetupHome().findByPrimaryKey(
					companyNumber);

			if (setup.getDeleted()) {
				return null;
			}

			Collection col = getNetbokhaldAccountingKeysHome()
					.findAllBySetupID(setup);

			if (col != null && !col.isEmpty()) {
				map = new HashMap();
				Iterator it = col.iterator();
				while (it.hasNext()) {
					NetbokhaldAccountingKeys key = (NetbokhaldAccountingKeys) it
							.next();
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
		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	}

	public Collection getNetbokhaldConnection(Group club) {
		try {
			return getNetbokhaldSetupHome().findAllByClub(club);
		} catch (IDOLookupException e) {
		} catch (FinderException e) {
		}

		return null;
	}

	public boolean insertNetbokhaldConnection(String externalID, Group club,
			Group division, Group group) {
		NetbokhaldSetup setup = null;

		try {
			setup = getNetbokhaldSetupHome().findByPrimaryKey(externalID);
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
		}

		if (setup == null) {
			try {
				setup = getNetbokhaldSetupHome().create();
			} catch (IDOLookupException e) {
				e.printStackTrace();
			} catch (CreateException e) {
				e.printStackTrace();
			}
		}

		if (setup != null) {
			setup.setClub(club);
			setup.setDivision(division);
			setup.setExternalID(externalID);
			setup.setGroup(group);
			setup.setDeleted(false);
			setup.store();

			return true;
		}

		return false;
	}
	
	public boolean deleteNetbokhaldConnection(String ids[]) {
		try {
			for (int i = 0; i < ids.length; i++) {
				Integer id = new Integer(ids[i]);
				NetbokhaldSetup setup = getNetbokhaldSetupHome()
						.findByPrimaryKey(id);
				setup.setDeleted(true);
				setup.store();
			}

			return true;
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}

		return false;
	}

	public NetbokhaldSetup getNetbokhaldSetup(String setupID) {
		NetbokhaldSetup setup = null;

		try {
			setup = getNetbokhaldSetupHome().findByPrimaryKey(setupID);
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
		}

		return setup;
	}
	
	
	public boolean insertNetbokhaldAccountingKey(String setupID,
			String type, int key, String debit, String credit) {

		NetbokhaldSetup setup = getNetbokhaldSetup(setupID);

		if (setup != null) {
			return insertNetbokhaldAccountingKey(setup, type, key, debit, credit);
		}

		return false;
	}

	public boolean insertNetbokhaldAccountingKey(NetbokhaldSetup setup,
			String type, int key, String debit, String credit) {

		NetbokhaldAccountingKeys accKey = null;

		try {
			accKey = getNetbokhaldAccountingKeysHome().findBySetupIDTypeAndKey(
					setup, type, key);
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
		}

		if (accKey == null) {
			try {
				accKey = getNetbokhaldAccountingKeysHome().create();
			} catch (IDOLookupException e) {
				e.printStackTrace();
			} catch (CreateException e) {
				e.printStackTrace();
			}
		}

		if (accKey != null) {
			accKey.setCreditKey(credit);
			accKey.setDebitKey(debit);
			accKey.setKey(key);
			accKey.setSetup(setup);
			accKey.setType(type);
			accKey.setDeleted(false);
			accKey.store();

			return true;
		}

		return false;
	}

	public boolean deleteAccountingKeys(String ids[]) {
		try {
			for (int i = 0; i < ids.length; i++) {
				Integer id = new Integer(ids[i]);
				NetbokhaldAccountingKeys key = getNetbokhaldAccountingKeysHome()
						.findByPrimaryKey(id);
				key.setDeleted(true);
				key.store();
			}

			return true;
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}

		return false;
	}

	public Map getAccountingKeys(NetbokhaldSetup setup) {
		return getAccountingKeys(setup.getExternalID());
	}

	private FinanceEntryHome getFinanceEntryHome() throws IDOLookupException {
		return (FinanceEntryHome) IDOLookup.getHome(FinanceEntry.class);
	}

	private DiscountEntryHome getDiscountEntryHome() throws IDOLookupException {
		return (DiscountEntryHome) IDOLookup.getHome(DiscountEntry.class);
	}

	private NetbokhaldSetupHome getNetbokhaldSetupHome()
			throws IDOLookupException {
		return (NetbokhaldSetupHome) IDOLookup.getHome(NetbokhaldSetup.class);
	}

	private NetbokhaldAccountingKeysHome getNetbokhaldAccountingKeysHome()
			throws IDOLookupException {
		return (NetbokhaldAccountingKeysHome) IDOLookup
				.getHome(NetbokhaldAccountingKeys.class);
	}
}