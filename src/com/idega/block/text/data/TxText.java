package com.idega.block.text.data;

import javax.ejb.*;

public interface TxText extends com.idega.data.IDOLegacyEntity
{
 public java.lang.String getAttribute();
 public int getContentId();
 public void setAttribute(java.lang.String p0);
 public void setContentId(java.lang.Integer p0);
 public void setContentId(int p0);
}
