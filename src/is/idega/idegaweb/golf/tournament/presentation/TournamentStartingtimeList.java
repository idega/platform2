/*
 * Created on 25.4.2004
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
package is.idega.idegaweb.golf.tournament.presentation;

import is.idega.idegaweb.golf.entity.Field;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberHome;
import is.idega.idegaweb.golf.entity.StartingtimeView;
import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.entity.TournamentRound;
import is.idega.idegaweb.golf.entity.TournamentRoundHome;
import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.handicap.business.Handicap;
import is.idega.idegaweb.golf.presentation.GolfBlock;
import is.idega.idegaweb.golf.tournament.business.TournamentSession;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.FinderException;

import com.ibm.icu.util.StringTokenizer;
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
import com.idega.util.text.TextSoap;

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

			
			boolean areTournamentGroups =  tournament.getTournamentType().getUseGroups();
			
			if (areTournamentGroups) {
				Text tim = new Text(getResourceBundle().getLocalizedString("tournament.time", "Time"));
				Text sc = new Text(getResourceBundle().getLocalizedString("tournament.social_security_number", "Social security number")+"/"+getResourceBundle().getLocalizedString("tournament.name", "Name"));
				Text name = new Text(getResourceBundle().getLocalizedString("tournament.group_name", "Group name"));
				Text club = new Text(getResourceBundle().getLocalizedString("tournament.union", "Union"));
				Text hc = new Text(getResourceBundle().getLocalizedString("tournament.handicap", "Handicap"));
	
				table.add(tim, 1, row);
				table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_CENTER);
				table.add(name, 2, row);
				table.add(sc, 3, row);
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
			} else {
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
			MemberHome mHome = (MemberHome) IDOLookup.getHome(Member.class);

			boolean displayTee = false;
			if (tournamentRound.getStartingtees() > 1) {
				displayTee = true;
			}

			int minutesBetween = tournament.getInterval();
			int numberInGroup = tournament.getNumberInGroup();
			int membersPerTournamentGroup = -1;
			if (areTournamentGroups) {
				membersPerTournamentGroup = tournament.getNumberInTournamentGroup();
			}
			int groupCounterNum = 0;
			Handicap handicap = new Handicap(-1);
//			Map CR = new HashMap();
//			Map slope = new HashMap();
//			int par = field.getFieldPar();
//			try {
//				TournamentGroup[] groups = tournament.getTournamentGroups();
//				for (int i = 0; i < groups.length; i++) {
//					TournamentTournamentGroup[] tTGroup = (TournamentTournamentGroup[]) ((TournamentTournamentGroup) IDOLookup.instanciateEntity(TournamentTournamentGroup.class)).findAllByColumnEquals("tournament_id", tournament.getID() + "", "tournament_group_id", groups[i].getID() + "");
//					for (int j = 0; j < tTGroup.length; j++) {
//						Tee tee = ((TeeHome) IDOLookup.getHomeLegacy(Tee.class)).findByFieldAndTeeColorAndHoleNumber(field.getID(), tTGroup[j].getTeeColorId(), 1);
//						if (tee != null) {
//							CR.put(groups[i].getPrimaryKey(), new Float(tee.getCourseRating()));
//							slope.put(groups[i].getPrimaryKey(), new Integer(tee.getSlope()));
//						}
//					}
//				}
//			}
//			catch (Exception e) {
//			}

			for (int y = 1; y <= tournamentRound.getStartingtees(); y++) {
				// HARÐKÓÐUN DAUÐANS
				int tee_number = 1;
				if (y == 2) tee_number = 10;

				IWTimestamp startHour = new IWTimestamp(tournamentRound.getRoundDate());
				IWTimestamp endHour = new IWTimestamp(tournamentRound.getRoundEndDate());
				endHour.addMinutes(1);

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
						if (areTournamentGroups && membersPerTournamentGroup > 1) {
							int groupID = sView[i].getMemberId();
							Member group = mHome.findByPrimaryKey(groupID);
							String ids = group.getMetaData("group_members");
							StringTokenizer tokanizer = new StringTokenizer(ids, ",");

							if (zebraRow % 2 != 0) {
								styleClass = getLightRowClass();
							}
							else {
								styleClass = getDarkRowClass();
							}
							
							table.setStyleClass(2, row, styleClass);
							table.add(new Text(group.getName()), 2, row);
							HiddenInput nameInp = new HiddenInput("groupname_for_group_"+groupCounter, group.getName());
							table.add(nameInp, 2, row);
							table.setAlignment(2, (row+1), Table.HORIZONTAL_ALIGN_CENTER);
							int id = getTournamentBusiness(modinfo).getTournamentGroup(group, tournament);
//							int leikhandi = handicap.getLeikHandicap(((Integer)slope.get(new Integer(id))).doubleValue(), ((Float)CR.get(new Integer(id))).doubleValue(), (double) par, sView[i].getHandicap());
//							table.add(new Text(getResourceBundle().getLocalizedString("tournament.handicap", "Handicap")+" : "+TextSoap.singleDecimalFormat(sView[i].getHandicap())), 2, (row+1) );

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
//														paid.setChecked(sView[i].getPaid());
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
							
							
							int handirow = (row+1);

							int totalHandi = 0;
							while (tokanizer.hasMoreTokens()) {
								String mID = tokanizer.nextToken();
								Member member = mHome.findByPrimaryKey(new Integer(mID));
								float memberHandicap = member.getHandicap();
//								int id = getTournamentBusiness(modinfo).getTournamentGroup(member, tournament);
//								int leikhandi = handicap.getLeikHandicap(((Integer)slope.get(new Integer(id))).doubleValue(), ((Float)CR.get(new Integer(id))).doubleValue(), (double) par, memberHandicap);
//								totalHandi += leikhandi;
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
									table.add(new Text(member.getName()), 3, row);
									if (member.getMainUnion() != null) {
										table.add(new Text(member.getMainUnion().getAbbrevation()), 4, row);
									} else {
										table.add(new Text("-"), 4, row);
									}
									if (memberHandicap > tournament.getMaxHandicap()) {
										table.add(new Text(TextSoap.singleDecimalFormat(memberHandicap) + " ("+tournament.getMaxHandicap()+")"), 5, row);
									} else {
										table.add(new Text(TextSoap.singleDecimalFormat(memberHandicap)), 5, row);
									}
									table.setStyleClass(2, row, styleClass);
									table.setStyleClass(3, row, styleClass);
									table.setStyleClass(4, row, styleClass);
									table.setStyleClass(5, row, styleClass);

									table.setStyleClass(6, row, styleClass);
									table.setStyleClass(7, row, styleClass);
								}
								else {
									table.mergeCells(2, row, 7, row);
									table.setStyleClass(2, row, styleClass);
								}
								row++;
							}
//							double divider = 5;
//							if (membersPerTournamentGroup == 3) {
//								divider = 7.5;
//							}else if (membersPerTournamentGroup == 4) {
//								divider = 10;
//							}
//							BigDecimal bd = new BigDecimal((double) totalHandi / divider);
//							int leikhandi = bd.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
//							table.add(new Text(getResourceBundle().getLocalizedString("tournament.playing_handicap", "Playing Handicap")+" : "+leikhandi), 2, handirow );
							table.add(new Text(getResourceBundle().getLocalizedString("tournament.playing_handicap", "Playing Handicap")+" : "+(int) group.getHandicap()), 2, handirow );

						} else {
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
								addStartingtimes(tournamentId, table, row, sView, styleClass, i);
							}
							else {
								table.mergeCells(2, row, 7, row);
								table.setStyleClass(2, row, styleClass);
							}
							row++;
						}
					}
					

					row = addMemberInputs(table, row, roundNumber, tee_number, numberInGroup, groupCounter, startInGroup, zebraRow, areTournamentGroups, membersPerTournamentGroup);
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

	private int addMemberInputs(Table table, int row, int roundNumber, int tee_number, int numberInGroup, int groupCounter, int startInGroup, int zebraRow, boolean useTournamentGroups, int membersPerTournamentGroup) {
		TextInput socialNumber;
		String styleClass;
		if (useTournamentGroups) {
			numberInGroup = numberInGroup / membersPerTournamentGroup;
		}

		for (int i = startInGroup; i < (numberInGroup); i++) {

			if (zebraRow % 2 != 0) {
				styleClass = getLightRowClass();
			}
			else {
				styleClass = getDarkRowClass();
			}

			if (!useTournamentGroups) {
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
			} else {

				table.setHeight(row, 10);
				if ((!viewOnly) && (roundNumber == 1)) {
					TextInput nameInp = new TextInput("groupname_for_group_"+groupCounter);
					nameInp.setLength(25);
					table.add(nameInp, 2, row);
				}

				for (int j = 0; j < membersPerTournamentGroup; j++) {
					if (zebraRow % 2 != 0) {
						styleClass = getLightRowClass();
					}
					else {
						styleClass = getDarkRowClass();
					}

					if ((!viewOnly) && (roundNumber == 1)) {
						if (tee_number == 10) {
							socialNumber = (TextInput) getStyledInterface(new TextInput("social_security_number_for_group_" + groupCounter+"_"+i + "_"));
						}
						else {
							socialNumber = (TextInput) getStyledInterface(new TextInput("social_security_number_for_group_" + groupCounter+"_"+i));
						}
						socialNumber.setLength(15);
						table.add(socialNumber, 3, row);
					}
					table.mergeCells(3, row, 7, row);
					table.setStyleClass(2, row, styleClass);
					table.setStyleClass(3, row++, styleClass);
					zebraRow++;
				}

			}
		}
		return row;
	}

	private void addStartingtimes(int tournamentId, Table table, int row, StartingtimeView[] sView, String styleClass, int i) {
		Text dMemberSsn = null;
		Text dMemberName = null;
		Text dMemberHand = null;
		Text dMemberUnion = null;
		CheckBox delete;
		CheckBox paid;
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