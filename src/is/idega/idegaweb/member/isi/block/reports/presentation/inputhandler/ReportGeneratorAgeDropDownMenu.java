package is.idega.idegaweb.member.isi.block.reports.presentation.inputhandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.idega.business.InputHandler;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.util.IWTimestamp;
/**
 * A presentation object for dynamic reports. Select an age (1-123).
 * 
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */
public class ReportGeneratorAgeDropDownMenu extends DropdownMenu implements InputHandler {

	private static final int youngest = 1;

	private static final int oldest = 123;
	
	private List _allAges = new ArrayList();

	protected static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
	
	public ReportGeneratorAgeDropDownMenu() {
		super();
	}

	public void main(IWContext iwc) throws Exception {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		addMenuElement(" ",iwrb.getLocalizedString("AgeDropdownmenu.all_ages", "All ages"));
		for (int i = youngest; i <= oldest; i++) {
			String age = Integer.toString(i);
			addMenuElement(i, age);
			_allAges.add(age);
		}
		
		setSelectedElement(" ");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.business.InputHandler#getHandlerObject(java.lang.String, java.lang.String, com.idega.presentation.IWContext)
	 */
	public PresentationObject getHandlerObject(String name, String stringValue, IWContext iwc) {
		this.setName(name);

		if (stringValue != null) {
			this.setSelectedElement(stringValue);
		}

		return this;
	}

	/**
	 * @return the year, Integer
	 *  
	 */
	public Object getResultingObject(String[] values, IWContext iwc) throws Exception {
		Collection ages = null;
		if (values != null && values.length > 0) {
			ages = new ArrayList();
			if(values.length==1 && values[0].equals(" ")) {
				ages = _allAges;
			} else {
				for(int i=0; i<values.length; i++) {
					int age = Integer.parseInt(values[i]);
					
					IWTimestamp stamp = IWTimestamp.RightNow();
				
					stamp.addYears(-age);
								
					ages.add(stamp.toString());
				}
			}
		} else {
			ages = _allAges;
		}
		return ages;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.business.InputHandler#getDisplayNameOfValue(java.lang.String, com.idega.presentation.IWContext)
	 */
	public String getDisplayNameOfValue(Object value, IWContext iwc) {
		if (value != null) {
			Iterator iter = ((Collection) value).iterator();
			StringBuffer ages = new StringBuffer();
			int numberOfAges = ((Collection) value).size();
			int counter = 0;

			while (iter.hasNext()) {
				String age = (String) iter.next();
				ages.append(age);
				counter++;
				if (counter < numberOfAges) {
					ages.append(",");
				}
			}

			return ages.toString();

		}
		IWResourceBundle iwrb = getResourceBundle(iwc);
		return iwrb.getLocalizedString("AgeDropdownmenu.all_ages", "All ages");
	}
	
	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#getBundleIdentifier()
	 */
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
}
