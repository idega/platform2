/*
 * Created on 4.3.2004
 */
package is.idega.idegaweb.golf.clubs.presentation;

import is.idega.block.modernus.presentation.Modernus;
import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.presentation.GolfBlock;

import java.sql.SQLException;
import java.util.Vector;

import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;

/**
 * @author laddi
 */
public class ClubNavigation extends GolfBlock {

	public void main(IWContext modinfo) throws Exception {
		modinfo.removeSessionAttribute("golf_union_id");
		String URI = modinfo.getRequestURI();

		add(Text.getBreak());
		add(getLinkTable(URI));
		add(Text.getBreak());
		add(getUnions(modinfo));
	}

	public Table getUnions(IWContext modinfo) throws SQLException {
		String location = modinfo.getParameter("i_golf_field_location");
		Table returnTable = new Table();

		if (location != null) {
			returnTable = getUnionListTable(modinfo, Integer.parseInt(location));
		}
		else {
			returnTable = getUnionListTable(modinfo, 1);
		}

		return returnTable;
	}

	public Vector getUnionArray(IWContext modinfo, int location) throws SQLException {
		Union union = (Union) IDOLookup.instanciateEntity(Union.class);
		Vector vector = (Vector) modinfo.getServletContext().getAttribute("i_clubs_array_" + location);

		if (vector == null) {
			String SQLString = "";
			int zipcode_from = 0;
			int zipcode_to = 1;
			int zipcode_from2 = 0;
			int zipcode_to2 = 1;
			int zipcode_is_not = 0;
			int zipcode_is_not2 = 0;

			switch (location) {
				case 1 :
					zipcode_from = 101;
					zipcode_to = 225;
					zipcode_from2 = 270;
					zipcode_to2 = 270;
					zipcode_is_not = 128;
					zipcode_is_not2 = 190;
					break;
				case 2 :
					zipcode_from = 226;
					zipcode_to = 299;
					zipcode_from2 = 190;
					zipcode_to2 = 190;
					zipcode_is_not = 270;
					break;
				case 3 :
					zipcode_from = 300;
					zipcode_to = 399;
					break;
				case 4 :
					zipcode_from = 400;
					zipcode_to = 499;
					break;
				case 5 :
					zipcode_from = 500;
					zipcode_to = 599;
					break;
				case 6 :
					zipcode_from = 600;
					zipcode_to = 699;
					break;
				case 7 :
					zipcode_from = 700;
					zipcode_to = 799;
					break;
				case 8 :
					zipcode_from = 800;
					zipcode_to = 999;
					zipcode_from2 = 128;
					zipcode_to2 = 128;
					break;
				case 10 :
					zipcode_from = 100;
					zipcode_to = 999;
					break;
				case 12 :
					zipcode_from = 1000;
					zipcode_to = 1000;
					break;

			}
			vector = (Vector) union.getUnionsBetweenZipcodesOrderBy(union, zipcode_from, zipcode_to, zipcode_from2, zipcode_to2, zipcode_is_not, zipcode_is_not2, "abbrevation");
			if (vector == null) {
				vector = new Vector();
			}
			modinfo.getServletContext().setAttribute("i_clubs_array_" + location, vector);
		}

		return vector;
	}

