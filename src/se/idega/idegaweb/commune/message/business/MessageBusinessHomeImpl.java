package se.idega.idegaweb.commune.message.business;


public class MessageBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements MessageBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return MessageBusiness.class;
 }


 public MessageBusiness create() throws javax.ejb.CreateException{
  return (MessageBusiness) super.createIBO();
 }



}