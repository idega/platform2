/*
 * $Id$
 *
 * Copyright (C) 2000-2003 Idega Software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega Software.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.atvr.supplier.application.business;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.idega.block.importer.business.ImportFileHandler;
import com.idega.block.importer.data.ImportFile;
import com.idega.business.IBOServiceBean;
import com.idega.user.data.Group;
import com.idega.util.IWTimestamp;
import com.idega.util.Timer;

/**
 * @author palli
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ProductCategoryFileImportHandlerBean extends IBOServiceBean implements ImportFileHandler, ProductCategoryFileImportHandler {
	private ImportFile _file;
	//	private UserTransaction transaction;

	private ArrayList _failedRecords = new ArrayList();
	private ArrayList _value;

	private static int COLUMN_ID = 0;
	private static int COLUMN_NAME = 1;
	private static int COLUMN_PARENT = 2;

	/* (non-Javadoc)
	 * @see com.idega.block.importer.business.ImportFileHandler#handleRecords()
	 */
	public boolean handleRecords() throws RemoteException {
		//	UserTransaction transaction = getSessionContext().getUserTransaction();

		Timer clock = new Timer();
		clock.start();

		try {
			//if the transaction failes all the users and their relations are removed
			//		transaction.begin();

			//iterate through the records and process them
			String item;

			int count = 0;
			while (!(item = (String) _file.getNextRecord()).equals("")) {
				count++;

				if (!processRecord(item))
					_failedRecords.add(item);

				if ((count % 100) == 0) {
					System.out.println("ProductCategoryHandler processing RECORD [" + count + "] time: " + IWTimestamp.getTimestampRightNow().toString());
				}

				item = null;
			}

			printFailedRecords();

			clock.stop();
			System.out.println("Time to handleRecords: " + clock.getTime() + " ms  OR " + ((int) (clock.getTime() / 1000)) + " s");

			//		transaction.commit();

			return true;
		}
		catch (Exception ex) {
			ex.printStackTrace();

			//		try {
			//			transaction.rollback();
			//		}
			//		catch (SystemException e) {
			//			e.printStackTrace();
			//		}

			return false;
		}

	}

	private boolean processRecord(String record) throws RemoteException {
		_value = _file.getValuesFromRecordString(record);

		boolean success = storeProductCategoryEntry();

		_value = null;

		return success;
	}

	public void printFailedRecords() {
		System.out.println("Import failed for these records, please fix and import again:");

		Iterator iter = _failedRecords.iterator();
		while (iter.hasNext()) {
			System.out.println((String) iter.next());
		}
	}

	protected boolean storeProductCategoryEntry() throws RemoteException {
		//variables
		String id = getProperty(COLUMN_ID);
		String name = getProperty(COLUMN_NAME);
		String parent = getProperty(COLUMN_PARENT);

		//initialize business beans and data homes           
		NewProductApplicationBusiness newProdBiz = (NewProductApplicationBusiness) getServiceInstance(NewProductApplicationBusiness.class);
		return newProdBiz.storeProductCategory(id,name,parent);
	}

	private String getProperty(int columnIndex) {
		String value = null;

		if (_value != null) {

			try {
				value = (String) _value.get(columnIndex);
			}
			catch (RuntimeException e) {
				return null;
			}
			if (_file.getEmptyValueString().equals(value))
				return null;
			else
				return value;
		}
		else
			return null;
	}

	/* (non-Javadoc)
	 * @see com.idega.block.importer.business.ImportFileHandler#setImportFile(com.idega.block.importer.data.ImportFile)
	 */
	public void setImportFile(ImportFile file) throws RemoteException {
		_file = file;
	}

	/* (non-Javadoc)
	 * @see com.idega.block.importer.business.ImportFileHandler#setRootGroup(com.idega.user.data.Group)
	 */
	public void setRootGroup(Group rootGroup) throws RemoteException {
	}

	/* (non-Javadoc)
	 * @see com.idega.block.importer.business.ImportFileHandler#getFailedRecords()
	 */
	public List getFailedRecords() throws RemoteException {
		return _failedRecords;
	}
}