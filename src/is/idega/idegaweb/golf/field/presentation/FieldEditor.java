/*
 * Created on 4.3.2004
 */
package is.idega.idegaweb.golf.field.presentation;

import is.idega.idegaweb.golf.entity.Field;
import is.idega.idegaweb.golf.entity.FieldHome;
import is.idega.idegaweb.golf.entity.FieldImage;
import is.idega.idegaweb.golf.entity.FieldImageHome;
import is.idega.idegaweb.golf.entity.HoleText;
import is.idega.idegaweb.golf.entity.HoleTextHome;
import is.idega.idegaweb.golf.entity.Tee;
import is.idega.idegaweb.golf.entity.TeeColor;
import is.idega.idegaweb.golf.entity.TeeHome;
import is.idega.idegaweb.golf.entity.TeeImage;
import is.idega.idegaweb.golf.entity.TeeImageHome;
import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.presentation.GolfBlock;
import is.idega.idegaweb.golf.templates.page.GolfWindow;

import java.sql.SQLException;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.media.presentation.ImageInserter;
import com.idega.block.text.business.TextBusiness;
import com.idega.block.text.data.TxText;
import com.idega.block.text.presentation.TextReader;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;

/**
 * @author laddi
 */
public class FieldEditor extends GolfWindow {

	public FieldEditor() {
		setWidth(850);
		setHeight(580);
		setTitle("Field Editor");
		setScrollbar(true);
		setResizable(true);
		setMenubar(false);
		setStatus(false);
		add(new Editor());
	}

	public class Editor extends GolfBlock {

		private int field_id = -1;

//		private String table_header_color = "#cedfd0";
//
//		private String table_color = "#cedfd0";
//
//		private String table_border_color = "#336660";

		public void main(IWContext modinfo) throws Exception {
			if (isAdmin() || isClubAdmin()) {
				checkAction(modinfo);
			}
			else {
				add("Ekki næg réttindi");
			}
		}

		protected boolean checkForId(IWContext modinfo) {

			boolean returner = false;

			String temp_field_id = modinfo.getParameter("field_id");
			if (temp_field_id != null) {
				this.field_id = Integer.parseInt(temp_field_id);
				modinfo.setSessionAttribute("field_admin_id", temp_field_id);
			}
			else {
				String temp_field_id_2 = (String) modinfo.getSessionAttribute("field_admin_id");
				if (temp_field_id_2 != null) {
					this.field_id = Integer.parseInt(temp_field_id_2);
				}
			}

			if (this.field_id != -1) {
				returner = true;
			}

			return returner;
		}

