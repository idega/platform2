package com.idega.block.projectmanager.data;


public class ProjectHomeImpl extends com.idega.data.IDOFactory implements ProjectHome
{
 protected Class getEntityInterfaceClass(){
  return Project.class;
 }


 public Project create() throws javax.ejb.CreateException{
  return (Project) super.createIDO();
 }


public java.util.Collection findAllOrderByNumber()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ProjectBMPBean)entity).ejbFindAllOrderByNumber();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public Project findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Project) super.findByPrimaryKeyIDO(pk);
 }



}