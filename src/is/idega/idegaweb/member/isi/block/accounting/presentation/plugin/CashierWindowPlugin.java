/*
 * $Id: CashierWindowPlugin.java,v 1.3 2004/11/27 20:12:47 eiki Exp $
 * Created on Sep 1, 2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.member.isi.block.accounting.presentation.plugin;

import is.idega.idegaweb.member.isi.block.accounting.presentation.CashierWindow;
import java.util.Map;
import com.idega.core.accesscontrol.business.AccessController;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplicationSettings;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.user.app.ToolbarElement;


/**
 * 
 *  Last modified: $Date: 2004/11/27 20:12:47 $ by $Author: eiki $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.3 $
 */
public class CashierWindowPlugin implements ToolbarElement {
	
	private final static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";

	/* (non-Javadoc)
	 * @see com.idega.user.app.ToolbarElement#getButtonImage(com.idega.presentation.IWContext)
	 */
	public Image getButtonImage(IWContext iwc) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.app.ToolbarElement#isButton(com.idega.presentation.IWContext)
	 */
	public boolean isButton(IWContext iwc) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.app.ToolbarElement#getName(com.idega.presentation.IWContext)
	 */
	public String getName(IWContext iwc) {
		IWBundle bundle = iwc.getApplicationContext().getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER);
		IWResourceBundle resourceBundle = bundle.getResourceBundle(iwc);
		return resourceBundle.getLocalizedString("button.cashier", "Cashier");

	}

	/* (non-Javadoc)
	 * @see com.idega.user.app.ToolbarElement#getPresentationObjectClass(com.idega.presentation.IWContext)
	 */
	public Class getPresentationObjectClass(IWContext iwc) {
		return CashierWindow.class;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.app.ToolbarElement#getParameterMap(com.idega.presentation.IWContext)
	 */
	public Map getParameterMap(IWContext iwc) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.app.ToolbarElement#isValid(com.idega.presentation.IWContext)
	 */
	public boolean isValid(IWContext iwc) {
		//only cashiers and admin and role masters can use this plugin
		AccessController security = iwc.getAccessController();
		IWMainApplicationSettings settings = iwc.getApplicationSettings();
		boolean isiPropertyExists = settings.getProperty("temp_show_is_related_stuff") != null;
		boolean isValid = false;
		if(isiPropertyExists && iwc.isSuperAdmin()){
			isValid = true;
		}
		else{
			isValid = (isiPropertyExists) && (security.isRoleMaster(iwc) || security.hasRole(CashierWindow.ROLE_KEY_CASHIER, iwc) || security.hasRole(CashierWindow.ROLE_KEY_CASHIER_ADMIN, iwc));
		}
		
		return isValid;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.app.ToolbarElement#getPriority(com.idega.presentation.IWContext)
	 */
	public int getPriority(IWContext iwc) {
		// TODO Auto-generated method stub
		return 3;
	}
}
