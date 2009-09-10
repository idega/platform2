package com.idega.block.school.data;


public class SchoolUserInfoHomeImpl extends com.idega.data.IDOFactory implements SchoolUserInfoHome
{
 protected Class getEntityInterfaceClass(){
  return SchoolUserInfo.class;
 }


 public SchoolUserInfo create() throws javax.ejb.CreateException{
  return (SchoolUserInfo) super.createIDO();
 }


 public SchoolUserInfo findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SchoolUserInfo) super.findByPrimaryKeyIDO(pk);
 }



}