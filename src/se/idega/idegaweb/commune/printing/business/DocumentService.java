/*
 * $Id: DocumentService.java,v 1.1 2004/11/04 20:34:48 aron Exp $
 * Created on 4.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.printing.business;

import java.util.Collection;


import se.idega.idegaweb.commune.message.business.MessageBusiness;
import se.idega.idegaweb.commune.message.data.PrintMessage;

import com.idega.block.pdf.business.PrintingContext;
import com.idega.block.pdf.business.PrintingService;
import com.idega.business.IBOLookupException;
import com.idega.business.IBOService;
import com.idega.idegaweb.IWUserContext;

/**
 * 
 *  Last modified: $Date: 2004/11/04 20:34:48 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.1 $
 */
public interface DocumentService extends IBOService {
    /**
     * @see se.idega.idegaweb.commune.printing.business.DocumentServiceBean#createPDF
     */
    public Integer createPDF(IWUserContext iwuc, PrintMessage msg,
            String fileName, boolean flagPrinted)
            throws ContentCreationException, java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.printing.business.DocumentServiceBean#createPDF
     */
    public Integer createPDF(IWUserContext iwuc, String[] ids, String type,
            String fileName, boolean flagPrinted)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.printing.business.DocumentServiceBean#createPDF
     */
    public Integer createPDF(IWUserContext iwuc, Collection msgs, String type,
            String fileName, boolean flagPrinted)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.printing.business.DocumentServiceBean#getPrintingContext
     */
    public PrintingContext getPrintingContext(IWUserContext iwuc,
            PrintMessage msg) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.printing.business.DocumentServiceBean#getPrintingService
     */
    public PrintingService getPrintingService() throws IBOLookupException,
            java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.printing.business.DocumentServiceBean#getMessageService
     */
    public MessageBusiness getMessageService() throws IBOLookupException,
            java.rmi.RemoteException;

}
