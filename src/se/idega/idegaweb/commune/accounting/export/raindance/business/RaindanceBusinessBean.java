package se.idega.idegaweb.commune.accounting.export.raindance.business;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;

import javax.ejb.FinderException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import se.idega.idegaweb.commune.accounting.export.business.ExportBusiness;
import se.idega.idegaweb.commune.accounting.export.business.MoveFileException;
import se.idega.idegaweb.commune.accounting.export.data.ExportDataMapping;
import se.idega.idegaweb.commune.accounting.export.raindance.data.RaindanceCheckHeader;
import se.idega.idegaweb.commune.accounting.export.raindance.data.RaindanceCheckHeaderHome;
import se.idega.idegaweb.commune.accounting.export.raindance.data.RaindanceCheckRecord;
import se.idega.idegaweb.commune.accounting.export.raindance.data.RaindanceCheckRecordHome;
import se.idega.idegaweb.commune.accounting.export.raindance.data.RaindanceJournalLog;
import se.idega.idegaweb.commune.accounting.export.raindance.data.RaindanceJournalLogHome;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeader;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeaderHome;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeader;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderHome;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordHome;
import se.idega.idegaweb.commune.accounting.posting.business.PostingBusiness;
import se.idega.idegaweb.commune.accounting.school.business.ProviderBusiness;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.SchoolCategory;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

