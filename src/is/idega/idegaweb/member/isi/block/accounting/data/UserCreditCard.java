package is.idega.idegaweb.member.isi.block.accounting.data;


public interface UserCreditCard extends com.idega.data.IDOEntity
{
 public java.lang.String getCardNumber();
 public com.idega.user.data.User getCardOwner();
 public int getCardOwnerId();
 public is.idega.idegaweb.member.isi.block.accounting.data.CreditCardType getCardType();
 public int getCardTypeId();
 public java.sql.Date getCardValidDate();
 public com.idega.user.data.Group getClub();
 public int getClubID();
 public boolean getDeleted();
 public com.idega.user.data.Group getDivision();
 public int getDivisionID();
 public java.lang.String getExpirationMonth();
 public java.lang.String getExpirationYear();
 public void setCardNumber(java.lang.String p0);
 public void setCardOwner(com.idega.user.data.User p0);
 public void setCardOwnerId(int p0);
 public void setCardType(is.idega.idegaweb.member.isi.block.accounting.data.CreditCardType p0);
 public void setCardTypeId(int p0);
 public void setCardValidDate(java.sql.Date p0);
 public void setClub(com.idega.user.data.Group p0);
 public void setClubID(int p0);
 public void setDeleted(boolean p0);
 public void setDivision(com.idega.user.data.Group p0);
 public void setDivisionID(int p0);
 public void setExpirationMonth(java.lang.String p0);
 public void setExpirationYear(java.lang.String p0);
}
