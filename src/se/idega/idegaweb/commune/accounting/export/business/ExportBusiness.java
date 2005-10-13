/*
 * $Id: ExportBusiness.java,v 1.5 2005/10/13 08:09:38 palli Exp $
 * Created on Oct 11, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.accounting.export.business;

import java.util.Collection;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.accounting.export.data.ExportDataMapping;

import com.idega.business.IBOService;


/**
 * 
 *  Last modified: $Date: 2005/10/13 08:09:38 $ by $Author: palli $
 * 
 * @author <a href="mailto:bluebottle@idega.com">bluebottle</a>
 * @version $Revision: 1.5 $
 */
public interface ExportBusiness extends IBOService {

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.business.ExportBusinessBean#getAccountSettlementTypeDayByDay
	 */
	public int getAccountSettlementTypeDayByDay() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.business.ExportBusinessBean#getAccountSettlementTypeSpecificDate
	 */
	public int getAccountSettlementTypeSpecificDate() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.business.ExportBusinessBean#getExportDataMapping
	 */
	public ExportDataMapping getExportDataMapping(String operationalField) throws FinderException,
			java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.business.ExportBusinessBean#storeExportDataMapping
	 */
	public void storeExportDataMapping(String operationalField, String journalNumber, String account,
			String counterAccount, String payableAccount, String customerClaimAccount, String fileCreationFolder,
			String IFSFileFolder, String fileBackupFolder, String listCreationFolder, String listBackupFolder,
			int accountSettlementType, int standardPaymentDay, boolean cashFlowIn, boolean cashFlowOut,
			boolean providerAuthorization, boolean createOutSideCommune, boolean useSpecificDays, int specificDays)
			throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.business.ExportBusinessBean#getAllOperationalFields
	 */
	public Collection getAllOperationalFields() throws java.rmi.RemoteException;

}
