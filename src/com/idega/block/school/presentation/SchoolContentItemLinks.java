package com.idega.block.school.presentation;

import java.rmi.RemoteException;

import com.idega.block.boxoffice.presentation.Box;
import com.idega.block.documents.presentation.Doc;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;

/**
 * @author gimmi
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class SchoolContentItemLinks extends SchoolContentItem {

	/**
	 * @see com.idega.block.school.presentation.SchoolContentItem#getObject()
	 */
	protected PresentationObject getObject() throws RemoteException {
		Box doc = new Box(_school.getPrimaryKey().toString() );
		
		Table table = new Table();
		table.setCellpaddingAndCellspacing(0);
		
		table.add(getText("Doc/Box her i toflunni...<br>"));
		table.setBorder(1);
		table.add(doc);
		
		
		return table;
	}

}
