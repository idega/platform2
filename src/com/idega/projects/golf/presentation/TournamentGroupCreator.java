package com.idega.projects.golf.presentation;

import com.idega.presentation.ui.*;
import com.idega.projects.golf.moduleobject.GolfDialog;
import com.idega.presentation.*;
import java.sql.SQLException;

/**
*@author <a href="mailto:gimmi@idega.is">Grímur Jónsson</a>
*@version 1.0
*/
 public class TournamentGroupCreator extends TournamentAdmin {

    public TournamentGroupCreator(){
        super();
        //System.out.println("TournamentModifier()");
    }


    public void main(IWContext iwc)throws SQLException{
        add("TournamentGroupCreator");
    }


}// class TournamentGroupCreator


