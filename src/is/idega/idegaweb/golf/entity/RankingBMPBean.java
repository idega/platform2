//idega 2000 - Gimmi

package is.idega.idegaweb.golf.entity;

//import java.util.*;
import com.idega.data.GenericEntity;


public class RankingBMPBean extends GenericEntity implements Ranking{

	public String getEntityName(){
		return "ranking";
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("member_id","númer meðlims",true,true,"java.lang.Integer");
		addAttribute("score","staða",true, true , "java.lang.Integer");
		addAttribute("hole","hola",true, true , "java.lang.Integer");
		addAttribute("abbrevation","stytting",true,true, "java.lang.String");
		addAttribute("tournament_group_id","hópnúmer",true,true, "java.lang.Integer");
	}
	
	public void setTournamentGroupID(int id) {
		setColumn("tournament_group_id",id);	
	}
	
	public int getTournamentGroupID() {
		return getIntColumnValue("tournament_group_id");
	}
	
	
	public void setAbbrevation(String abbr) {
		setColumn("abbrevation",abbr);
	}

	public void setMemberID(int id) {
		setColumn("member_id",id);
	}

	public void setScore(int score) {
		setColumn("score",score);
	}
	
	public void setHole(int hole) {
		setColumn("hole",hole);
	}

	public String getAbbrevation() {
		return (String) getColumnValue("abbrevation");
	}
	
	public int getMemberID() {
		return getIntColumnValue("member_id");
	}

	public int getScore() {
		return getIntColumnValue("score");
	}
	
	public int getHole() {
		return getIntColumnValue("hole");
	}
	
	


}
