package com.idega.block.poll.data;


public interface PollReply extends com.idega.data.IDOEntity
{
 public java.lang.String getAnswer();
 public java.lang.String getParticipantKey();
 public com.idega.block.poll.data.PollEntity getPoll();
 public com.idega.block.poll.data.PollQuestion getPollQuestion();
 public void setAnswer(java.lang.String p0);
 public void setParticipantKey(java.lang.String p0);
 public void setPoll(com.idega.block.poll.data.PollEntity p0);
 public void setPollQuestion(com.idega.block.poll.data.PollQuestion p0);
}