	public Table getUnionListTable(IWContext modinfo, int location) throws SQLException {

		Vector unions = getUnionArray(modinfo, location);
		Union union;
		Image image = getImage(location);
		Table contentTable = new Table(3, 1);
		contentTable.setBorder(0);
		contentTable.setWidth("100%");
		Table table = new Table();
		table.setWidth("100%");
		table.setBorder(0);

		Link link;
		Text header = new Text(getLocationName(location));
		header.setFontSize(3);
		header.setBold();

		add(header);

		for (int i = 0; i < unions.size(); i++) {
			union = ((Union) unions.elementAt(i));
			link = new Link(union.getName(), "index2.jsp");
			link.addParameter("union_id", union.getID());

			table.setVerticalAlignment(1, i + 1, "top");
			table.setVerticalAlignment(2, i + 1, "top");

			table.add(union.getAbbrevation(), 1, i + 1);
			table.add(" - ", 2, i + 1);
			table.add(link, 2, i + 1);
		}

		contentTable.add(table, 1, 1);
		contentTable.add(image, 3, 1);
		contentTable.setVerticalAlignment(1, 1, "top");
		contentTable.setVerticalAlignment(2, 1, "top");
		contentTable.setVerticalAlignment(3, 1, "top");
		contentTable.setAlignment(1, 1, "right");
		contentTable.setAlignment(3, 1, "right");

		Modernus modernus = new Modernus("6725", "Club_overview_" + location, "golf-is");
		contentTable.add(modernus, 3, 1);

		return contentTable;
	}

	public Image getImage(int location) {
		Image returner = new Image("/");

		if (location == 12)
			location = 1;
		switch (location) {
			case 1 :
				returner = new Image("/pics/clubs/city.gif");
				break;
			case 2 :
				returner = new Image("/pics/clubs/reykjanes.gif");
				break;
			case 3 :
				returner = new Image("/pics/clubs/west.gif");
				break;
			case 4 :
				returner = new Image("/pics/clubs/westfjord.gif");
				break;
			case 5 :
				returner = new Image("/pics/clubs/northwest.gif");
				break;
			case 6 :
				returner = new Image("/pics/clubs/northeast.gif");
				break;
			case 7 :
				returner = new Image("/pics/clubs/east.gif");
				break;
			case 8 :
				returner = new Image("/pics/clubs/south.gif");
				break;
			case 10 :
				returner = new Image("/pics/clubs/iceland.gif");
				break;
		}
		return returner;
	}

	public String getLocationName(int location) {
		String returner = "";
		switch (location) {
			case 1 :
				returner = "Höfuðborgarsvæðið";
				break;
			case 2 :
				returner = "Reykjanes";
				break;
			case 3 :
				returner = "Vesturland";
				break;
			case 4 :
				returner = "Vestfirðir";
				break;
			case 5 :
				returner = "Norðurland vestra";
				break;
			case 6 :
				returner = "Norðurland eystra";
				break;
			case 7 :
				returner = "Austurland";
				break;
			case 8 :
				returner = "Suðurland";
				break;
			case 10 :
				returner = "Allt landið";
				break;
			case 12 :
				returner = "Aðrir";
				break;
		}
		return returner;
	}

