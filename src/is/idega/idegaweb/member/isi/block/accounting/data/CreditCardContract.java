package is.idega.idegaweb.member.isi.block.accounting.data;


public interface CreditCardContract extends com.idega.data.IDOEntity
{
 public java.sql.Date getCardExpires();
 public java.lang.String getCardNumber();
 public is.idega.idegaweb.member.isi.block.accounting.data.CreditCardType getCardType();
 public int getCardTypeId();
 public com.idega.user.data.Group getClub();
 public int getClubID();
 public java.lang.String getContractNumber();
 public boolean getDeleted();
 public com.idega.user.data.Group getDivision();
 public int getDivisionId();
 public java.sql.Date getFirstPayment();
 public com.idega.user.data.Group getGroup();
 public int getGroupId();
 public int getNumberOfPayments();
 public com.idega.user.data.User getUser();
 public int getUserId();
 public void setCardExpires(java.sql.Date p0);
 public void setCardNumber(java.lang.String p0);
 public void setCardType(is.idega.idegaweb.member.isi.block.accounting.data.CreditCardType p0);
 public void setCardTypeId(int p0);
 public void setClub(com.idega.user.data.Group p0);
 public void setClubID(int p0);
 public void setContractNumber(java.lang.String p0);
 public void setDeleted(boolean p0);
 public void setDivision(com.idega.user.data.Group p0);
 public void setDivisionId(int p0);
 public void setFirstPayment(java.sql.Date p0);
 public void setGroup(com.idega.user.data.Group p0);
 public void setGroupId(int p0);
 public void setNumberOfPayments(int p0);
 public void setUser(com.idega.user.data.User p0);
 public void setUserId(int p0);
}
