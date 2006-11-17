package is.idega.idegaweb.campus.nortek.business;

import is.idega.idegaweb.campus.nortek.data.Card;
import is.idega.idegaweb.campus.nortek.data.CardHome;
import is.idega.idegaweb.campus.nortek.data.CardTransactionLog;
import is.idega.idegaweb.campus.nortek.data.CardTransactionLogHome;

import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.util.IWTimestamp;

public class NortekBusinessBean extends IBOServiceBean implements
		NortekBusiness {
	
	public static final String ACTION_BAN = "ban";
	
	public static final String ACTION_VERIFY = "verify";
	
	public static final String ACTION_ADD = "add";
	
	public boolean isCardValid(String serialNumber) {
		Card card = null;
		
		try {
			card = getCardHome().findByPrimaryKey(serialNumber);
		} catch (FinderException e) {
			return false;
		}
		
		return card.getIsValid();
	}
	
	public boolean banCard(String serialNumber, boolean ban) {
		Card card = null;
		
		try {
			card = getCardHome().findByPrimaryKey(serialNumber);
		} catch (FinderException e) {
			try {
				card = getCardHome().create();
				card.setCardSerialNumber(serialNumber);
			} catch (CreateException e1) {
				return false;
			}
		}
		card.setIsValid(!ban);
		card.store();
		
		return true;
	}
	
	public boolean addAmountToCardUser(String serialNumber, Date timestamp, double amount, String terminalNumber) {
		return true;
	}
	
	private void addLogEntry(Card card, IWTimestamp stamp, IWTimestamp externalStamp, String action, String value, String terminal, boolean isError, String errorMessage) throws CreateException {
		CardTransactionLog log = getCardTransactionLogHome().create();
		log.setCard(card);
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
		log.store();
	}
	
	private CardHome getCardHome() {
		try {
			return (CardHome) IDOLookup.getHome(Card.class);
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
}