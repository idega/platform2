//idega 2000 - Eiki

package is.idega.idegaweb.golf.entity;

//import java.util.*;
import java.sql.*;

public class Poll_option extends GolfEntity{

	public Poll_option(){
		super();	
	}
	
	public Poll_option(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("poll_id", "Spurning", true, true, "java.lang.Integer","many-to-one","is.idega.idegaweb.golf.entity.Poll");
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
		return (findAssociated(new Poll_result())).length;
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
		Poll_result result = new Poll_result();
		result.deleteMultiple("poll_option_id",Integer.toString(this.getID()));
		super.delete();
	}
	


}
