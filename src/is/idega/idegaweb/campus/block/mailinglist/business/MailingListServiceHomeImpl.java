package is.idega.idegaweb.campus.block.mailinglist.business;


public class MailingListServiceHomeImpl extends com.idega.business.IBOHomeImpl implements MailingListServiceHome
{
 protected Class getBeanInterfaceClass(){
  return MailingListService.class;
 }


 public MailingListService create() throws javax.ejb.CreateException{
  return (MailingListService) super.createIBO();
 }



}