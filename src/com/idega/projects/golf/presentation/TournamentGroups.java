package com.idega.projects.golf.presentation;

import com.idega.jmodule.object.interfaceobject.*;
import com.idega.projects.golf.moduleobject.GolfDialog;
import com.idega.jmodule.object.*;
import java.sql.SQLException;

/**
*@author <a href="mailto:gimmi@idega.is">Grímur</a>
*@version 1.0
*/
 public class TournamentGroups extends TournamentAdmin {

    public TournamentGroups(){
        super();
        //System.out.println("TournamentModifier()");
    }


    public void main(ModuleInfo modinfo)throws Exception{
        GolfDialog dialog = new GolfDialog("Mótastjóri");
        add(dialog);

        Form form5 = new Form(Class.forName("com.idega.projects.golf.presentation.TournamentGroupCreator"));
//        Form form5 = new Form("createtournamentgroup.jsp");
        SubmitButton Button5 = new SubmitButton("b5","Nýr mótshópur");
        form5.add(Button5);
        dialog.add(form5);

        Form form6 = new Form(Class.forName("com.idega.projects.golf.presentation.TournamentGroupDeleter") );
//        Form form6 = new Form("deletetournamentgroup.jsp");
        SubmitButton Button6 = new SubmitButton("b5","Eyða mótshóp");
        form6.add(Button6);
        dialog.add(form6);
    }


}// class TournamentGroups


