/**
 * 
 */
package com.idega.block.finance.data;



import com.idega.data.IDOEntity;

/**
 * @author bluebottle
 *
 */
public interface Bank extends IDOEntity {
	/**
	 * @see com.idega.block.finance.data.BankBMPBean#getBankName
	 */
	public String getBankName();

	/**
	 * @see com.idega.block.finance.data.BankBMPBean#getBankSSN
	 */
	public String getBankSSN();

	/**
	 * @see com.idega.block.finance.data.BankBMPBean#getPluginName
	 */
	public String getPluginName();

	/**
	 * @see com.idega.block.finance.data.BankBMPBean#getShortName
	 */
	public String getShortName();

	/**
	 * @see com.idega.block.finance.data.BankBMPBean#setBankName
	 */
	public void setBankName(String bankName);

	/**
	 * @see com.idega.block.finance.data.BankBMPBean#setBankSSN
	 */
	public void setBankSSN(String bankSSN);

	/**
	 * @see com.idega.block.finance.data.BankBMPBean#setPluginName
	 */
	public void setPluginName(String pluginName);

	/**
	 * @see com.idega.block.finance.data.BankBMPBean#setShortName
	 */
	public void setShortName(String shortName);

}
