package is.idega.idegaweb.golf.presentation;

import is.idega.idegaweb.golf.business.StatisticsBusiness;
import is.idega.idegaweb.golf.entity.Tee;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;

/**
 * @author Anna
 */
public class HoleStatistics extends GolfBlock {

	public static final String PARAMETER_HOLE_NUMBER = "hole_number";
	public static final String PARAMETER_FIELD_ID = "field_id";
	
	int fieldId = -1;
	int holeNumber = -1;
		
	public void main(IWContext iwc) throws Exception {

		try {
			fieldId = Integer.parseInt(iwc.getParameter(PARAMETER_FIELD_ID));
			holeNumber = Integer.parseInt(iwc.getParameter(PARAMETER_HOLE_NUMBER));
		} catch (NumberFormatException e) {}
		
		if (fieldId > 0 && holeNumber > 0) {
			addStatistics(iwc);
		}
		else {
			add("Field or holenumber error");
		}
	}
	
	public void addStatistics(IWContext iwc) throws RemoteException {
		StatisticsBusiness business = getBusiness(iwc);
		Collection coll = business.getTeeFromFieldIDAndHoleNumber(fieldId, holeNumber);
				
		Table table = getTable(coll);
		int row = 1;
		
		table.add(getSmallHeader(localize("hole.on_fairway","On fairway")), 2, row);
		table.add(getSmallHeader(localize("hole.on_green","On green")), 3, row);
		table.add(getSmallHeader(localize("hole.putts","Putts")), 4, row);
		table.setRowStyleClass(row++, getHeaderRowClass());
		
		table.add(getSmallText(localize("hole.total","total")), 1, row);
		table.add(getSmallText(business.getPercentText(business.getNumberOnFairwayByTeeID(coll))), 2, row);
		table.add(getSmallText(business.getPercentText(business.getNumberOnGreenByTeeID(coll))), 3, row);
		table.add(getSmallText(business.getDecimalText(business.getPuttAverageByTeeID(coll))), 4, row);
		table.setRowStyleClass(row++, getLightRowClass());
		
		boolean darkBackground = true;
		
		Tee tee;
		if (coll != null) {
			Iterator iter = coll.iterator();
			Collection tmp;
			while (iter.hasNext()) {
				tee = (Tee) iter.next();
				tmp = new Vector();
				tmp.add(tee.getPrimaryKey());
				table.add(getSmallText(business.getTeeColor(tee).getName()), 1, row);
				table.add(getSmallText(business.getPercentText(business.getNumberOnFairwayByTeeID(tmp))), 2, row);
				table.add(getSmallText(business.getPercentText(business.getNumberOnGreenByTeeID(tmp))), 3, row);
				table.add(getSmallText(business.getDecimalText(business.getPuttAverageByTeeID(tmp))), 4, row);
				if (darkBackground) {
					table.setRowStyleClass(row++, getDarkRowClass());
				}
				else {
					table.setRowStyleClass(row++, getLightRowClass());
				}
				darkBackground = !darkBackground;
			}
		}
		table.setBorder(1);
		table.mergeCells(1,row,4,row);
		table.setCellpadding(1,row,4);
		table.add(getSmallText(localize("hole.statistic_explained","Statistic explained")),1,row++);
		add(table);
	}

	private Table getTable(Collection coll) {
		int noTees = 0;
		if (coll != null) {
			noTees = coll.size();
		}
		Table table = new Table(4, 3 + noTees);
//		Table table = new Table();
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setColumnAlignment(2, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(3, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(4, Table.HORIZONTAL_ALIGN_CENTER);
		return table;
	}
	
	protected StatisticsBusiness getBusiness(IWContext iwc) {
		try {
			return (StatisticsBusiness) IBOLookup.getServiceInstance(iwc, StatisticsBusiness.class);
		} catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}
}
