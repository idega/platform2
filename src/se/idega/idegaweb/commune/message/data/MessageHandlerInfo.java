package se.idega.idegaweb.commune.message.data;


public interface MessageHandlerInfo extends com.idega.data.IDOEntity
{
 public se.idega.idegaweb.commune.message.business.MessagePdfHandler getHandler();
 public java.lang.String getHandlerCode();
 public com.idega.core.component.data.ICObject getICObject();
 public java.lang.Class getPrimaryKeyClass();
 public void setHandlerCode(java.lang.String p0);
 public void setICObject(com.idega.core.component.data.ICObject p0);
}
