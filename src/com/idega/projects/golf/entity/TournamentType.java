//idega 2000 - Tryggvi Larusson
/*
*Copyright 2000 idega.is All Rights Reserved.
*/

package com.idega.projects.golf.entity;

import java.sql.*;
import java.util.*;
import com.idega.data.*;


/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.2
*/
public class TournamentType extends GolfEntity{

	public TournamentType(){
		super();
	}

	public TournamentType(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("name","Tegund móts",true,true,"java.lang.String");
		addAttribute("tournament_type","Einkvæmur strengur",true,true,"java.lang.String");
		addAttribute("number_in_group","Hámarksfjöldi í hóp",true,true,"java.lang.String");
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

	public void setNumberInGroup(int number){
		setColumn("number_in_group",new Integer(number));
	}

	public int getNumberInGroup(){
		return getIntColumnValue("number_in_group");
	}


	public String getName(){
		return getStringColumnValue("name");
	}

	public void setName(String name){
		setColumn("name",name);
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
