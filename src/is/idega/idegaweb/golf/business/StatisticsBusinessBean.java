package is.idega.idegaweb.golf.business;


import is.idega.idegaweb.golf.entity.Statistic;
import is.idega.idegaweb.golf.entity.StatisticHome;
import is.idega.idegaweb.golf.entity.Tee;
import is.idega.idegaweb.golf.entity.TeeColor;
import is.idega.idegaweb.golf.entity.TeeColorHome;
import is.idega.idegaweb.golf.entity.TeeHome;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.business.IBOServiceBean;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;

/**
 * @author Anna
 */
public class StatisticsBusinessBean extends IBOServiceBean implements StatisticsBusiness{

	private static NumberFormat nf = NumberFormat.getPercentInstance();
	private static DecimalFormat iif = new DecimalFormat("0.00");
	
	public double getNumberOnFairwayByTeeID(Collection teeIDs){
		double fairwayPercent = 0;
		try {			
			int totalCount = getHome().getCountByTeeId(teeIDs);
			int fairwayCount = getHome().getNumberOnFairwayByTeeID(teeIDs);
			fairwayPercent = ((double) fairwayCount / (double) totalCount);
			return fairwayPercent;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fairwayPercent;
	}

	public double getNumberOnGreenByTeeID(Collection teeIDs){
		double greenPercent = 0;
		try {
			int totalCount = getHome().getCountByTeeId(teeIDs);
			int greenCount = getHome().getNumberOnGreenByTeeID(teeIDs);
			greenPercent = ((double) greenCount / (double) totalCount);
			return greenPercent;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return greenPercent;
	}
	public double getPuttAverageByTeeID(Collection teeIDs){
		try {
			return getHome().getPuttAverageByTeeID(teeIDs);
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (IDOException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public Collection getStatisticsByTeeID(Collection teeIDs){		
		try {
			return getHome().findByTeeID(teeIDs);
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		return null;	
	}
	
	public Collection getTeeFromFieldIDAndHoleNumber(int fieldID, int holeNumber) {
		try {
			return getTeeHome().findByFieldAndHoleNumber(fieldID, holeNumber);
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public TeeColor getTeeColor(Tee tee) {
		try {
			return getTeeColorHome().findByPrimaryKey(tee.getTeeColorID());
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getPercentText(double number) {
		return nf.format(number);
	}
	
	public String getDecimalText(double number) {
		return iif.format(number);
	}
	
	private StatisticHome getHome()throws IDOLookupException{
		return (StatisticHome) IDOLookup.getHome(Statistic.class);
	}	

	private TeeHome getTeeHome()throws IDOLookupException{
		return (TeeHome) IDOLookup.getHome(Tee.class);
	}		

	private TeeColorHome getTeeColorHome()throws IDOLookupException{
		return (TeeColorHome) IDOLookup.getHome(TeeColor.class);
	}
}
