//idega 2000 - Eiki

package is.idega.idegaweb.golf.block.poll.data;

//import java.util.*;
import com.idega.data.*;

import is.idega.idegaweb.golf.block.poll.data.*;

import java.sql.*;

public class PollBMPBean extends GenericEntity implements Poll {

	public PollBMPBean(){
		super();
	}

	public PollBMPBean(int id)throws SQLException{
		super(id);
	}

	public void setDefaultValues(){
		setColumn("in_use",new Boolean(false));
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("question", "Spurning", true, true, "java.lang.String");
		addAttribute("start_time", "Hefst", true, true, "java.sql.Timestamp");
		addAttribute("end_time", "Lýkur", true, true, "java.sql.Timestamp");
		addAttribute("in_use","í Notkun", false, false, "java.lang.Boolean");
	}


	public String getEntityName(){
		return "poll";
	}
	public String getQuestion(){
		return getStringColumnValue("question");
	}
	public void setQuestion(String question){
		setColumn("question",question);
	}

	public Timestamp getStartTime(){
		return (Timestamp)getColumnValue("start_time");
	}

	public Timestamp getEndTime() {
		return (Timestamp)getColumnValue("end_time");
	}

        public void setStartTime(Timestamp from) {
              setColumn("start_time",from);
        }
        public void setEndTime(Timestamp from) {
              setColumn("end_time",from);
        }


	public String getName(){
		return getQuestion();
	}

	public PollOption[] findOptions()throws SQLException{
		return (PollOption[]) findAssociatedOrdered(GenericEntity.getStaticInstance(PollOption.class),"poll_option_id");
	}

	public void delete() throws SQLException{
		PollOption[] options = (PollOption[])(GenericEntity.getStaticInstance(PollOption.class)).findAllByColumn("poll_id",Integer.toString(this.getID()));
		for (int i = 0; i < options.length; i++){
			options[i].delete();
		}
		super.delete();
	}

	public boolean getInUse(){
		return getBooleanColumnValue("in_use");
	}


	public void setInUse(boolean inUse){
		setColumn("in_use",new Boolean(inUse));
	}

}
