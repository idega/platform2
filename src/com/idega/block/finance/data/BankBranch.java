/**
 * 
 */
package com.idega.block.finance.data;



import com.idega.data.IDOEntity;

/**
 * @author bluebottle
 *
 */
public interface BankBranch extends IDOEntity {
	/**
	 * @see com.idega.block.finance.data.BankBranchBMPBean#getBankBranchName
	 */
	public String getBankBranchName();

	/**
	 * @see com.idega.block.finance.data.BankBranchBMPBean#getBankBranchNumber
	 */
	public String getBankBranchNumber();

	/**
	 * @see com.idega.block.finance.data.BankBranchBMPBean#getBankId
	 */
	public int getBankId();

	/**
	 * @see com.idega.block.finance.data.BankBranchBMPBean#getBank
	 */
	public Bank getBank();

	/**
	 * @see com.idega.block.finance.data.BankBranchBMPBean#setBankBranchName
	 */
	public void setBankBranchName(String bankBranchName);

	/**
	 * @see com.idega.block.finance.data.BankBranchBMPBean#setBankBranchNumber
	 */
	public void setBankBranchNumber(String bankBranchNumber);

	/**
	 * @see com.idega.block.finance.data.BankBranchBMPBean#setBankId
	 */
	public void setBankId(int id);

}
