/*
 * $Id: ApartmentTypeComplexHelper.java,v 1.4 2001/08/18 12:41:12 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.block.building.business;

import java.util.StringTokenizer;

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class ApartmentTypeComplexHelper {
  private int iKey_1,iKey_2;
  private String key_ = null;
  private String name_ = null;

  public ApartmentTypeComplexHelper() {
  }

  public ApartmentTypeComplexHelper(int key1, int key2){
    setKey(key1,key2);
  }

  public void setKey(int key1, int key2) {
    iKey_1 = key1;
    iKey_2 = key2;
    key_ = Integer.toString(key1) + "-" + Integer.toString(key2);
  }

  public int getKeyOne(){
    return iKey_1;
  }

  public int getKeyTwo(){
    return iKey_2;
  }

  public void setKey(String key) {
    key_ = key;
  }

  public String getKey() {
    return(key_);
  }

  public void setName(String name) {
    name_ = name;
  }

  public String getName() {
    return(name_);
  }

  public static int getPartKey(String key, int index) {
    StringTokenizer t = new StringTokenizer(key,"-");
    int not = t.countTokens();
    if (index > not)
      return(-1);

    int i = 0;
    while (t.hasMoreElements()) {
      i++;
      String txt = (String)t.nextElement();
      if (index == i) {
        int ret = -1;
        try {
          ret = Integer.parseInt(txt);
        }
        catch(java.lang.NumberFormatException e) {}

        return(ret);
      }
    }

    return(-1);
  }
}