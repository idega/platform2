package com.idega.projects.golf.presentation;

import com.idega.jmodule.login.business.AccessControl;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.projects.golf.moduleobject.GolfDialog;
import com.idega.projects.golf.entity.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.projects.golf.business.TournamentController;

/**
*@author <a href="mailto:gimmi@idega.is">Grímur</a>
*@version 1.0
*/
 public class TournamentModifier extends TournamentAdmin {

    public TournamentModifier(){
        super();
        //System.out.println("TournamentModifier()");
    }


public void main(ModuleInfo modinfo) throws Exception{
	//initializeButtons();
        //System.out.println("TournamentModifier.main()");

	String tournament_id=modinfo.getParameter("tournament");

	if (tournament_id == null){
                GolfDialog dialog = new GolfDialog("Breyta móti");
                add(dialog);
                Form form = new Form(Class.forName("com.idega.projects.golf.presentation.TournamentCreator"));
//                Form form = new Form("createtournament.jsp");
                form.add(new HiddenInput("tournament_control_mode","edit"));
                dialog.add(new Text("Veldu mót:"));
                dialog.add(form);



                        Tournament tournament = new Tournament();
                        DropdownMenu Dropdown2 = new DropdownMenu();
                        if (AccessControl.isClubAdmin(modinfo)) {
                            com.idega.data.genericentity.Member member = com.idega.jmodule.login.business.AccessControl.getMember(modinfo);
                            int member_id = member.getID();
                            Member golfMember = new Member(member_id);
                            int main_union_id = golfMember.getMainUnionID();
                            Dropdown2 = TournamentController.getDropdownOrderedByUnion(new DropdownMenu("tournament"),modinfo);

//                            Dropdown2 = new DropdownMenu(tournament.findAll("Select * from tournament where union_id =3 OR union_id = "+main_union_id+" order by name"));
                        }
                        else if (AccessControl.isAdmin(modinfo)) {
                            Dropdown2 = TournamentController.getDropdownOrderedByUnion(new DropdownMenu("tournament"),modinfo);
//                            Dropdown2 = TournamentController. new DropdownMenu(tournament.findAllOrdered("name"));
                            //Dropdown2 = new DropdownMenu(tournament.findAllOrdered("name"));
                        }
                            Dropdown2.setAttribute("size","10");
                            //Dropdown2.setToSubmit();
                        form.add(Dropdown2);


                SubmitButton editTournament1 = new SubmitButton("et1","Áfram");
                form.add(editTournament1);
        }
        else{
          GolfDialog dialog = new GolfDialog("Breyta móti");
          add(dialog);
	  Tournament tournament = new Tournament(Integer.parseInt(tournament_id));
	  Tournament[] tournaments = (Tournament[])java.lang.reflect.Array.newInstance(tournament.getClass(),1);
	  tournaments[0]=tournament;

          EntityUpdater updater = new EntityUpdater(tournaments);

	  dialog.add( updater );

          if(updater.thisObjectSubmitted(modinfo)){
            dialog.add("Móti breytt");
            updater.setAsPrinted(false);
          }

	}
}


}// class TournamentDeleter


