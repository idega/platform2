package com.idega.block.contract.presentation;

import java.sql.SQLException;
import java.util.List;

import com.idega.block.contract.business.ContractBusiness;
import com.idega.block.contract.business.ContractFinder;
import com.idega.block.contract.business.ContractWriter;
import com.idega.block.contract.data.ContractCategory;
import com.idega.block.contract.data.ContractCategoryHome;
import com.idega.block.contract.data.ContractTag;
import com.idega.block.contract.data.ContractText;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.util.Edit;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */
public class ContractTextSetter extends com.idega.presentation.PresentationObjectContainer {
	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.contract";
	protected IWResourceBundle iwrb;
	protected IWBundle iwb;
	private String localesParameter = "iw_locales";
	private String bottomThickness = "8";
	private boolean isAdmin;
	public final static String prmCategoryId = "con_cat";
	public ContractTextSetter() {
	}
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	protected void control(IWContext iwc)throws Exception {
		iwb = getBundle(iwc);
		iwrb = getResourceBundle(iwc);
		int iCategoryId = -1;
		if (isAdmin) {
			if (iwc.isParameterSet(prmCategoryId))
				iCategoryId = Integer.parseInt(iwc.getParameter(prmCategoryId));
			//add(getPDFLink(iwb.getImage("print.gif")));
			if (iwc.getParameter("savetitle") != null) {
				updateTitleForm(iwc, iCategoryId);
				add(getMainTable(iCategoryId));
			}
			else if (iwc.getParameter("savetext") != null) {
				updateForm(iwc, iCategoryId);
				add(getMainTable(iCategoryId));
			}
			else if (iwc.getParameter("delete") != null) {
				add(ConfirmDelete(iwc));
			}
			else if (iwc.getParameter("conf_delete") != null) {
				deleteText(iwc);
				add(getMainTable(iCategoryId));
			}
			else if (iwc.getParameter("text_id") != null || iwc.getParameter("new_text") != null) {
				add(getSetupForm(iwc, iCategoryId));
			}
			else if (iwc.getParameter("new_title") != null) {
				add(getTitleForm(iwc, iCategoryId));
			}
			else if (iwc.getParameter("title_id") != null) {
				add(getTitleForm(iwc, iCategoryId));
			}
			else {
				add(getMainTable(iCategoryId));
			}
		}
		else
			add(iwrb.getLocalizedString("access_denied", "Access_denied"));
	}
	private PresentationObject getMainTable(int iCategoryId) {
		Table T = new Table();
		T.setCellpadding(0);
		T.setCellspacing(0);
		T.setWidth("100%");
		List L = ContractFinder.listOfContractTexts(iCategoryId);
		String sTitle = getTitle(iCategoryId);

		Link newTitleLink = new Link();
		if (sTitle != null) {
			newTitleLink.addParameter("title_id", iCategoryId);
		}
		else {
			sTitle = iwrb.getLocalizedString("new_title", "New Title");
			newTitleLink.addParameter("new_title", "new_title");
		}
		newTitleLink.setText(sTitle);
		newTitleLink.addParameter(prmCategoryId, iCategoryId);
		int row = 1;
		T.add(getPDFLink(iwb.getImage("print.gif"), iCategoryId), 1, row);
		T.add(getNewLink(iCategoryId), 2, row);
		row++;
		T.add(Edit.titleText(iwrb.getLocalizedString("header", "Header")), 1, row);
		row++;
		T.add(newTitleLink, 2, row);
		row++;
		T.add(Edit.titleText(iwrb.getLocalizedString("order", "Order")), 1, row);
		T.add(Edit.titleText(iwrb.getLocalizedString("title", "Title")), 2, row);
		row++;
		if (L != null) {
			int len = L.size();
			ContractText CT;
			for (int i = 0; i < len; i++) {
				CT = (ContractText) L.get(i);
				Link link = new Link(CT.getName());
				link.addParameter("text_id", CT.getID());
				link.addParameter(prmCategoryId, iCategoryId);
				T.add(String.valueOf(CT.getOrdinal()), 1, row);
				T.add(link, 2, row);
				row++;
			}
			//T.setColumnAlignment(1,"right");
			T.setHorizontalZebraColored(Edit.colorLightBlue, Edit.colorWhite);
			T.setRowColor(1, Edit.colorWhite);
			T.setRowColor(2, Edit.colorBlue);
			T.setRowColor(4, Edit.colorBlue);
			T.setRowColor(row, Edit.colorRed);
			T.setWidth(1, "30");
			T.mergeCells(1, 2, 2, 2);
			T.mergeCells(1, row, 8, row);
			T.add(Edit.formatText(" "), 1, row);
			T.setHeight(row, bottomThickness);
		}
		else {
			T.add(Edit.formatText(iwrb.getLocalizedString("no_texts", "No text in database")), 1, 2);
		}
		T.setAlignment(1, 2, "right");
		return T;
	}
	private Form getTitleForm(IWContext iwc, int iCategoryId) {
		Form F = new Form();
		Table T = new Table();
		int row = 1;
		TextInput text = null;
		String sId = iwc.getParameter("title_id");
		ContractCategory Cat = ContractFinder.getContractCategory(Integer.parseInt(sId));
		if (Cat != null) {
			text = new TextInput("tname", Cat.getDescription());
			HiddenInput HI = new HiddenInput("title_id", sId);
			T.add(HI);
		}
		else {
			text = new TextInput("tname");
		}
		SubmitButton save = new SubmitButton("savetitle", "Save");
		text.setLength(80);
		T.add(new HiddenInput(prmCategoryId, String.valueOf(iCategoryId)));
		T.add(getUpLink(iCategoryId), 1, row++);
		T.add(Edit.formatText(iwrb.getLocalizedString("text", "Text")), 1, row++);
		T.add(text, 1, row++);
		T.add(save, 1, row);
		F.add(T);
		return (F);
	}
	private Form getSetupForm(IWContext iwc, int iCategoryId) {
		//Table Frame = new Table(2,1);
		Table T = new Table();
		T.add(getUpLink(iCategoryId), 1, 1);
		T.add(getNewLink(iCategoryId), 1, 1);
		int row = 2;
		DropdownMenu intDrop = getIntegerDrop("ordinal", 1, 100);
		TextInput name = null;
		TextArea text = null;
		CheckBox CB = new CheckBox("usetags", "true");
		String sId = iwc.getParameter("text_id");
		if (sId != null) {
			ContractText CT = ContractFinder.getContractText(Integer.parseInt(sId));
			if (CT != null) {
				name = new TextInput("name", CT.getName());
				text = getTextArea("texti", CT.getText());
				CB.setChecked(CT.getUseTags());
				intDrop.setSelectedElement(String.valueOf(CT.getOrdinal()));
				HiddenInput HI = new HiddenInput("text_id", sId);
				T.add(HI);
			}
		}
		else {
			name = new TextInput("name");
			text = getTextArea("texti", "");
			int max = ContractFinder.getContractTextMaxOrdinal() + 1;
			intDrop.setSelectedElement(String.valueOf(max));
		}
		SubmitButton save = new SubmitButton("savetext", "Save");
		SubmitButton delete = new SubmitButton("delete", "Delete");
		DropdownMenu tagDrop = getTagDrop("tags", iCategoryId);
		tagDrop.setOnChange("this.form.texti.value += this.options[this.selectedIndex].value;");
		name.setLength(80);
		T.add(Edit.formatText(iwrb.getLocalizedString("title", "Title")), 1, row++);
		T.add(name, 1, row++);
		T.add(Edit.formatText(iwrb.getLocalizedString("text", "Text")), 1, row++);
		T.add(text, 1, row++);
		Table bottomTable = new Table();
		bottomTable.setWidth("100%");
		bottomTable.add(intDrop, 1, 1);
		bottomTable.add(CB, 2, 1);
		bottomTable.add(save, 3, 1);
		bottomTable.add(tagDrop, 4, 1);
		bottomTable.add(delete, 5, 1);
		T.add(bottomTable, 1, row);
		T.add(new HiddenInput(prmCategoryId, String.valueOf(iCategoryId)));

		Form myForm = new Form();
		myForm.add(T);
		return myForm;
	}

