/*
 * Copyright (C) 2004 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */

package is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data;

import is.idega.idegaweb.member.isi.block.accounting.data.CreditCardType;

import java.sql.Timestamp;


import com.idega.data.IDOEntity;

/**
 * @author palli
 */
public interface Configuration extends IDOEntity {
    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.ConfigurationBMPBean#setCreditcardTypeID
     */
    public void setCreditcardTypeID(int typeID);

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.ConfigurationBMPBean#setCreditCardType
     */
    public void setCreditCardType(CreditCardType type);

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.ConfigurationBMPBean#setSendFTPServer
     */
    public void setSendFTPServer(String server);

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.ConfigurationBMPBean#setSendFTPUser
     */
    public void setSendFTPUser(String user);

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.ConfigurationBMPBean#setSendFTPPassword
     */
    public void setSendFTPPassword(String passwd);

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.ConfigurationBMPBean#setSendFTPPath
     */
    public void setSendFTPPath(String path);

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.ConfigurationBMPBean#setSendFTPBackup
     */
    public void setSendFTPBackup(String backup);

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.ConfigurationBMPBean#setSendFTPFileCreationPlugin
     */
    public void setSendFTPFileCreationPlugin(String plugin);

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.ConfigurationBMPBean#setSendFTPFileCreationPath
     */
    public void setSendFTPFileCreationPath(String path);

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.ConfigurationBMPBean#setSendFTPLastBatchNumber
     */
    public void setSendFTPLastBatchNumber(String number);

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.ConfigurationBMPBean#setSendFTPLastBatchDate
     */
    public void setSendFTPLastBatchDate(Timestamp date);

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.ConfigurationBMPBean#setSendFTPEncryptionPlugin
     */
    public void setSendFTPEncryptionPlugin(String plugin);

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.ConfigurationBMPBean#setGetFTPServer
     */
    public void setGetFTPServer(String server);

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.ConfigurationBMPBean#setGetFTPUser
     */
    public void setGetFTPUser(String user);

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.ConfigurationBMPBean#setGetFTPPassword
     */
    public void setGetFTPPassword(String passwd);

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.ConfigurationBMPBean#setGetFTPPath
     */
    public void setGetFTPPath(String path);

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.ConfigurationBMPBean#setGetFTPBackup
     */
    public void setGetFTPBackup(String backup);

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.ConfigurationBMPBean#setGetFTPFileReadPlugin
     */
    public void setGetFTPFileReadPlugin(String plugin);

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.ConfigurationBMPBean#setGetFTPEncryptionPlugin
     */
    public void setGetFTPEncryptionPlugin(String plugin);

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.ConfigurationBMPBean#getCreditcardTypeID
     */
    public int getCreditcardTypeID();

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.ConfigurationBMPBean#getCreditCardType
     */
    public CreditCardType getCreditCardType();

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.ConfigurationBMPBean#getSendFTPServer
     */
    public String getSendFTPServer();

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.ConfigurationBMPBean#getSendFTPUser
     */
    public String getSendFTPUser();

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.ConfigurationBMPBean#getSendFTPPassword
     */
    public String getSendFTPPassword();

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.ConfigurationBMPBean#getSendFTPPath
     */
    public String getSendFTPPath();

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.ConfigurationBMPBean#getSendFTPBackup
     */
    public String getSendFTPBackup();

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.ConfigurationBMPBean#getSendFTPFileCreationPlugin
     */
    public String getSendFTPFileCreationPlugin();

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.ConfigurationBMPBean#getSendFTPFileCreationPath
     */
    public String getSendFTPFileCreationPath();

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.ConfigurationBMPBean#getSendFTPLastBatchNumber
     */
    public String getSendFTPLastBatchNumber();

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.ConfigurationBMPBean#getSendFTPLastBatchDate
     */
    public Timestamp getSendFTPLastBatchDate();

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.ConfigurationBMPBean#getSendFTPEncryptionPlugin
     */
    public String getSendFTPEncryptionPlugin();

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.ConfigurationBMPBean#getGetFTPServer
     */
    public String getGetFTPServer();

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.ConfigurationBMPBean#getGetFTPUser
     */
    public String getGetFTPUser();

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.ConfigurationBMPBean#getGetFTPPassword
     */
    public String getGetFTPPassword();

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.ConfigurationBMPBean#getGetFTPPath
     */
    public String getGetFTPPath();

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.ConfigurationBMPBean#getGetFTPBackup
     */
    public String getGetFTPBackup();

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.ConfigurationBMPBean#getGetFTPFileReadPlugin
     */
    public String getGetFTPFileReadPlugin();

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.ConfigurationBMPBean#getGetFTPEncryptionPlugin
     */
    public String getGetFTPEncryptionPlugin();

}
