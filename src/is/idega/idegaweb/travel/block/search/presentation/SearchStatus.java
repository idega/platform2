package is.idega.idegaweb.travel.block.search.presentation;

import is.idega.idegaweb.travel.block.search.business.ServiceSearchSession;
import is.idega.idegaweb.travel.presentation.TravelBlock;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;

/**
 * @author gimmi
 */
public class SearchStatus extends TravelBlock {

	private String localizationKeyPrefix = "travel.search.search_status_";
	private int activeColumn = 1;
	
	private int sinkPixels = 5;
	private int rowHeight = 12;
		
	public SearchStatus() {
		super();
	}
	
	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		Table table = new Table(7, 8);
		table.setBorder(0);
		table.setCellpaddingAndCellspacing(0);
		table.setWidth("100%");
		table.setColumnAlignment(1, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(3, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(5, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(7, Table.HORIZONTAL_ALIGN_CENTER);
		
		table.setWidth(1, 80);
		table.setWidth(3, 80);
		table.setWidth(5, 80);
		table.setWidth(7, 80);
		
		Image icon = null;
		
		switch (getSession(iwc).getState()) {
			case AbstractSearchForm.STATE_SHOW_SEARCH_FORM :
				activeColumn = 1;
				icon = super.getBundle().getImage("images/icons/leita_01.jpg");
			break;
			case AbstractSearchForm.STATE_DEFINED_PRODUCT :
			case AbstractSearchForm.STATE_SHOW_SEARCH_RESULTS :
				activeColumn = 1;
				icon = super.getBundle().getImage("images/icons/leita_01.jpg");
				break;
			case AbstractSearchForm.STATE_SHOW_DETAILED_PRODUCT :
				activeColumn = 3;
				icon = super.getBundle().getImage("images/icons/detail_01.jpg");
				break;
			case AbstractSearchForm.STATE_SHOW_BOOKING_FORM :
				activeColumn = 5;
				icon = super.getBundle().getImage("images/icons/payment_01.jpg");
				break;
			case AbstractSearchForm.STATE_CHECK_BOOKING :
				activeColumn = 7;
				icon = super.getBundle().getImage("images/icons/confirm_01.jpg");
				break;
			default :
				
				break;
		}
				
		int row = 1;
		//table.add(icon, activeColumn, row);
		Table iconTable = new Table(3, 4);
		iconTable.setBorder(0);
		iconTable.setWidth("100%");
		iconTable.setCellpaddingAndCellspacing(0);
		iconTable.setWidth(2, 1, 2);
		iconTable.add( icon, 2, 1);
		iconTable.mergeCells(2, 1, 2, 4);
		iconTable.setHeight(1, 2);
		iconTable.setHeight(1, 3);
		iconTable.setHeight(1, 1, 27);
		iconTable.setHeight(1, 4, sinkPixels);
		iconTable.setVerticalAlignment(2, 1, Table.VERTICAL_ALIGN_BOTTOM);
		iconTable.setRowStyleClass(2, getStyleName(ServiceSearch.STYLENAME_BLUE_BACKGROUND_COLOR));
		iconTable.setStyleClass(1, 4, getStyleName(ServiceSearch.STYLENAME_BLUE_BACKGROUND_COLOR));
		iconTable.setStyleClass(3, 4, getStyleName(ServiceSearch.STYLENAME_BACKGROUND_COLOR));

		table.add(iconTable, activeColumn, row);
		table.mergeCells(activeColumn, row, activeColumn, row+3);
		table.setHeight(row, 27);
		table.setVerticalAlignment(activeColumn, row, Table.VERTICAL_ALIGN_BOTTOM);
		
		++row;
			table.setRowStyleClass(row, getStyleName(ServiceSearch.STYLENAME_BLUE_BACKGROUND_COLOR));

		++row;
		++row;
//		table.setRowStyleClass(row, getStyleName(ServiceSearch.STYLENAME_BLUE_BACKGROUND_COLOR));
		for ( int i = 1; i <= 7; i++) {
			if (i < activeColumn) {
				table.setStyleClass(i, row, getStyleName(ServiceSearch.STYLENAME_BLUE_BACKGROUND_COLOR));
			}
			else if ( i > activeColumn ) {
				table.setStyleClass(i, row, getStyleName(ServiceSearch.STYLENAME_BACKGROUND_COLOR));
			}
		}
		table.setHeight(row, sinkPixels);
		++row;
		for ( int i = 1; i <= 7; i++)  {
			table.add(getColoredTable(i), i, row);
		}
		//table.setRowHeight(row, Integer.toString((rowHeight - sinkPixels)));
		
		++row;
		
		++row;
		table.setRowStyleClass(row, getStyleName(ServiceSearch.STYLENAME_BACKGROUND_COLOR));
		String searchText = getResourceBundle().getLocalizedString(localizationKeyPrefix+AbstractSearchForm.STATE_SHOW_SEARCH_FORM); 
		String infoText = getResourceBundle().getLocalizedString(localizationKeyPrefix+AbstractSearchForm.STATE_SHOW_DETAILED_PRODUCT); 
		String paymentText = getResourceBundle().getLocalizedString(localizationKeyPrefix+AbstractSearchForm.STATE_SHOW_BOOKING_FORM); 
		String confirmText = getResourceBundle().getLocalizedString(localizationKeyPrefix+AbstractSearchForm.STATE_CHECK_BOOKING);
		table.add(getText(searchText), 1, row);
		table.add(getText(infoText), 3, row);
		table.add(getText(paymentText), 5, row);
		table.add(getText(confirmText), 7, row);
		table.setHeight(row, rowHeight);
		++row;
		table.setRowStyleClass(row, getStyleName(ServiceSearch.STYLENAME_HEADER_BACKGROUND_COLOR));
		
//		table.add(super.getResourceBundle().getLocalizedString(localizedKey, localizedKey), activeColumn, row);
		
		//add("State = "+super.getResourceBundle().getLocalizedString(localizedKey, localizedKey));
		
		
		add(table);
	}

	private Table getColoredTable(int column) {
		Table table = new Table(3, 1);
		table.setCellpaddingAndCellspacing(0);
		table.setWidth(2, 1);
		//table.setHeight(1, 50);
		table.setWidth("100%");
		table.setHeight(1, rowHeight - sinkPixels);
		if (column < activeColumn) {
			table.setStyleClass(1, 1, getStyleName(ServiceSearch.STYLENAME_BLUE_BACKGROUND_COLOR));
			table.setStyleClass(3, 1, getStyleName(ServiceSearch.STYLENAME_BLUE_BACKGROUND_COLOR));
			if ( column % 2 == 0) {
				table.setStyleClass(2, 1, getStyleName(ServiceSearch.STYLENAME_BLUE_BACKGROUND_COLOR));
			}
		}
		else if (column > activeColumn) {
			table.setStyleClass(1, 1, getStyleName(ServiceSearch.STYLENAME_BACKGROUND_COLOR));
			table.setStyleClass(3, 1, getStyleName(ServiceSearch.STYLENAME_BACKGROUND_COLOR));
			if ( column % 2 == 0) {
				table.setStyleClass(2, 1, getStyleName(ServiceSearch.STYLENAME_BACKGROUND_COLOR));
			}
		}
		else if (column == activeColumn) {
			table.setStyleClass(1, 1, getStyleName(ServiceSearch.STYLENAME_BLUE_BACKGROUND_COLOR));
			table.setStyleClass(3, 1, getStyleName(ServiceSearch.STYLENAME_BACKGROUND_COLOR));
		}
		
		return table;
	}
	
	protected Text getText(String content) {
		Text text = new Text(content);
		if (getStyleName(ServiceSearch.STYLENAME_TEXT) != null) {
			text = getStyleText(text, ServiceSearch.STYLENAME_TEXT);
		}
		return text;
	}
	protected ServiceSearchSession getSession(IWContext iwc) {
		try {
			return (ServiceSearchSession) IBOLookup.getSessionInstance(iwc, ServiceSearchSession.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException();
		}
	}
	
}
