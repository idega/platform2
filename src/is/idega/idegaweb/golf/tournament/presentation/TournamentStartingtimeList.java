/*
 * Created on 25.4.2004
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
package is.idega.idegaweb.golf.tournament.presentation;

import is.idega.idegaweb.golf.entity.Field;
import is.idega.idegaweb.golf.entity.StartingtimeView;
import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.entity.TournamentRound;
import is.idega.idegaweb.golf.entity.TournamentRoundHome;
import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.presentation.GolfBlock;
import is.idega.idegaweb.golf.tournament.business.TournamentSession;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.data.IDOLookup;
import com.idega.data.SimpleQuerier;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWTimestamp;

/**
 * @author laddi
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class TournamentStartingtimeList extends GolfBlock {

	private Tournament tournament;
	private String tournament_round_id;
	private boolean viewOnly;
	private boolean onlineRegistration;
	private boolean useBorder;
	private boolean forPrinting=false;
	private Form form;
	private String[] submitButtonParameter;
	
	public TournamentStartingtimeList(Tournament tournament, String tournament_round_id, boolean viewOnly, boolean onlineRegistration, boolean useBorder, boolean forPrinting) {
		this.tournament = tournament;
		this.tournament_round_id = tournament_round_id;
		this.viewOnly = viewOnly;
		this.onlineRegistration = onlineRegistration;
		this.useBorder = useBorder;
		this.forPrinting = forPrinting;
	}
	
	public TournamentStartingtimeList() {
		viewOnly = true;
		forPrinting = false;
		onlineRegistration = false;
		useBorder = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.presentation.PresentationObject#main(com.idega.presentation.IWContext)
	 */
	public void main(IWContext modinfo) throws Exception {
		if (tournament == null) {
			tournament = getTournamentSession(modinfo).getTournament();
		}
		Form cachedForm = null;
		String cacheString = null;
		int tournamentId = -1;

		if (tournament != null) {
			tournamentId = tournament.getID();
			
			if (tournament_round_id == null) {
				tournament_round_id = modinfo.getParameter("tournament_round");
			}
			
			cacheString = "tournament_startingtime_" + tournamentId + "_" + tournament_round_id + "_" + viewOnly + "_" + onlineRegistration + "_" + useBorder;
			cachedForm = (Form) modinfo.getApplicationAttribute(cacheString);
		}

		if (cachedForm != null && !onlineRegistration) {
			//EIKI: TODO ENABLE CACHING FOR ONLINE ALSO
			//THE PROBLEM IS THAT THE form.maintainParameter("action") is causing a
			// hiddenInput(action)
			//being added endlessly to the form from the cache and therefor ruining
			// the online registration
			//because the last action never changes (unless you restart)
			//lower in the method we also don't add to the cache is
			// onlineRegistration is true

			//System.out.println("Getting startingtime table from cache:
			// "+cacheString);
			add(cachedForm);
		}	else if (tournament != null ){
			form = new Form();
			form.add(new HiddenInput("viewOnly", "" + viewOnly));
			
			
			Table topTable = new Table();
			Table table = new Table();
			Table borderTable = new Table();
			borderTable.setWidth(Table.HUNDRED_PERCENT);
			borderTable.setCellpaddingAndCellspacing(0);
			borderTable.setCellBorder(1, 1, 1, "#3A5A20", "solid");
			
			if (forPrinting) {
				table.setWidth("600");
				topTable.setWidth("600");
				borderTable.setWidth("600");
			}
			else {
				table.setWidth(Table.HUNDRED_PERCENT);
				topTable.setWidth(Table.HUNDRED_PERCENT);
				borderTable.setWidth(Table.HUNDRED_PERCENT);
			}
			table.setCellpadding(0);
			table.setCellspacing(0);
			topTable.setCellpaddingAndCellspacing(0);
			
			form.add(topTable);
			borderTable.add(table);
			form.add(borderTable);
			int row = 1;
			int numberOfMember = 0;


			TournamentRound[] tourRounds = tournament.getTournamentRounds();

			int tournamentRoundId = -1;
			if (tournament_round_id == null) {
				tournament_round_id = "-1";
				tournamentRoundId = tourRounds[0].getID();
			}
			else {
				tournamentRoundId = Integer.parseInt(tournament_round_id);
			}

			TournamentRound tournamentRound = null;
			try {
				tournamentRound = ((TournamentRoundHome) IDOLookup.getHomeLegacy(TournamentRound.class)).findByPrimaryKey(tournamentRoundId);
			}
			catch (FinderException fe) {
				throw new SQLException(fe.getMessage());
			}

			boolean display = false;
			if (tournamentRound.getVisibleStartingtimes()) {
				display = true;
			}
			int roundNumber = tournamentRound.getRoundNumber();

			IWTimestamp tourDay = null;

			DropdownMenu rounds = (DropdownMenu) getStyledInterface(new DropdownMenu("tournament_round"));
			if (!onlineRegistration) {
				for (int i = 0; i < tourRounds.length; i++) {
					tourDay = new IWTimestamp(tourRounds[i].getRoundDate());
					rounds.addMenuElement(tourRounds[i].getID(), getResourceBundle().getLocalizedString("tournament.round", "Round") + " " + tourRounds[i].getRoundNumber() + " " + tourDay.getISLDate(".", true));
				}

				if (tournamentRoundId != -1) {
					rounds.setSelectedElement("" + tournamentRound.getID());
				}
				rounds.setToSubmit();
			}
			else {
				tourDay = new IWTimestamp(tournamentRound.getRoundDate());
				rounds.addMenuElement(tournamentRound.getID(), getResourceBundle().getLocalizedString("tournament.round", "Round") + " " + tournamentRound.getRoundNumber() + " " + tourDay.getISLDate(".", true));
			}

			Text timeText;

			if (forPrinting) {
				Text tepText = null;
				Text cepText = null;
				tourDay = new IWTimestamp(tournamentRound.getRoundDate());
				tepText = getText(getResourceBundle().getLocalizedString("tournament.round", "Round") + " " + tournamentRound.getRoundNumber() + " " + tourDay.getISLDate(".", true));
				cepText = getText(tournament.getName());
				topTable.add(cepText, 1, 1);
				topTable.add(tepText, 2, 1);
				topTable.add(new HiddenInput("tournament_round", tournament_round_id), 2, 1);
				topTable.setAlignment(2, row, "right");
			}
			else {
				topTable.setAlignment(1, 1, "right");
				topTable.setCellpaddingRight(1, 1, 5);
				topTable.add(rounds, 1, 1);
			}
			topTable.setHeight(1, 40);

			

			Text dMemberSsn;
			Text dMemberName;
			Text dMemberHand;
			Text dMemberUnion;

			Text tim = new Text(getResourceBundle().getLocalizedString("tournament.time", "Time"));
			Text sc = new Text(getResourceBundle().getLocalizedString("tournament.social_security_number", "Social security number"));
			Text name = new Text(getResourceBundle().getLocalizedString("tournament.name", "Name"));
			Text club = new Text(getResourceBundle().getLocalizedString("tournament.club", "Club"));
			Text hc = new Text(getResourceBundle().getLocalizedString("tournament.handicap", "Handicap"));

			table.add(tim, 1, row);
			table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_CENTER);
			table.add(sc, 2, row);
			table.add(name, 3, row);
			table.add(club, 4, row);
			table.add(hc, 5, row);

			if (viewOnly || onlineRegistration) {
				table.mergeCells(5, row, 7, row);
			}
			else {
				Text paid = new Text(getResourceBundle().getLocalizedString("tournament.paid", "Paid"));
				table.add(paid, 6, row);
				Text del = new Text(getResourceBundle().getLocalizedString("tournament.remove", "Remove"));
				table.add(del, 7, row);
			}
			table.setRowStyleClass(row, getHeaderRowClass());

			java.text.DecimalFormat extraZero = new java.text.DecimalFormat("00");
			java.text.DecimalFormat handicapFormat = new java.text.DecimalFormat("###.0");
			Field field = tournament.getField();
			List members;
			CheckBox delete;

			Image removeImage = getBundle().getImage("/shared/tournament/de.gif", getResourceBundle().getLocalizedString("tournament.remove_from_tournament", "Remove from tournament"));
			removeImage.setToolTip(getResourceBundle().getLocalizedString("tournament.remove_from_tournament", "Remove from tournament"));
			//Flash time;
			Text tTime = new Text("");
			/*if (forPrinting) {
				tTime.setFontStyle("letter-spacing:1px;font-family:Arial,Helvetica,sans-serif;font-size:28px;color:#000000;font-weight:bold;");
			}
			else {
				tTime.setFontStyle("letter-spacing:1px;font-family:Arial,Helvetica,sans-serif;font-size:28px;color:#2C4E3B;font-weight:bold;");
			}*/

			Link remove;
			Text tooMany = getSmallErrorText(getResourceBundle().getLocalizedString("tournament.no_room", "No room"));

			Union union;
			int union_id;
			String abbrevation = "'";

			boolean displayTee = false;
			if (tournamentRound.getStartingtees() > 1) {
				displayTee = true;
			}

			int groupCounterNum = 0;

			for (int y = 1; y <= tournamentRound.getStartingtees(); y++) {
				// HARÐKÓÐUN DAUÐANS
				int tee_number = 1;
				if (y == 2) tee_number = 10;

				IWTimestamp startHour = new IWTimestamp(tournamentRound.getRoundDate());
				IWTimestamp endHour = new IWTimestamp(tournamentRound.getRoundEndDate());
				endHour.addMinutes(1);

				int minutesBetween = tournament.getInterval();
				int numberInGroup = tournament.getNumberInGroup();
				int groupCounter = 0;

				if (displayTee) {
					++row;
					table.setHeight(row, 1);
					table.mergeCells(1, row, 7, row);
					table.setRowColor(row++, this.getLineSeperatorColor());
					Text startTee = new Text(getResourceBundle().getLocalizedString("tournament.starting_tee", "Starting tee") + " : " + tee_number);
					table.add(startTee, 1, row);
					table.mergeCells(1, row, 7, row);
					table.setRowStyleClass(row, getHeaderRowClass());
					table.setAlignment(1, row, "center");
				}

				int startInGroup = 0;
				is.idega.idegaweb.golf.entity.Member tempMember;
				TextInput socialNumber;
				CheckBox paid;
				int zebraRow = 1;

				StartingtimeView[] sView;

				while (endHour.isLaterThan(startHour)) {
					++row;
					++groupCounter;
					++groupCounterNum;
					startInGroup = 0;

					timeText = (Text) tTime.clone();
					timeText.setText(Text.NON_BREAKING_SPACE + extraZero.format(startHour.getHour()) + ":" + extraZero.format(startHour.getMinute()) + Text.NON_BREAKING_SPACE);
					table.add(timeText, 1, row);

					table.setAlignment(1, row, "center");
					if (forPrinting) {
						table.setVerticalAlignment(1, row, "top");
					}
					else {
						table.setVerticalAlignment(1, row, "middle");
					}
					table.mergeCells(1, row, 1, row + (numberInGroup - 1));
					table.setStyleClass(1, row, getBigRowClass());

					sView = getTournamentBusiness(modinfo).getStartingtimeView(tournamentRound.getID(), "", "", "grup_num", groupCounter + "", tee_number, "");


					startInGroup = sView.length;

					String styleClass = null;
					for (int i = 0; i < sView.length; i++) {
						if (zebraRow % 2 != 0) {
							styleClass = getLightRowClass();
						}
						else {
							styleClass = getDarkRowClass();
						}
						zebraRow++;
						
						table.setHeight(row, 10);
						++numberOfMember;
						if (i != 0) table.add(tooMany, 1, row);

						if (display) {
							dMemberSsn = null;
							dMemberName = null;
							dMemberHand = null;
							dMemberUnion = null;
							if (sView[i].getMemberId() != 1) {
								dMemberSsn = new Text(sView[i].getSocialSecurityNumber());
								dMemberName = new Text(sView[i].getName());
								dMemberUnion = new Text(sView[i].getAbbrevation());
								dMemberHand = new Text(com.idega.util.text.TextSoap.singleDecimalFormat(sView[i].getHandicap()));
							}
							else {
								dMemberSsn = new Text("-");
								dMemberName = new Text(getResourceBundle().getLocalizedString("tournament.reserved", "Reserved"));
								dMemberUnion = new Text("-");
								dMemberHand = new Text("-");
							}

							table.add(dMemberSsn, 2, row);
							table.setStyleClass(2, row, styleClass);
							table.add(dMemberName, 3, row);
							table.setStyleClass(3, row, styleClass);
							table.add(dMemberUnion, 4, row);
							table.setStyleClass(4, row, styleClass);
							table.add(dMemberHand, 5, row);
							table.setStyleClass(5, row, styleClass);

							if (!viewOnly) {
								if (!onlineRegistration) {
									paid = getCheckBox("paid", Integer.toString(sView[i].getMemberId()));
									try {
										String[] repps = SimpleQuerier.executeStringQuery("select paid from tournament_member where member_id = "+sView[i].getMemberId()+" and tournament_id = "+tournamentId);
										if (repps != null && repps.length > 0 && "Y".equals(repps[0])) {
											paid.setChecked(true);
										}
									} catch (Exception e) {
										System.out.println("TournamentController : cannot find paid status (message = "+e.getMessage()+")");
									}
//									paid.setChecked(sView[i].getPaid());
									table.add(paid, 6, row);
									table.setStyleClass(6, row, styleClass);
									delete = getCheckBox("deleteMember", Integer.toString(sView[i].getMemberId()));
									table.add(delete, 7, row);
									table.setStyleClass(7, row, styleClass);
								}
								else {
									table.mergeCells(5, row, 7, row);
								}
							}
							else {
								table.mergeCells(5, row, 7, row);
								table.setStyleClass(5, row, styleClass);
							}
						}
						else {
							table.mergeCells(2, row, 7, row);
							table.setStyleClass(2, row, styleClass);
						}
						row++;
					}

					for (int i = startInGroup; i < (numberInGroup); i++) {
						if (zebraRow % 2 != 0) {
							styleClass = getLightRowClass();
						}
						else {
							styleClass = getDarkRowClass();
						}
						zebraRow++;

						table.setHeight(row, 10);
						if ((!viewOnly) && (roundNumber == 1)) {
							if (tee_number == 10) {
								socialNumber = (TextInput) getStyledInterface(new TextInput("social_security_number_for_group_" + groupCounter + "_"));
							}
							else {
								socialNumber = (TextInput) getStyledInterface(new TextInput("social_security_number_for_group_" + groupCounter));
							}
							socialNumber.setLength(15);
							socialNumber.setMaxlength(10);
							table.add(socialNumber, 2, row);
						}
						table.mergeCells(2, row, 7, row);
						table.setStyleClass(2, row++, styleClass);
					}
					startHour.addMinutes(minutesBetween);


					--row;
				}
			}

			++row;
			table.setHeight(row, 10);
			table.setRowStyleClass(row,getHeaderRowClass());
			table.mergeCells(1, row, 3, row);
			Text many = getSmallHeader(getResourceBundle().getLocalizedString("tournament.number_of_participants", "Number of participants") + " : " + numberOfMember);
			table.add(many, 1, row);
			table.setWidth(2, 1);

			table.mergeCells(4, row, 7, row);
			if (!viewOnly) {
				SubmitButton submitButton = (SubmitButton) getButton(new SubmitButton(getResourceBundle().getLocalizedString("tournament.save", "Save")));
				if (submitButtonParameter != null) {
					submitButton = (SubmitButton) getButton(new SubmitButton(getResourceBundle().getLocalizedString("tournament.save", "Save"), submitButtonParameter[0], submitButtonParameter[1]));
				}
				table.add(new HiddenInput("sub_action", "saveDirectRegistration"), 4, row);
				table.add(submitButton, 4, row);
				table.add(new HiddenInput("number_of_groups", "" + groupCounterNum), 4, row);
				table.setAlignment(4, row, "right");
			}

			if (!onlineRegistration) modinfo.setApplicationAttribute(cacheString, form);

			add(form);
		} else {
			logError("Tournament not found in session, or in parameter");
			
		}
	}
	
	public void setSubmitButtonParameter(String name, String value) {
		if (name != null && value != null) {
			submitButtonParameter = new String[]{name, value};
		}
	}
	
	private TournamentSession getTournamentSession(IWContext iwc) {
		try {
			return (TournamentSession) IBOLookup.getSessionInstance(iwc, TournamentSession.class);	
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}
}