/*
 * $Id: TabAction.java,v 1.3 2001/11/08 15:40:40 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.service;


import com.idega.presentation.PresentationObject;

/**
 *
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public interface TabAction {

  public static final int NOACT = 0;
  public static final int ACT1 = 1;
  public static final int ACT2 = 2;
  public static final int ACT3 = 3;
  public static final int ACT4 = 4;
  public static final int ACT5 = 5;
  public static final int ACT6 = 6;
  public static final int ACT7 = 7;
  public static final int ACT8 = 8;
  public static final String sAction = "campus_action";

  PresentationObject getTabs();


}
