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
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;

/**
 * @author palli
 * 
 */
public class ConfigurationBMPBean extends GenericEntity implements
        Configuration {

    protected final static String ENTITY_NAME = "isi_creditcard_conf";

    protected final static String COLUMN_CREDITCARD_TYPE = "card_type_id";

    protected final static String COLUMN_SEND_SERVER = "ftp_send_server";

    protected final static String COLUMN_SEND_USER = "ftp_send_user";
    
    protected final static String COLUMN_SEND_PASSWD = "ftp_send_passwd";
    
    protected final static String COLUMN_SEND_PATH = "ftp_send_path";

    protected final static String COLUMN_SEND_BACKUP = "ftp_send_backup";
        
    protected final static String COLUMN_SEND_FILE_LAST_BATCH = "ftp_send_file_last_batch";

    protected final static String COLUMN_SEND_FILE_BATCH_DATE = "ftp_send_file_batch_date";

    protected final static String COLUMN_SEND_FILE_CREATE_PLUGIN = "ftp_send_file_create_plug";

    protected final static String COLUMN_SEND_FILE_CREATE_PATH = "ftp_send_file_create_path";

    protected final static String COLUMN_SEND_ENCRYPTION_PLUGIN = "ftp_send_enc_plug";

    protected final static String COLUMN_GET_SERVER = "ftp_get_server";

    protected final static String COLUMN_GET_USER = "ftp_get_user";
    
    protected final static String COLUMN_GET_PASSWD = "ftp_get_passwd";
    
    protected final static String COLUMN_GET_PATH = "ftp_get_path";

    protected final static String COLUMN_GET_BACKUP = "ftp_get_backup";
        
    protected final static String COLUMN_GET_FILE_READ_PLUGIN = "ftp_get_file_read_plug";

    protected final static String COLUMN_GET_ENCRYPTION_PLUGIN = "ftp_get_enc_plug";
    
    /*
     * (non-Javadoc)
     * 
     * @see com.idega.data.GenericEntity#getEntityName()
     */
    public String getEntityName() {
        return ENTITY_NAME;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.idega.data.GenericEntity#initializeAttributes()
     */
    public void initializeAttributes() {
        addAttribute(getIDColumnName());
        addManyToOneRelationship(COLUMN_CREDITCARD_TYPE, CreditCardType.class);
        addAttribute(COLUMN_SEND_SERVER,"Send server", true, true, String.class, 255);
        addAttribute(COLUMN_SEND_USER, "Send user", true, true, String.class, 255);
        addAttribute(COLUMN_SEND_PASSWD, "Send pwd", true, true, String.class, 255);
        addAttribute(COLUMN_SEND_PATH, "Send path", true, true, String.class, 255);
        addAttribute(COLUMN_SEND_BACKUP, "Send backup", true, true, String.class, 255);
        addAttribute(COLUMN_SEND_FILE_CREATE_PLUGIN, "Send plugin", true, true, String.class, 255);
        addAttribute(COLUMN_SEND_FILE_CREATE_PATH, "Send path", true, true, String.class, 255);
        addAttribute(COLUMN_SEND_FILE_LAST_BATCH, "Last batch", true, true, String.class, 255);
        addAttribute(COLUMN_SEND_FILE_BATCH_DATE, "Batch date", true, true, Timestamp.class);
        addAttribute(COLUMN_SEND_ENCRYPTION_PLUGIN, "Send encryption plugin", true, true, String.class, 255);        
        addAttribute(COLUMN_GET_SERVER, "Get server", true, true, String.class, 255);
        addAttribute(COLUMN_GET_USER, "Get user", true, true, String.class, 255);
        addAttribute(COLUMN_GET_PASSWD, "Get pwd", true, true, String.class, 255);
        addAttribute(COLUMN_GET_PATH, "Get path", true, true, String.class, 255);
        addAttribute(COLUMN_GET_BACKUP, "Get backup", true, true, String.class, 255);
        addAttribute(COLUMN_GET_FILE_READ_PLUGIN, "Get plugin", true, true, String.class, 255);
        addAttribute(COLUMN_GET_ENCRYPTION_PLUGIN, "Get encryption plugin", true, true, String.class, 255);
    }

    //Setters
    public void setCreditcardTypeID(int typeID) {
        setColumn(COLUMN_CREDITCARD_TYPE, typeID);
    }
    
    public void setCreditCardType(CreditCardType type) {
        setColumn(COLUMN_CREDITCARD_TYPE, type);
    }
    
    public void setSendFTPServer(String server) {
        setColumn(COLUMN_SEND_SERVER, server);
    }
    
    public void setSendFTPUser(String user) {
        setColumn(COLUMN_SEND_USER, user);
    }
    
    public void setSendFTPPassword(String passwd) {
        setColumn(COLUMN_SEND_PASSWD, passwd);
    }
    
    public void setSendFTPPath(String path) {
        setColumn(COLUMN_SEND_PATH, path);
    }
    
    public void setSendFTPBackup(String backup) {
        setColumn(COLUMN_SEND_BACKUP, backup);
    }
    
    public void setSendFTPFileCreationPlugin(String plugin) {
        setColumn(COLUMN_SEND_FILE_CREATE_PLUGIN, plugin);
    }
    
    public void setSendFTPFileCreationPath(String path) {
        setColumn(COLUMN_SEND_FILE_CREATE_PATH, path);
    }

    public void setSendFTPLastBatchNumber(String number) {
        setColumn(COLUMN_SEND_FILE_LAST_BATCH, number);
    }
    
    public void setSendFTPLastBatchDate(Timestamp date) {
        setColumn(COLUMN_SEND_FILE_BATCH_DATE, date);
    }
    
    public void setSendFTPEncryptionPlugin(String plugin) {
        setColumn(COLUMN_SEND_ENCRYPTION_PLUGIN, plugin);
    }
    
    public void setGetFTPServer(String server) {
        setColumn(COLUMN_GET_SERVER, server);
    }
    
    public void setGetFTPUser(String user) {
        setColumn(COLUMN_GET_USER, user);
    }
    
    public void setGetFTPPassword(String passwd) {
        setColumn(COLUMN_GET_PASSWD, passwd);
    }
    
    public void setGetFTPPath(String path) {
        setColumn(COLUMN_GET_PATH, path);
    }
    
    public void setGetFTPBackup(String backup) {
        setColumn(COLUMN_GET_BACKUP, backup);
    }
    
    public void setGetFTPFileReadPlugin(String plugin) {
        setColumn(COLUMN_GET_FILE_READ_PLUGIN, plugin);
    }
    
    public void setGetFTPEncryptionPlugin(String plugin) {
        setColumn(COLUMN_GET_ENCRYPTION_PLUGIN, plugin);
    }

    //Getters
    public int getCreditcardTypeID() {
        return getIntColumnValue(COLUMN_CREDITCARD_TYPE);
    }
    
    public CreditCardType getCreditCardType() {
        return (CreditCardType) getColumnValue(COLUMN_CREDITCARD_TYPE);
    }
    
    public String getSendFTPServer() {
        return getStringColumnValue(COLUMN_SEND_SERVER);
    }
    
    public String getSendFTPUser() {
        return getStringColumnValue(COLUMN_SEND_USER);
    }
    
    public String getSendFTPPassword() {
        return getStringColumnValue(COLUMN_SEND_PASSWD);
    }
    
    public String getSendFTPPath() {
        return getStringColumnValue(COLUMN_SEND_PATH);
    }
    
    public String getSendFTPBackup() {
        return getStringColumnValue(COLUMN_SEND_BACKUP);
    }
    
    public String getSendFTPFileCreationPlugin() {
        return getStringColumnValue(COLUMN_SEND_FILE_CREATE_PLUGIN);
    }
    
    public String getSendFTPFileCreationPath() {
        return getStringColumnValue(COLUMN_SEND_FILE_CREATE_PATH);
    }

    public String getSendFTPLastBatchNumber() {
        return getStringColumnValue(COLUMN_SEND_FILE_LAST_BATCH);
    }
    
    public Timestamp getSendFTPLastBatchDate() {
        return getTimestampColumnValue(COLUMN_SEND_FILE_BATCH_DATE);
    }
    
    public String getSendFTPEncryptionPlugin() {
        return getStringColumnValue(COLUMN_SEND_ENCRYPTION_PLUGIN);
    }
    
    public String getGetFTPServer() {
        return getStringColumnValue(COLUMN_GET_SERVER);
    }
    
    public String getGetFTPUser() {
        return getStringColumnValue(COLUMN_GET_USER);
    }
    
    public String getGetFTPPassword() {
        return getStringColumnValue(COLUMN_GET_PASSWD);
    }
    
    public String getGetFTPPath() {
        return getStringColumnValue(COLUMN_GET_PATH);
    }
    
    public String getGetFTPBackup() {
        return getStringColumnValue(COLUMN_GET_BACKUP);
    }
    
    public String getGetFTPFileReadPlugin() {
        return getStringColumnValue(COLUMN_GET_FILE_READ_PLUGIN);
    }
    
    public String getGetFTPEncryptionPlugin() {
        return getStringColumnValue(COLUMN_GET_ENCRYPTION_PLUGIN);
    }

	public Collection ejbFindAll() throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);

		return idoFindPKsBySQL(sql.toString());
	}	

	public Object ejbFindByCreditcardType(CreditCardType type) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhereEquals(COLUMN_CREDITCARD_TYPE, type);

		return idoFindOnePKByQuery(sql);
	}	

	public Object ejbFindByCreditcardTypeID(int typeID) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhereEquals(COLUMN_CREDITCARD_TYPE, typeID);

		return idoFindOnePKByQuery(sql);
	}	
}