package is.idega.idegaweb.member.isi.block.accounting.data;


public class CreditCardContractHomeImpl extends com.idega.data.IDOFactory implements CreditCardContractHome
{
 protected Class getEntityInterfaceClass(){
  return CreditCardContract.class;
 }


 public CreditCardContract create() throws javax.ejb.CreateException{
  return (CreditCardContract) super.createIDO();
 }


public java.util.Collection findAllByClub(com.idega.user.data.Group p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((CreditCardContractBMPBean)entity).ejbFindAllByClub(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllByClubAndType(com.idega.user.data.Group p0,is.idega.idegaweb.member.isi.block.accounting.data.CreditCardType p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((CreditCardContractBMPBean)entity).ejbFindAllByClubAndType(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllByClubDivisionAndType(com.idega.user.data.Group p0,com.idega.user.data.Group p1,is.idega.idegaweb.member.isi.block.accounting.data.CreditCardType p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((CreditCardContractBMPBean)entity).ejbFindAllByClubDivisionAndType(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllByClubDivisionGroupAndType(com.idega.user.data.Group p0,com.idega.user.data.Group p1,com.idega.user.data.Group p2,is.idega.idegaweb.member.isi.block.accounting.data.CreditCardType p3)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((CreditCardContractBMPBean)entity).ejbFindAllByClubDivisionGroupAndType(p0,p1,p2,p3);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public CreditCardContract findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (CreditCardContract) super.findByPrimaryKeyIDO(pk);
 }



}