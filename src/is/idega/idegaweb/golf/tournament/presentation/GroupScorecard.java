/*
 * Created on 5.3.2004 To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package is.idega.idegaweb.golf.tournament.presentation;

import is.idega.idegaweb.golf.entity.Field;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberHome;
import is.idega.idegaweb.golf.entity.Scorecard;
import is.idega.idegaweb.golf.entity.Stroke;
import is.idega.idegaweb.golf.entity.Tee;
import is.idega.idegaweb.golf.entity.TeeHome;
import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.entity.TournamentRound;
import is.idega.idegaweb.golf.entity.TournamentRoundHome;
import is.idega.idegaweb.golf.handicap.business.Handicap;
import is.idega.idegaweb.golf.presentation.GolfBlock;
import is.idega.idegaweb.golf.tournament.business.TournamentController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Vector;

import javax.ejb.FinderException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.transaction.IdegaTransactionManager;
import com.idega.util.IWTimestamp;

/**
 * @author gimmi To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Generation - Code and Comments
 */
public class GroupScorecard extends GolfBlock {

	IWResourceBundle iwrb;

	public void main(IWContext modinfo) throws Exception {
		iwrb = getResourceBundle();
		String[] members = modinfo.getParameterValues("members");
		String tournament_round_id = modinfo.getParameter("tournament_round_id");
		String mode = modinfo.getParameter("mode");
		if (mode == null)
			mode = "insert";

		if (members != null) {
			if (members.length > 0) {
				main(modinfo, members, tournament_round_id, mode);
			}
		}
		else {
			close(modinfo);
		}
	}

