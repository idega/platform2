/*
 * Created on 4.3.2004
 */
package is.idega.idegaweb.golf.field.presentation;

import is.idega.idegaweb.golf.block.text.presentation.TextReader;
import is.idega.idegaweb.golf.entity.Field;
import is.idega.idegaweb.golf.entity.FieldHome;
import is.idega.idegaweb.golf.entity.FieldImage;
import is.idega.idegaweb.golf.entity.HoleText;
import is.idega.idegaweb.golf.entity.Tee;
import is.idega.idegaweb.golf.entity.TeeColor;
import is.idega.idegaweb.golf.entity.TeeColorHome;
import is.idega.idegaweb.golf.entity.TeeImage;
import is.idega.idegaweb.golf.presentation.GolfBlock;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

import javax.ejb.FinderException;

import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.Window;

/**
 * @author laddi
 */
public class FieldOverview extends GolfBlock {

	String field_admin_url = "/field/index.jsp";

	public void main(IWContext modinfo) throws Exception {
		String union_id = modinfo.getParameter("union_id");
		if (union_id == null) {
			union_id = (String) modinfo.getSessionAttribute("golf_union_id");
		}

		String hole_number = modinfo.getParameter("hole_number");
		String field_id = modinfo.getParameter("field_id");
		if (field_id == null)
			field_id = "50";

		Table contentTable = new Table();
		contentTable.setWidth("100%");

		if (field_id != null && hole_number == null) {
			Field field = ((FieldHome) IDOLookup.getHomeLegacy(Field.class)).findByPrimaryKey(Integer.parseInt(field_id));
			Table myTable = getFieldInfo(field, field_id);

			fillContentTable(field, contentTable, myTable, field_id);
		}

		else if (field_id != null && hole_number != null) {
			Field field = ((FieldHome) IDOLookup.getHomeLegacy(Field.class)).findByPrimaryKey(Integer.parseInt(field_id));
			Table myTable = getHoleInfo(field, field_id, hole_number);

			contentTable.add(myTable, 1, 1);
		}

		if (isAdmin()) {
			Window gluggi = new Window("Vallarstjórinn", 1000, 600, field_admin_url);
			Form form = new Form(gluggi);
			//                        Form form = new Form("/field/index.jsp");
			form.add(new HiddenInput("field_id", field_id + ""));
			form.add(new HiddenInput("union_id", union_id));
			form.add(new HiddenInput("redir", "Brautastjórinn"));
			form.add(new SubmitButton(new Image("/pics/formtakks/brautarstjorinn.gif", "", 145, 19)));
			add(form);
		}

		add(contentTable);
	}

	public String getTeeColor(int teeColorId) throws IOException {

		String litur = "";

		switch (teeColorId) {
			case 1 :
				litur = "FFFFFF";
				break;

			case 2 :
				litur = "EEEB86";
				break;

			case 3 :
				litur = "8B86EE";
				break;

			case 4 :
				litur = "EE8686";
				break;

			case 5 :
				litur = "8B86EE";
				break;

			case 6 :
				litur = "EE8686";
				break;

		}

		return litur;
	}