		protected void action(IWContext modinfo) throws SQLException, FinderException {
			TeeColor[] tee_color = (TeeColor[]) ((TeeColor) IDOLookup.instanciateEntity(TeeColor.class)).findAllOrdered("TEE_COLOR_NAME");
			Field field = ((FieldHome) IDOLookup.getHomeLegacy(Field.class)).findByPrimaryKey(this.field_id);

			int row = 1;
			int textInputSize = 2;
			boolean displayPar = false;
			boolean parDone = false;
			boolean displayHandicap = false;
			boolean handicapDone = false;
			boolean displayEmptyHandicap = true;
			boolean displayEmptyPar = true;

			TextInput text_input;
			Link link;
			Link image_link;
			int imageID = this.checkForImage(modinfo);

			Form form = new Form();

			Table backTable = new Table(1, 1);
//			backTable.setColor("#336660");
			form.add(backTable);

			Table table = new Table();
			backTable.add(table, 1, 1);
			table.setBorder(0);
			table.setCellpadding(1);
			table.setCellspacing(0);
//			table.setColor("#CEDFD0");

			table.add("<b>" + field.getName() + "</b>", 1, row);
			table.mergeCells(1, row, 23, row);
			table.setAlignment(1, row, "center");

			row++;
			table.mergeCells(1, row, 23, row);
			table.setAlignment(1, row, "center");
			
			ImageInserter imageInsert = new ImageInserter();
			imageInsert.setHiddenInputName("field_image_id");
			imageInsert.setHasUseBox(false);
			table.add(imageInsert, 1, row);

			if (imageID != -1) {
				imageInsert.setImageId(imageID);
				table.add("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", 1, row);
				Link henda = new Link("Henda mynd");
				henda.addParameter("action", "delete_field_image");
				table.add(henda, 1, row);
			}

			row++;
			table.add("Holur", 1, row);
			table.mergeCells(1, row, 22, row);
			table.setAlignment(1, row, "center");
			++row;
			table.add("Teigur", 1, row);
			for (int i = 1; i <= 18; i++) {
				table.add(i + "", 1 + i, row);
			}
			table.add("CR", 21, row);
			table.add("Slope", 22, row);

			++row;

			if ((tee_color != null) && (tee_color.length > 0)) {

				for (int i = 0; i < tee_color.length; i++) {
					table.add(tee_color[i].getName(), 1, i + row);

					Tee[] tee = (Tee[]) ((Tee) IDOLookup.instanciateEntity(Tee.class)).findAllByColumnOrdered("field_id", this.field_id + "", "tee_color_id", tee_color[i].getID() + "", "hole_number");

					if ((tee != null) && (tee.length > 0)) {

						link = new Link(getBundle().getImage("shared/trash.gif", "Henda " + tee_color[i].getName()));
						link.addParameter("action", "delete");
						link.addParameter("tee_color_id", tee_color[i].getID());
						table.add(link, 23, row + i);

						for (int j = 0; j < tee.length; j++) {
							displayEmptyHandicap = false;
							displayEmptyPar = false;

							text_input = new TextInput(i + "liturhola", tee[j].getTeeLength() + "");
							text_input.setSize(3);
							table.add(text_input, j + 2, i + row);

							if (tee[j].getHandicap() != -1) {
								displayHandicap = true;
							}
							if (tee[j].getPar() != -1) {
								displayPar = true;
							}

							text_input = new TextInput("forgjof", "");

							if (!handicapDone) if (displayHandicap) {
								text_input.setValue(((int) tee[j].getHandicap()));
								text_input.setSize(2);
								table.add(text_input, j + 2, tee_color.length + row + 1);
							}

							text_input = new TextInput("par", "");
							if (!parDone) if (displayPar) {
								text_input.setValue(tee[j].getPar());
								text_input.setSize(2);
								table.add(text_input, j + 2, tee_color.length + row + 2);
							}

							if (j == tee.length - 1) {
								text_input = new TextInput(i + "liturcr", tee[j].getCourseRating() + "");
								text_input.setSize(4);
								table.add(text_input, j + 4, i + row);
								text_input = new TextInput(i + "liturslope", tee[j].getSlope() + "");
								text_input.setSize(3);
								table.add(text_input, j + 5, i + row);
							}
						}
					}
					else {
						for (int j = 0; j < 18; j++) {
							text_input = new TextInput(i + "liturhola", "");
							text_input.setSize(3);
							table.add(text_input, j + 2, i + row);
							if (j == 17) {
								text_input = new TextInput(i + "liturcr", "");
								text_input.setSize(4);
								table.add(text_input, j + 4, i + row);
								text_input = new TextInput(i + "liturslope", "");
								text_input.setSize(3);
								table.add(text_input, j + 5, i + row);
							}

						}

					}
					if (displayHandicap) {
						handicapDone = true;
					}
					displayHandicap = false;
					if (displayPar) {
						parDone = true;
					}
					displayPar = false;

				}

				if (displayEmptyHandicap) {
					for (int j = 0; j < 18; j++) {
						text_input = new TextInput("forgjof", "");
						text_input.setSize(2);
						table.add(text_input, j + 2, tee_color.length + row + 1);
					}
				}

				if (displayEmptyPar) {
					for (int j = 0; j < 18; j++) {
						text_input = new TextInput("par", "");
						text_input.setSize(2);
						table.add(text_input, j + 2, tee_color.length + row + 2);
					}
				}

				row = row + tee_color.length + 1;

				table.setWidth(20, "22");

				table.add("Forgjöf", 1, row);
				row++;

				table.add("Par", 1, row);

				row++;

				for (int i = 1; i < 19; i++) {
					image_link = new Link(getBundle().getImage("shared/change.gif", "Skoða holu " + i + " nánar"));
					image_link.addParameter("action", "view_hole");
					image_link.addParameter("hole_number", i + "");

					table.add(image_link, i + 1, row);

				}

				table.mergeCells(21, row, 23, row);
				table.setAlignment(21, row, "right");
				table.add(new SubmitButton("takki", "Uppfæra"), 21, row);
				table.add(new HiddenInput("action", "update"), 21, row);

				row++;

				//        table.mergeCells(1,row,22,row);

			}

			Table rightTable = new Table();
			int text_id = getHoleTextId(modinfo, 0);
			if (text_id != -1) {
				TextReader reader = new TextReader(text_id);
				reader.setEnableDelete(false);
				reader.setCacheable(false);
				rightTable.add(reader, 1, 1);
			}

			add(form);
			add(rightTable);
		}

