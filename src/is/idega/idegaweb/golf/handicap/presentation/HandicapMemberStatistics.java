/*
 * Created on 4.6.2004
 */
package is.idega.idegaweb.golf.handicap.presentation;

import java.rmi.RemoteException;

import is.idega.idegaweb.golf.business.StatisticsBusiness;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.presentation.GolfBlock;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;


/**
 * @author laddi
 */
public class HandicapMemberStatistics extends GolfBlock {

	private boolean iShowHoleStatistics = true;
	private boolean iShowTotalStatistics = false;
	private boolean iShowRegisteredStatistics = false;
	
	private boolean iShowInfo = false;
	
	private StatisticsBusiness statBusiness;
	String iMemberID;
	
	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#main(com.idega.presentation.IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
		statBusiness = getStatisticsBusiness(iwc);
		
		iMemberID = iwc.getRequest().getParameter("member_id");
		if (iMemberID == null) {
			iMemberID = (String) iwc.getSession().getAttribute("member_id");
		}
		if (iMemberID == null) {
			Member member = (Member) iwc.getSession().getAttribute("member_login");
			if (member != null) {
				iMemberID = String.valueOf(member.getID());
				if (iMemberID == null) {
					iMemberID = "1";
				}
			}
			else {
				iMemberID = "1";
			}
		}

