/**
 * 
 */
package com.idega.block.finance.data;



import com.idega.data.IDOEntity;
import com.idega.user.data.Group;

/**
 * @author bluebottle
 *
 */
public interface BankInfo extends IDOEntity {
	/**
	 * @see com.idega.block.finance.data.BankInfoBMPBean#getAccountBook
	 */
	public int getAccountBook();

	/**
	 * @see com.idega.block.finance.data.BankInfoBMPBean#getAccountId
	 */
	public String getAccountId();

	/**
	 * @see com.idega.block.finance.data.BankInfoBMPBean#getClaimantsBankBranchId
	 */
	public int getClaimantsBankBranchId();

	/**
	 * @see com.idega.block.finance.data.BankInfoBMPBean#getClaimantsBankBranch
	 */
	public BankBranch getClaimantsBankBranch();

	/**
	 * @see com.idega.block.finance.data.BankInfoBMPBean#getClubId
	 */
	public int getClubId();

	/**
	 * @see com.idega.block.finance.data.BankInfoBMPBean#getClub
	 */
	public Group getClub();

	/**
	 * @see com.idega.block.finance.data.BankInfoBMPBean#getDivisionId
	 */
	public int getDivisionId();

	/**
	 * @see com.idega.block.finance.data.BankInfoBMPBean#getDivision
	 */
	public Group getDivision();

	/**
	 * @see com.idega.block.finance.data.BankInfoBMPBean#getClaimantsSSN
	 */
	public String getClaimantsSSN();

	/**
	 * @see com.idega.block.finance.data.BankInfoBMPBean#getClaimantsName
	 */
	public String getClaimantsName();

	/**
	 * @see com.idega.block.finance.data.BankInfoBMPBean#getGroupId
	 */
	public int getGroupId();

	/**
	 * @see com.idega.block.finance.data.BankInfoBMPBean#getGroup
	 */
	public Group getGroup();

	/**
	 * @see com.idega.block.finance.data.BankInfoBMPBean#getUsername
	 */
	public String getUsername();

	/**
	 * @see com.idega.block.finance.data.BankInfoBMPBean#getPassword
	 */
	public String getPassword();

	/**
	 * @see com.idega.block.finance.data.BankInfoBMPBean#setAccountBook
	 */
	public void setAccountBook(int accountBook);

	/**
	 * @see com.idega.block.finance.data.BankInfoBMPBean#setAccountId
	 */
	public void setAccountId(String accountId);

	/**
	 * @see com.idega.block.finance.data.BankInfoBMPBean#setClaimantsBankBranchId
	 */
	public void setClaimantsBankBranchId(int bankBranchId);

	/**
	 * @see com.idega.block.finance.data.BankInfoBMPBean#setClaimantsBankBranchNumber
	 */
	public void setClaimantsBankBranchNumber(BankBranch bankBranch);

	/**
	 * @see com.idega.block.finance.data.BankInfoBMPBean#setClubId
	 */
	public void setClubId(int clubId);

	/**
	 * @see com.idega.block.finance.data.BankInfoBMPBean#setDivisionId
	 */
	public void setDivisionId(int divisionId);

	/**
	 * @see com.idega.block.finance.data.BankInfoBMPBean#setClaimantsSSN
	 */
	public void setClaimantsSSN(String claimantsSSN);

	/**
	 * @see com.idega.block.finance.data.BankInfoBMPBean#setClaimantsName
	 */
	public void setClaimantsName(String claimantsName);

	/**
	 * @see com.idega.block.finance.data.BankInfoBMPBean#setGroupId
	 */
	public void setGroupId(int groupId);

	/**
	 * @see com.idega.block.finance.data.BankInfoBMPBean#setUsername
	 */
	public void setUsername(String username);

	/**
	 * @see com.idega.block.finance.data.BankInfoBMPBean#setPassword
	 */
	public void setPassword(String pwd);

}
