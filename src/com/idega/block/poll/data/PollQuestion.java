//idega 2000 - Eiki

package com.idega.block.poll.data;

//import java.util.*;
import com.idega.data.*;
import com.idega.block.poll.data.*;
import com.idega.block.text.data.LocalizedText;
import java.sql.*;

public class PollQuestion extends GenericEntity{

	public PollQuestion(){
		super();
	}

	public PollQuestion(int id)throws SQLException{
		super(id);
	}

  public void insertStartData()throws Exception{
    PollQuestion question = new PollQuestion();
      question.insert();

    LocalizedText text = new LocalizedText();
      text.setLocaleId(1);
      text.setHeadline("Is this a question?");
      text.insert();

    text.addTo(question);
  }

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute(getColumnNameStartTime(), "Begins", true, true, "java.sql.Timestamp");
		addAttribute(getColumnNameEndTime(), "Ends", true, true, "java.sql.Timestamp");
    this.addManyToManyRelationShip(LocalizedText.class,"PO_POLL_QUESTION_LOCALIZED_TEXT");
	}

  public static String getColumnNameID(){ return "PO_POLL_QUESTION_ID";}
  public static String getColumnNameStartTime(){ return "START_TIME";}
  public static String getColumnNameEndTime(){ return "END_TIME";}

	public String getIDColumnName(){
		return getColumnNameID();
	}

	public String getEntityName(){
		return "PO_POLL_QUESTION";
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

	public void delete() throws SQLException{
    removeFrom(LocalizedText.getStaticInstance(LocalizedText.class));
    PollAnswer.getStaticInstance(PollAnswer.class).deleteMultiple(getColumnNameID(),Integer.toString(this.getID()));
		super.delete();
	}
}
