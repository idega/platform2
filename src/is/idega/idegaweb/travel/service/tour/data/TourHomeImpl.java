package is.idega.idegaweb.travel.service.tour.data;


public class TourHomeImpl extends com.idega.data.IDOFactory implements TourHome
{
 protected Class getEntityInterfaceClass(){
  return Tour.class;
 }


 public Tour create() throws javax.ejb.CreateException{
  return (Tour) super.createIDO();
 }


 public Tour findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Tour) super.findByPrimaryKeyIDO(pk);
 }



}