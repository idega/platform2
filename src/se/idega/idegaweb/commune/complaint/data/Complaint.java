package se.idega.idegaweb.commune.complaint.data;


public interface Complaint extends com.idega.data.IDOEntity,com.idega.block.process.data.Case
{
 public java.lang.String getAnswer();
 public java.lang.String getCaseCodeDescription();
 public java.lang.String getCaseCodeKey();
 public java.lang.String[] getCaseStatusDescriptions();
 public java.lang.String[] getCaseStatusKeys();
 public java.lang.String getComplaint();
 public com.idega.block.process.data.CaseCode getComplaintCaseType();
 public java.lang.String getComplaintType();
 public java.lang.String getDescription();
 public int getManagerID();
 public void initializeAttributes();
 public void setAnswer(java.lang.String p0);
 public void setComplaint(java.lang.String p0);
 public void setComplaintType(java.lang.String p0);
 public void setComplaintType(com.idega.block.process.data.CaseCode p0);
 public void setDescription(java.lang.String p0);
 public void setManagerID(int p0);
 public void setManagerID(java.lang.Integer p0);
}
