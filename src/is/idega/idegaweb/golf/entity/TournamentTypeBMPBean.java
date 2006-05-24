//idega 2000 - Tryggvi Larusson
/*
*Copyright 2000 idega.is All Rights Reserved.
*/

package is.idega.idegaweb.golf.entity;

import java.sql.*;
import java.util.*;
import com.idega.data.*;


/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.2
*/
public class TournamentTypeBMPBean extends GenericEntity implements TournamentType{

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("tournament_type","Einkvæmur strengur",true,true,"java.lang.String");
		addAttribute("with_handicap","Með forgjöf",true,true,"java.lang.Boolean");
		addAttribute("without_handicap","Án forgjafar",true,true,"java.lang.Boolean");
		addAttribute("modifier","Margföldunarstuðull",true,true,"java.lang.Float");
		addAttribute("name","Tegund móts",true,true,"java.lang.String");
		addAttribute("use_groups", "use groups", true, true, Boolean.class);
     }

	public String getEntityName(){
		return "tournament_type";
	}

	public void setTournamentType(String tournamentType){
		setColumn("tournament_type",tournamentType);
	}

	public String getTournamentType(){
		return getStringColumnValue("tournament_type");
	}

        public void setWithHandicap(boolean with_handicap) {
            setColumn("with_handicap",with_handicap);
        }

        public boolean getWithHandicap() {
            return getBooleanColumnValue("with_handicap");
        }

        public void setWithoutHandicap(boolean without_handicap) {
            setColumn("without_handicap",without_handicap);
        }

        public boolean getWithoutHandicap() {
            return getBooleanColumnValue("without_handicap");
        }

        public void setModifier(float modifier) {
            setColumn("modifier",modifier);
        }

        public float getModifier() {
            return getFloatColumnValue("modifier");
        }

	public String getName(){
		return getStringColumnValue("name");
	}

	public void setName(String name){
		setColumn("name",name);
	}
	
	public void setUseGroups(boolean useGroups) {
		setColumn("use_groups", useGroups);
	}
	
	public boolean getUseGroups() {
		return getBooleanColumnValue("use_groups");
	}

        /**
         * Returns all the tournamentgroups ordered by name and without the internal no_tournament
         */
        public List getVisibleTournamentTypes(){
          try{
            return EntityFinder.findAll(this,"select * from "+getTableName()+" where "+this.getIDColumnName()+"!='1' order by name");
          }
          catch(SQLException ex){
            ex.printStackTrace(System.err);
            return null;
          }
        }

}