package is.idega.idegaweb.member.business;


public class KRImportFileHandlerHomeImpl extends com.idega.business.IBOHomeImpl implements KRImportFileHandlerHome
{
 protected Class getBeanInterfaceClass(){
  return KRImportFileHandler.class;
 }


 public KRImportFileHandler create() throws javax.ejb.CreateException{
  return (KRImportFileHandler) super.createIBO();
 }



}