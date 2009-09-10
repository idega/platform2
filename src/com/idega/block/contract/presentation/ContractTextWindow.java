package com.idega.block.contract.presentation;



import com.idega.idegaweb.presentation.IWAdminWindow;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class ContractTextWindow extends IWAdminWindow{

private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.contract";

  public ContractTextWindow(){
    setWidth(570);
    setHeight(550);
    setResizable(true);
		setTitle("Contract Text");

  }

	public void main(com.idega.presentation.IWContext iwc){
	  add(new ContractTextSetter());
		addTitle("Contract Text");
	}

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }
}
