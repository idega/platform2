/*
 * Copyright (C) 2004 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */

package is.idega.idegaweb.member.isi.block.accounting.export.creditcard.business;

import is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.Configuration;

import java.util.Collection;


import com.idega.business.IBOService;
import com.idega.util.IWTimestamp;

/**
 * @author palli
 */
public interface ExportBusiness extends IBOService {
    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.business.ExportBusinessBean#createFileFromContracts
     */
    public boolean createFileFromContracts(String dateFrom, String dateTo)
            throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.business.ExportBusinessBean#getConfiguration
     */
    public Configuration getConfiguration(String typeID)
            throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.business.ExportBusinessBean#saveConfiguration
     */
    public boolean saveConfiguration(String configurationID, String sendServer,
            String sendUser, String sendPasswd, String sendPath,
            String sendBackupPath, String createPlugin, String createPath,
            String lastBatch, IWTimestamp batchDate, String createEncPlugin,
            String getServer, String getUser, String getPasswd, String getPath,
            String getBackupPath, String readPlugin, String readEncPlugin)
            throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.business.ExportBusinessBean#findAllBatches
     */
    public Collection findAllBatches() throws java.rmi.RemoteException;

}
