package se.idega.idegaweb.commune.accounting.regulations.data;


public class VATRegulationHomeImpl extends com.idega.data.IDOFactory implements VATRegulationHome
{
 protected Class getEntityInterfaceClass(){
  return VATRegulation.class;
 }


 public VATRegulation create() throws javax.ejb.CreateException{
  return (VATRegulation) super.createIDO();
 }


public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((VATRegulationBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByPeriod(java.sql.Date p0,java.sql.Date p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((VATRegulationBMPBean)entity).ejbFindByPeriod(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public VATRegulation findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (VATRegulation) super.findByPrimaryKeyIDO(pk);
 }



}