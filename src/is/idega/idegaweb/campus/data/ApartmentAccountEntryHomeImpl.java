package is.idega.idegaweb.campus.data;


public class ApartmentAccountEntryHomeImpl extends com.idega.data.IDOFactory implements ApartmentAccountEntryHome
{
 protected Class getEntityInterfaceClass(){
  return ApartmentAccountEntry.class;
 }


 public ApartmentAccountEntry create() throws javax.ejb.CreateException{
  return (ApartmentAccountEntry) super.createIDO();
 }


 public ApartmentAccountEntry findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ApartmentAccountEntry) super.findByPrimaryKeyIDO(pk);
 }



}