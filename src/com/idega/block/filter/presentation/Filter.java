package com.idega.block.filter.presentation;

import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObjectContainer;

/**
 * A Block that contains other blocks but dedcides wheter to show them or not. Can be use to display/hide blocks depending on a request parameter or 
 * wheter a user is loged on or not. If none of the properties defined can be used to decide to hide a block, it is shown by default.
 */
public class Filter extends PresentationObjectContainer {
	/** If true, show contained block only if a user is loged on. If false, show contained block only if no user is loged. 
	 *  If null show/hide is determined by other means.
	 */
	private Boolean _showOnUserLoged = null;
	
	/** If given request parameter has value <code>true</code> then show contained block, if value is <code>false</code> hide it. 
	 *  If null show/hide is determined by other means. 
	 */
	private String _showParameter = null;
	
	/** Wheter the contets of this Filter is shown or not. */
	private boolean _show = true;
	
	public void _main(IWContext iwc) throws Exception {
		if(!iwc.isInEditMode()) { // always show block if were editing in the builder
			if(_showOnUserLoged!=null) {
				boolean isLogedOn = iwc.isLoggedOn();
				if(_showOnUserLoged.booleanValue()) {
					_show = isLogedOn;
				} else {
					_show = !isLogedOn;
				}
				System.out.println("show set to " + _show + ", logedOn is " + isLogedOn);
			}
			if(_show) { // if it has already been decided to hide block, it must be hidden
				if(_showParameter!=null) {
					String param = iwc.getParameter(_showParameter);
					if(param!=null) {
						_show = "true".equals(param);
						System.out.println("show set to " + _show + " because show parameter was " + param);
					}
				}
			}
		}
		if(_show) {
			super._main(iwc);
		}
	}
	
	public void _print(IWContext iwc) throws Exception {
		if(_show) {
			System.out.println("showing contained block");
			super._print(iwc);
		} else {
			System.out.println("hiding contained block");
		}
	}
	
	public void setShowOnUserLoged(boolean value) {
		_showOnUserLoged = Boolean.valueOf(value);
	}
	
	public void setShowParameter(String value) {
		_showParameter = value;
	}

}