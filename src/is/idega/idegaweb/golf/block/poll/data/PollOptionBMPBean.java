//idega 2000 - Eiki

package is.idega.idegaweb.golf.block.poll.data;

//import java.util.*;
import is.idega.idegaweb.golf.block.poll.data.*;

import java.sql.*;
import com.idega.data.*;

public class PollOptionBMPBean extends GenericEntity implements PollOption{

	public PollOptionBMPBean(){
		super();
	}

	public PollOptionBMPBean(int id)throws SQLException{
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
		return (findAssociated(GenericEntity.getStaticInstance(PollResult.class))).length;
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
		PollResult result = (PollResult)GenericEntity.getStaticInstance(PollResult.class);
		result.deleteMultiple("poll_option_id",Integer.toString(this.getID()));
		super.delete();
	}



}
