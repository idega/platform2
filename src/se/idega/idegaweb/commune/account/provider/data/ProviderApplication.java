package se.idega.idegaweb.commune.account.provider.data;


public interface ProviderApplication extends com.idega.data.IDOEntity,com.idega.block.process.data.Case,se.idega.idegaweb.commune.account.data.AccountApplication
{
 public void setEmail(java.lang.String p0);
 public java.lang.String getEmail();
 public java.lang.String getEmailAddress() ;
 public void setSchoolArea(int p0);
 public int getNumberOfPlaces() ;
 public java.lang.String getAdditionalInfo() ;
 public void setName(java.lang.String p0) ;
 public void setPostalCode(int p0);
 public void setAdditionalInfo(java.lang.String p0) ;
 public void initializeAttributes();
 public void setPostalCode(com.idega.core.location.data.PostalCode p0);
 public java.lang.String getManagerName() ;
 public java.util.Collection getSchoolTypes() ;
 public java.lang.String getAddress() ;
 public java.lang.String getName() ;
 public com.idega.block.school.data.SchoolArea getSchoolArea() ;
 public void setSchoolTypes(int[] p0) ;
 public void setNumberOfPlaces(int p0) ;
 public com.idega.core.location.data.PostalCode getPostalCode() ;
 public void setApplicantName(java.lang.String p0);
 public void setPhone(java.lang.String p0) ;
 public void addSchoolType(com.idega.block.school.data.SchoolType p0) ;
 public java.lang.String getPhone() ;
 public java.lang.String getCaseCodeDescription() ;
 public void setEmailAddress(java.lang.String p0) ;
 public void setAddress(java.lang.String p0) ;
 public void setSchoolArea(com.idega.block.school.data.SchoolArea p0);
 public void setManagerName(java.lang.String p0) ;
 public void setSchoolTypes(java.util.Collection p0) ;
 public java.lang.String getCaseCodeKey() ;
 public java.lang.String getApplicantName();
}
