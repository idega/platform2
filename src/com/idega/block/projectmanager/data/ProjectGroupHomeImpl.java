package com.idega.block.projectmanager.data;


public class ProjectGroupHomeImpl extends com.idega.data.IDOFactory implements ProjectGroupHome
{
 protected Class getEntityInterfaceClass(){
  return ProjectGroup.class;
 }


 public ProjectGroup create() throws javax.ejb.CreateException{
  return (ProjectGroup) super.createIDO();
 }


 public ProjectGroup findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ProjectGroup) super.findByPrimaryKeyIDO(pk);
 }



}