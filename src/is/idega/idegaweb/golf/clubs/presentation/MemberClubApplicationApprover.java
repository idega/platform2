/*
 * Created on 4.3.2004
 */
package is.idega.idegaweb.golf.clubs.presentation;

import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import is.idega.idegaweb.golf.entity.Subscription;
import is.idega.idegaweb.golf.entity.SubscriptionHome;
import is.idega.idegaweb.golf.presentation.GolfBlock;

/**
 * @author laddi
 */
public class MemberClubApplicationApprover extends GolfBlock {

	public void main(IWContext modinfo) throws Exception {
		getParentPage().setTitle("Innsendar umsóknir");

		String subscriber = modinfo.getParameter("subscriber");

		if (subscriber != null) {
			Subscription deleteSubscriber = ((SubscriptionHome) IDOLookup.getHomeLegacy(Subscription.class)).findByPrimaryKey(Integer.parseInt(subscriber));
			deleteSubscriber.delete();
		}

		String union_id = modinfo.getParameter("union_id");
		if (union_id == null) {
			union_id = "3";
		}

		Table content = new Table();
		content.setAlignment("center");
		content.setCellpadding(3);
		content.setCellspacing(3);
		content.setBorder(0);
		content.setWidth("100%");

		Subscription[] subscribe = (Subscription[]) ((Subscription) IDOLookup.instanciateEntity(Subscription.class)).findAllByColumnEquals("union_id", union_id);

		for (int a = 0; a < subscribe.length; a++) {

			Text name2 = new Text(subscribe[a].getName());
			name2.setFontSize(1);
			Text kt2 = new Text(subscribe[a].getSSC());
			kt2.setFontSize(1);
			Text home2 = new Text(subscribe[a].getHome());
			home2.setFontSize(1);
			Text zip2 = new Text(subscribe[a].getZip() + " " + subscribe[a].getState());
			zip2.setFontSize(1);
			Text phone2 = new Text(subscribe[a].getHPhone());
			phone2.setFontSize(1);
			Text email2 = new Text(subscribe[a].getEmail());
			email2.setFontSize(1);
			Text workplace2 = new Text(subscribe[a].getWorkplace());
			workplace2.setFontSize(1);
			Text work2 = new Text(subscribe[a].getWPhone());
			work2.setFontSize(1);
			Text rookie2 = new Text(subscribe[a].getRookie());
			rookie2.setFontSize(1);
			Text inclub2 = new Text(subscribe[a].getInClub());
			inclub2.setFontSize(1);
			Text club2 = new Text(subscribe[a].getClub());
			club2.setFontSize(1);
			Text handicap2 = new Text(subscribe[a].getHandicap());
			handicap2.setFontSize(1);
			Text misc2 = new Text(subscribe[a].getMisc());
			misc2.setFontSize(1);

			content.add(name2, 1, a + 2);
			content.add(kt2, 2, a + 2);
			content.add(home2, 3, a + 2);
			content.add(zip2, 4, a + 2);
			content.add(phone2, 5, a + 2);
			content.add(email2, 6, a + 2);
			content.add(workplace2, 7, a + 2);
			content.add(work2, 8, a + 2);
			content.add(rookie2, 9, a + 2);
			content.add(inclub2, 10, a + 2);
			content.add(club2, 11, a + 2);
			content.add(handicap2, 12, a + 2);
			content.add(misc2, 13, a + 2);

			Link myLink = new Link("Eyða", "/clubs/request_member.jsp");
			myLink.addParameter("subscriber", "" + subscribe[a].getID());
			myLink.addParameter("union_id", union_id);
			myLink.setFontSize(1);

			content.add(myLink, 13, a + 2);
		}

		Text name = new Text("Nafn");
		name.setBold();
		name.setFontSize(1);
		Text home = new Text("Heimili");
		home.setBold();
		home.setFontSize(1);
		Text kt = new Text("Kennitala");
		kt.setBold();
		kt.setFontSize(1);
		Text zip = new Text("Póstfang");
		zip.setBold();
		zip.setFontSize(1);
		Text phone = new Text("Heimasími");
		phone.setBold();
		phone.setFontSize(1);
		Text email = new Text("Netfang");
		email.setBold();
		email.setFontSize(1);
		Text workplace = new Text("Vinnustaður");
		workplace.setBold();
		workplace.setFontSize(1);
		Text work = new Text("Vinnusími");
		work.setBold();
		work.setFontSize(1);
		Text rookie = new Text("Nýliði");
		rookie.setBold();
		rookie.setFontSize(1);
		Text inclub = new Text("Í klúbbi");
		inclub.setBold();
		inclub.setFontSize(1);
		Text club = new Text("Klúbbur");
		club.setBold();
		club.setFontSize(1);
		Text handicap = new Text("Forgjöf");
		handicap.setBold();
		handicap.setFontSize(1);
		Text misc = new Text("Annað");
		misc.setBold();
		misc.setFontSize(1);

		content.add(name, 1, 1);
		content.add(kt, 2, 1);
		content.add(home, 3, 1);
		content.add(zip, 4, 1);
		content.add(phone, 5, 1);
		content.add(email, 6, 1);
		content.add(workplace, 7, 1);
		content.add(work, 8, 1);
		content.add(rookie, 9, 1);
		content.add(inclub, 10, 1);
		content.add(club, 11, 1);
		content.add(handicap, 12, 1);
		content.add(misc, 13, 1);

		add(content);
	}

}