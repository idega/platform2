package com.idega.projects.golf.presentation;

import com.idega.jmodule.login.business.AccessControl;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.projects.golf.moduleobject.GolfDialog;
import com.idega.projects.golf.entity.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.util.idegaTimestamp;
import com.idega.projects.golf.business.TournamentController;
import com.idega.jmodule.ModuleEvent;
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


    public void main(ModuleInfo modinfo) throws Exception{
        System.out.println("TournamentCreator().main()");
        add("Kominn inn í TournamentCreator");

        checkIfUpdate(modinfo);
	initializeButtons(modinfo);
        checkAction(modinfo);
    }

public void checkIfUpdate(ModuleInfo modinfo) {
        System.out.println("TournamentCreator().checkIfUpdate()");

        String sIsUpdate = modinfo.getParameter("tournament_control_mode");

        bIsUpdate = false;
        boolean remove = false;

        if (sIsUpdate != null) {
            if (sIsUpdate.equals("edit")) {
              bIsUpdate = true;
              remove = false;
              String tournament_id = modinfo.getParameter("tournament");

              if (tournament_id != null) {
                  sTournamentIdToUpdate = tournament_id;
                  modinfo.setSessionAttribute("i_golf_tournament_update_id",tournament_id);
              }
            }
            else if (sIsUpdate.equals("create")) {
                remove = true;
            }
        }
        else {
            String temp_tournament_id = (String) modinfo.getSessionAttribute("i_golf_tournament_update_id");
            if (temp_tournament_id != null) {
                sTournamentIdToUpdate = temp_tournament_id;
                bIsUpdate = true;
                remove = false;
            }
        }

        if (remove) {
                modinfo.removeSessionAttribute("i_golf_tournament_update_id");
        }
}

