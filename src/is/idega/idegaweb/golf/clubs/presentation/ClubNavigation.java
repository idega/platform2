/*
 * Created on 4.3.2004
 */
package is.idega.idegaweb.golf.clubs.presentation;

import is.idega.block.modernus.presentation.Modernus;
import is.idega.idegaweb.golf.entity.Union;

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
public class ClubNavigation extends ClubBlock {
  
  private ICPage iPage = null;

	public void main(IWContext modinfo) throws Exception {
		modinfo.removeSessionAttribute("golf_union_id");

		add(getUnions(modinfo));
	}

	public Table getUnions(IWContext modinfo) throws SQLException {
		int location = 1;
		if (modinfo.isParameterSet(PARAMETER_CLUB_LOCATION)) {
			location = Integer.parseInt(modinfo.getParameter(PARAMETER_CLUB_LOCATION));
		}
		Table returnTable = new Table();
		returnTable = getUnionListTable(modinfo, location);

		Modernus modernus = new Modernus("6725", "Club_overview_" + location, "golf-is");
		add(modernus);

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
		Table contentTable = new Table(2, 1);
		contentTable.setBorder(0);
		contentTable.setWidth("100%");
		contentTable.setCellpadding(0);
		contentTable.setCellpadding(2, 1, 20);
		contentTable.setCellpadding(1, 1, 10);
		contentTable.setCellspacing(0);
		
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth("100%");
		table.setBorder(0);

		Text link = null;
		for (int i = 0; i < unions.size(); i++) {
			union = ((Union) unions.elementAt(i));
			
			Group grUnion = union.getUnionFromIWMemberSystem();
			if (grUnion != null) {
				ICPage clubPage = grUnion.getHomePage();
				if(clubPage!=null) {
					link = getLink(grUnion.getName());
					((Link)link).setPage(clubPage);
				}
				else {
					link = getText(grUnion.getName());
				}

				table.setVerticalAlignment(1, i + 1, "top");
				table.setVerticalAlignment(2, i + 1, "top");
	
				table.add(getHeader(grUnion.getAbbrevation()), 1, i + 1);
				table.add(getText(" - "), 1, i + 1);
				table.add(link, 1, i + 1);
				if ((i + 1) < unions.size()) {
					table.setCellpaddingBottom(1, i + 1, 10);
				}
			}
		}

		contentTable.add(table, 1, 1);
		contentTable.add(image, 2, 1);
		contentTable.setVerticalAlignment(1, 1, Table.VERTICAL_ALIGN_TOP);
		contentTable.setVerticalAlignment(2, 1, Table.VERTICAL_ALIGN_TOP);
		contentTable.setAlignment(2, 1, Table.HORIZONTAL_ALIGN_RIGHT);

		return contentTable;
	}

	public Image getImage(int location) {
		Image returner = new Image();

		if (location == 12)
			location = 10;
		switch (location) {
			case 1 :
				returner = getBundle().getImage("shared/map/city.jpg");
				break;
			case 2 :
				returner = getBundle().getImage("shared/map/reykjanes.jpg");
				break;
			case 3 :
				returner = getBundle().getImage("shared/map/west.jpg");
				break;
			case 4 :
				returner = getBundle().getImage("shared/map/westfjord.jpg");
				break;
			case 5 :
				returner = getBundle().getImage("shared/map/northwest.jpg");
				break;
			case 6 :
				returner = getBundle().getImage("shared/map/northeast.jpg");
				break;
			case 7 :
				returner = getBundle().getImage("shared/map/east.jpg");
				break;
			case 8 :
				returner = getBundle().getImage("shared/map/south.jpg");
				break;
			case 10 :
				returner = getBundle().getImage("shared/map/iceland.jpg");
				break;
		}
		return returner;
	}
}