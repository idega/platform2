package com.idega.block.school.data;


public class SchoolUserPaymentHistoryHomeImpl extends com.idega.data.IDOFactory implements SchoolUserPaymentHistoryHome
{
 protected Class getEntityInterfaceClass(){
  return SchoolUserPaymentHistory.class;
 }


 public SchoolUserPaymentHistory create() throws javax.ejb.CreateException{
  return (SchoolUserPaymentHistory) super.createIDO();
 }


public java.util.Collection findAllBySchoolUserPaymentID(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolUserPaymentHistoryBMPBean)entity).ejbFindAllBySchoolUserPaymentID(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public SchoolUserPaymentHistory findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SchoolUserPaymentHistory) super.findByPrimaryKeyIDO(pk);
 }


public java.lang.Object makeHistoryFromSchoolUserPaymentID(int p0)throws com.idega.data.IDOLookupException,javax.ejb.FinderException,javax.ejb.CreateException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.lang.Object theReturn = ((SchoolUserPaymentHistoryBMPBean)entity).ejbHomeMakeHistoryFromSchoolUserPaymentID(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}