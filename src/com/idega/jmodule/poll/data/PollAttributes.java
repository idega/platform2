//idega 2000 - Gimmi

package com.idega.jmodule.poll.data;


//import java.util.*;
import java.sql.*;
import com.idega.data.*;

public class PollAttributes extends GenericEntity{

	public PollAttributes(){
		super();
	}

	public PollAttributes(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("poll_id","númer auglýsingu",true,true, "java.lang.Integer","one-to-many","com.idega.jmodule.poll.data.Poll");
                addAttribute("attribute_name","nafn einkennis",true,true, "java.lang.String");
                addAttribute("attribute_id","númer einkennis",true,true, "java.lang.Integer");

        }

	public String getIDColumnName(){
		return "poll_attributes_id";
	}

	public String getEntityName(){
		return "poll_attributes";
	}

        public void setName(String name) {
          setAttributeName(name);
        }

        public String getName() {
          return getAttributeName();
        }

        public void setAttributeName(String name) {
          setColumn("attribute_name",name);
        }

        public String getAttributeName() {
          return (String) getStringColumnValue("attribute_name");
        }

        public void setAttributeId(int id) {
          setColumn("attribute_id",new Integer(id));
        }

        public int getAttributeId() {
          return getIntColumnValue("attribute_id");
        }

        public void setPollId(int id) {
          setColumn("poll_id",new Integer(id));
        }

        public int getPollId() {
          return getIntColumnValue("poll_id");
        }


}
