package se.idega.idegaweb.commune.message.data;


public class MessageHandlerInfoHomeImpl extends com.idega.data.IDOFactory implements MessageHandlerInfoHome
{
 protected Class getEntityInterfaceClass(){
  return MessageHandlerInfo.class;
 }


 public MessageHandlerInfo create() throws javax.ejb.CreateException{
  return (MessageHandlerInfo) super.createIDO();
 }


 public MessageHandlerInfo findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (MessageHandlerInfo) super.findByPrimaryKeyIDO(pk);
 }



}