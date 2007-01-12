package com.idega.block.filter.presentation;

import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObjectContainer;

/**
 * A Block that contains other blocks but dedcides wheter to show them or not. Can be use to display/hide blocks depending on a request parameter or 
 * wheter a user is loged on or not. If none of the properties defined can be used to decide to hide a block, it is shown by default.
 */
public class Filter extends PresentationObjectContainer {
	/** If true, hide contained block only if a user is not loged on. If false, hide contained block only if user is loged. 
	 *  If null show/hide is determined by other means.
	 */
	private Boolean _showOnUserLoged = null;
	
	/** If given request parameter has value <code>true</code> then show contained block, otherwise hide it. 
	 *  If null show/hide is determined by other means. 
	 */
	private String _showParameter = null;
	
	/** Wheter the contents of this Filter is shown or not. */
	private boolean _show = true;
	
	public void _main(IWContext iwc) throws Exception {
		if(!iwc.isInEditMode()) { // always show block if were editing in the builder
			if(this._showOnUserLoged!=null) {
				boolean isLogedOn = iwc.isLoggedOn();
				if(this._showOnUserLoged.booleanValue()) {
					this._show = isLogedOn;
				} else {
					this._show = !isLogedOn;
				}
				System.out.println("show set to " + this._show + ", logedOn is " + isLogedOn);
			}
			if(this._show) { // if it has already been decided to hide block, it must be hidden
				if(this._showParameter!=null) {
					String param = iwc.getParameter(this._showParameter);
					if(param!=null) {
						this._show = "true".equals(param);
						System.out.println("show set to " + this._show + " because show parameter was " + param);
					}
				}
			}
		}
		if(this._show) {
			super._main(iwc);
		}
	}
	
	public void _print(IWContext iwc) throws Exception {
		if(this._show) {
			System.out.println("showing contained block");
			super._print(iwc);
		} else {
			System.out.println("hiding contained block");
		}
	}
	
	public void setShowOnUserLoged(boolean value) {
		this._showOnUserLoged = Boolean.valueOf(value);
	}
	
	public void setShowParameter(String value) {
		this._showParameter = value;
	}

}