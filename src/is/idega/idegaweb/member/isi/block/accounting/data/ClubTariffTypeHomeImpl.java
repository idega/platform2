package is.idega.idegaweb.member.isi.block.accounting.data;


public class ClubTariffTypeHomeImpl extends com.idega.data.IDOFactory implements ClubTariffTypeHome
{
 protected Class getEntityInterfaceClass(){
  return ClubTariffType.class;
 }


 public ClubTariffType create() throws javax.ejb.CreateException{
  return (ClubTariffType) super.createIDO();
 }


public java.util.Collection findAllByClub(com.idega.user.data.Group p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ClubTariffTypeBMPBean)entity).ejbFindAllByClub(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public ClubTariffType findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ClubTariffType) super.findByPrimaryKeyIDO(pk);
 }



}