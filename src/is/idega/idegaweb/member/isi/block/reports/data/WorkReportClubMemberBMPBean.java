/*
 * Created on May 19, 2003
 */
package is.idega.idegaweb.member.isi.block.reports.data;

import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.core.data.PostalCode;
import com.idega.data.GenericEntity;
import com.idega.data.IDOException;
import com.idega.user.data.User;

/**
 * Description: The list of people in a club for a particular year<br>
 * Copyright: Idega Software 2003 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */
public class WorkReportClubMemberBMPBean extends GenericEntity implements WorkReportClubMember{
	protected final static String ENTITY_NAME = "ISI_WR_CLUB_MEMBERS";
	protected final static String COLUMN_NAME_REPORT_ID = "ISI_WORK_REPORT_ID";
	protected final static String COLUMN_NAME_WORK_REPORT_GROUP = "WR_GROUP_ID";
	protected final static String COLUMN_NAME_USER_ID = "IC_USER_ID";
	protected final static String COLUMN_NAME_PERSONAL_ID = "PERSONAL_ID";
	protected final static String COLUMN_NAME_NAME = "NAME";
	protected final static String COLUMN_NAME_AGE = "AGE_FOR_YEAR";
	protected final static String COLUMN_NAME_DATE_OF_BIRTH = "DATE_OF_BIRTH";
	protected final static String COLUMN_NAME_GENDER = "GENDER";
	protected final static String COLUMN_NAME_STREET_NAME = "STREET_NAME";
	protected final static String COLUMN_NAME_POSTAL_CODE_ID = "POSTAL_CODE_ID";

	
	protected final static String MALE = "m";
	protected final static String FEMALE = "f";

	
	public WorkReportClubMemberBMPBean() {
		super();
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_NAME_REPORT_ID, "Id of the work report",true,true,Integer.class,"many-to-one",WorkReport.class);
		addAttribute(COLUMN_NAME_USER_ID, "User id",true,true,Integer.class,"one-to-one",User.class);
		addAttribute(COLUMN_NAME_PERSONAL_ID,"Personal id",true,true,String.class,10);
		addAttribute(COLUMN_NAME_NAME,"Name from file",true,true,String.class,180);	
		addAttribute(COLUMN_NAME_DATE_OF_BIRTH,"Date of birth",true,true,Timestamp.class);
		addAttribute(COLUMN_NAME_AGE, "The yearly age of the member",true,true,Integer.class);
		addAttribute(COLUMN_NAME_GENDER,"Gender m/f",true,true,String.class,1);
		addAttribute(COLUMN_NAME_WORK_REPORT_GROUP, "The league/division connection, null then use club",true,true,Integer.class,"many-to-many",WorkReportGroup.class);
		addAttribute(COLUMN_NAME_STREET_NAME,"Streetname",true,true,String.class);
		addAttribute(COLUMN_NAME_POSTAL_CODE_ID, "Postal code id",true,true,Integer.class,"many-to-one",PostalCode.class);
		
	}
	public String getEntityName() {
		return ENTITY_NAME;
	}
	
	
	public int getAge() {
		return getIntColumnValue(COLUMN_NAME_AGE);
	}

	public void setAge(int age) {
		setColumn(COLUMN_NAME_AGE,age);
	}

	public Timestamp getDateOfBirth() {
		return (Timestamp) getColumnValue(COLUMN_NAME_AGE);
	}


	public void setDateOfBirth(Timestamp dateOfBirth) {
		setColumn(COLUMN_NAME_DATE_OF_BIRTH,dateOfBirth);
	}

	public boolean isMale() {
		return MALE.equals(getStringColumnValue(COLUMN_NAME_GENDER));
	}
	
	public boolean isFemale() {
		return FEMALE.equals(getStringColumnValue(COLUMN_NAME_GENDER));
	}
	
	public String ejbHomeGetMaleGenderString(){
		return MALE;
	}
	
	public String ejbHomeGetFemaleGenderString(){
		return FEMALE;
	}
		

	public void setAsMale() {
		setColumn(COLUMN_NAME_GENDER,MALE);
	}
	
	public void setAsFemale() {
		setColumn(COLUMN_NAME_GENDER,FEMALE);
	}

	public String getName() {
		return getStringColumnValue(COLUMN_NAME_NAME);
	}

	public void setName(String name) {
		setColumn(COLUMN_NAME_NAME,name);
	}

	public String getPersonalId() {
		return getStringColumnValue(COLUMN_NAME_PERSONAL_ID);
	}

	public void setPersonalId(String pin) {
		setColumn(COLUMN_NAME_PERSONAL_ID,pin);
	}
	
	public int getReportId() {
		return getIntColumnValue(COLUMN_NAME_REPORT_ID);
	}

	public void setReportId(int reportId) {
		setColumn(COLUMN_NAME_REPORT_ID,reportId);
	}

	public int getUserId() {
		return getIntColumnValue(COLUMN_NAME_USER_ID);
	}

	public void setUserId(int userId) {
		setColumn(COLUMN_NAME_USER_ID,userId);
	}
	
	public Collection ejbFindAllClubMembersByWorkReportIdOrderedByMemberName(int reportId) throws FinderException{
		return idoFindAllIDsByColumnOrderedBySQL(COLUMN_NAME_REPORT_ID,reportId,COLUMN_NAME_NAME);
	}
	
	public Collection getLeaguesForMember() throws IDOException {
		return idoGetRelatedEntities(WorkReportGroup.class);
	}
}
