package com.idega.block.poll.data;


public interface PollAnswerHome extends com.idega.data.IDOHome
{
 public PollAnswer create() throws javax.ejb.CreateException;
 public PollAnswer createLegacy();
 public PollAnswer findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public PollAnswer findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public PollAnswer findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}