/*
 * Created on 29.7.2003
 */
package is.idega.idegaweb.golf.tournament.data;

import java.sql.Date;

import com.idega.data.GenericEntity;
import com.idega.user.data.Group;

/**
 * @author laddi
 */
public class TournamentBMPBean extends GenericEntity implements Tournament {

	public static final String ENTITY_NAME = "golf_tournament";

	public static final String COLUMN_TOURNAMENT_ID = "tournament_id";
	public static final String COLUMN_NAME = "tournament_name";
	public static final String COLUMN_INFORMATION = "information";

	public static final String COLUMN_NUMBER_OF_HOLES = "number_of_holes";
	public static final String COLUMN_NUMBER_OF_ROUNDS = "number_of_rounds";
	public static final String COLUMN_STARTINGTIME_INTERVAL = "startingtime_interval";
	public static final String COLUMN_NUMBER_IN_GROUP = "number_in_group";

	public static final String COLUMN_VALID = "valid";
	public static final String COLUMN_IS_OPEN = "is_open";
	public static final String COLUMN_ONLINE_REGISTRATION = "online_registration";
	public static final String COLUMN_DIRECT_REGISTRATION = "direct_registration";

	public static final String COLUMN_CREATION_DATE = "creation_date";
	public static final String COLUMN_FIRST_REGISTRATION_DATE = "first_registration_date";
	public static final String COLUMN_LAST_REGISTRATION_DATE = "last_registration_date";
	public static final String COLUMN_START_DATE = "start_date";
	public static final String COLUMN_CLOSING_DATE = "closing_date";

	public static final String COLUMN_CLUB_ID = "club_id";
	public static final String COLUMN_TOURNAMENT_TYPE_ID = "tournament_type_id";
	public static final String COLUMN_TOURNAMENT_FORM_ID = "tournament_form_id";
	
	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(COLUMN_TOURNAMENT_ID);
		setAsPrimaryKey(COLUMN_TOURNAMENT_ID, true);
		
		addAttribute(COLUMN_NAME, "The tournament's name", true, true, String.class, 255);
		addAttribute(COLUMN_INFORMATION, "The tournament's type", true, true, String.class, 4000);

		addAttribute(COLUMN_NUMBER_OF_HOLES, "Number of holes played", true, true, Integer.class);
		addAttribute(COLUMN_NUMBER_OF_ROUNDS, "Number of rounds played", true, true, Integer.class);
		addAttribute(COLUMN_STARTINGTIME_INTERVAL, "Interval between starting times", true, true, Integer.class);
		addAttribute(COLUMN_NUMBER_IN_GROUP, "Number of golfers in startingtime group", true, true, Integer.class);

		addAttribute(COLUMN_VALID, "Is valid", true, true, Boolean.class);
		addAttribute(COLUMN_IS_OPEN, "Is an open tournament", true, true, Boolean.class);
		addAttribute(COLUMN_ONLINE_REGISTRATION, "Has online registration", true, true, Boolean.class);
		addAttribute(COLUMN_DIRECT_REGISTRATION, "Has registration directly to startingtime", true, true, Boolean.class);

		addAttribute(COLUMN_CREATION_DATE, "Creation date", true, true, Date.class);
		addAttribute(COLUMN_FIRST_REGISTRATION_DATE, "First registration date", true, true, Date.class);
		addAttribute(COLUMN_LAST_REGISTRATION_DATE, "Last registration date", true, true, Date.class);
		addAttribute(COLUMN_START_DATE, "Start date", true, true, Date.class);
		addAttribute(COLUMN_CLOSING_DATE, "Closed date", true, true, Date.class);
		
		addOneToOneRelationship(COLUMN_CLUB_ID, Group.class);
		//addOneToOneRelationship(COLUMN_TOURNAMENT_TYPE_ID, TournamentType.class);
		//addOneToOneRelationship(COLUMN_TOURNAMENT_FORM_ID, TournamentForm.class);
	}

	public void setTournamentName(String name) {
		setColumn(COLUMN_NAME, name);
	}
}