/*
 * $Id: ClubHandler.java,v 1.3 2005/02/07 13:56:06 laddi Exp $
 * Created on 7.2.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.golf.handicap.presentation.handler;

import is.idega.idegaweb.golf.access.AccessControl;
import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.entity.UnionHome;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.business.InputHandler;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.DropdownMenu;


/**
 * Last modified: $Date: 2005/02/07 13:56:06 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.3 $
 */
public class ClubHandler extends DropdownMenu implements InputHandler {

	public void main(IWContext iwc) throws Exception {
		UnionHome home = (UnionHome) IDOLookup.getHomeLegacy(Union.class);

		Union mainUnion = null;
		String unionID = AccessControl.getGolfUnionOfClubAdmin(iwc);
		if (unionID != null) {
			mainUnion = home.findByPrimaryKey(Integer.parseInt(unionID));
		}

		if (mainUnion != null && (mainUnion.getUnionType().equalsIgnoreCase("golf_club") || mainUnion.getUnionType().equalsIgnoreCase("extra_club"))) {
			addMenuElement(mainUnion.getPrimaryKey().toString(), mainUnion.getAbbrevation());
		}
		else {
			Collection unions = home.findAllUnions();
			Iterator iter = unions.iterator();
			while (iter.hasNext()) {
				Union union = (Union) iter.next();
				if (union.getUnionType().equalsIgnoreCase("golf_club")) {
					addMenuElement(union.getPrimaryKey().toString(), union.getAbbrevation());
				}
			}
			addMenuElementFirst("", "");
		}
		
		super.main(iwc);
	}

	/* (non-Javadoc)
	 * @see com.idega.business.InputHandler#getHandlerObject(java.lang.String, java.lang.String, com.idega.presentation.IWContext)
	 */
	public PresentationObject getHandlerObject(String name, String value, IWContext iwc) {
		setName(name);
		if (value != null) {
			setSelectedElement(value);
		}
		
		return this;
	}

	/* (non-Javadoc)
	 * @see com.idega.business.InputHandler#getHandlerObject(java.lang.String, java.util.Collection, com.idega.presentation.IWContext)
	 */
	public PresentationObject getHandlerObject(String name, Collection values, IWContext iwc) {
		String value = (String) Collections.min(values);
		return getHandlerObject(name, value, iwc);
	}

	/* (non-Javadoc)
	 * @see com.idega.business.InputHandler#getResultingObject(java.lang.String[], com.idega.presentation.IWContext)
	 */
	public Object getResultingObject(String[] value, IWContext iwc) throws Exception {
		if (value != null && value.length > 0) {
			UnionHome home = (UnionHome) IDOLookup.getHomeLegacy(Union.class);
			Union union = home.findByPrimaryKey(new Integer(value[0].toString()));
			return union;
		}
		else
			return null;
	}

	/* (non-Javadoc)
	 * @see com.idega.business.InputHandler#getDisplayForResultingObject(java.lang.Object, com.idega.presentation.IWContext)
	 */
	public String getDisplayForResultingObject(Object value, IWContext iwc) {
		if (value != null) {
			if (value instanceof Union) {
				return ((Union) value).getName();
			}
			else {
				UnionHome home = (UnionHome) IDOLookup.getHomeLegacy(Union.class);
				try {
					Union union = home.findByPrimaryKey(new Integer(value.toString()));
					return union.getName();
				}
				catch (FinderException fe) {
					return value.toString();
				}
			}
		}
		else
			return null;
	}

	/* (non-Javadoc)
	 * @see com.idega.business.InputHandler#convertSingleResultingObjectToType(java.lang.Object, java.lang.String)
	 */
	public Object convertSingleResultingObjectToType(Object value, String className) {
		if (value != null && value.toString().length() > 0) {
			UnionHome home = (UnionHome) IDOLookup.getHomeLegacy(Union.class);
			try {
				Union union = home.findByPrimaryKey(new Integer(value.toString()));
				return union;
			}
			catch (FinderException fe) {
				return null;
			}
		}
		else
			return null;
	}
}