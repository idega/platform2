//idega 2000 - aron

package com.idega.projects.golf.entity;

//import java.util.*;
import java.sql.*;

public class PaymentRound extends GolfEntity{

	public PaymentRound(){
		super();
	}

	public PaymentRound(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
                addAttribute("name","Forði",true,true,"java.lang.String");
		addAttribute("round_date","Álagningardagur",true,true,"java.sql.Timestamp");
                addAttribute("union_id", "Álagningar klúbbur", true, true, "java.lang.Integer","many-to-one","com.idega.projects.golf.entity.Union");
                addAttribute("totals", "Upphæð", true, true, "java.lang.Integer");

	}

        public String getName(){
		return getStringColumnValue("name");
	}

	public void setName(String name){
		setColumn("name", name);
	}

	public String getEntityName(){
		return "payment_round";
	}

	public Timestamp getRoundDate(){
		return (Timestamp) getColumnValue("round_date");
	}

	public void setRoundDate(Timestamp round_date){
		setColumn("round_date", round_date);
	}

        public int getUnionId(){
		return getIntColumnValue("union_id");
	}

        public void setUnionId(Integer union_id){
		setColumn("union_id", union_id);
	}

        public void setUnionId(int union_id){
		setColumn("union_id", union_id);
	}

        public int getTotals(){
		return getIntColumnValue("totals");
	}

	public void setTotals(Integer totals){
		setColumn("totals", totals);
	}

        public void setTotals(int totals){
		setColumn("totals", totals);
	}

}
