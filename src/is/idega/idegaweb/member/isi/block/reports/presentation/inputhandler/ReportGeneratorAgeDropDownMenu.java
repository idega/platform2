package is.idega.idegaweb.member.isi.block.reports.presentation.inputhandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.DropDownMenuInputHandler;
import com.idega.util.IWTimestamp;
/**
 * A presentation object for dynamic reports. Select an age (1-123).
 * 
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 * 
 */
public class ReportGeneratorAgeDropDownMenu extends DropDownMenuInputHandler  {

	private static final int youngest = 1;

	private static final int oldest = 123;
	
	protected static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
	
	private final static String ALL_AGES_VALUE = "all_ages";
	
	public ReportGeneratorAgeDropDownMenu() {
		super();
	}

	public void main(IWContext iwc) throws Exception {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		addMenuElement(ALL_AGES_VALUE,iwrb.getLocalizedString("AgeDropdownmenu.all_ages", "All ages"));
		for (int i = youngest; i <= oldest; i++) {
			String age = Integer.toString(i);
			addMenuElement(i, age);
		}
		String selectedElement = getSelectedElementValue();
		if (selectedElement == null || selectedElement.length() == 0) {
			setSelectedElement(ALL_AGES_VALUE);
		}	
	}
	
	public Collection getAllAgesCollection(){
		List allAges = new ArrayList(oldest - youngest + 1); 
		for (int i = youngest; i <= oldest; i++) {
			allAges.add(new Integer(i));
			
		}
		return allAges;
	}

	/**
	 * @return the year, Integer
	 *  
	 */
	public Object getResultingObject(String[] values, IWContext iwc) throws Exception {
		Collection ages = null;
		if (values != null && values.length > 0) {
			ages = new ArrayList();
			if(values.length==1 && values[0].equals(ALL_AGES_VALUE)) {
				ages = getAllAgesCollection();
			} 
			else {
				for(int i=0; i<values.length; i++) {
					Integer age = new Integer(values[i]);
					ages.add(age);
				}
			}
		} else {
			ages = getAllAgesCollection();
		}
		return ages;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.business.InputHandler#getDisplayNameOfValue(java.lang.String, com.idega.presentation.IWContext)
	 */
	public String getDisplayForResultingObject(Object value, IWContext iwc) {
		if (value != null) {
			Iterator iter = ((Collection) value).iterator();
			StringBuffer ages = new StringBuffer();
			int numberOfAges = ((Collection) value).size();
			int counter = 0;

			while (iter.hasNext()) {
				Integer age = (Integer) iter.next();
				ages.append(age);
				counter++;
				if (counter < numberOfAges) {
					ages.append(",");
				}
			}
			Collection allAges = getAllAgesCollection();
			if (! ((Collection) value).containsAll(allAges)) {
				return ages.toString();
			}
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

	/* (non-Javadoc)
	 * @see com.idega.business.MultiInputHandler#getHandlerObject(java.lang.String, java.util.List, com.idega.presentation.IWContext)
	 */
	public PresentationObject getHandlerObject(String name, Collection values, IWContext iwc) {
		if (values == null || values.isEmpty()) {
			return getHandlerObject(name, (String)  null, iwc);
		}
		// because the PresentationObject is a DropDownMenu only one selection can be done
		Collection allAges = getAllAgesCollection();
		String selection;
		if (values.containsAll(allAges))		{
			selection = ALL_AGES_VALUE;
		}
		else {
			// select only one value
			selection = (String) Collections.min(values);
		}
		return getHandlerObject(name, selection, iwc);
	}

	/* (non-Javadoc)
	 * @see com.idega.business.InputHandler#convertResultingObjectToType(java.lang.Object, java.lang.String)
	 */
	public Object convertSingleResultingObjectToType(Object value, String className) {
		
		int age = ((Integer) value).intValue();
		
		IWTimestamp stamp = IWTimestamp.RightNow();
	
		stamp.addYears(-age);
		stamp.setMonth(1);
		stamp.setDay(1);
		stamp.setAsDate();
		
		return stamp.toSQLDateString();
	}
}
