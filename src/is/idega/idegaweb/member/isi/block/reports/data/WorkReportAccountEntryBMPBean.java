package is.idega.idegaweb.member.isi.block.reports.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.user.data.Group;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Aug 29, 2003
 */
public class WorkReportAccountEntryBMPBean extends GenericEntity {
  
  protected final static String ENTITY_NAME = "ISI_WR_ACCOUNT_ENTRY";
  protected final static String COLUMN_NAME_WR_ID = "WR_ID";
  protected final static String COLUMN_NAME_WR_ACCOUNT_KEY_ID = "WR_ACCOUNT_KEY_ID";
  protected final static String COLUMN_NAME_GROUP_ID = "GROUP_ID";
  protected final static String COLUMN_NAME_AMOUNT = "AMOUNT";
  
   
  public WorkReportAccountEntryBMPBean() {
    super();
  }

  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addManyToOneRelationship(COLUMN_NAME_WR_ID,WorkReport.class);
    addManyToOneRelationship(COLUMN_NAME_WR_ACCOUNT_KEY_ID, WorkReportAccountKey.class);
    addManyToOneRelationship(COLUMN_NAME_GROUP_ID, Group.class);
    addAttribute(COLUMN_NAME_AMOUNT,"amount of the item defined by the account id",true,true, Double.class);
  }
  
  public String getEntityName() {
    return ENTITY_NAME;
  }

  public WorkReport getWorkReport() {
    return (WorkReport) getColumnValue(COLUMN_NAME_WR_ID);
  }
  
  public int getWorkReportID()  {
    return getIntColumnValue(COLUMN_NAME_WR_ID);
  }
  
  public void setWorkReport(WorkReport workReport)  {
    setColumn(COLUMN_NAME_WR_ID, workReport);
  }
  
  public void setWorkReportID(int workReportID) {
    setColumn(COLUMN_NAME_WR_ID, workReportID);
  }
  
  public WorkReportAccountKey getWorkReportAccountKey() {
    return (WorkReportAccountKey) getColumnValue(COLUMN_NAME_WR_ACCOUNT_KEY_ID);
  }

  public int getWorkReportAccountKeyID() {
    return getIntColumnValue(COLUMN_NAME_WR_ACCOUNT_KEY_ID);
  }
  
  public void setWorkReportAccountKey(WorkReportAccountKey workReportAccountKey) {
    setColumn(COLUMN_NAME_WR_ACCOUNT_KEY_ID, workReportAccountKey);
  }
  
  public void setWorkReportAccountKeyID(int workReportAccountKeyID) {
    setColumn(COLUMN_NAME_WR_ACCOUNT_KEY_ID, workReportAccountKeyID);
  }
    
  public Group getGroup() {
    return (Group) getColumnValue(COLUMN_NAME_GROUP_ID);
  }

  public int getGroupID() {
    return getIntColumnValue(COLUMN_NAME_GROUP_ID);
  }
  
  public void setGroup(Group group) {
    setColumn(COLUMN_NAME_GROUP_ID, group);
  }
  
  public void setGroupID(int groupID) {
    setColumn(COLUMN_NAME_GROUP_ID, groupID);
  }

  public Collection ejbFindAllWorkReportAccountEntriesByWorkReportId(int reportId) throws FinderException{
    return idoFindAllIDsByColumnOrderedBySQL(COLUMN_NAME_WR_ID,reportId);
  }


}