public void initializeButtons(ModuleInfo modinfo){
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

public void checkAction(ModuleInfo modinfo) throws Exception{
    String action = modinfo.getParameter("TournamentCreatorAction");
    add("<br>"+action);
    System.err.println("TournamentCreatorAction = "+action);


    String entityPar = modinfo.getParameter("idega_entity");
    if (entityPar != null) {
        String thePar = (String) modinfo.getSessionAttribute("group_tournament");
        if (thePar == null) { thePar = "";}
        if (thePar.equals("Y")){
                createTournament3(modinfo);
        }
        else{
                createTournament5(modinfo);
        }
    }
    else {
        if (action == null) {
            createTournament(modinfo);
        }
        else if (action.equals("b1") ) {
            createTournament(modinfo);
        }
        else if (action.equals("bu2")) {
            createTournament2(modinfo);
        }
        else if (action.equals("bu3")) {
            createTournament4(modinfo);
        }
        else if (action.equals("bu4")) {
            createTournament6(modinfo);
        }
        else if (action.equals("bu5")) {
            createTournament6(modinfo);
        }
        else if (action.equals("bu6")) {
            SaveTournament(modinfo);
        }
        else if (action.equals("stbtt7")) {
            confirmSaveTournament(modinfo);
        }
        else if (action.equals("dbu")) {
            typeInTournamentText(modinfo);
        }
    }

}


public void actionPerformed(ModuleEvent e)throws Exception{

        ModuleInfo modinfo = e.getModuleInfo();
	initializeButtons(modinfo);
        checkIfUpdate(modinfo);
//        add(""+bIsUpdate);

	//try{
		if(e.getSource().equals(Button1)){
			createTournament(modinfo);
		}
		/*else if(e.getSource().equals(modifyTournamentB1)){
			editTournament();
		}*/
		else if(e.getSource().equals(Button2)){
			createTournament2(modinfo);
		}
		/*else if(e.getSource().equals(Dropdown2)){
			editTournament2();
		}*/
		/*else if(e.getSource().equals(editTournament1)){
			editTournament2();
		}*/
		else if(e.getSource().equals(entityPar)){
			if (((String)modinfo.getSessionAttribute("group_tournament")).equals("Y")){
				createTournament3(modinfo);
			}
			else{
				createTournament5(modinfo);
			}
		}
		else if(e.getSource().equals(Button3)){
			createTournament4(modinfo);
		}
		else if(e.getSource().equals(Button4)){
			//if (((String)getSessionAttribute("group_tournament")).equals("Y")){
				createTournament6(modinfo);
			//}
			//else{
			//	createTournament5(modinfo);
			//}
		}
		else if(e.getSource().equals(Button5)){
                        createTournament6(modinfo);

                        //setTournamentDays(modinfo);

		}
		else if(e.getSource().equals(daysButton)){
                    typeInTournamentText(modinfo);
		}
		else if(e.getSource().equals(textFinished)){
                    confirmSaveTournament(modinfo);
		}
		else if(e.getSource().equals(Button6)){
			SaveTournament(modinfo);
		}
		/*else if (e.getSource().equals(RegisterButton1)){
			FindRegistrationMember();
		}
		else if (e.getSource().equals(RegistermodifyTournamentB1)){
			RegisterMember();
		}
		else if (e.getSource().equals(RegisterButton3)){
			SaveRegistration();
		}
		else if (e.getSource().equals(selectedTournament)){
			tournamentInfo();
		}
		else if (e.getSource().equals(startingTimeB1)){
			setupStartingtime();
		}*/
		else{
                        createTournament(modinfo);
		}
	//}
	/*catch(SQLException ex){
		throw new IOException(ex.getMessage());
		//ex.printStackTrace();
		//throw (IOException) ex.fillInStackTrace();
	}*/
}


public void createTournament(ModuleInfo modinfo)throws SQLException{
	GolfDialog dialog1;

        String sSelectedTournamentType = "-1";
        String sSelectedTournamentForm = "-1";
        boolean bSelectedTournamentUseGroups = false;
        boolean bSelectedTournamentIsOpen = false;



        if (bIsUpdate) {
          dialog1 = new GolfDialog("Breyta móti");
          Tournament tour = new Tournament(Integer.parseInt(sTournamentIdToUpdate));
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
           //addIfUpdate(modinfo,dialog);
        dialog1.add(dialog);

	Table table = new Table(2,5);
	dialog.add(table);
	TournamentType type = new TournamentType();
	Dropdown1 = new DropdownMenu(type.getVisibleTournamentTypes());
            Dropdown1.setSelectedElement(sSelectedTournamentType);
	//Dropdown1.setToSubmit();

	TournamentForm form = new TournamentForm();
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

public void createTournament2(ModuleInfo modinfo)throws SQLException{
        Tournament tournament = null;
        if (this.bIsUpdate) {
            tournament = new Tournament(Integer.parseInt(sTournamentIdToUpdate));
            tournament.setTournamentTypeID(Integer.parseInt(modinfo.getParameter("tournament_type")));
            tournament.setTournamentFormID(Integer.parseInt(modinfo.getParameter("tournament_form")));
        }
        else {
            tournament = new Tournament();

            //TournamentType type = new TournamentType();
            //TournamentType type = new TournamentType(Integer.parseInt(getParameter("tournament_type")));
            //TournamentForm form = new TournamentForm(Integer.parseInt(getParameter("tournament_form")));

            //type.setID(Integer.parseInt(getModuleInfo().getRequest().getParameter("tournament_type")));
            //type.setID(1);

            //add(new Text(type.getName()));
            //tournament.setTournamentType(type);
            tournament.setTournamentTypeID(Integer.parseInt(modinfo.getParameter("tournament_type")));
            tournament.setTournamentFormID(Integer.parseInt(modinfo.getParameter("tournament_form")));

            //default settings
            tournament.setName("Nafn móts");
            tournament.setNumberOfRounds(2);
            tournament.setRegistrationFee(0);
            tournament.setNumberOfDays(1);
        }

            GolfDialog dialog1 = new GolfDialog("Skráðu inn upplýsingar fyrir mótið");
            add(dialog1);
            Form dialog = new Form();
              //addIfUpdate(modinfo,dialog);
            dialog1.add(dialog);

            EntityInsert entityForm = new EntityInsert(tournament,modinfo.getRequestURI()+"?idega_entity=true");
            entityForm.setFieldNotDisplayed("tournament_type_id");
            entityForm.setNotToInsert();
            entityForm.setButtonText("Áfram");
            dialog.add(entityForm);

            if(AccessControl.isClubAdmin(modinfo)){
              Member member = (Member)com.idega.jmodule.login.business.AccessControl.getMember(modinfo);
              int union_id = member.getMainUnionID();
              Union union = new Union(union_id);
              tournament.setUnion(union);
              entityForm.setFieldNotDisplayed("union_id");
              entityForm.setColumnValueRange("field_id",union.getOwningFields());
            }

              modinfo.setSessionAttribute("group_tournament",modinfo.getParameter("group_tournament"));
            if(modinfo.getParameter("group_tournament").equals("Y")){
                    //tournament.setVisible("registration_fee",false);
                    entityForm.setFieldNotDisplayed("registration_fee");
                    tournament.setGroupTournament(true);
            }
            else{
                    tournament.setGroupTournament(false);
            }

            if(modinfo.getParameter("open_tournament").equals("Y")){
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





public void createTournament3(ModuleInfo modinfo)throws SQLException{
	TournamentGroup group = new TournamentGroup();

        GolfDialog dialog1 = new GolfDialog("Veldu flokka sem eiga að verða með");
	add(dialog1);
        Form dialog = new Form();
        dialog1.add(dialog);
        //addIfUpdate(modinfo,dialog);
        SelectionBox  box = new SelectionBox("tournament_group");
        if(AccessControl.isClubAdmin(modinfo)){
          Member member = (Member)com.idega.jmodule.login.business.AccessControl.getMember(modinfo);
          int union_id = member.getMainUnionID();
          Union union = new Union(union_id);
          box.addMenuElements(union.getTournamentGroupsRecursive());

        }
        else if(AccessControl.isAdmin(modinfo)){
          box.addMenuElements(EntityFinder.findAll(group));
        }

        if (bIsUpdate) {
            Tournament temp_tournament = new Tournament(Integer.parseInt(sTournamentIdToUpdate));
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


public void createTournament4(ModuleInfo modinfo) throws SQLException{

	String[] stringArr = modinfo.getParameterValues("tournament_group");
	if (stringArr == null){
		add(new Text("Þú verdur að velja einhverja flokka"));
		createTournament3(modinfo);
	}
	else{
		TournamentGroup[] groupArr = (TournamentGroup[]) (new TournamentGroup()).constructArray(stringArr);
		modinfo.setSessionAttribute("tournament_group",groupArr);


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

public void createTournament5(ModuleInfo modinfo) throws SQLException{

        TeeColor[] color = (TeeColor[]) new TeeColor().findAll();;
	SelectionBox  box = new SelectionBox(color);

        if (this.bIsUpdate) {
            Tournament tempTour = new Tournament(Integer.parseInt(this.sTournamentIdToUpdate));
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

public void createTournament6(ModuleInfo modinfo) throws Exception{
	//Tournament tournament = (Tournament) getSession().getAttribute("idega_entity");
	//Enumeration enum = getRequest().getParameterNames();

	if (((String)modinfo.getSessionAttribute("group_tournament")).equals("Y")){

			String[] stringArr = modinfo.getParameterValues("group_fee");
			modinfo.setSessionAttribute("group_fee",stringArr);

                        setTournamentDays(modinfo);



	}
	else{
		String[] stringArr = modinfo.getParameterValues("tee_color");
		if (stringArr == null){
			add(new Text("Þú verður ad velja einhverja teiga"));
			createTournament5(modinfo);
		}
		else{

			TeeColor[] teecolorArr = (TeeColor[])(new TeeColor()).constructArray(stringArr);
			modinfo.setSessionAttribute("tee_color",teecolorArr);

                        setTournamentDays(modinfo);

		}
	}
}


public void setTournamentDays(ModuleInfo modinfo)throws Exception{
  Tournament tournament = (Tournament) modinfo.getSessionAttribute("idega_entity");
  int numberOfDays=tournament.getNumberOfDays();
  if(numberOfDays>1){
      GolfDialog dialog1 = new GolfDialog("Skilgreindu mótsdaga");
      add(dialog1);
      Form form = new Form();
      dialog1.add(form);
      idegaTimestamp stamp = new idegaTimestamp(tournament.getStartTime());
      for(int i=0;i<numberOfDays;i++){
          DateInput input = new DateInput("tournament_day");
          if(i!=0){
            stamp.addDays(1);
          }
          input.setYear(stamp.getYear());
          input.setMonth(stamp.getMonth());
          input.setDay(stamp.getDate());
         form.add("Dagur "+i+1+":");
         form.add(input);
         form.addBreak();
      }
      form.add(daysButton);
  }
  else{
      TournamentDay day = new TournamentDay();
      idegaTimestamp stamp = new idegaTimestamp(tournament.getStartTime());

        day.setDate(stamp.getSQLDate());
        //day.setTournament(tournament);
        //day.insert();
        modinfo.setSessionAttribute("idega_tournament_day",day);
      typeInTournamentText(modinfo);
  }
}

public void typeInTournamentText(ModuleInfo modinfo) throws Exception{
    Tournament tournament = (Tournament) modinfo.getSessionAttribute("idega_entity");

    String[] tournamentDays = modinfo.getParameterValues("tournament_day");
    if(tournamentDays!=null){
      for(int i=0;i<tournamentDays.length;i++){
        idegaTimestamp stamp = new idegaTimestamp(tournamentDays[i]);
        TournamentDay day = new TournamentDay();
        day.setDate(stamp.getSQLDate());
        //day.setTournament(tournament);
        //day.insert();
        modinfo.setSessionAttribute("idega_tournament_day"+i,day);
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


public void confirmSaveTournament(ModuleInfo modinfo) throws Exception{
    Tournament tournament = (Tournament) modinfo.getSessionAttribute("idega_entity");
    String extra_text = modinfo.getParameter("extra_text");

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


public void SaveTournament(ModuleInfo modinfo) throws SQLException,IOException{

	Tournament tournament = (Tournament) modinfo.getSessionAttribute("idega_entity");
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

        TournamentDay day = (TournamentDay) modinfo.getSessionAttribute("idega_tournament_day");
        modinfo.removeSessionAttribute("idega_tournament_day");

        if(day==null){
          int i = 0;
          day=(TournamentDay) modinfo.getSessionAttribute("idega_tournament_day"+i);
          while (day!=null){
            modinfo.removeSessionAttribute("idega_tournament_day"+i);
            day.setTournament(tournament);
            day.insert();
            i++;
            day=(TournamentDay) modinfo.getSessionAttribute("idega_tournament_day"+i);
          }
        }
        else{
            day.setTournament(tournament);
            day.insert();
        }





	if (((String)modinfo.getSessionAttribute("group_tournament")).equals("Y")){
		TournamentGroup[] group = (TournamentGroup[]) modinfo.getSessionAttribute("tournament_group");
		String[] group_fee = (String[]) modinfo.getSessionAttribute("group_fee");
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
		TeeColor[] color = (TeeColor[]) modinfo.getSessionAttribute("tee_color");
		for (int i = 0; i< color.length;i++){
			color[i].addTo(tournament);
		}
	}

        TournamentController.removeTournamentTableApplicationAttribute(modinfo);
	GolfDialog dialog = new GolfDialog("Lokaskref");
	add(dialog);
	dialog.add(new Text("Mót vistað!"));

	//sends again to begin
	//getResponse().sendRedirect(getRequest().getRequestURI());
        getWindow().setParentToReload();
        getWindow().close();
}


public void selectTournament(String controlParameter)throws SQLException{

	GolfDialog dialog = new GolfDialog("Veldu mót");
	add(dialog);
	dialog.add(new DropdownMenu((new Tournament()).findAll()));

	if (controlParameter.equals("startingtime")){
		dialog.add(startingTimeB1);
	}
}


/**
 * UNFINISHED
 */
public boolean isInEditMode(ModuleInfo modinfo){
/*
  String controlParameter="tournament_control_mode";
  String mode = modinfo.getParameter(controlParameter);
  if(mode!=null){
    modinfo.setSessionAttribute(controlParameter,mode);
    if(mode.equals("edit"))
      return true;
    return false;
  }
  else{
    mode = (String)modinfo.getSessionAttribute(controlParameter);
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
public Tournament getTournament(ModuleInfo modinfo)throws Exception{
  String tournament_par = modinfo.getParameter("tournament");
  Tournament tournament=null;
  if(tournament_par==null){
    tournament = (Tournament)modinfo.getSessionAttribute("tournament_admin_tournament");
  }
  else{
    tournament = new Tournament(Integer.parseInt(tournament_par));
    modinfo.setSessionAttribute("tournament_admin_tournament",tournament);
  }
  return tournament;
}



}// class TournamentCreator


