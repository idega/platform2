package com.idega.projects.golf.presentation;

import com.idega.jmodule.login.business.AccessControl;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.projects.golf.moduleobject.GolfDialog;
import com.idega.projects.golf.entity.*;
import com.idega.jmodule.object.*;
import com.idega.projects.golf.business.TournamentController;

/**
*@author <a href="mailto:gimmi@idega.is">Grímur</a>
*@version 1.0
*/
 public class TournamentDeleter extends TournamentAdmin {

    public TournamentDeleter(){
        super();
        //System.out.println("TournamentDeleter()");
    }


public void main(ModuleInfo modinfo){
	//initializeButtons();
        //System.out.println("TournamentDeleter.main()");
        try{
          String tournament_id;
          String action = modinfo.getParameter("action");
          Table table = new Table(2,3);
          add(table);
          Member member = (Member) AccessControl.getMember(modinfo);
          //if (member == null){
          //  member = new Member();
          //}

          tournament_id=modinfo.getParameter("tournament_id");
          String OK = modinfo.getParameter("OK");

          if (tournament_id != null){

            Tournament tournament = new Tournament(Integer.parseInt(tournament_id));

            if(OK==null){
                boolean permission=false;
                if(AccessControl.isAdmin(modinfo)){
                  permission=true;
                  if(AccessControl.isClubAdmin(modinfo)){

                    //member = (Member)com.idega.jmodule.login.business.AccessControl.getMember(modinfo);
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
                TournamentController.removeTournamentTableApplicationAttribute(modinfo);
                getWindow().setParentToReload();

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


