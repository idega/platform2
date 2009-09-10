package com.idega.block.projectmanager.data;


public class ProjectExtraHomeImpl extends com.idega.data.IDOFactory implements ProjectExtraHome
{
 protected Class getEntityInterfaceClass(){
  return ProjectExtra.class;
 }


 public ProjectExtra create() throws javax.ejb.CreateException{
  return (ProjectExtra) super.createIDO();
 }


 public ProjectExtra findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ProjectExtra) super.findByPrimaryKeyIDO(pk);
 }



}