/*
 * Created on Jan 12, 2005
 *
 */
package com.idega.block.finance.business;

import com.idega.block.finance.data.BankInfo;


/**
 * InvoiceDataInsert is to used to insert data into files that are sent to the banks to create
 * invoices for a specific claimant. 
 * 
 * @author birna
 *
 */
public interface InvoiceDataInsert {
	
	/**
	 * Formats the file/data to be sent to the bank when creating claims
	 * @param batchNumber - the number of the batch / one batch includes a bunch of invoices
	 * @param groupId - the id for the group that is the claimant for the invoices
	 */
	public void createClaimsInBank(int batchNumber, int groupId);

	public void createClaimsInBank(int batchNumber, BankInfo info);
	
	/**
	 * Formats the file/data to be sent to the bank when receiving claim status
	 * and updates the status of the claims
	 * @param batchNumber
	 * @param groupId
	 */
	public void getClaimStatusFromBank(int batchNumber, int groupId, java.util.Date from, java.util.Date to);

	public void getClaimStatusFromBank(int batchNumber, BankInfo info, java.util.Date from, java.util.Date to);

	/**
	 * Formats the file/data to be sent to the bank when deleting a single claim
	 * @param groupId
	 * @param claimNumber
	 * @param dueDate
	 * @param payersSSN
	 */
	public void deleteClaim(int groupId, int claimNumber, java.util.Date dueDate, String payersSSN);
	
	public void deleteClaim(BankInfo info, int claimNumber, java.util.Date dueDate, String payersSSN);

}
