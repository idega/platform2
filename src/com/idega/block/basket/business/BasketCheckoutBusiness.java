/*
 * Copyright (C) 2004 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */
package com.idega.block.basket.business;

import java.util.Map;

/**
 * 
 * @author palli
 *
 */
public interface BasketCheckoutBusiness {
    /**
     * Perform a checkout function on the ...
     * 
     * @param basket
     */
    public void checkout(Map basket);
}
