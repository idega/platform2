/*
 * Created on Apr 23, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.idegaweb.commune.block.forum.presentation;

import com.idega.presentation.IWContext;
import com.idega.presentation.ui.Window;
;

/**
 * In this window you should be able to edit the name, description and the invalidation date of the topic (ICCategery)
 */
public class CommuneForumTopicWindow extends Window {
	
	public static String PARAMETER_TOPIC_ID = "cm_forum_t_id";
	
	public CommuneForumTopicWindow(){
		super();
	}
	

	public void main(IWContext iwc) throws Exception {
		
		//get parameter
		//fetch iccategory instance
		//make form with edit fields
		//and save and close button
		//the invalidation date should then lock the topic if the date has been reached
	}
	
}
