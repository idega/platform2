/*
 * Created on 2.1.2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.idega.block.poll.data;


import com.idega.data.GenericEntity;

/**
 * Title:		PollReplyBMPBean
 * Description:
 * Copyright:	Copyright (c) 2004
 * Company:		idega Software
 * @author		2004 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version		1.0
 */
public class PollReplyBMPBean extends GenericEntity {

	private static final String ENTITY_NAME ="PO_POLL_REPLY";
	private final static String COLUMNNAME_PARTICIPANT_KEY = "PARTICIPANT_KEY";
	private static final String COLUMNNAME_ANSWER = "ANSWER";
	private final static String COLUMNNAME_POLL_ID = "PO_POLL_ID";
	private final static String COLUMNNAME_QUESTION_ID = "PO_POLL_ID";

	
	
	
	/**
	 * 
	 */
	public PollReplyBMPBean() {
		super();
	}


	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(this.getIDColumnName());
		addAttribute(COLUMNNAME_PARTICIPANT_KEY,"Participant key",true,true,String.class,255);
		addAttribute(COLUMNNAME_ANSWER,"Answer",true,true,String.class,1000);
		
		addManyToOneRelationship(COLUMNNAME_POLL_ID,PollEntity.class);
		addManyToOneRelationship(COLUMNNAME_QUESTION_ID,PollQuestion.class);

	}
	
	public void setParticipantKey(String key){
		setColumn(COLUMNNAME_PARTICIPANT_KEY,key);
	}
	
	public void setAnswer(String answer){
		setColumn(COLUMNNAME_ANSWER,answer);
	}
	
	public void setPoll(PollEntity poll){
		setColumn(COLUMNNAME_POLL_ID,poll);
	}
	
	public void setPollQuestion(PollQuestion question){
		setColumn(COLUMNNAME_QUESTION_ID,question);
	}
	
	
	public String getParticipantKey(){
		return getStringColumnValue(COLUMNNAME_PARTICIPANT_KEY);
	}

	public String getAnswer(){
		return getStringColumnValue(COLUMNNAME_ANSWER);
	}

	public PollEntity getPoll(){
		return (PollEntity)getColumnValue(COLUMNNAME_POLL_ID);
	}

	public PollQuestion getPollQuestion(){
		return (PollQuestion)getColumnValue(COLUMNNAME_QUESTION_ID);
	}
	
	

}
