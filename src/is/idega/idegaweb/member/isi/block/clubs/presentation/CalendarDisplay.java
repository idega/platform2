/*
 * Created on Apr 3, 2004
 */
package is.idega.idegaweb.member.isi.block.clubs.presentation;

import com.idega.block.cal.presentation.CalendarView;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;

/**
 * Description: <br>
 * Copyright: Idega Software 2004 <br>
 * Company: Idega Software <br>
 * @author <a href="mailto:birna@idega.is">Birna Iris Jonsdottir</a>
 */
public class CalendarDisplay extends Block{
	public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
	
	public CalendarDisplay() {
		super();
	}
	
	public void main(IWContext iwc) {
		String clubIDString = iwc.getParameter(ClubInfoBar.PARAM_NAME_GROUP_ID); 

		
		CalendarView cal = new CalendarView();
		if(clubIDString != null && !clubIDString.equals("")) {
			cal.setViewInGroupID(Integer.parseInt(clubIDString));
		}
		Table table = new Table();
		table.add(cal);
		add(table);
		
	}
	
	
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
}
