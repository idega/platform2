package com.idega.block.finance.data;


public interface Bank extends com.idega.data.IDOEntity {
	public String getBankName();
	public String getBankSSN();
	
	public void setBankName(String bankName);
	public void setBankSSN(String bankSSN);
}
