package is.idega.idegaweb.golf.presentation;



import com.idega.jmodule.login.business.AccessControl;

import com.idega.presentation.ui.*;

import is.idega.idegaweb.golf.moduleobject.GolfDialog;

import is.idega.idegaweb.golf.entity.*;

import com.idega.presentation.*;

import com.idega.presentation.text.*;

import com.idega.util.IWTimestamp;

import is.idega.idegaweb.golf.business.EntityInsert;
import is.idega.idegaweb.golf.business.TournamentController;

//import com.idega.jmodule.ModuleEvent;

import java.sql.SQLException;

import java.io.IOException;

import com.idega.data.EntityFinder;



/**

*@author <a href="mailto:gimmi@idega.is">Grímur</a>

*@version 1.0

*/

 public class TournamentCreator extends TournamentAdmin {



      boolean bIsUpdate;

      String sTournamentIdToUpdate;



      SubmitButton Button1;

      SubmitButton Button2;

      SubmitButton modifyTournamentB1;

      Form modifyTournamentF1;

      SubmitButton modifyTournamentB2;

      DropdownMenu Dropdown1;

      DropdownMenu Dropdown2;

      SubmitButton Button3;

      SubmitButton Button4;

      SubmitButton Button5;

      SubmitButton Button6;

      SubmitButton daysButton;



      SubmitButton editTournament1;

      SubmitButton startingTimeB1;

      SubmitButton startingTimeB2;

      SubmitButton startingTimeB3;

      SubmitButton startingTimeB4;

      SubmitButton startingTimeB5;

      SubmitButton startingTimeB6;



      SubmitButton RegisterButton1;

      SubmitButton RegistermodifyTournamentB1;

      SubmitButton RegisterButton3;



      SubmitButton textFinished;



      Parameter entityPar;

      Parameter selectedTournament;



    public TournamentCreator(){

        super();

        System.out.println("TournamentCreator()");

    }





    public void main(IWContext iwc) throws Exception{

        System.out.println("TournamentCreator().main()");

        add("Kominn inn í TournamentCreator");



        checkIfUpdate(iwc);

	initializeButtons(iwc);

        checkAction(iwc);

    }



public void checkIfUpdate(IWContext iwc) {

        System.out.println("TournamentCreator().checkIfUpdate()");



        String sIsUpdate = iwc.getParameter("tournament_control_mode");



        bIsUpdate = false;

        boolean remove = false;



        if (sIsUpdate != null) {

            if (sIsUpdate.equals("edit")) {

              bIsUpdate = true;

              remove = false;

              String tournament_id = iwc.getParameter("tournament");



              if (tournament_id != null) {

                  sTournamentIdToUpdate = tournament_id;

                  iwc.setSessionAttribute("i_golf_tournament_update_id",tournament_id);

              }

            }

            else if (sIsUpdate.equals("create")) {

                remove = true;

            }

        }

        else {

            String temp_tournament_id = (String) iwc.getSessionAttribute("i_golf_tournament_update_id");

            if (temp_tournament_id != null) {

                sTournamentIdToUpdate = temp_tournament_id;

                bIsUpdate = true;

                remove = false;

            }

        }



        if (remove) {

                iwc.removeSessionAttribute("i_golf_tournament_update_id");

        }

}



public void initializeButtons(IWContext iwc){

        System.out.println("TournamentCreator().initializeButtons()");



        if (Button1 == null){

            System.out.println("TournamentCreator().initializeButtons().Button1==Null");



                /*

                Button1 = new SubmitButton(new Image("/pics/tournament/stofna.gif"),"b1");

                modifyTournamentF1 = new Form("modifytournament.jsp");

                modifyTournamentB1 = new SubmitButton(new Image("/pics/tournament/breyta.gif"),"mt1");

                modifyTournamentF1.add(modifyTournamentB1);

                RegisterButton1 = new SubmitButton(new Image("/pics/tournament/skra.gif"),"rb1");

                startingTimeB1 = new SubmitButton(new Image("/pics/tournament/stilla.gif"),"stb1");



                daysButton = new SubmitButton("dbu"

                ,"Áfram");

                Button2 = new SubmitButton("bu2","Áfram");

                Button3 = new SubmitButton("bu3","Áfram");

                Button4 = new SubmitButton("bu4","Áfram");

                Button5 = new SubmitButton("bu5","Áfram");

                Button6 = new SubmitButton("bu6","Vista mót");



                modifyTournamentB2= new SubmitButton("mt2","Breyta móti");



               RegistermodifyTournamentB1 = new SubmitButton("rt1","Finna");

               //RegisterButton3 = new SubmitButton("rb3","Skrá og uppfæra grunnforgjöf");

               RegisterButton3 = new SubmitButton("rb3","Skrá kylfinga");



               startingTimeB2 = new SubmitButton("stb2","Vista rástíma");



               entityPar = new Parameter("idega_entity","");

               selectedTournament = new Parameter("action","selectedtournament");



               startingTimeB3 = new SubmitButton("stb3","");

               startingTimeB4 = new SubmitButton("stb4","");

               startingTimeB5 = new SubmitButton("stb5","");



               textFinished = new SubmitButton("stbtt7","Áfram");

               */



                Button1 = new SubmitButton(new Image("/pics/tournament/stofna.gif"),"TournamentCreatorAction","b1");

                modifyTournamentF1 = new Form("modifytournament.jsp");

                modifyTournamentB1 = new SubmitButton(new Image("/pics/tournament/breyta.gif"),"TournamentCreatorAction","mt1");

                modifyTournamentF1.add(modifyTournamentB1);

                RegisterButton1 = new SubmitButton(new Image("/pics/tournament/skra.gif"),"TournamentCreatorAction","rb1");

                startingTimeB1 = new SubmitButton(new Image("/pics/tournament/stilla.gif"),"TournamentCreatorAction","stb1");



                daysButton = new SubmitButton("Áfram","TournamentCreatorAction","dbu");

                Button2 = new SubmitButton("Áfram","TournamentCreatorAction","bu2");

                Button3 = new SubmitButton("Áfram","TournamentCreatorAction","bu3");

                Button4 = new SubmitButton("Áfram","TournamentCreatorAction","bu4");

                Button5 = new SubmitButton("Áfram","TournamentCreatorAction","bu5");

                Button6 = new SubmitButton("Vista mót","TournamentCreatorAction","bu6");



                modifyTournamentB2= new SubmitButton("Breyta móti","TournamentCreatorAction","mt2");



               RegistermodifyTournamentB1 = new SubmitButton("Finna","TournamentCreatorAction","rt1");

               //RegisterButton3 = new SubmitButton("rb3","Skrá og uppfæra grunnforgjöf");

               RegisterButton3 = new SubmitButton("Skrá kylfinga","TournamentCreatorAction","rb3");



               startingTimeB2 = new SubmitButton("Vista rástíma","TournamentCreatorAction","stb2");



               entityPar = new Parameter("idega_entity","");

               selectedTournament = new Parameter("action","selectedtournament");



               startingTimeB3 = new SubmitButton("","TournamentCreatorAction","stb3");

               startingTimeB4 = new SubmitButton("","TournamentCreatorAction","stb4");

               startingTimeB5 = new SubmitButton("","TournamentCreatorAction","stb5");



               textFinished = new SubmitButton("Áfram","TournamentCreatorAction","stbtt7");



        }

}



public void checkAction(IWContext iwc) throws Exception{

    String action = iwc.getParameter("TournamentCreatorAction");

    add("<br>"+action);

    System.err.println("TournamentCreatorAction = "+action);





    String entityPar = iwc.getParameter("idega_entity");

    if (entityPar != null) {

        String thePar = (String) iwc.getSessionAttribute("group_tournament");

        if (thePar == null) { thePar = "";}

        if (thePar.equals("Y")){

                createTournament3(iwc);

        }

        else{

                createTournament5(iwc);

        }

    }

    else {

        if (action == null) {

            createTournament(iwc);

        }

        else if (action.equals("b1") ) {

            createTournament(iwc);

        }

        else if (action.equals("bu2")) {

            createTournament2(iwc);

        }

        else if (action.equals("bu3")) {

            createTournament4(iwc);

        }

        else if (action.equals("bu4")) {

            createTournament6(iwc);

        }

        else if (action.equals("bu5")) {

            createTournament6(iwc);

        }

        else if (action.equals("bu6")) {

            SaveTournament(iwc);

        }

        else if (action.equals("stbtt7")) {

            confirmSaveTournament(iwc);

        }

        else if (action.equals("dbu")) {

            typeInTournamentText(iwc);

        }

    }



}



/**

 * @todo: Check if the actionPerformed function is actually used

 */



/*

public void actionPerformed(ModuleEvent e)throws Exception{



        IWContext iwc = e.getIWContext();

	initializeButtons(iwc);

        checkIfUpdate(iwc);

//        add(""+bIsUpdate);



	//try{

		if(e.getSource().equals(Button1)){

			createTournament(iwc);

		}

		//else if(e.getSource().equals(modifyTournamentB1)){

		//	editTournament();

		//}

		else if(e.getSource().equals(Button2)){

			createTournament2(iwc);

		}

		//else if(e.getSource().equals(Dropdown2)){

		//	editTournament2();

		//}

		//else if(e.getSource().equals(editTournament1)){

		//	editTournament2();

		//}

		else if(e.getSource().equals(entityPar)){

			if (((String)iwc.getSessionAttribute("group_tournament")).equals("Y")){

				createTournament3(iwc);

			}

			else{

				createTournament5(iwc);

			}

		}

		else if(e.getSource().equals(Button3)){

			createTournament4(iwc);

		}

		else if(e.getSource().equals(Button4)){

			//if (((String)getSessionAttribute("group_tournament")).equals("Y")){

				createTournament6(iwc);

			//}

			//else{

			//	createTournament5(iwc);

			//}

		}

		else if(e.getSource().equals(Button5)){

                        createTournament6(iwc);



                        //setTournamentDays(iwc);



		}

		else if(e.getSource().equals(daysButton)){

                    typeInTournamentText(iwc);

		}

		else if(e.getSource().equals(textFinished)){

                    confirmSaveTournament(iwc);

		}

		else if(e.getSource().equals(Button6)){

			SaveTournament(iwc);

		}

		//else if (e.getSource().equals(RegisterButton1)){

		//	FindRegistrationMember();

		//}

		//else if (e.getSource().equals(RegistermodifyTournamentB1)){

		//	RegisterMember();

		//}

		//else if (e.getSource().equals(RegisterButton3)){

		//	SaveRegistration();

		//}

		//else if (e.getSource().equals(selectedTournament)){

		//	tournamentInfo();

		//}

		//else if (e.getSource().equals(startingTimeB1)){

		//	setupStartingtime();

		}

		//else{

                //        createTournament(iwc);

		//}

	//}

	//catch(SQLException ex){

	//	throw new IOException(ex.getMessage());

	//	//ex.printStackTrace();

	//	//throw (IOException) ex.fillInStackTrace();

	//}

}

*/



public void createTournament(IWContext iwc)throws SQLException{

	GolfDialog dialog1;



        String sSelectedTournamentType = "-1";

        String sSelectedTournamentForm = "-1";

        boolean bSelectedTournamentUseGroups = false;

        boolean bSelectedTournamentIsOpen = false;







        if (bIsUpdate) {

          dialog1 = new GolfDialog("Breyta móti");

          Tournament tour = ((is.idega.idegaweb.golf.entity.TournamentHome)com.idega.data.IDOLookup.getHomeLegacy(Tournament.class)).findByPrimaryKeyLegacy(Integer.parseInt(sTournamentIdToUpdate));

          dialog1.addMessage(tour.getName());



          sSelectedTournamentForm = Integer.toString(tour.getTournamentFormId());

          sSelectedTournamentType = Integer.toString(tour.getTournamentTypeId());

          bSelectedTournamentUseGroups = tour.getIfGroupTournament();

          bSelectedTournamentIsOpen = tour.getIfOpenTournament();

        }

        else {



          dialog1 = new GolfDialog("Búa til mót");



        }

	add(dialog1);

        Form dialog = new Form();

           //addIfUpdate(iwc,dialog);

        dialog1.add(dialog);



	Table table = new Table(2,5);

	dialog.add(table);

	TournamentType type = ((is.idega.idegaweb.golf.entity.TournamentTypeHome)com.idega.data.IDOLookup.getHomeLegacy(TournamentType.class)).createLegacy();

	Dropdown1 = new DropdownMenu(type.getVisibleTournamentTypes());

            Dropdown1.setSelectedElement(sSelectedTournamentType);

	//Dropdown1.setToSubmit();



	TournamentForm form = ((is.idega.idegaweb.golf.entity.TournamentFormHome)com.idega.data.IDOLookup.getHomeLegacy(TournamentForm.class)).createLegacy();

	DropdownMenu Dropdown2 = new DropdownMenu(form.findAll());

            Dropdown2.setSelectedElement(sSelectedTournamentForm);





        BooleanInput groupTournament = new BooleanInput("group_tournament");

            groupTournament.setSelected(bSelectedTournamentUseGroups);



        BooleanInput openTournament = new BooleanInput("open_tournament");

            openTournament.setSelected(bSelectedTournamentIsOpen);







	table.add(new Text("Keppnisform"),1,1);

	table.add(Dropdown1,2,1);



	table.add(new Text("Tegund"),1,2);

	table.add(Dropdown2,2,2);



	table.add(new Text("Flokkar"),1,3);

	table.add(groupTournament,2,3);



	table.add(new Text("Opið"),1,4);

	table.add(openTournament,2,4);



	table.setAlignment(2,5,"right");

	table.add(Button2,2,5);

}



public void createTournament2(IWContext iwc)throws SQLException{

        Tournament tournament = null;

        if (this.bIsUpdate) {

            tournament = ((is.idega.idegaweb.golf.entity.TournamentHome)com.idega.data.IDOLookup.getHomeLegacy(Tournament.class)).findByPrimaryKeyLegacy(Integer.parseInt(sTournamentIdToUpdate));

            tournament.setTournamentTypeID(Integer.parseInt(iwc.getParameter("tournament_type")));

            tournament.setTournamentFormID(Integer.parseInt(iwc.getParameter("tournament_form")));

        }

        else {

            tournament = ((is.idega.idegaweb.golf.entity.TournamentHome)com.idega.data.IDOLookup.getHomeLegacy(Tournament.class)).createLegacy();



            //TournamentType type = ((is.idega.idegaweb.golf.entity.TournamentTypeHome)com.idega.data.IDOLookup.getHomeLegacy(TournamentType.class)).createLegacy();

            //TournamentType type = ((is.idega.idegaweb.golf.entity.TournamentTypeHome)com.idega.data.IDOLookup.getHomeLegacy(TournamentType.class)).findByPrimaryKeyLegacy(Integer.parseInt(getParameter("tournament_type")));

            //TournamentForm form = ((is.idega.idegaweb.golf.entity.TournamentFormHome)com.idega.data.IDOLookup.getHomeLegacy(TournamentForm.class)).findByPrimaryKeyLegacy(Integer.parseInt(getParameter("tournament_form")));



            //type.setID(Integer.parseInt(getIWContext().getRequest().getParameter("tournament_type")));

            //type.setID(1);



            //add(new Text(type.getName()));

            //tournament.setTournamentType(type);

            tournament.setTournamentTypeID(Integer.parseInt(iwc.getParameter("tournament_type")));

            tournament.setTournamentFormID(Integer.parseInt(iwc.getParameter("tournament_form")));



            //default settings

            tournament.setName("Nafn móts");

            tournament.setNumberOfRounds(2);

            tournament.setRegistrationFee(0);

            tournament.setNumberOfDays(1);

        }



            GolfDialog dialog1 = new GolfDialog("Skráðu inn upplýsingar fyrir mótið");

            add(dialog1);

            Form dialog = new Form();

              //addIfUpdate(iwc,dialog);

            dialog1.add(dialog);



            EntityInsert entityForm = new EntityInsert(tournament,iwc.getRequestURI()+"?idega_entity=true");

            entityForm.setFieldNotDisplayed("tournament_type_id");

            entityForm.setNotToInsert();

            entityForm.setButtonText("Áfram");

            dialog.add(entityForm);



            if(AccessControl.isClubAdmin(iwc)){

              Member member = (Member)com.idega.jmodule.login.business.AccessControl.getMember(iwc);

              int union_id = member.getMainUnionID();

              Union union = ((is.idega.idegaweb.golf.entity.UnionHome)com.idega.data.IDOLookup.getHomeLegacy(Union.class)).findByPrimaryKeyLegacy(union_id);

              tournament.setUnion(union);

              entityForm.setFieldNotDisplayed("union_id");

              entityForm.setColumnValueRange("field_id",union.getOwningFields());

            }



              iwc.setSessionAttribute("group_tournament",iwc.getParameter("group_tournament"));

            if(iwc.getParameter("group_tournament").equals("Y")){

                    //tournament.setVisible("registration_fee",false);

                    entityForm.setFieldNotDisplayed("registration_fee");

                    tournament.setGroupTournament(true);

            }

            else{

                    tournament.setGroupTournament(false);

            }



            if(iwc.getParameter("open_tournament").equals("Y")){

                    //tournament.setVisible("registration_fee",false);

                    entityForm.setFieldNotDisplayed("registration_fee");

                    tournament.setOpenTournament(true);

            }

            else{

                    tournament.setOpenTournament(false);

            }



            //tournament.setVisible("open_tournament",false);

            //tournament.setVisible("group_tournament",false);

            entityForm.setFieldNotDisplayed("tournament_form_id");

            entityForm.setFieldNotDisplayed("open_tournament");

            entityForm.setFieldNotDisplayed("group_tournament");



            /*tournament.setVisible("open_tournament",true);

            tournament.setVisible("group_tournament",true);

            tournament.setVisible("registration_fee",true);

            tournament.setVisible("registration_fee",true);*/

}











public void createTournament3(IWContext iwc)throws SQLException{

	TournamentGroup group = ((is.idega.idegaweb.golf.entity.TournamentGroupHome)com.idega.data.IDOLookup.getHomeLegacy(TournamentGroup.class)).createLegacy();



        GolfDialog dialog1 = new GolfDialog("Veldu flokka sem eiga að verða með");

	add(dialog1);

        Form dialog = new Form();

        dialog1.add(dialog);

        //addIfUpdate(iwc,dialog);

        SelectionBox  box = new SelectionBox("tournament_group");

        if(AccessControl.isClubAdmin(iwc)){

          Member member = (Member)com.idega.jmodule.login.business.AccessControl.getMember(iwc);

          int union_id = member.getMainUnionID();

          Union union = ((is.idega.idegaweb.golf.entity.UnionHome)com.idega.data.IDOLookup.getHomeLegacy(Union.class)).findByPrimaryKeyLegacy(union_id);

          box.addMenuElements(union.getTournamentGroupsRecursive());



        }

        else if(AccessControl.isAdmin(iwc)){

          box.addMenuElements(EntityFinder.findAll(group));

        }



        if (bIsUpdate) {

            Tournament temp_tournament = ((is.idega.idegaweb.golf.entity.TournamentHome)com.idega.data.IDOLookup.getHomeLegacy(Tournament.class)).findByPrimaryKeyLegacy(Integer.parseInt(sTournamentIdToUpdate));

            TournamentGroup[] tempTourGroup = temp_tournament.getTournamentGroups();

            for (int i = 0; i < tempTourGroup.length; i++) {

                box.setSelectedElement(Integer.toString(tempTourGroup[i].getID()));

            }

        }



        box.setHeight(10);

	dialog.add(box);

	//Button3= new SubmitButton("bu3","Áfram");

	dialog.add(Button3);

}





public void createTournament4(IWContext iwc) throws SQLException{



	String[] stringArr = iwc.getParameterValues("tournament_group");

	if (stringArr == null){

		add(new Text("Þú verdur að velja einhverja flokka"));

		createTournament3(iwc);

	}

	else{

		TournamentGroup[] groupArr = (TournamentGroup[]) (((is.idega.idegaweb.golf.entity.TournamentGroupHome)com.idega.data.IDOLookup.getHomeLegacy(TournamentGroup.class)).createLegacy()).constructArray(stringArr);

		iwc.setSessionAttribute("tournament_group",groupArr);





        GolfDialog dialog1 = new GolfDialog("Skilgreindu keppnisgjöld fyrir hvern flokk fyrir sig");

	add(dialog1);

        Form form = new Form();

        dialog1.add(form);





		for(int i = 0; i < groupArr.length;i++){

			form.add(new Text(groupArr[i].getName()));

                        IntegerInput input = null;

                        if (bIsUpdate) {

                            input = new IntegerInput("group_fee",groupArr[i].getRegistrationFee(Integer.parseInt(sTournamentIdToUpdate)));

                        }

                        else {

			    input =new IntegerInput("group_fee",0);

                        }

			input.setLength(5);

			form.add(input);

			form.addBreak();

		}







		//Button4= new SubmitButton("bu4","Áfram");

		form.add(Button4);

	}

}



public void createTournament5(IWContext iwc) throws SQLException{



        TeeColor[] color = (TeeColor[]) ((is.idega.idegaweb.golf.entity.TeeColorHome)com.idega.data.IDOLookup.getHomeLegacy(TeeColor.class)).createLegacy().findAll();;

	SelectionBox  box = new SelectionBox(color);



        if (this.bIsUpdate) {

            Tournament tempTour = ((is.idega.idegaweb.golf.entity.TournamentHome)com.idega.data.IDOLookup.getHomeLegacy(Tournament.class)).findByPrimaryKeyLegacy(Integer.parseInt(this.sTournamentIdToUpdate));

            TeeColor[] selected_color =  tempTour.getTeeColors();

            for (int i = 0; i < selected_color.length; i++) {

                box.setSelectedElement(Integer.toString(selected_color[i].getID())) ;

            }



        }



        GolfDialog dialog1 = new GolfDialog("Veldu teiga sem eiga að fylgja móti");

	add(dialog1);

        Form dialog = new Form();

        dialog1.add(dialog);





        box.setHeight(8);

	dialog.add(box);

	//Button5= new SubmitButton("bu5","Áfram");

	dialog.add(Button5);



}



public void createTournament6(IWContext iwc) throws Exception{

	//Tournament tournament = (Tournament) getSession().getAttribute("idega_entity");

	//Enumeration enum = getRequest().getParameterNames();



	if (((String)iwc.getSessionAttribute("group_tournament")).equals("Y")){



			String[] stringArr = iwc.getParameterValues("group_fee");

			iwc.setSessionAttribute("group_fee",stringArr);



                        setTournamentDays(iwc);







	}

	else{

		String[] stringArr = iwc.getParameterValues("tee_color");

		if (stringArr == null){

			add(new Text("Þú verður ad velja einhverja teiga"));

			createTournament5(iwc);

		}

		else{



			TeeColor[] teecolorArr = (TeeColor[])(((is.idega.idegaweb.golf.entity.TeeColorHome)com.idega.data.IDOLookup.getHomeLegacy(TeeColor.class)).createLegacy()).constructArray(stringArr);

			iwc.setSessionAttribute("tee_color",teecolorArr);



                        setTournamentDays(iwc);



		}

	}

}





public void setTournamentDays(IWContext iwc)throws Exception{

  Tournament tournament = (Tournament) iwc.getSessionAttribute("idega_entity");

  int numberOfDays=tournament.getNumberOfDays();

  if(numberOfDays>1){

      GolfDialog dialog1 = new GolfDialog("Skilgreindu mótsdaga");

      add(dialog1);

      Form form = new Form();

      dialog1.add(form);

      IWTimestamp stamp = new IWTimestamp(tournament.getStartTime());

      for(int i=0;i<numberOfDays;i++){

          DateInput input = new DateInput("tournament_day");

          if(i!=0){

            stamp.addDays(1);

          }

          input.setYear(stamp.getYear());

          input.setMonth(stamp.getMonth());

          input.setDay(stamp.getDay());

         form.add("Dagur "+i+1+":");

         form.add(input);

         form.addBreak();

      }

      form.add(daysButton);

  }

  else{

      TournamentDay day = ((is.idega.idegaweb.golf.entity.TournamentDayHome)com.idega.data.IDOLookup.getHomeLegacy(TournamentDay.class)).createLegacy();

      IWTimestamp stamp = new IWTimestamp(tournament.getStartTime());



        day.setDate(stamp.getSQLDate());

        //day.setTournament(tournament);

        //day.insert();

        iwc.setSessionAttribute("idega_tournament_day",day);

      typeInTournamentText(iwc);

  }

}



public void typeInTournamentText(IWContext iwc) throws Exception{

    Tournament tournament = (Tournament) iwc.getSessionAttribute("idega_entity");



    String[] tournamentDays = iwc.getParameterValues("tournament_day");

    if(tournamentDays!=null){

      for(int i=0;i<tournamentDays.length;i++){

        IWTimestamp stamp = new IWTimestamp(tournamentDays[i]);

        TournamentDay day = ((is.idega.idegaweb.golf.entity.TournamentDayHome)com.idega.data.IDOLookup.getHomeLegacy(TournamentDay.class)).createLegacy();

        day.setDate(stamp.getSQLDate());

        //day.setTournament(tournament);

        //day.insert();

        iwc.setSessionAttribute("idega_tournament_day"+i,day);

      }

    }



    GolfDialog dialog1 = new GolfDialog("Skráðu inn texta sem nánari umfjöllun um mótið");

    add(dialog1);

    Form form = new Form();

    TextArea area = new TextArea("extra_text");

    if (bIsUpdate) {

        area.setContent(tournament.getExtraText());

    }

    area.setWidth(45);

    area.setHeight(15);

    area.setWrap(true);

    form.add(area);

    dialog1.add(form);

    form.addBreak();

    form.add(textFinished);



}





public void confirmSaveTournament(IWContext iwc) throws Exception{

    Tournament tournament = (Tournament) iwc.getSessionAttribute("idega_entity");

    String extra_text = iwc.getParameter("extra_text");



    if(extra_text!=null){

      if (!extra_text.equalsIgnoreCase("")){

        tournament.setExtraText(extra_text);

      }

    }



    GolfDialog dialog1 = new GolfDialog("Lokaskref");

    add(dialog1);



    Form form = new Form();

    dialog1.add(form);

    form.add(Button6);

}





public void SaveTournament(IWContext iwc) throws SQLException,IOException{



	Tournament tournament = (Tournament) iwc.getSessionAttribute("idega_entity");

        if (bIsUpdate) {

            tournament.update();



            TournamentDay[] tempTournamentDays = tournament.getTournamentDays();

            for (int i = 0; i < tempTournamentDays.length; i++) {

                tempTournamentDays[i].delete();

            }



            TournamentGroup[] tempTournamentGroup = tournament.getTournamentGroups();

            for (int i = 0; i < tempTournamentGroup.length; i++) {

                tempTournamentGroup[i].removeFrom(tournament);

            }





            TeeColor[] tempTeeColor = tournament.getTeeColors();

            for (int i = 0; i < tempTeeColor.length; i++) {

                tempTeeColor[i].removeFrom(tournament);

            }



        }

        else {

    	    tournament.insert();

        }



        TournamentDay[] tournamentDays = tournament.getTournamentDays();



        TournamentDay day = (TournamentDay) iwc.getSessionAttribute("idega_tournament_day");

        iwc.removeSessionAttribute("idega_tournament_day");



        if(day==null){

          int i = 0;

          day=(TournamentDay) iwc.getSessionAttribute("idega_tournament_day"+i);

          while (day!=null){

            iwc.removeSessionAttribute("idega_tournament_day"+i);

            day.setTournament(tournament);

            day.insert();

            i++;

            day=(TournamentDay) iwc.getSessionAttribute("idega_tournament_day"+i);

          }

        }

        else{

            day.setTournament(tournament);

            day.insert();

        }











	if (((String)iwc.getSessionAttribute("group_tournament")).equals("Y")){

		TournamentGroup[] group = (TournamentGroup[]) iwc.getSessionAttribute("tournament_group");

		String[] group_fee = (String[]) iwc.getSessionAttribute("group_fee");

		for (int i = 0 ; i < group.length;i++){

			group[i].addTo(tournament,"registration_fee",group_fee[i]);

			try{

                          group[i].getTeeColor().addTo(tournament);

                        }

                        catch(Exception ex){

                          //NOCATCH

                        }

		}

	}

	else{

		TeeColor[] color = (TeeColor[]) iwc.getSessionAttribute("tee_color");

		for (int i = 0; i< color.length;i++){

			color[i].addTo(tournament);

		}

	}



        TournamentController.removeTournamentTableApplicationAttribute(iwc);

	GolfDialog dialog = new GolfDialog("Lokaskref");

	add(dialog);

	dialog.add(new Text("Mót vistað!"));



	//sends again to begin

	//getResponse().sendRedirect(getRequest().getRequestURI());

        this.setParentToReload();

        this.close();

}





public void selectTournament(String controlParameter)throws SQLException{



	GolfDialog dialog = new GolfDialog("Veldu mót");

	add(dialog);

	dialog.add(new DropdownMenu((((is.idega.idegaweb.golf.entity.TournamentHome)com.idega.data.IDOLookup.getHomeLegacy(Tournament.class)).createLegacy()).findAll()));



	if (controlParameter.equals("startingtime")){

		dialog.add(startingTimeB1);

	}

}





/**

 * UNFINISHED

 */

public boolean isInEditMode(IWContext iwc){

/*

  String controlParameter="tournament_control_mode";

  String mode = iwc.getParameter(controlParameter);

  if(mode!=null){

    iwc.setSessionAttribute(controlParameter,mode);

    if(mode.equals("edit"))

      return true;

    return false;

  }

  else{

    mode = (String)iwc.getSessionAttribute(controlParameter);

    if(mode!=null){

      if(mode.equals("edit"))

        return true;

      return false;

    }

    return false;

  }

*/

    return bIsUpdate;

}



/**

 * UNFINISHED???

 */

public Tournament getTournament(IWContext iwc)throws Exception{

  String tournament_par = iwc.getParameter("tournament");

  Tournament tournament=null;

  if(tournament_par==null){

    tournament = (Tournament)iwc.getSessionAttribute("tournament_admin_tournament");

  }

  else{

    tournament = ((is.idega.idegaweb.golf.entity.TournamentHome)com.idega.data.IDOLookup.getHomeLegacy(Tournament.class)).findByPrimaryKeyLegacy(Integer.parseInt(tournament_par));

    iwc.setSessionAttribute("tournament_admin_tournament",tournament);

  }

  return tournament;

}







}// class TournamentCreator





