package se.idega.idegaweb.commune.accounting.invoice.data;


public class BatchRunErrorHomeImpl extends com.idega.data.IDOFactory implements BatchRunErrorHome
{
 protected Class getEntityInterfaceClass(){
  return BatchRunError.class;
 }


 public BatchRunError create() throws javax.ejb.CreateException{
  return (BatchRunError) super.createIDO();
 }


 public BatchRunError findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (BatchRunError) super.findByPrimaryKeyIDO(pk);
 }



}