package is.idega.idegaweb.golf.tournament.presentation;

/**
 * Title: TournamentResults Description: Displayes the results of a tournament
 * Copyright: Copyright (c) 2001 Company: idega co.
 * 
 * @author Laddi
 * @version 1.3
 */

import is.idega.idegaweb.golf.entity.Dismissal;
import is.idega.idegaweb.golf.entity.DismissalHome;
import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.entity.TournamentHome;
import is.idega.idegaweb.golf.presentation.GolfBlock;
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
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;

public class TournamentResults extends GolfBlock {

	private final static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.golf";

	public static final int TOTALSTROKES = 1;
	public static final int TOTALSTROKESWITHHANDICAP = 2;
	public static final int TOTALPOINTS = 3;
	public static final int NAME = 4;
	public static final int ABBREVATION = 5;
	public static final int TOURNAMENTROUND = 6;

	protected IWResourceBundle iwrb;
	protected IWBundle iwb;

	private int tournamentId_ = -1;
	private int tournamentGroupId_ = -1;
	private int tournamentType_ = -1;
	private int[] tournamentRounds_ = null;

	private String gender_ = null;

	private int sortBy = -1;

	private Vector result = null;

	private Table myTable = null;

	private Tournament tournament = null;

	private int numberOfRounds = -1;
	private int numberOfColumns = -1;

	public void sortBy(int toSortBy) {
		sortBy = toSortBy;
	}

	public TournamentResults(int tournamentId, int tournamentType) {
		tournamentId_ = tournamentId;
		tournamentType_ = tournamentType;
	}

	public TournamentResults(int tournamentId, int tournamentType, int tournamentGroupId) {
		tournamentId_ = tournamentId;
		tournamentType_ = tournamentType;
		tournamentGroupId_ = tournamentGroupId;
	}

	public TournamentResults(int tournamentId, int tournamentType, String gender) {
		tournamentId_ = tournamentId;
		tournamentType_ = tournamentType;
		gender_ = gender;
	}

	public TournamentResults(int tournamentId, int tournamentType, int[] tournamentRounds) {
		tournamentId_ = tournamentId;
		tournamentType_ = tournamentType;
		tournamentRounds_ = tournamentRounds;
	}

	public TournamentResults(int tournamentId, int tournamentType, int tournamentGroupId, String gender) {
		tournamentId_ = tournamentId;
		tournamentType_ = tournamentType;
		tournamentGroupId_ = tournamentGroupId;
		gender_ = gender;
	}

	public TournamentResults(int tournamentId, int tournamentType, int tournamentGroupId, int[] tournamentRounds) {
		tournamentId_ = tournamentId;
		tournamentType_ = tournamentType;
		tournamentGroupId_ = tournamentGroupId;
		tournamentRounds_ = tournamentRounds;
	}

	public TournamentResults(int tournamentId, int tournamentType, int[] tournamentRounds, String gender) {
		tournamentId_ = tournamentId;
		tournamentType_ = tournamentType;
		tournamentRounds_ = tournamentRounds;
		gender_ = gender;
	}

	public TournamentResults(int tournamentId, int tournamentType, int tournamentGroupId, int[] tournamentRounds, String gender) {
		tournamentId_ = tournamentId;
		tournamentType_ = tournamentType;
		tournamentGroupId_ = tournamentGroupId;
		tournamentRounds_ = tournamentRounds;
		gender_ = gender;
	}

