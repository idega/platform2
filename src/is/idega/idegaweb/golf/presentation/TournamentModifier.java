package is.idega.idegaweb.golf.presentation;



import is.idega.idegaweb.golf.business.TournamentController;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.moduleobject.GolfDialog;

import com.idega.jmodule.login.business.AccessControl;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import is.idega.idegaweb.golf.business.EntityUpdater;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;



/**

*@author <a href="mailto:gimmi@idega.is">Grímur</a>

*@version 1.0

*/

 public class TournamentModifier extends TournamentAdmin {



    public TournamentModifier(){

        super();

        //System.out.println("TournamentModifier()");

    }





public void main(IWContext iwc) throws Exception{

	//initializeButtons();

        //System.out.println("TournamentModifier.main()");



	String tournament_id=iwc.getParameter("tournament");



	if (tournament_id == null){

                GolfDialog dialog = new GolfDialog("Breyta móti");

                add(dialog);

                Form form = new Form(Class.forName("is.idega.idegaweb.golf.presentation.TournamentCreator"));

//                Form form = new Form("createtournament.jsp");

                form.add(new HiddenInput("tournament_control_mode","edit"));

                dialog.add(new Text("Veldu mót:"));

                dialog.add(form);







                        Tournament tournament = ((is.idega.idegaweb.golf.entity.TournamentHome)com.idega.data.IDOLookup.getHomeLegacy(Tournament.class)).createLegacy();

                        DropdownMenu Dropdown2 = new DropdownMenu();

                        if (AccessControl.isClubAdmin(iwc)) {

                            com.idega.data.genericentity.Member member = com.idega.jmodule.login.business.AccessControl.getMember(iwc);

                            int member_id = member.getID();

                            Member golfMember = ((is.idega.idegaweb.golf.entity.MemberHome)com.idega.data.IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKeyLegacy(member_id);

                            int main_union_id = golfMember.getMainUnionID();

                            Dropdown2 = TournamentController.getDropdownOrderedByUnion(new DropdownMenu("tournament"),iwc);



//                            Dropdown2 = new DropdownMenu(tournament.findAll("Select * from tournament where union_id =3 OR union_id = "+main_union_id+" order by name"));

                        }

                        else if (AccessControl.isAdmin(iwc)) {

                            Dropdown2 = TournamentController.getDropdownOrderedByUnion(new DropdownMenu("tournament"),iwc);

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

	  Tournament tournament = ((is.idega.idegaweb.golf.entity.TournamentHome)com.idega.data.IDOLookup.getHomeLegacy(Tournament.class)).findByPrimaryKeyLegacy(Integer.parseInt(tournament_id));

	  Tournament[] tournaments = (Tournament[])java.lang.reflect.Array.newInstance(tournament.getClass(),1);

	  tournaments[0]=tournament;



          EntityUpdater updater = new EntityUpdater(tournaments);



	  dialog.add( updater );



          if(updater.thisObjectSubmitted(iwc)){

            dialog.add("Móti breytt");

            updater.setAsPrinted(false);

          }



	}

}





}// class TournamentDeleter





