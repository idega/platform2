/*
 * Created on Sep 17, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package is.idega.idegaweb.member.isi.block.reports.data;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.core.location.data.PostalCode;
import com.idega.data.GenericEntity;
import com.idega.data.IDOException;
import com.idega.data.IDOQuery;
import com.idega.user.data.User;

/**
 * @author palli
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class WorkReportImportMemberBMPBean extends GenericEntity implements WorkReportImportMember {
	protected final static String ENTITY_NAME = "TMP_WR_CM";
	protected final static String COLUMN_NAME_REPORT_ID = "ISI_WORK_REPORT_ID";

	protected final static String COLUMN_NAME_USER_ID = "IC_USER_ID";
	protected final static String COLUMN_NAME_PERSONAL_ID = "PERSONAL_ID";
	protected final static String COLUMN_NAME_NAME = "NAME";
	protected final static String COLUMN_NAME_AGE = "AGE_FOR_YEAR";
	protected final static String COLUMN_NAME_DATE_OF_BIRTH = "DATE_OF_BIRTH";
	protected final static String COLUMN_NAME_GENDER = "GENDER";

	protected final static String COLUMN_NAME_STREET_NAME = "STREET_NAME";
	protected final static String COLUMN_NAME_POSTAL_CODE_ID = "POSTAL_CODE_ID";
	protected final static String COLUMN_NAME_HOME_PHONE = "HOME_PHONE";
	protected final static String COLUMN_NAME_WORK_PHONE = "WORK_PHONE";
	protected final static String COLUMN_NAME_FAX = "FAX";
	protected final static String COLUMN_NAME_EMAIL = "EMAIL";
	;

	protected final static String MALE = "m";
	protected final static String FEMALE = "f";

	public WorkReportImportMemberBMPBean() {
		super();
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_NAME_REPORT_ID, "Id of the work report", true, true, Integer.class, "many-to-one", WorkReport.class);
		addAttribute(COLUMN_NAME_USER_ID, "User id", true, true, Integer.class, "one-to-one", User.class);
		addAttribute(COLUMN_NAME_PERSONAL_ID, "Personal id", true, true, String.class, 10);
		addAttribute(COLUMN_NAME_NAME, "Name from file", true, true, String.class, 180);
		addAttribute(COLUMN_NAME_DATE_OF_BIRTH, "Date of birth", true, true, Timestamp.class);
		addAttribute(COLUMN_NAME_AGE, "The yearly age of the member", true, true, Integer.class);
		addAttribute(COLUMN_NAME_GENDER, "Gender m/f", true, true, String.class, 1);

		addAttribute(COLUMN_NAME_STREET_NAME, "Streetname", true, true, String.class);
		addAttribute(COLUMN_NAME_POSTAL_CODE_ID, "Postal code id", true, true, Integer.class, "many-to-one", PostalCode.class);
		addAttribute(COLUMN_NAME_HOME_PHONE, "Home phone number", true, true, String.class);
		addAttribute(COLUMN_NAME_WORK_PHONE, "Work phone number", true, true, String.class);
		addAttribute(COLUMN_NAME_FAX, "Fax number", true, true, String.class);
		addAttribute(COLUMN_NAME_EMAIL, "Email", true, true, String.class);

		addManyToManyRelationShip(WorkReportGroup.class);
	}
	public String getEntityName() {
		return ENTITY_NAME;
	}

	public int getAge() {
		return getIntColumnValue(COLUMN_NAME_AGE);
	}

	public void setAge(int age) {
		setColumn(COLUMN_NAME_AGE, age);
	}

	public Timestamp getDateOfBirth() {
		return (Timestamp)getColumnValue(COLUMN_NAME_AGE);
	}

	public void setDateOfBirth(Timestamp dateOfBirth) {
		setColumn(COLUMN_NAME_DATE_OF_BIRTH, dateOfBirth);
	}

	public boolean isMale() {
		return MALE.equals(getStringColumnValue(COLUMN_NAME_GENDER));
	}

	public boolean isFemale() {
		return FEMALE.equals(getStringColumnValue(COLUMN_NAME_GENDER));
	}

	public String ejbHomeGetMaleGenderString() {
		return MALE;
	}

	public String ejbHomeGetFemaleGenderString() {
		return FEMALE;
	}

	public void setAsMale() {
		setColumn(COLUMN_NAME_GENDER, MALE);
	}

	public void setAsFemale() {
		setColumn(COLUMN_NAME_GENDER, FEMALE);
	}

	public String getName() {
		return getStringColumnValue(COLUMN_NAME_NAME);
	}

	public void setName(String name) {
		setColumn(COLUMN_NAME_NAME, name);
	}

	public String getPersonalId() {
		return getStringColumnValue(COLUMN_NAME_PERSONAL_ID);
	}

	public void setPersonalId(String pin) {
		setColumn(COLUMN_NAME_PERSONAL_ID, pin);
	}

	public int getReportId() {
		return getIntColumnValue(COLUMN_NAME_REPORT_ID);
	}

	public void setReportId(int reportId) {
		setColumn(COLUMN_NAME_REPORT_ID, reportId);
	}

	public int getUserId() {
		return getIntColumnValue(COLUMN_NAME_USER_ID);
	}

	public void setUserId(int userId) {
		setColumn(COLUMN_NAME_USER_ID, userId);
	}

	public Collection getLeaguesForMember() throws IDOException {
		//could be optimized by only getting league workreportgroups
		return idoGetRelatedEntities(WorkReportGroup.class);
	}

	public String getStreetName() {
		return (String)getColumnValue(COLUMN_NAME_STREET_NAME);
	}
	public void setStreetName(String streetName) {
		setColumn(COLUMN_NAME_STREET_NAME, streetName);
	}

	public PostalCode getPostalCode() throws SQLException {
		return (PostalCode)getColumnValue(COLUMN_NAME_POSTAL_CODE_ID);
	}

	public int getPostalCodeID() {
		return getIntColumnValue(COLUMN_NAME_POSTAL_CODE_ID);
	}

	public void setPostalCode(PostalCode postalCode) {
		setColumn(COLUMN_NAME_POSTAL_CODE_ID, postalCode);
	}
	public void setPostalCodeID(int postal_code_id) {
		setColumn(COLUMN_NAME_POSTAL_CODE_ID, postal_code_id);
	}

	public void setHomePhone(String number) {
		setColumn(COLUMN_NAME_HOME_PHONE, number);
	}

	public String getHomePhone() {
		return getStringColumnValue(COLUMN_NAME_HOME_PHONE);
	}

	public void setWorkPhone(String number) {
		setColumn(COLUMN_NAME_WORK_PHONE, number);
	}

	public String getWorkPhone() {
		return getStringColumnValue(COLUMN_NAME_WORK_PHONE);
	}

	public void setFax(String number) {
		setColumn(COLUMN_NAME_FAX, number);
	}

	public String getFax() {
		return getStringColumnValue(COLUMN_NAME_FAX);
	}

	public void setEmail(String email) {
		setColumn(COLUMN_NAME_EMAIL, email);
	}

	public String getEmail() {
		return getStringColumnValue(COLUMN_NAME_EMAIL);
	}

	public Collection ejbFindAllWorkReportMembersByWorkReportId(int reportId) throws FinderException {
		return idoFindAllIDsByColumnOrderedBySQL(COLUMN_NAME_REPORT_ID, reportId);
	}

	public Integer ejbFindWorkReportMemberByUserIdAndWorkReportId(int userId, int reportId) throws FinderException {
		IDOQuery sql = idoQuery();

		sql.appendSelectAllFrom(this.getEntityName()).appendWhere().append(COLUMN_NAME_USER_ID).appendEqualSign().append(userId).appendAndEquals(COLUMN_NAME_REPORT_ID, reportId);

		return (Integer)idoFindOnePKByQuery(sql);
	}
}