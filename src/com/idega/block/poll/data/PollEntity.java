//idega 2000 - Eiki

package com.idega.block.poll.data;

//import java.util.*;
import com.idega.data.*;
import com.idega.block.poll.data.*;
import com.idega.core.data.ICObjectInstance;
import java.sql.*;

public class PollEntity extends GenericEntity{

	public PollEntity(){
		super();
	}

	public PollEntity(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute(getColumnNameAttribute(), "Attribute", true, true, String.class);
		addAttribute(PollQuestion.getColumnNameID(), "Question", true, true, Integer.class,"many-to-one",PollQuestion.class);
    addManyToManyRelationShip(ICObjectInstance.class,"PO_POLL_IC_OBJECT_INSTANCE");
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
    return getIntColumnValue(PollQuestion.getColumnNameID());
  }

  public void setAttribute(String attribute){
    setColumn(getColumnNameAttribute(),attribute);
  }

  public void setPollQuestionID(int pollQuestionID){
    setColumn(PollQuestion.getColumnNameID(),pollQuestionID);
  }

}
