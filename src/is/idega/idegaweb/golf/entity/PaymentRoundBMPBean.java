//idega 2000 - aron

package is.idega.idegaweb.golf.entity;

//import java.util.*;
import java.sql.Timestamp;

import com.idega.data.GenericEntity;

public class PaymentRoundBMPBean extends GenericEntity implements PaymentRound{

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
                addAttribute("name","For�i",true,true,"java.lang.String");
		addAttribute("round_date","�lagningardagur",true,true,"java.sql.Timestamp");
                addAttribute("union_id", "�lagningar kl�bbur", true, true, "java.lang.Integer","many-to-one","is.idega.idegaweb.golf.entity.Union");
                addAttribute("totals", "Upph��", true, true, "java.lang.Integer");

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
