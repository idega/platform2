package com.idega.block.school.data;


public class SchoolViewHomeImpl extends com.idega.data.IDOFactory implements SchoolViewHome
{
 protected Class getEntityInterfaceClass(){
  return SchoolView.class;
 }


 public SchoolView create() throws javax.ejb.CreateException{
  return (SchoolView) super.createIDO();
 }


 public SchoolView findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SchoolView) super.findByPrimaryKeyIDO(pk);
 }



}