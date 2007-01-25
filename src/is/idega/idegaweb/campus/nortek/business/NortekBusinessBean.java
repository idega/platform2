package is.idega.idegaweb.campus.nortek.business;

import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.block.finance.business.CampusAssessmentBusiness;
import is.idega.idegaweb.campus.nortek.data.Card;
import is.idega.idegaweb.campus.nortek.data.CardHome;
import is.idega.idegaweb.campus.nortek.data.CardTransactionLog;
import is.idega.idegaweb.campus.nortek.data.CardTransactionLogHome;
import is.idega.idegaweb.campus.nortek.data.NortekSetup;
import is.idega.idegaweb.campus.nortek.data.NortekSetupHome;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.finance.data.Account;
import com.idega.block.finance.data.AccountBMPBean;
import com.idega.block.finance.data.AccountHome;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;
import com.idega.util.IWTimestamp;

public class NortekBusinessBean extends IBOServiceBean implements
		NortekBusiness {
	
	public static final String ACTION_BAN = "ban";
	
	public static final String ACTION_VERIFY = "verify";
	
	public static final String ACTION_ADD = "add";
	
	public static final String CARD_PREFIX = "010500";
	
	public boolean isCardValid(String serialNumber) {
		Card card = null;
		
		try {
			card = getCardHome().findByPrimaryKey(serialNumber);
			addLogEntry(card, IWTimestamp.RightNow(), null, ACTION_VERIFY, Boolean.toString(card.getIsValid()), null, false, null, serialNumber);
		} catch (FinderException e) {
			e.printStackTrace();
			addLogEntry(card, IWTimestamp.RightNow(), null, ACTION_VERIFY, Boolean.toString(false), null, true, e.getMessage(), serialNumber);
			return false;
		} 
		
		return card.getIsValid();
	}
	
	public boolean banCard(String serialNumber, boolean ban) {
		Card card = null;
		
		try {
			card = getCardHome().findByPrimaryKey(serialNumber);
			addLogEntry(card, IWTimestamp.RightNow(), null, ACTION_BAN, Boolean.toString(ban), null, false, null, serialNumber);
		} catch (FinderException e) {
			e.printStackTrace();
			try {
				card = getCardHome().create();
				card.setCardSerialNumber(serialNumber);
				card.setDecodedCardSerialNumber(decodeSerialNumber(serialNumber));
				card.store();
				addLogEntry(card, IWTimestamp.RightNow(), null, ACTION_BAN, Boolean.toString(ban), null, false, null, serialNumber);
			} catch (CreateException e1) {
				e1.printStackTrace();
				addLogEntry(card, IWTimestamp.RightNow(), null, ACTION_BAN, Boolean.toString(ban), null, true, e1.getMessage(), serialNumber);
				return false;
			}
		}
		card.setIsValid(!ban);
		card.store();
		
		return true;
	}
	
	public boolean addAmountToCardUser(String serialNumber, Date timestamp, double amount, String terminalNumber) {
		Card card = null;
		
		try {
			card = getCardHome().findByPrimaryKey(serialNumber);
			User user = card.getUser();
			Account account = getAccountHome().findByUserAndType(user, AccountBMPBean.typeFinancial);
			
			NortekSetup setup = getNortekSetupHome().findEntry();
			Contract contract = getCampusAssessmentBusiness().findContractForUser(user);
			Integer division = new Integer(contract.getApartment().getFloor().getBuilding().getDivision());
			String name = setup.getAccountKey().getName();
			String info = setup.getAccountKey().getInfo();
			
			getCampusAssessmentBusiness().assessTariffsToAccount((float)amount, name, info, (Integer) account.getPrimaryKey(), (Integer) setup.getAccountKey().getPrimaryKey(), timestamp, (Integer) setup.getTariffGroup().getPrimaryKey(), (Integer) setup.getFinanceCategory().getPrimaryKey(), division, false, null); 
			
			addLogEntry(card, IWTimestamp.RightNow(), new IWTimestamp(timestamp), ACTION_ADD, Double.toString(amount), terminalNumber, false, null, serialNumber);
		} catch (Exception e) {
			e.printStackTrace();
			Throwable t = e.getCause();
			if (t != null) {
				addLogEntry(card, IWTimestamp.RightNow(), new IWTimestamp(timestamp), ACTION_ADD, Double.toString(amount), terminalNumber, true, t.getMessage(), serialNumber);
			} else {
				addLogEntry(card, IWTimestamp.RightNow(), new IWTimestamp(timestamp), ACTION_ADD, Double.toString(amount), terminalNumber, true, e.getMessage(), serialNumber);				
			}
			return false;
		}
		
		return true;
	}
	
	private void addLogEntry(Card card, IWTimestamp stamp, IWTimestamp externalStamp, String action, String value, String terminal, boolean isError, String errorMessage, String serialNumber) {
		CardTransactionLog log;
		try {
			log = getCardTransactionLogHome().create();
			if (card != null) {
				log.setCard(card);
			}
			if (stamp != null) {
				log.setEntryDate(stamp.getTimestamp());
			} else {
				log.setEntryDate(IWTimestamp.getTimestampRightNow());
			}
			if (externalStamp != null) {
				log.setExternalEntryDate(externalStamp.getTimestamp());
			}
			log.setAction(action);
			log.setValue(value);
			log.setTerminal(terminal);
			log.setIsError(isError);
			log.setErrorMessage(errorMessage);
			log.setSerialNumber(serialNumber);
			log.store();
		} catch (CreateException e) {
			e.printStackTrace();
		}
	}
	
	public Collection getCards() {
		Collection col = null;
		try {
			col = getCardHome().findAll();
		} catch (FinderException e) {
		}
		
		return col;
	}
	
	public Card getCard(String serialNumber) {
		try {
			return (Card) getCardHome().findByPrimaryKey(serialNumber);
		} catch (FinderException e) {
		}
		
		return null;
	}
	
	public void saveCard(String decodedSerial, String ssn, String valid) throws CreateException, FinderException {
		String encoded = encodeDecodedSerialNumber(decodedSerial);
		Card card = getCard(encoded);
		if (card == null) {
			card = getCardHome().create();
		}
		
		User user = null;
		if (ssn != null && !"".equals(ssn)) {
			user = getUserHome().findByPersonalID(ssn);
		}
		
		card.setCardSerialNumber(encoded);
		card.setDecodedCardSerialNumber(decodedSerial);
		card.setUser(user);
		if (valid != null && !"".equals(valid)) {
			card.setIsValid(true);
		} else {
			card.setIsValid(false);
		}
		
		card.store();
	}
	
	public String decodeSerialNumber(String serialNumber) {
		if (serialNumber.length() == 10) {
			String hex = serialNumber.substring(6);
			return Integer.valueOf(hex, 16).toString();
		}
		
		return null;
	}
	
	public String encodeDecodedSerialNumber(String encodedSerialNumber) {
		String ret = CARD_PREFIX + Integer.toHexString(0x10000 | Integer.parseInt(encodedSerialNumber)).substring(1).toUpperCase();
		
		return ret;
	}
	
	private CardHome getCardHome() {
		try {
			return (CardHome) IDOLookup.getHome(Card.class);
		} catch (IDOLookupException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private NortekSetupHome getNortekSetupHome() {
		try {
			return (NortekSetupHome) IDOLookup.getHome(NortekSetup.class);
		} catch (IDOLookupException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private CardTransactionLogHome getCardTransactionLogHome() {
		try {
			return (CardTransactionLogHome) IDOLookup.getHome(CardTransactionLog.class);
		} catch (IDOLookupException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private AccountHome getAccountHome() {
		try {
			return (AccountHome) IDOLookup.getHome(Account.class);
		} catch (IDOLookupException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private UserHome getUserHome() {
		try {
			return (UserHome) IDOLookup.getHome(User.class);
		} catch (IDOLookupException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	
	public CampusAssessmentBusiness getCampusAssessmentBusiness() throws RemoteException {
		return (CampusAssessmentBusiness) getServiceInstance(CampusAssessmentBusiness.class);
	}
}