package is.idega.idegaweb.golf.presentation;



import com.idega.jmodule.login.business.AccessControl;

import com.idega.presentation.ui.*;

import com.idega.presentation.IWContext;

import is.idega.idegaweb.golf.moduleobject.GolfDialog;

//    is.idega.idegaweb.golf.entity.*,

//    is.idega.idegaweb.golf.moduleobject.*,

//    is.idega.idegaweb.golf.templates.*"



/**

*@author <a href="mailto:gimmi@idega.is">Grímur</a>

*@version 1.0

*/

 public class TournamentAdmin extends com.idega.presentation.ui.Window {



    public TournamentAdmin(){

      super();

      super.setMenubar(false);

    }



    public void _main(IWContext iwc)throws Exception{

       this.empty();

       super._main(iwc);

    }



    /*

    public void main(IWContext iwc)throws Exception{

        if (AccessControl.isAdmin(iwc)){

                GolfDialog dialog = new GolfDialog("Mótastjóri");

                add(dialog);



            if (AccessControl.isClubAdmin(iwc)){



                Form form1 = new Form("createtournament.jsp");

                SubmitButton Button1 = new SubmitButton(new Image("/pics/tournament/stofna.gif"),"b1");

                HiddenInput Hidden1 = new HiddenInput("tournament_control_mode","create");

                form1.add(Button1);

                dialog.add(form1);



                Form form5 = new Form("tournamentgroups.jsp");

                SubmitButton Button5 = new SubmitButton("b5","Mótshópar");

                form5.add(Button5);

                dialog.add(form5);



                Form form2 = new Form("modifytournament.jsp");

                SubmitButton Button2 = new SubmitButton(new Image("/pics/tournament/breyta.gif"),"b2");

                HiddenInput Hidden2 = new HiddenInput("tournament_control_mode","edit");

                form2.add(Hidden2);

                form2.add(Button2);

                dialog.add(form2);



          }

          else{



                Form form1 = new Form("createtournament.jsp");

                SubmitButton Button1 = new SubmitButton(new Image("/pics/tournament/stofna.gif"),"b1");

                HiddenInput Hidden1 = new HiddenInput("tournament_control_mode","create");

                form1.add(Hidden1);

                form1.add(Button1);

                dialog.add(form1);



                Form form5 = new Form("tournamentgroups.jsp");

                SubmitButton Button5 = new SubmitButton("b5","Mótshópar");

                form5.add(Button5);

                dialog.add(form5);



                Form form2 = new Form("modifytournament.jsp");

                SubmitButton Button2 = new SubmitButton(new Image("/pics/tournament/breyta.gif"),"b2");

                HiddenInput Hidden2 = new HiddenInput("tournament_control_mode","edit");

                form2.add(Hidden2);

                form2.add(Button2);

                dialog.add(form2);



                Form form3 = new Form("registermember.jsp");

                SubmitButton Button3 = new SubmitButton(new Image("/pics/tournament/skra.gif"),"b3");

                form3.add(Button3);

                dialog.add(form3);



                Form form4 = new Form("setupstartingtime.jsp");

                SubmitButton Button4 = new SubmitButton(new Image("/pics/tournament/stilla.gif"),"b4");

                form4.add(Button4);

                dialog.add(form4);





            }



        }

        else {

//            add("Mótastjórinn er niðri sem stendur, kemur upp aftur bráðlega");

            add("Þú hefur ekki réttindi til þess að vera hér");

        }

    }*/







}// class PaymentViewer





