/*
 * $Id: FinanceServiceHome.java,v 1.2 2004/11/17 22:50:25 aron Exp $
 * Created on 17.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.finance.business;



import com.idega.business.IBOHome;

/**
 * 
 *  Last modified: $Date: 2004/11/17 22:50:25 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.2 $
 */
public interface FinanceServiceHome extends IBOHome {
    public FinanceService create() throws javax.ejb.CreateException,
            java.rmi.RemoteException;

}
