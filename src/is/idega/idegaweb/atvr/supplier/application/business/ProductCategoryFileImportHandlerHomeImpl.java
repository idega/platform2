package is.idega.idegaweb.atvr.supplier.application.business;


public class ProductCategoryFileImportHandlerHomeImpl extends com.idega.business.IBOHomeImpl implements ProductCategoryFileImportHandlerHome
{
 protected Class getBeanInterfaceClass(){
  return ProductCategoryFileImportHandler.class;
 }


 public ProductCategoryFileImportHandler create() throws javax.ejb.CreateException{
  return (ProductCategoryFileImportHandler) super.createIBO();
 }



}