//idega 2000 - Gimmi

package com.idega.jmodule.calendar.data;

//import java.util.*;
import java.sql.*;
import com.idega.data.*;
import com.idega.jmodule.calendar.data.*;

public class FaerslurExtra extends GenericEntity{

	public FaerslurExtra(){
		super();
	}

	public FaerslurExtra(int id)throws SQLException{
		super(id);
	}


	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("faerslu_id","faerslu_id",true,true,"java.lang.Integer","many-to-one","com.idega.jmodule.calendar.data.Faerslur");
		addAttribute("fra","fra",true,true,"java.lang.Double");
		addAttribute("til","til",true,true,"java.lang.Double");
		addAttribute("ath","ath",true,true,"java.lang.String");
	}

	public String getIDColumnName() {
		return "faerslur_extra_id";
	}

	public String getEntityName(){
		return "faerslur_extra";
	}

/*	public Faerslur getFaerslur() {
		Faerslur faerslur = null;
		try {
			faerslur = new Faerslur(getFaersluId());
		}
		catch (Exception e ) {
		}
		
		return faerslur;
	}
*/
	public int getFaersluId() {
		return getIntColumnValue("faerslu_id");
	}
	
	public void setFaersluId(String faerslu_id) {
		setColumn("faerslur_id",faerslu_id);
	}
	
	public double getFra() {
		return ((Double)getColumnValue("fra")).doubleValue();
	}

	public void setFra(double fra) {
		setColumn("fra",new Double(fra));
	}

	public double getTil() {
		return ((Double)getColumnValue("til")).doubleValue();
	}

	public void setTil(double til) {
		setColumn("til",new Double(til));
	}

	public String getAth() {
		return getStringColumnValue("ath");
	}	
	
	public void setAth(String ath) {
		setColumn("ath",ath);
	}

}