	public void main(IWContext modinfo) throws SQLException {
		try {
			iwrb = getResourceBundle(modinfo);
			iwb = getBundle(modinfo);
			try {
				tournament = ((TournamentHome) IDOLookup.getHomeLegacy(Tournament.class)).findByPrimaryKey(tournamentId_);
			}
			catch (FinderException fe) {
				throw new SQLException(fe.getMessage());
			}
			numberOfRounds = tournament.getNumberOfRounds();
			getMemberVector();
			if (result != null) {
				if (result.size() > 1) {
					sortMemberVector();
				}
			}
			drawResultTable();
			getResults();
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

	private void getMemberVector() {
		try {
			ResultDataHandler handler = new ResultDataHandler(tournamentId_, tournamentType_, tournamentGroupId_, tournamentRounds_, gender_);
			result = handler.getTournamentMembers();
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	private void sortMemberVector() {
		ResultComparator comparator = new ResultComparator(sortBy);
		Collections.sort(result, comparator);
	}

	private void drawResultTable() {
		try {
			myTable = new Table();
			myTable.setCellpadding(0);
			myTable.setCellspacing(0);
			myTable.setWidth("100%");
			myTable.setBorder(0);

			String[] headers = { iwrb.getLocalizedString("tournament.position", "Position"), iwrb.getLocalizedString("tournament.golfer", "Member"), iwrb.getLocalizedString("tournament.club", "Club"), iwrb.getLocalizedString("tournament.handicap_short", "Hcp.")};
			for (int a = 0; a < headers.length; a++) {
				if (a == 0 && (this.sortBy == ResultComparator.NAME || this.sortBy == ResultComparator.ABBREVATION)) {
					addHeaders("", a + 1, 1);
				}
				else {
					addHeaders(headers[a], a + 1, 1);
				}
				myTable.mergeCells(a + 1, 1, a + 1, 2);
			}

			getTotalHeaders();

			add(myTable);
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	private void getResults() {
		try {
			int size = 0;
			if (result != null) size = result.size();
			int row = 3;
			int zebraRow = 1;

			for (int a = 0; a < size; a++) {
				ResultsCollector collector = (ResultsCollector) result.elementAt(a);
				if (size <= 1) {
					collector.calculateCompareInfo();
				}

				int handicap = collector.getHandicap();
				int finalScore = collector.getTotalScore();
				int difference = collector.getDifference();
				String hole = collector.getHole();
				int memberID = collector.getMemberId();
				
				Link nameLink = getSmallLink(collector.getName());
				nameLink.setWindowToOpen(HandicapScore.class);
				nameLink.addParameter("member_id", collector.getMemberId());
				nameLink.addParameter("tournament_id", tournamentId_);
				nameLink.addParameter("tournament_group_id", collector.getTournamentGroupId());

				Text positionText = getSmallText(Integer.toString(a + 1));
				Text clubText = getSmallText(collector.getAbbrevation());
				Text handicapText = getSmallText(Integer.toString(handicap));
				Text holeText = getSmallText(hole);
				Text firstNineText = getSmallText("");
				Text lastNineText = getSmallText("");
				Text totalRoundScore = getSmallText("");
				Text diffText = getSmallText("");
				Text bruttoText = getSmallText("");

				int totalScore = 0;
				int totalDifference = 0;
				int totalBrutto = 0;
				int roundNumber = 0;

				Vector roundScore = collector.getRoundScore();

				if (hole.equalsIgnoreCase("f")) {
					int lastNine = (int) collector.getRealLastNine();
					if (roundScore != null) {
						totalScore = ((Integer) roundScore.elementAt(roundScore.size() - 1)).intValue();
						totalDifference = totalScore - collector.getFieldPar();
						totalBrutto = totalScore + handicap;
					}
					int firstNine = totalScore - lastNine;
					if (this.tournamentType_ == ResultComparator.TOTALSTROKESWITHHANDICAP) {
						firstNine = totalBrutto - lastNine;
					}

					lastNineText.setText(Integer.toString(lastNine));
					firstNineText.setText(Integer.toString(firstNine));
					totalRoundScore.setText(Integer.toString(totalScore));
					diffText.setText(Integer.toString(totalDifference));
					bruttoText.setText(Integer.toString(totalBrutto));
				}

				try {
					switch (tournamentType_) {
						case ResultComparator.TOTALSTROKES:
							int roundScoreColumn = 10;
							for (int b = 1; b <= numberOfRounds; b++) {
								int roundScore2 = 0;
								int roundIncNumber = collector.getRound(b);
								int position = roundScoreColumn + roundIncNumber - 1;

								if (roundIncNumber != -1) {
									roundScore2 = collector.getRoundScore(collector.getRoundNumber(b));
								}

								Text roundScoreText = getSmallText(Integer.toString(roundScore2));

								if (roundScore2 > 0) {
									myTable.add(roundScoreText, position, row);
								}
							}
							Text finalScoreText = getSmallBoldText(Integer.toString(finalScore));
							Text finalDifferenceText = getSmallBoldText(Integer.toString(difference));
							if (finalScore > 0) {
								myTable.add(finalScoreText, numberOfColumns - 1, row);
								myTable.add(finalDifferenceText, numberOfColumns, row);
							}
							myTable.add(totalRoundScore, 8, row);
							myTable.add(diffText, 9, row);
							break;

						case ResultComparator.TOTALSTROKESWITHHANDICAP:
							int roundScoreColumn2 = 10;
							for (int b = 1; b <= numberOfRounds; b++) {
								int roundScore2 = 0;
								int roundScoreBrutto = 0;
								int roundIncNumber = collector.getRound(b);
								int position2 = roundScoreColumn2 + (roundIncNumber * 2 - 1) - 1;

								if (roundIncNumber != -1) {
									roundScore2 = collector.getRoundScore(collector.getRoundNumber(b));
									roundScoreBrutto = roundScore2 + handicap;
								}

								Text roundScoreText = getSmallText(Integer.toString(roundScore2));
								Text roundScoreBruttoText = getSmallText(Integer.toString(roundScoreBrutto));

								if (roundScore2 > 0) {
									myTable.add(roundScoreBruttoText, position2, row);
									myTable.add(roundScoreText, position2 + 1, row);
								}
							}

							Text finalBruttoText = getSmallBoldText(Integer.toString(collector.getTotalStrokes()));
							Text finalScoreText2 = getSmallBoldText(Integer.toString(finalScore));
							if (finalScore > 0) {
								myTable.add(finalBruttoText, numberOfColumns - 1, row);
								myTable.add(finalScoreText2, numberOfColumns, row);
							}
							myTable.add(bruttoText, 8, row);
							myTable.add(totalRoundScore, 9, row);
							break;

						case ResultComparator.TOTALPOINTS:
							int roundScoreColumn3 = 9;
							for (int b = 1; b <= numberOfRounds; b++) {
								int roundScore2 = 0;
								int roundIncNumber = collector.getRound(b);
								int position = roundScoreColumn3 + roundIncNumber - 1;

								if (roundIncNumber != -1) {
									roundScore2 = collector.getRoundScore(collector.getRoundNumber(b));
								}

								Text roundScoreText = getSmallText(Integer.toString(roundScore2));
								if (roundScore2 > 0) {
									myTable.add(roundScoreText, position, row);
								}
							}
							Text finalScoreText3 = getSmallBoldText(Integer.toString(finalScore));
							if (finalScore > 0) myTable.add(finalScoreText3, numberOfColumns, row);
							myTable.add(totalRoundScore, 8, row);
							break;
					}
				}
				catch (Exception e) {
					e.printStackTrace(System.err);
					System.err.println("MemberID: " + memberID);
				}

				myTable.add(positionText, 1, row);
				myTable.add(nameLink, 2, row);
				myTable.add(clubText, 3, row);
				if (finalScore > 0) {
					myTable.add(handicapText, 4, row);
				}
				myTable.add(holeText, 5, row);
				myTable.add(firstNineText, 6, row);
				myTable.add(lastNineText, 7, row);
				myTable.setHeight(row, 10);

				if (collector.getDismissal() > 0) {
					Dismissal dismissal = ((DismissalHome) IDOLookup.getHomeLegacy(Dismissal.class)).findByPrimaryKey(collector.getDismissal());

					Image dismissImage = iwb.getImage("shared/red.gif");
					dismissImage.setHorizontalSpacing(4);
					dismissImage.setAlignment("absmiddle");
					if (dismissal.getName() != null) {
						dismissImage.setName(dismissal.getName());
					}

					myTable.add(dismissImage, 2, row);
				}
				
				myTable.setRowPadding(row, getCellpadding());
				if (zebraRow % 2 != 0) {
					myTable.setRowStyleClass(row++, getLightRowClass());
				}
				else {
					myTable.setRowStyleClass(row++, getDarkRowClass());
				}
				zebraRow++;
			}

			for (int c = 1; c <= numberOfColumns; c++) {
				if (c != 2) {
					myTable.setColumnAlignment(c, "center");
				}
			}
			myTable.setAlignment(2, 1, "center");
			myTable.setRowStyleClass(1, getHeaderRowClass());
			myTable.setRowStyleClass(2, getHeaderRowClass());
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	private void addHeaders(String header, int column, int row) {
		try {
			Text headerText = new Text(header);
			myTable.add(headerText, column, row);
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	private void getTotalHeaders() {
		try {
			String frontNine = iwrb.getLocalizedString("tournament.front_nine", "F9");
			String backNine = iwrb.getLocalizedString("tournament.back_nine", "B9");
			String total = iwrb.getLocalizedString("tournament.total", "Total");
			String netto = iwrb.getLocalizedString("tournament.net", "Net");
			String difference = iwrb.getLocalizedString("tournament.difference", "Difference");

			int firstColumn = 6;
			int column = firstColumn;
			addHeaders(frontNine, column, 2);
			addHeaders(backNine, column + 1, 2);

			String roundShort = iwrb.getLocalizedString("tournament.round_short", "R");
			if (tournament.getNumberOfRounds() >= 4) {
				roundShort = iwrb.getLocalizedString("tournament.day_short", "D");
			}

			String rounds = iwrb.getLocalizedString("tournament.rounds", "Rounds");
			if (tournament.getNumberOfRounds() >= 4) {
				rounds = iwrb.getLocalizedString("tournament.days", "Days");
			}

			switch (tournamentType_) {
				case ResultComparator.TOTALSTROKES:
					addHeaders(total, column + 2, 2);
					addHeaders(difference, column + 3, 2);
					column += 4;
					for (int a = 0; a < numberOfRounds; a++) {
						addHeaders(roundShort + Integer.toString(a + 1), column + a, 2);
					}
					myTable.mergeCells(column, 1, column + numberOfRounds - 1, 1);
					addHeaders(rounds, column, 1);
					addHeaders(total, column + numberOfRounds, 2);
					addHeaders(difference, column + numberOfRounds + 1, 2);
					myTable.mergeCells(column + numberOfRounds, 1, column + numberOfRounds + 1, 1);
					addHeaders(iwrb.getLocalizedString("tournament.total", "Total"), column + numberOfRounds, 1);
					break;

				case ResultComparator.TOTALSTROKESWITHHANDICAP:
					addHeaders(total, column + 2, 2);
					addHeaders(netto, column + 3, 2);
					column += 4;
					int roundColumn = column;
					for (int a = 0; a < numberOfRounds; a++) {
						myTable.mergeCells(roundColumn, 1, roundColumn + 1, 1);
						addHeaders(total, roundColumn, 2);
						addHeaders(netto, roundColumn + 1, 2);
						addHeaders(roundShort + Integer.toString(a + 1), roundColumn, 1);
						roundColumn += 2;
					}
					addHeaders(total, roundColumn, 2);
					addHeaders(netto, roundColumn + 1, 2);
					myTable.mergeCells(roundColumn, 1, roundColumn + 1, 1);
					addHeaders(iwrb.getLocalizedString("tournament.total", "Total"), roundColumn, 1);
					break;

				case ResultComparator.TOTALPOINTS:
					addHeaders(total, column + 2, 2);
					column += 3;
					for (int a = 0; a < numberOfRounds; a++) {
						addHeaders(roundShort + Integer.toString(a + 1), column + a, 2);
					}
					myTable.mergeCells(column, 1, column + numberOfRounds - 1, 1);
					addHeaders(rounds, column, 1);
					myTable.mergeCells(column + numberOfRounds, 1, column + numberOfRounds, 2);
					addHeaders(iwrb.getLocalizedString("tournament.total", "Total"), column + numberOfRounds, 1);
					break;
			}

			myTable.mergeCells(firstColumn - 1, 1, column - 1, 1);
			addHeaders(iwrb.getLocalizedString("tournament.hole", "Hole"), firstColumn - 1, 2);
			addHeaders(iwrb.getLocalizedString("tournament.last_round", "Last round"), firstColumn - 1, 1);

			numberOfColumns = myTable.getColumns();
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

}