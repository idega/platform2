//idega 2000 - Eiki



package com.idega.block.poll.data;



//import java.util.*;

import java.sql.SQLException;

import com.idega.core.data.ICObjectInstance;



public class PollEntityBMPBean extends com.idega.data.GenericEntity implements com.idega.block.poll.data.PollEntity {



	public PollEntityBMPBean(){

		super();

	}



	public PollEntityBMPBean(int id)throws SQLException{

		super(id);

	}



	public void initializeAttributes(){

		addAttribute(getIDColumnName());

		addAttribute(getColumnNameAttribute(), "Attribute", true, true, String.class);

		addAttribute(com.idega.block.poll.data.PollQuestionBMPBean.getColumnNameID(), "Question", true, true, Integer.class,"many-to-one",PollQuestion.class);

    addManyToManyRelationShip(ICObjectInstance.class,"PO_POLL_IC_OBJECT_INSTANCE");

    addManyToManyRelationShip(PollQuestion.class,"PO_POLL_POLL_QUESTION");

	}



  public static String getEntityTableName(){ return "PO_POLL";}

  public static String getColumnNameID(){ return "PO_POLL_ID";}

  public static String getColumnNameAttribute(){ return "ATTRIBUTE";}



	public String getIDColumnName(){

		return getColumnNameID();

	}



	public String getEntityName(){

		return getEntityTableName();

	}



  public String getAttribute(){

		return (String)getColumnValue(getColumnNameAttribute());

	}



  public int getPollQuestionID(){

    return getIntColumnValue(com.idega.block.poll.data.PollQuestionBMPBean.getColumnNameID());

  }



  public void setAttribute(String attribute){

    setColumn(getColumnNameAttribute(),attribute);

  }



  public void setPollQuestionID(int pollQuestionID){

    setColumn(com.idega.block.poll.data.PollQuestionBMPBean.getColumnNameID(),pollQuestionID);

  }



  public void delete() throws SQLException {

    removeFrom(com.idega.core.data.ICObjectInstanceBMPBean.getStaticInstance(ICObjectInstance.class));

    removeFrom(com.idega.block.poll.data.PollQuestionBMPBean.getStaticInstance(PollQuestion.class));

    super.delete();

  }

}

