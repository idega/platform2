/*
 * Copyright (C) 2004 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */

package is.idega.idegaweb.member.isi.block.accounting.export.creditcard.presentation;

import is.idega.idegaweb.member.isi.block.accounting.export.creditcard.business.ExportBusiness;
import is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.Batch;
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
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.IWTimestamp;


/**
 * @author palli
 */
public class SendFiles extends CashierSubWindowTemplate {

    protected static final String ACTION_SUBMIT = "sf_submit";
    
    protected static final String LABEL_DATE_FROM = "sf_date_from";
    
    protected static final String LABEL_DATE_TO = "sf_date_to";

    protected static final String LABEL_BATCH_NUMBER = "sf_batch_nr";
    
    protected static final String LABEL_CREATED = "sf_created";
    
    protected static final String LABEL_TYPE = "sf_type";
    
    protected static final String LABEL_CONTRACT_NUMBER = "sf_contract_number";

    protected static final String LABEL_SENT = "sf_sent";

    protected static final String ERROR_ALREADY_RUNNING = "sf_thread_already_running";
    
    public SendFiles() {
        super();
    }
    
    public boolean sendFiles(IWContext iwc) {
        String dateFrom = iwc.getParameter(LABEL_DATE_FROM);
        String dateTo = iwc.getParameter(LABEL_DATE_TO);
        
        try {
            return getExportBusiness(iwc).createFileFromContracts(dateFrom, dateTo);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public void main(IWContext iwc) {
        IWResourceBundle iwrb = getResourceBundle(iwc);
        Form f = new Form();
        
        if (iwc.isParameterSet(ACTION_SUBMIT)) {
            if (!sendFiles(iwc)) {
                Table error = new Table();
                Text labelError = new Text(iwrb.getLocalizedString(
                        ERROR_ALREADY_RUNNING, "Batch already running")
                        + ":");
                labelError
                        .setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE_RED);

                error.add(labelError, 1, 1);
                f.add(error);
            }
        } 
        
        Table t = new Table();
        Table inputTable = new Table();
        t.setCellpadding(5);
        inputTable.setCellpadding(5);

        int row = 1;
        Text labelDateFrom = new Text(iwrb.getLocalizedString(LABEL_DATE_FROM, "Date from"));
        labelDateFrom.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        Text labelDateTo = new Text(iwrb.getLocalizedString(LABEL_DATE_TO, "Date to"));
        labelDateTo.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        Text labelBatchNumber = new Text(iwrb.getLocalizedString(LABEL_BATCH_NUMBER, "Batch number"));
        labelBatchNumber.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        Text labelCreated = new Text(iwrb.getLocalizedString(LABEL_CREATED, "Created"));
        labelCreated.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        Text labelType = new Text(iwrb.getLocalizedString(LABEL_TYPE, "Creditcard type"));
        labelType.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        Text labelContractNumber = new Text(iwrb.getLocalizedString(LABEL_CONTRACT_NUMBER, "Contract number"));
        labelContractNumber.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        Text labelSent = new Text(iwrb.getLocalizedString(LABEL_SENT, "Sent"));
        labelSent.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);

        SubmitButton submit = new SubmitButton(iwrb.getLocalizedString(
                ACTION_SUBMIT, "Submit"), ACTION_SUBMIT, "submit");

        IWTimestamp dateFrom = null;
        IWTimestamp dateTo = null;
        String dateFromString = iwc.getParameter(LABEL_DATE_FROM);
        String dateToString = iwc.getParameter(LABEL_DATE_TO);
        
        try {
            if (dateFromString != null) {
                dateFrom = new IWTimestamp(dateFromString); 
            }
        } catch (Exception e) {
            dateFrom = null;
        }
        
        try {
            if (dateToString != null) {
                dateTo = new IWTimestamp(dateToString);
                dateTo.addDays(1);
            }
        } catch (Exception e) {
            dateTo = null;
        }
        
        DateInput dateFromInput = new DateInput(LABEL_DATE_FROM, true);
        if (dateFrom != null) {
            dateFromInput.setDate(dateFrom.getDate());
        }
        DateInput dateToInput = new DateInput(LABEL_DATE_TO, true);
        if (dateTo != null) {
            dateToInput.setDate(dateTo.getDate());
        } else {
            dateToInput.setToCurrentDate();            
        }
        
        inputTable.add(labelDateFrom, 1, row);
        inputTable.add(labelDateTo, 2, row++);
        inputTable.add(dateFromInput, 1, row);
        inputTable.add(dateToInput, 2, row);
        inputTable.setAlignment(3, row, "RIGHT");
        inputTable.add(submit, 3, row);
        
        Collection batches = null;
        try {
            batches = getExportBusiness(iwc).findAllBatches();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        row = 1;
        t.add(labelBatchNumber, 1, row);
        t.add(labelCreated, 2, row);
        t.add(labelType, 3, row);
        t.add(labelContractNumber, 4, row);
        t.add(labelSent, 5, row++);
        
        if (batches != null) {
            Iterator it = batches.iterator();
            while (it.hasNext()) {
                Batch batch = (Batch) it.next();
                if (batch.getBatchNumber() != null) {
                    t.add(batch.getBatchNumber(), 1, row);
                }
                
                if (batch.getCreated() != null) {
                    IWTimestamp created = new IWTimestamp(batch.getCreated());
                    t.add(created.getDateString("dd.MM.yyyy HH:mm:ss"), 2, row);
                }
                
                if (batch.getCreditCardType() != null) {
                    if (batch.getCreditCardType().getLocalizedKey() != null) {
                        t.add(iwrb.getLocalizedString(batch.getCreditCardType().getLocalizedKey(), batch.getCreditCardType().getLocalizedKey()), 3, row);
                    }
                }
                
                if (batch.getContract() != null) {
                    t.add(batch.getContract(), 4, row);
                }
                
                if (batch.getSent() != null) {
                    IWTimestamp sent = new IWTimestamp(batch.getSent());
                    t.add(sent.getDateString("dd.MM.yyyy HH:mm:ss"), 5, row);                    
                }
                
                row++;
            }
        }
        
        f.maintainParameter(CashierWindow.ACTION);
        f.maintainParameter(CashierWindow.PARAMETER_GROUP_ID);
        f.maintainParameter(CashierWindow.PARAMETER_DIVISION_ID);
        f.maintainParameter(CashierWindow.PARAMETER_CLUB_ID);

        f.add(inputTable);
        f.add(t);
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