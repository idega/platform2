/*
 * Created on Jan 12, 2005
 *
 */
package com.idega.block.finance.business;


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
	
	/**
	 * Formats the file/data to be sent to the bank when receiving claim status
	 * and updates the status of the claims
	 * @param batchNumber
	 * @param groupId
	 */
	public void getClaimStatusFromBank(int batchNumber, int groupId);
}
