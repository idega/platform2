/*
 * Created on 4.3.2004
 */
package is.idega.idegaweb.golf.clubs.presentation;

import is.idega.idegaweb.golf.entity.Subscription;
import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.entity.UnionHome;
import is.idega.idegaweb.golf.presentation.GolfBlock;

import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;

/**
 * @author laddi
 */
public class MemberClubApplication extends GolfBlock {

	public void main(IWContext modinfo) throws Exception {
		getParentPage().setTitle("Umsóknareyðublað");

		String mode = modinfo.getParameter("mode");
		if (mode == null) {
			mode = "";
		}

		String union_id = modinfo.getParameter("union_id");
		if (union_id == null) {
			union_id = "3";
		}

		if (mode.equalsIgnoreCase("")) {

			Union union = ((UnionHome) IDOLookup.getHomeLegacy(Union.class)).findByPrimaryKey(Integer.parseInt(union_id));

			Text unionText = new Text("Umsókn um aðild að " + union.getAbbrevation());
			unionText.setBold();
			unionText.setFontSize(3);

			Form myForm = new Form();
			myForm.setMethod("post");
			myForm.add(new HiddenInput("union_id", union_id));

			TextInput nafn = new TextInput("nafn");
			nafn.setLength(50);
			TextInput kennitala = new TextInput("kennitala");
			kennitala.setMaxlength(10);
			kennitala.setLength(10);
			TextInput netfang = new TextInput("netfang");
			netfang.setLength(20);
			TextInput heimili = new TextInput("heimili");
			heimili.setLength(20);
			TextInput postnumer = new TextInput("postnumer");
			postnumer.setLength(3);
			postnumer.setMaxlength(3);
			TextInput baejarfelag = new TextInput("baejarfelag");
			baejarfelag.setLength(15);
			TextInput heimasimi = new TextInput("heimasimi");
			heimasimi.setLength(7);
			TextInput vinnustadur = new TextInput("vinnustadur");
			vinnustadur.setLength(25);
			TextInput vinnusimi = new TextInput("vinnusimi");
			vinnusimi.setLength(7);
			CheckBox nylidi = new CheckBox("nylidi");
			CheckBox iklubbi = new CheckBox("iklubbi");
			TextInput klubbur = new TextInput("klubbur");
			klubbur.setLength(3);
			TextInput forgjof = new TextInput("forgjof");
			forgjof.setLength(4);
			forgjof.setMaxlength(4);
			TextArea annad = new TextArea("annad", 35, 4);
			annad.setWrap(true);

			Table myTable = new Table();
			myTable.setAlignment("center");
			myTable.setBorder(0);
			myTable.setCellpadding(3);
			myTable.setCellspacing(3);
			myTable.mergeCells(1, 1, 2, 1);

			myTable.add(unionText, 1, 1);

			myTable.addText("Nafn:", 1, 3);
			myTable.add(nafn, 2, 3);

			myTable.addText("Kennitala:", 1, 4);
			myTable.add(kennitala, 2, 4);

			myTable.addText("Heimilisfang:", 1, 5);
			myTable.add(heimili, 2, 5);

			myTable.addText("Póstnúmer/Staður:", 1, 6);
			myTable.add(postnumer, 2, 6);
			myTable.addText(" ", 2, 6);
			myTable.add(baejarfelag, 2, 6);

			myTable.addText("Heimasími:", 1, 7);
			myTable.add(heimasimi, 2, 7);

			myTable.addText("Netfang:", 1, 8);
			myTable.add(netfang, 2, 8);

			myTable.addText("Vinnustaður:", 1, 9);
			myTable.add(vinnustadur, 2, 9);

			myTable.addText("Vinnusími:", 1, 10);
			myTable.add(vinnusimi, 2, 10);

			myTable.addText("Nýliði:", 1, 11);
			myTable.add(nylidi, 2, 11);

			myTable.addText("Skráður í klúbb:", 1, 12);
			myTable.add(iklubbi, 2, 12);
			myTable.addText(" ef já, hvaða klúbb: ", 2, 12);
			myTable.add(klubbur, 2, 12);

			myTable.addText("Forgjöf:", 1, 13);
			myTable.add(forgjof, 2, 13);

			myTable.addText("Annað:", 1, 14);
			myTable.add(annad, 2, 14);

			myTable.add(new CloseButton("Loka"), 1, 15);
			myTable.add(new SubmitButton("mode", "Senda"), 2, 15);

			myTable.setColumnAlignment(1, "right");
			myTable.setAlignment(1, 1, "center");
			myTable.setAlignment(1, 15, "left");
			myTable.setAlignment(2, 15, "right");
			myTable.setColumnVerticalAlignment(1, "top");

			myForm.addBreak();
			myForm.add(myTable);

			boolean addForm = true;
			if (Integer.parseInt(union_id) == 2008)
				addForm = false;

			if (addForm)
				add(myForm);
			else
				add(new Text("Ekki er tekid vid umsoknum ad svo stoddu, vinsamlegast hafid samband vid klubbinn fyrir frekari upplysingar."));
		}

		if (mode.equalsIgnoreCase("senda")) {

			String nafn = modinfo.getParameter("nafn");
			if (nafn == null)
				nafn = "";
			String kennitala = modinfo.getParameter("kennitala");
			if (kennitala == null)
				kennitala = "";
			String netfang = modinfo.getParameter("netfang");
			if (netfang == null)
				netfang = "";
			String heimili = modinfo.getParameter("heimili");
			if (heimili == null)
				heimili = "";
			String postnumer = modinfo.getParameter("postnumer");
			if (postnumer == null)
				postnumer = "";
			String baejarfelag = modinfo.getParameter("baejarfelag");
			if (baejarfelag == null)
				baejarfelag = "";
			String heimasimi = modinfo.getParameter("heimasimi");
			if (heimasimi == null)
				heimasimi = "";
			String vinnustadur = modinfo.getParameter("vinnustadur");
			if (vinnustadur == null)
				vinnustadur = "";
			String vinnusimi = modinfo.getParameter("vinnusimi");
			if (vinnusimi == null)
				vinnusimi = "";
			String nylidi = modinfo.getParameter("nylidi");
			if (nylidi == null)
				nylidi = "Nei";
			else
				nylidi = "Já";
			String iklubbi = modinfo.getParameter("iklubbi");
			if (iklubbi == null)
				iklubbi = "Nei";
			else
				iklubbi = "Já";
			String klubbur = modinfo.getParameter("klubbur");
			if (klubbur == null)
				klubbur = "";
			String forgjof = modinfo.getParameter("forgjof");
			if (forgjof == null)
				forgjof = "-";
			String annad = modinfo.getParameter("annad");
			if (annad == null)
				annad = "";

			Subscription subscribe = (Subscription) IDOLookup.createLegacy(Subscription.class);
			subscribe.setUnionID(Integer.parseInt(union_id));
			subscribe.setName(nafn);
			subscribe.setSSC(kennitala);
			subscribe.setEmail(netfang);
			subscribe.setHome(heimili);
			subscribe.setZip(postnumer);
			subscribe.setState(baejarfelag);
			subscribe.setHPhone(heimasimi);
			subscribe.setWPhone(vinnusimi);
			subscribe.setWorkplace(vinnustadur);
			subscribe.setRookie(nylidi);
			subscribe.setInClub(iklubbi);
			subscribe.setClub(klubbur);
			subscribe.setHandicap(forgjof);
			subscribe.setMisc(annad);

			subscribe.insert();
			if (union_id.equals("81")) {

				//for mister gusti
				try {
					StringBuffer umsokn = new StringBuffer();
					umsokn.append("Nafn: ");
					umsokn.append(nafn);
					umsokn.append("\nKennitala: ");
					umsokn.append(kennitala);
					umsokn.append("\nNetfang: ");
					umsokn.append(netfang);
					umsokn.append("\nHeimili: ");
					umsokn.append(heimili);
					umsokn.append("\nPostnúmer: ");
					umsokn.append(postnumer);
					umsokn.append("\nBæjarfélag: ");
					umsokn.append(baejarfelag);
					umsokn.append("\nHeimasimi: ");
					umsokn.append(heimasimi);
					umsokn.append("\nVinnusimi: ");
					umsokn.append(vinnusimi);
					umsokn.append("\nNýliði: ");
					umsokn.append(nylidi);
					umsokn.append("\nEr í klúbbi: ");
					umsokn.append(iklubbi);
					umsokn.append("\nKlúbbur: ");
					umsokn.append(klubbur);
					umsokn.append("\nAnnað: ");
					umsokn.append(annad);

					umsokn.append("\n\nUmsókn búin til af golf.is.");

					com.idega.util.SendMail.send(netfang, "keilir@isholf.is", "", "", "mail.idega.is", "Umsókn af golf.is", umsokn.toString());
					//com.idega.util.SendMail.send(netfang,"eiki@idega.is","","","mail.idega.is","Umsókn
					// af golf.is",umsokn.toString());

				}
				catch (Exception e) {

				}
			}//end if
			Table myTable = new Table(1, 3);
			myTable.setCellpadding(3);
			myTable.setCellspacing(3);
			myTable.setWidth(250);
			myTable.setColumnAlignment(1, "center");
			myTable.setAlignment("center");

			Text reply = new Text("Takk fyrir!");
			reply.setBold();
			reply.setFontSize(3);

			Text reply2 = new Text("Umsókn þín hefur verið móttekin og verður afgreidd við fyrsta tækifæri.");
			CloseButton loka = new CloseButton("Loka skráningu");

			myTable.add(reply, 1, 1);
			myTable.add(reply2, 1, 2);
			myTable.add(loka, 1, 3);

			add(myTable);
		}
	}

}