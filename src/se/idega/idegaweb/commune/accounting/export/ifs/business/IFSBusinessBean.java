/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.export.ifs.business;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.FinderException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import se.idega.idegaweb.commune.accounting.export.ifs.data.IFSCheckHeader;
import se.idega.idegaweb.commune.accounting.export.ifs.data.IFSCheckHeaderHome;
import se.idega.idegaweb.commune.accounting.export.ifs.data.IFSCheckRecord;
import se.idega.idegaweb.commune.accounting.export.ifs.data.IFSCheckRecordHome;
import se.idega.idegaweb.commune.accounting.export.ifs.data.JournalLog;
import se.idega.idegaweb.commune.accounting.export.ifs.data.JournalLogHome;

import com.idega.block.school.data.SchoolCategory;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * @author palli
 */
public class IFSBusinessBean extends IBOServiceBean implements IFSBusiness {
	/**
	 * Returns all <code>JournalLog</code> data beans from the database.
	 */
	public Collection getJournalLog() {
		Collection col = new Vector();
		try {
			col = ((JournalLogHome) IDOLookup.getHome(JournalLog.class)).findAll();
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}

		return col;
	}

	/**
	 * Returns all <code>JournalLog</code> data beans from the database for the given category.
	 * 
	 * @param category A school category primary key (String)
	 * 
	 * @return A collection of JournalLog beans
	 */
	public Collection getJournalLogBySchoolCategory(String category) {
		Collection col = new Vector();
		try {
			col = ((JournalLogHome) IDOLookup.getHome(JournalLog.class)).findAllBySchoolCategory(category);
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}

		return col;
	}

	/**
	 * Returns all <code>JournalLog</code> data beans from the database for the given category.
	 * 
	 * @param category A school category bean
	 * 
	 * @return A collection of JournalLog beans
	 */
	public Collection getJournalLogBySchoolCategory(SchoolCategory category) {
		Collection col = new Vector();
		try {
			col = ((JournalLogHome) IDOLookup.getHome(JournalLog.class)).findAllBySchoolCategory(category);
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}

		return col;
	}

	/**
	 * Returns the <code>IFSCheckHeader</code> data bean from the database for the given category.
	 * 
	 * @param category A school category primary key (String)
	 * 
	 * @return A IFSCheckHeader bean
	 */
	public IFSCheckHeader getIFSCheckHeaderBySchoolCategory(String category) {
		IFSCheckHeader header = null;
		try {
			header = ((IFSCheckHeaderHome) IDOLookup.getHome(IFSCheckHeader.class)).findBySchoolCategory(category);
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}

		return header;
	}

	/**
	 * Returns the <code>IFSCheckHeader/code> data bean from the database for the given category.
	 * 
	 * @param category A school category bean
	 * 
	 * @return A IFSCheckHeader bean
	 */
	public IFSCheckHeader getIFSCheckHeaderBySchoolCategory(SchoolCategory category) {
		IFSCheckHeader header = null;
		try {
			header = ((IFSCheckHeaderHome) IDOLookup.getHome(IFSCheckHeader.class)).findBySchoolCategory(category);
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}

		return header;
	}

	/**
	 * Returns a collection of <code>IFSCheckRecord</code> data beans from the database for the given header.
	 * 
	 * @param headerId The primary key id for the header the records belong to.
	 * 
	 * @return A Collection of IFSCheckRecord beans
	 */
	public Collection getIFSCheckRecordByHeaderId(int headerId) {
		Collection col = new Vector();
		try {
			col = ((IFSCheckRecordHome) IDOLookup.getHome(IFSCheckRecord.class)).findAllByHeaderId(headerId);
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}

		return col;
	}

