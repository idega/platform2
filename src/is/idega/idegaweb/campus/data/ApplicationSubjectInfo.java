package is.idega.idegaweb.campus.data;


public interface ApplicationSubjectInfo extends com.idega.data.IDOLegacyEntity
{
 public java.sql.Timestamp getFirstChange();
 public java.sql.Timestamp getFirstSubmission() ;
 public java.sql.Timestamp getLastSubmission();
 public int getNumber() ;
 public java.lang.String getSubjectName() ;
 public java.sql.Timestamp getLastChange() ;
 public java.lang.String getStatus() ;
 public void delete() throws java.sql.SQLException;
 public int getSubjectId() ;
}
