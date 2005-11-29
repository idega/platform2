package is.idega.idegaweb.campus.bn.presentation;

import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObjectContainer;
import com.idega.presentation.Table;

public class BNCampusApply extends PresentationObjectContainer {
	private static final String IW_RESOURCE_BUNDLE = "is.idega.idegaweb.campus";

	public String getBundleIdentifier() {
		return IW_RESOURCE_BUNDLE;
	}

	public void main(IWContext iwc) {
		IWBundle iwb = getBundle(iwc);
		IWResourceBundle iwrb = getResourceBundle(iwc);
		Table T = new Table(1, 1);
		T.setWidth("100%");
		T.setAlignment(1, 1, "center");
		T.setVerticalAlignment(1, 1, "top");
		T.add(new BNCampusApplicationForm(), 1, 1);
		add(T);
	}
}