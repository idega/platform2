package se.idega.idegaweb.commune.message.data;

/**
 * Title: PrintMessage.java
 * Description: 
 * Company: idegaweb 
 * @author aron
 * @version 1.0
 * 
 * 
 * 
 */
public interface PrintMessage extends Message {
	public String getPrintType();
	public void setMessageData(com.idega.core.file.data.ICFile p0);
 	public void setMessageData(int p0);
	public void setMessageBulkData(com.idega.core.file.data.ICFile p0);
	public void setMessageBulkData(int p0);
 	public int getMessageDataFileID();
	public int getMessageBulkDataFileID();
 	
}
