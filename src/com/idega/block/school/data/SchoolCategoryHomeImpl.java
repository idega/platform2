package com.idega.block.school.data;


public class SchoolCategoryHomeImpl extends com.idega.data.IDOFactory implements SchoolCategoryHome
{
 protected Class getEntityInterfaceClass(){
  return SchoolCategory.class;
 }


 public SchoolCategory create() throws javax.ejb.CreateException{
  return (SchoolCategory) super.createIDO();
 }


 public SchoolCategory findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SchoolCategory) super.findByPrimaryKeyIDO(pk);
 }



}