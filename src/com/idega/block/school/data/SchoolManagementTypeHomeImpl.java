package com.idega.block.school.data;


public class SchoolManagementTypeHomeImpl extends com.idega.data.IDOFactory implements SchoolManagementTypeHome
{
 protected Class getEntityInterfaceClass(){
  return SchoolManagementType.class;
 }


 public SchoolManagementType create() throws javax.ejb.CreateException{
  return (SchoolManagementType) super.createIDO();
 }


public java.util.Collection findAllManagementTypes()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolManagementTypeBMPBean)entity).ejbFindAllManagementTypes();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public SchoolManagementType findCommuneManagementType()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((SchoolManagementTypeBMPBean)entity).ejbFindCommuneManagementType();
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public SchoolManagementType findCompanyManagementType()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((SchoolManagementTypeBMPBean)entity).ejbFindCompanyManagementType();
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public SchoolManagementType findCooperativeCommuneLiabilityManagementType()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((SchoolManagementTypeBMPBean)entity).ejbFindCooperativeCommuneLiabilityManagementType();
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public SchoolManagementType findCountyCouncilManagementType()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((SchoolManagementTypeBMPBean)entity).ejbFindCountyCouncilManagementType();
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public SchoolManagementType findFoundationManagementType()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((SchoolManagementTypeBMPBean)entity).ejbFindFoundationManagementType();
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public SchoolManagementType findGovernmentManagementType()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((SchoolManagementTypeBMPBean)entity).ejbFindGovernmentManagementType();
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public SchoolManagementType findManagementType(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((SchoolManagementTypeBMPBean)entity).ejbFindManagementType(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public java.util.Collection findManagementTypesByCategories(java.lang.String[] p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolManagementTypeBMPBean)entity).ejbFindManagementTypesByCategories(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public SchoolManagementType findPrivateManagementType()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((SchoolManagementTypeBMPBean)entity).ejbFindPrivateManagementType();
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public SchoolManagementType findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SchoolManagementType) super.findByPrimaryKeyIDO(pk);
 }



}