package com.idega.block.school.data;


public class SchoolClassHomeImpl extends com.idega.data.IDOFactory implements SchoolClassHome
{
 protected Class getEntityInterfaceClass(){
  return SchoolClass.class;
 }


 public SchoolClass create() throws javax.ejb.CreateException{
  return (SchoolClass) super.createIDO();
 }


 public SchoolClass findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SchoolClass) super.findByPrimaryKeyIDO(pk);
 }



}