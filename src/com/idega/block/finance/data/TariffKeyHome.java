/*
 * $Id: TariffKeyHome.java,v 1.3 2004/11/17 22:50:55 aron Exp $
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
 * @version $Revision: 1.3 $
 */
public interface TariffKeyHome extends IDOHome {
    public TariffKey create() throws javax.ejb.CreateException;

    public TariffKey findByPrimaryKey(Object pk)
            throws javax.ejb.FinderException;

    /**
     * @see com.idega.block.finance.data.TariffKeyBMPBean#ejbFindByCategory
     */
    public Collection findByCategory(Integer categoryID) throws FinderException;

    /**
     * @see com.idega.block.finance.data.TariffKeyBMPBean#ejbFindAll
     */
    public Collection findAll() throws FinderException;

}
