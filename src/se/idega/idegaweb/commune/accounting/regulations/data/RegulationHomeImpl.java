package se.idega.idegaweb.commune.accounting.regulations.data;


public class RegulationHomeImpl extends com.idega.data.IDOFactory implements RegulationHome
{
 protected Class getEntityInterfaceClass(){
  return Regulation.class;
 }


 public Regulation create() throws javax.ejb.CreateException{
  return (Regulation) super.createIDO();
 }


public java.util.Collection findAllRegulations()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((RegulationBMPBean)entity).ejbFindAllRegulations();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public Regulation findRegulation(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((RegulationBMPBean)entity).ejbFindRegulation(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public Regulation findRegulationOverlap(java.sql.Date p0,java.sql.Date p1,se.idega.idegaweb.commune.accounting.regulations.data.Regulation p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((RegulationBMPBean)entity).ejbFindRegulationOverlap(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public java.util.Collection findRegulationsByPeriod(java.sql.Date p0,java.sql.Date p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((RegulationBMPBean)entity).ejbFindRegulationsByPeriod(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findRegulationsByPeriod(java.sql.Date p0,java.sql.Date p1,java.lang.String p2,int p3,int p4)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((RegulationBMPBean)entity).ejbFindRegulationsByPeriod(p0,p1,p2,p3,p4);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public Regulation findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Regulation) super.findByPrimaryKeyIDO(pk);
 }



}