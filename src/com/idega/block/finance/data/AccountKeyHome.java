/*
 * $Id: AccountKeyHome.java,v 1.4 2004/11/17 22:50:55 aron Exp $
 * Created on 17.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.finance.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOHome;

/**
 * 
 *  Last modified: $Date: 2004/11/17 22:50:55 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.4 $
 */
public interface AccountKeyHome extends IDOHome {
    public AccountKey create() throws javax.ejb.CreateException;

    public AccountKey findByPrimaryKey(Object pk)
            throws javax.ejb.FinderException;

    /**
     * @see com.idega.block.finance.data.AccountKeyBMPBean#ejbFindAll
     */
    public Collection findAll() throws FinderException;

    /**
     * @see com.idega.block.finance.data.AccountKeyBMPBean#ejbFindBySQL
     */
    public Collection findBySQL(String sql) throws FinderException;

    /**
     * @see com.idega.block.finance.data.AccountKeyBMPBean#ejbFindByCategory
     */
    public Collection findByCategory(Integer categoryID) throws FinderException;

    /**
     * @see com.idega.block.finance.data.AccountKeyBMPBean#ejbFindByPrimaryKeys
     */
    public Collection findByPrimaryKeys(String[] keys) throws FinderException;

}
