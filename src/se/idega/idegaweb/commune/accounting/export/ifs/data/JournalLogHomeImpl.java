package se.idega.idegaweb.commune.accounting.export.ifs.data;


public class JournalLogHomeImpl extends com.idega.data.IDOFactory implements JournalLogHome
{
 protected Class getEntityInterfaceClass(){
  return JournalLog.class;
 }


 public JournalLog create() throws javax.ejb.CreateException{
  return (JournalLog) super.createIDO();
 }


public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((JournalLogBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllBySchoolCategory(com.idega.block.school.data.SchoolCategory p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((JournalLogBMPBean)entity).ejbFindAllBySchoolCategory(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllBySchoolCategory(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((JournalLogBMPBean)entity).ejbFindAllBySchoolCategory(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public JournalLog findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (JournalLog) super.findByPrimaryKeyIDO(pk);
 }



}