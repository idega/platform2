package is.idega.idegaweb.golf.presentation;



import com.idega.jmodule.login.business.AccessControl;

import com.idega.presentation.ui.*;

import is.idega.idegaweb.golf.moduleobject.GolfDialog;

import is.idega.idegaweb.golf.entity.*;

import com.idega.presentation.*;

import is.idega.idegaweb.golf.business.TournamentController;



/**

*@author <a href="mailto:gimmi@idega.is">Grímur</a>

*@version 1.0

*/

 public class TournamentDeleter extends TournamentAdmin {



    public TournamentDeleter(){

        super();

        //System.out.println("TournamentDeleter()");

    }





public void main(IWContext iwc){

	//initializeButtons();

        //System.out.println("TournamentDeleter.main()");

        try{

          String tournament_id;

          String action = iwc.getParameter("action");

          Table table = new Table(2,3);

          add(table);

          Member member = (Member) AccessControl.getMember(iwc);

          //if (member == null){

          //  member = ((is.idega.idegaweb.golf.entity.MemberHome)com.idega.data.IDOLookup.getHomeLegacy(Member.class)).createLegacy();

          //}



          tournament_id=iwc.getParameter("tournament_id");

          String OK = iwc.getParameter("OK");



          if (tournament_id != null){



            Tournament tournament = ((is.idega.idegaweb.golf.entity.TournamentHome)com.idega.data.IDOLookup.getHomeLegacy(Tournament.class)).findByPrimaryKeyLegacy(Integer.parseInt(tournament_id));



            if(OK==null){

                boolean permission=false;

                if(AccessControl.isAdmin(iwc)){

                  permission=true;

                  if(AccessControl.isClubAdmin(iwc)){



                    //member = (Member)com.idega.jmodule.login.business.AccessControl.getMember(iwc);

                    int union_id = member.getMainUnionID();

                    int tourn_union_id=tournament.getUnionId();



                    if(union_id==tourn_union_id){

                      permission=true;

                    }

                    else{

                      permission=false;

                    }

                  }

                }

                if(permission){

                  table.add("Ertu viss um að eyða mótinu "+tournament.getName()+"?");

                  table.addBreak();

                  table.add("Athugaðu að ef mótinu er eytt afskrást allir sem hafa skráð sig a mótið");

                  Form form = new Form();

                  table.add(form);

                  SubmitButton button = new SubmitButton("Já");

                  form.add(button);

                  form.add(new Parameter("tournament_id",tournament_id));

                  form.add(new Parameter("OK","OK"));

                }

                else{

                  table.add("Þú hefur ekki réttindi til að eyða mótinu "+tournament.getName()+"?",1,1);

                  table.add(new CloseButton("Loka"),1,2);

                }



            }

            else{

                tournament.delete();

                table.add("Móti "+tournament.getName()+" eytt",1,1);

                table.add(new CloseButton("Loka"),1,2);

                TournamentController.removeTournamentTableApplicationAttribute(iwc);

                this.setParentToReload();



            }

          }

          else {

             add("Verður að velja mót");

          }

        }

        catch(Exception ex){

          add(new ExceptionWrapper(ex,this));

        }

}





}// class TournamentDeleter





