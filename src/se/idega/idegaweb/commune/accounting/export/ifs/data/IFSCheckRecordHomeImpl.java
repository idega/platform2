package se.idega.idegaweb.commune.accounting.export.ifs.data;


public class IFSCheckRecordHomeImpl extends com.idega.data.IDOFactory implements IFSCheckRecordHome
{
 protected Class getEntityInterfaceClass(){
  return IFSCheckRecord.class;
 }


 public IFSCheckRecord create() throws javax.ejb.CreateException{
  return (IFSCheckRecord) super.createIDO();
 }


public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((IFSCheckRecordBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllByHeader(se.idega.idegaweb.commune.accounting.export.ifs.data.IFSCheckHeader p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((IFSCheckRecordBMPBean)entity).ejbFindAllByHeader(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllByHeaderId(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((IFSCheckRecordBMPBean)entity).ejbFindAllByHeaderId(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public IFSCheckRecord findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (IFSCheckRecord) super.findByPrimaryKeyIDO(pk);
 }



}