package com.idega.block.finance.data;


public interface BankBranch extends com.idega.data.IDOEntity {
	public String getBankBranchNumber();
	public String getBankBranchName();
	public int getBankId();
	
	public void setBankBranchNumber(String bankBranchNumber);
	public void setBankBranchName(String bankBranchName);
	public void setBankId(int id);
}
