package se.idega.idegaweb.commune.accounting.regulations.data;


public class AgeRegulationHomeImpl extends com.idega.data.IDOFactory implements AgeRegulationHome
{
 protected Class getEntityInterfaceClass(){
  return AgeRegulation.class;
 }


 public AgeRegulation create() throws javax.ejb.CreateException{
  return (AgeRegulation) super.createIDO();
 }


public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AgeRegulationBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByCategory(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AgeRegulationBMPBean)entity).ejbFindByCategory(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByPeriod(java.sql.Date p0,java.sql.Date p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AgeRegulationBMPBean)entity).ejbFindByPeriod(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByPeriodAndCategory(java.sql.Date p0,java.sql.Date p1,java.lang.String p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AgeRegulationBMPBean)entity).ejbFindByPeriodAndCategory(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public AgeRegulation findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (AgeRegulation) super.findByPrimaryKeyIDO(pk);
 }



}