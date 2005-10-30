/*
 * $Id: ExtendedDivisionSelectionBox.java,v 1.1 2005/10/30 23:43:09 palli Exp $
 * Created on Feb 10, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.member.isi.block.reports.presentation.inputhandler;


/**
 * 
 *  Last modified: $Date: 2005/10/30 23:43:09 $ by $Author: palli $
 * 
 * @author <a href="mailto:palli@idega.com">palli</a>
 * @version $Revision: 1.1 $
 */
public class ExtendedDivisionSelectionBox extends DivisionSelectionBox {
	private final static String DISPLAY_KEY = "all_divisions";
	private final static String DISPLAY_VALUE = "All divisions";
	
	
	public ExtendedDivisionSelectionBox(String name) {
		super(name);
		initialize();		
	}

	public ExtendedDivisionSelectionBox() {
		super();
		initialize();
	}
	
	private void initialize() {
		setAddAllOptionToList(true);
		setAddAllOptionToListDisplayKey(DISPLAY_KEY);
		setAddAllOptionToListDisplayValue(DISPLAY_VALUE);
	}
}