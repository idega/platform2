package com.idega.block.poll.data;


public interface PollReplyHome extends com.idega.data.IDOHome
{
 public PollReply create() throws javax.ejb.CreateException;
 public PollReply findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

}