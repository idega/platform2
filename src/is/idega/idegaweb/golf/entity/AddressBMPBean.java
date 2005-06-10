//idega 2000 - eiki

package is.idega.idegaweb.golf.entity;

//import java.util.*;
import java.sql.SQLException;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOLookup;

public class AddressBMPBean extends GenericEntity implements Address{

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		//addAttribute("first_name","Fornafn",true,true,"java.lang.String");
		addAttribute("street", "Heimilisfang", true, true, "java.lang.String");
		addAttribute("street_number", "Númer", true, true, "java.lang.String");
		addAttribute("season", "Árstíð", true, true, "java.lang.String");
		addAttribute("zipcode_id", "Póstnúmer", true, true, "java.lang.Integer", "one-to-many","is.idega.idegaweb.golf.entity.ZipCode");
		addAttribute("address_type", "Tegund heimisfangs", true, true, "java.lang.String");
		addAttribute("extra_info", "Auka uppl.", true, true, "java.lang.String");
		addAttribute("country_id", "Land id", true, true, "java.lang.Integer","one-to-many","is.idega.idegaweb.golf.entity.Country");

                      addManyToManyRelationShip(is.idega.idegaweb.golf.entity.Member.class,"member_address");
                      addManyToManyRelationShip(is.idega.idegaweb.golf.entity.Union.class,"union_address");

                      addIndex("IDX_ADDRESS_1", new String[]{"address_id", "street"});
                      addIndex("IDX_ADDRESS_2", "zipcode_id");
                      addIndex("IDX_ADDRESS_3", new String[]{"address_id","zipcode_id"});
        }

        public void setDefaultValues(){
          this.setStreetNumber("");
        }

	public String getEntityName(){
		return "address";
	}

	public String getName(){
		return getStreet();
	}

	public String getStreet(){
		return (String) getColumnValue("street");
	}

	public void setStreet(String street){
			setColumn("street",street);	}

	public String getStreetNumber(){
			return getStringColumnValue("street_number");
	}

	public void setStreetNumber(String street_number){
			setColumn("street_number",street_number);
	}

	public void setStreetNumber(int street_number){
			setColumn("street_number",street_number);
	}

	public String getSeason(){
		return (String) getColumnValue("season");
	}

	public void setSeason(String season){
		setColumn("season",season);
	}

	public int getZipcodeId(){
		return getIntColumnValue("zipcode_id");
	}

	public void setZipcode(ZipCode zipcode){
		setColumn("zipcode_id",zipcode);
	}

	public ZipCode getZipCode()throws SQLException{
		return (ZipCode) getColumnValue("zipcode_id");
	}

	public void setZipcodeId(Integer zipcode_id){
		setColumn("zipcode_id",zipcode_id);
	}

	public void setZipcodeId(int zipcode_id){
		setColumn("zipcode_id",zipcode_id);
	}

	public String getAddressType(){
		return (String) getColumnValue("address_type");
	}

	public void setAddressType(String address_type){
		setColumn("address_type",address_type);
	}

	public String getExtraInfo(){
		return (String) getColumnValue("extra_info");
	}

	public void setExtraInfo(String extra_info){
		setColumn("extra_info",extra_info);
	}

	public Country getCountry(){
		return (Country) getColumnValue("country_id");
	}

	public int getCountryId(){
		return getIntColumnValue("country_id");
	}

	public void setCountryId(Integer country_id){
		setColumn("country_id",country_id);
	}

	public void setCountryId(int country_id){
		setColumn("country_id",country_id);
	}

	//Many to many relations
	public void setMember(Member type){
		setColumn("address_id",new Integer(type.getID()));
	}

	public Member getMember()throws FinderException{
		return ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(getIntColumnValue("member_id"));
	}


	//public void addTo(Union union,String MemberShip type){
	//}

}
