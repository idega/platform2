//idega 2000 - Eiki

package is.idega.idegaweb.golf.entity;

//import java.util.*;
import java.sql.SQLException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOLookup;

public class Poll_optionBMPBean extends GenericEntity implements Poll_option{

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
		return (findAssociated((Poll_result) IDOLookup.instanciateEntity(Poll_result.class))).length;
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
		Poll_result result = (Poll_result) IDOLookup.instanciateEntity(Poll_result.class);
		result.deleteMultiple("poll_option_id",Integer.toString(this.getID()));
		super.delete();
	}
	


}
