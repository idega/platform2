/*
 * Created on Jan 11, 2004
 *
 */
package se.idega.idegaweb.commune.message.data;

import se.idega.idegaweb.commune.message.business.MessagePdfHandler;
import com.idega.core.component.data.ICObject;
import com.idega.data.GenericEntity;

/**
 * MessageHandlerInfoBMPBean
 * @author aron 
 * @version 1.0
 */
public class MessageHandlerInfoBMPBean extends GenericEntity implements  MessageHandlerInfo{
	
	public final static String MSG_HANDLER ="MSG_LETTER_HANDLER";
	public final static String OBJECT_ID ="OBJECT_ID";
	public final static String HANDLER_CODE ="HANDLER_CODE";
	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	public String getEntityName() {
		return MSG_HANDLER;
	}
	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(HANDLER_CODE,"Code",String.class,20);
		this.setAsPrimaryKey(HANDLER_CODE,true);
		addManyToOneRelationship(OBJECT_ID,ICObject.class);
		
	}
	
	public Class getPrimaryKeyClass(){
		return String.class;
	}

	public String getIDColumnName(){
		return HANDLER_CODE;
	}

	
	public ICObject getICObject(){
		return (ICObject)this.getColumnValue(OBJECT_ID);
	}
	
	public void setICObject(ICObject object){
		setColumn(OBJECT_ID,object);
	}
	
	public String getHandlerCode(){
		return getStringColumnValue(HANDLER_CODE);
	}
	
	public void setHandlerCode(String code){
		this.setColumn(HANDLER_CODE,code);
	}
	
	public MessagePdfHandler getHandler(){
		ICObject obj = getICObject();
		if(obj!=null){
			try {
				return (MessagePdfHandler)obj.getObjectClass().newInstance();
			}
			catch (InstantiationException e) {
				e.printStackTrace();
			}
			catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	

}
