package se.idega.idegaweb.commune.accounting.regulations.data;


public class RegulationHomeImpl extends com.idega.data.IDOFactory implements RegulationHome
{
 protected Class getEntityInterfaceClass(){
  return Regulation.class;
 }


 public Regulation create() throws javax.ejb.CreateException{
  return (Regulation) super.createIDO();
 }


 public Regulation findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Regulation) super.findByPrimaryKeyIDO(pk);
 }



}