package is.idega.idegaweb.golf.tournament.presentation;

import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberHome;
import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.entity.TournamentGroup;
import is.idega.idegaweb.golf.entity.TournamentGroupHome;
import is.idega.idegaweb.golf.entity.TournamentHome;
import is.idega.idegaweb.golf.entity.TournamentTour;
import is.idega.idegaweb.golf.entity.TournamentTourHome;
import is.idega.idegaweb.golf.entity.TournamentTourMember;
import is.idega.idegaweb.golf.entity.TournamentTourMemberHome;
import is.idega.idegaweb.golf.entity.TournamentTourResultMember;
import is.idega.idegaweb.golf.presentation.GolfBlock;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;

import javax.ejb.FinderException;

import com.ibm.icu.util.StringTokenizer;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORuntimeException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.util.IWTimestamp;

public class TournamentTourResults extends GolfBlock {

	private TournamentTour tour = null;
	private int tournamentGroupID = -1;
	private int numberOfMembersToDisplay = -1;
	private String width = null;
	private boolean showTournamentDropdown = true;
	private boolean useFormParameters = true;
	private boolean showTournamentGroupDropdown = true;
	private boolean showPlace = true;

	private static final String PARAMETER_TOURNAMENT_IDS = "ttr_ptmids";
	private static final String PARAMETER_TOURNAMENT_GROUP_ID = "ttr_ptmgrid";
	private static DecimalFormat format = new DecimalFormat("0.00");


