/*
 * $Id: ApartmentTypeComplexHelper.java,v 1.1 2001/06/28 13:07:38 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.block.building.business;

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class ApartmentTypeComplexHelper {
  private String key_ = null;
  private String name_ = null;

  public ApartmentTypeComplexHelper() {
  }

  public void setKey(int key1, int key2) {
    key_ = Integer.toString(key1) + "-" + Integer.toString(key2);
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
}