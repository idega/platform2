package is.idega.idegaweb.atvr.supplier.application.business;


public class NewProductApplicationBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements NewProductApplicationBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return NewProductApplicationBusiness.class;
 }


 public NewProductApplicationBusiness create() throws javax.ejb.CreateException{
  return (NewProductApplicationBusiness) super.createIBO();
 }



}