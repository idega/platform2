/*
 * $Id$
 *
 * Copyright (C) 2000-2003 Idega Software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega Software.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.atvr.supplier.application.data;

import java.util.Collection;

import com.idega.block.importer.business.NoRecordsException;
import com.idega.block.importer.data.GenericImportFile;
import com.idega.block.importer.data.ImportFile;

/**
 * @author palli
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ProductCategoryImportFile extends GenericImportFile implements ImportFile {

	/* (non-Javadoc)
	 * @see com.idega.block.importer.data.ImportFile#getRecords()
	 */
	public Collection getRecords() throws NoRecordsException {
		return super.getRecords();
	}
}