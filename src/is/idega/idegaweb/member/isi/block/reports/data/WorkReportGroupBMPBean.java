/*
 * Created on May 19, 2003
 */
package is.idega.idegaweb.member.isi.block.reports.data;

import java.sql.SQLException;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.core.location.data.PostalCode;
import com.idega.data.GenericEntity;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOEntity;
import com.idega.data.IDOException;
import com.idega.data.IDOQuery;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.user.data.Group;
import com.idega.user.data.GroupType;

/**
 * Description: The list of leagues and their info in the import files<br>
 * Copyright: Idega Software 2003 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */
public class WorkReportGroupBMPBean extends GenericEntity implements WorkReportGroup {
	protected final static String ENTITY_NAME = "ISI_WR_GROUP";
	protected final static String COLUMN_NAME_GROUP_YEAR = "YEAR_OF_REPORT";
	protected final static String COLUMN_NAME_GROUP_NAME = "NAME";
	protected final static String COLUMN_NAME_GROUP_NR= "GROUP_NUMBER";
	protected final static String COLUMN_NAME_GROUP_SHORT_NAME = "SHORT_NAME";
	protected final static String COLUMN_NAME_GROUP_ID = "IC_GROUP_ID";
	protected final static String COLUMN_NAME_GROUP_TYPE = "GROUP_TYPE";
	protected final static String COLUMN_NAME_PERSONAL_ID = "PERSONAL_ID";
	protected final static String COLUMN_NAME_STREET_NAME = "STREET_NAME";
	protected final static String COLUMN_NAME_POSTAL_CODE_ID = "POSTAL_CODE_ID";
	protected final static String COLUMN_NAME_HOME_PHONE = "HOME_PHONE";
	protected final static String COLUMN_NAME_WORK_PHONE = "WORK_PHONE";
	protected final static String COLUMN_NAME_FAX = "FAX";
	protected final static String COLUMN_NAME_EMAIL = "EMAIL";
	
	
	public WorkReportGroupBMPBean() {
		super();
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_NAME_GROUP_YEAR, "Year of the work report",true,true,Integer.class);
		addAttribute(COLUMN_NAME_GROUP_NAME,"Name of group",true,true,String.class);
		addAttribute(COLUMN_NAME_GROUP_SHORT_NAME,"Short name of group",true,true,String.class,30);
		addAttribute(COLUMN_NAME_GROUP_NR,"Nr of group",true,true,String.class,30);
		addAttribute(COLUMN_NAME_GROUP_ID, "Group id",true,true,Integer.class,"many-to-one",Group.class);
		addAttribute(COLUMN_NAME_GROUP_TYPE, "Group type",true,true,String.class,30,"many-to-one",GroupType.class);
		addAttribute(COLUMN_NAME_PERSONAL_ID,"Personal id",true,true,String.class,10);
		addAttribute(COLUMN_NAME_STREET_NAME,"Streetname",true,true,String.class);
		addAttribute(COLUMN_NAME_POSTAL_CODE_ID, "Postal code id",true,true,Integer.class,"many-to-one",PostalCode.class);
		addAttribute(COLUMN_NAME_HOME_PHONE,"Home phone number",true,true,String.class);
		addAttribute(COLUMN_NAME_WORK_PHONE,"Work phone number",true,true,String.class);
		addAttribute(COLUMN_NAME_FAX,"Fax number",true,true,String.class);
		addAttribute(COLUMN_NAME_EMAIL,"Email",true,true,String.class);
	}
	
	public String getEntityName() {
		return ENTITY_NAME;
	}
	
	public Integer getYearOfReport(){
		return getIntegerColumnValue(COLUMN_NAME_GROUP_YEAR);
	}
	

	public void setYearOfReport(int year){
		setColumn(COLUMN_NAME_GROUP_YEAR,year);
	}
	
	public Integer getGroupId(){
		return getIntegerColumnValue(COLUMN_NAME_GROUP_ID);
	}
	
	public void setGroupId(int groupId){
		setColumn(COLUMN_NAME_GROUP_ID,groupId);
	}
	
	public String getGroupType() {
		return getStringColumnValue(COLUMN_NAME_GROUP_TYPE);
	}

	public void setGroupType(String type) {
		setColumn(COLUMN_NAME_GROUP_TYPE,type);
	}
	
	public String getName() {
		return getStringColumnValue(COLUMN_NAME_GROUP_NAME);
	}

	public void setNumber(String number) {
		setColumn(COLUMN_NAME_GROUP_NR,number);
	}
	
	public String getNumber() {
		return getStringColumnValue(COLUMN_NAME_GROUP_NR);
	}

	public void setName(String name) {
		setColumn(COLUMN_NAME_GROUP_NAME,name);
	}
	
	public String getShortName() {
		String shortName = getStringColumnValue(COLUMN_NAME_GROUP_SHORT_NAME);
		return (shortName == null) ? getName() : shortName;
	}
	
	public String getPersonalId() {
		return getStringColumnValue(COLUMN_NAME_PERSONAL_ID);
	}

	public void setPersonalId(String pin) {
		setColumn(COLUMN_NAME_PERSONAL_ID,pin);
	}

	public void setShortName(String name) {
		setColumn(COLUMN_NAME_GROUP_SHORT_NAME,name);
	}
	
	
	
	
	public String getStreetName() {
		return (String) getColumnValue(COLUMN_NAME_STREET_NAME);
	}
	public void setStreetName(String streetName) {
		setColumn(COLUMN_NAME_STREET_NAME, streetName);
	}
	
	public PostalCode getPostalCode() throws SQLException {
		return (PostalCode) getColumnValue(COLUMN_NAME_POSTAL_CODE_ID);
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
	
	public void setHomePhone(String number){
		setColumn(COLUMN_NAME_HOME_PHONE, number);
	}
	
	public String getHomePhone(){
		return getStringColumnValue(COLUMN_NAME_HOME_PHONE);
	}
	
	public void setWorkPhone(String number){
		setColumn(COLUMN_NAME_WORK_PHONE, number);
	}
	
	public String getWorkPhone(){
		return getStringColumnValue(COLUMN_NAME_WORK_PHONE);
	}
	
	public void setFax(String number){
		setColumn(COLUMN_NAME_FAX, number);
	}
	
	public String getFax(){
		return getStringColumnValue(COLUMN_NAME_FAX);
	}
	
	public void setEmail(String email){
		setColumn(COLUMN_NAME_EMAIL, email);
	}
	
	public String getEmail(){
		return getStringColumnValue(COLUMN_NAME_EMAIL);
	}
	
	public Collection getMembers() throws IDOException {
		return idoGetRelatedEntities(WorkReportMember.class);
	}
	
	public void addEntity(IDOEntity entity) throws IDOAddRelationshipException{
		this.idoAddTo(entity);
	}
	
  public void removeEntity(IDOEntity entity) throws IDORemoveRelationshipException {
    this.idoRemoveFrom(entity);
  }
  
  public Integer ejbFindWorkReportGroupByGroupIdAndYear(int groupId, int year) throws FinderException{
		IDOQuery sql = idoQuery();
		
		sql.appendSelectAllFrom(this.getEntityName())
		.appendWhere()
		.append(COLUMN_NAME_GROUP_ID).appendEqualSign().append(groupId)
		.appendAndEquals(COLUMN_NAME_GROUP_YEAR,year);
	
		return (Integer) idoFindOnePKByQuery(sql);
		
	}
	
	public Integer ejbFindWorkReportGroupByShortNameAndYear(String shortName, int year) throws FinderException{
		IDOQuery sql = idoQuery();
		
		sql.appendSelectAllFrom(this.getEntityName())
		.appendWhere()
		.appendEqualsQuoted(COLUMN_NAME_GROUP_SHORT_NAME,shortName)
		.appendAndEquals(COLUMN_NAME_GROUP_YEAR,year);
	
		return (Integer) idoFindOnePKByQuery(sql);
		
	}
	
	public Integer ejbFindWorkReportGroupByNameAndYear(String name, int year) throws FinderException{
		IDOQuery sql = idoQuery();
		
		sql.appendSelectAllFrom(this.getEntityName())
		.appendWhere()
		.appendEqualsQuoted(COLUMN_NAME_GROUP_NAME,name)
		.appendAndEquals(COLUMN_NAME_GROUP_YEAR,year);
	
		return (Integer) idoFindOnePKByQuery(sql);
		
	}
	
	public Collection ejbFindAllWorkReportGroupsByYear(int year) throws FinderException{
		return idoFindAllIDsByColumnOrderedBySQL(COLUMN_NAME_GROUP_YEAR,year);
	}
	
	
	public Collection ejbFindAllWorkReportGroupsByGroupTypeAndYear(String groupType, int year) throws FinderException{
		IDOQuery sql = idoQuery();
		
		sql.appendSelectAllFrom(this.getEntityName())
		.appendWhere()
		.appendEqualsQuoted(COLUMN_NAME_GROUP_TYPE, groupType)
		.appendAndEquals(COLUMN_NAME_GROUP_YEAR,year);
	
		return idoFindIDsBySQL(sql.toString());
	}
	
}