	public void main(IWContext modinfo) throws Exception {

		if (tour != null) {
			String currentTournamentIDS = modinfo.getParameter(PARAMETER_TOURNAMENT_IDS);
			Collection tournamentIDs = new Vector();
			if (currentTournamentIDS != null && useFormParameters) {
				if (currentTournamentIDS.startsWith("__")) {
					currentTournamentIDS = currentTournamentIDS.replaceAll("__", "");
					StringTokenizer nizer = new StringTokenizer(currentTournamentIDS, "_");
					while (nizer.hasMoreTokens()) {
						tournamentIDs.add(new Integer(nizer.nextToken()));
					}
				} else if (!currentTournamentIDS.equals("-1")) {
					tournamentIDs.add(new Integer(currentTournamentIDS));
				} else {
					tournamentIDs = getTourHome().getTournamentIDs(tour);
				}
			} else {
				tournamentIDs = getTourHome().getTournamentIDs(tour);
			}
			String grpID = modinfo.getParameter(PARAMETER_TOURNAMENT_GROUP_ID);
			if (grpID != null && useFormParameters) {
				tournamentGroupID = Integer.parseInt(grpID);
			}
			
			
			Table table = new Table();
			if (width != null) {
				table.setWidth(width);
			}
			int row = 1;

			Table dropdownTable = new Table();
			dropdownTable.setCellpadding(0);
			dropdownTable.setCellspacing(0);
			dropdownTable.setWidth("100%");
			
			if (showTournamentGroupDropdown) {
				int[] ids = getTourMemberHome().getTournamentGroupsInUse(tour);
				if (ids != null && ids.length > 0) {
					DropdownMenu groups = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_TOURNAMENT_GROUP_ID));
					groups.keepStatusOnAction(true);
					groups.setToSubmit(true);
					for (int i = 0; i < ids.length; i++) {
						if (i == 0 && tournamentGroupID < 0) {
							tournamentGroupID = ids[i];
						}
						TournamentGroup group = getTournamentGroupHome().findByPrimaryKey(ids[i]);
						groups.addMenuElement(ids[i], group.getName());
					}
					groups.setSelectedElement(tournamentGroupID);
					dropdownTable.add(groups, 1, 1);
				} else {
					showTournamentGroupDropdown = false;
				}
			}
			
			
			if (showTournamentDropdown) {
				DropdownMenu tournaments = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_TOURNAMENT_IDS));
				tournaments.addMenuElement("-1", localize("tournament.all_tournaments", "All Tournaments"));
				Collection allTournamentIDs = getTourHome().getTournamentIDs(tour);
				if (allTournamentIDs != null) {
					Locale locale = modinfo.getCurrentLocale();
					Iterator iter = allTournamentIDs.iterator();
					Vector ids = new Vector();
					Vector names = new Vector();
					String prevID = "_";
					while (iter.hasNext()) {
						Tournament tournament = getTournamentHome().findByPrimaryKey(new Integer(iter.next().toString()));
						names.add(tournament.getName());
						prevID += "_"+tournament.getPrimaryKey().toString();
						ids.add(prevID);

						IWTimestamp stamp = new IWTimestamp(tournament.getStartTime());
						tournaments.addMenuElement(tournament.getPrimaryKey().toString(), tournament.getName()+" - "+stamp.getLocaleDate(locale, IWTimestamp.MEDIUM));
					}

					iter = names.iterator();
					Iterator iiter = ids.iterator();
					while (iter.hasNext()) {
						tournaments.addMenuElement(iiter.next().toString(), localize("tournament.the_score_after", "The score after")+" "+iter.next().toString());
					}
				}
				tournaments.keepStatusOnAction(true);
				tournaments.setToSubmit(true);

				dropdownTable.add(tournaments, 2, 1);
				dropdownTable.setAlignment(2, 1, Table.HORIZONTAL_ALIGN_RIGHT);
			}
			
			if (showTournamentDropdown || showTournamentGroupDropdown) {
				table.add(dropdownTable, 1, row);
				if (showPlace) {
					table.mergeCells(1, row, 4, row);
				} else {
					table.mergeCells(1, row, 3, row);
				}
				++row;
			}
			
			Collection tournamentGroupIDs = null;
			if (tournamentGroupID > 0) {
				tournamentGroupIDs = new Vector();
				tournamentGroupIDs.add(new Integer(tournamentGroupID));
			}

			Collection tourMembers = getTourMemberHome().getScoresOrdered(tour, tournamentIDs, tournamentGroupIDs);
			
			if (showPlace) {
				table.setWidth(1, 10);
				table.add(getHeader(localize("name", "Name")), 2, row);
				table.add(getHeader(localize("union", "Union")), 3, row);
				table.add(getHeader(localize("score", "Score")), 4, row);
				table.setAlignment(4, row, Table.HORIZONTAL_ALIGN_RIGHT);
			} else {
				table.add(getHeader(localize("name", "Name")), 1, row);
				table.add(getHeader(localize("union", "Union")), 2, row);
				table.add(getHeader(localize("score", "Score")), 3, row);
				table.setAlignment(3, row, Table.HORIZONTAL_ALIGN_RIGHT);
			}
			++row;
			if (tourMembers != null) {
				Iterator iter = tourMembers.iterator();
				int counter = 0;
				float prevScore = -1;
				float score = -1;
				boolean cont = true;
				while (iter.hasNext() && cont) {
					++counter;
					if (counter == numberOfMembersToDisplay) {
						cont = false;
					}
					TournamentTourResultMember m = (TournamentTourResultMember) iter.next();
					Member member = getMemberHome().findByPrimaryKey(new Integer(m.getMemberID()));
					score = m.getScore();
					if (showPlace) {
						if (score == prevScore) {
							table.add("-", 1, row);
						} else {
							table.add(Integer.toString(counter), 1, row);
						}
						prevScore = score;
						table.add(member.getName(), 2, row);
						table.add(member.getMainUnion().getAbbrevation(), 3, row);
						table.add(format.format(score), 4, row);
						table.setVerticalAlignment(1, row, Table.VERTICAL_ALIGN_TOP);
						table.setVerticalAlignment(2, row, Table.VERTICAL_ALIGN_TOP);
						table.setVerticalAlignment(3, row, Table.VERTICAL_ALIGN_TOP);
						table.setVerticalAlignment(4, row, Table.VERTICAL_ALIGN_TOP);
						table.setAlignment(4, row, Table.HORIZONTAL_ALIGN_RIGHT);
					} else {
						table.add(member.getName(), 1, row);
						table.add(member.getMainUnion().getAbbrevation(), 2, row);
						table.add(format.format(score), 3, row);
						table.setVerticalAlignment(1, row, Table.VERTICAL_ALIGN_TOP);
						table.setVerticalAlignment(2, row, Table.VERTICAL_ALIGN_TOP);
						table.setVerticalAlignment(3, row, Table.VERTICAL_ALIGN_TOP);
						table.setAlignment(3, row, Table.HORIZONTAL_ALIGN_RIGHT);
					}
					++row;
				}
			}
			Form form = new Form();
			form.add(table);
			add(form);

		} else {
			add(localize("tournament.no_tour_selected", "No tour selected"));
		}

	}

	public void setWidth(String width) {
		super.setWidth(width);
		this.width = width;
	}

	public void setTournamentTourID(int id) {
		try {
			tour = getTourHome().findByPrimaryKey(new Integer(id));
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}

	protected TournamentTourHome getTourHome() {
		try {
			return (TournamentTourHome) IDOLookup.getHome(TournamentTour.class);
		} catch (IDOLookupException e) {
			throw new IDORuntimeException(e);
		}
	}

	protected TournamentHome getTournamentHome() {
		try {
			return (TournamentHome) IDOLookup.getHome(Tournament.class);
		} catch (IDOLookupException e) {
			throw new IDORuntimeException(e);
		}
	}

	protected TournamentTourMemberHome getTourMemberHome() {
		try {
			return (TournamentTourMemberHome) IDOLookup.getHome(TournamentTourMember.class);
		} catch (IDOLookupException e) {
			throw new IDORuntimeException(e);
		}
	}
	protected TournamentGroupHome getTournamentGroupHome() {
		try {
			return (TournamentGroupHome) IDOLookup.getHome(TournamentGroup.class);
		} catch (IDOLookupException e) {
			throw new IDORuntimeException(e);
		}
	}

	protected MemberHome getMemberHome() {
		try {
			return (MemberHome) IDOLookup.getHome(Member.class);
		} catch (IDOLookupException e) {
			throw new IDORuntimeException(e);
		}
	}

	public void setNumberOfMembersToDisplay(int numberOfMembersToDisplay) {
		this.numberOfMembersToDisplay = numberOfMembersToDisplay;
	}

	public void setTournamentGroupID(int tournamentGroupID) {
		this.tournamentGroupID = tournamentGroupID;
	}

	public void setShowTournamentDropdown(boolean showTournamentDropdown) {
		this.showTournamentDropdown = showTournamentDropdown;
	}

	public void setUseFormParameters(boolean useFormParameters) {
		this.useFormParameters = useFormParameters;
	}

	public void setShowTournamentGroupDropdown(boolean showTournamentGroupDropdown) {
		this.showTournamentGroupDropdown = showTournamentGroupDropdown;
	}

	public void setShowPlace(boolean showPlace) {
		this.showPlace = showPlace;
	}


}
