/*
 * $Id: ControlListBusinessBean.java,v 1.3 2003/10/30 09:09:06 kjell Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.invoice.business;

import java.util.Collection;
import java.util.ArrayList;

import com.idega.business.IBOServiceBean;

/** 
 * Business logic for the Control list
 * <p>
 * $Id: ControlListBusinessBean.java,v 1.3 2003/10/30 09:09:06 kjell Exp $
 *
 * @author Kelly
 */
public class ControlListBusinessBean extends IBOServiceBean implements ControlListBusiness {

	/**
	 * AdHoc thingy to get some values for the control list
	 * This will be connected to the invoice/batch run later
	 * 
	 * @author Kelly
	 */
	public Collection getControlListValues() {
		ArrayList arr = new ArrayList();
		arr.add(new Object[]{new Integer(1), "Förskola X", "65", "61", "173.276", "172.100"});
		arr.add(new Object[]{new Integer(2), "Skola Z", "60", "60", "123.176", "112.400"});
		arr.add(new Object[]{new Integer(3), "Daghem X", "68", "69", "133.276", "132.600"});
		arr.add(new Object[]{new Integer(4), "Förskola Y", "62", "61", "113.276", "112.500"});
		arr.add(new Object[]{new Integer(5), "Skola X", "61", "63", "193.276", "194.120"});
		arr.add(new Object[]{new Integer(6), "Förskola Z", "65", "66", "170.276", "170.120"});
		arr.add(new Object[]{new Integer(7), "Skola Y", "68", "68", "153.276", "152.150"});
		arr.add(new Object[]{new Integer(8), "Daghem Y", "69", "67", "178.276", "168.300"});
		return arr; 
	}	


}