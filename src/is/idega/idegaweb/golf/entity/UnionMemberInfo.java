package is.idega.idegaweb.golf.entity;


public interface UnionMemberInfo extends is.idega.idegaweb.golf.entity.GolfEntity
{
 public is.idega.idegaweb.golf.entity.Card getCard();
 public int getCardId();
 public java.lang.String getComment();
 public is.idega.idegaweb.golf.entity.Family getFamily();
 public int getFamilyId();
 public java.lang.String getFamilyStatus();
 public java.sql.Date getFirstInstallmentDate();
 public java.lang.String getLockerNumber();
 public int getMemberID();
 public int getMemberNumber();
 public java.lang.String getMemberStatus();
 public java.lang.String getMembershipType();
 public int getPaymentTypeID();
 public int getPreferredInstallmentNr();
 public int getPriceCatalogueID();
 public java.sql.Date getRegistrationDate();
 public int getUnionID();
 public boolean getVisible();
 public void setCardId(int p0);
 public void setComment(java.lang.String p0);
 public void setDefaultValues();
 public void setFamily(is.idega.idegaweb.golf.entity.Family p0);
 public void setFamilyId(java.lang.Integer p0);
 public void setFamilyId(int p0);
 public void setFamilyStatus(java.lang.String p0);
 public void setFirstInstallmentDate(java.sql.Date p0);
 public void setLockerNumber(java.lang.String p0);
 public void setMemberID(java.lang.Integer p0);
 public void setMemberID(int p0);
 public void setMemberNumber(int p0);
 public void setMemberNumber(java.lang.Integer p0);
 public void setMemberStatus(char p0);
 public void setMemberStatus(java.lang.Character p0);
 public void setMemberStatus(java.lang.String p0);
 public void setMembershipType(java.lang.String p0);
 public void setPaymentTypeID(int p0);
 public void setPaymentTypeID(java.lang.Integer p0);
 public void setPreferredInstallmentNr(int p0);
 public void setPreferredInstallmentNr(java.lang.Integer p0);
 public void setPriceCatalogueID(int p0);
 public void setPriceCatalogueID(java.lang.Integer p0);
 public void setRegistrationDate(java.sql.Date p0);
 public void setUnionID(int p0);
 public void setUnionID(java.lang.Integer p0);
 public void setVisible(boolean p0);
}
