package is.idega.idegaweb.travel.business;


public class TravelStockroomBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements TravelStockroomBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return TravelStockroomBusiness.class;
 }


 public TravelStockroomBusiness create() throws javax.ejb.CreateException{
  return (TravelStockroomBusiness) super.createIBO();
 }



}
