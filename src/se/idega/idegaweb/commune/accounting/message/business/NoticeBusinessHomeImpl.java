package se.idega.idegaweb.commune.accounting.message.business;


public class NoticeBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements NoticeBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return NoticeBusiness.class;
 }


 public NoticeBusiness create() throws javax.ejb.CreateException{
  return (NoticeBusiness) super.createIBO();
 }



}