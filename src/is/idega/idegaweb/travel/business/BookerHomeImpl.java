package is.idega.idegaweb.travel.business;


public class BookerHomeImpl extends com.idega.business.IBOHomeImpl implements BookerHome
{
 protected Class getBeanInterfaceClass(){
  return Booker.class;
 }


 public Booker create() throws javax.ejb.CreateException{
  return (Booker) super.createIBO();
 }



}