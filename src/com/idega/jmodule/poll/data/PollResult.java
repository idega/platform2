//idega 2000 - Eiki

package com.idega.jmodule.poll.data;

//import java.util.*;
import java.sql.*;
import com.idega.jmodule.poll.data.*;
import com.idega.data.*;

public class PollResult extends GenericEntity{

	public PollResult(){
		super();
	}

	public PollResult(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("poll_option_id", "Svar", true, true, "java.lang.Integer","many-to-one","com.idega.projects.golf.entity.Poll_option");
                addAttribute("hits","hits",true,true,"java.lang.Integer");
	}


        public int getHits() {
          return getIntColumnValue("hits");
        }

        public void setHits(int hits) {
          setColumn("hits",new Integer(hits));
        }

	public PollOption getOption(){
		return (PollOption) getColumnValue("poll_option_id");
	}

	public int getOptionId(){
		return getIntColumnValue("poll_option_id");
	}

	public String getEntityName(){
		return "poll_result";
	}


	public void setOption(int option_id){
		setColumn("poll_option_id",new Integer(option_id));
	}

	public void setOption(PollOption option){
		setColumn("poll_option_id",option);
	}

}
