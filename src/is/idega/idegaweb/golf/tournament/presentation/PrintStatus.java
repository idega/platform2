package is.idega.idegaweb.golf.tournament.presentation;

import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.entity.TournamentHome;
import is.idega.idegaweb.golf.entity.TournamentRound;
import is.idega.idegaweb.golf.entity.TournamentType;
import is.idega.idegaweb.golf.presentation.GolfBlock;
import is.idega.idegaweb.golf.tournament.business.ResultComparator;
import is.idega.idegaweb.golf.tournament.business.ResultDataHandler;
import is.idega.idegaweb.golf.tournament.business.ResultsCollector;
import is.idega.idegaweb.golf.tournament.business.TournamentSession;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Page;

/**
 * @author gimmi
 */
public class PrintStatus extends GolfBlock {

  public void main(IWContext modinfo) throws SQLException,IOException, FinderException {

	String excel = modinfo.getParameter("xls");
	
    String sTournamentId = modinfo.getParameter("tournament_id");
    int tournament_id = getTournamentSession(modinfo).getTournamentID();
    if(tournament_id==-1) {
    		tournament_id = Integer.parseInt(sTournamentId);
    }
	if (excel == null) {
	  ResultsViewer results = new ResultsViewer(tournament_id);
	  add(results);
    }else {
      String fileSeperator = System.getProperty("file.separator");
      String filepath = modinfo.getServletContext().getRealPath(fileSeperator+"files"+fileSeperator);
      String fileName = "status.xls";

      try{
        String file = filepath+fileName;
        FileWriter out = new FileWriter(file);
        
        char[] c  = null;


		String sGender = modinfo.getParameter("gender");
		String sTournamentGroupId = modinfo.getParameter("tournament_group_id");
		String[] sTournamentRoundIds = modinfo.getParameterValues("tournament_round_id");
		String sOrder = modinfo.getParameter("order");
		String sSort = modinfo.getParameter("sort");

		int iTournamentGroupId = -1;
		int[] iTournamentRoundIds = new int[]{};
		int iOrder = -1;
		int iSort = -1;
		
		if (sTournamentGroupId != null) {
			iTournamentGroupId = Integer.parseInt(sTournamentGroupId);
		}
		if (sTournamentRoundIds != null) {
			iTournamentRoundIds = new int[sTournamentRoundIds.length];
			for (int i = 0; i < sTournamentRoundIds.length; i++) {
				iTournamentRoundIds[i] = Integer.parseInt(sTournamentRoundIds[i]);
			}
		}
		if (sOrder != null) {
			iOrder = Integer.parseInt(sOrder);
		}
		if (sSort != null) {
			iSort = Integer.parseInt(sSort);
		}

        Tournament tournament = ((TournamentHome) IDOLookup.getHomeLegacy(Tournament.class)).findByPrimaryKey(tournament_id);
        TournamentRound[] rounds = tournament.getTournamentRounds();
        int totalRounds = rounds.length;
		int tournamentType = getTournamentType(tournament, iOrder);
		
		ResultDataHandler rdh = new ResultDataHandler(tournament_id, tournamentType, iTournamentGroupId, iTournamentRoundIds, sGender);
//		int numberOfRounds = tournament.getNumberOfRounds();
		
    	ResultComparator comparator = new ResultComparator(tournamentType);
		java.util.Vector members = rdh.getTournamentMembers();
   	 	Collections.sort(members,comparator);
   		String sql = rdh.getSQLString();
		

        StringBuffer data = new StringBuffer();
        data.append(localize("tournament.position","Position")); data.append("\t");
        data.append(localize("tournament.player","Player")); data.append("\t");
        data.append(localize("tournament.club","Club")); data.append("\t");
        data.append(localize("tournament.handicap","Handicap")); data.append("\t");
        
		  switch (tournamentType) {
		    case ResultComparator.TOTALSTROKES :
		        data.append(localize("tournament.latest_rount","Latest round")); data.append("\t");
		        data.append(""); data.append("\t");
		        data.append(""); data.append("\t");
		        data.append(""); data.append("\t");
		        data.append(""); data.append("\t");
		        data.append(localize("trounament.rounds","Rounds")); data.append("\t");
		        if (totalRounds > 1) {
		        	for (int i = 2; i <= totalRounds; i++) {
		        		data.append(""); data.append("\t");
		        	}
		        }
		        data.append(localize("trounament.total","Total"));data.append("\t");
		        data.append(""); data.append("\t");
		        data.append("\n");
		    break;
		
		    case ResultComparator.TOTALSTROKESWITHHANDICAP :
		        data.append(localize("trounament.last_rounds","Last round")); data.append("\t");
		        data.append(""); data.append("\t");
		        data.append(""); data.append("\t");
		        data.append(""); data.append("\t");
		        data.append(""); data.append("\t");
	        	for (int i = 1; i <= totalRounds; i++) {
	        		data.append("H"+i); data.append("\t");
	        		data.append(""); data.append("\t");
	        	}
		        data.append(localize("trounament.total","Total"));data.append("\t");
		        data.append(""); data.append("\t");
		        data.append("\n");
		    break;
		
		    case ResultComparator.TOTALPOINTS :
		        data.append(localize("trounament.last_rounds","Last round")); data.append("\t");
		        data.append(""); data.append("\t");
		        data.append(""); data.append("\t");
		        data.append(""); data.append("\t");
		        data.append(localize("trounament.rounds","Rounds")); data.append("\t");
		        if (totalRounds > 1) {
		        	for (int i = 2; i <= totalRounds; i++) {
		        		data.append(""); data.append("\t");
		        	}
		        }
		        data.append("Samt");data.append("\t");
		        data.append("\n");
		    break;
		  }
        data.append("\n");
        
        data.append(""); data.append("\t");
        data.append(""); data.append("\t");
        data.append(""); data.append("\t");
        data.append(""); data.append("\t");
        data.append(localize("trounament.hole","Hole")); data.append("\t");
        data.append(localize("trounament.F9","F9")); data.append("\t");
        data.append(localize("trounament.S9","S9")); data.append("\t");
        data.append(localize("trounament.total","Total")); data.append("\t");
		  switch (tournamentType) {
		    case ResultComparator.TOTALSTROKES :
	        	data.append(localize("trounament.placement","Placement")); data.append("\t");
		        data.append(localize("trounament.H1","H1")); data.append("\t");
		        if (totalRounds > 1) {
		        	for (int i = 2; i <= totalRounds; i++) {
		        		data.append(localize("trounament.H","H")+i); data.append("\t");
		        	}
			        data.append(localize("trounament.total","Total")); data.append("\t");
		    	    data.append(localize("trounament.placement","Placement")); data.append("\t");
		        }
		        data.append("\n");
		    break;
		
		    case ResultComparator.TOTALSTROKESWITHHANDICAP :
	        	data.append(localize("trounament.netto","Netto")); data.append("\t");
	        	for (int i = 1; i <= totalRounds; i++) {
	        		data.append(localize("trounament.total","Total")); data.append("\t");
	        		data.append(localize("trounament.netto","Netto")); data.append("\t");
	        	}
		        data.append(localize("trounament.total","Total")); data.append("\t");
	    	    data.append(localize("trounament.netto","Netto")); data.append("\t");
		        data.append("\n");
		    break;
		
		    case ResultComparator.TOTALPOINTS :
		        data.append(localize("trounament.H1","H1")); data.append("\t");
		        if (totalRounds > 1) {
		        	for (int i = 2; i <= totalRounds; i++) {
		        		data.append(localize("trounament.H","H")+i); data.append("\t");
		        	}
		        }
		        data.append("\n");
		    break;
		  }

        out.write(data.toString().toCharArray());

        int count = 0;
        String tournName = tournament.getName();

        int handicap = 0;
        int finalScore = 0;
        int difference = 0;
        String hole = "0";
        int memberID = 0;
        
        int iRoundScore = 0;
        int iRoundScoreBrutto = 0;
        int totalScore = 0;
        int totalDifference = 0;
        int totalBrutto = 0;
        int roundNumber = 0;

		int vectorSize = members.size();
		ResultsCollector r;
		for (int i = 0; i < vectorSize; i++) {
			++count;
			r = (ResultsCollector) members.get(i);
        	handicap = r.getHandicap();
        	iRoundScore = 0;
        	iRoundScoreBrutto = 0;
	        finalScore = r.getTotalScore();
	        difference = r.getDifference();
	        hole = r.getHole();
	        memberID = r.getMemberId();
        	totalScore = 0;
        	totalDifference = 0;
        	totalBrutto = 0;
        	roundNumber = 0;
        	
        	int lastNine = 0;
        	int firstNine = 0;

	        Vector roundScore = r.getRoundScore();
	
	        if ( hole.equalsIgnoreCase("f") ) {
	          lastNine = (int) r.getRealLastNine();
	          if ( roundScore != null ) {
	            totalScore = ((Integer)roundScore.elementAt(roundScore.size()-1)).intValue();
	            totalDifference = totalScore - r.getFieldPar();
	            totalBrutto = totalScore + handicap;
	          }
	          firstNine = totalScore - lastNine;
	          if ( tournamentType == ResultComparator.TOTALSTROKESWITHHANDICAP ) {
	            firstNine = totalBrutto - lastNine;
	          }
	        }

			data = new StringBuffer();
			data.append(count).append("\t");
			data.append(r.getName()).append("\t");
			data.append(r.getAbbrevation()).append("\t");
			data.append(r.getHandicap()).append("\t");
			data.append(hole).append("\t");
			data.append(firstNine).append("\t");
			data.append(lastNine).append("\t");
			data.append(firstNine+lastNine).append("\t");
	        
	        try {
	          switch (tournamentType) {
	            case ResultComparator.TOTALSTROKES :
	            	int[] roundScores = new int[totalRounds+1];
	            	int roundScore2 = 0;
	            	totalScore = 0;
	            	
	              for ( int b = 1; b <= totalRounds; b++ ) {
	                roundScore2 = 0;
	                int roundIncNumber = r.getRound(b);

		        	try {
		                roundScores[b] = r.getRoundScore(r.getRoundNumber(b));
	    	        }catch (Exception e) {
	            		roundScores[b] = 0;
	            	}
					totalScore += roundScores[b];
	                if ( roundIncNumber != -1 ) {
	                  roundScore2 = roundScores[b];
	                }
	              }

	              data.append(firstNine+lastNine - r.getFieldPar()).append("\t");
	              
	              for ( int b = 1; b <= totalRounds; b++) {
	              	if ( roundScores[b] > 0 ) {
		              data.append(roundScores[b]).append("\t");
		            }else {
		              data.append("\t");
		            }
	              }
	              data.append(totalScore).append("\t");
	              data.append(difference).append("\t");
	            break;
	
	            case ResultComparator.TOTALSTROKESWITHHANDICAP :
					data.append(firstNine+lastNine-handicap).append("\t");
	            	roundScores = new int[totalRounds+1];
	            	int[] roundBruttos = new int[totalRounds+1];
	            	totalScore = 0;
	            	totalBrutto = 0;
	            	roundScore2 = 0;
	            	int roundScoreBrutto = 0;
	              
		              int roundScoreColumn2 = 10;
		              for ( int b = 1; b <= totalRounds; b++ ) {
		                roundScore2 = 0;
		                roundScoreBrutto = 0;
		                int roundIncNumber = r.getRound(b);
		                //int position2 = roundScoreColumn2 + (roundIncNumber*2 - 1) - 1;
		
			        	try {
			                roundScores[b] = r.getRoundScore(r.getRoundNumber(b));
		    	        }catch (Exception e) {
		            		roundScores[b] = 0;
		            	}
		                roundBruttos[b] = roundScores[b] + handicap;
		                if ( roundIncNumber != -1 ) {
		                  roundScore2 = roundScores[b];
		                  roundScoreBrutto = roundBruttos[b];
		                }
						
						totalScore += roundScore2;
						totalBrutto += roundScoreBrutto;
//						totalScore += roundScores[b];
//						totalBrutto += roundBruttos[b];
						
		              }
		
//	              data.append(roundScore2).append("\t");
//	              data.append(roundScoreBrutto).append("\t");
	              for ( int b = 1; b <= totalRounds; b++) {
	              	if ( roundScores[b] > 0 ) {
		              data.append(roundBruttos[b]).append("\t");
		              data.append(roundScores[b]).append("\t");
		            }else {
		              data.append("\t\t");
		            }
	              }

	              data.append(totalBrutto).append("\t");
	              data.append(totalScore).append("\t");
	
	            break;
	
	            case ResultComparator.TOTALPOINTS :
	            	
	            	 roundScores = new int[totalRounds+1];
	            	totalScore = 0;
	            	 roundScore2 = 0;
	              
	              for ( int b = 1; b <= totalRounds; b++ ) {
	                roundScore2 = 0;
	                int roundIncNumber = r.getRound(b);
	                //int position = roundScoreColumn3 + roundIncNumber - 1;
	
		        	try {
		                roundScores[b] = r.getRoundScore(r.getRoundNumber(b));
	    	        }catch (Exception e) {
	            		roundScores[b] = 0;
	            	}

	                if ( roundIncNumber != -1 ) {
	                  roundScore2 = roundScores[b];
	                }

	                totalScore += roundScores[b];
	              }	              
          
//	              data.append(roundScore2).append("\t");
	              for ( int b = 1; b <= totalRounds; b++) {
	              	if ( roundScores[b] > 0 ) {
		              data.append(roundScores[b]).append("\t");
		            }else {
		              data.append("\t");
		            }
	              }
	              data.append(totalScore).append("\t");
	              
	            break;
	          }
	        }
	        catch (Exception e) {
	          e.printStackTrace(System.err);
	          System.err.println("MemberID: "+memberID);
	        }
			
			data.append("\n");

	  	    c = data.toString().toCharArray();
         	out.write(c);
			
		}		

        out.close();

        Page page = getParentPage();
        page.setToRedirect("/servlet/Excel?&dir="+file,1);

      }
      catch(IOException io){
        add("io villa");
      }
      catch(SQLException sql){
        add("sql villa");sql.printStackTrace();
      }
		add("excel :D");
    }

  }

  private int getTournamentType(Tournament tournament, int orderBy) {
    int tournamentType = ResultComparator.TOTALSTROKES;
    try {
      TournamentType type = tournament.getTournamentType();
      String typeName = type.getTournamentType();

      if ( typeName.equalsIgnoreCase("points") )
	tournamentType = ResultComparator.TOTALPOINTS;
      if ( orderBy == ResultComparator.TOTALPOINTS )
	tournamentType = ResultComparator.TOTALPOINTS;
      if ( orderBy == ResultComparator.TOTALSTROKES )
	tournamentType = ResultComparator.TOTALSTROKES;
      if ( orderBy == ResultComparator.TOTALSTROKESWITHHANDICAP )
	tournamentType = ResultComparator.TOTALSTROKESWITHHANDICAP;

    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }

    return tournamentType;
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
