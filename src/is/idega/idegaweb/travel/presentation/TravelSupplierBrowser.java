package is.idega.idegaweb.travel.presentation;

import com.idega.block.trade.stockroom.business.TradeConstants;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;

public abstract class TravelSupplierBrowser extends TravelManager {

	protected abstract Class getPlugin();
	protected abstract Table getHeaderTable(IWContext iwc);
	protected abstract boolean showBrowser(IWContext iwc);

	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		
		add(Text.BREAK);
		

		if (super.isSupplierManagerBookerStaff() || hasRole(iwc, TradeConstants.ROLE_SUPPLIER_MANAGER_CASHIER_STAFF)) {
			Table startTable = getHeaderTable(iwc);
			if (startTable != null) {
				startTable.setWidth("90%");
				add(startTable);
				add(Text.BREAK);
			}

			if (showBrowser(iwc)) {
				SupplierBrowser sb = new SupplierBrowser();
				sb.setWidth("90%");
				sb.setUseTravelLook(true);
				sb.setSupplierManager(super.getSupplierManager());
				sb.setPlugin(getPlugin().getName());
				sb.setHeaderStyleClass("sbrowser_header");
				sb.setInterfaceObjectStyleClass("sbrowser_interface");
				
				add(sb);
			}
			
		} else if (!super.isLoggedOn(iwc)) {
			add(super.getResourceBundle().getLocalizedString("travel.not_logged_on", "Not logged on"));
		} else {
			add(super.getResourceBundle().getLocalizedString("travel.no_permission", "No permission"));
		}
	}
	
}