		protected int checkForImage(IWContext modinfo) throws SQLException {
			int returner = -1;

			FieldImage[] images = null;
			images = (FieldImage[]) ((FieldImage) IDOLookup.instanciateEntity(FieldImage.class)).findAllByColumn("field_id", "" + this.field_id);
			if (images.length > 0) {
				returner = images[0].getImageID();

			}

			return returner;
		}

		protected int checkForTeeImage(IWContext modinfo, String hole_number) throws SQLException {
			int returner = -1;

			TeeImage[] images = null;
			images = (TeeImage[]) ((TeeImage) IDOLookup.instanciateEntity(TeeImage.class)).findAllByColumn("hole_number", hole_number, "field_id", "" + this.field_id);
			if (images.length > 0) {
				returner = images[0].getImageID();
			}

			return returner;
		}

		protected void viewHole(IWContext modinfo) throws SQLException, FinderException {
			String hole_number = modinfo.getParameter("hole_number");

			if (hole_number != null) {
				Field field = ((FieldHome) IDOLookup.getHomeLegacy(Field.class)).findByPrimaryKey(this.field_id);
				TeeColor[] tee_color = (TeeColor[]) ((TeeColor) IDOLookup.instanciateEntity(TeeColor.class)).findAllOrdered("TEE_COLOR_NAME");
				int par = 0;
				int handicap = 0;
				boolean needsToUpdate = false;
				String hole_name = "";
				String temp_hole_name = "";

				int row = 1;
				int imageID = this.checkForTeeImage(modinfo, hole_number);

				Table contentTable = new Table(2, 1);
//				contentTable.setWidth("100%");
				//            contentTable.setAlignment(2,1,"right");
				contentTable.setVerticalAlignment(1, 1, "top");
				contentTable.setVerticalAlignment(2, 1, "top");
				contentTable.setBorder(0);

				Form form = new Form();

				Table backTable = new Table(1, 1);
//				backTable.setColor("#336660");
				form.add(backTable);

				Table table = new Table();
				backTable.add(table, 1, 1);
				table.setBorder(0);
				table.setCellpadding(1);
				table.setCellspacing(0);
//				table.setColor("#CEDFD0");

				table.add(field.getName(), 1, row);
				table.setAlignment(7, row, "right");
				table.add("Hola: " + hole_number, 7, row);

				++row;

				table.mergeCells(1, row, 7, row);
				table.setAlignment(1, row, "center");
				//                if (image_id != null) {
				//                  form.add(new HiddenInput("field_image_id",image_id));
				//                }

				ImageInserter imageInsert = new ImageInserter();
				imageInsert.setHiddenInputName("hole_image_id");
				imageInsert.setHasUseBox(false);
				table.add(imageInsert, 1, row);

				if (imageID != -1) {
					imageInsert.setImageId(imageID);
					table.add("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", 1, row);
					Link henda = new Link("Henda mynd");
					henda.addParameter("hole_number", hole_number);
					henda.addParameter("action", "delete_tee_image");
					table.add(henda, 1, row);

				}

				++row;

				for (int i = 0; i < tee_color.length; i++) {
					Tee[] tee = (Tee[]) ((Tee) IDOLookup.instanciateEntity(Tee.class)).findAll("Select * from tee where field_id=" + this.field_id + " AND tee_color_id=" + tee_color[i].getID() + " AND hole_number=" + hole_number + "");
					for (int j = 0; j < tee.length; j++) {
						++row;
						table.add(tee_color[i].getName(), 1, row);
						table.add("" + tee[j].getTeeLength(), 2, row);

						if (i == 0) {
							par = tee[i].getPar();
							handicap = (int) tee[i].getHandicap();
							temp_hole_name = tee[i].getName();
							if (temp_hole_name == null) {
								hole_name = "";
							}
							else {
								hole_name = temp_hole_name;
							}
						}

					}

				}
				table.mergeCells(6, row, 7, row);
				table.setAlignment(6, row, "right");
				table.add(new SubmitButton("jamm", "Uppfæra"), 6, row);

				++row;
				++row;
				row = 3;

				table.mergeCells(2, row, 7, row);
				table.add("Nafn:", 1, row);
				table.add(new TextInput("i_hole_name", hole_name), 2, row);

				++row;
				table.add("Par:", 6, row);
				table.add("" + par, 7, row);

				++row;
				table.add("Forgjöf: ", 6, row);
				table.add("" + handicap, 7, row);
				/*
				 * ++row; table.add("CR: ",6,row); table.add(""+CR,7,row); ++row;
				 * table.add("Slope: ",6,row); table.add(""+slope,7,row);
				 */
				++row;
				++row;

				if (!hole_number.equals("1")) {
					table.mergeCells(1, 11, 2, 11);
					Link previous = new Link("Fyrri hola");
					previous.addParameter("action", "view_hole");
					previous.addParameter("hole_number", (Integer.parseInt(hole_number) - 1));
					table.add(previous, 1, 11);
				}
				if (!hole_number.equals("18")) {
					table.mergeCells(6, 11, 7, 11);
					table.setAlignment(6, 11, "right");
					Link next = new Link("Næsta hola");
					next.addParameter("action", "view_hole");
					next.addParameter("hole_number", (Integer.parseInt(hole_number) + 1));
					table.add(next, 6, 11);
				}

				table.add(new HiddenInput("hole_number", hole_number), 6, row);
				table.add(new HiddenInput("action", "update_tee"), 6, row);

				contentTable.add(form, 1, 1);
				//     add(form);

				Table rightTable = new Table();
				int text_id = getHoleTextId(modinfo, Integer.parseInt(hole_number));
				if (text_id != -1) {
					TextReader reader = new TextReader(text_id);
					reader.setEnableDelete(false);
					reader.setCacheable(false);
					rightTable.add(reader, 1, 1);
				}

				contentTable.add(rightTable, 2, 1);
				contentTable.setWidth(1, 1, "300");

				add(contentTable);

				add("<br>");
				add("<br>");
				add(new Link("Til baka"));
			}
		}