	public Table getFieldInfo(Field field, String field_id) throws IOException, SQLException, FinderException {

		Table myTable = new Table();

		Tee[] teeColor = (Tee[]) ((Tee) IDOLookup.instanciateEntity(Tee.class)).findAll("select distinct tee_color_id from tee where field_id='" + field.getID() + "'");

		for (int a = 0; a < teeColor.length; a++) {

			Tee[] tee = (Tee[]) ((Tee) IDOLookup.instanciateEntity(Tee.class)).findAllByColumnOrdered("field_id", String.valueOf(field.getID()), "tee_color_id", String.valueOf(teeColor[a].getIntColumnValue("tee_color_id")), "hole_number");

			Text teeColorName = new Text(((TeeColorHome) IDOLookup.getHomeLegacy(TeeColor.class)).findByPrimaryKey(teeColor[a].getIntColumnValue("tee_color_id")).getName() + "&nbsp;");
			teeColorName.setFontSize(1);

			myTable.add(teeColorName, 1, a + 2);

			int teeLength = 0;
			int par = 0;
			int holePar = 0;

			for (int b = 0; b < tee.length; b++) {

				int holeLength = tee[b].getIntColumnValue("tee_length");
				Text lengthText = new Text(String.valueOf(holeLength));
				lengthText.setFontSize(1);

				myTable.add(lengthText, b + 2, a + 2);
				teeLength += tee[b].getIntColumnValue("tee_length");

				if (a == 0) {
					Text holeNumber = new Text(tee[b].getStringColumnValue("hole_number"));
					holeNumber.setFontSize(1);
					holeNumber.setFontColor("FFFFFF");

					Link holeView = new Link(holeNumber, "/clubs/field.jsp");
					holeView.setFontColor("#FFFFFF");
					holeView.addParameter("field_id", field_id);
					holeView.addParameter("hole_number", tee[b].getStringColumnValue("hole_number"));

					myTable.add(holeView, b + 2, 1);
				}

				if (a + 1 == teeColor.length) {
					holePar = tee[b].getPar();
					par += tee[b].getPar();

					Text parText = new Text(String.valueOf(holePar));
					parText.setFontSize(1);
					parText.setFontColor("FFFFFF");

					Text handicapText = new Text("" + ((int) tee[b].getFloatColumnValue("handicap")));
					handicapText.setFontSize(1);
					handicapText.setFontColor("FFFFFF");

					myTable.add(handicapText, b + 2, a + 3);
					myTable.add(parText, b + 2, a + 4);
				}

				if (b + 1 == tee.length) {
					Text holeText = new Text("Hola&nbsp;");
					holeText.setFontSize(1);
					holeText.setFontColor("FFFFFF");

					Text totalText = new Text("Total");
					totalText.setFontSize(1);
					totalText.setFontColor("FFFFFF");

					Text rating = new Text(tee[b].getStringColumnValue("course_rating") + "/" + tee[b].getStringColumnValue("slope"));
					rating.setFontSize(1);

					Text teeLengthText = new Text(String.valueOf(teeLength));
					teeLengthText.setFontSize(1);

					Text totalPar = new Text(String.valueOf(par));
					totalPar.setFontSize(1);
					totalPar.setFontColor("FFFFFF");

					Text ratingText = new Text("CR/Slope");
					ratingText.setFontSize(1);
					ratingText.setFontColor("FFFFFF");

					Text parText2 = new Text("Par&nbsp;");
					parText2.setFontSize(1);
					parText2.setFontColor("FFFFFF");

					Text handicap = new Text("Forgjöf&nbsp;");
					handicap.setFontSize(1);
					handicap.setFontColor("FFFFFF");

					myTable.add(rating, b + 4, a + 2);
					myTable.add(teeLengthText, b + 3, a + 2);
					if (a + 1 == teeColor.length) {
						myTable.add(holeText, 1, 1);
						myTable.add(totalText, b + 3, 1);
						myTable.add(totalPar, b + 3, a + 4);
						myTable.add(ratingText, b + 4, 1);
						myTable.addText("", b + 3, a + 3);
						myTable.addText("", b + 4, a + 3);
						myTable.addText("", b + 4, a + 4);
						myTable.add(parText2, 1, a + 4);
						myTable.add(handicap, 1, a + 3);
					}
				}
			}

		}

		for (int a = 1; a <= myTable.getRows(); a++) {
			myTable.setRowAlignment(a, "center");
			myTable.setRowVerticalAlignment(a, "middle");

			if (a > 1 && a <= teeColor.length + 1) {
				String teeLitur = getTeeColor(teeColor[a - 2].getIntColumnValue("tee_color_id"));
				myTable.setRowColor(a, teeLitur);
			}

			else if (a == 1) {
				myTable.setRowColor(a, "65A86E");
			}

			else if (a > teeColor.length + 1) {
				myTable.setRowColor(a, "8ab490");
			}
		}

		myTable.setColumnAlignment(1, "right");
		myTable.setWidth("100%");
		myTable.setCellpadding(2);
		myTable.setCellspacing(1);
		myTable.setColor("000000");
		myTable.setWidth(1, "90");

		return myTable;
	}

