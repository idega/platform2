package is.idega.idegaweb.golf.tournament.presentation;

/**
 * Title: TournamentResults Description: Displayes the results of a tournament
 * Copyright: Copyright (c) 2001 Company: idega co.
 * 
 * @author Laddi
 * @version 1.3
 */

import is.idega.idegaweb.golf.entity.Tee;
import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.entity.TournamentGroup;
import is.idega.idegaweb.golf.entity.TournamentGroupHome;
import is.idega.idegaweb.golf.entity.TournamentHome;
import is.idega.idegaweb.golf.entity.TournamentRound;
import is.idega.idegaweb.golf.entity.TournamentRoundHome;
import is.idega.idegaweb.golf.tournament.business.ResultComparator;
import is.idega.idegaweb.golf.tournament.business.ResultDataHandler;
import is.idega.idegaweb.golf.tournament.business.ResultsCollector;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Window;

public class TournamentResultsDetailed extends Block {

	private final static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.golf";
	protected IWResourceBundle iwrb;
	protected IWBundle iwb;

	private int tournamentId_ = -1;
	private int tournamentGroupId_ = -1;
	private int tournamentRound_ = -1;

	private Tournament tournament;
	private TournamentRound tournamentRound;
	private Vector result = null;
	private Table myTable;
	private Text blackText;
	private Text whiteText;

	private int[] pastTournamentRounds_ = null;
	private int[] allTournamentRounds_;

	private int totalPar = 0;
	private int outValue = 0;
	private int inValue = 0;

	public TournamentResultsDetailed(int tournamentId, int tournamentGroupId, int tournamentRound) {
		tournamentId_ = tournamentId;
		tournamentGroupId_ = tournamentGroupId;
		tournamentRound_ = tournamentRound;
	}

