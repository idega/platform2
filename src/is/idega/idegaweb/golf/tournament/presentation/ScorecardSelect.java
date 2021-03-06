/*
 * Created on 14.7.2003 by tryggvil in project golf.project
 */
package is.idega.idegaweb.golf.tournament.presentation;

import java.rmi.RemoteException;

import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.entity.TournamentHome;
import is.idega.idegaweb.golf.entity.TournamentParticipants;
import is.idega.idegaweb.golf.entity.TournamentRound;
import is.idega.idegaweb.golf.entity.TournamentRoundParticipants;
import is.idega.idegaweb.golf.entity.TournamentType;
import is.idega.idegaweb.golf.handicap.presentation.HandicapRegisterWindow;
import is.idega.idegaweb.golf.handicap.presentation.HandicapUtility;
import is.idega.idegaweb.golf.presentation.GolfBlock;

import com.idega.core.localisation.business.LocaleSwitcher;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.Parameter;
import com.idega.presentation.ui.SubmitButton;

/**
 * ScorecardSelect : Initial conversion from scorecard_select.jsp Copyright (C)
 * idega software 2003
 * 
 * @author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson </a>
 * @version 1.0
 */
public class ScorecardSelect extends TournamentBlock {

	IWResourceBundle iwrb;

	public void main(IWContext modinfo) throws Exception {
		iwrb = getResourceBundle();

		String tournament_round_id = modinfo.getParameter("tournament_round_id");
		String tournament_id = null;
		if (getTournamentID(modinfo) > 0) {
			tournament_id = Integer.toString(getTournamentID(modinfo));
		}


		if (tournament_id != null && tournament_round_id == null) {
			Tournament motid = ((TournamentHome) IDOLookup.getHomeLegacy(Tournament.class)).findByPrimaryKey(Integer.parseInt(tournament_id));
			TournamentRound[] round = motid.getTournamentRounds();
			if (round.length == 1) {
				main(tournament_id, Integer.toString(round[0].getID()), modinfo);
			}
			else {
				getRounds(tournament_id, round, modinfo);
			}
		}

		if (tournament_id != null && tournament_round_id != null) {
			main(tournament_id, tournament_round_id, modinfo);
		}

	}

	public void getTournaments(IWContext modinfo) throws RemoteException {
		iwrb = getResourceBundle(modinfo);
		//dialog = new GolfTournamentAdminDialog();
		//super.add(dialog);

		DropdownMenu menu = null;
		GenericButton submit = getButton(new SubmitButton(localize("tournament.continue", "Continue")));

		Form myForm = new Form();
		myForm.setMethod("get");

		Table myTable = new Table(1, 3);
		myTable.setAlignment("center");
		myTable.setAlignment(1, 2, "center");
		myTable.setAlignment(1, 3, "right");
		myTable.setCellpadding(4);

		menu = getTournamentBusiness(modinfo).getDropdownOrderedByUnion(new DropdownMenu("tournament"), modinfo);
		menu.setMarkupAttribute("size", "10");

		Text selectText = new Text(iwrb.getLocalizedString("tournament.choose_tournament", "Choose tournament") + ":");
		selectText.setBold();
		selectText.setFontSize(3);

		myTable.add(selectText, 1, 1);
		myTable.add(menu, 1, 2);
		myTable.add(submit, 1, 3);

		myForm.add(myTable);
		add("<br>");
		add(myForm);

	}

