/*
 * Created on 21.5.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.block.pki.data;

import java.sql.Date;

/**
 * @author Roar
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class NBSSignedEntity {
	public final static int ACTION_INIT = 1, ACTION_PROCESS = 2, ACTION_END = 3;
	private int _action = ACTION_INIT;
	
	public abstract void setXmlSignedData(String data);
	public abstract void setSignedBy(int userId);
	public abstract void setSignedDate(Date time);
	public abstract void setSignedFlag(boolean flag);
	public abstract void store();
	public abstract String getText();
	
	public int getAction(){
		return _action;	
	}
	
	public void setNextAction(){
		switch(_action){
			case ACTION_INIT:
				_action = ACTION_PROCESS;
				break;
			case ACTION_PROCESS:
				_action = ACTION_END;
				break;
			case ACTION_END:
				_action = ACTION_INIT;
				break;
		}
		System.out.println("Action set to: " + _action);
		
	
	}
}
