package se.agura.applications.vacation.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.GenericEntity;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;

/**
 * @author Anna
 */
public class VacationTimeBMPBean extends GenericEntity implements
        VacationTime {

    public static final String ENTITY_NAME = "vac_vacation_time";

    public static final String COLUMN_VACATION_TIME_ID = "vacation_time_id";

    public static final String COLUMN_VACATION_REQUEST_ID = "vacation_request_id";

    public static final String COLUMN_YEAR = "year";

    public static final String COLUMN_WEEK_NO = "week_no";

    public static final String COLUMN_MONDAY = "monday";

    public static final String COLUMN_TUESDAY = "tuesday";

    public static final String COLUMN_WEDNESDAY = "wednesday";

    public static final String COLUMN_THURSDAY = "thursday";

    public static final String COLUMN_FRIDAY = "friday";

    public static final String COLUMN_SATURDAY = "saturday";

    public static final String COLUMN_SUNDAY = "sunday";

    public String getEntityName() {
        return ENTITY_NAME;
    }

    public void initializeAttributes() {

        addAttribute(COLUMN_VACATION_TIME_ID);
        setAsPrimaryKey(COLUMN_VACATION_TIME_ID, true);

        addAttribute(COLUMN_YEAR, "Year", Integer.class);
        addAttribute(COLUMN_WEEK_NO, "Week no.", Integer.class);
        addAttribute(COLUMN_MONDAY, "Monday", Integer.class);
        addAttribute(COLUMN_TUESDAY, "Tuesday", Integer.class);
        addAttribute(COLUMN_WEDNESDAY, "Wednesday", Integer.class);
        addAttribute(COLUMN_THURSDAY, "Thursday", Integer.class);
        addAttribute(COLUMN_FRIDAY, "Friday", Integer.class);
        addAttribute(COLUMN_SATURDAY, "Saturday", Integer.class);
        addAttribute(COLUMN_SUNDAY, "Sunday", Integer.class);

        addManyToOneRelationship(COLUMN_VACATION_REQUEST_ID,
                VacationRequest.class);
    }

    ///////////////////////////////////////////////////
    //  getters
    ///////////////////////////////////////////////////

    public int getYear() {
        return getIntColumnValue(COLUMN_YEAR);
    }

    public int getWeekNumber() {
        return getIntColumnValue(COLUMN_WEEK_NO);
    }

    public int getMonday() {
        return getIntColumnValue(COLUMN_MONDAY);
    }

    public int getTuesday() {
        return getIntColumnValue(COLUMN_TUESDAY);
    }

    public int getWednesday() {
        return getIntColumnValue(COLUMN_WEDNESDAY);
    }

    public int getThursday() {
        return getIntColumnValue(COLUMN_THURSDAY);
    }

    public int getFriday() {
        return getIntColumnValue(COLUMN_FRIDAY);
    }

    public int getSaturday() {
        return getIntColumnValue(COLUMN_SATURDAY);
    }

    public int getSunday() {
        return getIntColumnValue(COLUMN_SUNDAY);
    }
    
    public VacationRequest getVacationRequest() {
        return (VacationRequest) getColumnValue(COLUMN_VACATION_REQUEST_ID);
    }

    ///////////////////////////////////////////////////
    //  setters
    ///////////////////////////////////////////////////

    public void setYear(int year) {
        setColumn(COLUMN_YEAR, year);
    }

    public void setWeekNumber(int weekNo) {
        setColumn(COLUMN_WEEK_NO, weekNo);
    }

    public void setMonday(int monday) {
        setColumn(COLUMN_MONDAY, monday);
    }

    public void setTuesday(int tuesday) {
        setColumn(COLUMN_TUESDAY, tuesday);
    }

    public void setWednesday(int wednesday) {
        setColumn(COLUMN_WEDNESDAY, wednesday);
    }

    public void setThursday(int thursday) {
        setColumn(COLUMN_THURSDAY, thursday);
    }

    public void setFriday(int friday) {
        setColumn(COLUMN_FRIDAY, friday);
    }

    public void setSaturday(int saturday) {
        setColumn(COLUMN_SATURDAY, saturday);
    }

    public void setSunday(int sunday) {
        setColumn(COLUMN_SUNDAY, sunday);
    }
    
    public void setVacationRequest(VacationRequest vacationRequest) {
        setColumn(COLUMN_VACATION_REQUEST_ID, vacationRequest);
    }
    
    ///////////////////////////////////////////////////
    //  finders
    ///////////////////////////////////////////////////
    
    public Collection ejbFindAllByVacationRequest(VacationRequest request) throws FinderException {
    		Table table = new Table(this);
    		
    		SelectQuery query = new SelectQuery(table);
    		query.addColumn(new WildCardColumn());
    		query.addCriteria(new MatchCriteria(table, COLUMN_VACATION_REQUEST_ID, MatchCriteria.EQUALS, request));
    		
    		return idoFindPKsByQuery(query);
    }
}