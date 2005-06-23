package is.idega.idegaweb.travel.presentation;

import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;

public class TravelSupplierBrowser extends TravelManager {

		public void main(IWContext iwc) throws Exception {
			super.main(iwc);
			SupplierBrowser sb = new SupplierBrowser();
			
			String plug = iwc.getParameter("tmp_plugin");
			
			Table table = new Table();
			table.setColor("WHITE");
			sb.setSupplierManager(super.getSupplierManager());
			sb.setPlugin(plug);
			table.add(sb, 1, 1);
			add(Text.BREAK);
			add(table);
		}
		
}
