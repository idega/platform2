package se.idega.idegaweb.commune.complaint.data;


public interface ComplaintHome extends com.idega.data.IDOHome
{
 public Complaint create() throws javax.ejb.CreateException;
 public Complaint findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllComplaints()throws javax.ejb.FinderException;
 public java.util.Collection findAllComplaintsByManager(com.idega.user.data.User p0)throws javax.ejb.FinderException;
 public java.util.Collection findAllComplaintsByStatus(com.idega.block.process.data.CaseStatus p0)throws javax.ejb.FinderException;
 public java.util.Collection findAllComplaintsByType(com.idega.block.process.data.CaseCode p0)throws javax.ejb.FinderException;
 public java.util.Collection findAllComplaintsByUser(com.idega.user.data.User p0)throws javax.ejb.FinderException;

}