	public void getRounds(String tournament_id, TournamentRound[] round, IWContext modinfo) throws Exception {
		iwrb = getResourceBundle(modinfo);
		//dialog = new GolfTournamentAdminDialog();
		//super.add(dialog);

		DropdownMenu menu = new DropdownMenu("tournament_round_id");
		HiddenInput tournament = new HiddenInput("tournament", tournament_id);
		GenericButton submit = getButton(new SubmitButton(localize("tournament.continue", "Continue")));

		Form myForm = new Form();
		myForm.add(tournament);

		Table myTable = new Table(1, 3);
		myTable.setAlignment("center");
		myTable.setAlignment(1, 2, "center");
		myTable.setAlignment(1, 3, "right");
		myTable.setCellpadding(4);

		DropdownMenu select_round = new DropdownMenu("tournament_round_id");
		select_round.setStyleAttribute("font-size: 7pt");

		for (int y = 0; y < round.length; y++) {
			select_round.addMenuElement(String.valueOf(round[y].getID()), iwrb.getLocalizedString("tournament.round", "Round") + " " + round[y].getRoundNumber());
		}

		Text selectText = new Text(iwrb.getLocalizedString("tournament.choose_round", "Choose round") + ":");
		selectText.setBold();
		selectText.setFontSize(3);

		myTable.add(selectText, 1, 1);
		myTable.add(select_round, 1, 2);
		myTable.add(submit, 1, 3);

		myForm.add(myTable);
		add("<br>");
		add(myForm);

	}

