package is.idega.idegaweb.golf.startingtime.presentation;

import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.presentation.GolfBlock;
import is.idega.idegaweb.golf.startingtime.business.TeeTimeBusiness;

import java.io.IOException;
import java.sql.SQLException;

import com.idega.business.IBOLookup;
import com.idega.core.builder.data.ICPage;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;

/**
 * Title: TeeTimeSearch 
 * Description: 
 * Copyright: Copyright (c) 2004 
 * Company: idega Software
 * @author 2004 - idega team -<br>
 *         <a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson </a> <br>
 * @version 1.0
 */

public class TeeTimes extends GolfBlock {
	
	private ICPage _teeTimeTablePage = null;
	private ICPage _teeTimeSearchPage = null;
	
	private String _clubLinkTableWidth = Table.HUNDRED_PERCENT;
	
//	private Table _clubLinkTable = new Table();
//
//	public void initializeInMain(IWContext iwc) {
//		add(_clubLinkTable);
//	}
//	
//	public Object clone() {
//		TeeTimes t = (TeeTimes)super.clone();
//		t._clubLinkTable = (Table)this._clubLinkTable.clone();
//		return t;
//	}
	
	public void main(IWContext iwc) throws Exception {
		removeUnionIdSessionAttribute(iwc);
		add(getListOfClubLinks(iwc));
		
	}

	public PresentationObject getListOfClubLinks(IWContext iwc) throws IOException, SQLException {
		Table clubLinkTable = new Table();
		clubLinkTable.setWidth(_clubLinkTableWidth);
		clubLinkTable.setCellspacing(0);
		clubLinkTable.setCellpadding(0);
		

		int row = 1;
		
		//Header
		clubLinkTable.add(getLocalizedSmallHeader("start.search.club","Club"),1,row);
		clubLinkTable.add(getLocalizedSmallHeader("start.search.location","Location"),2,row);
		clubLinkTable.add(getLocalizedSmallHeader("start.search.go","Go"),3,row);
		clubLinkTable.setRowStyleClass(row,getHeaderRowClass());
		clubLinkTable.setRowVerticalAlignment(row, Table.VERTICAL_ALIGN_BOTTOM);
		row++;
		//-Header ends
		
		//Union list
		TeeTimeBusiness business = (TeeTimeBusiness)IBOLookup.getServiceInstance(iwc,TeeTimeBusiness.class);
		Union[] union = business.getStartingEntryUnion();
		for (int i = 0; i < union.length; i++) {
			Link l = getLink(union[i].getName());
			l.addParameter("club",union[i].getPrimaryKey().toString());
			
			Link go = getLink(localize("start.search.go","Go"));
			go.setImage(getBundle().getSharedImage("teetimes/go.gif",localize("start.search.go","Go")));
			go.addParameter("club",union[i].getPrimaryKey().toString());
			
			if(_teeTimeTablePage != null) {
				l.setPage(_teeTimeTablePage);
				go.setPage(_teeTimeTablePage);
			}
			
			clubLinkTable.add(l,1,row);
			//clubLinkTable.add(*location*);
			clubLinkTable.add(go,3,row);
			
			clubLinkTable.setRowStyleClass(row,((i%2==0)?getLightRowClass():getDarkRowClass()));
			row++;
		}
		//-Union list ends
		

		//myTable.add(getListOfClubs("club", modinfo, 6), 2, 3);


		return clubLinkTable;
	}

	public ICPage getTeeTimeSearchPage() {
		return _teeTimeSearchPage;
	}
	public void setTeeTimeSearchPage(ICPage p) {
		_teeTimeSearchPage = p;
	}
	public ICPage getTeeTimeTablePage() {
		return _teeTimeTablePage;
	}
	public void setTeeTimeTablePage(ICPage p) {
		_teeTimeTablePage = p;
	}

}