		private int updateHoleText(IWContext modinfo, int hole_number) throws SQLException {
			int returner = -1;

			if (hole_number != -1) {
				TxText text = (TxText) IDOLookup.createLegacy(TxText.class);

				

				try {
					HoleText[] hole_texts = (HoleText[]) ((HoleText) IDOLookup.instanciateEntity(HoleText.class)).findAllByColumn("FIELD_ID", "" + this.field_id, "hole_number", "" + hole_number);
					if (hole_texts.length > 0) {
						hole_texts[0].setTextID(returner);
						hole_texts[0].store();
						returner = text.getID();
					}
					else {
						HoleText holeText = ((HoleTextHome) IDOLookup.getHomeLegacy(HoleText.class)).create();
						holeText.setTextID(returner);
						holeText.setHoleNumber(hole_number);
						holeText.setFieldId(this.field_id);
						holeText.store();
						returner = text.getID();
						
						if(holeText.getTextID() < 0) {
							TxText hText = TextBusiness.saveText(-1,-1,modinfo.getCurrentLocaleId(),modinfo.getCurrentUserId(),this.getICObjectInstanceID(),null,null,"","","",null,null);
							holeText.setTextID(hText.getID());
							holeText.store();
						}
					}
				}
				catch (CreateException ce) {
					throw new SQLException(ce.getMessage());
				}
				
			}

			return returner;
		}

