/*
 * Copyright (C) 2004 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */
package com.idega.block.basket.presentation;

import com.idega.block.basket.business.BasketBusiness;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;

/**
 * A simple block to view the items in the basket.
 * 
 * @author palli
 */
public class SimpleBasketView extends Block {

    /**
     * 
     * @uml.property name="biz"
     * @uml.associationEnd multiplicity="(0 1)"
     */
    protected BasketBusiness biz = null;

    
    /**
     * Default constructor.
     */
    public SimpleBasketView() {
        
    }
    
    
    
    /* (non-Javadoc)
     * @see com.idega.presentation.PresentationObject#main(com.idega.presentation.IWContext)
     */
    public void main(IWContext iwc) throws Exception {
        // TODO Auto-generated method stub
        super.main(iwc);
    }
    
    /* (non-Javadoc)
     * @see com.idega.presentation.PresentationObject#_main(com.idega.presentation.IWContext)
     */
    public void _main(IWContext iwc) throws Exception {
        // TODO Auto-generated method stub
        super._main(iwc);
    }
}