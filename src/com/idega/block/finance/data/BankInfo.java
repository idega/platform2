package com.idega.block.finance.data;



public interface BankInfo extends com.idega.data.IDOEntity {
	public String getClaimantsSSN();
	public int getClaimantsBankBranchNumber();
	public int getAccountBook();
	public String getAccountId();
	public int getGroupId();
	public String getUsername();
	public String getPassword();
	
	public void setClaimantsSSN(String claimantsSSN);
	public void setClaimantsBankBranchNumber(int bankBranchNumber);
	public void setAccountBook(int accountBook);
	public void setAccountId(String accountId);
	public void setGroupId(int groupId);
	public void setUsername(String userName);
	public void setPassword(String password);
}
