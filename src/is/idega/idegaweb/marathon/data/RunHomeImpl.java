package is.idega.idegaweb.marathon.data;


public class RunHomeImpl extends com.idega.data.IDOFactory implements RunHome
{
 protected Class getEntityInterfaceClass(){
  return Run.class;
 }


 public Run create() throws javax.ejb.CreateException{
  return (Run) super.createIDO();
 }


 public Run findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Run) super.findByPrimaryKeyIDO(pk);
 }



}