package com.idega.block.cal.data;

import java.sql.Timestamp;
import java.util.Collection;

import com.idega.user.data.User;


public interface CalendarLedger extends com.idega.data.IDOEntity{
	public int getLedgerID();
	public Timestamp getDate();
	public String getName();
	public int getGroupID();
	public int getCoachGroupID();
	public Collection getUsers();
	public int getCoachID();
	public void setDate(Timestamp stamp);
	public void setName(String name);
	public void setGroupID(int groupID);
	public void addUser(User user);
	public void setCoachID(int coachID);
	public void setCoachGroupID(int coachGroupID);
	public void removeUserRelation();
	public void removeOneUserRelation(User user);

}
