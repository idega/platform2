/*
 * $Id: BatchDeadlineHome.java,v 1.1 2004/11/01 17:00:25 aron Exp $
 * Created on 1.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.accounting.invoice.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOHome;

/**
 * 
 *  Last modified: $Date: 2004/11/01 17:00:25 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.1 $
 */
public interface BatchDeadlineHome extends IDOHome {
    public BatchDeadline create() throws javax.ejb.CreateException;

    public BatchDeadline findByPrimaryKey(Object pk)
            throws javax.ejb.FinderException;

    /**
     * @see se.idega.idegaweb.commune.accounting.invoice.data.BatchDeadlineBMPBean#ejbFindAllCurrent
     */
    public Collection findAllCurrent() throws FinderException;

    /**
     * @see se.idega.idegaweb.commune.accounting.invoice.data.BatchDeadlineBMPBean#ejbFindCurrent
     */
    public BatchDeadline findCurrent() throws FinderException;

}
