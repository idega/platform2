package is.idega.idegaweb.campus.nortek.presentation;

import is.idega.idegaweb.campus.presentation.CampusBlock;

import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;

public class NortekSettings extends CampusBlock {

	public final static String BUNDLE_IDENTIFIER = "is.idega.idegaweb.campus.nortek";

	
	
	public String getBundleIdentifier() {
		return BUNDLE_IDENTIFIER;
	}

	protected void control(IWContext iwc) {
		if (iwc.isParameterSet(PARAM_SAVE)) {
			saveCard(iwc);
		}
		
		add(getSetup(iwc));
	}
	
	private void saveCard(IWContext iwc) {
		
	}
	
	private PresentationObject getSetup(IWContext iwc) {
		return null;
	}
	
	public void main(IWContext iwc) throws Exception {
		control(iwc);
	}
}