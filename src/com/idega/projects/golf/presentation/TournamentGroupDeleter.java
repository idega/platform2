package com.idega.projects.golf.presentation;

import com.idega.jmodule.object.interfaceobject.*;
import com.idega.projects.golf.moduleobject.GolfDialog;
import com.idega.jmodule.object.*;
import java.sql.SQLException;

/**
*@author <a href="mailto:gimmi@idega.is">Grímur</a>
*@version 1.0
*/
 public class TournamentGroupDeleter extends TournamentAdmin {

    public TournamentGroupDeleter(){
        super();
        //System.out.println("TournamentGroupDeleter()");
    }


    public void main(ModuleInfo modinfo)throws SQLException{
        add("TournamentGroupDeleter");
    }


}// class TournamentGroupDeleter


