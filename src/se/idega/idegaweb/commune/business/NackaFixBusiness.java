/*
 * $Id: NackaFixBusiness.java,v 1.2 2004/12/07 21:21:54 laddi Exp $
 * Created on 7.12.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.business;




import com.idega.business.IBOService;


/**
 * Last modified: $Date: 2004/12/07 21:21:54 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
 */
public interface NackaFixBusiness extends IBOService {

	/**
	 * @see se.idega.idegaweb.commune.business.NackaFixBusinessBean#fixElementarySchoolPlacements
	 */
	public void fixElementarySchoolPlacements() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.business.NackaFixBusinessBean#fixChildCarePlacements
	 */
	public void fixChildCarePlacements() throws java.rmi.RemoteException;

}
