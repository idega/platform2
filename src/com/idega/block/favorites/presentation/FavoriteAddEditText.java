/*
 * $Id: FavoriteAddEditText.java,v 1.1 2004/11/05 13:26:10 laddi Exp $
 * Created on 5.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.favorites.presentation;

import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Text;


/**
 * Last modified: 5.11.2004 11:09:46 by laddi
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class FavoriteAddEditText extends Text {

	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#main(com.idega.presentation.IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
		IWResourceBundle iwrb = iwc.getIWMainApplication().getBundle(FavoriteBlock.IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc);
		
		setText(iwrb.getLocalizedString("add_favorite", "Add favorite"));
		if (iwc.isParameterSet(FavoriteBlock.PARAMETER_ACTION)) {
			String action = iwc.getParameter(FavoriteBlock.PARAMETER_ACTION);
			if (action.equals(FavoriteBlock.ACTION_EDIT)) {
				setText(iwrb.getLocalizedString("edit_favorite", "Edit favorite"));
			}
		}
	}
}
