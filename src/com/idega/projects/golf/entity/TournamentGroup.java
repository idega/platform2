//idega 2000 - Tryggvi Larusson
/*
*Copyright 2000 idega.is All Rights Reserved.
*/

package com.idega.projects.golf.entity;

import java.util.*;
import java.sql.*;
import com.idega.data.*;

/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.2
*/
public class TournamentGroup extends Group{


	public TournamentGroup(){
		super();
	}

	public TournamentGroup(int id)throws SQLException{
		super(id);
	}


	public void initializeAttributes(){
		super.initializeAttributes();
		addAttribute("union_id","Stofnað undir",true,true,"java.lang.Integer","one-to-many","com.idega.projects.golf.entity.Union");
		addAttribute("tee_color_id","Spilar á teig",true,true,"java.lang.Integer","one-to-many","com.idega.projects.golf.entity.TeeColor");
		addAttribute("handicap_min","Lágmarksforgjöf", true, true, "java.lang.Float");
		addAttribute("handicap_max","Hámarksforgjöf", true, true, "java.lang.Float");
		addAttribute("age_min","Lágmarksaldur", true, true, "java.lang.Integer");
		addAttribute("age_max","Hámarksaldur", true, true, "java.lang.Integer");
		addAttribute("gender","Kyn", true, true, "com.idega.util.Gender");
		//addAttribute("name","Nafn", true, true, "java.lang.String");

                addAttribute("name","Nafn",true,true,"java.lang.String");
                addAttribute("description","Lýsing",true,true,"java.lang.String");
                addAttribute("extra_info","Aðrar upplýsingar",false,false,"java.lang.String");
                addAttribute("group_type","Gerð hóps",false,false,"java.lang.String");


		//setVisible("group_type",false);
		//setVisible("extra_info",false);
	}

	public String getEntityName(){
		return "tournament_group";
	}



	public void setDefaultValues(){
		setColumn("group_type","tournament_group");
                setMaxHandicap(36);
                setMinHandicap(-1);
                setMinAge(0);
                setMaxAge(99);
	}



        public void setName(String name) {
            setColumn("name",name);
        }

        public String getDescription() {
            return getStringColumnValue("description");
        }

        public void setDescription(String description) {
            setColumn("description",description);
        }

        public String getExtraInfo() {
            return getStringColumnValue("extra_info");
        }

        public void setExtraInfo(String extra_info) {
            setColumn("extra_info",extra_info);
        }
        public String getGroupType() {
            return getStringColumnValue("group_type");
        }

        public void setGroupType(String group_type) {
            setColumn("group_type",group_type);
        }


        public int getRegistrationFee(Tournament tournament) {
            return getRegistrationFee(tournament.getID());
        }

        public int getRegistrationFee(int tournament_id) {
		Connection conn= null;
		Statement Stmt= null;
                String SQLString = null;
		ResultSetMetaData metaData;
                int fee = 0;
		try{
                        SQLString = "Select REGISTRATION_FEE from TOURNAMENT_TOURNAMENT_GROUP where TOURNAMENT_ID ="+tournament_id+" and TOURNAMENT_GROUP_ID ="+this.getID();
			conn = this.getConnection();
			Stmt = conn.createStatement();
			ResultSet RS = Stmt.executeQuery(SQLString);
			metaData = RS.getMetaData();
			while (RS.next()){
                            try {
                                fee = Integer.parseInt(RS.getString("REGISTRATION_FEE"));
                            }
                            catch (NumberFormatException n) {}
			}
			RS.close();

		}
                catch (SQLException s) {}
		finally{
			if(Stmt != null){
                            try {
				Stmt.close();
                            }
                            catch (SQLException s){}
			}
			if (conn != null){
				this.freeConnection(conn);
			}
		}



                return fee;



        }


	public float getMinHandicap(){
		return getFloatColumnValue("handicap_min");
	}

        public void setMinHandicap(float handicap){
          setColumn("handicap_min",handicap);
        }

	public float getMaxHandicap(){
		return getFloatColumnValue("handicap_max");
	}

        public void setMaxHandicap(float handicap){
          setColumn("handicap_max",handicap);
        }

	public int getMinAge(){
		return getIntColumnValue("age_min");
	}

        public void setMinAge(int age){
          setColumn("age_min",age);
        }

	public int getMaxAge(){
		return getIntColumnValue("age_max");
	}

        public void setMaxAge(int age){
          setColumn("age_max",age);
        }

        public void setGender(String gender) {
            setColumn("gender",gender);
        }

	public char getGender(){
            return getStringColumnValue("gender").charAt(0);
	}

        public String getGenderString() {
            return getStringColumnValue("gender");
        }

	/*public Gender getGender(){
            return (Gender)getColumnValue("gender");
	}*/

	public String getName(){
		return (String) getColumnValue("name");
	}

	public TeeColor getTeeColor(){
		return (TeeColor)getColumnValue("tee_color_id");
	}

        public int getTeeColorID() {
            return getIntColumnValue("tee_color_id");
        }

	public void setTeeColor(TeeColor color){
		setColumn("tee_color_id",color);
	}

        public void setTeeColor(int tee_color_id) {
            setColumn("tee_color_id",tee_color_id);
        }

        public void setUnion(Union union){
          setColumn("union_id",union);
        }

        public void setUnionID(int union_id){
          setColumn("union_id",union_id);
        }

        public Union getUnion(){
          return (Union)getColumnValue("union_id");
        }

        public int getUnionID() {
            return getIntColumnValue("union_id");
        }


        public List getTournamentGroupsForUnion(Union union)throws Exception{
          return EntityFinder.findAllByColumn(this,"union_id",union.getID());
        }

        public List getTournamentGroupsForUnion(int union_id)throws Exception{
          return EntityFinder.findAllByColumn(this,"union_id",union_id);
        }




}
