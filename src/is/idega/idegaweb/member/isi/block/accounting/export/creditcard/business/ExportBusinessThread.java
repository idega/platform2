/*
 * Copyright (C) 2004 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */

package is.idega.idegaweb.member.isi.block.accounting.export.creditcard.business;

import is.idega.idegaweb.member.isi.block.accounting.data.CreditCardContract;
import is.idega.idegaweb.member.isi.block.accounting.data.CreditCardContractHome;
import is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntry;
import is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryHome;
import is.idega.idegaweb.member.isi.block.accounting.data.PaymentType;
import is.idega.idegaweb.member.isi.block.accounting.data.PaymentTypeHome;
import is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.Batch;
import is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.BatchHome;

import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.user.data.Group;
import com.idega.util.IWTimestamp;


/**
 * @author palli
 *
 */
public class ExportBusinessThread extends Thread {
    
    private boolean isExport = false;
    private String dateFromString = null;
    private String dateToString = null;
    
    public ExportBusinessThread(String dateFrom, String dateTo, boolean isExport) {
        dateFromString = dateFrom;
        dateToString = dateTo;
        this.isExport = isExport;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run() {
        if (isExport) {
            IWTimestamp dateFrom = null;
            IWTimestamp dateTo = null;

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
            
            try {
                createBatches(dateFrom, dateTo);
                createFiles();
                encryptFiles();
                sendFiles();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                BatchRunning.releaseSendFilesBatch();            
            }            
        } else {
            BatchRunning.releaseGetFilesBatch();            
        }
        
    }
    
    private void createBatches(IWTimestamp dateFrom, IWTimestamp dateTo) throws IDOLookupException, FinderException, CreateException {
        Collection contracts = ((CreditCardContractHome) IDOLookup.getHome(CreditCardContract.class)).findAllClubContracts();

        PaymentTypeHome pHome = (PaymentTypeHome) IDOLookup.getHome(PaymentType.class);        
        PaymentType creditCard = pHome.findPaymentTypeCreditcard();

        if (contracts != null) {
            Iterator it = contracts.iterator();
            while (it.hasNext()) {
                CreditCardContract contract = (CreditCardContract) it.next();
                Group parent = contract.getGroup();
                Batch batch = null;
                BatchHome bHome = (BatchHome) IDOLookup.getHome(Batch.class);
                try {
                    batch = bHome.findUnsentByContractNumberAndType(contract.getContractNumber(), contract.getCardType());                    
                } catch (FinderException e) {
                }
                
                if (batch == null) {
                    batch = bHome.create();
                    batch.setBatchNumber(""); //Get batch number from plugin pr. type
                    batch.setContract(contract.getContractNumber());
                    batch.setCreated(IWTimestamp.getTimestampRightNow());
                    batch.setCreditCardType(contract.getCardType());
                }
                
                putEntriesIntoBatches(parent, contract, dateFrom, dateTo, creditCard, batch);       
            }
        }
    }
    
    private void putEntriesIntoBatches(Group group, CreditCardContract contract, IWTimestamp dateFrom, IWTimestamp dateTo, PaymentType paymentType, Batch batch) throws IDOLookupException, FinderException {
        CreditCardContractHome cHome = (CreditCardContractHome) IDOLookup.getHome(CreditCardContract.class);
        CreditCardContract groupContract = null;
        try {
            groupContract = cHome.findByGroupAndType(group, contract.getCardType());
        } catch (FinderException e) {
        }
        
        if (groupContract != null && !contract.equals(groupContract)) {
            return;
        }
        
        FinanceEntryHome fHome = (FinanceEntryHome) IDOLookup.getHome(FinanceEntry.class);
        Collection entries = fHome.findAllByGroupAndPaymentTypeNotInBatch(group, paymentType, dateFrom, dateTo);
        
        if (entries != null && !entries.isEmpty()) {
            Iterator it = entries.iterator();
            while (it.hasNext()) {
                FinanceEntry entry = (FinanceEntry) it.next();
                if (entry.getPaymentContract() != null) {
                    if (entry.getPaymentContract().getCardType().equals(contract.getCardType())) {
                        entry.setCreditCardBatch(batch);
                        entry.store();
                    }
                }
            }
        }
    }
    
    private void createFiles() {
        
    }
    
    private void encryptFiles() {
        
    }
    
    private void sendFiles() {
        
    }
}