		if (iShowHoleStatistics) {
			getHoleStatistics(iwc);
		}
		else if (iShowTotalStatistics) {
			getTotalStatistics(iwc);
		}
		else if (iShowRegisteredStatistics) {
			getRegisteredStatistics(iwc);
		}
	}
	
	private void getHoleStatistics(IWContext iwc) throws RemoteException {
		int eagles = statBusiness.getNumberOfEaglesByMember(Integer.parseInt(iMemberID));
		int birdies = statBusiness.getNumberOfBirdiesByMember(Integer.parseInt(iMemberID));
		int pars = statBusiness.getNumberOfParsByMember(Integer.parseInt(iMemberID));
		int bogeys = statBusiness.getNumberOfBogeysByMember(Integer.parseInt(iMemberID));
		int doubleBogeys = statBusiness.getNumberOfDoubleBogeysByMember(Integer.parseInt(iMemberID));
		int totalStrokes = eagles + birdies + pars + bogeys + doubleBogeys;
		
		double averageEagles = 0;
		double averageBirdies = 0;
		double averagePars = 0;
		double averageBogeys = 0;
		double averageDoubleBogeys = 0;
		if (totalStrokes > 0) {
			averageEagles = (double) eagles / (double) totalStrokes;
			averageBirdies = (double) birdies / (double) totalStrokes;
			averagePars = (double) pars / (double) totalStrokes;
			averageBogeys = (double) bogeys / (double) totalStrokes;
			averageDoubleBogeys = (double) doubleBogeys / (double) totalStrokes;
		}

		Table table = new Table();
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setRows(6);
		table.setColumns(3);
		table.setColumnAlignment(2, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(3, Table.HORIZONTAL_ALIGN_CENTER);
		int row = 1;
		
		table.add(getSmallHeader(localize("handicap.count", "Count")), 2, row);
		table.add(getSmallHeader(localize("handicap.average_of_total", "Total")), 3, row);
		table.setRowStyleClass(row++, getHeaderRowClass());
		
		table.add(getSmallText(localize("handicap.eagles", "Eagles")), 1, row);
		table.add(getSmallText(String.valueOf(eagles)), 2, row);
		table.add(getSmallText(statBusiness.getPercentText(averageEagles)), 3, row);
		table.setRowStyleClass(row++, getLightRowClass());

		table.add(getSmallText(localize("handicap.birdies", "Birdies")), 1, row);
		table.add(getSmallText(String.valueOf(birdies)), 2, row);
		table.add(getSmallText(statBusiness.getPercentText(averageBirdies)), 3, row);
		table.setRowStyleClass(row++, getDarkRowClass());

		table.add(getSmallText(localize("handicap.pars", "Pars")), 1, row);
		table.add(getSmallText(String.valueOf(pars)), 2, row);
		table.add(getSmallText(statBusiness.getPercentText(averagePars)), 3, row);
		table.setRowStyleClass(row++, getLightRowClass());
		
		table.add(getSmallText(localize("handicap.bogeys", "Bogeys")), 1, row);
		table.add(getSmallText(String.valueOf(bogeys)), 2, row);
		table.add(getSmallText(statBusiness.getPercentText(averageBogeys)), 3, row);
		table.setRowStyleClass(row++, getDarkRowClass());

		table.add(getSmallText(localize("handicap.double_bogeys", "Double bogeys")), 1, row);
		table.add(getSmallText(String.valueOf(doubleBogeys)), 2, row);
		table.add(getSmallText(statBusiness.getPercentText(averageDoubleBogeys)), 3, row);
		table.setRowStyleClass(row++, getLightRowClass());
		
		if (iShowInfo) {
			table.setRows(7);
			table.mergeCells(1, row, 3, row);
			table.setCellpadding(1, row, 4);
			table.add(getText(localize("handicap.hole_statistics_info", "The statistics shows information calculated from every scorecard entered in the database for the golfer.")), 1, row);
		}
		
		add(table);
	}
	
	private void getTotalStatistics(IWContext iwc) throws RemoteException {
		int strokes = statBusiness.getSumOfStrokesByMember(Integer.parseInt(iMemberID));
		int points = statBusiness.getSumOfPointsByMember(Integer.parseInt(iMemberID));
		int holesPlayed = statBusiness.getNumberOfHolesPlayedByMember(Integer.parseInt(iMemberID));
		int roundsPlayed = statBusiness.getNumberOfRoundsByMember(Integer.parseInt(iMemberID));

		Table table = new Table();
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setColumns(2);
		table.setRows(5);
		table.setColumnAlignment(2, Table.HORIZONTAL_ALIGN_CENTER);
		int row = 1;
		
		table.add(getSmallHeader(localize("handicap.count", "Count")), 2, row);
		table.setRowStyleClass(row++, getHeaderRowClass());
		
		table.add(getSmallText(localize("handicap.strokes", "Strokes")), 1, row);
		table.add(getSmallText(String.valueOf(strokes)), 2, row);
		table.setRowStyleClass(row++, getLightRowClass());

		table.add(getSmallText(localize("handicap.points", "Points")), 1, row);
		table.add(getSmallText(String.valueOf(points)), 2, row);
		table.setRowStyleClass(row++, getDarkRowClass());

		table.add(getSmallText(localize("handicap.holes_played_total", "Holes played")), 1, row);
		table.add(getSmallText(String.valueOf(holesPlayed)), 2, row);
		table.setRowStyleClass(row++, getLightRowClass());

		table.add(getSmallText(localize("handicap.rounds_played_total", "Rounds played")), 1, row);
		table.add(getSmallText(String.valueOf(roundsPlayed)), 2, row);
		table.setRowStyleClass(row++, getDarkRowClass());

		if (iShowInfo) {
			table.setRows(6);
			table.mergeCells(1, row, 2, row);
			table.setCellpadding(1, row, 4);
			table.add(getText(localize("handicap.total_statistics_info", "The statistics shows information calculated from every scorecard entered in the database for the golfer.")), 1, row);
		}
		
		add(table);
	}
	
	private void getRegisteredStatistics(IWContext iwc) throws RemoteException {
		int fairways = statBusiness.getNumberOnFairwayByMember(Integer.parseInt(iMemberID));
		int greens = statBusiness.getNumberOnGreenByMember(Integer.parseInt(iMemberID));
		int putts = statBusiness.getSumOfPuttsByMember(Integer.parseInt(iMemberID));
		
		double averageFairways = statBusiness.getFairwayAverageByMember(Integer.parseInt(iMemberID));
		double averageGreens = statBusiness.getOnGreenAverageByMember(Integer.parseInt(iMemberID));
		double averagePutts = statBusiness.getPuttAverageByMember(Integer.parseInt(iMemberID));

		Table table = new Table();
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setRows(4);
		table.setColumns(3);
		table.setColumnAlignment(2, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(3, Table.HORIZONTAL_ALIGN_CENTER);
		int row = 1;
		
		table.add(getSmallHeader(localize("handicap.count", "Count")), 2, row);
		table.add(getSmallHeader(localize("handicap.average_of_total", "Total")), 3, row);
		table.setRowStyleClass(row++, getHeaderRowClass());
		
		table.add(getSmallText(localize("handicap.fairways", "Fairways")), 1, row);
		if (fairways > 0) {
			table.add(getSmallText(String.valueOf(fairways)), 2, row);
			table.add(getSmallText(statBusiness.getPercentText(averageFairways)), 3, row);
		}
		else {
			table.add(getSmallText("-"), 2, row);
			table.add(getSmallText("-"), 3, row);
		}
		table.setRowStyleClass(row++, getLightRowClass());

		table.add(getSmallText(localize("handicap.green_in_regulation", "GIR")), 1, row);
		if (fairways > 0) {
			table.add(getSmallText(String.valueOf(greens)), 2, row);
			table.add(getSmallText(statBusiness.getPercentText(averageGreens)), 3, row);
		}
		else {
			table.add(getSmallText("-"), 2, row);
			table.add(getSmallText("-"), 3, row);
		}
		table.setRowStyleClass(row++, getDarkRowClass());

		table.add(getSmallText(localize("handicap.putts", "Putts")), 1, row);
		if (fairways > 0) {
			table.add(getSmallText(String.valueOf(putts)), 2, row);
			table.add(getSmallText(statBusiness.getPercentText(averagePutts)), 3, row);
		}
		else {
			table.add(getSmallText("-"), 2, row);
			table.add(getSmallText("-"), 3, row);
		}
		table.setRowStyleClass(row++, getLightRowClass());
		
		if (iShowInfo) {
			table.setRows(5);
			table.mergeCells(1, row, 3, row);
			table.setCellpadding(1, row, 4);
			table.add(getText(localize("handicap.registered_statistics_info", "The statistics shows information entered manually by user.")), 1, row);
		}
		
		add(table);
	}
	
	private StatisticsBusiness getStatisticsBusiness(IWApplicationContext iwac) {
		try {
			return (StatisticsBusiness) IBOLookup.getServiceInstance(iwac, StatisticsBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	/**
	 * @param showHoleStatistics The iShowHoleStatistics to set.
	 */
	public void setShowHoleStatistics(boolean showHoleStatistics) {
		iShowHoleStatistics = showHoleStatistics;
		iShowTotalStatistics = !showHoleStatistics;
		iShowRegisteredStatistics = !showHoleStatistics;
	}
	
	/**
	 * @param showTotalStatistics The iShowTotalStatistics to set.
	 */
	public void setShowTotalStatistics(boolean showTotalStatistics) {
		iShowTotalStatistics = showTotalStatistics;
		iShowHoleStatistics = !showTotalStatistics;
		iShowRegisteredStatistics = !showTotalStatistics;
	}
	
	/**
	 * @param showRegisteredStatistics The iShowRegisteredStatistics to set.
	 */
	public void setShowRegisteredStatistics(boolean showRegisteredStatistics) {
		iShowRegisteredStatistics = showRegisteredStatistics;
		iShowTotalStatistics = !showRegisteredStatistics;
		iShowHoleStatistics = !showRegisteredStatistics;
	}
	
	/**
	 * @param showInfo The iShowInfo to set.
	 */
	public void setShowInfo(boolean showInfo) {
		iShowInfo = showInfo;
	}
}