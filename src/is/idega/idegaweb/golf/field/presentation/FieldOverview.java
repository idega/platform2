/*
 * Created on 4.3.2004
 */
package is.idega.idegaweb.golf.field.presentation;

import is.idega.idegaweb.golf.entity.Field;
import is.idega.idegaweb.golf.entity.FieldHome;
import is.idega.idegaweb.golf.entity.FieldImage;
import is.idega.idegaweb.golf.entity.HoleText;
import is.idega.idegaweb.golf.entity.Tee;
import is.idega.idegaweb.golf.entity.TeeColor;
import is.idega.idegaweb.golf.entity.TeeColorHome;
import is.idega.idegaweb.golf.entity.TeeImage;
import is.idega.idegaweb.golf.handicap.presentation.HandicapTable;
import is.idega.idegaweb.golf.presentation.GolfBlock;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

import javax.ejb.FinderException;

import com.idega.block.text.business.TextBusiness;
import com.idega.block.text.data.TxText;
import com.idega.block.text.presentation.TextReader;
import com.idega.core.builder.data.ICPage;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;

/**
 * @author laddi
 */
public class FieldOverview extends GolfBlock {
  
  private ICPage gameHandicapPage;
  public final static String PRM_FIELD_ID = "field_id";
  private int iMaxImageWidth = 0;
  
  private ICPage holeViewPage;

	public void main(IWContext modinfo) throws Exception {
		String hole_number = modinfo.getParameter("hole_number");
		String field_id = modinfo.getParameter("field_id");
		if (field_id == null)
			field_id = "50";

		Table contentTable = new Table();
		contentTable.setWidth("100%");
		contentTable.setCellpadding(0);
		contentTable.setCellspacing(0);

		if (field_id != null && hole_number == null) {
			Field field = ((FieldHome) IDOLookup.getHomeLegacy(Field.class)).findByPrimaryKey(Integer.parseInt(field_id));
			Table myTable = getFieldInfo(field, field_id);

			fillContentTable(modinfo,field, contentTable, myTable, field_id);
		}

		else if (field_id != null && hole_number != null) {
			Field field = ((FieldHome) IDOLookup.getHomeLegacy(Field.class)).findByPrimaryKey(Integer.parseInt(field_id));
			Table myTable = getHoleInfo(modinfo,field, field_id, hole_number);

			contentTable.add(myTable, 1, 1);
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
				litur = "#FCFA44";
				break;

			case 3 :
				litur = "#2F78DC";
				break;

			case 4 :
				litur = "#DC2F2F";
				break;

			case 5 :
				litur = "#2F78DC";
				break;

			case 6 :
				litur = "#DC2F2F";
				break;

		}

		return litur;
	}

