package se.idega.idegaweb.commune.message.business;


public class MessageSessionHomeImpl extends com.idega.business.IBOHomeImpl implements MessageSessionHome
{
 protected Class getBeanInterfaceClass(){
  return MessageSession.class;
 }


 public MessageSession create() throws javax.ejb.CreateException{
  return (MessageSession) super.createIBO();
 }



}