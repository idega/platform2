package is.idega.block.nationalregister.business;


public class NationalRegisterFileImportHandlerHomeImpl extends com.idega.business.IBOHomeImpl implements NationalRegisterFileImportHandlerHome
{
 protected Class getBeanInterfaceClass(){
  return NationalRegisterFileImportHandler.class;
 }


 public NationalRegisterFileImportHandler create() throws javax.ejb.CreateException{
  return (NationalRegisterFileImportHandler) super.createIBO();
 }



}