	public void main(IWContext modinfo) throws SQLException {
		try {
			iwrb = getResourceBundle(modinfo);
			iwb = getBundle(modinfo);
			try {
				tournament = ((TournamentHome) IDOLookup.getHomeLegacy(Tournament.class)).findByPrimaryKey(tournamentId_);
				tournamentRound = ((TournamentRoundHome) IDOLookup.getHomeLegacy(TournamentRound.class)).findByPrimaryKey(tournamentRound_);
			}
			catch (FinderException fe) {
				throw new SQLException(fe.getMessage());
			}
			getMemberVector();
			setMemberVectorPastRounds();
			setMemberVectorAllRounds();
			if (result != null) {
				if (result.size() > 1) {
					sortMemberVector();
				}
			}

			setValues();
			getFieldInfo();
			getResults();
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

	private void getResults() {
		try {
			int size = result.size();
			int row = 5;

			int[] tournamentRounds_ = new int[1];
			tournamentRounds_[0] = tournamentRound_;

			ResultDataHandler handler;

			for (int a = 0; a < size; a++) {
				ResultsCollector r = (ResultsCollector) result.elementAt(a);

				handler = new ResultDataHandler(tournamentId_, ResultComparator.TOTALSTROKES, tournamentGroupId_, tournamentRounds_, r.getMemberId());
				Vector v = handler.getTournamentMembers();
				if (v != null) {
					if (v.size() > 0) {
						r = (ResultsCollector) v.elementAt(0);
					}
				}

				if (size <= 1) {
					r.calculateCompareInfo();
				}

				if (r.getDismissal() == 0 || (r.getDismissal() == 15 && tournamentRound.getRoundNumber() <= 2)) {
					myTable.mergeCells(1, row + 2, 22, row + 2);
					myTable.addText("", 1, row + 2);
					myTable.setHeight(1, row + 2, "5");
					getMemberScore(r, row, a + 1);
					row += 3;
				}
			}

			for (int a = 2; a <= myTable.getColumns(); a++) {
				myTable.setColumnAlignment(a, "center");
			}
			myTable.setColumnAlignment(1, "left");
			myTable.setAlignment(1, 1, "center");
			myTable.setAlignment(1, 2, "right");
			myTable.setAlignment(1, 3, "right");
			myTable.setRowColor(1, "#2C4E3B");
			myTable.setRowColor(2, "#2C4E3B");
			myTable.setRowColor(3, "#DCEFDE");
			myTable.mergeCells(1, 4, 22, 4);
			myTable.addText("", 1, 4);
			//myTable.setColor(1,4,"#2C4E3B");
			myTable.setHeight(1, 4, "5");

			add(myTable);
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	private void getMemberScore(ResultsCollector r, int row, int position) {
		try {
			int column = 2;
			int strokeValue = 0;
			int parValue = 0;
			int outScore = 0;
			int inScore = 0;
			int totalStrokes = 0;
			int difference = 0;

			if (this.pastTournamentRounds_ != null) {
				ResultDataHandler handler = new ResultDataHandler(tournamentId_, ResultComparator.TOTALSTROKES, tournamentGroupId_, pastTournamentRounds_, r.getMemberId());
				Vector v = handler.getTournamentMembers();
				if (v != null) {
					if (v.size() > 0) {
						ResultsCollector rip = (ResultsCollector) v.get(0);
						if (rip != null) {
							rip.calculateCompareInfo();
							difference = rip.getDifference();
						}
					}
				}
			}

			myTable.setHeight(row, "20");
			myTable.setHeight(row + 1, "20");
			myTable.setRowColor(row, "#EAFAEC");
			myTable.setRowColor(row + 1, "#DCEFDE");

			Text positionText = (Text) blackText.clone();
			positionText.setText("&nbsp;" + Integer.toString(position) + ".&nbsp;");
			Text member = (Text) blackText.clone();
			member.setText(r.getName());

			Link seeScores = new Link(member);
			seeScores.setWindowToOpen(HandicapScoreWindow.class);
			seeScores.addParameter("member_id", r.getMemberId());
			seeScores.addParameter("tournament_id", tournamentId_);
			seeScores.addParameter("tournament_group_id", r.getTournamentGroupId());
			myTable.add(positionText, 1, row);
			myTable.add(seeScores, 1, row);

			Vector strokes = r.getStrokes();
			Vector pars = r.getPar();

			if (strokes != null) {
				int strokeSize = strokes.size();
				for (int b = 0; b < strokeSize; b++) {
					strokeValue = ((Double) strokes.elementAt(b)).intValue();

					if (strokeValue > 0) {

						parValue = ((Integer) pars.elementAt(b)).intValue();

						difference += strokeValue - parValue;
						totalStrokes += strokeValue;

						Text stroke = (Text) blackText.clone();
						stroke.setText(Integer.toString(strokeValue));
						Text differ = getDifference(difference);

						String color = this.getBackgroundColor(strokeValue, parValue);
						if (color != null) {
							myTable.setColor(column, row, color);
							stroke.setFontColor("#FFFFFF");
						}

						myTable.add(stroke, column, row);
						myTable.add(differ, column, row + 1);
						column++;

						if (b + 1 == 9) {
							outScore = totalStrokes;
							Text outValueText = (Text) blackText.clone();
							outValueText.setText(Integer.toString(outScore));
							outValueText.setFontColor(getColor(outScore, outValue));
							myTable.add(outValueText, column, row);
							myTable.add(differ, column, row + 1);
							column++;
						}

						if (b + 1 == 18) {
							inScore = totalStrokes - outScore;
							Text inValueText = (Text) blackText.clone();
							inValueText.setText(Integer.toString(inScore));
							inValueText.setFontColor(getColor(inScore, inValue));
							myTable.add(inValueText, column, row);
							myTable.add(differ, column, row + 1);
							column++;
						}

					}

				}

				Text totalText = (Text) blackText.clone();
				totalText.setText(Integer.toString(totalStrokes));
				totalText.setFontColor(getColor(totalStrokes, totalPar));
				myTable.add(totalText, 22, row);
				myTable.add(getDifference(difference), 22, row + 1);
			}
			else {
				myTable.add(getDifference(difference), 22, row + 1);
			}

		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	private Text getDifference(int difference) {
		String differ = "";
		Text differenceText = (Text) blackText.clone();

		if (difference > 0) {
			differ = "+" + Integer.toString(difference);
			differenceText.setText(differ);
			differenceText.setFontColor("#0000FF");
		}
		else if (difference == 0) {
			differ = "E";
			differenceText.setText(differ);
		}
		else {
			differ = Integer.toString(difference);
			differenceText.setText(differ);
			differenceText.setFontColor("#FF0000");
		}

		return differenceText;
	}

	private void setValues() {
		try {
			getParentPage().setAllMargins(0);
			getParentPage().setTitle(tournament.getName());

			myTable = new Table();
			myTable.setAlignment("center");
			myTable.setCellpadding(1);
			myTable.setCellspacing(1);
			myTable.setWidth("780");

			blackText = new Text();
			blackText.setFontSize(Text.FONT_SIZE_7_HTML_1);

			whiteText = new Text();
			whiteText.setFontSize(Text.FONT_SIZE_7_HTML_1);
			whiteText.setFontColor("#FFFFFF");
			whiteText.setBold();
			whiteText.setFontFace(Text.FONT_FACE_VERDANA);
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	private void getFieldInfo() {
		try {
			int parValue = 0;
			int column = 2;
			Tee[] tee = (Tee[]) ((Tee) IDOLookup.instanciateEntity(Tee.class)).findAll("select distinct hole_number,par from tee where field_id = " + tournament.getFieldId() + " order by hole_number");

			Text holeText = (Text) whiteText.clone();
			holeText.setText(iwrb.getLocalizedString("tournament.hole", "Hole") + "&nbsp;");
			Text parText = (Text) blackText.clone();
			parText.setText(iwrb.getLocalizedString("tournament.par", "Par") + "&nbsp;");
			parText.setBold();
			parText.setFontFace(Text.FONT_FACE_VERDANA);
			Text outText = (Text) whiteText.clone();
			outText.setText(iwrb.getLocalizedString("tournament.out", "Out"));
			outText.setBold();
			outText.setFontFace(Text.FONT_FACE_VERDANA);
			Text inText = (Text) whiteText.clone();
			inText.setText(iwrb.getLocalizedString("tournament.in", "In"));
			inText.setBold();
			inText.setFontFace(Text.FONT_FACE_VERDANA);
			Text totalText = (Text) whiteText.clone();
			totalText.setText(iwrb.getLocalizedString("tournament.total", "Total"));
			totalText.setBold();
			totalText.setFontFace(Text.FONT_FACE_VERDANA);

			for (int b = 0; b < tee.length; b++) {
				parValue = tee[b].getPar();
				totalPar += parValue;
				Text par = (Text) blackText.clone();
				par.setText(Integer.toString(parValue));
				par.setBold();
				par.setFontFace(Text.FONT_FACE_VERDANA);
				Text hole = (Text) whiteText.clone();
				hole.setText(Integer.toString(b + 1));

				myTable.add(hole, column, 2);
				myTable.add(par, column, 3);
				myTable.setWidth(column, 2, "25");
				column++;

				if (b + 1 == 9) {
					outValue = totalPar;
					Text outValueText = (Text) blackText.clone();
					outValueText.setText(Integer.toString(outValue));
					outValueText.setBold();
					outValueText.setFontFace(Text.FONT_FACE_VERDANA);
					myTable.add(outText, column, 2);
					myTable.setWidth(column, 2, "30");
					myTable.add(outValueText, column, 3);
					column++;
				}

				if (b + 1 == 18) {
					inValue = totalPar - outValue;
					Text inValueText = (Text) blackText.clone();
					inValueText.setText(Integer.toString(inValue));
					inValueText.setBold();
					inValueText.setFontFace(Text.FONT_FACE_VERDANA);
					myTable.add(inText, column, 2);
					myTable.setWidth(column, 2, "30");
					myTable.add(inValueText, column, 3);
					column++;
				}

			}

			Text totalParText = (Text) blackText.clone();
			totalParText.setText(Integer.toString(totalPar));
			totalParText.setBold();
			totalParText.setFontFace(Text.FONT_FACE_VERDANA);
			myTable.setWidth(column, 2, "40");
			myTable.add(totalText, column, 2);
			myTable.add(totalParText, column, 3);
			myTable.add(holeText, 1, 2);
			myTable.add(parText, 1, 3);

			myTable.setHeight(2, "20");
			myTable.setHeight(3, "20");

			myTable.mergeCells(1, 1, myTable.getColumns(), 1);

			Text tournamentText = new Text("&nbsp;&nbsp;" + tournament.getName());
			tournamentText.setBold();
			tournamentText.setFontSize(Text.FONT_SIZE_10_HTML_2);
			tournamentText.setFontFace(Text.FONT_FACE_VERDANA);
			tournamentText.setFontColor("FFFFFF");

			Text groupAndRound = new Text(((TournamentGroupHome) IDOLookup.getHomeLegacy(TournamentGroup.class)).findByPrimaryKey(this.tournamentGroupId_).getName() + " - " + iwrb.getLocalizedString("tournament.round", "Round") + " " + ((TournamentRoundHome) IDOLookup.getHomeLegacy(TournamentRound.class)).findByPrimaryKey(this.tournamentRound_).getRoundNumber() + "&nbsp;&nbsp;");
			groupAndRound.setBold();
			groupAndRound.setFontSize(Text.FONT_SIZE_10_HTML_2);
			groupAndRound.setFontFace(Text.FONT_FACE_VERDANA);
			groupAndRound.setFontColor("FFFFFF");

			Table headerTable = new Table(2, 1);
			headerTable.setWidth("100%");
			headerTable.setCellpadding(0);
			headerTable.setCellspacing(0);
			headerTable.setAlignment(2, 1, "right");

			headerTable.add(tournamentText, 1, 1);
			headerTable.add(groupAndRound, 2, 1);

			myTable.add(headerTable, 1, 1);
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	private void setMemberVectorPastRounds() throws SQLException, FinderException {
		TournamentRound[] rounds = tournament.getTournamentRounds();
		TournamentRound tRound = ((TournamentRoundHome) IDOLookup.getHomeLegacy(TournamentRound.class)).findByPrimaryKey(tournamentRound_);
		Vector ids = new Vector();
		for (int i = 0; i < rounds.length; i++) {
			if (rounds[i].getRoundNumber() < tRound.getRoundNumber()) {
				ids.add(new Integer(rounds[i].getID()));
			}
		}

		if (ids.size() > 0) {
			pastTournamentRounds_ = new int[ids.size()];
			for (int i = 0; i < ids.size(); i++) {
				pastTournamentRounds_[i] = ((Integer) ids.get(i)).intValue();
			}
		}
	}

	private void setMemberVectorAllRounds() throws SQLException {
		TournamentRound[] allRounds = tournament.getTournamentRounds();
		allTournamentRounds_ = new int[allRounds.length];
		for (int i = 0; i < allRounds.length; i++) {
			allTournamentRounds_[i] = allRounds[i].getID();
		}

	}

	private void getMemberVector() {
		try {
			int[] tournamentRounds_ = new int[1];
			tournamentRounds_[0] = tournamentRound_;

			ResultDataHandler handler = new ResultDataHandler(tournamentId_, ResultComparator.TOTALSTROKES, tournamentGroupId_, this.allTournamentRounds_, null);
			result = handler.getTournamentMembers();

		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	private String getColor(int score, int par) {
		if (score < par) {
			return "FF0000";
		}
		else if (score == par) {
			return "000000";
		}
		else {
			return "0000FF";
		}
	}

	private String getBackgroundColor(int score, int par) {
		String color = null;
		int birdie = score - par;

		if (birdie >= 2) {
			color = "#777D1A";
		}
		else if (birdie == 1) {
			color = "#04463C";
		}
		else if (birdie == -1) {
			color = "#BB2322";
		}
		else if (birdie == -2) {
			color = "#2050A8";
		}

		return color;
	}

	private void sortMemberVector() {
		ResultComparator comparator = new ResultComparator(ResultComparator.TOTALSTROKES);
		Collections.sort(result, comparator);
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

}