	public void fillContentTable(Field field, Table contentTable, Table myTable, String field_id) throws IOException, SQLException {

		Text fieldName = new Text(field.getName());
		fieldName.setFontSize(4);
		fieldName.setBold();

		FieldImage[] fieldImage = (FieldImage[]) ((FieldImage) IDOLookup.instanciateEntity(FieldImage.class)).findAllByColumn("field_id", String.valueOf(field.getID()));

		if (fieldImage.length != 0) {

			Image fieldMynd = new Image(fieldImage[0].getImageId());
			fieldMynd.setVerticalSpacing(6);
			fieldMynd.setHorizontalSpacing(6);

			contentTable.add(fieldMynd, 1, 2);

		}

		else {

			//			Text fieldMynd = new Text("No picture entry in database...");

			//			contentTable.add(fieldMynd,1,2);

		}

		TextReader fieldText = null;
		HoleText[] hole_text = (HoleText[]) ((HoleText) IDOLookup.instanciateEntity(HoleText.class)).findAllByColumn("field_id", "" + field.getID(), "hole_number", "0");
		if (hole_text.length > 0) {
			fieldText = new TextReader("" + hole_text[0].getTextId());
			fieldText.displayHeadline(false);
			fieldText.setEnableDelete(false);

		}

		contentTable.setCellpadding(0);
		contentTable.setCellspacing(3);
		contentTable.setAlignment(1, 2, "center");

		int row = 3;
		contentTable.add(fieldName, 1, 1);

		if (fieldText != null) {
			contentTable.add(fieldText, 1, 3);
			row++;
		}
		contentTable.add(getHoleChooser(field_id), 1, row);
		row++;
		contentTable.add(myTable, 1, row);
		row++;

		Link handicapTables = new Link(new Image("/pics/formtakks/handicap_table.gif", "", 145, 19), "/handicap/handicap_table.jsp");
		//Link handicapTables = new Link(new
		// Flash("http://jgenerator.sidan.is/takki1.swt?type=gif&texti=FORGJAFARTÖFLUR","",145,19),"/handicap/handicap_table.jsp");
		handicapTables.addParameter("field_id", field_id);
		handicapTables.setTarget("_blank");
		Link gameHandicaps = new Link(new Image("/pics/formtakks/reiknalf.gif", "", 145, 19), "/clubs/field2.jsp");
		gameHandicaps.addParameter("field_id", field_id);

		Table formTable = new Table();
		formTable.setAlignment(2, 1, "right");
		formTable.setWidth("100%");

		formTable.add(handicapTables, 1, 1);
		formTable.add(gameHandicaps, 2, 1);

		contentTable.add(formTable, 1, row);

	}

	public Form getHoleChooser(String field_id) throws IOException {

		Form myForm = new Form("/clubs/field.jsp");
		myForm.add(new HiddenInput("field_id", field_id));

		DropdownMenu holeChooser = new DropdownMenu("hole_number");

		for (int a = 1; a <= 18; a++) {

			holeChooser.addMenuElement(a, a + ". hola");

		}

		Table myTable = new Table();
		myTable.setAlignment("right");

		Text holeText = new Text("Veldu holu:");
		holeText.setBold();

		SubmitButton senda = new SubmitButton(new Image("/pics/formtakks/saekja.gif", "", 76, 19));

		myTable.add(holeText, 1, 1);
		myTable.add(holeChooser, 2, 1);
		myTable.add(senda, 3, 1);

		myForm.add(myTable);

		return myForm;

	}

