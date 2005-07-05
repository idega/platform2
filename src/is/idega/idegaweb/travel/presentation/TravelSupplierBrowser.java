package is.idega.idegaweb.travel.presentation;

import com.idega.presentation.IWContext;
import com.idega.presentation.text.Text;

public abstract class TravelSupplierBrowser extends TravelManager {

	protected abstract Class getPlugin();	

	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		
		add(Text.BREAK);

		if (super.isSupplierManagerBookerStaff()) {
			SupplierBrowser sb = new SupplierBrowser();
			sb.setWidth("90%");
			sb.setUseTravelLook(true);
			sb.setSupplierManager(super.getSupplierManager());
			sb.setPlugin(getPlugin().getName());
			
			add(sb);
		} else if (!super.isLoggedOn(iwc)) {
			add(super.getResourceBundle().getLocalizedString("travel.not_logged_on", "Not logged on"));
		} else {
			add(super.getResourceBundle().getLocalizedString("travel.no_permission", "No permission"));
		}
	}
	
}
