/*
 * Copyright (C) 2004 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.member.isi.block.accounting.export.creditcard.business;

import is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.Batch;
import is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.BatchHome;
import is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.Configuration;
import is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.ConfigurationHome;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
import com.idega.util.IWTimestamp;

/**
 * @author palli
 */
public class ExportBusinessBean extends IBOServiceBean implements
        ExportBusiness {

    public boolean createFileFromContracts(String dateFrom, String dateTo) {
        if (!BatchRunning.reserveSendFileBatch()) {
            return false;
        }
        
        Thread exportThread = new ExportBusinessThread(dateFrom, dateTo, true);
        exportThread.start();

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
                cHome = (ConfigurationHome) IDOLookup.getHome(Configuration.class);
                conf = cHome.create();
                conf.setCreditcardTypeID(new Integer(configurationID).intValue());
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
}