	public void main(IWContext modinfo, String[] members, String tournament_round_id, String mode) throws IOException, SQLException, FinderException {

		if (mode.equalsIgnoreCase("insert")) {

			Form myForm = new Form();
			maintainParentReloadURL(modinfo,myForm);
			myForm.add(new HiddenInput("tournament_round_id", tournament_round_id));
			myForm.add(new HiddenInput("mode", "save"));

			Table myTable = new Table();
			myTable.setAlignment("center");

			Text holeText = new Text("");
			holeText.setFontSize(1);
			holeText.setBold();

			Tournament tournament = null;
			try {
				tournament = ((TournamentRoundHome) IDOLookup.getHomeLegacy(TournamentRound.class)).findByPrimaryKey(Integer.parseInt(tournament_round_id)).getTournament();
			}
			catch (FinderException fe) {
				throw new SQLException(fe.getMessage());
			}
			int holeNumber = tournament.getNumberOfHoles();

			for (int a = 0; a < holeNumber; a++) {
				Text holeTextTemp = (Text) holeText.clone();
				holeTextTemp.setText((a + 1) + "");
				myTable.add(holeTextTemp, a + 2, 1);
			}

			Text time = new Text(iwrb.getLocalizedString("tournament.", "Time"));
			time.setFontSize(1);

			myTable.add(time, holeNumber + 2, 1);

			TournamentRound round = null;
			try {
				round = ((TournamentRoundHome) IDOLookup.getHomeLegacy(TournamentRound.class)).findByPrimaryKey(Integer.parseInt(tournament_round_id));
			}
			catch (FinderException fe) {
				throw new SQLException(fe.getMessage());
			}
			StringBuffer inString = new StringBuffer();
			for (int i = 0; i < members.length; i++) {
				inString.append(members[i]);
				if (i != members.length - 1)
					inString.append(",");
			}

			Scorecard[] scorecard = (Scorecard[]) ((Scorecard) IDOLookup.instanciateEntity(Scorecard.class)).findAll("select * from scorecard where tournament_round_id = " + tournament_round_id + " and member_id in (" + inString.toString() + ")");
			Hashtable membersHash = new Hashtable(scorecard.length);
			Hashtable scorecardHash = new Hashtable(scorecard.length);
			inString = new StringBuffer();
			for (int i = 0; i < scorecard.length; i++) {
				inString.append(scorecard[i].getID());
				if (i != scorecard.length - 1)
					inString.append(",");
				membersHash.put(Integer.toString(scorecard[i].getMemberId()), scorecard[i]);
			}

			Stroke[] stroke = (Stroke[]) ((Stroke) IDOLookup.instanciateEntity(Stroke.class)).findAll("select * from stroke s,tee t where t.tee_id = s.tee_id and scorecard_id in (" + inString.toString() + ") order by scorecard_id, hole_number");
			for (int i = 0; i < stroke.length; i++) {
				Tee tee = ((TeeHome) IDOLookup.getHomeLegacy(Tee.class)).findByPrimaryKey(stroke[i].getTeeID());
				Vector v = (Vector) scorecardHash.get(Integer.toString(stroke[i].getScorecardID()));
				if (v == null) {
					v = new Vector(holeNumber);
				}
				if (v.size() < tee.getHoleNumber())
					v.setSize(tee.getHoleNumber());
				v.add(tee.getHoleNumber() - 1, stroke[i]);
				scorecardHash.put(Integer.toString(stroke[i].getScorecardID()), v);
			}

			for (int a = 0; a < members.length; a++) {
				myForm.add(new HiddenInput("members", members[a]));
				Member member = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(Integer.parseInt(members[a]));
				Text memberText = new Text(member.getName() + ": ");
				memberText.setFontSize(1);

				myTable.add(memberText, 1, a + 2);

				Scorecard sc = (Scorecard) membersHash.get(members[a]);
				Vector strokes = (Vector) scorecardHash.get(Integer.toString(sc.getID()));
				int vecSize = 0;
				if (strokes != null)
					vecSize = strokes.size();

				for (int b = 0; b < holeNumber; b++) {
					TextInput score = new TextInput(members[a] + "_" + (b + 1));
					if (vecSize > b) {
						if (strokes.elementAt(b) != null) {
							if (((Stroke) (strokes.elementAt(b))).getStrokeCount() > 0) {
								score.setValue(((Stroke) (strokes.elementAt(b))).getStrokeCount());
							}
							else {
								score.setValue("X");
							}
						}
					}
					score.setMaxlength(2);
					score.setLength(2);
					score.setStyleAttribute("font-family: Verdana; font-size: 8pt; border: 1 solid #000000");

					myTable.add(score, b + 2, a + 2);
				}

				IWTimestamp stampur = new IWTimestamp(round.getRoundDate());

				if (sc.getScorecardDate() != null) {
					stampur = new IWTimestamp(sc.getScorecardDate());
				}

				TextInput hour = new TextInput(members[a] + "_hour");
				hour.setMaxlength(2);
				hour.setLength(2);
				hour.setStyleAttribute("font-family: Verdana; font-size: 8pt; border: 1 solid #000000");
				hour.setValue(stampur.getHour());

				TextInput minute = new TextInput(members[a] + "_minute");
				minute.setMaxlength(2);
				minute.setLength(2);
				minute.setStyleAttribute("font-family: Verdana; font-size: 8pt; border: 1 solid #000000");
				minute.setValue(stampur.getMinute());

				Text dot = new Text(": ");
				dot.setFontSize(1);

				myTable.add(hour, holeNumber + 2, a + 2);
				myTable.add(dot, holeNumber + 2, a + 2);
				myTable.add(minute, holeNumber + 2, a + 2);

			}

			int column = myTable.getColumns();
			myTable.setRowAlignment(1, "center");
			myTable.setColumnAlignment(1, "right");
			myTable.setColumnAlignment(holeNumber + 2, "center");
			int rows = myTable.getRows();

			GenericButton button = getButton(new SubmitButton(localize("tournament.calculate","Calculate")));
			button.setOnClick("this.form.submit();this.disabled = true;");
			myTable.add(button, holeNumber + 2, rows + 1);

			myForm.addBreak();
			myForm.add(myTable);
			add(myForm);
		}

		else if (mode.equalsIgnoreCase("save")) {
			TournamentRound round = ((TournamentRoundHome) IDOLookup.getHomeLegacy(TournamentRound.class)).findByPrimaryKey(Integer.parseInt(tournament_round_id));
			Tournament tournament = round.getTournament();
			Field field = tournament.getField();
			int holeNumber = tournament.getNumberOfHoles();

			Form myForm = new Form();
			maintainParentReloadURL(modinfo,myForm);

			Table myTable = new Table();
			myTable.setAlignment("center");

			StringBuffer inString = new StringBuffer();
			for (int i = 0; i < members.length; i++) {
				inString.append(members[i]);
				if (i != members.length - 1)
					inString.append(",");
			}
			Scorecard[] scorecard = (Scorecard[]) ((Scorecard) IDOLookup.instanciateEntity(Scorecard.class)).findAll("select * from scorecard where tournament_round_id = " + tournament_round_id + " and member_id in (" + inString.toString() + ")");
			Hashtable membersHash = new Hashtable(scorecard.length);
			Hashtable scorecardHash = new Hashtable(scorecard.length);
			Hashtable scorecardTeeHash = new Hashtable(scorecard.length);
			inString = new StringBuffer();
			for (int i = 0; i < scorecard.length; i++) {
				inString.append(scorecard[i].getID());
				if (i != scorecard.length - 1)
					inString.append(",");
				membersHash.put(Integer.toString(scorecard[i].getMemberId()), scorecard[i]);
			}

			Tee[] tee = (Tee[]) ((Tee) IDOLookup.instanciateEntity(Tee.class)).findAll("select * from tee where field_id = " + String.valueOf(field.getID()) + " order by tee_color_id, hole_number");
			Hashtable teesHash = new Hashtable();
			Hashtable allTeesHash = new Hashtable();
			for (int i = 0; i < tee.length; i++) {
				Vector v = (Vector) teesHash.get(Integer.toString(tee[i].getTeeColorID()));
				if (v == null) {
					v = new Vector();
				}
				v.add(tee[i]);
				teesHash.put(Integer.toString(tee[i].getTeeColorID()), v);
				allTeesHash.put(Integer.toString(tee[i].getID()), tee[i]);
			}

			Stroke[] stroke = (Stroke[]) ((Stroke) IDOLookup.instanciateEntity(Stroke.class)).findAll("select * from stroke where scorecard_id in (" + inString.toString() + ") order by scorecard_id, tee_id");
			for (int i = 0; i < stroke.length; i++) {
				Vector v = (Vector) scorecardHash.get(Integer.toString(stroke[i].getScorecardID()));
				if (v == null) {
					v = new Vector();
					//            v.setSize(holeNumber);
				}
				Tee tmp = (Tee) allTeesHash.get(Integer.toString(stroke[i].getTeeID()));
				if (tmp != null) {
					if (tmp.getHoleNumber() > v.size())
						v.setSize(tmp.getHoleNumber());
					v.setElementAt(stroke[i], tmp.getHoleNumber() - 1);
				}
				scorecardHash.put(Integer.toString(stroke[i].getScorecardID()), v);
			}

			
			for (int a = 0; a < members.length; a++) {
				Scorecard sc = (Scorecard) membersHash.get(members[a]);
				Vector strokes = (Vector) scorecardHash.get(Integer.toString(sc.getID()));
				Vector tees = (Vector) teesHash.get(Integer.toString(sc.getTeeColorID()));

				Member member = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(Integer.parseInt(members[a]));
				Handicap handicap = new Handicap((double) sc.getHandicapBefore());
				int playHandicap = handicap.getLeikHandicap((double) sc.getSlope(), (double) sc.getCourseRating(), (double) field.getFieldPar());

				TransactionManager trans = IdegaTransactionManager.getInstance();
				try {
					trans.begin();
					if (strokes == null) {
						strokes = new Vector();
						scorecardHash.put(Integer.toString(sc.getID()), strokes);
					}
	
					String hour = modinfo.getParameter(members[a] + "_hour");
					String minute = modinfo.getParameter(members[a] + "_minute");
	
					IWTimestamp stampur = new IWTimestamp(round.getRoundDate());
	
					for (int b = 0; b < tees.size(); b++) {
						Tee currentTee = (Tee) tees.elementAt(b);
	
						if (b == 0) {
							if (hour != null && minute != null) {
								stampur.setHour(Integer.parseInt(hour));
								stampur.setMinute(Integer.parseInt(minute));
								sc.setScorecardDate(stampur.getTimestamp());
							}
							sc.setSlope(currentTee.getSlope());
							sc.setCourseRating(currentTee.getCourseRating());
							sc.setFieldID(field.getID());
							sc.update();
						}
	
						Stroke currentStroke = null;
						if (strokes.size() > b)
							currentStroke = (Stroke) strokes.elementAt(currentTee.getHoleNumber() - 1);
						String stroke_count = modinfo.getParameter(members[a] + "_" + (b + 1));
						if (stroke_count == null || stroke_count.equals("")) {
							stroke_count = "";
						}
						if (stroke_count.equalsIgnoreCase("x")) {
							stroke_count = "0";
						}
	
						if (stroke_count != "") {
							int stroke_new = Integer.parseInt(stroke_count);
							if (stroke_new == 0) {
								stroke_new = currentTee.getPar() + 5;
							}
							if (currentStroke != null) {
								currentStroke.setHoleHandicap((int) currentTee.getHandicap());
								currentStroke.setHolePar(currentTee.getPar());
								currentStroke.setTeeID(currentTee.getID());
								currentStroke.setStrokeCount(stroke_new);
								currentStroke.setPointCount(0);
								currentStroke.update();
							}
							else {
								Stroke newStrokes = (Stroke) IDOLookup.createLegacy(Stroke.class);
								newStrokes.setScorecardID(sc.getID());
								newStrokes.setHoleHandicap((int) tee[b].getHandicap());
								newStrokes.setHolePar(tee[b].getPar());
								newStrokes.setTeeID(tee[b].getID());
								newStrokes.setStrokeCount(stroke_new);
								newStrokes.setPointCount(0);
								newStrokes.insert();
								if (currentTee.getHoleNumber() > strokes.size())
									strokes.setSize(currentTee.getHoleNumber());
								strokes.setElementAt(newStrokes, currentTee.getHoleNumber() - 1);
							}
						}
						else {
							if (currentStroke != null) {
								currentStroke.delete();
							}
						}
					}
					int heildarpunktar = Handicap.calculatePoints(sc, strokes, playHandicap);
					trans.commit();
				}
				catch (Exception e) {
					try {
						trans.rollback();
					}
					catch (SystemException se) {
						System.err.println(se);
					}
				}
				//System.out.println(strokes.size());


				Table scoreTable = new Table();
				scoreTable.setAlignment("center");
				scoreTable.setWidth(1, "200");
				scoreTable.setAlignment(1, 2, "right");
				scoreTable.setAlignment(1, 3, "right");

				int inScore = 0;
				int outScore = 0;
				int inPoints = 0;
				int outPoints = 0;
				int totalScore = 0;
				int totalPoints = 0;
				int b = 1;

				Text memberName = new Text(member.getName() + ",&nbsp;" + iwrb.getLocalizedString("tournament.handicap_short", "Hcp.") + "&nbsp;" + playHandicap + "&nbsp;:");
				memberName.setFontSize(1);
				memberName.setBold();
				memberName.setFontFace("Verdana,Arial,sans serif");

				Text strokeText = new Text(iwrb.getLocalizedString("tournament.strokes", "Strokes") + ":");
				strokeText.setFontSize(1);
				strokeText.setBold();

				Text pointText = new Text(iwrb.getLocalizedString("tournament.points", "Points") + ":");
				pointText.setFontSize(1);
				pointText.setBold();

				Text outText = new Text("<u>" + iwrb.getLocalizedString("tournament.out", "Out") + "</u>");
				outText.setFontSize(1);
				outText.setBold();
				outText.setFontFace("Verdana,Arial,sans serif");

				Text inText = new Text("<u>" + iwrb.getLocalizedString("tournament.in", "In") + "</u>");
				inText.setFontSize(1);
				inText.setBold();
				inText.setFontFace("Verdana,Arial,sans serif");

				Text totalText = new Text("<u>" + iwrb.getLocalizedString("tournament.total", "Total") + "</u>");
				totalText.setFontSize(1);
				totalText.setBold();
				totalText.setFontFace("Verdana,Arial,sans serif");

				Text totalNettoText = new Text("<u>" + iwrb.getLocalizedString("tournament.net", "Net") + "</u>");
				totalNettoText.setFontSize(1);
				totalNettoText.setBold();
				totalNettoText.setFontFace("Verdana,Arial,sans serif");

				scoreTable.add(memberName, 1, 1);
				scoreTable.add(strokeText, 1, 2);
				scoreTable.add(pointText, 1, 3);

				int holeCount = 0;

				for (int c = 0; c < holeNumber; c++) {
					Stroke stroke2 = null;
					if (strokes.size() > c) {
						stroke2 = (Stroke) strokes.elementAt(c);
					}

					b++;
					int strokeCount = 0;
					int pointCount = 0;
					if (stroke2 != null) {
						strokeCount = stroke2.getStrokeCount();
						pointCount = stroke2.getPointCount();
						holeCount++;
					}

					Text holeNumber2 = new Text("<u>" + (c + 1) + "</u>");
					holeNumber2.setFontSize(1);
					holeNumber2.setBold();
					Text holeScore = new Text(strokeCount + "");
					holeScore.setFontSize(1);
					Text holePoint = new Text(pointCount + "");
					holePoint.setFontSize(1);

					scoreTable.add(holeNumber2, b, 1);
					if (strokeCount > 0)
						scoreTable.add(holeScore, b, 2);
					if (pointCount > 0)
						scoreTable.add(holePoint, b, 3);
					scoreTable.setColumnAlignment(b, "center");
					scoreTable.setWidth(b, "20");

					if (c < 9) {
						outScore += strokeCount;
						outPoints += pointCount;
					}
					else {
						inScore += strokeCount;
						inPoints += pointCount;
					}

					if (c + 1 == 9) {
						b++;

						Text outScoreText = new Text(outScore + "");
						outScoreText.setFontSize(1);
						outScoreText.setBold();
						outScoreText.setFontFace("Verdana,Arial,sans serif");
						Text outPointsText = new Text(outPoints + "");
						outPointsText.setFontSize(1);
						outPointsText.setBold();
						outPointsText.setFontFace("Verdana,Arial,sans serif");

						scoreTable.add(outText, b, 1);
						scoreTable.add(outScoreText, b, 2);
						scoreTable.add(outPointsText, b, 3);
						scoreTable.setColumnAlignment(b, "center");
						scoreTable.setWidth(b, "30");

					}

					if (c + 1 == 18) {
						b++;

						Text inScoreText = new Text(inScore + "");
						inScoreText.setFontSize(1);
						inScoreText.setBold();
						inScoreText.setFontFace("Verdana,Arial,sans serif");
						Text inPointsText = new Text(inPoints + "");
						inPointsText.setFontSize(1);
						inPointsText.setBold();
						inPointsText.setFontFace("Verdana,Arial,sans serif");

						scoreTable.add(inText, b, 1);
						scoreTable.add(inScoreText, b, 2);
						scoreTable.add(inPointsText, b, 3);
						scoreTable.setColumnAlignment(b, "center");
						scoreTable.setWidth(b, "30");

					}

					totalScore += strokeCount;
					totalPoints += pointCount;

				}

				b++;

				Text totalScoreText = new Text(totalScore + "");
				totalScoreText.setFontSize(1);
				totalScoreText.setBold();
				totalScoreText.setFontFace("Verdana,Arial,sans serif");
				Text totalPointsText = new Text(totalPoints + "");
				totalPointsText.setFontSize(1);
				totalPointsText.setBold();
				totalPointsText.setFontFace("Verdana,Arial,sans serif");
				Text totalNettoScore = new Text((totalScore - playHandicap) + "");
				totalNettoScore.setFontSize(1);
				totalNettoScore.setBold();
				totalNettoScore.setFontFace("Verdana,Arial,sans serif");
				Text totalOverAllScore = new Text();
				totalOverAllScore.setFontSize(1);
				totalOverAllScore.setBold();
				totalOverAllScore.setFontFace("Verdana,Arial,sans serif");

				if (tournament.getNumberOfRounds() > 1) {
					try {
						StringBuffer sql = new StringBuffer();
						sql.append("select sum(stroke_count) from stroke st, scorecard s, tournament_round tr, tournament t");
						sql.append(" where st.scorecard_id = s.scorecard_id");
						sql.append(" and s.tournament_round_id = tr.tournament_round_id");
						sql.append(" and tr.tournament_id = t.tournament_id");
						sql.append(" and t.tournament_id = " + Integer.toString(tournament.getID()));
						sql.append(" and tr.round_number <= " + Integer.toString(round.getRoundNumber()));
						sql.append(" and s.member_id = " + Integer.toString(member.getID()));

						String[] overAllScore = com.idega.data.SimpleQuerier.executeStringQuery(sql.toString());
						if (overAllScore != null) {
							totalOverAllScore.setText(overAllScore[0]);
						}
					}
					catch (Exception e) {
						e.printStackTrace(System.err);
					}
				}

				scoreTable.add(totalText, b, 1);
				scoreTable.add(totalScoreText, b, 2);
				if (holeCount >= 18) {
					scoreTable.add(totalNettoText, b + 1, 1);
					scoreTable.add(totalNettoScore, b + 1, 2);
					if (tournament.getNumberOfRounds() > 1) {
						scoreTable.add(totalText, b + 2, 1);
						scoreTable.add(totalOverAllScore, b + 2, 2);
						scoreTable.setWidth(b + 2, "45");
						scoreTable.setColumnAlignment(b + 2, "center");
					}
				}
				scoreTable.add(totalPointsText, b, 3);
				scoreTable.setColumnAlignment(b, "center");
				scoreTable.setColumnAlignment(b + 1, "center");
				scoreTable.setWidth(b, "40");

				myTable.add(scoreTable, 1, a + 1);

			}

			int rows = myTable.getRows();
			myTable.setAlignment(1, rows, "right");
			myTable.add(getButton(TournamentController.getBackLink(modinfo)), 1, rows);
			myTable.addText(" ", 1, rows);
			myTable.add(getButton(new SubmitButton(localize("tournament.save","Save"))), 1, rows);
			myForm.add(myTable);

			add("<br>");
			add(myForm);
		}

	}

	public void close(IWContext iwc) {
		getParentPage().setParentToReloadWithURL(getParentReloadURL(iwc));
		getParentPage().close();
	}
	
	
	
}