package com.idega.block.finance.data;




public interface BankInfo extends com.idega.data.IDOEntity {
	public int getClubId();
	public int getDivisionId();
	public int getGroupId();
	public String getClaimantsSSN();
	public String getClaimantsName();
	public String getClaimantsBankBranchNumber();
	public int getAccountBook();
	public String getAccountId();
	public String getUsername();
	public String getPassword();
	
	public void setClubId(int club);
	public void setDivisionId(int division);
	public void setGroupId(int groupId);
	public void setClaimantsSSN(String claimantsSSN);
	public void setClaimantsName(String claimantsName);
	public void setClaimantsBankBranchNumber(String bankBranchNumber);
	public void setAccountBook(int accountBook);
	public void setAccountId(String accountId);
	public void setUsername(String userName);
	public void setPassword(String password);
}
