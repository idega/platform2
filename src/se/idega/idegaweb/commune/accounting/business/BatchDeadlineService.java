/*
 * $Id: BatchDeadlineService.java,v 1.1 2004/11/22 16:40:26 aron Exp $
 * Created on 12.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.accounting.business;


import se.idega.idegaweb.commune.accounting.invoice.data.BatchDeadline;

import com.idega.business.IBOService;
import com.idega.util.TimePeriod;

/**
 * 
 *  Last modified: $Date: 2004/11/22 16:40:26 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.1 $
 */
public interface BatchDeadlineService extends IBOService {
    /**
     * @see se.idega.idegaweb.commune.accounting.business.BatchDeadlineServiceBean#getValidPeriod
     */
    public TimePeriod getValidPeriod() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.accounting.business.BatchDeadlineServiceBean#hasDeadlinePassed
     */
    public boolean hasDeadlinePassed() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.accounting.business.BatchDeadlineServiceBean#getCurrentDeadlineDay
     */
    public int getCurrentDeadlineDay() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.accounting.business.BatchDeadlineServiceBean#getCurrentDeadline
     */
    public BatchDeadline getCurrentDeadline() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.accounting.business.BatchDeadlineServiceBean#storeDeadline
     */
    public BatchDeadline storeDeadline(int deadlineDay)
            throws java.rmi.RemoteException;

}
