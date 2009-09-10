package com.idega.block.survey.data;


public class SurveyEntityHomeImpl extends com.idega.data.IDOFactory implements SurveyEntityHome
{
 protected Class getEntityInterfaceClass(){
  return SurveyEntity.class;
 }


 public SurveyEntity create() throws javax.ejb.CreateException{
  return (SurveyEntity) super.createIDO();
 }


public java.util.Collection findActiveSurveys(com.idega.block.category.data.ICInformationFolder p0,java.sql.Timestamp p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SurveyEntityBMPBean)entity).ejbFindActiveSurveys(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllSurveys(com.idega.block.category.data.ICInformationFolder p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SurveyEntityBMPBean)entity).ejbFindAllSurveys(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public SurveyEntity findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SurveyEntity) super.findByPrimaryKeyIDO(pk);
 }



}