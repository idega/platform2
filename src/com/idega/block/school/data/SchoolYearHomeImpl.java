package com.idega.block.school.data;


public class SchoolYearHomeImpl extends com.idega.data.IDOFactory implements SchoolYearHome
{
 protected Class getEntityInterfaceClass(){
  return SchoolYear.class;
 }


 public SchoolYear create() throws javax.ejb.CreateException{
  return (SchoolYear) super.createIDO();
 }


 public SchoolYear findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SchoolYear) super.findByPrimaryKeyIDO(pk);
 }



}