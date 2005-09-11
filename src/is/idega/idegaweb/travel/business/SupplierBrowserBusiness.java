/*
 * $Id: SupplierBrowserBusiness.java,v 1.3 2005/09/11 14:14:55 gimmi Exp $
 * Created on Aug 19, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.travel.business;

import is.idega.idegaweb.travel.service.business.ServiceHandler;
import java.io.File;
import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.block.basket.business.BasketBusiness;
import com.idega.business.IBOService;
import com.idega.data.IDOAddRelationshipException;
import com.idega.presentation.text.Paragraph;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.xml.XMLException;


/**
 * 
 *  Last modified: $Date: 2005/09/11 14:14:55 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.3 $
 */
public interface SupplierBrowserBusiness extends IBOService {

	/**
	 * @see is.idega.idegaweb.travel.business.SupplierBrowserBusinessBean#sendToCashier
	 */
	public void sendToCashier(Group supplierManager, String clientName, User cashier, User performer,
			BasketBusiness basketBusiness) throws CreateException, RemoteException, IDOAddRelationshipException;

	/**
	 * @see is.idega.idegaweb.travel.business.SupplierBrowserBusinessBean#parseXML
	 */
	public Collection parseXML(Group supplierManager, File xml) throws RemoteException, XMLException;

	/**
	 * @see is.idega.idegaweb.travel.business.SupplierBrowserBusinessBean#getParameters
	 */
	public Collection getParameters(String engineName, String searchFormName) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.SupplierBrowserBusinessBean#getBundleIdentifier
	 */
	public String getBundleIdentifier() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.SupplierBrowserBusinessBean#getServiceHandler
	 */
	public ServiceHandler getServiceHandler() throws java.rmi.RemoteException;
	public boolean isSeparator(Paragraph paragraph);
}
