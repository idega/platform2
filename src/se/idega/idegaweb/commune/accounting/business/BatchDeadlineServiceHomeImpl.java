/*
 * $Id: BatchDeadlineServiceHomeImpl.java,v 1.1 2004/11/22 16:40:26 aron Exp $
 * Created on 12.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.accounting.business;



import com.idega.business.IBOHomeImpl;

/**
 * 
 *  Last modified: $Date: 2004/11/22 16:40:26 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.1 $
 */
public class BatchDeadlineServiceHomeImpl extends IBOHomeImpl implements
        BatchDeadlineServiceHome {
    protected Class getBeanInterfaceClass() {
        return BatchDeadlineService.class;
    }

    public BatchDeadlineService create() throws javax.ejb.CreateException {
        return (BatchDeadlineService) super.createIBO();
    }

}
