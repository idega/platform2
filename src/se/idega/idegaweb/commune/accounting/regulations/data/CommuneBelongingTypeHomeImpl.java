package se.idega.idegaweb.commune.accounting.regulations.data;


public class CommuneBelongingTypeHomeImpl extends com.idega.data.IDOFactory implements CommuneBelongingTypeHome
{
 protected Class getEntityInterfaceClass(){
  return CommuneBelongingType.class;
 }


 public CommuneBelongingType create() throws javax.ejb.CreateException{
  return (CommuneBelongingType) super.createIDO();
 }


public java.util.Collection findAllCommuneBelongingTypes()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((CommuneBelongingTypeBMPBean)entity).ejbFindAllCommuneBelongingTypes();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public CommuneBelongingType findCommuneBelongingType(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((CommuneBelongingTypeBMPBean)entity).ejbFindCommuneBelongingType(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public CommuneBelongingType findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (CommuneBelongingType) super.findByPrimaryKeyIDO(pk);
 }



}