package is.idega.idegaweb.member.isi.block.accounting.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;
import com.idega.user.data.Group;
import com.idega.user.data.User;

public class InvoiceReceiverBMPBean extends GenericEntity implements
		InvoiceReceiver {

	protected static final String ENTITY_NAME = "isi_inv_receiver";

	protected static final String COLUMN_USER = "user_id";

	protected static final String COLUMN_INVOICE_RECEIVER = "receiver_id";

	protected static final String COLUMN_CLUB = "club_id";

	protected static final String COLUMN_DIVISION = "division_id";

	protected static final String COLUMN_GROUP = "group_id";

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());

		addManyToOneRelationship(COLUMN_USER, User.class);
		addManyToOneRelationship(COLUMN_INVOICE_RECEIVER, User.class);
		addManyToOneRelationship(COLUMN_CLUB, Group.class);
		addManyToOneRelationship(COLUMN_DIVISION, Group.class);
		addManyToOneRelationship(COLUMN_GROUP, Group.class);
	}

	// getters
	public User getUser() {
		return (User) getColumnValue(COLUMN_USER);
	}

	public User getInvoiceReceiver() {
		return (User) getColumnValue(COLUMN_INVOICE_RECEIVER);
	}

	public Group getClub() {
		return (Group) getColumnValue(COLUMN_CLUB);
	}

	public Group getDivision() {
		return (Group) getColumnValue(COLUMN_DIVISION);
	}

	public Group getGroup() {
		return (Group) getColumnValue(COLUMN_GROUP);
	}

	// setters
	public void setUser(User user) {
		setColumn(COLUMN_USER, user);
	}

	public void setInvoiceReceiver(User invoiceReceiver) {
		setColumn(COLUMN_INVOICE_RECEIVER, invoiceReceiver);
	}

	public void setClub(Group club) {
		setColumn(COLUMN_CLUB, club);
	}

	public void setDivision(Group division) {
		setColumn(COLUMN_DIVISION, division);
	}

	public void setGroup(Group group) {
		setColumn(COLUMN_GROUP, group);
	}

	// ejb
	public Collection ejbFindAll() throws FinderException {
		return super.idoFindAllIDsBySQL();
	}

	public Collection ejbFindByUser(User user) throws FinderException {
		Table receiver = new Table(this);
		SelectQuery query = new SelectQuery(receiver);
		query.addColumn(new WildCardColumn(receiver));
		query.addCriteria(new MatchCriteria(receiver, COLUMN_USER,
				MatchCriteria.EQUALS, user));

		return idoFindPKsByQuery(query);
	}

	public Collection ejbFindByInvoiceReceiver(User invoiceReceiver) throws FinderException {
		Table receiver = new Table(this);
		SelectQuery query = new SelectQuery(receiver);
		query.addColumn(new WildCardColumn(receiver));
		query.addCriteria(new MatchCriteria(receiver, COLUMN_INVOICE_RECEIVER,
				MatchCriteria.EQUALS, invoiceReceiver));

		return idoFindPKsByQuery(query);
	}

	public Collection ejbFindByClub(Group club) throws FinderException {
		Table receiver = new Table(this);
		SelectQuery query = new SelectQuery(receiver);
		query.addColumn(new WildCardColumn(receiver));
		query.addCriteria(new MatchCriteria(receiver, COLUMN_CLUB,
				MatchCriteria.EQUALS, club));

		return idoFindPKsByQuery(query);
	}

	public Collection ejbFindByDivision(Group division) throws FinderException {
		Table receiver = new Table(this);
		SelectQuery query = new SelectQuery(receiver);
		query.addColumn(new WildCardColumn(receiver));
		query.addCriteria(new MatchCriteria(receiver, COLUMN_DIVISION,
				MatchCriteria.EQUALS, division));

		return idoFindPKsByQuery(query);
	}
	
	public Collection ejbFindByGroup(Group group) throws FinderException {
		Table receiver = new Table(this);
		SelectQuery query = new SelectQuery(receiver);
		query.addColumn(new WildCardColumn(receiver));
		query.addCriteria(new MatchCriteria(receiver, COLUMN_GROUP,
				MatchCriteria.EQUALS, group));

		return idoFindPKsByQuery(query);
	}
}