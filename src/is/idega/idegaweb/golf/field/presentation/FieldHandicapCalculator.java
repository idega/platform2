/*
 * Created on 4.3.2004
 */
package is.idega.idegaweb.golf.field.presentation;

import is.idega.idegaweb.golf.entity.Field;
import is.idega.idegaweb.golf.entity.FieldHome;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.Tee;
import is.idega.idegaweb.golf.entity.TeeColor;
import is.idega.idegaweb.golf.entity.TeeColorHome;
import is.idega.idegaweb.golf.handicap.business.Handicap;
import is.idega.idegaweb.golf.presentation.GolfBlock;
import is.idega.idegaweb.golf.templates.page.GolfWindow;

import java.io.IOException;
import java.sql.SQLException;

import javax.ejb.FinderException;

import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;

/**
 * @author laddi
 */
public class FieldHandicapCalculator extends GolfWindow {

	public FieldHandicapCalculator() {
		setWidth(300);
		setHeight(230);
		setTitle("Field handicap calculator");
		add(new Calculator());
		this.setScrollbar(true);
	}

	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		addHeading(_iwrb.getLocalizedString("field.field_handicap_calculator", "Field handicap calculator"));
	}

	public class Calculator extends GolfBlock {

		public void main(IWContext modinfo) throws Exception {
			String field_id = modinfo.getParameter("field_id");

			Field field = ((FieldHome) IDOLookup.getHomeLegacy(Field.class)).findByPrimaryKey(Integer.parseInt(field_id));
			fillContentTable(modinfo, field, field_id);
		}

		public void fillContentTable(IWContext modinfo, Field field, String field_id) throws IOException, SQLException, FinderException {

			Table contentTable = new Table();
			contentTable.setWidth("100%");
			contentTable.setCellpadding(0);
			contentTable.setCellspacing(0);
			contentTable.setAlignment(1, 1, Table.HORIZONTAL_ALIGN_CENTER);

			String gender = "none";

			String handicap = modinfo.getParameter("handicap");
			if (handicap == null || handicap.equals("")) {
				handicap = "36";
			}

			String tee_color_id = modinfo.getParameter("tee_color_id");

			Member member = (Member) modinfo.getSessionAttribute("member_login");
			if (member != null) {
				gender = member.getGender();
			}

			Table myTable = new Table(2, 5);
			myTable.mergeCells(1, 5, 2, 5);
			myTable.setCellpadding(5);
			myTable.setCellspacing(0);
			Form myForm = new Form();
			myForm.add(new HiddenInput("field_id", field_id));

			DropdownMenu tees = (DropdownMenu) getStyledInterface(new DropdownMenu("tee_color_id"));
			TextInput handicapInput = (TextInput) getStyledInterface(new TextInput("handicap"));
			handicapInput.setLength(4);
			handicapInput.setMaxlength(4);
			handicapInput.setSize(4);

			if (member != null) {
				handicapInput.setValue(String.valueOf(member.getHandicap()));
			}

			Tee[] teeColor = (Tee[]) ((Tee) IDOLookup.instanciateEntity(Tee.class)).findAll("select distinct tee_color_id from tee where field_id='" + String.valueOf(field.getID()) + "'");

			if (gender != null) {
				if (gender.equals("M")) {
					teeColor = (Tee[]) ((Tee) IDOLookup.instanciateEntity(Tee.class)).findAll("select distinct tee_color_id from tee where field_id='" + String.valueOf(field.getID()) + "' and tee_color_id != 3 and tee_color_id != 4");
				}
				if (gender.equals("F")) {
					teeColor = (Tee[]) ((Tee) IDOLookup.instanciateEntity(Tee.class)).findAll("select distinct tee_color_id from tee where field_id='" + String.valueOf(field.getID()) + "' and tee_color_id > 2 and tee_color_id < 5");
				}
			}

			for (int a = 0; a < teeColor.length; a++) {
				tees.addMenuElement(teeColor[a].getStringColumnValue("tee_color_id"), ((TeeColorHome) IDOLookup.getHomeLegacy(TeeColor.class)).findByPrimaryKey(teeColor[a].getIntColumnValue("tee_color_id")).getName());
			}

			if (handicap != null && tee_color_id != null) {
				tees.setSelectedElement(tee_color_id);
				if (handicap.equals("")) {
					handicap = "36";
				}

				if (handicap.indexOf(",") != -1) {
					handicap = handicap.replace(',', '.');
				}

				Double forgjof = Double.valueOf(handicap);
				double grunn = forgjof.doubleValue();
				Handicap leikForgjof = new Handicap(grunn);

				Tee[] tee = (Tee[]) ((Tee) IDOLookup.instanciateEntity(Tee.class)).findAllByColumn("tee_color_id", tee_color_id, "field_id", field_id);
				int slope = tee[0].getIntColumnValue("slope");
				float course_rating = tee[0].getFloatColumnValue("course_rating");
				int field_par = field.getIntColumnValue("field_par");

				int leik = leikForgjof.getLeikHandicap((double) slope, (double) course_rating, (double) field_par);

				myTable.add(getText(String.valueOf(leik)), 2, 4);

				handicapInput.setValue(handicap);

			}

			Text courseText = getHeader(localize("field.course", "Course"));
			Text handicapText = getHeader(localize("field.base_handicap", "Handicap"));
			Text teeText = getHeader(localize("field.tees", "Tees"));
			Text gameHandText = getHeader(localize("field.play_handicap", "Play handicap"));
			Text fieldText = getText(field.getName());			

			myTable.add(courseText, 1, 1);
			myTable.add(handicapText, 1, 2);
			myTable.add(teeText, 1, 3);
			myTable.add(gameHandText, 1, 4);

			myTable.add(fieldText, 2, 1);
			myTable.add(handicapInput, 2, 2);
			myTable.add(tees, 2, 3);
			myTable.add(new CloseButton(localize("close", "Close")), 1, 5);
			myTable.add(Text.getNonBrakingSpace(), 1, 5);
			myTable.add(new SubmitButton(localize("calculate", "Calculate")), 1, 5);

			myTable.setAlignment(1, 1, "right");
			myTable.setAlignment(1, 2, "right");
			myTable.setAlignment(1, 3, "right");
			myTable.setAlignment(1, 4, "right");
			myTable.setAlignment(1, 5, "right");

			myForm.add(myTable);
			contentTable.add(myForm, 1, 1);

			add(contentTable);

		}
	}
}