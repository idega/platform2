package is.idega.idegaweb.member.business;


public class KRClubImportFileHandlerHomeImpl extends com.idega.business.IBOHomeImpl implements KRClubImportFileHandlerHome
{
 protected Class getBeanInterfaceClass(){
  return KRClubImportFileHandler.class;
 }


 public KRClubImportFileHandler create() throws javax.ejb.CreateException{
  return (KRClubImportFileHandler) super.createIBO();
 }


}