	private Form ConfirmDelete(IWContext iwc) {
		String sTextId = iwc.getParameter("text_id");
		Form F = new Form();
		Table T = new Table(3, 2);
		T.mergeCells(1, 1, 3, 1);
		if (sTextId != null)
			T.add(new HiddenInput("text_id", sTextId));
		SubmitButton del = new SubmitButton("conf_delete", iwrb.getLocalizedString("ok", "OK"));
		BackButton back = new BackButton(iwrb.getLocalizedString("cancel", "Cancel"));
		back.setHistoryMove(2);
		T.add(Edit.formatText(iwrb.getLocalizedString("sure_to_delete", "Do really want to delete")), 1, 1);
		T.add(del, 1, 2);
		T.add(back, 3, 2);
		F.add(T);
		return F;
	}
	private void deleteText(IWContext iwc) {
		String sTextId = iwc.getParameter("text_id");
		if (sTextId != null) {
			try {
				int id = Integer.parseInt(sTextId);
				ContractText CT = ((com.idega.block.contract.data.ContractTextHome) com.idega.data.IDOLookup.getHomeLegacy(ContractText.class)).findByPrimaryKeyLegacy(id);
				CT.delete();
			}
			catch (SQLException ex) {
			}
		}
	}
	private void updateForm(IWContext iwc, int iCategoryId) {
		String sTextId = iwc.getParameter("text_id");
		String sOrdinal = iwc.getParameter("ordinal");
		String sName = iwc.getParameter("name");
		String sText = iwc.getParameter("texti");
		String sUseTags = iwc.getParameter("usetags");
		int id = sTextId != null ? Integer.parseInt(sTextId) : -1;
		int iOrd = Integer.parseInt(sOrdinal);
		boolean useTags = sUseTags != null;
		sName = sName != null ? sName : "";
		sText = sText != null ? sText : "";
		ContractBusiness.saveContractText(id, iCategoryId, sName, sText, iOrd, useTags);
	}
	private void updateTitleForm(IWContext iwc, int iCategoryId) throws IDOLookupException{
		String sCatId = iwc.getParameter("title_id");
		String sText = iwc.getParameter("tname");
		if (sCatId != null && sText != null) {
			((ContractCategoryHome) IDOLookup.getHome(ContractCategory.class)).updateDescription(Integer.parseInt(sCatId),sText);
		}
	}
	private String getTitle(int iCategoryId) {
		return ContractFinder.getContractCategory(iCategoryId).getDescription();
	}
	private Link getUpLink(int iCategoryId) {
		Link L = new Link(iwb.getImage("list.gif"));
		L.addParameter(prmCategoryId, iCategoryId);
		return L;
	}
	private Link getNewLink(int iCategoryId) {
		Link newLink = new Link(iwb.getImage("new.gif"));
		newLink.addParameter(prmCategoryId, iCategoryId);
		newLink.addParameter("new_text", "new");
		return newLink;
	}
	public Link getPDFLink(PresentationObject MO, int iCategoryId) {
		Link L = new Link(MO);
		L.setWindowToOpen(ContractFilerWindow.class);
		L.addParameter(prmCategoryId, iCategoryId);
		L.addParameter("test", "test");
		return L;
	}
	private DropdownMenu getTagDrop(String name, int iCategoryId) {
		List L = ContractFinder.listOfContractTagsInUse(iCategoryId);
		DropdownMenu drp = new DropdownMenu(name);
		drp.addMenuElementFirst("tag", iwrb.getLocalizedString("tags", "Tags"));
		if (L != null) {
			java.util.Iterator I = L.iterator();
			while (I.hasNext()) {
				ContractTag tag = (ContractTag) I.next();
				drp.addMenuElement(" [" + tag.getName() + "]", tag.getName());
			}
			drp.addMenuElement(" [" + ContractWriter.contract_starts + "]", ContractWriter.contract_starts);
			drp.addMenuElement(" [" + ContractWriter.contract_ends + "]", ContractWriter.contract_ends);
			drp.addMenuElement(" [" + ContractWriter.today + "]", ContractWriter.today);
		}
		return drp;
	}
	private DropdownMenu getIntegerDrop(String name, int from, int to) {
		DropdownMenu drp = new DropdownMenu(name);
		for (int i = from; i <= to; i++) {
			drp.addMenuElement(i, String.valueOf(i));
		}
		return drp;
	}
	private TextArea getTextArea(String name, String content) {
		TextArea TA = new TextArea(name, content);
		TA.setStyleClass(Edit.styleAttribute);
		TA.setWidth(80);
		TA.setHeight(20);
		return TA;
	}
	public void main(IWContext iwc)throws Exception {
		//isStaff = com.idega.core.accesscontrol.business.AccessControl
		isAdmin = iwc.hasEditPermission(this);
		control(iwc);
	}
}