package com.idega.block.poll.data;


public interface PollQuestionHome extends com.idega.data.IDOHome
{
 public PollQuestion create() throws javax.ejb.CreateException;
 public PollQuestion createLegacy();
 public PollQuestion findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public PollQuestion findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public PollQuestion findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}