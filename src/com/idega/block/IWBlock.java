package com.idega.block;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public interface IWBlock {

public final static String IW_CORE_BUNDLE_IDENTIFIER="com.idega.core";

  public boolean deleteBlock(int ICObjectInstanceId);
  public String getBundleIdentifier();
  public String getLocalizedNameKey();
  public String getLocalizedNameValue();

}