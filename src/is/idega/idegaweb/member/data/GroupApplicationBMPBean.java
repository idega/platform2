package is.idega.idegaweb.member.data;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.FinderException;

import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.user.data.Group;
import com.idega.user.data.User;



/**

 * Title: is.idega.idegaweb.member.data.GroupApplicationBMPBean
 * Description: 
 * Copyright: Idega Software (c) 2002
 * Company: Idega Software
 * @author <a href="mailto:eiki@idega.is">Eirikur Hrafnsson</a>
 * @version 1.0
 */



public class GroupApplicationBMPBean extends com.idega.data.GenericEntity implements GroupApplication {


    private static final String COLUMN_USER_ID = "ic_user_id";
    private static final String COLUMN_APPLICATION_GROUP_ID = "ic_group_id";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_USER_COMMENT = "user_comment";
    private static final String COLUMN_ADMIN_COMMENT = "admin_comment";
    private static final String COLUMN_CREATED = "created";
    private static final String COLUMN_MODIFIED = "modified";
    
    private static final String STATUS_PENDING = "pending";
    private static final String STATUS_APPROVED = "approved";
    private static final String STATUS_DENIED= "denied";
            
    
  public final void initializeAttributes(){
    
    addAttribute(getIDColumnName(),"Application group",true,true,Integer.class,"many-to-one",Group.class);    
    addAttribute(COLUMN_USER_ID,"User",true,true,Integer.class,"many-to-one",User.class);
    addAttribute(COLUMN_STATUS,"Status", true, true, "java.lang.String",15);    
    addAttribute(COLUMN_USER_COMMENT,"User comment", true, true, "java.lang.String",1000);
    addAttribute(COLUMN_ADMIN_COMMENT,"Admin comment", true, true, "java.lang.String",1000);
    addAttribute(COLUMN_CREATED,"Created",Timestamp.class);
    addAttribute(COLUMN_MODIFIED,"Modified",Timestamp.class);
    
    this.addManyToManyRelationShip(Group.class);
    
    setAsPrimaryKey(getIDColumnName(),true);
  }



  public final String getEntityName(){
    return "iwme_group_application";
  }



  /*  ColumNames begin   should do this later*/
  public String getIDColumnName(){
  	return COLUMN_APPLICATION_GROUP_ID;
  }
    
  /*  ColumNames end   */


  /*  functions begin   */


  
  public void setUserId(int id){
  	setColumn(COLUMN_USER_ID,id);
  }
  

  public int getUserId(){
          return getIntColumnValue(COLUMN_USER_ID);
  }
  
  public void setApplicationGroupId(int id){
  	setColumn(COLUMN_APPLICATION_GROUP_ID,id);
  }
  

  public int getApplicationGroupId(){
          return getIntColumnValue(COLUMN_APPLICATION_GROUP_ID);
  }
  
  public String getStatus(){
  	return (String) getColumnValue(COLUMN_STATUS);
  }

  public void setStatus(String status){
  	setColumn(COLUMN_STATUS,status);
  }
  
  public String getUserComment(){
          return (String) getColumnValue(COLUMN_USER_COMMENT);
  }

  public void setUserComment(String comment){
          setColumn(COLUMN_USER_COMMENT,comment);
  }
    
  public String getAdminComment(){
          return (String) getColumnValue(COLUMN_ADMIN_COMMENT);
  }

  public void setAdminComment(String comment){
          setColumn(COLUMN_ADMIN_COMMENT,comment);
  }
  public Timestamp getCreated() {
    return((Timestamp)getColumnValue(COLUMN_CREATED));
  }

  public void setCreated(Timestamp created) {
    setColumn(this.COLUMN_CREATED,created);
  }

  public String ejbHomeGetPendingStatusString() throws RemoteException{
    return this.STATUS_PENDING;
  }

  public String ejbHomeGetApprovedStatusString() throws RemoteException{
   return this.STATUS_APPROVED;
  }
  
  public String ejbHomeGetDeniedStatusString() throws RemoteException{
   return this.STATUS_DENIED;
  }
  
  public void addGroup(Group group) throws IDOAddRelationshipException {
	this.idoAddTo(group);
  }
  
  public void removeAllGroups() throws IDORemoveRelationshipException{
  	this.idoRemoveFrom(Group.class);	
  } 
	
  public void addGroups(List groups) throws IDOAddRelationshipException {
	for (Iterator iter = groups.iterator(); iter.hasNext();) {
		Group group = (Group) iter.next();
		this.idoAddTo(group);
	}
  }	

  public Collection ejbFindAllApplicationsByStatus(String status) throws FinderException,RemoteException{
   return  super.idoFindAllIDsByColumnBySQL(COLUMN_STATUS,status);
  }
  
  public Collection ejbFindAllApplicationsByStatusOrderedByCreationDate(String status) throws FinderException,RemoteException{
   return  super.idoFindAllIDsByColumnOrderedBySQL(COLUMN_STATUS,status,COLUMN_CREATED);
  }


  public Collection ejbFindAllApplications()throws FinderException{
    return super.idoFindAllIDsBySQL();
  }

}   

