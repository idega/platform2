package is.idega.idegaweb.travel.service.business;


public class ProductCategoryFactoryHomeImpl extends com.idega.business.IBOHomeImpl implements ProductCategoryFactoryHome
{
 protected Class getBeanInterfaceClass(){
  return ProductCategoryFactory.class;
 }


 public ProductCategoryFactory create() throws javax.ejb.CreateException{
  return (ProductCategoryFactory) super.createIBO();
 }



}