		private int getHoleTextId(IWContext modinfo, int hole_number) throws SQLException {
			int returner = -1;

			try {
				HoleText[] hole_texts = (HoleText[]) ((HoleText) IDOLookup.instanciateEntity(HoleText.class)).findAllByColumn("FIELD_ID", "" + this.field_id, "hole_number", "" + hole_number);
				if (hole_texts.length > 0) {
					returner = hole_texts[0].getTextID();
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}

			if (returner == -1) {
				returner = updateHoleText(modinfo, hole_number);
			}

			return returner;
		}

		protected void updateTee(IWContext modinfo) throws SQLException, FinderException {
			String hole_image_id = modinfo.getParameter("hole_image_id");
			String hole_number = modinfo.getParameter("hole_number");

			// saving image...
			if ((hole_image_id != null) && (hole_number != null)) {
				TeeImage[] images = null;
				images = (TeeImage[]) ((TeeImage) IDOLookup.instanciateEntity(TeeImage.class)).findAllByColumn("hole_number", hole_number, "field_id", "" + this.field_id);
				if (images != null) {
					if (images.length > 0) {
						for (int i = 0; i < images.length; i++) {
							images[i].setImageID(Integer.parseInt(hole_image_id));
							images[i].store();
						}
					}
					else {
						try {
							TeeImage tee_image = ((TeeImageHome) IDOLookup.getHomeLegacy(TeeImage.class)).create();
							tee_image.setHoleNumber(Integer.parseInt(hole_number));
							tee_image.setFieldId(this.field_id);
							tee_image.setImageID(Integer.parseInt(hole_image_id));
							tee_image.store();
						}
						catch (CreateException ce) {
							throw new SQLException(ce.getMessage());
						}
					}
				}
			}

			// saving tee(s)...
			if (hole_number != null) {
				String hole_name = modinfo.getParameter("i_hole_name");
				Tee[] tees = (Tee[]) ((Tee) IDOLookup.instanciateEntity(Tee.class)).findAll("Select * from tee where field_id=" + this.field_id + "  AND hole_number=" + hole_number + "");
				for (int i = 0; i < tees.length; i++) {
					tees[i].setHoleName(hole_name);
					tees[i].update();
				}

			}

			viewHole(modinfo);
		}

		protected void deleteTeeImage(IWContext modinfo) throws SQLException, FinderException {
			String hole_number = modinfo.getParameter("hole_number");

			if (hole_number != null) {
				TeeImage[] images = null;
				images = (TeeImage[]) ((TeeImage) IDOLookup.instanciateEntity(TeeImage.class)).findAllByColumn("hole_number", hole_number, "field_id", "" + this.field_id);

				if (images != null) {
					if (images.length > 0) {
						for (int i = 0; i < images.length; i++) {
							images[i].delete();
						}
					}
				}
			}

			viewHole(modinfo);
		}

		protected void deleteFieldImage(IWContext modinfo) throws SQLException {
			FieldImage[] images = null;
			images = (FieldImage[]) ((FieldImage) IDOLookup.instanciateEntity(FieldImage.class)).findAllByColumn("field_id", "" + this.field_id);

			if (images != null) {
				if (images.length > 0) {
					for (int i = 0; i < images.length; i++) {
						images[i].delete();
					}
				}
			}

			add("augnablik...");
			getParentPage().setToRedirect(modinfo.getRequest().getRequestURI());
		}

		protected void update(IWContext modinfo) throws SQLException, FinderException {
			String field_image_id = modinfo.getParameter("field_image_id");

			String[] hola_0 = modinfo.getParameterValues("0liturhola");
			String[] hola_1 = modinfo.getParameterValues("1liturhola");
			String[] hola_2 = modinfo.getParameterValues("2liturhola");
			String[] hola_3 = modinfo.getParameterValues("3liturhola");
			String[] hola_4 = modinfo.getParameterValues("4liturhola");
			String[] hola_5 = modinfo.getParameterValues("5liturhola");

			String[][] hola = { hola_0, hola_1, hola_2, hola_3, hola_4, hola_5};

			String[] forgjof = modinfo.getParameterValues("forgjof");
			String[] par = modinfo.getParameterValues("par");

			String[] temp_holu_array;

			for (int i = 0; i < hola.length; i++) {
				String[] temp = hola[i];
			}

			TeeColor[] tee_color = (TeeColor[]) ((TeeColor) IDOLookup.instanciateEntity(TeeColor.class)).findAllOrdered("TEE_COLOR_NAME");

			Field field = ((FieldHome) IDOLookup.getHomeLegacy(Field.class)).findByPrimaryKey(this.field_id);
			if (field_image_id != null) {
				FieldImage[] field_image = (FieldImage[]) ((FieldImage) IDOLookup.instanciateEntity(FieldImage.class)).findAllByColumn("field_id", this.field_id);
				if (field_image.length > 0) {
					field_image[0].setImageID(Integer.parseInt(field_image_id));
					field_image[0].update();
				}
				else {
					try {
						FieldImage f_i = ((FieldImageHome) IDOLookup.getHomeLegacy(FieldImage.class)).create();
						f_i.setFieldId(this.field_id);
						f_i.setImageID(Integer.parseInt(field_image_id));
						f_i.insert();
					}
					catch (CreateException ce) {
						throw new SQLException(ce.getMessage());
					}
				}
			}

			boolean alright = false;
			boolean color_0 = false;
			boolean color_1 = false;
			boolean color_2 = false;
			boolean color_3 = false;
			boolean color_4 = false;
			boolean color_5 = false;

			boolean forgjof_bl = true;
			boolean par_bl = true;

			for (int i = 0; i < forgjof.length; i++) {
				if (forgjof[i].equalsIgnoreCase("")) {
					forgjof_bl = false;
					continue;
				}
			}
			for (int i = 0; i < par.length; i++) {
				if (par[i].equalsIgnoreCase("")) {
					par_bl = false;
					continue;
				}
			}

			if ((par_bl) && (forgjof_bl)) {
				// check which to update;

				for (int i = 0; i < hola_0.length; i++) {
					if (!hola_0[i].equalsIgnoreCase("")) {
						color_0 = true;
						continue;
					}
				}

				alright = false;
				for (int i = 0; i < hola_1.length; i++) {
					if (!hola_1[i].equalsIgnoreCase("")) {
						color_1 = true;
						continue;
					}
				}

				alright = false;
				for (int i = 0; i < hola_2.length; i++) {
					if (!hola_2[i].equalsIgnoreCase("")) {
						color_2 = true;
						continue;
					}
				}

				alright = false;
				for (int i = 0; i < hola_3.length; i++) {
					if (!hola_3[i].equalsIgnoreCase("")) {
						color_3 = true;
						continue;
					}
				}

				alright = false;
				for (int i = 0; i < hola_4.length; i++) {
					if (!hola_4[i].equals("")) {
						color_4 = true;
						continue;
					}
				}

				alright = false;
				for (int i = 0; i < hola_5.length; i++) {
					if (!hola_5[i].equalsIgnoreCase("")) {
						color_5 = true;
						continue;
					}
				}

				//update selected tees
				try {
					for (int i = 0; i < tee_color.length; i++) {
						if (updateThis(i, color_0, color_1, color_2, color_3, color_4, color_5)) {
							String slope = modinfo.getParameter(i + "liturslope");
							String cr = modinfo.getParameter(i + "liturcr");
							temp_holu_array = hola[i];
							if (slope.equals("")) {
								slope = "113";
							}
							if (cr.equals("")) {
								cr = "70";
							}
							Tee[] tee = (Tee[]) ((Tee) IDOLookup.instanciateEntity(Tee.class)).findAllByColumnOrdered("field_id", this.field_id + "", "tee_color_id", tee_color[i].getID() + "", "hole_number");
							if (tee.length > 0) {
								for (int j = 0; j < tee.length; j++) {
									//                          add("<br> litur: "+i+" hola : "+j+" lengd:
									// "+temp_holu_array[j]+" forgjof: "+forgjof[j] +" par :
									// "+par[j]+" slope: "+slope+" cr: "+cr);
									tee[j].setSlope(Integer.parseInt(slope));
									tee[j].setCourseRating(cr);
									tee[j].setHoleNumber(j + 1);
									tee[j].setHoleName("");
									tee[j].setPar(Integer.parseInt(par[j]));
									tee[j].setHandicap(forgjof[j]);
									tee[j].setTeeLength(Integer.parseInt(temp_holu_array[j]));
									tee[j].update();

								}
							}
							else {
								tee = new Tee[18];

								for (int j = 0; j < tee.length; j++) {
									tee[j] = ((TeeHome) IDOLookup.getHomeLegacy(Tee.class)).create();
									tee[j].setFieldID(this.field_id);
									tee[j].setTeeColorID(tee_color[i].getID());
									tee[j].setSlope(Integer.parseInt(slope));
									tee[j].setCourseRating(cr);
									tee[j].setHoleNumber(j + 1);
									tee[j].setHoleName("");
									tee[j].setPar(Integer.parseInt(par[j]));
									tee[j].setHandicap(forgjof[j]);
									tee[j].setTeeLength(Integer.parseInt(temp_holu_array[j]));
									tee[j].insert();

								}
							}

						} //if
					} //for
					//                add("Uppfærslu lokið");
					this.action(modinfo);
				}
				catch (Exception e) {
					add("Eitthvað mis");
				}

			}
			else { // if ! par && forgjof
				if (par_bl) add("forgjöf er ófullnægjandi");

				if (forgjof_bl) add("par er ófullnægjandi");
			}

		}

		protected boolean updateThis(int i, boolean color_0, boolean color_1, boolean color_2, boolean color_3, boolean color_4, boolean color_5) {
			boolean returner = false;

			switch (i) {
				case 0:
					if (color_0) {
						returner = true;
					}
					break;
				case 1:
					if (color_1) {
						returner = true;
					}
					break;
				case 2:
					if (color_2) {
						returner = true;
					}
					break;
				case 3:
					if (color_3) {
						returner = true;
					}
					break;
				case 4:
					if (color_4) {
						returner = true;
					}
					break;
				case 5:
					if (color_5) {
						returner = true;
					}
					break;
			}

			return returner;
		}

		protected void deleteFieldTeeColor(IWContext modinfo) throws SQLException, FinderException {
			String tee_color_id = modinfo.getRequest().getParameter("tee_color_id");

			if (tee_color_id != null) {
				Tee[] tee = (Tee[]) ((Tee) IDOLookup.instanciateEntity(Tee.class)).findAllByColumnOrdered("field_id", this.field_id + "", "tee_color_id", tee_color_id + "", "hole_number");

				if (tee != null) {
					for (int i = 0; i < tee.length; i++) {
						tee[i].delete();
					}
				}

			}

			this.action(modinfo);

		}

		protected void createField(IWContext modinfo) throws SQLException {
			String union_id = modinfo.getParameter("i_field_union_id");

			Field[] fields = (Field[]) ((Field) IDOLookup.instanciateEntity(Field.class)).findAllByColumn("union_id", union_id);

			if (fields.length == 0) {
				Form form = new Form();

				Table content = new Table(1, 1);
				form.add(content);
//				content.setColor(this.table_border_color);
				Table table = new Table(2, 5);
				content.add(table);
				table.setBorder(0);
//				table.setColor(this.table_color);

				DropdownMenu unions = new DropdownMenu((Union[]) ((Union) IDOLookup.instanciateEntity(Union.class)).findAllOrdered("name"), "i_field_union_id");
				unions.setSelectedElement(union_id);
				TextInput name = new TextInput("i_field_field_name");
				TextInput par = new TextInput("i_field_field_par");

				table.add("<b>Nýr Völlur</b>", 1, 1);
				table.mergeCells(1, 1, 2, 1);
				table.add("Nafn", 1, 2);
				table.add("Klúbbur", 1, 3);
				table.add("Par", 1, 4);
				table.add(new SubmitButton("jahhh", "Smíða"), 2, 5);
				table.add(new HiddenInput("action", "save_new_field"), 2, 5);
				table.setAlignment(2, 5, "right");

				table.add(name, 2, 2);
				table.add(unions, 2, 3);
				table.add(par, 2, 4);

				add(form);
				add("jahh smíða völl hér" + union_id);
			}
			else {
				add("Þar er þegar til völlur í þessum klúbb");
			}
		}

		protected void saveNewField(IWContext modinfo) throws SQLException {
			String name = modinfo.getParameter("i_field_field_name");
			String union_id = modinfo.getParameter("i_field_union_id");
			String par = modinfo.getParameter("i_field_field_par");

			int intPar;
			boolean parOk = false;
			boolean nameOk = false;

			if (!name.equalsIgnoreCase("")) {
				nameOk = true;
			}
			try {
				intPar = Integer.parseInt(par);
				parOk = true;
			}
			catch (NumberFormatException n) {
			}

			if ((parOk) && (nameOk)) {
				Field field = null;
				try {
					field = ((FieldHome) IDOLookup.getHomeLegacy(Field.class)).create();
					field.setUnionID(union_id);
					field.setName(name);
					field.setNumberOfHoles(18);
					field.setFieldPar(par);
					field.insert();
				}
				catch (CreateException ce) {
					throw new SQLException(ce.getMessage());
				}

				modinfo.setSessionAttribute("field_admin_id", "" + field.getID());

				add("Völlur buinn til<br>");
				Form form = new Form();
				form.add(new SubmitButton("jahh", "Áfram"));
				add(form);
			}

			else {
				if (!nameOk) {
					add("Nafn er ekki í lagi<br>");
				}
				if (!parOk) {
					add("Par er ekki í lagi<br>");
				}
				add(new BackButton("Til baka"));

			}

		}

		protected void checkAction(IWContext modinfo) throws SQLException, FinderException {
			String action = modinfo.getParameter("action");

			setTitle(getResourceBundle().getLocalizedString("field.field_editor", "Field Editor"));

			if (checkForId(modinfo)) {
				if (action == null) {
					action(modinfo);
				}
				else if (action.equals("update")) {
					update(modinfo);
				}
				else if (action.equals("delete")) {
					deleteFieldTeeColor(modinfo);
				}
				else if (action.equals("view_hole")) {
					viewHole(modinfo);
				}
				else if (action.equals("update_tee")) {
					updateTee(modinfo);
				}
				else if (action.equals("delete_tee_image")) {
					deleteTeeImage(modinfo);
				}
				else if (action.equals("delete_field_image")) {
					deleteFieldImage(modinfo);
				}
			}
			else {
				if (action == null) {
					add("Enginn golfvöllur var valinn");
					String union_id = modinfo.getParameter("union_id");
					if (union_id != null) {
						Form myForm = new Form();
						myForm.add(new HiddenInput("action", "create_field"));
						myForm.add(new HiddenInput("i_field_union_id", union_id));
						myForm.add(new SubmitButton("jahh_bestir", "Smíða nýjan völlur"));
						add(myForm);
					}

				}
				else if (action.equals("create_field")) {
					createField(modinfo);
				}
				else if (action.equals("save_new_field")) {
					saveNewField(modinfo);
				}

			}
		}
	}
}