public class RaindanceBusinessBean extends IBOServiceBean implements
		RaindanceBusiness {
	/**
	 * Returns all <code>JournalLog</code> data beans from the database.
	 */
	public Collection getJournalLog() {
		Collection col = new Vector();
		try {
			col = ((RaindanceJournalLogHome) IDOLookup.getHome(RaindanceJournalLog.class))
					.findAll();
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}

		return col;
	}

	/**
	 * Returns all <code>JournalLog</code> data beans from the database for
	 * the given category.
	 * 
	 * @param category
	 *            A school category primary key (String)
	 * 
	 * @return A collection of JournalLog beans
	 */
	public Collection getJournalLogBySchoolCategory(String category) {
		Collection col = new Vector();
		try {
			col = ((RaindanceJournalLogHome) IDOLookup.getHome(RaindanceJournalLog.class))
					.findAllBySchoolCategory(category);
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}

		return col;
	}

	/**
	 * Returns all <code>JournalLog</code> data beans from the database for
	 * the given category.
	 * 
	 * @param category
	 *            A school category bean
	 * 
	 * @return A collection of JournalLog beans
	 */
	public Collection getJournalLogBySchoolCategory(SchoolCategory category) {
		Collection col = new Vector();
		try {
			col = ((RaindanceJournalLogHome) IDOLookup.getHome(RaindanceJournalLog.class))
					.findAllBySchoolCategory(category);
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}

		return col;
	}

	/**
	 * Returns the <code>RaindanceCheckHeader</code> data bean from the database for
	 * the given category.
	 * 
	 * @param category
	 *            A school category primary key (String)
	 * 
	 * @return A RaindanceCheckHeader bean
	 */
	public RaindanceCheckHeader getRaindanceCheckHeaderBySchoolCategory(String category) {
		RaindanceCheckHeader header = null;
		try {
			header = ((RaindanceCheckHeaderHome) IDOLookup
					.getHome(RaindanceCheckHeader.class))
					.findBySchoolCategory(category);
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}

		return header;
	}

	/**
	 * Returns the
	 * <code>RaindanceCheckHeader/code> data bean from the database for the given category.
	 * 
	 * @param category A school category bean
	 * 
	 * @return A RaindanceCheckHeader bean
	 */
	public RaindanceCheckHeader getRaindanceCheckHeaderBySchoolCategory(
			SchoolCategory category) {
		RaindanceCheckHeader header = null;
		try {
			header = ((RaindanceCheckHeaderHome) IDOLookup
					.getHome(RaindanceCheckHeader.class))
					.findBySchoolCategory(category);
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}

		return header;
	}

	/**
	 * Returns a collection of <code>RaindanceCheckRecord</code> data beans from the
	 * database for the given header.
	 * 
	 * @param headerId
	 *            The primary key id for the header the records belong to.
	 * 
	 * @return A Collection of RaindanceCheckRecord beans
	 */
	public Collection getRaindanceCheckRecordByHeaderId(int headerId) {
		Collection col = new Vector();
		try {
			col = ((RaindanceCheckRecordHome) IDOLookup.getHome(RaindanceCheckRecord.class))
					.findAllByHeaderId(headerId);
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}

		return col;
	}

	/**
	 * A method to create the files to be sent to the Raindance system and updates the
	 * status of PaymentHeaders/InvoiceHeaders from P to L.
	 * 
	 * @param schoolCategory
	 *            The string primary key representing the school category we are
	 *            creating files for.
	 * @param paymentDate
	 *            The date the payment is supposed to be done on.
	 * @param periodText
	 *            A text to be inserted on the customers bill/providers payment
	 *            slip.
	 * @param user
	 *            The user that is executing the file creation.
	 * @param currentLocale
	 *            The current locale, used to format numbers.
	 * 
	 * @author palli
	 */
	public void createFiles(String schoolCategory, IWTimestamp paymentDate,
			String periodText, User user, Locale currentLocale) {
		RaindanceFileCreationThread creationThread = new RaindanceFileCreationThread(
				schoolCategory, paymentDate, periodText, user, currentLocale,
				getIWApplicationContext());
		creationThread.start();
	}

	/**
	 * A method to delete the files created for the Raindance system. Also reverts the
	 * status of PaymentHeaders/InvoiceHeaders to P.
	 * 
	 * @param schoolCategory
	 *            The string primary key representing the school category we are
	 *            deleting files for.
	 * 
	 * @author palli
	 */
	public void deleteFiles(String schoolCategory, User user) {
		UserTransaction trans = null;
		try {
			trans = getSessionContext().getUserTransaction();
			trans.begin();

			IWTimestamp now = IWTimestamp.RightNow();
			RaindanceJournalLog log = ((RaindanceJournalLogHome) IDOLookup
					.getHome(RaindanceJournalLog.class)).create();
			log.setSchoolCategoryString(schoolCategory);
			log.setEventFileDeleted();
			log.setEventDate(now.getTimestamp());
			log.setUser(user);
			log.store();

			RaindanceCheckHeader header = getRaindanceCheckHeaderBySchoolCategory(schoolCategory);
			if (header != null) {
				Collection col = this
						.getRaindanceCheckRecordByHeaderId(((Integer) header
								.getPrimaryKey()).intValue());
				if (col != null && !col.isEmpty()) {
					Iterator it = col.iterator();
					while (it.hasNext()) {
						RaindanceCheckRecord rec = (RaindanceCheckRecord) it.next();
						rec.remove();
					}
				}
			}

			Collection phInCommune = ((PaymentHeaderHome) IDOLookup
					.getHome(PaymentHeader.class))
					.findBySchoolCategoryStatusInCommuneWithCommunalManagement(
							schoolCategory, 'L');
			Collection phOutsideCommune = ((PaymentHeaderHome) IDOLookup
					.getHome(PaymentHeader.class))
					.findBySchoolCategoryStatusOutsideCommuneOrWithoutCommunalManagement(
							schoolCategory, 'L');

			if (phInCommune != null && !phInCommune.isEmpty()) {
				Collection rec = ((PaymentRecordHome) IDOLookup
						.getHome(PaymentRecord.class))
						.findByPaymentHeaders(phInCommune);
				Iterator it = rec.iterator();

				while (it.hasNext()) {
					PaymentRecord pRec = (PaymentRecord) it.next();
					pRec.setStatus('P');
					pRec.store();
				}

				Iterator itor = phInCommune.iterator();
				while (itor.hasNext()) {
					PaymentHeader head = (PaymentHeader) itor.next();
					head.setStatus('P');
					head.store();
				}
			}

			if (phOutsideCommune != null && !phOutsideCommune.isEmpty()) {
				Collection rec = ((PaymentRecordHome) IDOLookup
						.getHome(PaymentRecord.class))
						.findByPaymentHeaders(phOutsideCommune);
				Iterator it = rec.iterator();

				while (it.hasNext()) {
					PaymentRecord pRec = (PaymentRecord) it.next();
					pRec.setStatus('P');
					pRec.store();
				}

				Iterator itor = phOutsideCommune.iterator();
				while (itor.hasNext()) {
					PaymentHeader head = (PaymentHeader) itor.next();
					head.setStatus('P');
					head.store();
				}
			}

			Collection iHeaders = ((InvoiceHeaderHome) IDOLookup
					.getHome(InvoiceHeader.class)).findByStatusAndCategory("L",
					schoolCategory);
			Iterator itHead = iHeaders.iterator();
			while (itHead.hasNext()) {
				InvoiceHeader iHead = (InvoiceHeader) itHead.next();
				iHead.setStatus('P');
				iHead.store();
			}

			// Delete files in folder A. Must get folder info from
			// ExportMappingBean!!!
			// ExportDataMapping mapping =
			// getExportBusiness().getExportDataMapping(schoolCategory);

			trans.commit();
		} catch (Exception e) {
			if (trans != null) {
				try {
					trans.rollback();
				} catch (SystemException se) {
					se.printStackTrace();
				}
			}
		}
	}

	public void moveFiles(String schoolCategory) throws MoveFileException {
		ExportDataMapping mapping = null;
		String fileFolder = null; // Source folder
		String raindanceFolder = null; // Destination folder
		String backupFolder = null; // Destination folder
		String fileName = null; // Filename without path
		System.out.println("Category " + schoolCategory);
		try {
			mapping = getExportBusiness().getExportDataMapping(schoolCategory);
			fileFolder = mapping.getFileCreationFolder();
			raindanceFolder = mapping.getExportFileFolder();
			backupFolder = mapping.getFileBackupFolder();
			System.out.println("Files:" + fileFolder + "   Export:" + raindanceFolder
					+ "   Backup:" + backupFolder);

			if (null != fileFolder) {
				// Get all the files in the source folder
				File ff = new File("." + fileFolder);
				File[] filesToMove = ff.listFiles();
				if (null != filesToMove) {
					System.out.println("# of files to move:"
							+ filesToMove.length);
					// Move them to first destination folder
					for (int i = 0; i < filesToMove.length; i++) {

						// Get filename
						fileName = filesToMove[i].getName();
						System.out.println("Filename:" + fileName);

						if (null != backupFolder) {
							// Copy to the bacup folder
							FileChannel srcChannel = new FileInputStream(
									filesToMove[i].getPath()).getChannel();

							// Create channel on the destination
							FileChannel dstChannel = new FileOutputStream(
									backupFolder + fileName).getChannel();

							// Copy file contents from source to destination
							dstChannel.transferFrom(srcChannel, 0, srcChannel
									.size());

							// Close the channels
							srcChannel.close();
							dstChannel.close();
						}

						// Move
						if (null != raindanceFolder) {
							System.out
									.println("Backup:" + raindanceFolder + fileName);
							filesToMove[i].renameTo(new File(raindanceFolder
									+ fileName));
						}
					}
				} else {
					throw new MoveFileException();
				}
			} else {
				throw new MoveFileException();
			}
		} catch (RemoteException e1) {
			e1.printStackTrace();
		} catch (FinderException e1) {
			e1.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ExportBusiness getExportBusiness() {
		try {
			return (ExportBusiness) getServiceInstance(ExportBusiness.class);
		} catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	public UserBusiness getUserBusiness() {
		try {
			return (UserBusiness) getServiceInstance(UserBusiness.class);
		} catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	public SchoolBusiness getSchoolBusiness() {
		try {
			return (SchoolBusiness) getServiceInstance(SchoolBusiness.class);
		} catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	public PostingBusiness getPostingBusiness() {
		try {
			return (PostingBusiness) getServiceInstance(PostingBusiness.class);
		} catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	public ProviderBusiness getProviderBusiness() {
		try {
			return (ProviderBusiness) getServiceInstance(ProviderBusiness.class);
		} catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}
}