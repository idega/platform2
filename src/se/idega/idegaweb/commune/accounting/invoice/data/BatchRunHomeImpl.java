package se.idega.idegaweb.commune.accounting.invoice.data;


public class BatchRunHomeImpl extends com.idega.data.IDOFactory implements BatchRunHome
{
 protected Class getEntityInterfaceClass(){
  return BatchRun.class;
 }


 public BatchRun create() throws javax.ejb.CreateException{
  return (BatchRun) super.createIDO();
 }


 public BatchRun findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (BatchRun) super.findByPrimaryKeyIDO(pk);
 }



}