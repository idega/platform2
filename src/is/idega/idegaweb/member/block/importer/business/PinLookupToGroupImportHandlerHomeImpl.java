package is.idega.idegaweb.member.block.importer.business;


public class PinLookupToGroupImportHandlerHomeImpl extends com.idega.business.IBOHomeImpl implements PinLookupToGroupImportHandlerHome
{
 protected Class getBeanInterfaceClass(){
  return PinLookupToGroupImportHandler.class;
 }


 public PinLookupToGroupImportHandler create() throws javax.ejb.CreateException{
  return (PinLookupToGroupImportHandler) super.createIBO();
 }



}