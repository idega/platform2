/*
 * Created on 4.3.2004
 */
package is.idega.idegaweb.golf.startingtime.presentation;

import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import com.idega.data.EntityFinder;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.Form;
import is.idega.idegaweb.golf.entity.Startingtime;
import is.idega.idegaweb.golf.presentation.GolfBlock;
import is.idega.idegaweb.golf.startingtime.data.TeeTime;

/**
 * @author laddi
 */
public class StartingTimeCopy extends GolfBlock {

	public void main(IWContext modinfo) throws Exception {
		Object obj = modinfo.getParameter("param");
		String param = null;
		if (obj != null) {
			param = (String) obj;
		}
		else {
			param = "Z";
		}

		Form form = new Form();

		switch (param.charAt(0)) {
			case 'a' :
				putListsInSession(modinfo);
				form.add(new com.idega.presentation.ui.SubmitButton(" skipta upp ", "param", "b"));
				break;
			case 'b' :
				sortLists(modinfo);
				form.add(new com.idega.presentation.ui.SubmitButton("  copy  ", "param", "c"));
				break;
			case 'c' :
				copy(modinfo);
				form.add(new com.idega.presentation.ui.SubmitButton(" Hreinsa  ", "param", "d"));
				break;
			case 'd' :
				remove(modinfo);
				form.add(new com.idega.presentation.ui.SubmitButton(" til Baka  ", "param", "e"));
				break;
			default :
				form.add(new com.idega.presentation.ui.SubmitButton(" Sækja í grunn ", "param", "a"));
				break;
		}

		add(form);
	}

	public void putListsInSession(IWContext modinfo) throws SQLException {
		Startingtime stTime = (Startingtime) IDOLookup.instanciateEntity(Startingtime.class);
		List from = EntityFinder.findAll(stTime, "select * from " + stTime.getEntityName() + " where startingtime_date > '2001-05-15'");
		List notFrom = EntityFinder.findAll(stTime, "select * from tournament_round_startingtime trs, startingtime st where trs.startingtime_id = st.startingtime_id");

		modinfo.setSessionAttribute("from", from);
		modinfo.setSessionAttribute("notFrom", notFrom);
		System.err.println(" ends ");
	}

	public void sortLists(IWContext modinfo) {
		List from = (List) modinfo.getSessionAttribute("from");
		List notFrom = (List) modinfo.getSessionAttribute("notFrom");

		System.err.println("from : " + from.size());
		System.err.println("notfrom : " + notFrom.size());

		Vector vector = new Vector();

		for (int i = 0; i < notFrom.size(); i++) {
			from.remove(notFrom.get(i));
			System.err.println(i);
		}

		System.err.println("vector size: " + from.size());
		modinfo.setSessionAttribute("vector", from);
		System.err.println("ends ###########################");

	}

	public void copy(IWContext modinfo) throws SQLException {
		List toCopy = (List) modinfo.getSessionAttribute("vector");
		//TeeTime t = new TeeTime();
		Startingtime s = null;
		System.err.println("copy size " + toCopy.size());

		for (int i = 0; i < toCopy.size(); i++) {
			s = (Startingtime) toCopy.get(i);

			TeeTime t = (TeeTime) IDOLookup.createLegacy(TeeTime.class);
			//t.setID(s.getID());
			t.setCardName(s.getCardName());
			t.setCardNum(s.getCardNum());
			t.setClubName(s.getClubName());
			t.setFieldID(s.getFieldID());
			t.setGroupNum(s.getGroupNum());
			t.setHandicap(s.getHandicap());
			t.setMemberID(s.getMemberID());
			t.setOwnerID(s.getOwnerID());
			t.setPlayerName(s.getPlayerName());
			t.setStartingtimeDate(s.getStartingtimeDate());

			t.insert();
		}

		System.err.println("###################### ends ###########################");
	}

	public void remove(IWContext modinfo) {
		modinfo.removeSessionAttribute("from");
		modinfo.removeSessionAttribute("notFrom");
		modinfo.removeSessionAttribute("vector");
	}

}