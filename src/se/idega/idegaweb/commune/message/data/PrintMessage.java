package se.idega.idegaweb.commune.message.data;

import com.idega.block.process.message.data.Message;

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
	/**
	 * Returns a reference code for message content used by content generators
	 * @return
	 */
	public String getContentCode();
	public void setMessageData(com.idega.core.file.data.ICFile p0);
 	public void setMessageData(int p0);
	public void setMessageBulkData(com.idega.core.file.data.ICFile p0);
	public void setMessageBulkData(int p0);
 	public int getMessageDataFileID();
	public int getMessageBulkDataFileID();
 	public boolean isPrinted();
}
