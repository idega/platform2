//idega 2000 - Eiki



package com.idega.block.poll.data;



//import java.util.*;

import java.sql.SQLException;

import com.idega.block.text.data.LocalizedText;
import com.idega.data.GenericEntity;



public class PollAnswerBMPBean extends com.idega.data.GenericEntity implements com.idega.block.poll.data.PollAnswer {



	public PollAnswerBMPBean(){

		super();

	}



	public PollAnswerBMPBean(int id)throws SQLException{

		super(id);

	}



  public void initializeAttributes(){

		addAttribute(getIDColumnName());

		addAttribute(com.idega.block.poll.data.PollQuestionBMPBean.getColumnNameID(), "Question", true, true,Integer.class,"many-to-one",PollQuestion.class);

    addAttribute(getColumnNameHits(),"Hits",true,true,"java.lang.Integer");

    addManyToManyRelationShip(LocalizedText.class,"PO_POLL_ANSWER_LOCALIZED_TEXT");

    setNullable(com.idega.block.poll.data.PollQuestionBMPBean.getColumnNameID(),false);

    setNullable(getColumnNameHits(),false);

	}



	public String getEntityName(){

		return "PO_POLL_ANSWER";

	}



  public static String getColumnNameID(){ return "PO_POLL_ANSWER_ID";}

  public static String getColumnNameHits(){ return "HITS";}



	public String getIDColumnName(){

		return getColumnNameID();

	}



	public int getPollQuestionID(){

		return getIntColumnValue(com.idega.block.poll.data.PollQuestionBMPBean.getColumnNameID());

	}



  public int getHits() {

    return getIntColumnValue(getColumnNameHits());

  }





	public void setPollQuestionID(int pollQuestionID){

		setColumn(com.idega.block.poll.data.PollQuestionBMPBean.getColumnNameID(),pollQuestionID);

	}



  public void setHits(int hits) {

    setColumn(getColumnNameHits(),new Integer(hits));

  }





	public void delete() throws SQLException{

    removeFrom(GenericEntity.getStaticInstance(LocalizedText.class));

		super.delete();

	}

}

