package is.idega.idegaweb.golf.entity;


public interface MemberOverView extends is.idega.idegaweb.golf.entity.GolfEntity
{
 public com.idega.data.IDOLegacyEntity[] findAll()throws java.sql.SQLException;
 public java.lang.String getFamilyName();
 public java.lang.String getFirstName();
 public float getHandicap();
 public java.lang.String getIDColumnName();
 public java.lang.String getLastName();
 public java.lang.String getMemberStatus();
 public java.lang.String getMiddleName();
 public java.lang.String getName();
 public java.lang.String getSocialSecurityNumber();
 public java.lang.String getUnionName();
}
