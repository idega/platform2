package is.idega.idegaweb.campus.data;

import is.idega.idegaweb.campus.block.allocation.data.Contract;

import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.OR;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.user.data.User;

public class ContractRenewalOfferBMPBean extends GenericEntity implements
		ContractRenewalOffer {
	public final static String ENTITY_NAME = "CAM_RENEWAL_OFFER";

	public static final String COLUMN_USER = "user_id";
	public static final String COLUMN_CONTRACT = "contract_id";
	public static final String COLUMN_SENT = "offer_sent";
	public static final String COLUMN_ANSWERED = "date_answered";
	public static final String COLUMN_ANSWER = "answer";
	public static final String COLUMN_CLOSED = "closed";
	public static final String COLUMN_RENEWAL_GRANTED = "renewal_granted";
	public static final String COLUMN_CONTRACT_SENT = "contract_sent";
	

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addManyToOneRelationship(COLUMN_USER, User.class);
		addManyToOneRelationship(COLUMN_CONTRACT, Contract.class);
		addAttribute(COLUMN_SENT, "Date the offer was sent", Timestamp.class);
		addAttribute(COLUMN_ANSWERED, "Date of answer if any", Timestamp.class);
		addAttribute(COLUMN_ANSWER, "What is the answer", Boolean.class);
		addAttribute(COLUMN_CLOSED, "Is the offer closed", Boolean.class);
		addAttribute(COLUMN_RENEWAL_GRANTED, "Is renewal granted", String.class, 1);
		addAttribute(COLUMN_CONTRACT_SENT, "Is the contract sent", Boolean.class);
		addUniqueIDColumn();
	}

	// getters
	public User getUser() {
		return (User) getColumnValue(COLUMN_USER);
	}

	public Contract getContract() {
		return (Contract) getColumnValue(COLUMN_CONTRACT);
	}

	public Timestamp getOfferSentDate() {
		return getTimestampColumnValue(COLUMN_SENT);
	}

	public Timestamp getOfferAnsweredDate() {
		return getTimestampColumnValue(COLUMN_ANSWERED);
	}

	public boolean getAnswer() {
		return getBooleanColumnValue(COLUMN_ANSWER);
	}

	public boolean getIsOfferClosed() {
		return getBooleanColumnValue(COLUMN_CLOSED, false);
	}

	public String getRenewalGranted() {
		return getStringColumnValue(COLUMN_RENEWAL_GRANTED);
	}
	
	public boolean getIsContractSent() {
		return getBooleanColumnValue(COLUMN_CONTRACT_SENT, false);
	}

	// setters
	public void setUser(User user) {
		setColumn(COLUMN_USER, user);
	}

	public void setContract(Contract contract) {
		setColumn(COLUMN_CONTRACT, contract);
	}

	public void setOfferSentDate(Timestamp date) {
		setColumn(COLUMN_SENT, date);
	}

	public void setOfferAnsweredDate(Timestamp date) {
		setColumn(COLUMN_ANSWERED, date);
	}

	public void setAnswer(boolean answer) {
		setColumn(COLUMN_ANSWER, answer);
	}

	public void setIsOfferClosed(boolean closed) {
		setColumn(COLUMN_CLOSED, closed);
	}

	public void setRenewalGranted(String granted) {
		setColumn(COLUMN_RENEWAL_GRANTED, granted);
	}

	public void setIsContractSent(boolean sent) {
		setColumn(COLUMN_CONTRACT_SENT, sent);
	}

	// ejb
	public Collection ejbFindAll() throws FinderException {
		Table table = new Table(this);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(table, getIDColumnName());
		query.addCriteria(new OR(
				new MatchCriteria(table.getColumn(COLUMN_CONTRACT_SENT),
						MatchCriteria.IS, MatchCriteria.NULL),
				new MatchCriteria(table.getColumn(COLUMN_CONTRACT_SENT),
						MatchCriteria.EQUALS, false)));

		return idoFindPKsByQuery(query);
	}

	public Object ejbFindByContract(Contract contract) throws FinderException {
		Table table = new Table(this);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(table, getIDColumnName());
		query.addCriteria(
				new MatchCriteria(table.getColumn(COLUMN_CONTRACT),
						MatchCriteria.EQUALS, contract));

		System.out.println("query = " + query.toString());
		
		return idoFindOnePKByQuery(query);
	}

	public Collection ejbFindAllOpen() throws FinderException {
		Table table = new Table(this);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(table, getIDColumnName());
		query.addCriteria(new OR(
				new MatchCriteria(table.getColumn(COLUMN_CLOSED),
						MatchCriteria.IS, MatchCriteria.NULL),
				new MatchCriteria(table.getColumn(COLUMN_CLOSED),
						MatchCriteria.EQUALS, false)));

		return idoFindPKsByQuery(query);
	}

	public Collection ejbFindAllUnanswered() throws FinderException {
		Table table = new Table(this);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(table, getIDColumnName());
		query.addCriteria(new MatchCriteria(table.getColumn(COLUMN_ANSWERED), MatchCriteria.IS, MatchCriteria.NULL));
		query.addCriteria(new OR(
				new MatchCriteria(table.getColumn(COLUMN_CLOSED),
						MatchCriteria.IS, MatchCriteria.NULL),
				new MatchCriteria(table.getColumn(COLUMN_CLOSED),
						MatchCriteria.EQUALS, false)));

		return idoFindPKsByQuery(query);
	}

	public Object ejbFindByUUID(String uuid, boolean showClosed) throws FinderException {
		Table table = new Table(this);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(table, getIDColumnName());
		query.addCriteria(new MatchCriteria(table.getColumn(this
				.getUniqueIdColumnName()), MatchCriteria.EQUALS, uuid));
		if (!showClosed) {
		query.addCriteria(new OR(
				new MatchCriteria(table.getColumn(COLUMN_CLOSED),
						MatchCriteria.IS, MatchCriteria.NULL),
				new MatchCriteria(table.getColumn(COLUMN_CLOSED),
						MatchCriteria.EQUALS, false)));
		}
		System.out.println("sql = " + query.toString());
		
		return idoFindOnePKByQuery(query);
	}
	
	public Collection ejbFindAllUnsentContracts() throws FinderException {
		Table table = new Table(this);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(table, getIDColumnName());
		query.addCriteria(new MatchCriteria(table.getColumn(COLUMN_RENEWAL_GRANTED), MatchCriteria.EQUALS, "Y"));
		query.addCriteria(new OR(
				new MatchCriteria(table.getColumn(COLUMN_CONTRACT_SENT),
						MatchCriteria.IS, MatchCriteria.NULL),
				new MatchCriteria(table.getColumn(COLUMN_CONTRACT_SENT),
						MatchCriteria.EQUALS, false)));

		return idoFindPKsByQuery(query);
	}

}