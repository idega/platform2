package is.idega.idegaweb.member.isi.block.accounting.data;


public interface CreditCardContract extends com.idega.data.IDOEntity
{
 public is.idega.idegaweb.member.isi.block.accounting.data.CreditCardType getCardType();
 public int getCardTypeId();
 public com.idega.user.data.Group getClub();
 public int getClubID();
 public java.lang.String getContractNumber();
 public com.idega.user.data.Group getDivision();
 public int getDivisionId();
 public void initializeAttributes();
 public void setCardType(is.idega.idegaweb.member.isi.block.accounting.data.CreditCardType p0);
 public void setCardTypeId(int p0);
 public void setClub(com.idega.user.data.Group p0);
 public void setClubID(int p0);
 public void setContractNumber(java.lang.String p0);
 public void setDivision(com.idega.user.data.Group p0);
 public void setDivisionId(int p0);
}
