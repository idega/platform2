package se.idega.idegaweb.commune.childcare.presentation;

import com.idega.presentation.IWContext;
import com.idega.presentation.ui.Window;

/**
 * @author laddi
 */
public class ChildCareWindow extends Window {

	public ChildCareWindow() {
		this.setWidth(400);
		this.setHeight(350);
		this.setScrollbar(true);
		this.setResizable(true);	
		this.setAllMargins(0);
	}

	/**
	 * @see com.idega.presentation.PresentationObject#main(IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
		add(new ChildCareAdminWindow());
	}
	
}
