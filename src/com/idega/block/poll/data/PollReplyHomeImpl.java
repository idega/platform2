package com.idega.block.poll.data;


public class PollReplyHomeImpl extends com.idega.data.IDOFactory implements PollReplyHome
{
 protected Class getEntityInterfaceClass(){
  return PollReply.class;
 }


 public PollReply create() throws javax.ejb.CreateException{
  return (PollReply) super.createIDO();
 }


 public PollReply findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (PollReply) super.findByPrimaryKeyIDO(pk);
 }



}