	public Table getHoleInfo(Field field, String field_id, String hole_number) throws IOException, SQLException {

		Table outerTable = new Table(1, 4);
		outerTable.setWidth("100%");

		Table myTable = new Table();
		myTable.setColor("000000");
		myTable.setCellpadding(6);
		myTable.setCellspacing(2);

		int row = 1;

		Tee[] tee = (Tee[]) ((Tee) IDOLookup.instanciateEntity(Tee.class)).findAll("select * from tee where field_id='" + String.valueOf(field.getID()) + "' and hole_number='" + hole_number + "' and tee_color_id<5");

		for (int a = 0; a < tee.length; a++) {

			if (a == 0) {
				Text parText = new Text("Par " + tee[a].getPar());
				parText.setBold();
				parText.setFontSize(4);

				Text handicapText = new Text("Forgjöf " + ((int) tee[a].getFloatColumnValue("handicap")));
				handicapText.setBold();
				handicapText.setFontSize(4);

				myTable.setColor(1, 1, "FFFFFF");
				myTable.setColor(2, 1, "FFFFFF");

				myTable.add(parText, 1, 1);
				myTable.add(handicapText, 2, 1);
			}

			Text lengthText = new Text(tee[a].getStringColumnValue("tee_length"));
			lengthText.setBold();
			lengthText.setFontSize(4);

			myTable.add(lengthText, a + 3, 1);
			myTable.setColor(a + 3, 1, getTeeColor(tee[a].getIntColumnValue("tee_color_id")));
		}

		if (tee.length == 0) {

			//			myTable.setColor(1,1,"FFFFFF");
			//			myTable.addText("No entries in database...",1,1);

		}

		else {
			Text holeText = new Text(hole_number + ". hola");
			holeText.setBold();
			holeText.setFontSize(6);

			Text holeName = new Text("");
			holeName.setBold();
			holeName.setFontSize(5);

			if (tee[0].getStringColumnValue("hole_name") != null) {

				holeName.setText("&nbsp&nbsp" + tee[0].getStringColumnValue("hole_name"));
			}

			outerTable.add(holeText, 1, row);
			outerTable.add(holeName, 1, row);
			++row;
		}

		TeeImage[] teeImage = (TeeImage[]) ((TeeImage) IDOLookup.instanciateEntity(TeeImage.class)).findAllByColumn("field_id", String.valueOf(field.getID()), "hole_number", hole_number);

		if (teeImage.length != 0) {

			Image teeMynd = new Image(teeImage[0].getImageId());

			outerTable.add(teeMynd, 1, row);

		}

		else {

			//			Text teeMynd = new Text("No picture entry in database...");

			//			outerTable.add(teeMynd,1,2);

		}

		++row;

		TextReader fieldText = null;
		HoleText[] hole_text = (HoleText[]) ((HoleText) IDOLookup.instanciateEntity(HoleText.class)).findAllByColumn("field_id", "" + field.getID(), "hole_number", hole_number);
		if (hole_text.length > 0) {
			fieldText = new TextReader("" + hole_text[0].getTextId());
			fieldText.displayHeadline(false);
			fieldText.setEnableDelete(false);
		}

		if (fieldText != null) {
			outerTable.add(fieldText, 1, row);
			++row;
		}

		outerTable.add(myTable, 1, 3);

		Table linksTable = new Table(3, 1);
		linksTable.setWidth("100%");
		linksTable.setAlignment(2, 1, "center");
		linksTable.setAlignment(3, 1, "right");
		linksTable.setWidth(1, "33%");
		linksTable.setWidth(2, "33%");
		linksTable.setWidth(3, "33%");

		Link backHole = new Link("<<&nbsp;Fyrri&nbsp;hola", "/clubs/field.jsp");
		backHole.addParameter("hole_number", String.valueOf(Integer.parseInt(hole_number) - 1));
		backHole.addParameter("field_id", field_id);

		Link courseOverview = new Link("-&nbsp;Vallaryfirlit&nbsp;-", "/clubs/field.jsp");
		courseOverview.addParameter("field_id", field_id);

		Link nextHole = new Link("Næsta&nbsp;hola&nbsp;>>", "/clubs/field.jsp");
		nextHole.addParameter("hole_number", String.valueOf(Integer.parseInt(hole_number) + 1));
		nextHole.addParameter("field_id", field_id);

		linksTable.add(courseOverview, 2, 1);

		if (Integer.parseInt(hole_number) > 1) {
			linksTable.add(backHole, 1, 1);
		}

		if (Integer.parseInt(hole_number) < 18) {
			linksTable.add(nextHole, 3, 1);
		}

		outerTable.add(linksTable, 1, 4);

		outerTable.setCellspacing(3);
		outerTable.setAlignment(1, 2, "center");
		outerTable.setAlignment(1, 3, "center");

		return outerTable;

	}

	public String scale_decimals(String nyForgjof, int scale) throws IOException {

		BigDecimal test2 = new BigDecimal(nyForgjof);

		String nyForgjof2 = test2.setScale(scale, 5).toString();

		return nyForgjof2;

	}
}