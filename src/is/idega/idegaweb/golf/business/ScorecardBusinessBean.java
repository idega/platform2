/*
 * Created on 9.6.2004
 */
package is.idega.idegaweb.golf.business;

import is.idega.idegaweb.golf.entity.Scorecard;
import is.idega.idegaweb.golf.entity.ScorecardHome;

import java.rmi.RemoteException;
import java.sql.Date;

import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;

/**
 * @author laddi
 */
public class ScorecardBusinessBean extends IBOServiceBean implements ScorecardBusiness {

	public int getNumberOfRoundsAfterDate(int memberID, Date date) {
		try {
			return getScorecardHome().getNumberOfRoundsAfterDateByMember(memberID, date);
		}
		catch (IDOException ie) {
			log(ie);
			return 0;
		}
	}
	
	public Scorecard getBestRoundAfterDate(int memberID, Date date) {
		try {
			return getScorecardHome().findBestRoundAfterDateByMember(memberID, date);
		}
		catch (FinderException fe) {
			return null;
		}
	}
	
	public Scorecard getLastPlayedRound(int memberID) throws FinderException {
		try {
			return getScorecardHome().findLastPlayedRoundByMember(memberID);
		}
		catch (FinderException fe) {
			return null;
		}
	}
	
	public double getPointsAverage(int memberID) {
		try {
			int points = getStatisticsBusiness().getSumOfPointsByMember(memberID);
			int rounds = getScorecardHome().getCountRoundsPlayedByMember(memberID);
			
			if (rounds > 0) {
				return (double) points / (double) rounds;
			}
			return 0;
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
		catch (IDOException ie) {
			return 0;
		}
	}
	
	private StatisticsBusiness getStatisticsBusiness() {
		try {
			return (StatisticsBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), StatisticsBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	private ScorecardHome getScorecardHome() {
		return (ScorecardHome) IDOLookup.getHomeLegacy(Scorecard.class);
	}
}