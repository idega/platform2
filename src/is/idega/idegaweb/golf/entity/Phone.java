//idega 2000 - eiki

package is.idega.idegaweb.golf.entity;

//import java.util.*;
import java.sql.*;
import com.idega.data.*;

public class Phone extends GenericEntity{

	public Phone(){
		super();
	}

	public Phone(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
                addAttribute(getIDColumnName());
                addAttribute("number","Númer",true,true,"java.lang.String");
                addAttribute("country_id","Land",true,true,"java.lang.Integer","one-to-many","is.idega.idegaweb.golf.entity.Country");
                addAttribute("phone_type","Tegund",true,true,"java.lang.String");
                addAttribute("phone_type_id","Tegund",true,true,"java.lang.Integer");

                addManyToManyRelationShip("is.idega.idegaweb.golf.entity.Member","member_phone");
                addManyToManyRelationShip("is.idega.idegaweb.golf.entity.Union","union_phone");

	}

	public String getEntityName(){
		return "phone";


	}

	public String getNumber(){
		return (String)getColumnValue("number");
	}

	public void setNumber(String number){
		setColumn("number", number);
	}

	public int getCountryId(){
		return getIntColumnValue("country_id");
	}

	public Country getCountry()throws SQLException{
		return (Country) getColumnValue("country_id");
	}

	public void setCountry(Country country){
		setColumn("country_id",new Integer(country.getID()));
	}

	public void setCountryId(Integer country_id){
		setColumn("country_id", country_id);
	}

        public void setCountryId(int country_id){
		setColumn("country_id", country_id);
	}

	public String getPhoneType(){

		return (String) getColumnValue("phone_type");

	}

		public void setPhoneType(String phone_type){
			setColumn("phone_type", phone_type);
	}

        public int getPhoneTypeId(){
            return getIntColumnValue("phone_type_id");
	}

	public void setPhoneTypeId(int phoneTypeId){
            setColumn("phone_type_id", phoneTypeId);
	}




}
