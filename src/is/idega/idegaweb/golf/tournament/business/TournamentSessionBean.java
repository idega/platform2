/*
 * Created on 26.4.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package is.idega.idegaweb.golf.tournament.business;

import is.idega.idegaweb.golf.entity.Tournament;
import is.idega.idegaweb.golf.entity.TournamentHome;

import java.sql.Date;
import java.sql.SQLException;

import com.idega.business.IBOSessionBean;
import com.idega.data.IDOLookup;


/**
 * @author laddi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TournamentSessionBean extends IBOSessionBean implements TournamentSession {

	private static final String PARAMETER_TOURNAMENT_ID = "tournament_id";
	private static final String PARAMETER_START_DATE = "start_date";
	private static final String PARAMETER_END_DATE = "end_date";
	
	private Tournament tournament;
	private int tournamentID;
	private Date startDate;
	private Date endDate;
	
	public Tournament getTournament() {
		if (tournament == null) {
			if (tournamentID != -1) {
				try {
					tournament = ((TournamentHome) IDOLookup.getHomeLegacy(Tournament.class)).findByPrimaryKeyLegacy(tournamentID);
				}
				catch (SQLException e) {
					tournament = null;
				}
			}
		}
		return tournament;
	}
	
	public int getTournamentID() {
		return tournamentID;
	}

	public Date getStartDate() {
		return startDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	
	public void setTournament(Tournament tournament){
		this.tournament = tournament;
	}
	
	public void setTournamentID(int tournamentID) {
		this.tournamentID = tournamentID;
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getParameterNameTournamentID() {
		return PARAMETER_TOURNAMENT_ID;
	}
	
	public String getParameterNameStartDate() {
		return PARAMETER_START_DATE;
	}
	
	public String getParameterNameEndDate() {
		return PARAMETER_END_DATE;
	}
}