/*
 * $Id: Region.java,v 1.1 2001/05/16 19:00:33 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.experimental.builder;

import com.idega.jmodule.object.ModuleObject;
import java.util.Comparator;

/**
 * ?
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0alpha
 */
public final class Region extends ModuleObject {
  private boolean editable_;
  private int x_;
  private int y_ ;
  private String regionId_;

  public Region() {
    initialize(null,0,0,true);
  }

  public Region(String regionId) {
    initialize(regionId,0,0,true);
  }

  public Region(String regionId, int x, int y) {
    initialize(regionId,x,y,true);
  }

  public Region(String regionId, int x, int y, boolean editable) {
    initialize(regionId,x,y,editable);
  }

  private void initialize(String regionId, int x, int y, boolean editable) {
    editable_ = editable;
    x_ = x;
    y_ = y;
    regionId_ = regionId;
  }

  public void setRegionId(String regionId) {
    regionId_ = regionId;
  }

  public String getRegionId() {
    return(regionId_);
  }

  public void setX(int x) {
    x_ = x;
  }

  public int getX() {
    return(x_);
  }

  public void setY(int y) {
    y_ = y;
  }

  public int getY() {
    return(y_);
  }

  public void setEditable(boolean editable) {
    editable_ = editable;
  }

  public boolean getEditable() {
    return(editable_);
  }

  public boolean equals(Object o) {
    if (o instanceof Region) {
      Region cmp = (Region)o;
      if (cmp.regionId_.equalsIgnoreCase(regionId_))
        return(true);
      else
        return(false);
    }
    else
      return(false);
  }

  public String toString() {
    String ret = "Region: regionId = " + regionId_ + ", x = " + x_ + ", y = " + y_ + ", editable = " + editable_;

    return(ret);
  }
}