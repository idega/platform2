package is.idega.idegaweb.travel.service.tour.presentation;

import java.util.List;

import is.idega.idegaweb.travel.service.tour.data.TourCategory;

import com.idega.idegaweb.IWResourceBundle;

/**
 * @author gimmi
 */
public class ExcursionSearch extends TourSearch {

	protected String getTourCategory() {
		return TourCategory.CATEGORY_EXCURSION;
	}

	protected String getServiceName(IWResourceBundle iwrb) {
		return iwrb.getLocalizedString("travel.search.excursion","Excursion");		
	}

	protected void setupSpecialFieldsForBookingForm(List errorFields) {
	}

	protected List getErrorFormFields() {
		return null;
	}
}
