package com.idega.block.school.data;


public class SchoolSeasonHomeImpl extends com.idega.data.IDOFactory implements SchoolSeasonHome
{
 protected Class getEntityInterfaceClass(){
  return SchoolSeason.class;
 }


 public SchoolSeason create() throws javax.ejb.CreateException{
  return (SchoolSeason) super.createIDO();
 }


 public SchoolSeason findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SchoolSeason) super.findByPrimaryKeyIDO(pk);
 }



}