	/**
	 * A method to create the files to be sent to the IFS system and updates the status of PaymentHeaders/InvoiceHeaders 
	 * from P to L.
	 * 
	 * @param schoolCategory The string primary key representing the school category we are creating files for.
	 * @param paymentDate The date the payment is supposed to be done on.
	 * @param periodText A text to be inserted on the customers bill/providers payment slip.
	 * 
	 * @author palli
	 */
	public void createFiles(String schoolCategory, String paymentDate, String periodText, User user) {
		UserTransaction trans = null;
		try {
			trans = getSessionContext().getUserTransaction();
			trans.begin();

			IWTimestamp now = IWTimestamp.RightNow();
			JournalLog log = ((JournalLogHome) IDOLookup.getHome(JournalLog.class)).create();
			log.setSchoolCategoryString(schoolCategory);
			log.setEventFileCreated();
			log.setEventDate(now.getTimestamp());
			log.setUser(user);
			log.store();
			
			IFSCheckHeader header = getIFSCheckHeaderBySchoolCategory(schoolCategory);
			if (header == null) {
				header = ((IFSCheckHeaderHome) IDOLookup.getHome(IFSCheckHeader.class)).create();
			}
			else {
				Collection col = this.getIFSCheckRecordByHeaderId(((Integer)header.getPrimaryKey()).intValue());
				if (col != null && !col.isEmpty()) {
					Iterator it = col.iterator();
					while (it.hasNext()) {
						IFSCheckRecord rec = (IFSCheckRecord)it.next();
						rec.remove();
					}
				}
			}
			header.setSchoolCategoryString(schoolCategory);
			header.setStatusFileCreated();
			header.setEventDate(now.getTimestamp());
			header.store();

			trans.commit();
		}
		catch (Exception e) {
			if (trans != null) {
				try {
					trans.rollback();
				}
				catch (SystemException se) {
					se.printStackTrace();
				}
			}
		}
	}

	/**
	 * A method to delete the files created for the IFS system. Also reverts the status of PaymentHeaders/InvoiceHeaders 
	 * to P.
	 * 
	 * @param schoolCategory The string primary key representing the school category we are deleting files for.
	 * 
	 * @author palli
	 */
	public void deleteFiles(String schoolCategory, User user) {
		UserTransaction trans = null;
		try {
			trans = getSessionContext().getUserTransaction();
			trans.begin();

			IWTimestamp now = IWTimestamp.RightNow();
			JournalLog log = ((JournalLogHome) IDOLookup.getHome(JournalLog.class)).create();
			log.setSchoolCategoryString(schoolCategory);
			log.setEventFileDeleted();
			log.setEventDate(now.getTimestamp());
			log.setUser(user);
			log.store();
			
			IFSCheckHeader header = getIFSCheckHeaderBySchoolCategory(schoolCategory);
			if (header != null) {
				Collection col = this.getIFSCheckRecordByHeaderId(((Integer)header.getPrimaryKey()).intValue());
				if (col != null && !col.isEmpty()) {
					Iterator it = col.iterator();
					while (it.hasNext()) {
						IFSCheckRecord rec = (IFSCheckRecord)it.next();
						rec.remove();
					}
				}
			}
			
			trans.commit();
		}
		catch (Exception e) {
			if (trans != null) {
				try {
					trans.rollback();
				}
				catch (SystemException se) {
					se.printStackTrace();
				}
			}
		}
	}

	/**
	 * A method to send the files created to the IFS system. Also updates the status of PaymentHeaders/InvoiceHeaders 
	 * from L to H.
	 * 
	 * @param schoolCategory The string primary key representing the school category we are sending files for.
	 * 
	 * @author palli
	 */
	public void sendFiles(String schoolCategory, User user) {
		UserTransaction trans = null;
		try {
			trans = getSessionContext().getUserTransaction();
			trans.begin();

			IWTimestamp now = IWTimestamp.RightNow();
			JournalLog log = ((JournalLogHome) IDOLookup.getHome(JournalLog.class)).create();
			log.setSchoolCategoryString(schoolCategory);
			log.setEventFileSent();
			log.setEventDate(now.getTimestamp());
			log.setUser(user);
			log.store();
			
			trans.commit();
		}
		catch (Exception e) {
			if (trans != null) {
				try {
					trans.rollback();
				}
				catch (SystemException se) {
					se.printStackTrace();
				}
			}
		}

	}
}