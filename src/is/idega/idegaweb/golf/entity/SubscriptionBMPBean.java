//idega 2000 - idega team

package is.idega.idegaweb.golf.entity;

//import java.util.*;
import com.idega.data.GenericEntity;

public class SubscriptionBMPBean extends GenericEntity implements Subscription {

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("union_id","Klúbbur",true,true,"java.lang.Integer");
		addAttribute("name","Nafn",true,true,"java.lang.String");
		addAttribute("ssc","Kennitala",true,true,"java.lang.String");
		addAttribute("home","Heimilisfang",true,true,"java.lang.String");
		addAttribute("zip","Póstnúmer",true,true,"java.lang.String");
		addAttribute("state","Bæjarfélag",true,true,"java.lang.String");
		addAttribute("hphone","Heimasími",true,true,"java.lang.String");
		addAttribute("email","Netfang",true,true,"java.lang.String");
		addAttribute("workplace","Vinnustaður",true,true,"java.lang.String");
		addAttribute("wphone","Vinnusími",true,true,"java.lang.String");
		addAttribute("rookie","Nýliði",true,true,"java.lang.String");
		addAttribute("inclub","Í klúbbi",true,true,"java.lang.String");
		addAttribute("club","Klúbbur",true,true,"java.lang.String");
		addAttribute("handicap","Forgjöf",true,true,"java.lang.String");
		addAttribute("misc","Annað",true,true,"java.lang.String");

       }

	public String getEntityName(){
		return "subscription";
	}

	public int getUnionID() {
		return getIntColumnValue("union_id");
	}

	public void setUnionID(int union_id) {
		setColumn("union_id",union_id);
	}

	public String getEmail() {
		return getStringColumnValue("email");
	}

	public void setEmail(String email) {
		setColumn("email",email);
	}

	public String getName(){
		return getStringColumnValue("name");
	}

	public void setName(String name){
		setColumn("name",name);
	}

	public String getSSC(){
		return getStringColumnValue("ssc");
	}

	public void setSSC(String ssc){
		setColumn("ssc",ssc);
	}

	public String getHome(){
		return getStringColumnValue("home");
	}

	public void setHome(String home){
		setColumn("home",home);
	}

	public String getZip(){
		return getStringColumnValue("zip");
	}

	public void setZip(String zip){
		setColumn("zip",zip);
	}

	public String getState(){
		return getStringColumnValue("state");
	}

	public void setState(String state){
		setColumn("state",state);
	}

	public String getHPhone(){
		return getStringColumnValue("hphone");
	}

	public void setHPhone(String hphone){
		setColumn("hphone",hphone);
	}

	public String getWPhone(){
		return getStringColumnValue("wphone");
	}

	public void setWPhone(String wphone){
		setColumn("wphone",wphone);
	}

	public String getWorkplace(){
		return getStringColumnValue("workplace");
	}

	public void setWorkplace(String workplace){
		setColumn("workplace",workplace);
	}

	public String getRookie(){
		return getStringColumnValue("rookie");
	}

	public void setRookie(String rookie){
		setColumn("rookie",rookie);
	}

	public String getInClub(){
		return getStringColumnValue("inclub");
	}

	public void setInClub(String inclub){
		setColumn("inclub",inclub);
	}

	public String getClub(){
		return getStringColumnValue("club");
	}

	public void setClub(String club){
		setColumn("club",club);
	}

	public String getHandicap(){
		return getStringColumnValue("handicap");
	}

	public void setHandicap(String handicap){
		setColumn("handicap",handicap);
	}

	public String getMisc(){
		return getStringColumnValue("misc");
	}

	public void setMisc(String misc){
		setColumn("misc",misc);
	}

}
