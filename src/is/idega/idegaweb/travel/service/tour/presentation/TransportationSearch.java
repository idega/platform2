package is.idega.idegaweb.travel.service.tour.presentation;

import is.idega.idegaweb.travel.service.tour.data.TourCategory;

import java.util.List;

import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Table;

/**
 * @author gimmi
 */
public class TransportationSearch extends TourSearch {

	public TransportationSearch() {
		super();
	}
	
	protected String getTourCategory() {
		return TourCategory.CATEGORY_TRANSPORTATION;
	}
	
	protected String getServiceName(IWResourceBundle iwrb) {
		return iwrb.getLocalizedString("travel.search.transportation","Transportation");		
	}

	protected void setupSpecialFieldsForBookingForm(Table table, int row, List errorFields) {
	}

	protected List getErrorFormFields() {
		return null;
	}

}
