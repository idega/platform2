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
import is.idega.idegaweb.golf.tournament.business.TournamentController;
import is.idega.idegaweb.golf.tournament.business.TournamentSession;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.HorizontalRule;
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
	private boolean forPrinting;
	private Form form;
	
	public TournamentStartingtimeList(Tournament tournament, String tournament_round_id, boolean viewOnly, boolean onlineRegistration, boolean useBorder, boolean forPrinting) {
		this.tournament = tournament;
		this.tournament_round_id = tournament_round_id;
		this.viewOnly = viewOnly;
		this.onlineRegistration = onlineRegistration;
		this.useBorder = useBorder;
		this.forPrinting = forPrinting;
		form = new Form();
	}
	
	public TournamentStartingtimeList() {
		viewOnly = true;
		forPrinting = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.presentation.PresentationObject#main(com.idega.presentation.IWContext)
	 */
	public void main(IWContext modinfo) throws Exception {
		String cacheString = "tournament_startingtime_" + tournament.getID() + "_" + tournament_round_id + "_" + viewOnly + "_" + onlineRegistration + "_" + useBorder;

		Form cachedForm = (Form) modinfo.getApplicationAttribute(cacheString);
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
		}
		else {
			if (tournament == null) {
				tournament = getTournamentSession(modinfo).getTournament();
			}
			
			form.maintainParameter("action");
			form.add(new HiddenInput("viewOnly", "" + viewOnly));

			Table table = new Table();
			table.setBorder(1);
			if (useBorder) {
				table.setBorder(0);
			}
			if (forPrinting) {
				table.setWidth("600");
			}
			else {
				table.setWidth("100%");
			}
			table.setCellpadding(0);
			table.setCellspacing(0);
			form.add(table);
			int row = 1;
			int numberOfMember = 0;

			String headerColor = getHeaderColor();
			String darkColor = getZebraColor2();
			String lightColor = getZebraColor1();
			String activeColor = darkColor;

			if (forPrinting) {
				headerColor = "#000000";
				darkColor = "#FFFFFF";
				lightColor = "#FFFFFF";
				activeColor = "#FFFFFF";
			}

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
				table.add(cepText, 1, row);
				table.add(tepText, 4, row);
				table.mergeCells(1, row, 3, row);
				table.mergeCells(4, row, 8, row);
				table.setAlignment(4, row, "right");
			}
			else {
				table.mergeCells(1, row, 8, row);
				table.setAlignment(1, row, "right");
				table.setCellpaddingRight(1, row, 5);
				table.add(rounds, 1, row);
			}
			table.setHeight(row, 40);

			++row;

			Text dMemberSsn;
			Text dMemberName;
			Text dMemberHand;
			Text dMemberUnion;

			Text tim = getSmallHeader(getResourceBundle().getLocalizedString("tournament.time", "Time"));
			Text sc = getSmallHeader(getResourceBundle().getLocalizedString("tournament.social_security_number", "Social security number"));
			Text name = getSmallHeader(getResourceBundle().getLocalizedString("tournament.name", "Name"));
			Text club = getSmallHeader(getResourceBundle().getLocalizedString("tournament.club", "Club"));
			Text hc = getSmallHeader(getResourceBundle().getLocalizedString("tournament.handicap", "Handicap"));

			table.add(tim, 1, row);
			table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_CENTER);
			table.add(sc, 3, row);
			table.add(name, 4, row);
			table.add(club, 5, row);
			table.add(hc, 6, row);

			if (viewOnly || onlineRegistration) {
				table.mergeCells(6, row, 8, row);
			}
			else {
				Text paid = getSmallHeader(getResourceBundle().getLocalizedString("tournament.paid", "Paid"));
				table.add(paid, 7, row);
				Text del = getSmallHeader(getResourceBundle().getLocalizedString("tournament.remove", "Remove"));
				table.add(del, 8, row);
			}
			table.setRowPadding(row, getCellpadding());
			table.setCellpadding(2, row, 0);
			table.setRowColor(row, headerColor);

			java.text.DecimalFormat extraZero = new java.text.DecimalFormat("00");
			java.text.DecimalFormat handicapFormat = new java.text.DecimalFormat("0.0");
			Field field = tournament.getField();
			List members;
			CheckBox delete;

			Image removeImage = getBundle().getImage("/shared/tournament/de.gif", getResourceBundle().getLocalizedString("tournament.remove_from_tournament", "Remove from tournament"));
			removeImage.setToolTip(getResourceBundle().getLocalizedString("tournament.remove_from_tournament", "Remove from tournament"));
			//Flash time;
			Text tTime = getBigText("");
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

			HorizontalRule hr = new HorizontalRule("100%");
			hr.setNoShade(true);
			hr.setWidth("100%");
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
					table.mergeCells(1, row, 8, row);
					table.setRowColor(row++, this.getLineSeperatorColor());
					Text startTee = getSmallHeader(getResourceBundle().getLocalizedString("tournament.starting_tee", "Starting tee") + " : " + tee_number);
					table.add(startTee, 1, row);
					table.setRowColor(row, headerColor);
					table.mergeCells(1, row, 8, row);
					table.setRowPadding(row, getCellpadding());
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

					if (activeColor.equals(darkColor)) {
						activeColor = lightColor;
					}
					else {
						activeColor = darkColor;
					}

					timeText = (Text) tTime.clone();
					timeText.setText(Text.NON_BREAKING_SPACE + extraZero.format(startHour.getHour()) + ":" + extraZero.format(startHour.getMinute()) + Text.NON_BREAKING_SPACE);
					table.add(timeText, 1, row);
					//time = new
					// Flash("http://jgenerator.sidan.is/time.swt?type=gif&grc=true&time="
					// + extraZero.format(startHour.getHour()) + ":" +
					// extraZero.format(startHour.getMinute()), 60, 38);
					//table.add(time, 1, row);
					table.setAlignment(1, row, "center");
					if (forPrinting) {
						table.setVerticalAlignment(1, row, "top");
					}
					else {
						table.setVerticalAlignment(1, row, "middle");
					}
					//				table.add("<b>&nbsp;"+extraZero.format(startHour.getHour())+":"+extraZero.format(startHour.getMinute())+"</b>",1,row);
					table.mergeCells(1, row, 1, row + ((numberInGroup * 2) - 2));
					table.mergeCells(2, row, 2, row + ((numberInGroup * 2) - 2));
					table.setWidth(2, row, 1);
					table.setColor(2, row, getLineSeperatorColor());
					//				table.setVerticalAlignment(1,row,"top");

					sView = TournamentController.getStartingtimeView(tournamentRound.getID(), "", "", "grup_num", groupCounter + "", tee_number, "");

					// old sView =
					// TournamentController.getStartingtimeView(tournamentRound.getID(),"startingtime_date","'"+startHour.toSQLDateString()+"'","grup_num",groupCounter+"",tee_number,"");
					//sView = (StartingtimeView[]) (new
					// StartingtimeView()).findAll("Select sv.* from startingtime_view sv,
					// tournament_round_startingtime trs where sv.startingtime_id =
					// trs.startingtime_id AND trs.tournament_id =
					// "+tournamentRound.getID()+" AND sv.startingtime_date = '"
					// +startHour.toSQLDateString()+"' AND sv.grup_num ="+groupCounter );
					startInGroup = sView.length;

					for (int i = 0; i < sView.length; i++) {
						if (zebraRow % 2 != 0) {
							activeColor = lightColor;
						}
						else {
							activeColor = darkColor;
						}
						zebraRow++;
						
						table.setHeight(row, 14);
						table.setColor(1, row, lightColor);
						//table.setColor(2, row, activeColor);
						table.setColor(3, row, activeColor);
						table.setColor(4, row, activeColor);
						table.setColor(5, row, activeColor);
						table.setColor(6, row, activeColor);
						table.setColor(7, row, activeColor);
						table.setColor(8, row, activeColor);
						table.setRowPadding(row, getCellpadding());
						table.setCellpadding(2, row, 0);
						++numberOfMember;
						if (i != 0) table.add(tooMany, 1, row);

						if (display) {
							dMemberSsn = null;
							dMemberName = null;
							dMemberHand = null;
							dMemberUnion = null;
							if (sView[i].getMemberId() != 1) {
								dMemberSsn = getSmallText(sView[i].getSocialSecurityNumber());
								dMemberName = getSmallText(sView[i].getName());
								dMemberUnion = getSmallText(sView[i].getAbbrevation());
								dMemberHand = getSmallText(com.idega.util.text.TextSoap.singleDecimalFormat(sView[i].getHandicap()));
							}
							else {
								dMemberSsn = getSmallText("-");
								dMemberName = getSmallText(getResourceBundle().getLocalizedString("tournament.reserved", "Frátekið"));
								dMemberUnion = getSmallText("-");
								dMemberHand = getSmallText("-");
							}

							table.add(dMemberSsn, 3, row);
							table.add(dMemberName, 4, row);
							table.add(dMemberUnion, 5, row);
							table.add(dMemberHand, 6, row);
							//table.add(sView[i].getSocialSecurityNumber(),2,row);
							//abbrevation = sView[i].getAbbrevation();
							//table.add(sView[i].getName() ,3,row);
							//table.add(abbrevation,4,row);
							//table.add(com.idega.util.text.TextSoap.singleDecimalFormat(sView[i].getHandicap()),5,row);
							if (!viewOnly) {
								if (!onlineRegistration) {
									paid = getCheckBox("paid", Integer.toString(sView[i].getMemberId()));
									paid.setChecked(sView[i].getPaid());
									table.add(paid, 7, row);
									delete = getCheckBox("deleteMember", Integer.toString(sView[i].getMemberId()));
									table.add(delete, 8, row);
								}
								else {
									table.mergeCells(6, row, 8, row);
								}
							}
							else {
								table.mergeCells(6, row, 8, row);
							}
						}
						else {
							table.mergeCells(3, row, 8, row);
						}
						row++;
						table.setHeight(row, 1);
						table.mergeCells(3, row, 8, row);
						table.setRowColor(row++, getLineSeperatorColor());
					}

					for (int i = startInGroup; i < (numberInGroup); i++) {
						if (zebraRow % 2 != 0) {
							activeColor = lightColor;
						}
						else {
							activeColor = darkColor;
						}
						zebraRow++;

						table.setHeight(row, 14);
						table.setColor(1, row, lightColor);
						table.setColor(3, row, activeColor);
						table.setColor(4, row, activeColor);
						table.setColor(5, row, activeColor);
						table.setColor(6, row, activeColor);
						table.setColor(7, row, activeColor);
						table.setColor(8, row, activeColor);
						table.setRowPadding(row, getCellpadding());
						table.setCellpadding(2, row, 0);
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
						table.mergeCells(3, row, 8, row);
						table.setHeight(++row, 1);
						table.mergeCells(3, row, 8, row);
						table.setRowColor(row++, getLineSeperatorColor());
					}
					startHour.addMinutes(minutesBetween);

					if (forPrinting) {
						table.mergeCells(1, row, 8, row);
						table.setHeight(1, row, "1");
						table.add(hr, 1, row);
					}
					else {
						--row;
					}
					table.setHeight(row, 1);
					table.setRowColor(row, getLineSeperatorColor());
				}
			}

			++row;
			table.setHeight(row, 14);
			table.setRowColor(row, headerColor);
			table.mergeCells(1, row, 3, row);
			Text many = getSmallHeader(getResourceBundle().getLocalizedString("tournament.number_of_participants", "Number of participants") + " : " + numberOfMember);
			table.add(many, 1, row);
			table.setWidth(2, 1);

			table.mergeCells(4, row, 8, row);
			if (!viewOnly) {
				SubmitButton submitButton = (SubmitButton) getButton(new SubmitButton(getResourceBundle().getLocalizedString("tournament.save", "Save")));
				table.add(new HiddenInput("sub_action", "saveDirectRegistration"), 4, row);
				table.add(submitButton, 4, row);
				table.add(new HiddenInput("number_of_groups", "" + groupCounterNum), 4, row);
				table.setAlignment(4, row, "right");
			}

			if (!onlineRegistration) modinfo.setApplicationAttribute(cacheString, form);

			add(form);
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

	public void maintainParameter(String parameter) {
		form.maintainParameter(parameter);
	}
}