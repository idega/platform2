/*
 * Copyright (C) 2004 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */

package is.idega.idegaweb.member.isi.block.accounting.export.creditcard.presentation;

import is.idega.idegaweb.member.isi.block.accounting.export.creditcard.business.ExportBusiness;
import is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.Configuration;
import is.idega.idegaweb.member.isi.block.accounting.presentation.CashierSubWindowTemplate;
import is.idega.idegaweb.member.isi.block.accounting.presentation.CashierWindow;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWConstants;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.PasswordInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.util.SelectorUtility;
import com.idega.util.IWTimestamp;


/**
 * @author palli
 */
public class Setup extends CashierSubWindowTemplate {

    protected static final String ACTION_SUBMIT = "cas_submit";

    protected static final String LABEL_SEND_FILES = "cas_send_files";
    
    protected static final String LABEL_GET_FILES = "cas_get_files";
    
    protected static final String LABEL_TYPE = "cas_cc_type";
    
    protected static final String LABEL_SEND_SERVER = "cas_send_server";

    protected static final String LABEL_SEND_USER = "cas_send_user";

    protected static final String LABEL_SEND_PASSWD = "cas_send_passwd";

    protected static final String LABEL_SEND_PATH = "cas_send_path";

    protected static final String LABEL_SEND_BACKUP = "cas_send_backup";

    protected static final String LABEL_FILE_CREATE_PLUGIN = "cas_file_create_plugin";

    protected static final String LABEL_FILE_CREATE_PATH = "cas_file_create_path";

    protected static final String LABEL_FILE_LAST_BATCH = "cas_file_last_batch";

    protected static final String LABEL_FILE_BATCH_DATE = "cas_file_batch_date";

    protected static final String LABEL_SEND_ENCRYPTION_PLUGIN = "cas_send_encryption_plugin";

    protected static final String LABEL_GET_SERVER = "cas_get_server";

    protected static final String LABEL_GET_USER = "cas_get_user";
    
    protected static final String LABEL_GET_PASSWD = "cas_get_passwd";

    protected static final String LABEL_GET_PATH = "cas_get_path";

    protected static final String LABEL_GET_BACKUP = "cas_get_backup";
    
    protected static final String LABEL_GET_FILE_READ_PLUGIN = "cas_get_file_read_plugin";

    protected static final String LABEL_GET_ENCRYPTION_PLUGIN = "cas_get_encryption_plugin";

    public Setup() {
        super();
    }
    
