package is.idega.idegaweb.member.isi.block.accounting.data;


public class ClubTariffHomeImpl extends com.idega.data.IDOFactory implements ClubTariffHome
{
 protected Class getEntityInterfaceClass(){
  return ClubTariff.class;
 }


 public ClubTariff create() throws javax.ejb.CreateException{
  return (ClubTariff) super.createIDO();
 }


public java.util.Collection findAllByClub(com.idega.user.data.Group p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ClubTariffBMPBean)entity).ejbFindAllByClub(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllByClubAndDivision(com.idega.user.data.Group p0,com.idega.user.data.Group p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ClubTariffBMPBean)entity).ejbFindAllByClubAndDivision(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByGroupAndTariffType(com.idega.user.data.Group p0,is.idega.idegaweb.member.isi.block.accounting.data.ClubTariffType p1,com.idega.util.IWTimestamp p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ClubTariffBMPBean)entity).ejbFindByGroupAndTariffType(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByGroupAndTariffType(com.idega.user.data.Group p0,is.idega.idegaweb.member.isi.block.accounting.data.ClubTariffType p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ClubTariffBMPBean)entity).ejbFindByGroupAndTariffType(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public ClubTariff findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ClubTariff) super.findByPrimaryKeyIDO(pk);
 }



}