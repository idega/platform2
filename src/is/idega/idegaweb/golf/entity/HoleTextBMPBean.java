//idega 2000 - Gimmi



package is.idega.idegaweb.golf.entity;



//import java.util.*;

import java.sql.*;



public class HoleTextBMPBean extends is.idega.idegaweb.golf.entity.GolfEntityBMPBean implements is.idega.idegaweb.golf.entity.HoleText {



	public HoleTextBMPBean(){

		super();

	}



	public HoleTextBMPBean(int id)throws SQLException{

		super(id);

	}





	public void initializeAttributes(){

		addAttribute(getIDColumnName());

                addAttribute("field_id","Númer vallar",true,true,"java.lang.Integer","many-to-one","is.idega.idegaweb.golf.entity.Field");

                addAttribute("hole_number","Númer holu",true,true,"java.lang.Integer");

		addAttribute("text_id", "Félag", true, true, "java.lang.Integer","many-to-one","com.idega.jmodule.text.data.TextModule");

	}



        public String getEntityName(){

		return "hole_text";

	}



        public void setFieldId(int field_id) {

            setColumn("field_id",new Integer(field_id));

        }



        public int getFieldId() {

            return getIntColumnValue("field_id");

        }



        public void setHoleNumber(int hole_number) {

            setColumn("hole_number",new Integer(hole_number));

        }



        public int getHoleNumber() {

            return getIntColumnValue("hole_number");

        }



	public void setTextId(int text_id) {

            setColumn("text_id",new Integer(text_id));

        }



        public int getTextId() {

            return getIntColumnValue("text_id");

        }

}

