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
	
	public CommuneForumTopicWindow(){
		this.setWidth(400);
		this.setHeight(350);
		this.setScrollbar(true);
		this.setResizable(true);	
		this.setAllMargins(0);
	}
	
	public void main(IWContext iwc) throws Exception {
		add(new CommuneForumTopicEditor());
	}
}
