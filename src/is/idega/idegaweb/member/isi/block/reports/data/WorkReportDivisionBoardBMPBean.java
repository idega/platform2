package is.idega.idegaweb.member.isi.block.reports.data;

import java.sql.SQLException;
import java.util.Collection;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.core.data.PostalCode;
import com.idega.data.GenericEntity;
import com.idega.data.IDOException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.user.data.Group;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Jul 23, 2003
 */
public class WorkReportDivisionBoardBMPBean extends GenericEntity {

  protected final static String ENTITY_NAME = "ISI_WR_DIV_BOARD";
  
  // id of the work report to that this instance belongs
  protected final static String COLUMN_NAME_REPORT_ID = "ISI_WORK_REPORT_ID";
  // id of the division group
  protected final static String COLUMN_NAME_GROUP_ID = "IC_GROUP_ID";

  protected final static String COLUMN_NAME_HOME_PAGE = "HOME_PAGE";
  protected final static String COLUMN_NAME_PERSONAL_ID = "PERSONAL_ID";
  protected final static String COLUMN_NAME_STREET_NAME = "STREET_NAME";
  protected final static String COLUMN_NAME_POSTAL_CODE_ID = "POSTAL_CODE_ID";
  protected final static String COLUMN_NAME_FIRST_PHONE = "FIRST_PHONE";
  protected final static String COLUMN_NAME_SECOND_PHONE = "SECOND_PHONE";
  protected final static String COLUMN_NAME_FAX = "FAX";
  protected final static String COLUMN_NAME_EMAIL = "EMAIL";
  
  public WorkReportDivisionBoardBMPBean() {
    super();
  }

  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    
    addAttribute(COLUMN_NAME_REPORT_ID, "Id of the work report",true,true,Integer.class,"many-to-one",WorkReport.class);
    
    addAttribute(COLUMN_NAME_GROUP_ID, "Group id",true,true,Integer.class,"one-to-one",Group.class);
    
    addAttribute(COLUMN_NAME_HOME_PAGE, "Home page",true, true, String.class, 40);
    addAttribute(COLUMN_NAME_PERSONAL_ID,"Personal id",true,true,String.class,10);
    addAttribute(COLUMN_NAME_STREET_NAME,"Streetname",true,true,String.class);
    addAttribute(COLUMN_NAME_POSTAL_CODE_ID, "Postal code id",true,true,Integer.class,"many-to-one",PostalCode.class);
    addAttribute(COLUMN_NAME_FIRST_PHONE,"First phone number",true,true,String.class);
    addAttribute(COLUMN_NAME_SECOND_PHONE,"Second phone number",true,true,String.class);
    addAttribute(COLUMN_NAME_FAX,"Fax number",true,true,String.class);
    addAttribute(COLUMN_NAME_EMAIL,"Email",true,true,String.class);
    
    
    addManyToManyRelationShip(WorkReportGroup.class);
  }
  public String getEntityName() {
    return ENTITY_NAME;
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

  public int getGroupId() {
    return getIntColumnValue(COLUMN_NAME_GROUP_ID);
  }

  public void setGroupId(int groupId) {
    setColumn(COLUMN_NAME_GROUP_ID, groupId);
  }
  
  public Collection getLeaguesForMember() throws IDOException {
    //could be optimized by only getting league workreportgroups
    return idoGetRelatedEntities(WorkReportGroup.class);
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
  

  public void setFirstPhone(String number){
    setColumn(COLUMN_NAME_FIRST_PHONE, number);
  }
  
  public String getFirstPhone(){
    return getStringColumnValue(COLUMN_NAME_FIRST_PHONE);
  }
  
  public void setSecondPhone(String number){
    setColumn(COLUMN_NAME_SECOND_PHONE, number);
  }
  
  public String getSecondPhone(){
    return getStringColumnValue(COLUMN_NAME_SECOND_PHONE);
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
  
  public Collection ejbFindAllWorkReportBoardMembersByWorkReportId(int reportId) throws FinderException{
    return idoFindAllIDsByColumnOrderedBySQL(COLUMN_NAME_REPORT_ID,reportId);
  }
  
  /* (non-Javadoc)
   * @see javax.ejb.EJBLocalObject#remove()
   */
  public void remove() throws RemoveException {
    try {
      idoRemoveFrom(WorkReportGroup.class);
    }
    catch (IDORemoveRelationshipException e) {
      e.printStackTrace();
    }
    super.remove();
  }

}

