/*
 * Created on 4.3.2004
 */
package is.idega.idegaweb.golf.tournament.presentation;

import is.idega.idegaweb.golf.block.login.business.AccessControl;
import is.idega.idegaweb.golf.entity.Field;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.Scorecard;
import is.idega.idegaweb.golf.entity.StartingtimeFieldConfig;
import is.idega.idegaweb.golf.entity.TeeColor;
import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.entity.TournamentDay;
import is.idega.idegaweb.golf.entity.TournamentForm;
import is.idega.idegaweb.golf.entity.TournamentGroup;
import is.idega.idegaweb.golf.entity.TournamentGroupHome;
import is.idega.idegaweb.golf.entity.TournamentHome;
import is.idega.idegaweb.golf.entity.TournamentRound;
import is.idega.idegaweb.golf.entity.TournamentRoundHome;
import is.idega.idegaweb.golf.entity.TournamentTournamentGroup;
import is.idega.idegaweb.golf.entity.TournamentType;
import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.moduleobject.GolfDialog;
import is.idega.idegaweb.golf.moduleobject.GolfTournamentAdminDialog;
import is.idega.idegaweb.golf.tournament.business.TournamentController;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.FinderException;

import com.idega.data.EntityFinder;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.BooleanInput;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.IntegerInput;
import com.idega.presentation.ui.Parameter;
import com.idega.presentation.ui.SelectionBox;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.TimeInput;
import com.idega.presentation.ui.TimestampInput;
import com.idega.util.IWTimestamp;

/**
 * @author gimmi
 */
public class TournamentCreator extends TournamentBlock {

	boolean bIsUpdate;
	String sTournamentIdToUpdate;
	SubmitButton aframButton = new SubmitButton(new Image("/pics/formtakks/afram.gif","Áfram"));
	BackButton tilbakaButton = new BackButton(new Image("/pics/formtakks/tilbaka.gif","Til baka"));

	SubmitButton startingTimeB1;

	Parameter entityPar;
	Parameter selectedTournament;

	protected boolean tournamentMustBeSet() {
		return false;
	}

	public void main(IWContext modinfo)throws Exception{
		if (isAdmin() || isClubAdmin()) {
			super.setAdminView(GolfTournamentAdminDialog.ADMIN_VIEW_CREATE_TOURNAMENT);
		    try {
		        checkIfUpdate(modinfo);
			//initializeButtons(modinfo);
		
		        getAction(modinfo,super.getResourceBundle());
		    }
		    catch (Exception e) {
		        e.printStackTrace(System.err);
		    }
		}else {
			add(super.getResourceBundle().getLocalizedString("tournament.no_permission","No permission"));		
		}

	}

	public void getAction(IWContext modinfo,IWResourceBundle iwrb) throws Exception {
	        String mode = modinfo.getParameter("tournament_control_mode");
	        String action = modinfo.getParameter("tournament_admin_createtournament_action");


	        if (action == null) {
	            createTournament(modinfo,iwrb);
	        }
	        else if (action.equals("create")) {
	            createTournament(modinfo,iwrb);
	        }
	        else if(action.equals("create_two")) {
	            createTournament2(modinfo,iwrb);
	        }
	        else if (action.equals("tournament_admin_save_tournament")) {
	            try {
	                SaveTournament(modinfo,iwrb);
	            }
	            catch (Exception e) {
	                add("Villa í SaveTournament<br>");
	                add(e.getMessage());
	            }
	        }

	}

