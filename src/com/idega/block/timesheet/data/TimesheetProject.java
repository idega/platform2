package com.idega.block.timesheet.data;

import com.idega.block.projectmanager.data.Project;


public interface TimesheetProject extends com.idega.data.IDOEntity,Project
{
 public void addUser(com.idega.user.data.User p0)throws com.idega.data.IDOAddRelationshipException;
 public java.util.Collection getUsers();
 public void removeUser(com.idega.user.data.User p0)throws com.idega.data.IDORemoveRelationshipException;
}
