package com.idega.block.finance.data;


public class AssessmentRoundHomeImpl extends com.idega.data.IDOFactory implements AssessmentRoundHome
{
 protected Class getEntityInterfaceClass(){
  return AssessmentRound.class;
 }


 public AssessmentRound create() throws javax.ejb.CreateException{
  return (AssessmentRound) super.createIDO();
 }


public java.util.Collection findByCategoryAndTariffGroup(java.lang.Integer categoryId,java.lang.Integer groupId,java.sql.Date from,java.sql.Date to,String status,int resultSize,int startIndex)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AssessmentRoundBMPBean)entity).ejbFindByCategoryAndTariffGroup(categoryId,groupId,from,to,status,resultSize,startIndex);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public AssessmentRound findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (AssessmentRound) super.findByPrimaryKeyIDO(pk);
 }


public int getCountByCategoryAndTariffGroup(java.lang.Integer p0,java.lang.Integer p1,java.sql.Date p2,java.sql.Date p3,String status)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((AssessmentRoundBMPBean)entity).ejbHomeGetCountByCategoryAndTariffGroup(p0,p1,p2,p3,status);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}