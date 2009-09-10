package com.idega.block.projectmanager.data;


public interface Project extends com.idega.data.IDOEntity
{
 public int getForumId();
 public int getGroupId();
 public int getImportanceId();
 public int getIssueId();
 public java.lang.String getName();
 public int getParentId();
 public int getProjectGroupId();
 public int getProjectManagerId();
 public java.lang.String getProjectNumber();
 public int getProjectStatusId();
 public int getWheelGroupId();
 public boolean isGroupIdFinal();
 public boolean isProjectGroupIdFinal();
 public boolean isProjectManagerIdFinal();
 public boolean isValid();
 public boolean isWheelGroupIdFinal();
 public void setForumId(int p0);
 public void setGroupId(int p0);
 public void setGroupIdFinal(boolean p0);
 public void setImportanceId(int p0);
 public void setIssueId(int p0);
 public void setName(java.lang.String p0);
 public void setParentId(int p0);
 public void setProjectGroupId(int p0);
 public void setProjectGroupIdFinal(boolean p0);
 public void setProjectManagerId(int p0);
 public void setProjectManagerIdFinal(boolean p0);
 public void setProjectNumber(java.lang.String p0);
 public void setProjectStatusId(int p0);
 public void setValid(boolean p0);
 public void setWheelGroupId(int p0);
 public void setWheelGroupIdFinal(boolean p0);
}
