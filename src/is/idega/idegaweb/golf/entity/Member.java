package is.idega.idegaweb.golf.entity;


public interface Member extends com.idega.data.IDOLegacyEntity
{
 public is.idega.idegaweb.golf.entity.Address[] getAddress()throws java.sql.SQLException;
 public int getAge();
 public is.idega.idegaweb.golf.entity.Card[] getCards()throws java.sql.SQLException;
 public java.sql.Date getDateOfBirth();
 public java.lang.String getEmail();
 public is.idega.idegaweb.golf.entity.Member[] getFamilyMembers(int p0)throws java.sql.SQLException;
 public java.lang.String getFirstName();
 public java.lang.String getGender();
 public is.idega.idegaweb.golf.entity.Group[] getGroups()throws java.sql.SQLException;
 public float getHandicap()throws java.sql.SQLException;
 public com.idega.user.data.User getICUser();
 public is.idega.idegaweb.golf.block.image.data.ImageEntity getImage();
 public int getImageId();
 public java.lang.String getJob();
 public java.lang.String getLastName();
 public is.idega.idegaweb.golf.block.login.data.LoginType[] getLoginType();
 public is.idega.idegaweb.golf.entity.Union getMainUnion()throws javax.ejb.FinderException,java.sql.SQLException;
 public int getMainUnionID()throws java.sql.SQLException;
 public is.idega.idegaweb.golf.entity.MemberInfo getMemberInfo();
 public java.lang.String getMiddleName();
 public java.lang.String getName();
 public is.idega.idegaweb.golf.entity.Phone[] getPhone()throws java.sql.SQLException;
 public java.lang.String getSSN();
 public is.idega.idegaweb.golf.entity.Scorecard[] getScorecards()throws java.sql.SQLException;
 public java.lang.String getSocialSecurityNumber();
 public is.idega.idegaweb.golf.entity.UnionMemberInfo getUnionMemberInfo(java.lang.String p0)throws java.sql.SQLException;
 public is.idega.idegaweb.golf.entity.UnionMemberInfo getUnionMemberInfo(int p0)throws java.sql.SQLException;
 public is.idega.idegaweb.golf.entity.UnionMemberInfo getUnionMemberInfo(java.lang.String p0,java.lang.String p1)throws java.sql.SQLException;
 public is.idega.idegaweb.golf.entity.Union[] getUnions()throws java.sql.SQLException;
 public java.lang.String getWorkPlace();
 public boolean isMemberIn(is.idega.idegaweb.golf.entity.Union p0)throws java.sql.SQLException;
 public boolean isMemberInUnion(is.idega.idegaweb.golf.entity.Union p0)throws java.sql.SQLException;
 public boolean isMemberInUnion()throws java.sql.SQLException;
 public void setAddress(is.idega.idegaweb.golf.entity.Address p0);
 public void setDateOfBirth(java.sql.Date p0);
 public void setEmail(java.lang.String p0);
 public void setFirstName(java.lang.String p0);
 public void setFullName();
 public void setGender(java.lang.String p0);
 public void setICUser(com.idega.user.data.User p0);
 public void setImageId(int p0);
 public void setImageId(java.lang.Integer p0);
 public void setJob(java.lang.String p0);
 public void setLastName(java.lang.String p0);
 public void setMainUnion(int p0)throws java.sql.SQLException;
 public void setMainUnion(is.idega.idegaweb.golf.entity.Union p0)throws java.sql.SQLException;
 public void setMiddleName(java.lang.String p0);
 public void setSocialSecurityNumber(java.lang.String p0);
 public void setWorkPlace(java.lang.String p0);
 public void setimage_id(java.lang.Integer p0);
 public void setimage_id(int p0);
}
