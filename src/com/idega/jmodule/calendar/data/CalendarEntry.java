//idega 2000 - Gimmi

package com.idega.jmodule.calendar.data;

//import java.util.*;
import java.sql.*;
import com.idega.data.*;

public class CalendarEntry extends GenericEntity{

	public CalendarEntry(){
		super();
	}

	public CalendarEntry(int id)throws SQLException{
		super(id);
	}


	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("entry_date","date",true,true,"java.sql.Timestamp");
                addAttribute("header","Haus",true,true,"java.lang.String");
                addAttribute("body","Búkur",true,true,"java.lang.String",10000);
                addAttribute("time_from","frá",true,true,"java.lang.Double");
                addAttribute("time_to","til",true,true,"java.lang.Double");
                addAttribute("time_total","alls",true,true,"java.lang.Double");
                addAttribute("member_id","Starfsmannanúmer",true,true,"java.lang.Integer");
	}

	public String getIDColumnName() {
		return "calendar_entry_id";
	}

	public String getEntityName(){
		return "calendar_entry";
	}

        public void setDefaultValues() {
          this.setMemberId(-1);
        }

	public java.sql.Timestamp getDate(){
		return (java.sql.Timestamp) getColumnValue("entry_date");
	}

	public void setDate(java.sql.Timestamp date){
			setColumn("entry_date", date);
	}

        public void setMemberId(int member_id) {
            setColumn("member_id",new Integer(member_id));
        }

        public int getMemberId() {
            return getIntColumnValue("member_id");
        }
        public void setName(String name) {
            setHeader(name);
        }

        public String getName() {
            return getHeader();
        }

        public void setHeader(String header) {
            setColumn("header",header);
        }

        public String getHeader() {
            return getStringColumnValue("header");
        }

        public void setBody(String body) {
            setColumn("body",body);
        }

        public String getBody() {
            return getStringColumnValue("body");
        }

        public double getFrom() {
            return getDoubleColumnValue("time_from");
        }

        public void setFrom(double from) {
            setColumn("time_from",new Double(from));
        }

        public double getTo() {
            return getDoubleColumnValue("time_to");
        }

        public void setTo(double to) {
            setColumn("time_to",new Double(to));
        }

        public double getTotal() {
            return getDoubleColumnValue("time_total");
        }

        public void setTotal(double total) {
            setColumn("time_total",new Double(total));
        }
/*
        public void insert() {
            if ((this.getTo() != null) && (this.getFrom() != null)) {
                this.setTotal(this.getTo() - this.getFrom());
            }
            super.insert();
        }
        public void update() {
            if ((this.getTo() != null) && (this.getFrom() != null)) {
                this.setTotal(this.getTo() - this.getFrom());
            }
            super.update();
        }
*/

 }
