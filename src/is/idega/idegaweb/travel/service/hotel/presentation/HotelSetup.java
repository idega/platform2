/*
 * Created on Jun 16, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package is.idega.idegaweb.travel.service.hotel.presentation;

import java.util.Collection;
import java.util.Iterator;


import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;

import is.idega.idegaweb.travel.presentation.TravelManager;
import is.idega.idegaweb.travel.service.hotel.data.RoomType;
import is.idega.idegaweb.travel.service.hotel.data.RoomTypeHome;

/**
 * @author gimmi
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class HotelSetup extends TravelManager {

	private IWResourceBundle iwrb;
	
	private String ACTION = "hs_ac";
	private String ACTION_SAVE_ROOM_TYPES = "hs_asrt";
	private String PARAMETER_NAME = "hs_prm_n";
	private String PARAMETER_ROOM_TYPE_ID = "hs_prm_rtid";

	public void main(IWContext iwc) throws Exception {
		super.main(iwc);	
		iwrb = getTravelSessionManager(iwc).getIWResourceBundle();
		add(Text.BREAK);
		roomTypeSetup(iwc);
	}
	
	private void roomTypeSetup(IWContext iwc) {
		
		String action = iwc.getParameter(ACTION);
		if (action != null && action.equals(ACTION_SAVE_ROOM_TYPES)) {
			handleRoomTypeInsert(iwc);
		}
		
		drawRoomTypeSetup();
	}
	
	private void handleRoomTypeInsert(IWContext iwc) {
		String[] name = iwc.getParameterValues(PARAMETER_NAME);
		String[] ids = iwc.getParameterValues(PARAMETER_ROOM_TYPE_ID);
		
		if (ids != null) {
			try {
				RoomTypeHome rth;
					rth = (RoomTypeHome) IDOLookup.getHome(RoomType.class);
				RoomType roomType;
				for (int i = 0 ; i < ids.length; i++) {
					if ("-1".equals(ids[i])) {
						if ( !"".equals(name[i]) ) {
							roomType = rth.create();
							roomType.setName(name[i]);
							roomType.setIsValid(true);
							roomType.store();
						}
					}else {
						roomType = rth.findByPrimaryKey(new Integer(ids[i]));
						if ("".equals(name[i])) {
							roomType.setIsValid(false);
						}else {
							roomType.setName(name[i]);	
							roomType.setIsValid(true);
						}
						roomType.store();
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
	}
	
	private void drawRoomTypeSetup() {
		Form form = new Form();
		Table table = getTable();
		form.add(table);
		int row = 1;
		int empty = 1;
		
		
		
		table.add(getHeaderText(iwrb.getLocalizedString("travel.setup_room_types","Setup room types")), 1, row);
		table.setRowColor(row, backgroundColor);
//		++row;
		try {
			RoomTypeHome rth = (RoomTypeHome) IDOLookup.getHome(RoomType.class);
			Collection roomTypes = rth.findAll();
			RoomType roomType;
			TextInput name;
			 
//			table.add("name", 1, row);
			if (roomTypes != null) {
				Iterator iter = roomTypes.iterator();
				while (iter.hasNext()) {
					++row;
					roomType = (RoomType) iter.next();
					table.add(new TextInput(PARAMETER_NAME, roomType.getName()), 1, row);
					table.add(new HiddenInput(PARAMETER_ROOM_TYPE_ID, roomType.getPrimaryKey().toString()), 1, row);
					table.setRowColor(row, GRAY);
				}	
			}
			++row;
			table.add(new TextInput(PARAMETER_NAME), 1, row);
			table.add(new HiddenInput(PARAMETER_ROOM_TYPE_ID, "-1"), 1, row);
			table.setRowColor(row, GRAY);
			
			++row;
			table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
			table.add(new SubmitButton(iwrb.getImage("/buttons/save.gif"), ACTION, ACTION_SAVE_ROOM_TYPES), 1, row);
			table.setRowColor(row, GRAY);
		}catch (Exception fe) {
			++row;
			table.add(super.getText(iwrb.getLocalizedString("travel.error_getting_room_types","Error getting room types")), 1, row);
		} 
		add(form);
	}
}