	public Table getFieldInfo(Field field, String field_id) throws IOException, SQLException, FinderException {

		Tee[] teeColor = (Tee[]) ((Tee) IDOLookup.instanciateEntity(Tee.class)).findAll("select distinct tee_color_id from tee where field_id='" + field.getID() + "'");

		Table myTable = new Table();
		myTable.setRows(teeColor.length + 3);
		
		for (int a = 0; a < teeColor.length; a++) {

			Tee[] tee = (Tee[]) ((Tee) IDOLookup.instanciateEntity(Tee.class)).findAllByColumnOrdered("field_id", String.valueOf(field.getID()), "tee_color_id", String.valueOf(teeColor[a].getIntColumnValue("tee_color_id")), "hole_number");

			Text teeColorName = getSmallBoldBlackText(((TeeColorHome) IDOLookup.getHomeLegacy(TeeColor.class)).findByPrimaryKey(teeColor[a].getIntColumnValue("tee_color_id")).getName() + "&nbsp;");
			myTable.add(teeColorName, 1, a + 2);

			int teeLength = 0;
			int par = 0;
			int holePar = 0;

			for (int b = 0; b < tee.length; b++) {

				int holeLength = tee[b].getIntColumnValue("tee_length");
				Text lengthText = getSmallBlackText(String.valueOf(holeLength));
				myTable.add(lengthText, b + 2, a + 2);

				teeLength += tee[b].getIntColumnValue("tee_length");

				if (a == 0) {
					Text holeNumber = getSmallHeader(tee[b].getStringColumnValue("hole_number"));
					myTable.add(holeNumber, b + 2, 1);
				}

				if (a + 1 == teeColor.length) {
					holePar = tee[b].getPar();
					par += tee[b].getPar();

					Text parText = getSmallBlackText(String.valueOf(holePar));
					Text handicapText = getSmallBlackText("" + ((int) tee[b].getFloatColumnValue("handicap")));
					myTable.add(handicapText, b + 2, a + 3);
					myTable.add(parText, b + 2, a + 4);
				}

				if (b + 1 == tee.length) {
					Text holeText = getSmallHeader(getResourceBundle().getLocalizedString("field.hole", "Hole") + Text.NON_BREAKING_SPACE);
					Text totalText = getSmallHeader(getResourceBundle().getLocalizedString("field.total", "Total"));
					Text rating = getSmallBoldBlackText(tee[b].getStringColumnValue("course_rating") + "/" + tee[b].getStringColumnValue("slope"));
					Text teeLengthText = getSmallBoldBlackText(String.valueOf(teeLength));
					Text totalPar = getSmallBoldBlackText(String.valueOf(par));
					Text ratingText = getSmallHeader(getResourceBundle().getLocalizedString("field.cr_slope", "CR/Slope"));
					Text parText2 = getSmallBoldBlackText(getResourceBundle().getLocalizedString("field.par", "Par") + Text.NON_BREAKING_SPACE);
					Text handicap = getSmallBoldBlackText(getResourceBundle().getLocalizedString("field.handicap", "Handicap") + Text.NON_BREAKING_SPACE);

					myTable.add(rating, b + 4, a + 2);
					myTable.add(teeLengthText, b + 3, a + 2);
					if (a + 1 == teeColor.length) {
						myTable.add(holeText, 1, 1);
						myTable.add(totalText, b + 3, 1);
						myTable.add(totalPar, b + 3, a + 4);
						myTable.add(ratingText, b + 4, 1);
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
				myTable.setRowStyleClass(a, getDarkRowClass());
				String teeLitur = getTeeColor(teeColor[a - 2].getIntColumnValue("tee_color_id"));
				myTable.setRowColor(a, teeLitur);
			}

			else if (a == 1) {
				myTable.setRowStyleClass(a, getHeaderRowClass());
			}

			else if (a > teeColor.length + 1) {
				if (a != myTable.getRows()) {
					myTable.setRowStyleClass(a, getDarkRowClass());
				}
				else {
					myTable.setRowStyleClass(a, getLightRowClass());
				}
			}
		}

		myTable.setColumnAlignment(1, Table.HORIZONTAL_ALIGN_RIGHT);
		myTable.setWidth(Table.HUNDRED_PERCENT);
		myTable.setCellpadding(0);
		myTable.setCellspacing(0);

		return myTable;
	}

	public void fillContentTable(IWContext iwc,Field field, Table contentTable, Table myTable, String field_id) throws IOException, SQLException {
		int row = 1;

		Table formTable = new Table(2, 1);
		formTable.setAlignment(2, 1, "right");
		formTable.setCellpaddingLeft(1, 1, 5);
		formTable.setCellpaddingRight(2, 1, 5);
		formTable.setWidth("100%");

		formTable.add(getHoleChooser(field_id), 1, 1);

		GenericButton handicapTables = new GenericButton("handicap_tables", getResourceBundle().getLocalizedString("field.handicap_tables", "Handicap tables"));
		handicapTables.addParameterToPage("field_id", field_id);
		handicapTables.setWindowToOpen(HandicapTable.class);
		formTable.add(handicapTables, 2, 1);

	  GenericButton gameHandicaps = new GenericButton("game_handicap", getResourceBundle().getLocalizedString("field.game_handicap", "Game handicap"));
		gameHandicaps.addParameterToWindow("field_id", field_id);
		gameHandicaps.setWindowToOpen(FieldHandicapCalculator.class);
		formTable.add(Text.getNonBrakingSpace(), 2, 1);
		formTable.add(gameHandicaps, 2, 1);
		
		contentTable.setHeight(row, 40);
		contentTable.add(formTable, 1, row++);
		contentTable.add(myTable, 1, row++);
		
		Table infoTable = new Table();
		infoTable.setCellpadding(0);
		infoTable.setCellspacing(0);
		infoTable.setWidth(Table.HUNDRED_PERCENT);
		contentTable.add(infoTable, 1, row);
		int column = 1;

		contentTable.setCellpaddingLeft(1,row,"10%");
		contentTable.setCellpaddingRight(1,row,"10%");
		contentTable.setCellpaddingBottom(1,row,25);
		contentTable.setCellpaddingTop(1,row,10);

		FieldImage[] fieldImage = (FieldImage[]) ((FieldImage) IDOLookup.instanciateEntity(FieldImage.class)).findAllByColumn("field_id", String.valueOf(field.getID()));
		if (fieldImage.length != 0) {
			Image fieldMynd = new Image(fieldImage[0].getImageID());
			fieldMynd.setHorizontalSpacing(6);
			if (iMaxImageWidth > 0) {
				fieldMynd.setMaxImageWidth(iMaxImageWidth);
			}

			infoTable.add(fieldMynd, column++, 1);
		}

		TextReader fieldText = null;
		HoleText[] hole_text = (HoleText[]) ((HoleText) IDOLookup.instanciateEntity(HoleText.class)).findAllByColumn("field_id", "" + field.getID(), "hole_number", "0");
		
		if (hole_text.length > 0) {
			if(isAdmin() && hole_text[0].getTextID() < 0) {
				TxText text = TextBusiness.saveText(-1,-1,iwc.getCurrentLocaleId(),iwc.getCurrentUserId(),this.getICObjectInstanceID(),null,null,"","","",null,null);
				hole_text[0].setTextID(text.getID());
				hole_text[0].store();
			}
			fieldText = new TextReader(hole_text[0].getTextID());
			fieldText.setEnableDelete(false);
			fieldText.setCacheable(false);
		}

		if (fieldText != null) {
			infoTable.add(fieldText, column, 1);
		}
		infoTable.setRowVerticalAlignment(1, Table.VERTICAL_ALIGN_TOP);
	}

	public Form getHoleChooser(String field_id) throws IOException {

		Form myForm = new Form();
		myForm.add(new HiddenInput("field_id", field_id));
		if (holeViewPage != null) {
			myForm.setPageToSubmitTo(holeViewPage);
		}

		DropdownMenu holeChooser = new DropdownMenu("hole_number");

		for (int a = 1; a <= 18; a++) {

			holeChooser.addMenuElement(a, a + ". hola");

		}

		Table myTable = new Table();
		myTable.setCellpadding(0);
		myTable.setCellspacing(0);
		myTable.setCellpaddingRight(1, 1, 5);
		myTable.setCellpaddingRight(2, 1, 5);

		Text holeText = new Text(getResourceBundle().getLocalizedString("field.select_hole", "Select hole"));
		holeText.setBold();

		SubmitButton senda = new SubmitButton(getResourceBundle().getLocalizedString("field.get_hole", "Get"));

		myTable.add(holeText, 1, 1);
		myTable.add(holeChooser, 2, 1);
		myTable.add(senda, 3, 1);

		myForm.add(myTable);

		return myForm;

	}

	public Table getHoleInfo(IWContext iwc, Field field, String field_id, String hole_number) throws IOException, SQLException {

		Table outerTable = new Table();
		outerTable.setWidth("100%");
		outerTable.setCellpadding(0);
		outerTable.setCellspacing(0);
		int row = 1;

		Table myTable = new Table();
		myTable.setColor("000000");
		myTable.setCellpadding(6);
		myTable.setCellspacing(2);

		Tee[] tee = (Tee[]) ((Tee) IDOLookup.instanciateEntity(Tee.class)).findAll("select * from tee where field_id='" + String.valueOf(field.getID()) + "' and hole_number='" + hole_number + "' and tee_color_id<5");

		for (int a = 0; a < tee.length; a++) {

			if (a == 0) {
				Text parText = new Text("Par " + tee[a].getPar());
				parText.setBold();
				parText.setFontSize(4);

				Text handicapText = new Text(getResourceBundle().getLocalizedString("field.handicap", "Handicap") + Text.NON_BREAKING_SPACE + ((int) tee[a].getFloatColumnValue("handicap")));
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

		if (tee.length > 0) {
			if (tee[0].getStringColumnValue("hole_name") != null) {
				Text holeName = getBigHeader(tee[0].getStringColumnValue("hole_name"));
				outerTable.add(holeName, 1, row++);
				outerTable.setHeight(row++, 6);
			}
		}

		TeeImage[] teeImage = (TeeImage[]) ((TeeImage) IDOLookup.instanciateEntity(TeeImage.class)).findAllByColumn("field_id", String.valueOf(field.getID()), "hole_number", hole_number);
		if (teeImage.length != 0) {
			Image teeMynd = new Image(teeImage[0].getImageID());
			outerTable.setAlignment(1, row, Table.HORIZONTAL_ALIGN_CENTER);
			outerTable.add(teeMynd, 1, row++);
			outerTable.setHeight(row++, 6);
		}

		outerTable.setAlignment(1, row, Table.HORIZONTAL_ALIGN_CENTER);
		outerTable.add(myTable, 1, row++);
		outerTable.setHeight(row++, 18);

		TextReader fieldText = null;
		HoleText[] hole_text = (HoleText[]) ((HoleText) IDOLookup.instanciateEntity(HoleText.class)).findAllByColumn("field_id", "" + field.getID(), "hole_number", hole_number);
		if (hole_text.length > 0) {
			if(isAdmin() && hole_text[0].getTextID() < 0) {
				TxText text = TextBusiness.saveText(-1,-1,iwc.getCurrentLocaleId(),iwc.getCurrentUserId(),this.getICObjectInstanceID(),null,null,"","","",null,null);
				hole_text[0].setTextID(text.getID());
				hole_text[0].store();
			}
			fieldText = new TextReader(hole_text[0].getTextID());
			fieldText.setEnableDelete(false);
			fieldText.setCacheable(false);
			outerTable.add(fieldText, 1, row);
		}

		return outerTable;
	}

	public String scale_decimals(String nyForgjof, int scale) throws IOException {

		BigDecimal test2 = new BigDecimal(nyForgjof);

		String nyForgjof2 = test2.setScale(scale, 5).toString();

		return nyForgjof2;

	}
  
	/**
   * @param gameHandicapPage The gameHandicapPage to set.
   */
  public void setGameHandicapPage(ICPage gameHandicapPage) {
    this.gameHandicapPage = gameHandicapPage;
  }

  /**
	 * @param maxImageWidth The iMaxImageWidth to set.
	 */
	public void setMaxImageWidth(int maxImageWidth) {
		iMaxImageWidth = maxImageWidth;
	}

	/**
	 * @param holeViewPage The holeViewPage to set.
	 */
	public void setHoleViewPage(ICPage holeViewPage) {
		this.holeViewPage = holeViewPage;
	}
}