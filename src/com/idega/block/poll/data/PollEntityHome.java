package com.idega.block.poll.data;


public interface PollEntityHome extends com.idega.data.IDOHome
{
 public PollEntity create() throws javax.ejb.CreateException;
 public PollEntity createLegacy();
 public PollEntity findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public PollEntity findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public PollEntity findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}