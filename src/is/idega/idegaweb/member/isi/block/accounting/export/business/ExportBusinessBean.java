/*
 * Copyright (C) 2004 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.member.isi.block.accounting.export.business;

import is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntry;
import is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryHome;
import is.idega.idegaweb.member.isi.block.accounting.data.PaymentType;
import is.idega.idegaweb.member.isi.block.accounting.data.PaymentTypeHome;
import is.idega.idegaweb.member.isi.block.accounting.export.business.BatchRunning;
import is.idega.idegaweb.member.isi.block.accounting.export.business.ExportBusiness;
import is.idega.idegaweb.member.isi.block.accounting.export.business.ExportBusinessThread;
import is.idega.idegaweb.member.isi.block.accounting.export.data.Batch;
import is.idega.idegaweb.member.isi.block.accounting.export.data.BatchHome;
import is.idega.idegaweb.member.isi.block.accounting.export.data.Configuration;
import is.idega.idegaweb.member.isi.block.accounting.export.data.ConfigurationHome;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.util.IWTimestamp;

/**
 * @author palli
 */
public class ExportBusinessBean extends IBOServiceBean implements
		ExportBusiness {

	public boolean createFileFromContracts(String dateFrom, String dateTo, String userName) {
/*		if (!BatchRunning.reserveSendFileBatch()) {
			return false;
		}

		Thread exportThread = new ExportBusinessThread(dateFrom, dateTo, true, userName);
		exportThread.start();*/

		return true;
	}

	public Configuration getConfiguration(String typeID) {
		Configuration conf = null;

		ConfigurationHome cHome;
		try {
			cHome = (ConfigurationHome) IDOLookup.getHome(Configuration.class);
			conf = cHome.findByCreditcardTypeID(new Integer(typeID).intValue());
		} catch (RemoteException e) {
		} catch (FinderException e) {
		}

		return conf;
	}

	public boolean saveConfiguration(String configurationID, String sendServer,
			String sendUser, String sendPasswd, String sendPath,
			String sendBackupPath, String createPlugin, String createPath,
			String lastBatch, IWTimestamp batchDate, String createEncPlugin,
			String getServer, String getUser, String getPasswd, String getPath,
			String getBackupPath, String readPlugin, String readEncPlugin) {

		Configuration conf = null;
		conf = getConfiguration(configurationID);
		if (conf == null) {
			ConfigurationHome cHome;
			try {
				cHome = (ConfigurationHome) IDOLookup
						.getHome(Configuration.class);
				conf = cHome.create();
				conf.setCreditcardTypeID(new Integer(configurationID)
						.intValue());
			} catch (RemoteException e1) {
				e1.printStackTrace();
			} catch (CreateException e1) {
				e1.printStackTrace();
			}
		}

		if (conf != null) {
			conf.setSendFTPServer(sendServer);
			conf.setSendFTPUser(sendUser);
			conf.setSendFTPPassword(sendPasswd);
			conf.setSendFTPPath(sendPath);
			conf.setSendFTPBackup(sendBackupPath);
			conf.setSendFTPFileCreationPlugin(createPlugin);
			conf.setSendFTPFileCreationPath(createPath);
			conf.setSendFTPLastBatchNumber(lastBatch);
			if (batchDate != null) {
				conf.setSendFTPLastBatchDate(batchDate.getTimestamp());
			}
			conf.setSendFTPEncryptionPlugin(createEncPlugin);
			conf.setGetFTPServer(getServer);
			conf.setGetFTPUser(getUser);
			conf.setGetFTPPassword(getPasswd);
			conf.setGetFTPPath(getPath);
			conf.setGetFTPBackup(getBackupPath);
			conf.setGetFTPFileReadPlugin(readPlugin);
			conf.setGetFTPEncryptionPlugin(readEncPlugin);

			conf.store();

			return true;
		}

		return false;
	}

	public Collection findAllBatches() {
		Collection batches = null;

		try {
			BatchHome bHome = (BatchHome) IDOLookup.getHome(Batch.class);
			batches = bHome.findAllNewestFirst();
		} catch (RemoteException e1) {
			e1.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}

		return batches;

	}

	public Collection findAllEntriesNotInBatch() {
		try {
			FinanceEntryHome fHome = (FinanceEntryHome) IDOLookup
					.getHome(FinanceEntry.class);
			PaymentTypeHome pHome = (PaymentTypeHome) IDOLookup
					.getHome(PaymentType.class);
			PaymentType creditCard = pHome.findPaymentTypeCreditcardSystem();
			PaymentType bank = pHome.findPaymentTypeBankSystem();

			String[] types = { creditCard.getPrimaryKey().toString(),
					bank.getPrimaryKey().toString() };

			Collection entries = fHome.findAllByPaymentTypesNotInBatch(types,
					null, null);

			return entries;
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public Collection findAllEntriesInBatch(int batchID) {
		try {
			FinanceEntryHome fHome = (FinanceEntryHome) IDOLookup
					.getHome(FinanceEntry.class);

			Collection entries = fHome.findAllByBatchID(batchID);

			return entries;
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}