package is.idega.idegaweb.member.isi.block.accounting.data;

import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;
import com.idega.user.data.Group;
import com.idega.util.IWTimestamp;

public class DiscountEntryBMPBean extends GenericEntity implements
		DiscountEntry {

	protected final static String ENTITY_NAME = "isi_disc_entry"; 
	
	protected final static String COLUMN_FINANCE_ENTRY = "isi_fin_entry_id";
	
	protected final static String COLUMN_MAX_ID_ON_ENTRY = "max_id";
	
	protected final static String COLUMN_CLUB_ID = "club_id";

	protected final static String COLUMN_DIVISION_ID = "division_id";

	protected final static String COLUMN_GROUP_ID = "group_id";
	
	protected final static String COLUMN_DATE_OF_ENTRY = "date_of_entry";

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addOneToOneRelationship(COLUMN_FINANCE_ENTRY, FinanceEntry.class);
		addAttribute(COLUMN_MAX_ID_ON_ENTRY, "Max finance entry id at entrytime", Integer.class);
		addManyToOneRelationship(COLUMN_CLUB_ID, Group.class);
		addManyToOneRelationship(COLUMN_DIVISION_ID, Group.class);
		addManyToOneRelationship(COLUMN_GROUP_ID, Group.class);
		addAttribute(COLUMN_DATE_OF_ENTRY, "Timestamp", true, true, Timestamp.class);
	}
	
	//getters
	public FinanceEntry getFinanceEntry() {
		return (FinanceEntry) getColumnValue(COLUMN_FINANCE_ENTRY);
	}
	
	public int getMaxIdOnEntry() {
		return getIntColumnValue(COLUMN_MAX_ID_ON_ENTRY, 0);
	}
	
	public Group getClub() {
		return (Group) getColumnValue(COLUMN_CLUB_ID);
	}
	
	public Group getDivision() {
		return (Group) getColumnValue(COLUMN_DIVISION_ID);
	}
	
	public Group getGroup() {
		return (Group) getColumnValue(COLUMN_GROUP_ID);
	}
	
	public Timestamp getDateOfEntry() {
		return (Timestamp) getColumnValue(COLUMN_DATE_OF_ENTRY);
	}

	//setter
	public void setFinanceEntry(FinanceEntry entry) {
		setColumn(COLUMN_FINANCE_ENTRY, entry);
	}
	
	public void setMaxIdOnEntry(int id) {
		setColumn(COLUMN_MAX_ID_ON_ENTRY, id);
	}
	
	public void setClub(Group club) {
		setColumn(COLUMN_CLUB_ID, club);
	}
	
	public void setClubID(int id) {
		setColumn(COLUMN_CLUB_ID, id);
	}
	
	public void setDivision(Group division) {
		setColumn(COLUMN_DIVISION_ID, division);
	}
	
	public void setDivisionID(int id) {
		setColumn(COLUMN_DIVISION_ID, id);
	}
	
	public void setGroup(Group group) {
		setColumn(COLUMN_GROUP_ID, group);
	}
	
	public void setGroupID(int id) {
		setColumn(COLUMN_GROUP_ID, id);
	}
	
	public void setDateOfEntry(Timestamp date) {
		setColumn(COLUMN_DATE_OF_ENTRY, date);
	}

	//ejb
	public Collection ejbFindAllByClubAndDivisionAndGroupAndSerial(Group club, Group division, Group group, int fromSerialNumber) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhereEquals(COLUMN_CLUB_ID, club);
		if (division != null) {
			sql.appendAndEquals(COLUMN_DIVISION_ID, division);
		}
		if (group != null) {
			sql.appendAndEquals(COLUMN_GROUP_ID, group);
		}
		
		if (fromSerialNumber > 0) {
			sql.appendAnd();
			sql.append(COLUMN_MAX_ID_ON_ENTRY);
			sql.appendGreaterThanOrEqualsSign();
			sql.append(fromSerialNumber);
		}

		return idoFindPKsByQuery(sql);
	}

	public Collection ejbFindAllByClubAndDivisionAndGroupAndDate(Group club, Group division, Group group, IWTimestamp fromDate) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhereEquals(COLUMN_CLUB_ID, club);
		if (division != null) {
			sql.appendAndEquals(COLUMN_DIVISION_ID, division);
		}
		if (group != null) {
			sql.appendAndEquals(COLUMN_GROUP_ID, group);
		}
		
		if (fromDate != null) {
			sql.appendAnd();
			sql.append(COLUMN_DATE_OF_ENTRY);
			sql.appendGreaterThanSign();
			sql.append(fromDate);
		}

		return idoFindPKsByQuery(sql);
	}
}