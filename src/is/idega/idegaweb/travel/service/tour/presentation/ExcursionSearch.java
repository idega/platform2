package is.idega.idegaweb.travel.service.tour.presentation;

import is.idega.idegaweb.travel.service.tour.data.TourCategory;

import java.util.List;

import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Table;

/**
 * @author gimmi
 */
public class ExcursionSearch extends TourSearch {

	public ExcursionSearch() {
		super();
	}
	
	protected String getTourCategory() {
		return TourCategory.CATEGORY_EXCURSION;
	}

	protected String getServiceName(IWResourceBundle iwrb) {
		return iwrb.getLocalizedString("travel.search.excursion","Excursion");		
	}

	protected void setupSpecialFieldsForBookingForm(Table table, int row, List errorFields) {
	}

	protected List getErrorFormFields() {
		return null;
	}
}
