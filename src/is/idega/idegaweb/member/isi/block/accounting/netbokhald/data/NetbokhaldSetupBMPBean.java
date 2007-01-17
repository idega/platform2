package is.idega.idegaweb.member.isi.block.accounting.netbokhald.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;
import com.idega.user.data.Group;

public class NetbokhaldSetupBMPBean extends GenericEntity implements
		NetbokhaldSetup {

	protected final static String ENTITY_NAME = "nb_setup";
	
	protected final static String COLUMN_EXTERNAL_ID = "external_id";
	
	protected final static String COLUMN_CLUB_ID = "club_id";
	
	protected final static String COLUMN_DIVISION_ID = "division_id";
	
	protected final static String COLUMN_GROUP_ID = "group_id";
	
	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(COLUMN_EXTERNAL_ID, "ID from Netbokhald", String.class, 255);
		setAsPrimaryKey(COLUMN_EXTERNAL_ID, true);
		addManyToOneRelationship(COLUMN_CLUB_ID, Group.class);
		addManyToOneRelationship(COLUMN_DIVISION_ID, Group.class);
		addManyToOneRelationship(COLUMN_GROUP_ID, Group.class);
	}
	
	public Class getPrimaryKeyClass() {
		return String.class;
	}

	public String getIDColumnName() {
		return COLUMN_EXTERNAL_ID;
	}
	
	//getters
	public String getExternalID() {
		return getStringColumnValue(COLUMN_EXTERNAL_ID);
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
	
	//setters
	public void setExternalID(String id) {
		setColumn(COLUMN_EXTERNAL_ID, id);
	}
	
	public void setClub(Group club) {
		setColumn(COLUMN_CLUB_ID, club);
	}
	
	public void setDivision(Group division) {
		setColumn(COLUMN_DIVISION_ID, division);
	}
	
	public void setGroup(Group group) {
		setColumn(COLUMN_GROUP_ID, group);
	}
	
	//ejb
	public Collection ejbFindAllByClub(Group club) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);
		query.appendWhereEquals(COLUMN_CLUB_ID, club);
		
		return idoFindPKsByQuery(query);
	}
}