    private boolean saveTypeSetup(IWContext iwc) {
        String type = iwc.getParameter(LABEL_TYPE);
        String sendServer = iwc.getParameter(LABEL_SEND_SERVER);
        String sendUser = iwc.getParameter(LABEL_SEND_USER);
        String sendPasswd = iwc.getParameter(LABEL_SEND_PASSWD);
        String sendPath = iwc.getParameter(LABEL_SEND_PATH);
        String sendBackup = iwc.getParameter(LABEL_SEND_BACKUP);
        String createPlugin = iwc.getParameter(LABEL_FILE_CREATE_PLUGIN);
        String createPath = iwc.getParameter(LABEL_FILE_CREATE_PATH);
        String lastBatch = iwc.getParameter(LABEL_FILE_LAST_BATCH);
        String batchDate = iwc.getParameter(LABEL_FILE_BATCH_DATE);
        String sendEncPlugin = iwc.getParameter(LABEL_SEND_ENCRYPTION_PLUGIN);
        String getServer = iwc.getParameter(LABEL_GET_SERVER);
        String getUser = iwc.getParameter(LABEL_GET_USER);
        String getPasswd = iwc.getParameter(LABEL_GET_PASSWD);
        String getPath = iwc.getParameter(LABEL_GET_PATH);
        String getBackup = iwc.getParameter(LABEL_GET_BACKUP);
        String readPlugin = iwc.getParameter(LABEL_GET_FILE_READ_PLUGIN);
        String getEncPlugin = iwc.getParameter(LABEL_GET_ENCRYPTION_PLUGIN);
        
        IWTimestamp batchDateStamp = null;
        
        try {
            if (batchDate != null) {
                batchDateStamp = new IWTimestamp(batchDate); 
            }
        } catch (Exception e) {
            batchDateStamp = null;
        }

        try {
            return getExportBusiness(iwc).saveConfiguration(type, sendServer, sendUser, sendPasswd, sendPath, sendBackup, createPlugin, createPath, lastBatch, batchDateStamp, sendEncPlugin, getServer, getUser, getPasswd, getPath, getBackup, readPlugin, getEncPlugin);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    
    /* (non-Javadoc)
     * @see com.idega.presentation.PresentationObject#main(com.idega.presentation.IWContext)
     */
    public void main(IWContext iwc) throws Exception {
        IWResourceBundle iwrb = getResourceBundle(iwc);
        Form f = new Form();

        Configuration conf = null;
        
        if (iwc.isParameterSet(ACTION_SUBMIT)) {
            if (!saveTypeSetup(iwc)) {
                Table error = new Table();
                Text labelError = new Text(iwrb.getLocalizedString(
                        ERROR_COULD_NOT_SAVE, "Could not save")
                        + ":");
                labelError
                        .setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE_RED);

                int r = 1;
                error.add(labelError, 1, r++);
                if (errorList != null && !errorList.isEmpty()) {
                    Iterator it = errorList.iterator();
                    while (it.hasNext()) {
                        String loc = (String) it.next();
                        Text errorText = new Text(iwrb.getLocalizedString(loc,
                                ""));
                        errorText
                                .setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE_RED);

                        error.add(errorText, 1, r++);
                    }
                }

                f.add(error);
            }
        } 
        
        String type = iwc.getParameter(LABEL_TYPE);
        
        if (type != null) {
            conf = getExportBusiness(iwc).getConfiguration(type);
        }
        
        Table inputTable = new Table();
        f.add(inputTable);

        Text labelType = new Text(iwrb.getLocalizedString(LABEL_TYPE, "Creditcard type"));
        labelType.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        Text labelSendFiles = new Text(iwrb.getLocalizedString(LABEL_SEND_FILES, "Send files") + " :");
        labelSendFiles.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        Text labelGetFiles = new Text(iwrb.getLocalizedString(LABEL_GET_FILES, "Get files") + " :");
        labelGetFiles.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        Text labelSendServer = new Text(iwrb.getLocalizedString(LABEL_SEND_SERVER, "Server"));
        labelSendServer.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        Text labelSendUser = new Text(iwrb.getLocalizedString(LABEL_SEND_USER, "User"));
        labelSendUser.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        Text labelSendPassword = new Text(iwrb.getLocalizedString(LABEL_SEND_PASSWD, "Password"));
        labelSendPassword.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        Text labelSendPath = new Text(iwrb.getLocalizedString(LABEL_SEND_PATH, "Path"));
        labelSendPath.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        Text labelSendBackup = new Text(iwrb.getLocalizedString(LABEL_SEND_BACKUP, "Backup path"));
        labelSendBackup.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        Text labelFileCreatePlugin = new Text(iwrb.getLocalizedString(LABEL_FILE_CREATE_PLUGIN, "Create plugin"));
        labelFileCreatePlugin.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        Text labelFileCreatePath = new Text(iwrb.getLocalizedString(LABEL_FILE_CREATE_PATH, "Create path"));
        labelFileCreatePath.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        Text labelFileLastBatch = new Text(iwrb.getLocalizedString(LABEL_FILE_LAST_BATCH, "Last batch"));
        labelFileLastBatch.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        Text labelFileBatchDate = new Text(iwrb.getLocalizedString(LABEL_FILE_BATCH_DATE, "Batch date"));
        labelFileBatchDate.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        Text labelSendEncryptionPlugin = new Text(iwrb.getLocalizedString(LABEL_SEND_ENCRYPTION_PLUGIN, "Encryption plugin"));
        labelSendEncryptionPlugin.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        Text labelGetServer = new Text(iwrb.getLocalizedString(LABEL_GET_SERVER, "Server"));
        labelGetServer.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        Text labelGetUser = new Text(iwrb.getLocalizedString(LABEL_GET_USER, "User"));
        labelGetUser.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        Text labelGetPassword = new Text(iwrb.getLocalizedString(LABEL_GET_PASSWD, "Password"));
        labelGetPassword.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        Text labelGetPath = new Text(iwrb.getLocalizedString(LABEL_GET_PATH, "Path"));
        labelGetPath.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        Text labelGetBackup = new Text(iwrb.getLocalizedString(LABEL_GET_BACKUP, "Backup path"));
        labelGetBackup.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        Text labelGetFileReadPlugin = new Text(iwrb.getLocalizedString(LABEL_GET_FILE_READ_PLUGIN, "Read plugin"));
        labelGetFileReadPlugin.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        Text labelGetEncryptionPlugin = new Text(iwrb.getLocalizedString(LABEL_GET_ENCRYPTION_PLUGIN, "Encryption plugin"));
        labelGetEncryptionPlugin.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        
        Collection types = null;
        try {
            types = getAccountingBusiness(iwc).findAllCreditCardType();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        DropdownMenu typeInput = new DropdownMenu(LABEL_TYPE);
        SelectorUtility util = new SelectorUtility();
        if (types != null && !types.isEmpty()) {
            typeInput = (DropdownMenu) util.getSelectorFromIDOEntities(
                    typeInput, types, "getName");
        }
        
        if (type != null) {
            typeInput.setSelectedElement(type);
        }
        
        TextInput sendServerInput = new TextInput(LABEL_SEND_SERVER);
        sendServerInput.setLength(20);
        TextInput sendUserInput = new TextInput(LABEL_SEND_USER);
        sendUserInput.setLength(20);
        PasswordInput sendPasswordInput = new PasswordInput(LABEL_SEND_PASSWD);
        sendPasswordInput.setLength(20);
        TextInput sendPathInput = new TextInput(LABEL_SEND_PATH);
        sendPathInput.setLength(20);
        TextInput sendBackupPathInput = new TextInput(LABEL_SEND_BACKUP);
        sendBackupPathInput.setLength(20);
        TextInput fileCreatePluginInput = new TextInput(LABEL_FILE_CREATE_PLUGIN);
        fileCreatePluginInput.setLength(20);
        TextInput fileCreatePathInput = new TextInput(LABEL_FILE_CREATE_PATH);
        fileCreatePathInput.setLength(20);
        TextInput fileLastBatchInput = new TextInput(LABEL_FILE_LAST_BATCH);
        fileLastBatchInput.setLength(20);
        DateInput fileLastBatchDateInput = new DateInput(LABEL_FILE_BATCH_DATE, true);
        TextInput sendEncryptionPluginInput = new TextInput(LABEL_SEND_ENCRYPTION_PLUGIN);
        sendEncryptionPluginInput.setLength(20);
        TextInput getServerInput = new TextInput(LABEL_GET_SERVER);
        getServerInput.setLength(20);
        TextInput getUserInput = new TextInput(LABEL_GET_USER);
        getUserInput.setLength(20);
        PasswordInput getPasswordInput = new PasswordInput(LABEL_GET_PASSWD);
        getPasswordInput.setLength(20);
        TextInput getPathInput = new TextInput(LABEL_GET_PATH);
        getPathInput.setLength(20);
        TextInput getBackupPathInput = new TextInput(LABEL_GET_BACKUP);
        getBackupPathInput.setLength(20);
        TextInput getFileReadPluginInput = new TextInput(LABEL_GET_FILE_READ_PLUGIN);
        getFileReadPluginInput.setLength(20);
        TextInput getEncryptionPluginInput = new TextInput(LABEL_GET_ENCRYPTION_PLUGIN);
        getEncryptionPluginInput.setLength(20);
        
        if (conf != null) {
            sendServerInput.setValue(conf.getSendFTPServer());
            sendUserInput.setValue(conf.getSendFTPUser());
            sendPasswordInput.setValue(conf.getSendFTPPassword());
            sendPathInput.setValue(conf.getSendFTPPath());
            sendBackupPathInput.setValue(conf.getSendFTPBackup());
            fileCreatePluginInput.setValue(conf.getSendFTPFileCreationPlugin());
            fileCreatePathInput.setValue(conf.getSendFTPFileCreationPath());
            fileLastBatchInput.setValue(conf.getSendFTPLastBatchNumber());
            if (conf.getSendFTPLastBatchDate() != null) {
                IWTimestamp batchDate = new IWTimestamp(conf.getSendFTPLastBatchDate());
                fileLastBatchDateInput.setDate(batchDate.getDate());
            }
            sendEncryptionPluginInput.setValue(conf.getSendFTPEncryptionPlugin());
            
            getServerInput.setValue(conf.getGetFTPServer());
            getUserInput.setValue(conf.getGetFTPUser());
            getPasswordInput.setValue(conf.getGetFTPPassword());
            getPathInput.setValue(conf.getGetFTPPath());
            getBackupPathInput.setValue(conf.getGetFTPBackup());
            getFileReadPluginInput.setValue(conf.getGetFTPFileReadPlugin());
            getEncryptionPluginInput.setValue(conf.getGetFTPEncryptionPlugin());
        }
        
        
        SubmitButton submit = new SubmitButton(iwrb.getLocalizedString(
                ACTION_SUBMIT, "Submit"), ACTION_SUBMIT, "submit");

        int row = 1;
        inputTable.add(labelType, 1, row++);
        inputTable.add(typeInput, 1, row++);

        typeInput.setToSubmit();
        
        row++;
        
        inputTable.add(labelSendFiles, 1, row++);
        inputTable.add(labelSendServer, 1, row);
        inputTable.add(labelSendUser, 2, row);
        inputTable.add(labelSendPassword, 3, row);
        inputTable.add(labelSendPath, 4, row);
        inputTable.add(labelSendBackup, 5, row++);
        inputTable.add(sendServerInput, 1, row);
        inputTable.add(sendUserInput, 2, row);
        inputTable.add(sendPasswordInput, 3, row);
        inputTable.add(sendPathInput, 4, row);
        inputTable.add(sendBackupPathInput, 5, row++);
        inputTable.add(labelFileCreatePlugin, 1, row);
        inputTable.add(labelFileCreatePath, 2, row);
        inputTable.add(labelFileLastBatch, 3, row);
        inputTable.add(labelFileBatchDate, 4, row);
        inputTable.add(labelSendEncryptionPlugin, 5, row++);
        inputTable.add(fileCreatePluginInput, 1, row);
        inputTable.add(fileCreatePathInput, 2, row);
        inputTable.add(fileLastBatchInput, 3, row);
        inputTable.add(fileLastBatchDateInput, 4, row);
        inputTable.add(sendEncryptionPluginInput, 5, row++);
        
        row++;
        
        inputTable.add(labelGetFiles, 1, row++);
        inputTable.add(labelGetServer, 1, row);
        inputTable.add(labelGetUser, 2, row);
        inputTable.add(labelGetPassword, 3, row);
        inputTable.add(labelGetPath, 4, row);
        inputTable.add(labelGetBackup, 5, row++);
        inputTable.add(getServerInput, 1, row);
        inputTable.add(getUserInput, 2, row);
        inputTable.add(getPasswordInput, 3, row);
        inputTable.add(getPathInput, 4, row);
        inputTable.add(getBackupPathInput, 5, row++);
        inputTable.add(labelGetFileReadPlugin, 1, row);
        inputTable.add(labelGetEncryptionPlugin, 2, row++);
        inputTable.add(getFileReadPluginInput, 1, row);
        inputTable.add(getEncryptionPluginInput, 2, row++);
        
        row += 2;
        
        inputTable.setAlignment(5, row, "RIGHT");
        inputTable.add(submit, 5, row);

        f.maintainParameter(CashierWindow.ACTION);
        f.maintainParameter(CashierWindow.PARAMETER_GROUP_ID);
        f.maintainParameter(CashierWindow.PARAMETER_DIVISION_ID);
        f.maintainParameter(CashierWindow.PARAMETER_CLUB_ID);
        
        add(f);
     }
    
    protected ExportBusiness getExportBusiness(IWApplicationContext iwc) {
        try {
            return (ExportBusiness) IBOLookup.getServiceInstance(iwc,
                    ExportBusiness.class);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return null;
    }
}