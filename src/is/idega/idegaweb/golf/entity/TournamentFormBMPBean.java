//idega 2000 - Tryggvi Larusson
/*
*Copyright 2000 idega.is All Rights Reserved.
*/

package is.idega.idegaweb.golf.entity;

import com.idega.data.GenericEntity;


/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.2
*/
public class TournamentFormBMPBean extends GenericEntity implements TournamentForm{

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("name","Tegund móts",true,true,"java.lang.String");
		addAttribute("tournament_form","Einkvæmur strengur",true,true,"java.lang.String");
	}

	public String getEntityName(){
		return "tournament_form";
	}

	public void setTournamentForm(String tournamentForm){
		setColumn("tournament_form",tournamentForm);
	}

	public String getTournamentForm(){
		return getStringColumnValue("tournament_form");
	}



	public String getName(){
		return getStringColumnValue("name");
	}

	public void setName(String name){
		setColumn("name",name);
	}

}
