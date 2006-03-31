package se.idega.idegaweb.commune.accounting.regulations.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOLookup;
import com.idega.data.IDOQuery;

public class CareTimeBMPBean extends GenericEntity implements CareTime {

	protected static final String ENTITY_NAME = "cacc_regulation_care_time";

	protected static final String COLUMN_CARE_TIME_FROM = "care_time_from";
	
	protected static final String COLUMN_CARE_TIME_TO = "care_time_to";
	
	protected static final String COLUMN_DISPLAY = "display_string";
	
	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_CARE_TIME_FROM, "Care time from", Integer.class);
		addAttribute(COLUMN_CARE_TIME_TO, "Care time to", Integer.class);
		addAttribute(COLUMN_DISPLAY, "Display string", String.class, 255);
	}

	public void insertStartData() throws Exception {
		CareTimeHome home = (CareTimeHome) IDOLookup.getHome(CareTime.class);
		CareTime time = home.create();
		//1-25
		time.setCareTimeFrom(1);
		time.setCareTimeTo(25);
		time.setDisplayString("1-25");
		time.store();
		//26-35
		time = home.create();
		time.setCareTimeFrom(26);
		time.setCareTimeTo(35);
		time.setDisplayString("26-35");
		time.store();
		//36-
		time = home.create();
		time.setCareTimeFrom(36);
		time.setDisplayString(">=36");
		time.store();
		//-24
		time = home.create();
		time.setCareTimeTo(24);
		time.setDisplayString("<=24");
		time.store();
		//25-
		time = home.create();
		time.setCareTimeFrom(25);
		time.setDisplayString(">=25");
		time.store();
		//-13
		time = home.create();
		time.setCareTimeTo(13);
		time.setDisplayString("<=13");
		time.store();
		//14-
		time = home.create();
		time.setCareTimeFrom(14);
		time.setDisplayString(">=14");
		time.store();
		//1-15
		time = home.create();
		time.setCareTimeFrom(1);
		time.setCareTimeTo(15);
		time.setDisplayString("1-15");
		time.store();
		//16-25
		time = home.create();
		time.setCareTimeFrom(16);
		time.setCareTimeTo(25);
		time.setDisplayString("16-25");
		time.store();
		//1-
		time = home.create();
		time.setCareTimeFrom(1);
		time.setDisplayString(">=15");
		time.store();
		//1-19
		time = home.create();
		time.setCareTimeFrom(1);
		time.setCareTimeTo(19);
		time.setDisplayString("1-19");
		time.store();
		//20-24
		time = home.create();
		time.setCareTimeFrom(20);
		time.setCareTimeTo(24);
		time.setDisplayString("20-24");
		time.store();
		//25-29
		time = home.create();
		time.setCareTimeFrom(25);
		time.setCareTimeTo(29);
		time.setDisplayString("25-29");
		time.store();
		//30-34
		time = home.create();
		time.setCareTimeFrom(30);
		time.setCareTimeTo(34);
		time.setDisplayString("30-34");
		time.store();
		//35-39
		time = home.create();
		time.setCareTimeFrom(35);
		time.setCareTimeTo(39);
		time.setDisplayString("35-39");
		time.store();
		//40-
		time = home.create();
		time.setCareTimeFrom(40);
		time.setDisplayString(">=40");
		time.store();
	}

	//getters
	public int getCareTimeFrom() {
		return getIntColumnValue(COLUMN_CARE_TIME_FROM, -1);
	}
	
	public int getCareTimeTo() {
		return getIntColumnValue(COLUMN_CARE_TIME_TO, -1);
	}
	
	public String getDisplayString() {
		return getStringColumnValue(COLUMN_DISPLAY);
	}
	
	//setters
	public void setCareTimeFrom(int careTimeFrom) {
		setColumn(COLUMN_CARE_TIME_FROM, careTimeFrom);
	}
	
	public void setCareTimeTo(int careTimeTo) {
		setColumn(COLUMN_CARE_TIME_TO, careTimeTo);
	}
	
	public void setDisplayString(String display) {
		setColumn(COLUMN_DISPLAY, display);
	}
	
	//sql
	public Collection ejbFindAllCareTimeValues() throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		return idoFindPKsBySQL(sql.toString());
	}
}