	public Table getLinkTable(String URI) {
		int font_size = 2;

		Text rvkText = new Text(getLocationName(1));
		rvkText.setFontSize(font_size);
		Text reykjanesText = new Text(getLocationName(2));
		reykjanesText.setFontSize(font_size);
		Text vesturlandText = new Text(getLocationName(3));
		vesturlandText.setFontSize(font_size);
		Text vestfirdirText = new Text(getLocationName(4));
		vestfirdirText.setFontSize(font_size);
		Text nordurvestText = new Text(getLocationName(5));
		nordurvestText.setFontSize(font_size);
		Text norduraustText = new Text(getLocationName(6));
		norduraustText.setFontSize(font_size);
		Text austurText = new Text(getLocationName(7));
		austurText.setFontSize(font_size);
		Text sudurText = new Text(getLocationName(8));
		sudurText.setFontSize(font_size);
		Text alltLandText = new Text(getLocationName(10));
		alltLandText.setFontSize(font_size);
		Text otherText = new Text(getLocationName(12));
		otherText.setFontSize(font_size);

		Link rvk = new Link(rvkText, URI);
		rvk.addParameter("i_golf_field_location", "" + 1);
		Link reykjanes = new Link(reykjanesText, URI);
		reykjanes.addParameter("i_golf_field_location", "" + 2);
		Link vesturland = new Link(vesturlandText, URI);
		vesturland.addParameter("i_golf_field_location", "" + 3);
		Link vesturfirdir = new Link(vestfirdirText, URI);
		vesturfirdir.addParameter("i_golf_field_location", "" + 4);
		Link nordurvest = new Link(nordurvestText, URI);
		nordurvest.addParameter("i_golf_field_location", "" + 5);
		Link norduraust = new Link(norduraustText, URI);
		norduraust.addParameter("i_golf_field_location", "" + 6);
		Link austur = new Link(austurText, URI);
		austur.addParameter("i_golf_field_location", "" + 7);
		Link sudur = new Link(sudurText, URI);
		sudur.addParameter("i_golf_field_location", "" + 8);
		Link alltLand = new Link(alltLandText, URI);
		alltLand.addParameter("i_golf_field_location", "" + 10);
		Link other = new Link(otherText, URI);
		other.addParameter("i_golf_field_location", "" + 12);

		Table outerTable = new Table(1, 1);
		outerTable.setColor("#336661");
		outerTable.setWidth("100%");
		outerTable.setCellpadding(0);
		outerTable.setCellspacing(1);

		Table linkTable = new Table();
		linkTable.setBorder(0);
		linkTable.setWidth("100%");
		linkTable.setColor("#ADC9B0");
		linkTable.setAlignment(1, 1, "center");
		linkTable.setAlignment(1, 2, "center");

		addLinkToTable(linkTable, rvk, 1, 1);
		addLinkToTable(linkTable, reykjanes, 1, 1);
		addLinkToTable(linkTable, vesturland, 1, 1);
		addLinkToTable(linkTable, vesturfirdir, 1, 1);
		addLinkToTable(linkTable, nordurvest, 1, 1);
		addLinkToTable(linkTable, norduraust, 1, 2);
		addLinkToTable(linkTable, austur, 1, 2);
		addLinkToTable(linkTable, sudur, 1, 2);
		addLinkToTable(linkTable, alltLand, 1, 2);
		addLinkToTable(linkTable, other, 1, 2);

		outerTable.add(linkTable, 1, 1);

		return outerTable;
	}

	public void addLinkToTable(Table table, Link link, int x_pos, int y_pos) {
		table.add(" [ ", x_pos, y_pos);
		table.add(link, x_pos, y_pos);
		table.add(" ] ", x_pos, y_pos);
	}

	public void main_before_gimmi(IWContext modinfo) throws Exception {

		Table clubs = new Table();
		clubs.setAlignment("center");
		//clubs.setWidth("250");
		clubs.setCellpadding(3);
		clubs.setCellspacing(2);
		clubs.setColor("FFFFFF");

		Text clubText = new Text("Klúbbur");
		clubText.setBold();
		clubText.setFontColor("FFFFFF");
		Text abbreText = new Text("Skammstöfun");
		abbreText.setBold();
		abbreText.setFontColor("FFFFFF");
		Text numberText = new Text("Númer");
		numberText.setBold();
		numberText.setFontColor("FFFFFF");

		clubs.add(clubText, 1, 1);

		Union[] unions = (Union[]) ((Union) IDOLookup.instanciateEntity(Union.class)).findAllByColumnOrdered("union_type", "golf_club", "abbrevation");
		unions[0].setVisible("union_type", false);

		int rows = (unions.length / 2);

		for (int a = 0; a < unions.length; a++) {

			Text unionName = new Text(unions[a].getName());
			unionName.setFontSize(1);

			Text abbreviation = new Text(unions[a].getAbbrevation());
			abbreviation.setFontSize(1);

			Link unionLink = new Link(unionName, "/clubs/index2.jsp");
			unionLink.addParameter("union_id", String.valueOf(unions[a].getID()));

			if (a <= rows) {
				clubs.add(abbreviation, 1, a + 2);
				clubs.add(unionLink, 2, a + 2);
			}
			else {
				clubs.add(abbreviation, 3, a + 1 - rows);
				clubs.add(unionLink, 4, a + 1 - rows);
			}
		}

		clubs.mergeCells(1, 1, 4, 1);
		clubs.setRowColor(1, "8ab490");

		add(clubs);
	}
}