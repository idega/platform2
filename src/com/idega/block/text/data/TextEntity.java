package com.idega.block.text.data;

public interface TextEntity extends com.idega.data.IDOLegacyEntity {
	/**
	 * @see com.idega.block.text.data.TextEntityBMPBean#getTextId
	 */
	public int getTextId();

	/**
	 * @see com.idega.block.text.data.TextEntityBMPBean#getText
	 */
	public TxText getText();

	/**
	 * @see com.idega.block.text.data.TextEntityBMPBean#setTextId
	 */
	public void setTextId(int text_id);
	
	public String getName();
	 public String getInfo();
	 public void setInfo(String info);
	 public void setName(String name);
}