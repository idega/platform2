package is.idega.idegaweb.golf.startingtime.business;

import javax.ejb.FinderException;

import com.idega.util.IWTimestamp;


public interface TeeTimeBusiness extends com.idega.business.IBOService
{
 public int countEntriesInGroup(int p0,int p1,java.lang.String p2,com.idega.util.IWTimestamp p3)throws java.sql.SQLException, java.rmi.RemoteException;
 public int countEntriesInGroup(int p0,java.lang.String p1,com.idega.util.IWTimestamp p2)throws java.sql.SQLException, java.rmi.RemoteException;
 public int countMembersEntries(int p0,java.lang.String p1,com.idega.util.IWTimestamp p2)throws java.sql.SQLException, java.rmi.RemoteException;
 public int countOwnersEntries(int p0,java.lang.String p1,com.idega.util.IWTimestamp p2)throws java.sql.SQLException, java.rmi.RemoteException;
 public int entriesInGroup(int p0,java.lang.String p1,java.lang.String p2)throws java.sql.SQLException, java.rmi.RemoteException;
 public is.idega.idegaweb.golf.startingtime.data.TeeTime[] findAllPlayersByMemberOrdered(java.lang.String p0,java.lang.String p1,java.lang.String p2)throws java.io.IOException,java.sql.SQLException, java.rmi.RemoteException;
 public is.idega.idegaweb.golf.startingtime.data.TeeTime[] findAllPlayersInFieldOrdered(java.lang.String p0,java.lang.String p1)throws java.io.IOException,java.sql.SQLException, java.rmi.RemoteException;
 public is.idega.idegaweb.golf.entity.StartingtimeFieldConfig getFieldConfig(int p0,java.lang.String p1)throws java.sql.SQLException, java.rmi.RemoteException;
 public is.idega.idegaweb.golf.entity.StartingtimeFieldConfig getFieldConfig(int p0,com.idega.util.IWTimestamp p1)throws java.sql.SQLException, java.rmi.RemoteException;
 public java.lang.String getFieldName(int p0)throws java.sql.SQLException,javax.ejb.FinderException, java.rmi.RemoteException;
 public is.idega.idegaweb.golf.entity.Field[] getFields(java.lang.String p0)throws java.sql.SQLException, java.rmi.RemoteException;
 public int getFirstField(java.lang.String p0)throws java.sql.SQLException, java.rmi.RemoteException;
 public com.idega.util.IWTimestamp getFirstOpentime(IWTimestamp date)throws java.sql.SQLException, java.rmi.RemoteException, FinderException;
 public com.idega.util.IWTimestamp getLastClosetime(IWTimestamp date)throws java.sql.SQLException, java.rmi.RemoteException, FinderException;
 public int getMaxDaysShown(IWTimestamp date)throws java.sql.SQLException, java.rmi.RemoteException, FinderException;
 public is.idega.idegaweb.golf.startingtime.data.TeeTime[] getPlayersStartingToDay(java.lang.String p0,java.lang.String p1,java.lang.String p2,java.lang.String p3)throws java.sql.SQLException, java.rmi.RemoteException;
 public is.idega.idegaweb.golf.startingtime.data.TeeTime[] getPlayersStartingToDay(java.lang.String p0,java.lang.String p1)throws java.sql.SQLException, java.rmi.RemoteException;
 public is.idega.idegaweb.golf.entity.Field[] getStartingEntryField()throws java.sql.SQLException, java.rmi.RemoteException;
 public is.idega.idegaweb.golf.entity.Union[] getStartingEntryUnion()throws java.sql.SQLException, java.rmi.RemoteException;
 public is.idega.idegaweb.golf.startingtime.data.TeeTime getStartingtime(int p0,com.idega.util.IWTimestamp p1)throws java.sql.SQLException, java.rmi.RemoteException;
 public java.util.List getStartingtimeTableEntries(com.idega.util.IWTimestamp p0,java.lang.String p1,int p2,int p3)throws java.sql.SQLException, java.rmi.RemoteException;
 public java.util.List getStartingtimeTableEntries(com.idega.util.IWTimestamp p0,java.lang.String p1)throws java.sql.SQLException, java.rmi.RemoteException;
 public is.idega.idegaweb.golf.startingtime.data.TeeTime[] getTableEntries(java.lang.String p0,int p1,int p2,int p3)throws java.sql.SQLException, java.rmi.RemoteException;
 public int getFieldUnion(int p0)throws java.sql.SQLException,javax.ejb.FinderException, java.rmi.RemoteException;
 public void preSetStartingtime(int p0,java.lang.String p1,java.lang.String p2)throws java.sql.SQLException, java.rmi.RemoteException;
 public void setStartingtime(int p0,com.idega.util.IWTimestamp p1,java.lang.String p2,java.lang.String p3,java.lang.String p4,java.lang.String p5,java.lang.String p6,java.lang.String p7,java.lang.String p8,java.lang.String p9)throws java.sql.SQLException, java.rmi.RemoteException;
 public void setStartingtime(int p0,com.idega.util.IWTimestamp p1,java.lang.String p2,java.lang.String p3,java.lang.String p4,java.lang.String p5,java.lang.String p6,java.lang.String p7,java.lang.String p8)throws java.sql.SQLException, java.rmi.RemoteException;
 public void setStartingtime(int p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,java.lang.String p4,java.lang.String p5,java.lang.String p6,java.lang.String p7,java.lang.String p8)throws java.sql.SQLException, java.rmi.RemoteException;
}
