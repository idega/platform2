/*
 * $Id: StudentWindow.java,v 1.1 2005/06/02 06:24:37 laddi Exp $
 * Created on Jun 2, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.adulteducation.presentation;

import com.idega.presentation.IWContext;
import com.idega.presentation.ui.Window;


/**
 * Last modified: $Date: 2005/06/02 06:24:37 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class StudentWindow extends Window {

	public StudentWindow() {
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
		add(new StudentEditor());
	}
	
}