package com.idega.block.forum.data;

import javax.ejb.*;

public interface ForumAdmin extends com.idega.data.IDOLegacyEntity
{
 public boolean getUseForums();
 public void setUseForums(boolean p0);
}