	public void main(String tournament_id, String tournament_round_id, IWContext modinfo) throws Exception {
		IWBundle iwb = getBundle(modinfo);
		//dialog = new GolfTournamentAdminDialog();
		//super.add(dialog);
		getParentPage().setPageFontSize("1");
		
		Tournament tournament = ((TournamentHome) IDOLookup.getHomeLegacy(Tournament.class)).findByPrimaryKey(Integer.parseInt(tournament_id));
		TournamentType tournamentType = tournament.getTournamentType();

		String order = modinfo.getParameter("order");
		if (order == null)
			order = "byStartingTime";

		Table dialogTable = new Table(1, 2);
		dialogTable.setWidth("90%");

		Form orderForm = new Form();
		orderForm.setMethod("get");
		orderForm.add(new HiddenInput("tournament", tournament_id));
		orderForm.add(new HiddenInput("tournament_round_id", tournament_round_id));

		Table orderTable = new Table(1, 1);
		orderTable.setCellpadding(0);
		orderTable.setCellspacing(0);
		orderTable.setAlignment("right");

		DropdownMenu orderMenu = new DropdownMenu("order");
		orderMenu.addMenuElement("alphabetical", iwrb.getLocalizedString("tournament.order_by_name", "Order by name"));
		orderMenu.addMenuElement("byStartingTime", iwrb.getLocalizedString("tournament.order_by_tee_time", "Order by tee time"));
		orderMenu.setSelectedElement(order);
		orderTable.add(orderMenu, 1, 1);
		orderForm.add(orderTable);

		SubmitButton orderButton = new SubmitButton(iwrb.getLocalizedString("tournament.reorder", "Reorder"));
		orderTable.addText(" ");
		orderTable.add(orderButton, 1, 1);

		Form myForm = new Form();
		myForm.setWindowToOpen(GroupScorecardWindow.class);
		myForm.add(new HiddenInput("tournament_id", tournament_id));
		myForm.add(new HiddenInput("tournament_round_id", tournament_round_id));
		Parameter[] parentReloadParameters = getReloadParametersToAdd(tournament_id, tournament_round_id, modinfo);
		for (int i = 0; i < parentReloadParameters.length; i++) {
			myForm.add(parentReloadParameters[i]);
		}

		Table myTable = new Table();
		myTable.setAlignment("center");
		myTable.setCellpadding(3);
		myTable.setCellspacing(1);
		myTable.setWidth("100%");

		Text textProxy = new Text("");
		textProxy.setFontSize(1);

		Text checkText = (Text) textProxy.clone();
		checkText.setText(iwrb.getLocalizedString("tournament.register", "Register"));
		Text memberText = (Text) textProxy.clone();
		memberText.setText(iwrb.getLocalizedString("tournament.golfer", "Golfer"));
		Text unionText = (Text) textProxy.clone();
		unionText.setText(iwrb.getLocalizedString("tournament.club", "Club"));
		Text groupText = (Text) textProxy.clone();
		groupText.setText(iwrb.getLocalizedString("tournament.group", "Group"));
		Text handicapText = (Text) textProxy.clone();
		handicapText.setText(iwrb.getLocalizedString("tournament.handicap_short", "Hcp."));
		Text holeText = (Text) textProxy.clone();
		holeText.setText(iwrb.getLocalizedString("tournament.hole", "Hole"));
		Text strokeText = (Text) textProxy.clone();
		strokeText.setText(iwrb.getLocalizedString("tournament.total", "Total"));
		Text strokeText2 = (Text) textProxy.clone();
		strokeText2.setText(iwrb.getLocalizedString("tournament.net", "Net"));
		Text pointsText = (Text) textProxy.clone();
		pointsText.setText(iwrb.getLocalizedString("tournament.points", "Points"));
		Text parText = (Text) textProxy.clone();
		parText.setText(iwrb.getLocalizedString("tournament.paf", "Par"));

		myTable.add(checkText, 1, 1);
		myTable.add(memberText, 2, 1);
		myTable.add(unionText, 3, 1);
		myTable.add(groupText, 4, 1);
		myTable.add(handicapText, 5, 1);
		myTable.add(holeText, 6, 1);
		myTable.add(strokeText, 7, 1);
		myTable.add(strokeText2, 8, 1);
		myTable.add(pointsText, 9, 1);
		myTable.add(parText, 10, 1);

		Text proxyText = new Text("");
		CheckBox checkProxy = new CheckBox("members");

		int numberOfGolfers = 0;

		if (order.equalsIgnoreCase("alphabetical")) {
			TournamentParticipants[] members = getTournamentBusiness(modinfo).getTournamentParticipants("tr.tournament_round_id", tournament_round_id, "first_name,last_name,middle_name");

			numberOfGolfers = members.length;
			for (int a = 0; a < members.length; a++) {

				int scorecard_id = members[a].getScorecardID();

				CheckBox check = (CheckBox) checkProxy.clone();
				check.setContent(Integer.toString(members[a].getMemberID()));

				Text tournamentMember = (Text) proxyText.clone();
				tournamentMember.setText(members[a].getName());
				Link memberLink = new Link(tournamentMember);
				memberLink.setWindowToOpen(HandicapRegisterWindow.class);
				for (int i = 0; i < parentReloadParameters.length; i++) {
					memberLink.addParameter(parentReloadParameters[i]);
				}
				if (members[a].getScorecardID() > 0) {
					memberLink.addParameter("scorecard_id", members[a].getScorecardID());
				}
				else {
					memberLink.addParameter("member_id", members[a].getMemberID());
				}
				Text memberUnion = (Text) proxyText.clone();
				memberUnion.setText(members[a].getAbbrevation());
				Text memberGroup = (Text) proxyText.clone();
				memberGroup.setText(members[a].getGroupName());

				int holesPlayed = members[a].getHolesPlayed();
				if (holesPlayed == -1) {
					holesPlayed = 1;
				}

				int totalStrokes = members[a].getStrokesWithoutHandicap();
				if (totalStrokes == -1) {
					totalStrokes = 0;
				}

				int playHandicap = (int) members[a].getRoundHandicap();
				if (playHandicap == -1) {
					playHandicap = 0;
				}

				int totalStrokes2 = members[a].getStrokesWithHandicap();
				if (totalStrokes2 == -1) {
					totalStrokes2 = 0;
				}

				int totalPoints = members[a].getTotalPoints();
				if (totalPoints == -1) {
					totalPoints = 0;
				}

				int difference = members[a].getDifference();
				if (difference == -1 && holesPlayed == 0) {
					difference = 0;
				}

				String out_differ = "E";
				if (difference == 0) {
					out_differ = "E";
				}
				else if (difference < 0) {
					out_differ = String.valueOf(difference);
				}
				else if (difference > 0) {
					out_differ = "+" + String.valueOf(difference);
				}

				myTable.add(check, 1, a + 2);
				myTable.add(memberLink, 2, a + 2);
				myTable.add(memberUnion, 3, a + 2);
				myTable.add(memberGroup, 4, a + 2);
				myTable.add(playHandicap + "", 5, a + 2);
				myTable.addText(holesPlayed + "", 6, a + 2);
				if (totalStrokes > 0) {
					myTable.addText(totalStrokes + "", 7, a + 2);
				}
				if (totalStrokes2 > 0) {
					myTable.addText(totalStrokes2 + "", 8, a + 2);
				}
				myTable.addText(totalPoints + "", 9, a + 2);
				if (totalStrokes > 0) {
					myTable.addText(out_differ + "", 10, a + 2);
				}

				Image changeImage = iwb.getImage("shared/icons/tees.gif", iwrb.getLocalizedString("tournament.change_tees", "Change tees"));
				changeImage.setAlignment("absmiddle");
				Link changeLink = new Link(changeImage);
				changeLink.setWindowToOpen(HandicapUtility.class);
				changeLink.addParameter(HandicapUtility.PARAMETER_SCORECARD_ID, scorecard_id);
				changeLink.addParameter(HandicapUtility.PARAMETER_METHOD, HandicapUtility.ACTION_CHANGE_TEES);

				Image changeImage2 = iwb.getImage("shared/icons/updown.gif", iwrb.getLocalizedString("tournament.update_handicap", "Update handicap"));
				changeImage2.setAlignment("absmiddle");
				Link changeLink2 = new Link(changeImage2);
				changeLink2.setWindowToOpen(HandicapUtility.class);
				changeLink2.addParameter(HandicapUtility.PARAMETER_MEMBER_ID, members[a].getMemberID());
				changeLink2.addParameter(HandicapUtility.PARAMETER_TOURNAMENT_ID, members[a].getTournamentID());
				changeLink2.addParameter(HandicapUtility.PARAMETER_METHOD, HandicapUtility.ACTION_UPDATE_HANDICAP);


				Image positionImage = iwb.getImage("shared/icons/position.gif", iwrb.getLocalizedString("tournament.change_position", "Change position"));
				positionImage.setAlignment("absmiddle");
				Link positionLink = new Link(positionImage);
				positionLink.setWindowToOpen(ChangePositionWindow.class);
				positionLink.addParameter("member_id", members[a].getMemberID());
				positionLink.addParameter("tournament_id", members[a].getTournamentID());

				Image groupImage = iwb.getImage("shared/icons/edit.gif", iwrb.getLocalizedString("tournament.change_group", "Change group"));
				groupImage.setAlignment("absmiddle");
				Link groupLink = new Link(groupImage);
				groupLink.setWindowToOpen(ChangeGroupWindow.class);
				groupLink.addParameter("member_id", members[a].getMemberID());
				groupLink.addParameter("tournament_id", members[a].getTournamentID());


				Image dismissImage = iwb.getImage("shared/icons/delete.gif", iwrb.getLocalizedString("tournament.dismiss", "Dismiss"));
				dismissImage.setAlignment("absmiddle");
				Link dismissLink = new Link(dismissImage);
				dismissLink.setWindowToOpen(DismissWindow.class);
				dismissLink.addParameter("member_id", members[a].getMemberID());
				dismissLink.addParameter("tournament_id", members[a].getTournamentID());

				myTable.add(changeLink, 11, a + 2);
				myTable.add("&nbsp;", 11, a + 2);
				myTable.add(changeLink2, 11, a + 2);
				myTable.add("&nbsp;", 11, a + 2);
				myTable.add(groupLink, 11, a + 2);
				myTable.add("&nbsp;", 11, a + 2);
				myTable.add(dismissLink, 11, a + 2);
				if (tournamentType.getWithoutHandicap()) {
					myTable.add("&nbsp;", 11, a + 2);
					myTable.add(positionLink, 11, a + 2);
				}

			}

			if (members.length == 0) {
				myTable.addText(iwrb.getLocalizedString("tournament.no_golfer_in_tournament", "No golfer registered in tournament"), 1, 2);
			}

		}
		else if (order.equalsIgnoreCase("byStartingTime")) {
			TournamentRoundParticipants[] members = getTournamentBusiness(modinfo).getTournamentRoundParticipants("tr.tournament_round_id", tournament_round_id, "grup_num");

			numberOfGolfers = members.length;
			for (int a = 0; a < members.length; a++) {

				int scorecard_id = members[a].getScorecardID();

				CheckBox check = (CheckBox) checkProxy.clone();
				check.setContent(Integer.toString(members[a].getMemberID()));

				Text tournamentMember = (Text) proxyText.clone();
				tournamentMember.setText(members[a].getName());
				Link memberLink = new Link(tournamentMember);
				memberLink.setWindowToOpen(HandicapRegisterWindow.class);
				for (int i = 0; i < parentReloadParameters.length; i++) {
					memberLink.addParameter(parentReloadParameters[i]);
				}
				if (members[a].getScorecardID() > 0) {
					memberLink.addParameter("scorecard_id", members[a].getScorecardID());
				}
				else {
					memberLink.addParameter("member_id", members[a].getMemberID());
				}
				Text memberUnion = (Text) proxyText.clone();
				memberUnion.setText(members[a].getAbbrevation());
				Text memberGroup = (Text) proxyText.clone();
				memberGroup.setText(members[a].getGroupName());

				int holesPlayed = members[a].getHolesPlayed();
				if (holesPlayed == -1) {
					holesPlayed = 1;
				}

				int totalStrokes = members[a].getStrokesWithoutHandicap();
				if (totalStrokes == -1) {
					totalStrokes = 0;
				}

				int playHandicap = (int) members[a].getRoundHandicap();
				if (playHandicap == -1) {
					playHandicap = 0;
				}

				int totalStrokes2 = members[a].getStrokesWithHandicap();
				if (totalStrokes2 == -1) {
					totalStrokes2 = 0;
				}

				int totalPoints = members[a].getTotalPoints();
				if (totalPoints == -1) {
					totalPoints = 0;
				}

				int difference = members[a].getDifference();
				if (difference == -1 && holesPlayed == 0) {
					difference = 0;
				}

				String out_differ = "E";
				if (difference == 0) {
					out_differ = "E";
				}
				else if (difference < 0) {
					out_differ = String.valueOf(difference);
				}
				else if (difference > 0) {
					out_differ = "+" + String.valueOf(difference);
				}

				myTable.add(check, 1, a + 2);
				myTable.add(memberLink, 2, a + 2);
				myTable.add(memberUnion, 3, a + 2);
				myTable.add(memberGroup, 4, a + 2);
				myTable.add(playHandicap + "", 5, a + 2);
				myTable.addText(holesPlayed + "", 6, a + 2);
				if (totalStrokes > 0) {
					myTable.addText(totalStrokes + "", 7, a + 2);
				}
				if (totalStrokes2 > 0) {
					myTable.addText(totalStrokes2 + "", 8, a + 2);
				}
				myTable.addText(totalPoints + "", 9, a + 2);
				if (totalStrokes > 0) {
					myTable.addText(out_differ + "", 10, a + 2);
				}


				Image changeImage = iwb.getImage("shared/icons/tees.gif", iwrb.getLocalizedString("tournament.change_tees", "Change tees"));
				changeImage.setAlignment("absmiddle");
				Link changeLink = new Link(changeImage);
				changeLink.setWindowToOpen(HandicapUtility.class);
				changeLink.addParameter(HandicapUtility.PARAMETER_SCORECARD_ID, scorecard_id);
				changeLink.addParameter(HandicapUtility.PARAMETER_METHOD, HandicapUtility.ACTION_CHANGE_TEES);

				Image changeImage2 = iwb.getImage("shared/icons/updown.gif", iwrb.getLocalizedString("tournament.update_handicap", "Update handicap"));
				changeImage2.setAlignment("absmiddle");
				Link changeLink2 = new Link(changeImage2);
				changeLink2.setWindowToOpen(HandicapUtility.class);
				changeLink2.addParameter(HandicapUtility.PARAMETER_MEMBER_ID, members[a].getMemberID());
				changeLink2.addParameter(HandicapUtility.PARAMETER_TOURNAMENT_ID, members[a].getTournamentID());
				changeLink2.addParameter(HandicapUtility.PARAMETER_METHOD, HandicapUtility.ACTION_UPDATE_HANDICAP);

				Image positionImage = iwb.getImage("shared/icons/position.gif", iwrb.getLocalizedString("tournament.change_position", "Change position"));
				positionImage.setAlignment("absmiddle");
				Link positionLink = new Link(positionImage);
				positionLink.setWindowToOpen(ChangePositionWindow.class);
				positionLink.addParameter("member_id", members[a].getMemberID());
				positionLink.addParameter("tournament_id", members[a].getTournamentID());

				Image groupImage = iwb.getImage("shared/icons/edit.gif", iwrb.getLocalizedString("tournament.change_group", "Change group"));
				groupImage.setAlignment("absmiddle");
				Link groupLink = new Link(groupImage);
				groupLink.setWindowToOpen(ChangeGroupWindow.class);
				groupLink.addParameter("member_id", members[a].getMemberID());
				groupLink.addParameter("tournament_id", members[a].getTournamentID());

				Image dismissImage = iwb.getImage("shared/icons/delete.gif", iwrb.getLocalizedString("tournament.dismiss", "Dismiss"));
				dismissImage.setAlignment("absmiddle");
				Link dismissLink = new Link(dismissImage);
				dismissLink.setWindowToOpen(DismissWindow.class);
				dismissLink.addParameter("member_id", members[a].getMemberID());
				dismissLink.addParameter("tournament_id", members[a].getTournamentID());

				myTable.add(changeLink, 11, a + 2);
				myTable.add("&nbsp;", 11, a + 2);
				myTable.add(changeLink2, 11, a + 2);
				myTable.add("&nbsp;", 11, a + 2);
				myTable.add(groupLink, 11, a + 2);
				myTable.add("&nbsp;", 11, a + 2);
				myTable.add(dismissLink, 11, a + 2);
				if (tournamentType.getWithoutHandicap()) {
					myTable.add("&nbsp;", 11, a + 2);
					myTable.add(positionLink, 11, a + 2);
				}

			}

			if (members.length == 0) {
				myTable.addText(iwrb.getLocalizedString("tournament.no_tee_times", "No tee times distributed"), 1, 2);
			}

		}

		int rows = myTable.getRows();
		myTable.mergeCells(1, rows + 1, 10, rows + 1);
		myTable.mergeCells(1, rows + 2, 5, rows + 2);
		myTable.mergeCells(6, rows + 2, 10, rows + 2);
		myTable.setAlignment(6, rows + 2, "right");

		myTable.addText("<hr size=\"1\" noshade align=\"left\" width=\"150\">" + iwrb.getLocalizedString("tournament.number_of_contestants", "Number of contestants") + ": " + numberOfGolfers, 1, rows + 1);

		myTable.add(getButton(new SubmitButton(localize("tournament.register", "Register"))), 6, rows + 2);
		Link backLink = new Link(getTournamentBusiness(modinfo).getBackLink(modinfo));
		myTable.add(backLink, 1, rows + 2);
 
		myForm.addBreak();
		myForm.add(myTable);

		dialogTable.add(orderForm, 1, 1);
		dialogTable.add(myForm, 1, 2);

		add(dialogTable);
	}

	protected boolean tournamentMustBeSet() {
		return true;
	}

	private Parameter[] getReloadParametersToAdd(String tournament_id, String tournament_round_id,IWContext modinfo) {
		Parameter[] parentWindowParameters = new Parameter[4];
		parentWindowParameters[0] = new Parameter(GolfBlock.PRM_PARENT_PREFIX+"tournament",tournament_id);
		parentWindowParameters[1] = new Parameter(GolfBlock.PRM_PARENT_PREFIX+IWMainApplication.classToInstanciateParameter,modinfo.getParameter(IWMainApplication.classToInstanciateParameter));
		parentWindowParameters[2] = new Parameter(GolfBlock.PRM_PARENT_PREFIX+"tournament_round_id",tournament_round_id);
		parentWindowParameters[3] = new Parameter(GolfBlock.PRM_PARENT_PREFIX+LocaleSwitcher.languageParameterString,modinfo.getParameter(LocaleSwitcher.languageParameterString));
		return parentWindowParameters;

	}
	
}
