/*
 * Created on 7.6.2004
 */
package is.idega.idegaweb.golf.field.presentation;

import is.idega.idegaweb.golf.entity.Field;
import is.idega.idegaweb.golf.entity.FieldHome;
import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.entity.UnionHome;
import is.idega.idegaweb.golf.presentation.GolfBlock;

import com.idega.core.builder.data.ICPage;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.user.data.Group;


/**
 * @author laddi
 */
public class FieldLinks extends GolfBlock {

	private ICPage fieldViewPage;
	
	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#main(com.idega.presentation.IWContext)
	 */
	public void main(IWContext modinfo) throws Exception {
		String union_id = modinfo.getParameter("union_id");
		if (union_id == null) {
			union_id = (String) modinfo.getSessionAttribute("golf_union_id");
		}

		String hole_number = modinfo.getParameter("hole_number");
		String field_id = modinfo.getParameter("field_id");
		if (field_id == null)
			field_id = "50";

		Field field = ((FieldHome) IDOLookup.getHomeLegacy(Field.class)).findByPrimaryKey(Integer.parseInt(field_id));

		Table linksTable = new Table();
		linksTable.setCellpadding(0);
		linksTable.setCellspacing(0);
		int column = 1;

		if (hole_number != null) {
			Link backHole = getStyleLink(getResourceBundle().getLocalizedString("field.previous_hole", "&lt;&lt; Previous hole"), this.STYLENAME_TEMPLATE_HEADER_LINK);
			backHole.addParameter("hole_number", String.valueOf(Integer.parseInt(hole_number) - 1));
			backHole.addParameter("field_id", field_id);
	
			Link courseOverview = getStyleLink(getResourceBundle().getLocalizedString("field.field_overview", "Field overview"), this.STYLENAME_TEMPLATE_HEADER_LINK);
			courseOverview.addParameter("field_id", field_id);
			courseOverview.setToMaintainAllParameter(false);
			if (fieldViewPage != null) {
				courseOverview.setPage(fieldViewPage);
			}
	
			Link nextHole = getStyleLink(getResourceBundle().getLocalizedString("field.next_hole", "Next hole &gt;&gt;"), this.STYLENAME_TEMPLATE_HEADER_LINK);
			nextHole.addParameter("hole_number", String.valueOf(Integer.parseInt(hole_number) + 1));
			nextHole.addParameter("field_id", field_id);
	
			if (Integer.parseInt(hole_number) > 1) {
				linksTable.setCellpaddingRight(column, 1, 3);
				linksTable.add(backHole, column++, 1);
				linksTable.setCellpaddingRight(column, 1, 3);
				linksTable.add(getStyleText("|", this.STYLENAME_TEMPLATE_SMALL_HEADER), column++, 1);
			}
	
			linksTable.add(courseOverview, column++, 1);
	
			if (Integer.parseInt(hole_number) < 18) {
				linksTable.setCellpaddingLeft(column, 1, 3);
				linksTable.add(getStyleText("|", this.STYLENAME_TEMPLATE_SMALL_HEADER), column++, 1);
				linksTable.setCellpaddingLeft(column, 1, 3);
				linksTable.add(nextHole, column++, 1);
			}
		}
		else {
			Union union = ((UnionHome) IDOLookup.getHomeLegacy(Union.class)).findByPrimaryKey(field.getUnionID());
			Group golfUnion = union.getUnionFromIWMemberSystem();
			if (golfUnion != null) {
				ICPage clubPage = golfUnion.getHomePage();
				String text = getResourceBundle().getLocalizedString("field.club_page", "Club page");
				if (clubPage!=null) {
					Link link = getStyleLink(text, STYLENAME_TEMPLATE_HEADER_LINK);
					link.setPage(clubPage);
					linksTable.add(link, column++, 1);
				}
				else {
					linksTable.add(getStyleText(text, STYLENAME_TEMPLATE_SMALL_HEADER), column++, 1);
				}
			}
		}

		if (isAdmin()) {
			linksTable.setCellpaddingLeft(column, 1, 3);
			linksTable.add(getStyleText("|", this.STYLENAME_TEMPLATE_SMALL_HEADER), column++, 1);
			linksTable.setCellpaddingLeft(column, 1, 3);

			Link admin = getStyleLink(getResourceBundle().getLocalizedString("field.field_editor", "Field editor"), STYLENAME_TEMPLATE_HEADER_LINK);
			admin.setWindowToOpen(FieldEditor.class);
			admin.addParameter("field_id", field_id);
			admin.addParameter("union_id", union_id);
			if(hole_number!=null) {
				admin.addParameter("hole_number",hole_number);
				admin.addParameter("action","view_hole");
			}
			admin.addParameter("redir", getResourceBundle().getLocalizedString("field.field_editor", "Field editor"));
			linksTable.add(admin, column, 1);
		}

	}

	/**
	 * @param fieldViewPage The fieldViewPage to set.
	 */
	public void setFieldViewPage(ICPage fieldViewPage) {
		this.fieldViewPage = fieldViewPage;
	}
}
