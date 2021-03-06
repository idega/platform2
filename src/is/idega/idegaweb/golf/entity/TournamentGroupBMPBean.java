//idega 2000 - Tryggvi Larusson
/*
*Copyright 2000 idega.is All Rights Reserved.
*/

package is.idega.idegaweb.golf.entity;

import java.util.*;
import java.sql.*;
import com.idega.data.*;

/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.2
*/
public class TournamentGroupBMPBean extends GroupBMPBean implements TournamentGroup{

	public void initializeAttributes(){
		super.initializeAttributes();
		addAttribute("union_id","Stofna� undir",true,true,java.lang.Integer.class,"one-to-many",Union.class);
		addAttribute("tee_color_id","Spilar � teig",true,true,java.lang.Integer.class,"one-to-many",is.idega.idegaweb.golf.entity.TeeColor.class);
		addAttribute("handicap_min","L�gmarksforgj�f", true, true, java.lang.Float.class);
		addAttribute("handicap_max","H�marksforgj�f", true, true, java.lang.Float.class);
		addAttribute("age_min","L�gmarksaldur", true, true, java.lang.Integer.class);
		addAttribute("age_max","H�marksaldur", true, true, java.lang.Integer.class);
		addAttribute("gender","Kyn", true, true, com.idega.util.Gender.class);
		//addAttribute("name","Nafn", true, true, "java.lang.String");

                //addAttribute("name","Nafn",true,true,"java.lang.String");
                //addAttribute("description","L�sing",true,true,"java.lang.String");
                //addAttribute("extra_info","A�rar uppl�singar",false,false,"java.lang.String");
                //addAttribute("group_type","Ger� h�ps",false,false,"java.lang.String");


		//setVisible("group_type",false);
		//setVisible("extra_info",false);

                  //addManyToManyRelationShip("is.idega.idegaweb.golf.entity.Tournament","tournament_tournament_group");
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
