package is.idega.idegaweb.member.data;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.FinderException;

import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDORelationshipException;
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
    private static final String COLUMN_APPLICATION_GROUP_ID = "application_group_id";
        
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_USER_COMMENT = "user_comment";
    private static final String COLUMN_ADMIN_COMMENT = "admin_comment";
    private static final String COLUMN_CREATED = "created";
    private static final String COLUMN_MODIFIED = "modified";
    
    private static final String STATUS_PENDING = "pending";
    private static final String STATUS_APPROVED = "approved";
    private static final String STATUS_DENIED= "denied";
            
    
  public final void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute(COLUMN_APPLICATION_GROUP_ID,"Application group",true,true,Integer.class,"many-to-one",Group.class);    
    addAttribute(COLUMN_USER_ID,"User",true,true,Integer.class,"many-to-one",User.class);
    addAttribute(COLUMN_STATUS,"Status", true, true, "java.lang.String",15);    
    addAttribute(COLUMN_USER_COMMENT,"User comment", true, true, "java.lang.String",1000);
    addAttribute(COLUMN_ADMIN_COMMENT,"Admin comment", true, true, "java.lang.String",1000);
    addAttribute(COLUMN_CREATED,"Created",Timestamp.class);
    addAttribute(COLUMN_MODIFIED,"Modified",Timestamp.class);
    
    this.addManyToManyRelationShip(Group.class);
  }



  public final String getEntityName(){
    return "iwme_group_application";
  }




    
  /*  ColumNames end   */


  /*  functions begin   */


  
  public void setUserId(int id){
  	setColumn(COLUMN_USER_ID,id);
  }
  

  public int getUserId(){
          return getIntColumnValue(COLUMN_USER_ID);
  }
  
  public User getUser(){
  	return (User) getColumnValue(COLUMN_USER_ID);
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
    setColumn(COLUMN_CREATED,created);
  }
  
  public Timestamp getModified() {
    return((Timestamp)getColumnValue(COLUMN_MODIFIED));
  }

  public void setModified(Timestamp modified) {
    setColumn(COLUMN_MODIFIED,modified);
  }

  public String ejbHomeGetPendingStatusString() throws RemoteException{
    return STATUS_PENDING;
  }

  public String ejbHomeGetApprovedStatusString() throws RemoteException{
   return STATUS_APPROVED;
  }
  
  public String ejbHomeGetDeniedStatusString() throws RemoteException{
   return STATUS_DENIED;
  }
  
  public void addGroup(Group group) throws IDOAddRelationshipException {
	this.idoAddTo(group);
  }
  
  public void removeAllGroups() throws IDORemoveRelationshipException{
  	this.idoRemoveFrom(Group.class);	
  }
  
  public Collection getGroups(){
   Collection groups = null;
   
   try {
		groups = idoGetRelatedEntities(Group.class);	
	
	} catch (IDORelationshipException e) {
		e.printStackTrace();
	}	
	
	return groups;
  } 
	
  public void addGroups(List groups) throws IDOAddRelationshipException {
  	if(groups!=null){
		for (Iterator iter = groups.iterator(); iter.hasNext();) {
			Group group = (Group) iter.next();
			this.idoAddTo(group);
		}
  	}
  }	

  public Collection ejbFindAllApplicationsByStatus(String status) throws FinderException,RemoteException{
   return  idoFindAllIDsByColumnBySQL(COLUMN_STATUS,status);
  }
  
  public Collection ejbFindAllApplicationsByStatusOrderedByCreationDate(String status) throws FinderException,RemoteException{
   return  idoFindAllIDsByColumnOrderedBySQL(COLUMN_STATUS,status,COLUMN_CREATED);
  }
  
   public Collection ejbFindAllApplicationsByStatusAndApplicationGroup(String status, Group applicationGroup) throws FinderException,RemoteException{
   	StringBuffer sql = new StringBuffer();
   	sql.append("select * from ").append(getEntityName()).append(" where ").append(COLUMN_STATUS)
   	.append("='").append(status).append("' and ").append(COLUMN_APPLICATION_GROUP_ID)
   	.append("=").append( ((Integer)applicationGroup.getPrimaryKey()).intValue() );
  	
  	return this.idoFindPKsBySQL(sql.toString()); 
  }
  
  public Collection ejbFindAllApplicationsByStatusAndApplicationGroupOrderedByCreationDate(String status, Group applicationGroup) throws FinderException,RemoteException{
	StringBuffer sql = new StringBuffer();
   	sql.append("select * from ").append(getEntityName()).append(" where ").append(COLUMN_STATUS)
   	.append("='").append(status).append("' and ").append(COLUMN_APPLICATION_GROUP_ID)
   	.append("=").append( ((Integer)applicationGroup.getPrimaryKey()).intValue() )
   	.append(" order by ").append(COLUMN_CREATED);
  	
  	return this.idoFindPKsBySQL(sql.toString());
  }
  
  public Collection ejbFindAllApplicationsByStatusAndUserOrderedByCreationDate(String status, User user) throws FinderException,RemoteException{
	StringBuffer sql = new StringBuffer();
   	sql.append("select * from ").append(getEntityName()).append(" where ").append(COLUMN_STATUS)
   	.append("='").append(status).append("' and ").append(COLUMN_USER_ID)
   	.append("=").append( ((Integer)user.getPrimaryKey()).intValue() )
   	.append(" order by ").append(COLUMN_CREATED);
  	
  	return this.idoFindPKsBySQL(sql.toString());
  }


  public Collection ejbFindAllApplications()throws FinderException{
    return super.idoFindAllIDsBySQL();
  }

}   

