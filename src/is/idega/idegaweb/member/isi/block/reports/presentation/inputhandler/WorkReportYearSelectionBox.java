/*
 * Created on Dec 18, 2003
 */
package is.idega.idegaweb.member.isi.block.reports.presentation.inputhandler;

/**
 * Description: <br>
 * Copyright: Idega Software 2003 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:birna@idega.is">Birna Iris Jonsdottir</a>
 */
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.idega.business.InputHandler;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.SelectionBox;
import com.idega.util.IWTimestamp;

public class WorkReportYearSelectionBox extends SelectionBox implements InputHandler {
	
	protected static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
	
	private int year = IWTimestamp.getTimestampRightNow().getYear();
	
	
	/**
	 * Creates a new <code>RegionalUnionSelectionBox</code> with all regional unions.
	 * @param name	The name of the <code>RegionalUnionSelectionBox</code>
	 */
	public WorkReportYearSelectionBox() {
		super();
	}
	
	public PresentationObject getHandlerObject(String name,String value,IWContext iwc) {
		this.setName(name);
		SelectionBox yearInput=null;
		try {
			yearInput = getYearSelectionBox();
			
			yearInput.setName(name);
			if(value!=null){
				yearInput.setSelectedElement(value);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return yearInput;
	}
		
	public Object getResultingObject(String[] values, IWContext iwc) throws Exception{
		Collection result = new ArrayList();
		if(values == null || values.length==0) {
			IWTimestamp stamp = IWTimestamp.RightNow();
	
			int currentYear = stamp.getYear();
			int beginningYear = 2001;//Because we have no older data, could also be an application setting
			
			values = new String[currentYear-beginningYear+1];
			int count = 0;
			
			for (int i = beginningYear; i <= currentYear; i++) {
				values[count] = Integer.toString(i);
				count++;
			}
		}
		
		for(int i=0; i<values.length; i++) {
			result.add(values[i]);
		}
		
		return result;
	}
	
	public String getDisplayForResultingObject(Object value, IWContext iwc) {
		String result = "";
		if(value instanceof String){
			result = (String) value;
		} else if (value instanceof String[]) {
			String[] values = ((String[])value);
			int count = values.length;
			StringBuffer buf = new StringBuffer(5*count);
			for(int i=0; i<count; i++) {
				if(i>0) {
					buf.append(",");
				}
				buf.append(values[i]);
			}
			result = buf.toString();
		}
		return result;
	}
		
	public SelectionBox getYearSelectionBox() {
		SelectionBox dateSelector = new SelectionBox();
		IWTimestamp stamp = IWTimestamp.RightNow();

		int currentYear = stamp.getYear();
		int beginningYear = 2001;//Because we have no older data, could also be an application setting

		for (int i = beginningYear; i <= currentYear; i++) {
			dateSelector.addMenuElement(i,Integer.toString(i));
		}
		
		return dateSelector;
  	
	}		
	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#getBundleIdentifier()
	 */
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	public PresentationObject getHandlerObject(String name, Collection values, IWContext iwc) {
		String value = (String) Collections.min(values);
		return getHandlerObject(name, value, iwc);
	}


	/* (non-Javadoc)
	 * @see com.idega.business.InputHandler#convertResultingObjectToType(java.lang.Object, java.lang.String)
	 */
	public Object convertSingleResultingObjectToType(Object value, String className) {
		return value;
	}
}
	
	
	


