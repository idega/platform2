/*
 * Created on 4.3.2004
 */
package is.idega.idegaweb.golf.clubs.presentation;

import is.idega.block.modernus.presentation.Modernus;
import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.presentation.GolfBlock;

import java.sql.SQLException;
import java.util.Vector;

import com.idega.core.builder.data.ICPage;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.user.data.Group;

/**
 * @author laddi
 */
public class ClubNavigation extends GolfBlock {
  
  private ICPage iPage = null;

	public void main(IWContext modinfo) throws Exception {
		modinfo.removeSessionAttribute("golf_union_id");
		String URI = modinfo.getRequestURI();

		add(Text.getBreak());
		add(getLinkTable());
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

		Text link;
		Text header = getLocalizedHeader(getLocationNameKey(location),"");
//		header.setFontSize(3);
//		header.setBold();

		add(header);

		for (int i = 0; i < unions.size(); i++) {
			union = ((Union) unions.elementAt(i));
			
			Group grUnion = union.getUnionFromIWMemberSystem();
			ICPage clubPage = grUnion.getHomePage();
			if(clubPage!=null) {
				link = getLink(grUnion.getName());
				((Link)link).setPage(clubPage);
			} else {
				link = getText(grUnion.getName());
			}

			table.setVerticalAlignment(1, i + 1, "top");
			table.setVerticalAlignment(2, i + 1, "top");

			table.add(grUnion.getAbbrevation(), 1, i + 1);
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
		Image returner = new Image();

		if (location == 12)
			location = 1;
		switch (location) {
			case 1 :
				returner = getBundle().getImage("shared/map/city.gif");
				break;
			case 2 :
				returner = getBundle().getImage("shared/map/reykjanes.gif");
				break;
			case 3 :
				returner = getBundle().getImage("shared/map/west.gif");
				break;
			case 4 :
				returner = getBundle().getImage("shared/map/westfjord.gif");
				break;
			case 5 :
				returner = getBundle().getImage("shared/map/northwest.gif");
				break;
			case 6 :
				returner = getBundle().getImage("shared/map/northeast.gif");
				break;
			case 7 :
				returner = getBundle().getImage("shared/map/east.gif");
				break;
			case 8 :
				returner = getBundle().getImage("shared/map/south.gif");
				break;
			case 10 :
				returner = getBundle().getImage("shared/map/iceland.gif");
				break;
		}
		return returner;
	}

	public String getLocationNameKey(int location) {
		String returner = "";
		switch (location) {
			case 1 :
				returner = "capital_area";
				break;
			case 2 :
				returner = "reykjanes";
				break;
			case 3 :
				returner = "west";
				break;
			case 4 :
				returner = "westfords";
				break;
			case 5 :
				returner = "north_west";
				break;
			case 6 :
				returner = "north_east";
				break;
			case 7 :
				returner = "east";
				break;
			case 8 :
				returner = "south";
				break;
			case 10 :
				returner = "the_whole_country";
				break;
			case 12 :
				returner = "others";
				break;
		}
		return returner;
	}

	public Table getLinkTable() {
		int font_size = 2;

		Text rvkText = getLocalizedText(getLocationNameKey(1),"Capital Area");
		rvkText.setFontSize(font_size);
		Text reykjanesText = getLocalizedText(getLocationNameKey(2),"Reykjanes");
		reykjanesText.setFontSize(font_size);
		Text vesturlandText = getLocalizedText(getLocationNameKey(3),"West");
		vesturlandText.setFontSize(font_size);
		Text vestfirdirText = getLocalizedText(getLocationNameKey(4),"Westfjords");
		vestfirdirText.setFontSize(font_size);
		Text nordurvestText = getLocalizedText(getLocationNameKey(5),"North West");
		nordurvestText.setFontSize(font_size);
		Text norduraustText = getLocalizedText(getLocationNameKey(6),"North East");
		norduraustText.setFontSize(font_size);
		Text austurText = getLocalizedText(getLocationNameKey(7),"East");
		austurText.setFontSize(font_size);
		Text sudurText = getLocalizedText(getLocationNameKey(8),"South");
		sudurText.setFontSize(font_size);
		Text alltLandText = getLocalizedText(getLocationNameKey(10),"The Whole Country");
		alltLandText.setFontSize(font_size);
		Text otherText = getLocalizedText(getLocationNameKey(12),"Others");
		otherText.setFontSize(font_size);

		Link rvk = new Link(rvkText);
		rvk.addParameter("i_golf_field_location", "" + 1);
		Link reykjanes = new Link(reykjanesText);
		reykjanes.addParameter("i_golf_field_location", "" + 2);
		Link vesturland = new Link(vesturlandText);
		vesturland.addParameter("i_golf_field_location", "" + 3);
		Link vesturfirdir = new Link(vestfirdirText);
		vesturfirdir.addParameter("i_golf_field_location", "" + 4);
		Link nordurvest = new Link(nordurvestText);
		nordurvest.addParameter("i_golf_field_location", "" + 5);
		Link norduraust = new Link(norduraustText);
		norduraust.addParameter("i_golf_field_location", "" + 6);
		Link austur = new Link(austurText);
		austur.addParameter("i_golf_field_location", "" + 7);
		Link sudur = new Link(sudurText);
		sudur.addParameter("i_golf_field_location", "" + 8);
		Link alltLand = new Link(alltLandText);
		alltLand.addParameter("i_golf_field_location", "" + 10);
		Link other = new Link(otherText);
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

}