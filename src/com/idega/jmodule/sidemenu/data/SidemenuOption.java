//idega 2000 - Gimmi

package com.idega.jmodule.sidemenu.data;


//import java.util.*;
import java.sql.*;
import com.idega.data.*;

public class SidemenuOption extends GenericEntity{

	public SidemenuOption(){
		super();
	}

	public SidemenuOption(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
                addAttribute("sidemenu_option_name","nafn undirflokks",true,true,"java.lang.String");
                addAttribute("parent_id","parent_id",true,true,"java.lang.Integer");
                addAttribute("module_object_class_name","Nafn arachnea objects",true,true,"java.lang.String");
                addAttribute("entity_id_column_name","Nafn id_column entitys",true,true,"java.lang.String");
                addAttribute("entity_id_column_value","Gildi id_column entitys",true,true,"java.lang.Integer");

                this.addManyToManyRelationShip(com.idega.builder.data.IBEntity.class,"sidemenu_option_ib_entity");
                this.addManyToManyRelationShip(SidemenuModule.class,"sidemenu_sidemenu_option");
        }

	public String getIDColumnName(){
		return "sidemenu_option_id";
	}

	public String getEntityName(){
		return "sidemenu_option";
	}


        public void setModuleObjectClassName(String class_name) {
          setColumn("module_object_class_name",class_name);
        }
        public String getModuleObjectClassName() {
          return getStringColumnValue("module_object_class_name");
        }
        public void setEntityIdColumnName(String column_name) {
          setColumn("entity_id_column_name",column_name);
        }
        public String getEntityIdColumnName() {
          return getStringColumnValue("entity_id_column_name");
        }
        public void setEntityIdColumnValue(int column_value) {
          setColumn("entity_id_column_value",new Integer(column_value));
        }
        public int getEntityIdColumnValue() {
          return getIntColumnValue("entity_id_column_value");
        }



        public void setName(String name) {
          setColumn("sidemenu_option_name",name);
        }

        public String getName() {
          return getStringColumnValue("sidemenu_option_name");
        }

        public void setParentId(int parent_id) {
          setColumn("parent_id",new Integer(parent_id));
        }

        public int getParentId() {
          return getIntColumnValue("parent_id");
        }

}
