package se.idega.idegaweb.commune.message.presentation;

import com.idega.presentation.IWContext;
import com.idega.presentation.ui.Window;

/**
 * @author laddi
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class MessageViewerWindow extends Window {

	public MessageViewerWindow() {
		this.setWidth(350);
		this.setHeight(250);
		this.setScrollbar(true);
		this.setResizable(false);
	}

	/**
	 * @see com.idega.presentation.PresentationObject#main(IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
		add(new MessageViewer());
	}
	
}