	public void checkIfUpdate(IWContext modinfo) {

	        String sIsUpdate = modinfo.getParameter("tournament_control_mode");
	        bIsUpdate = false;
	        boolean remove = false;

	        if (sIsUpdate != null) {
	            if (sIsUpdate.equals("edit")) {
	              bIsUpdate = true;
	        			super.setAdminView(GolfTournamentAdminDialog.ADMIN_VIEW_MODIFY_TOURNAMENT);
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

	public void createTournament(IWContext modinfo,IWResourceBundle iwrb) throws SQLException {
	        String sSelectedTournamentType = "-1";
	        String sSelectedTournamentForm = "-1";
	        boolean bSelectedTournamentUseGroups = false;
	        boolean bSelectedTournamentIsOpen = false;
	        int row = 1;
	        boolean useForm = true;
	        Form dialog = new Form(modinfo.getRequestURI());
	            add(dialog);
	        if (bIsUpdate) {
	          //Tournament tour = ((TournamentHome) IDOLookup.getHomeLegacy(Tournament.class)).findByPrimaryKey(Integer.parseInt(sTournamentIdToUpdate));
	          dialog.maintainParameter("tournament");
	        }
	        else {
	        }




	        if (bIsUpdate) {
	            Tournament[] tournaments = (Tournament[]) ((Tournament) IDOLookup.instanciateEntity(Tournament.class)).findAllByColumn("tournament_id",sTournamentIdToUpdate);
	            if (tournaments.length < 1) {
	                useForm = false;
	            }
	        }


	        if (useForm) {
	            Tournament tournament = null;
	            if (bIsUpdate) {
	            		try {
	                tournament = ((TournamentHome) IDOLookup.getHomeLegacy(Tournament.class)).findByPrimaryKey(Integer.parseInt(sTournamentIdToUpdate));
	            		}
	            		catch (FinderException fe) {
	            			throw new SQLException(fe.getMessage());
	            		}
	            }

	            Table table  = new Table();
	                dialog.add(table);
	                table.setBorder(0);

	            Union union = null;

	            // CREATE INPUT FIELDS
	                TextInput tournamentName = new TextInput("tournament_admin_tournment_name","");

	                    tournamentName.setSize(40);

	                DropdownMenu unions = null;
	                DropdownMenu fields = null;
	                Field field;
	                List fieldList;



	                if (AccessControl.isClubAdmin(modinfo)) {
	                		try {
	                    union = ((Member) AccessControl.getMember(modinfo)).getMainUnion();
	                		}
	                		catch (FinderException fe) {
	                			throw new SQLException(fe.getMessage());
	                		}
	                    unions = new DropdownMenu("union_");
	                    unions.addMenuElement(union.getID(),union.getName());
	                    fields = new DropdownMenu("tournament_admin_field_id");
	                    fieldList = union.getOwningFields();
	                    if (fieldList != null)
	                    for (int j = 0; j < fieldList.size(); j++) {
	                        field = (Field) fieldList.get(j);
	                        fields.addMenuElement(field.getID(),union.getAbbrevation() + "&nbsp;&nbsp;" +field.getName() );
	                    }

	                }
	                else {
	                    Union[] theUnion = (Union[])((Union) IDOLookup.instanciateEntity(Union.class)).findAllOrdered("ABBREVATION");
	                    unions = new DropdownMenu(theUnion);
	                    fields = new DropdownMenu("tournament_admin_field_id");
	                    for (int i = 0; i < theUnion.length; i++) {
	                        fieldList = theUnion[i].getOwningFields();
	                        if (fieldList != null)
	                        for (int j = 0; j < fieldList.size(); j++) {
	                            field = (Field) fieldList.get(j);
	                            fields.addMenuElement(field.getID(),theUnion[i].getAbbrevation() + "&nbsp;&nbsp;" +field.getName() );
	                        }
	                    }

	                }


	                TimestampInput firstRegistartionDate = new TimestampInput("tournament_admin_first_registartion_date");
	firstRegistartionDate.setYearRange(2001, IWTimestamp.RightNow().getYear() + 5);                
	                TimestampInput lastRegistartionDate = new TimestampInput("tournament_admin_last_registartion_date");
	lastRegistartionDate.setYearRange(2001, IWTimestamp.RightNow().getYear() + 5);                
	                DateInput startTime = new DateInput("tournament_admin_start_time");
	startTime.setYearRange(2001, IWTimestamp.RightNow().getYear() + 5);                
	DateInput endTime = new DateInput("tournament_admin_end_time");
	endTime.setYearRange(2001, IWTimestamp.RightNow().getYear() + 5);                

	                TournamentType type = (TournamentType) IDOLookup.instanciateEntity(TournamentType.class);
	                DropdownMenu tournamentTypeDrop = new DropdownMenu(type.getVisibleTournamentTypes());

	                TournamentForm form = (TournamentForm) IDOLookup.instanciateEntity(TournamentForm.class);
	                DropdownMenu tournamentFormDrop = new DropdownMenu(form.findAll());

	                BooleanInput openTournament = new BooleanInput("tournament_admin_open_tournament");

	                BooleanInput onlineRegistration = new BooleanInput("tournament_admin_online_registration");

	                BooleanInput directRegistration = new BooleanInput("tournament_admin_direct_registration");

	                IntegerInput numberOfDays = new IntegerInput("tournament_admin_number_of_days",1);
	                    numberOfDays.setSize(3);
	                IntegerInput numberOfRounds = new IntegerInput("tournament_admin_number_of_rounds",1);
	                    numberOfRounds.setSize(3);
	                IntegerInput numberOfHoles = new IntegerInput("tournament_admin_number_of_holes",18);
	                    numberOfHoles.setSize(3);

	                List tGroup = TournamentController.getUnionTournamentGroups(union);
	                SelectionBox tournamentGroups = new SelectionBox(tGroup);
	                    tournamentGroups.setHeight(15);

	                DropdownMenu numberInGroup = new DropdownMenu("tournament_admin_number_in_group");
	                    numberInGroup.addMenuElement("1","1");
	                    numberInGroup.addMenuElement("2","2");
	                    numberInGroup.addMenuElement("3","3");
	                    numberInGroup.addMenuElement("4","4");

	                DropdownMenu interval = new DropdownMenu("tournament_admin_interval");
	                    interval.addMenuElement("8","8");
	                    interval.addMenuElement("10","10");
	                    interval.addMenuElement("12","12");

	                TextInput maxHandicap = new TextInput("tournament_admin_max_handicap","24");
	                    maxHandicap.setSize(3);
	                TextInput maxFemaleHandicap = new TextInput("tournament_admin_max_female_handicap","36");
	                    maxFemaleHandicap.setSize(3);


	                TextArea extraText = new TextArea("tournament_admin_extra_text","");
	                    extraText.setWidth(66);
	                    extraText.setHeight(10);

	                SubmitButton submitButton = new SubmitButton("çfram");
	                HiddenInput hiddenAction = new HiddenInput("tournament_admin_createtournament_action","create_two");

//	                Window myWindow = new Window("M—tsh—par",400,500,TournamentGroups.class);
	                Link tournamentGroupButton = getLink(iwrb.getLocalizedString("tournament.tournament_groups","Tournament Groups"));
	                tournamentGroupButton.setWindowToOpen(TournamentGroupsWindow.class);
//	                tournamentGroupButton.setWindowToOpen(TournamentGroups.class);


	                if (this.bIsUpdate) {
	                    tournamentName.setContent(tournament.getName());
	                    unions.setSelectedElement(tournament.getUnionId()+"");
	                    fields.setSelectedElement(tournament.getField()+"");
	                    tournamentTypeDrop.setSelectedElement(tournament.getTournamentTypeId()+"");
	                    openTournament.setSelected(tournament.getIfOpenTournament());
	                    onlineRegistration.setSelected(tournament.getIfRegistrationOnline());
	                    directRegistration.setSelected(tournament.isDirectRegistration());
	                    tournamentFormDrop.setSelectedElement(tournament.getTournamentFormId()+"");

	                    TournamentGroup[] tGroups = tournament.getTournamentGroups();
	                    for (int i = 0 ; i < tGroups.length ; i ++ ) {
	                        tournamentGroups.setSelectedElement(tGroups[i].getID()+"");
	                    }

	                    numberOfDays.setContent(tournament.getNumberOfDays()+"");
	                    numberOfHoles.setContent(tournament.getNumberOfHoles()+"");
	                    numberOfRounds.setContent(tournament.getNumberOfRounds()+"");

	                    maxHandicap.setContent(tournament.getMaxHandicap()+"");
	                    maxFemaleHandicap.setContent(tournament.getFemaleMaxHandicap()+"");
	                    if (tournament.getFirstRegistrationDate() != null) {
	                        IWTimestamp firstRegDate = new IWTimestamp(tournament.getFirstRegistrationDate());
	                            firstRegistartionDate.setYear(firstRegDate.getYear());
	                            firstRegistartionDate.setMonth(firstRegDate.getMonth());
	                            firstRegistartionDate.setDay(firstRegDate.getDay());
	                            firstRegistartionDate.setHour(firstRegDate.getHour());
	                            firstRegistartionDate.setMinute(firstRegDate.getMinute());
	                    }
	                    if (tournament.getLastRegistrationDate() != null) {
	                        IWTimestamp lastRegDate = new IWTimestamp(tournament.getLastRegistrationDate());
	                            lastRegistartionDate.setYear(lastRegDate.getYear());
	                            lastRegistartionDate.setMonth(lastRegDate.getMonth());
	                            lastRegistartionDate.setDay(lastRegDate.getDay());
	                            lastRegistartionDate.setHour(lastRegDate.getHour());
	                            lastRegistartionDate.setMinute(lastRegDate.getMinute());
	                    }
	                    if (tournament.getStartTime() != null) {
	                        IWTimestamp iStartTime = new IWTimestamp(tournament.getStartTime());
	                            startTime.setYear(iStartTime.getYear());
	                            startTime.setMonth(iStartTime.getMonth());
	                            startTime.setDay(iStartTime.getDay());
	                    }

	                    try {
	                        StartingtimeFieldConfig[] fieldConf = (StartingtimeFieldConfig[])((StartingtimeFieldConfig) IDOLookup.instanciateEntity(StartingtimeFieldConfig.class)).findAllByColumn("tournament_id",""+tournament.getID() );
	                        if (fieldConf.length > 0) {
	                            IWTimestamp endHour = new IWTimestamp(fieldConf[0].getCloseTime());
	                                endTime.setYear(endHour.getYear());
	                                endTime.setMonth(endHour.getMonth());
	                                endTime.setDay(endHour.getDay());
	                        }
	                        else {
	                            if (tournament.getStartTime() != null) {
	                                IWTimestamp tempStamp = new IWTimestamp(tournament.getStartTime());
	                                  tempStamp.addDays(tournament.getNumberOfDays()-1);
	                                    endTime.setYear(tempStamp.getYear());
	                                    endTime.setMonth(tempStamp.getMonth());
	                                    endTime.setDay(tempStamp.getDay());
	                                endTime.setDate(tempStamp.getSQLDate());
	                            }
	                        }
	                    }
	                    catch (Exception e){
	                        e.printStackTrace(System.err);
	                    }

	                    extraText.setContent(tournament.getExtraText());
	                    numberInGroup.setSelectedElement(tournament.getNumberInGroup()+"");
	                    interval.setSelectedElement(tournament.getInterval()+"");



	                }
	                else {
	                  firstRegistartionDate.setDate(IWTimestamp.RightNow().getSQLDate());
	                  firstRegistartionDate.setHour(8);
	                  firstRegistartionDate.setMinute(0);

	                  lastRegistartionDate.setDate(IWTimestamp.RightNow().getSQLDate());
	                  lastRegistartionDate.setHour(8);
	                  lastRegistartionDate.setMinute(0);
	                  startTime.setDate(IWTimestamp.RightNow().getSQLDate());
	                  //startTime.setHour(8);
	                  //startTime.setMinute(0);
	                  endTime.setDate(IWTimestamp.RightNow().getSQLDate());
	                  //endTime.setHour(20);
	                  //endTime.setMinute(59);

	                  directRegistration.setSelected(true);
	                  numberInGroup.setSelectedElement("4");
	                  interval.setSelectedElement("10");
	                }

	            // DONE CREATING INPUT FIELDS


	            table.add(iwrb.getLocalizedString("tournament.name","Name") ,1,row);
	            table.add(tournamentName,2,row);
	            table.mergeCells(2,row,4,row);
	            table.setAlignment(1,row,"left");
	            table.setAlignment(2,row,"left");
	            table.setAlignment(5,row,"right");

	            if (bIsUpdate && AccessControl.isAdmin(modinfo)) {
	                if(AccessControl.isAdmin(modinfo) || AccessControl.isClubAdmin(modinfo)){
	                    Link deleteLink = getLink(localize("tournament.delete_tournament","Delete Tournament"));
	                      deleteLink.setWindowToOpen(TournamentDeleteWindow.class);
	                      deleteLink.addParameter("tournament_id",sTournamentIdToUpdate);
	                    table.add(deleteLink,5,row);
	                }
	            }


	            ++row;
	            table.add(iwrb.getLocalizedString("tournament.held_by","Held by"),1,row);
	            table.add(unions,2,row);
	            table.setAlignment(1,row,"left");
	            table.setAlignment(2,row,"left");
	            table.mergeCells(2,row,5,row);

	            ++row;
	            table.add(iwrb.getLocalizedString("tournament.field","Field"),1,row);
	            table.add(fields,2,row);
	            table.setAlignment(1,row,"left");
	            table.setAlignment(2,row,"left");
	            table.mergeCells(2,row,5,row);

	            ++row;
	            ++row;
	            table.add(iwrb.getLocalizedString("tournament.arrangement","Arrangement"),1,row);
	            table.add(tournamentTypeDrop,2,row);
	            table.setAlignment(1,row,"left");
	            table.setAlignment(2,row,"left");

	            table.add(iwrb.getLocalizedString("tournament.groups","Groups"),4,row);
	            table.setAlignment(1,row,"left");
	            table.setAlignment(4,row,"left");
	            table.setAlignment(5,row,"right");
	            table.add(tournamentGroupButton,5,row);

	            ++row;
	            table.add(iwrb.getLocalizedString("tournament.type","Type"),1,row);
	            table.add(tournamentFormDrop,2,row);
	            table.setAlignment(1,row,"left");
	            table.setAlignment(2,row,"left");

	            table.setAlignment(4,row,"left");
	            table.mergeCells(4,row,5,row+8);
	            table.setVerticalAlignment(4,row,"top");
	            table.add(tournamentGroups,4,row);

	            ++row;
	            table.add(iwrb.getLocalizedString("tournament.open","Open"),1,row);
	            table.add(openTournament,2,row);
	            table.setAlignment(1,row,"left");
	            table.setAlignment(2,row,"left");


	            ++row;
	            table.add(iwrb.getLocalizedString("tournament.allow_online_registration","Allow online registration"),1,row);
	            table.add(onlineRegistration,2,row);
	            table.setAlignment(1,row,"left");
	            table.setAlignment(2,row,"left");

	            ++row;
	            table.add(iwrb.getLocalizedString("tournament.startingtime_registration","Register on tee time") ,1,row);
	            table.add(directRegistration,2,row);
	            table.setAlignment(1,row,"left");
	            table.setAlignment(2,row,"left");


	            ++row;
	            table.add(iwrb.getLocalizedString("tournament.number_of_days","Number of days") ,1,row);
	            table.add(numberOfDays,2,row);
	            table.setAlignment(1,row,"left");
	            table.setAlignment(2,row,"left");

	            ++row;
	            table.add(iwrb.getLocalizedString("tournament.number_of_rounds","Number of rounds") ,1,row);
	            table.add(numberOfRounds,2,row);
	            table.setAlignment(1,row,"left");
	            table.setAlignment(2,row,"left");

	            ++row;
	            table.add(iwrb.getLocalizedString("tournament.number_of_holes_per_round","Holes per round"),1,row);
	            table.add(numberOfHoles,2,row);
	            table.setAlignment(1,row,"left");
	            table.setAlignment(2,row,"left");

	            ++row;
	            table.add(iwrb.getLocalizedString("tournament.number_in_starting_group","Number in group"),1,row);
	            table.add(numberInGroup,2,row);
	            table.setAlignment(1,row,"left");
	            table.setAlignment(2,row,"left");

	            ++row;
	            table.add(iwrb.getLocalizedString("tournament.interval","Inteval"),1,row);
	            table.add(interval,2,row);
	            table.setAlignment(1,row,"left");
	            table.setAlignment(2,row,"left");

	            ++row;
	            table.add(iwrb.getLocalizedString("tournament.male_maximum_handicap","Male maximum handicap"),1,row);
	            table.add(maxHandicap,2,row);
	            table.setAlignment(1,row,"left");
	            table.setAlignment(2,row,"left");

	            ++row;
	            table.add(iwrb.getLocalizedString("tournament.female_maximum_handicap","Female maximum handicap"),1,row);
	            table.add(maxFemaleHandicap,2,row);
	            table.setAlignment(1,row,"left");
	            table.setAlignment(2,row,"left");


	            ++row;
	            ++row;
	            table.add(iwrb.getLocalizedString("tournament.first_registration_date","First registration date"),1,row);
	            table.add(firstRegistartionDate,2,row);
	            table.mergeCells(2,row,5,row);
	            table.setAlignment(1,row,"left");
	            table.setAlignment(2,row,"left");

	            ++row;
	            table.add(iwrb.getLocalizedString("tournament.last_registration_date","Last registration date"),1,row);
	            table.add(lastRegistartionDate,2,row);
	            table.mergeCells(2,row,5,row);
	            table.setAlignment(1,row,"left");
	            table.setAlignment(2,row,"left");

	            ++row;
	            table.add(iwrb.getLocalizedString("tournament.tournament_begins","Tournament begins"),1,row);
	            table.add(startTime,2,row);
	            table.mergeCells(2,row,5,row);
	            table.setAlignment(1,row,"left");
	            table.setAlignment(2,row,"left");

	            ++row;
	            table.add(iwrb.getLocalizedString("tournament.tournament_ends","Tournament ends") ,1,row);
	            table.add(endTime,2,row);
	            table.mergeCells(2,row,5,row);
	            table.setAlignment(1,row,"left");
	            table.setAlignment(2,row,"left");


	            ++row;
	            ++row;
	            table.add(iwrb.getLocalizedString("tournament.other_information","Other information")+"<br>",1,row);
	            table.add(extraText,1,row);
	            table.setAlignment(1,row,"center");
	            table.mergeCells(1,row,5,row);

	            ++row;
	            table.setAlignment(1,row,"left");
	            table.setAlignment(4,row,"right");
	            table.mergeCells(1,row,2,row);
	            table.mergeCells(4,row,5,row);
	            table.add(this.aframButton,4,row);
	      //        table.add(submitButton,1,row);
	            table.add(hiddenAction,1,row);


	            table.add(TournamentController.getBackLink(modinfo),1,row);

	        }
	        else {
	            add("Ekkert mót valið eða mót ekki til");
	        }



	}

	public void createTournament2(IWContext modinfo, IWResourceBundle iwrb)throws SQLException{
	    String[] tournament_groups = modinfo.getParameterValues("tournament_group");
	    if (tournament_groups == null) {
	        add(iwrb.getLocalizedString("tournament.you_must_pick_groups","You must pick at least one tournament group")+ "<br><br>");
	        add(TournamentController.getBackLink(modinfo));
	    }
	    else {
	        try {

	            Form theForm = new Form();
	                //theForm.maintainParameter("tournament_group");
	            String name = modinfo.getParameter("tournament_admin_tournment_name");
	            String union_id = modinfo.getParameter("union_");
	            String field_id = modinfo.getParameter("tournament_admin_field_id");
	            String tournament_type_id = modinfo.getParameter("tournament_type");
	            String tournament_form_id = modinfo.getParameter("tournament_form");
	            String open_tournament = modinfo.getParameter("tournament_admin_open_tournament");
	                boolean isOpenTournament = true;
	                if (open_tournament.equalsIgnoreCase("N")) isOpenTournament = false;
	            String online_registration = modinfo.getParameter("tournament_admin_online_registration");
	                boolean isOnlineRegistration = false;
	                if (online_registration.equalsIgnoreCase("Y")) isOnlineRegistration = true;
	            String number_of_days = modinfo.getParameter("tournament_admin_number_of_days");
	            String number_of_rounds = modinfo.getParameter("tournament_admin_number_of_rounds");
	          int iNumberOfRounds = Integer.parseInt(number_of_rounds);
	            String number_of_holes = modinfo.getParameter("tournament_admin_number_of_holes");
	            String first_registration_date = modinfo.getParameter("tournament_admin_first_registartion_date");
	                IWTimestamp firstRegistrationDate = new IWTimestamp(first_registration_date);
	            String last_registration_date = modinfo.getParameter("tournament_admin_last_registartion_date");
	                IWTimestamp lastRegistrationDate = new IWTimestamp(last_registration_date);
	            String start_time = modinfo.getParameter("tournament_admin_start_time");
	                IWTimestamp startTime = new IWTimestamp(start_time);
	            String end_time = modinfo.getParameter("tournament_admin_end_time");
	                IWTimestamp endTime = new IWTimestamp(end_time);
	            String extra_text = modinfo.getParameter("tournament_admin_extra_text");
	            String direct_registration = modinfo.getParameter("tournament_admin_direct_registration");
	                boolean isDirectRegistration = true;
	                if (direct_registration.equalsIgnoreCase("N")) isDirectRegistration = false;
	            String number_in_group = modinfo.getParameter("tournament_admin_number_in_group");
	            String interval = modinfo.getParameter("tournament_admin_interval");
	            String maxHandicap = modinfo.getParameter("tournament_admin_max_handicap");
	              if (maxHandicap.equals("")) maxHandicap = "36";
	            String maxFemaleHandicap = modinfo.getParameter("tournament_admin_max_female_handicap");
	              if (maxFemaleHandicap.equals("")) maxFemaleHandicap = "36";

	            if ( maxHandicap.indexOf(",") != -1 ) {
	                maxHandicap = maxHandicap.replace(',','.');
	            }
	            if ( maxFemaleHandicap.indexOf(",") != -1 ) {
	                maxFemaleHandicap = maxFemaleHandicap.replace(',','.');
	            }



	            Tournament tournament = (Tournament) IDOLookup.createLegacy(Tournament.class);
	              if (bIsUpdate) {
	                  tournament = ((TournamentHome) IDOLookup.getHomeLegacy(Tournament.class)).findByPrimaryKey(Integer.parseInt(sTournamentIdToUpdate));
	              }
	              tournament.setName(name);
	              tournament.setUnionId(Integer.parseInt(union_id));
	              tournament.setFieldId(new Integer(Integer.parseInt(field_id)));
	              tournament.setTournamentTypeID(Integer.parseInt(tournament_type_id));
	              tournament.setTournamentFormID(Integer.parseInt(tournament_form_id));
	              tournament.setOpenTournament(isOpenTournament);
	              tournament.setRegistrationOnline(isOnlineRegistration);
	              tournament.setIsDirectRegistration(isDirectRegistration);
	              tournament.setNumberOfDays(Integer.parseInt(number_of_days));
	              tournament.setNumberOfRounds(iNumberOfRounds);
	              tournament.setNumberOfHoles(Integer.parseInt(number_of_holes));
	              tournament.setNumberInGroup(Integer.parseInt(number_in_group));
	              tournament.setInterval(Integer.parseInt(interval));
	              if ( (tournament.getMaxHandicap() != Float.parseFloat(maxHandicap)) || (tournament.getFemaleMaxHandicap() != Float.parseFloat(maxFemaleHandicap)) ) {
	                  theForm.add(new HiddenInput("update_handicap","true"));
	              }

	              tournament.setMaxHandicap(Float.parseFloat(maxHandicap));
	              tournament.setMaxFemaleHandicap(Float.parseFloat(maxFemaleHandicap));
	              tournament.setLastRegistrationDate(lastRegistrationDate.getTimestamp());
	              tournament.setFirstRegistrationDate(firstRegistrationDate.getTimestamp());
	              tournament.setStartTime(startTime.getTimestamp());
	              tournament.setExtraText(extra_text);

	              tournament.setGroupTournament(true);
	              tournament.setCreationDate(IWTimestamp.getTimestampRightNow());
	              tournament.setIsClosed(false);

	              modinfo.setSessionAttribute("tournament_admin_create_tournament",tournament);


	            /*
	              for (int i = 1; i <= tournament.getNumberOfRounds();i++){
	                  TournamentRound round = new TournamentRound();
	                  round.setRoundNumber(i);
	                  round.setTournament(tournament);
	                  round.setRoundDate(com.idega.util.idegaCalendar.getTimestampAfter(tournament.getStartTime(),i+1));
	                  round.setIncreaseHandicap(true);
	                  round.setDecreaseHandicap(true);
	                  round.setRoundEndDate(com.idega.util.idegaCalendar.getTimestampAfter(tournament.getStartTime(),i+1));
	                  round.insert();
	              }
	            */



	            TournamentGroup tGroup;
	            Table groupTable = new Table();
	            	groupTable.setWidth("85%");

	            groupTable.add(iwrb.getLocalizedString("tournament.group","Group") ,1,1);
	            groupTable.add(iwrb.getLocalizedString("tournament.fee","Fee"),2,1);
	            groupTable.add(iwrb.getLocalizedString("tournament.tee","Tee color"),3,1);

	            TeeColor[] tee_colors = (TeeColor[]) ((TeeColor) IDOLookup.instanciateEntity(TeeColor.class)).findAllOrdered("TEE_COLOR_NAME");
	            int tableRow = 1;
	            TextInput feeText;
	            DropdownMenu teeColorDrop;
	            TournamentTournamentGroup[] tourTourGroup;
	            if (tournament_groups != null) {
	                for (int i = 0; i < tournament_groups.length; i++) {
	                    tGroup = ((TournamentGroupHome) IDOLookup.getHomeLegacy(TournamentGroup.class)).findByPrimaryKey(Integer.parseInt(tournament_groups[i]));
	                    ++tableRow;
	                    feeText = new TextInput("tournament_admin_fee_for_group"+tGroup.getID(),"0");
	                        feeText.setSize(6);
	                    teeColorDrop = new DropdownMenu("tournament_admin_tee_color_for_group"+tGroup.getID());
	                        for (int j = 0 ; j < tee_colors.length ; j++) {
	                            teeColorDrop.addMenuElement(tee_colors[j].getID(),tee_colors[j].getName());
	                        }
	                        teeColorDrop.setSelectedElement(tGroup.getTeeColor().getID()+"");

	                        if (bIsUpdate) {
	                            tourTourGroup = (TournamentTournamentGroup[]) ((TournamentTournamentGroup) IDOLookup.instanciateEntity(TournamentTournamentGroup.class)).findAll("Select * from tournament_tournament_group WHERE tournament_id ="+tournament.getID()+" AND tournament_group_id = "+tGroup.getID()+"");
	                            if (tourTourGroup.length > 0) {
	                              if (tourTourGroup[0].getRegistrationFee() != -1)
	                                    feeText.setContent(tourTourGroup[0].getRegistrationFee()+"");
	                                try {
	                                    teeColorDrop.setSelectedElement(tourTourGroup[0].getTeeColorId()+"");
	                                }
	                                catch (Exception e) {}
	                            }
	                        }

	                    groupTable.add(tGroup.getName(),1,tableRow);
	                    groupTable.add(feeText,2,tableRow);
	                    groupTable.add(new HiddenInput("tournament_group",tGroup.getID()+""),1,tableRow);
	                    groupTable.add(teeColorDrop,3,tableRow);

	                }
	            }


//	            ++tableRow;
//	            ++tableRow;

				tableRow = 1;
	            Table roundTable = new Table();
	            	roundTable.setWidth("85%");

				roundTable.add(iwrb.getLocalizedString("tournament.round","Round"),1,tableRow);
				roundTable.add(iwrb.getLocalizedString("tournament.time","Time") ,2,tableRow);
				roundTable.mergeCells(2,tableRow,3,tableRow);
				roundTable.add(iwrb.getLocalizedString("tournament.plays_on_1_and_10_tee","Tee off on 1 and 10 tee"),4,tableRow);


				++tableRow;
	            TimeInput tInput;
	            TimeInput toInput;
	            TournamentRound tRound;
	            TournamentRound[] tRounds = tournament.getTournamentRounds();
	            int tRoundsCounter = 0;
	            IWTimestamp temp = new IWTimestamp(endTime);
	            for ( int i = 0 ; i < tournament.getNumberOfRounds() ; i++) {
	                startTime = new IWTimestamp(start_time);
	                DropdownMenu availableDays = new DropdownMenu("round_date");
	                endTime.addDays(1);
	                while (endTime.isLaterThan(startTime) ) {
	                    availableDays.addMenuElement(startTime.toSQLDateString(), startTime.getISLDate(".",true) );
	                    startTime.addDays(1);
	                }
	                endTime = new IWTimestamp(temp);

					DropdownMenu oneAndTen = new DropdownMenu("oneAndTen_"+i);
						oneAndTen.addMenuElement("1","Nei");
						oneAndTen.addMenuElement("2","Já");
						oneAndTen.setSelectedElement("1");



	                if (bIsUpdate) {
	                    try{
	                        tRound = tRounds[i];
	                        tInput = new TimeInput("tournament_admin_round_from_time_"+tRound.getID() );
	                        toInput = new TimeInput("tournament_admin_round_to_time_"+tRound.getID() );
	                        availableDays.setSelectedElement(new IWTimestamp(tRounds[tRoundsCounter].getRoundDate()).toSQLDateString() );
	                        tInput.setTime(new java.sql.Time(tRounds[tRoundsCounter].getRoundDate().getTime()));
	                        toInput.setTime(new java.sql.Time(tRounds[tRoundsCounter].getRoundEndDate().getTime()));
	                        roundTable.add(new HiddenInput("tournament_round_id",""+tRound.getID()),1,tableRow);
	                        oneAndTen.setName("oneAndTen_"+tRound.getID());

	                        if (tRound.getStartingtees() == 2) {
								oneAndTen.setSelectedElement("2");
							}

	                        ++tRoundsCounter;
	                    }
	                    catch (Exception e) {
	                        tInput = new TimeInput("tournament_admin_round_from_time_"+i);
	                        toInput = new TimeInput("tournament_admin_round_to_time_"+i);

	                        startTime.addDays(i);
	                        tInput.setTime(new java.sql.Time(startTime.getSQLDate().getTime()));
	                        toInput.setTime(new java.sql.Time(startTime.getSQLDate().getTime()));

	                        startTime.addDays(-i);
	                    }
	                }
	                else {
	                    tInput = new TimeInput("tournament_admin_round_from_time_"+i);
	                    toInput = new TimeInput("tournament_admin_round_to_time_"+i);

	                    startTime.addDays(i);
	                    tInput.setTime(new java.sql.Time(startTime.getSQLDate().getTime()));
	                    toInput.setTime(new java.sql.Time(startTime.getSQLDate().getTime()));

	                    startTime.addDays(-i);
	                }

	                roundTable.add(iwrb.getLocalizedString("tournament.round","Round")+" "+(i+1),1,tableRow);
	                roundTable.add(availableDays,2,tableRow);
	                roundTable.add(" frá ",2,tableRow);
	                roundTable.add(tInput,2,tableRow);
	                roundTable.add(" til ",2,tableRow);
	                roundTable.add(toInput,2,tableRow);
	                roundTable.mergeCells(2,tableRow,3,tableRow);
	                roundTable.add(oneAndTen,4,tableRow);

	                ++tableRow;

	            }


				Table buttonTable = new Table();
					buttonTable.setWidth("85%");
	            GenericButton submitButton = getButton(new SubmitButton(localize("tournament.save","Save")));
	            HiddenInput hiddenInput = new HiddenInput("tournament_admin_createtournament_action","tournament_admin_save_tournament");
	            buttonTable.add(TournamentController.getBackLink(modinfo),1,1);
	            buttonTable.add(submitButton,3,1);
	            buttonTable.add(hiddenInput,3,1);
	            buttonTable.setAlignment(3,1,"right");


	            theForm.add(groupTable);
	            theForm.addBreak();
	            theForm.add(roundTable);
	            theForm.addBreak();
				theForm.add(buttonTable);
	          	add(theForm);
	            //dialog.add(dayTable);




	        }
	        catch (Exception e) {
	            add(iwrb.getLocalizedString("tournament.error","Error")+"<br>");
	            add(e.getMessage());
	            e.printStackTrace(System.out);
	        }


	    }


	}

	public void SaveTournament(IWContext modinfo, IWResourceBundle iwrb) throws Exception{

		Tournament tournament = (Tournament) modinfo.getSession().getAttribute("tournament_admin_create_tournament");
	        TournamentRound[] tourRounds = tournament.getTournamentRounds();
	        int manyRounds = tourRounds.length;

	        if (tournament == null) {
	          add(iwrb.getLocalizedString("tournament.no_tournament_selected","No tournament selected"));
	        }
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

	            StartingtimeFieldConfig[] sFieldConfig = (StartingtimeFieldConfig[]) ((StartingtimeFieldConfig) IDOLookup.instanciateEntity(StartingtimeFieldConfig.class)).findAllByColumn("tournament_id",tournament.getID()+"") ;
	            for (int i = 0; i < sFieldConfig.length; i++) {
	                sFieldConfig[i].delete();
	            }

	            if (manyRounds == tournament.getNumberOfRounds() ) {

	            }else if (manyRounds < tournament.getNumberOfRounds() ) {
	                // BÆTA VIÐ TOURNAMENT_ROUNDs
	                String[] round_date = modinfo.getParameterValues("round_date");
	                String theFromTime;
	                String theToTime;
	                String fromString;
	                String toString;
	                IWTimestamp stampFrom;
	                IWTimestamp stampTo;

	                Member[] members = TournamentController.getMembersInTournament(tournament);
	                Member member;

	                for (int i = (manyRounds +1) ; i <= tournament.getNumberOfRounds() ; i++){
	                  theFromTime = modinfo.getParameter("tournament_admin_round_from_time_"+(i-1));
	                  theToTime = modinfo.getParameter("tournament_admin_round_to_time_"+(i-1));

	                  IWTimestamp tempStamp = new IWTimestamp(tournament.getStartTime());
	                  fromString = round_date[i-1] + " "+ theFromTime;
	                  toString = round_date[i-1] + " "+ theToTime;

	                  stampFrom = new IWTimestamp(fromString);
	                  stampTo = new IWTimestamp(toString);

	                    TournamentRound round = (TournamentRound) IDOLookup.createLegacy(TournamentRound.class);
	                    round.setRoundNumber(i);
	                    round.setTournament(tournament);

	                    if (tournament.isDirectRegistration()) {
	                        round.setVisibleStartingtimes(true);
	                    }

	                    round.setRoundDate(stampFrom.getTimestamp());
	                    round.setIncreaseHandicap(true);
	                    round.setDecreaseHandicap(true);
	                    round.setRoundEndDate(stampTo.getTimestamp());
	                    round.insert();
	                }

	            }else if (manyRounds > tournament.getNumberOfRounds() ) {
	                // HENDA ÚT TOURNAMENT_ROUNDs

	                for (int i = (tournament.getNumberOfRounds()+1) ; i<= (manyRounds) ;i++) {
	                    List tournamentRounds = EntityFinder.findAllByColumn((TournamentRound) IDOLookup.instanciateEntity(TournamentRound.class),"tournament_id",""+tournament.getID(),"ROUND_NUMBER",""+i);
	                    if (tournamentRounds != null) {
	                        if (tournamentRounds.size() == 1) {
	                          try {
	                            TournamentRound tourRnd = (TournamentRound) tournamentRounds.get(0);
	                            tourRnd.delete();
	                          }
	                          catch (Exception e) {
	                              e.printStackTrace(System.err);
	                          }
	                        }
	                    }
	                }

	            }
	            
	 	       TournamentRound[] tournRounds = tournament.getTournamentRounds();
	 	       for (int u = 0; u < tournRounds.length; u++) {
	              TournamentController.invalidateStartingTimeCache(modinfo, tournRounds[u].getTournamentID(),  Integer.toString(tournRounds[u].getID()));
	           }


	        }
	        else {
	            try {
	                tournament.insert();
	            }
	            catch (Exception e) {
	                System.err.println("createTournament í insert()");
	                e.printStackTrace(System.err);
	            }
	        }

	      try {
	          String[] tournament_group_ids = modinfo.getParameterValues("tournament_group");
	          TournamentTournamentGroup tTGroup;
	          TournamentGroup tGroup = null;
	          String regFee = "";
	          String teeColorId = "";
	          if (tournament_group_ids != null) {
	              for (int i = 0; i < tournament_group_ids.length; i++) {
	                  teeColorId = modinfo.getParameter("tournament_admin_tee_color_for_group"+tournament_group_ids[i]);
	                  regFee = modinfo.getParameter("tournament_admin_fee_for_group"+tournament_group_ids[i]);
	                  tTGroup = (TournamentTournamentGroup) IDOLookup.createLegacy(TournamentTournamentGroup.class);
	                    tTGroup.setTournamentId(tournament.getID());
	                    tTGroup.setTournamentGroupId(Integer.parseInt(tournament_group_ids[i]));
	                    tTGroup.setRegistrationFee(Integer.parseInt(regFee));
	                    tTGroup.setTeeColorId(Integer.parseInt(teeColorId));
	                    tTGroup.insert();
	              }
	          }
	      }
	      catch (Exception e) {
	          add("villa í grúppum");
	          e.printStackTrace(System.err);
	      }


	      try {

	          String tournament_round_ids[] = modinfo.getParameterValues("tournament_round_id");
	          Scorecard[] scorecards;

	          if (tournament_round_ids != null) {
	              String[] round_date = modinfo.getParameterValues("round_date");
	              TournamentRound tRound;

	              String from_time;
	              String to_time;
	              String numberOfStartingtees;

	              IWTimestamp stampFrom;
	              IWTimestamp stampTo;

	              boolean updateScorecards = false;

	              String fromString = "";
	              String toString = "";
	              for (int i = 0; i < tournament_round_ids.length; i++) {
	                  updateScorecards = false;
	                  from_time = modinfo.getParameter("tournament_admin_round_from_time_"+tournament_round_ids[i]);
	                  to_time = modinfo.getParameter("tournament_admin_round_to_time_"+tournament_round_ids[i]);
	                  numberOfStartingtees = modinfo.getParameter("oneAndTen_"+tournament_round_ids[i]);

	                  fromString = round_date[i] + " "+ from_time;
	                  toString = round_date[i] + " "+ to_time;
	                  tRound = ((TournamentRoundHome) IDOLookup.getHomeLegacy(TournamentRound.class)).findByPrimaryKey(Integer.parseInt(tournament_round_ids[i]));
	                  stampFrom = new IWTimestamp(fromString);
	                  stampTo = new IWTimestamp(toString);

	                  if (tournament.isDirectRegistration()) {
	                      tRound.setVisibleStartingtimes(true);
	                  }

	                  if ( (tRound.getRoundDate().getDay() != stampFrom.getTimestamp().getDay()) || (tRound.getRoundDate().getMonth() != stampFrom.getTimestamp().getMonth()) || (tRound.getRoundDate().getYear() != stampFrom.getTimestamp().getYear()) ) {
	                      updateScorecards = true;
	                  }
	                  tRound.setRoundDate(stampFrom.getTimestamp());
	                  tRound.setRoundEndDate(stampTo.getTimestamp());
	                  tRound.setStartingtees(Integer.parseInt(numberOfStartingtees));

	                  tRound.update();

	                  if (updateScorecards) {
	                      scorecards = (Scorecard[]) ((Scorecard) IDOLookup.instanciateEntity(Scorecard.class)).findAllByColumn("tournament_round_id",tRound.getID());
	                      for (int g = 0; g < scorecards.length; g++) {
	                          scorecards[g].setScorecardDate(tRound.getRoundDate());
	                          scorecards[g].update();
	                          is.idega.idegaweb.golf.UpdateHandicap.update(scorecards[g].getMemberId(),new IWTimestamp(tournament.getStartTime()));
	                          //System.out.println("Uppfaerdi skorkort fyrir "+scorecards[g].getMember().getName());
	                      }
	                  }
	                  

	              }

	          }
	          else {
	              String[] round_date = modinfo.getParameterValues("round_date");
	              TournamentRound tRound;

	              String from_time;
	              String to_time;
	              String numberOfStartingtees;

	              IWTimestamp stampFrom;
	              IWTimestamp stampTo;

	              boolean updateScorecards = false;

	              String fromString = "";
	              String toString = "";
	              TournamentRound[] tRounds = tournament.getTournamentRounds();
	              for (int i = 0; i < tRounds.length  ; i++) {
	                  updateScorecards = false;
	                  from_time = modinfo.getParameter("tournament_admin_round_from_time_"+i);
	                  to_time = modinfo.getParameter("tournament_admin_round_to_time_"+i);
	                  numberOfStartingtees = modinfo.getParameter("oneAndTen_"+i);

	                  fromString = round_date[i] + " "+ from_time;
	                  toString = round_date[i] + " "+ to_time;
	                  tRound = ((TournamentRoundHome) IDOLookup.getHomeLegacy(TournamentRound.class)).findByPrimaryKey(tRounds[i].getID());
	                  stampFrom = new IWTimestamp(fromString);
	                  stampTo = new IWTimestamp(toString);

	                  if (tournament.isDirectRegistration()) {
	                      tRound.setVisibleStartingtimes(true);
	                  }

	                  if ( (tRound.getRoundDate().getDay() != stampFrom.getTimestamp().getDay()) || (tRound.getRoundDate().getMonth() != stampFrom.getTimestamp().getMonth()) || (tRound.getRoundDate().getYear() != stampFrom.getTimestamp().getYear()) ) {
	                      updateScorecards = true;
	                  }

	                  tRound.setRoundDate(stampFrom.getTimestamp());
	                  tRound.setRoundEndDate(stampTo.getTimestamp());
	                  tRound.setStartingtees(Integer.parseInt(numberOfStartingtees));
	                  tRound.update();

	                  if (updateScorecards) {
	                      scorecards = (Scorecard[]) ((Scorecard) IDOLookup.instanciateEntity(Scorecard.class)).findAllByColumn("tournament_round_id",tRound.getID());
	                      for (int g = 0; g < scorecards.length; g++) {
	                          scorecards[g].setScorecardDate(tRound.getRoundDate());
	                          scorecards[g].update();
	                          is.idega.idegaweb.golf.UpdateHandicap.update(scorecards[g].getMemberId(),new IWTimestamp(tournament.getStartTime()));
	                          //System.out.println("Uppfaerdi skorkort fyrir "+scorecards[g].getMember().getName());
	                      }
	                  }

	              }

	          }
	      }
	      catch (Exception ex) {
	          ex.printStackTrace(System.err);
	      }


	      String isUpdateHandicap = modinfo.getParameter("update_handicap");
	      if (isUpdateHandicap != null) {
	          if (isUpdateHandicap.equalsIgnoreCase("true")) {
	              try {  // updateHandicapForRegisteredMembers
	                  Member[] members = TournamentController.getMembersInTournament(tournament);

	                  if (members != null) {
	                      if (members.length > 0) {
	                          for (int i = 0; i < members.length; i++) {
	                              is.idega.idegaweb.golf.UpdateHandicap.update(members[i], new IWTimestamp(tournament.getStartTime()));
	                          }
	                      }
	                  }
	              }catch (Exception e) {
	                  System.err.println(this.getClassName()+" : saveTournament : updateHandicapForRegisteredMembers");
	                  e.printStackTrace(System.err);
	              }
	          }
	      }


	    TournamentController.removeTournamentTableApplicationAttribute(modinfo);
		add(iwrb.getLocalizedString("tournament.tournament_saved","Tournament saved"));

	}


	public void selectTournament(String controlParameter,IWResourceBundle iwrb)throws SQLException{

		GolfDialog dialog = new GolfDialog(iwrb.getLocalizedString("tournament.choose_tournament","Choose a tournament"));
		add(dialog);
		dialog.add(new DropdownMenu(((Tournament) IDOLookup.instanciateEntity(Tournament.class)).findAll()));

		if (controlParameter.equals("startingtime")){
			dialog.add(startingTimeB1);
		}
	}



	public boolean isInEditMode(IWContext modinfo){
	    return bIsUpdate;
	}


	public Tournament getTournament(IWContext modinfo)throws Exception{
	  String tournament_par = modinfo.getParameter("tournament");
	  Tournament tournament=null;
	  if(tournament_par==null){
	    tournament = (Tournament)modinfo.getSessionAttribute("tournament_admin_tournament");
	  }
	  else{
	    tournament = ((TournamentHome) IDOLookup.getHomeLegacy(Tournament.class)).findByPrimaryKey(Integer.parseInt(tournament_par));
	    modinfo.setSessionAttribute("tournament_admin_tournament",tournament);
	  }
	  return tournament;
	}


}
