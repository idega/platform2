package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.SortedSet;
import se.idega.idegaweb.commune.childcare.data.ChildCareQueue;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.HiddenInput;

class ChildCareQueueTable extends Table {
	
	/*private static Text HEADER_YOUR_CHOICE;
	private static Text HEADER_OFFER;
	private static Text HEADER_PROGNOSE;
	private static Text HEADER_QUEUE_INFO;
	private static Text HEADER_YES;
	private static Text HEADER_YES_BUT;
	private static Text HEADER_NO;
	private static String GRANTED;*/
	
	private static boolean _initializeStatics = false;
	private static ChildCareQueueUpdateTable _page;
	
	final static String[] REQUEST_INFO = new String[] { "ccatp1_request_info", "Request info" };
	
	private void initConstants(ChildCareQueueUpdateTable page) {
		if (!_initializeStatics) {
			_page = page;
			/*HEADER_YOUR_CHOICE = page.getLocalHeader("ccatp1_your_choice", "Your Choice");
			HEADER_OFFER = page.getLocalHeader("ccatp1_offer", "Offer");
			HEADER_PROGNOSE = page.getLocalHeader("ccatp1_prognose", "Prognoses");
			HEADER_QUEUE_INFO = page.getLocalHeader("ccatp1_queue_info", "Request queue information");
			HEADER_YES = page.getLocalHeader("ccatp1_yes", "Yes");
			HEADER_YES_BUT = page.getLocalHeader("ccatp1_yes_but", "No, but don't delete from queue");
			HEADER_NO = page.getLocalHeader("ccatp1_no", "No");
			GRANTED = page.localize("ccatp1_granted", "You have received an offer from ").toString();*/
			_initializeStatics = true;
		}
	}
	public ChildCareQueueTable(ChildCareQueueUpdateTable page, SortedSet choices) throws RemoteException {
		super(7, choices.size() + 1);
		initConstants(page);
		initTable();
		System.out.println("Choices: " + choices);
		Iterator i = choices.iterator();
		int row = 2;
		while (i.hasNext()) {
			ChildCareQueue queueSelection = ((ComparableQueue) i.next()).getQueue();
			queueSelection.getChoiceNumber();
			String id = ((Integer) queueSelection.getPrimaryKey()).toString();
			String name = queueSelection.getProviderName();
			String choice = "" + queueSelection.getChoiceNumber();
			String area = queueSelection.getSchoolAreaName();
			String date = queueSelection.getQueueDate().toString();
			addToTable(row, id, choice, name, area, date, "0");
			row++;
		}
	}
	/**
	 * Method addToTable.
	 * @param table
	 * @param row 
	 * @param name
	 * @param status
	 * @param prognosis
	 */
	private void addToTable(
		int tRow,
		String id,
		String choice,
		String name,
		String area,
		String date,
		String placeNr) {
		row = tRow;
		int index = row - 1; //row=2 for first row because of heading is in row 1
		add(new HiddenInput(CCConstants.APPID + index, id));
		textColor = "black";
		col = 1;
		addStr(choice);
		addStr(name);
		addStr(area);
		addStr(date);
		addStr(placeNr);
		CheckBox cb = new CheckBox();
		add(cb, col++, row);
		if (row % 2 == 0)
			setRowColor(row++, _page.getZebraColor1());
		else
			setRowColor(row++, _page.getZebraColor2());
	}
	String textColor = "black";
	int row, col = 1;
	private void addStr(String str) {
		if (str != null) {
			Text t = _page.getSmallText(str);
			t.setStyleAttribute("color:" + textColor);
			add(t, col++, row);
		}
	}
	/**
	 * Method createTable.
	 * @return Table
	 */
	private void initTable() {
		//		Table table = new Table(7, rows + 1); //Heading
		//		setBorder(1);
		//		setBorderColor("GREEN");
		setRowColor(1, _page.getHeaderColor());
		setCellspacing(2);
		setCellpadding(4);
		//Heading
		col=1;
		add(_page.getLocalHeader("Nr","Nr"),col++,1);
		add(_page.getLocalHeader("Anordnare","Anordnare"),col++,1);
		add(_page.getLocalHeader("Område","Område"),col++,1);
		add(_page.getLocalHeader("Ködatum","Ködatum"),col++,1);
		add(_page.getLocalHeader("Köplats","Köplats"),col++,1);
		add(_page.getLocalHeader("Val","Val"),col++,1);
/*
		add(HEADER_YOUR_CHOICE, 1, 1);
		add(HEADER_OFFER, 2, 1);
		add(HEADER_PROGNOSE, 3, 1);
		add(HEADER_QUEUE_INFO, 4, 1);
		add(HEADER_YES, 5, 1);
		add(HEADER_YES_BUT, 6, 1);
		add(HEADER_NO, 7, 1);
*/
	}
}
