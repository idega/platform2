//idega 2000 - Eiki

package com.idega.jmodule.poll.data;

//import java.util.*;
import java.sql.*;
import com.idega.jmodule.poll.data.*;
import com.idega.data.*;

public class PollOption extends GenericEntity{

	public PollOption(){
		super();
	}

	public PollOption(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("poll_id", "Spurning", true, true, "java.lang.Integer","many-to-one","com.idega.projects.golf.entity.Poll");
		addAttribute("answer","Svar",true,true,"java.lang.String");
	}

	public String getAnswer(){
		return getStringColumnValue("answer");
	}

	public int getPollID(){
		return getIntColumnValue("poll_id");
	}

	public Poll getPoll() {
		return (Poll)getColumnValue("poll_id");
	}

	public String getEntityName(){
		return "poll_option";
	}

	public String getName(){
		return getAnswer();
	}

	public int findNumberOfResponses()throws SQLException{
		return (findAssociated(new PollResult())).length;
	}

	public void setAnswer(String answer){
		setColumn("answer",answer);
	}

	public void setPoll(Poll poll){
		setColumn("poll_id",poll);
	}

	public void setPollID(int poll_id){
		//setPoll("poll_id",new Poll(poll_id));
		setColumn("poll_id",poll_id);
	}


	public void delete() throws SQLException{
		PollResult result = new PollResult();
		result.deleteMultiple("poll_option_id",Integer.toString(this.getID()));
		super.delete();
	}



}
