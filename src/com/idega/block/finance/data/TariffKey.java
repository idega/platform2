/*
 * $Id: TariffKey.java,v 1.9 2004/11/17 22:50:55 aron Exp $
 * Created on 17.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.finance.data;



import com.idega.block.category.data.CategoryEntity;
import com.idega.data.IDOEntity;

/**
 * 
 *  Last modified: $Date: 2004/11/17 22:50:55 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.9 $
 */
public interface TariffKey extends IDOEntity, CategoryEntity {
    /**
     * @see com.idega.block.finance.data.TariffKeyBMPBean#getName
     */
    public String getName();

    /**
     * @see com.idega.block.finance.data.TariffKeyBMPBean#setName
     */
    public void setName(String name);

    /**
     * @see com.idega.block.finance.data.TariffKeyBMPBean#getInfo
     */
    public String getInfo();

    /**
     * @see com.idega.block.finance.data.TariffKeyBMPBean#setInfo
     */
    public void setInfo(String info);

}
