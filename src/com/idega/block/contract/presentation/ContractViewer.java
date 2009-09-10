package com.idega.block.contract.presentation;

import com.idega.idegaweb.block.presentation.Builderaware;
import com.idega.block.contract.business.ContractBusiness;
import com.idega.block.contract.business.ContractFinder;
import com.idega.block.contract.data.Contract;
import com.idega.block.contract.data.ContractTag;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.util.Edit;

import java.text.DateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */
public class ContractViewer extends Block implements Builderaware {
	protected final int ACT1 = 1, ACT2 = 2, ACT3 = 3, ACT4 = 4, ACT5 = 5;
	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.contract";
	protected boolean isAdmin = false;
	private int iCategoryId = -1;
	protected IWResourceBundle iwrb;
	protected IWBundle iwb;
	private IWBundle core;
	private String sGlobalStatus = "C";
	boolean newobjinst = false;
	private static final String prmCategoryId = "conv_cat";
	private String conPrm = "contract_status";
	private String sessConPrm = "sess_con_status";

	public ContractViewer() {
		super();
	}

	protected void control(IWContext iwc) {
		this.iwrb = getResourceBundle(iwc);
		this.iwb = getBundle(iwc);
		boolean info = false;
		Table T = new Table();
		T.setCellpadding(0);
		T.setCellspacing(0);
		if (this.iCategoryId <= 0) {
			String sCategoryId = iwc.getParameter(prmCategoryId);
			if (sCategoryId != null) {
				this.iCategoryId = Integer.parseInt(sCategoryId);
			}
			else if (getICObjectInstanceID() > 0) {
				this.iCategoryId = ContractFinder.getObjectInstanceCategoryId(getICObjectInstanceID(), true);
				if (this.iCategoryId <= 0) {
					this.newobjinst = true;
				}
			}
		}
		if (this.isAdmin) {
			T.add(getAdminPart(this.iCategoryId, false, this.newobjinst, info, iwc), 1, 1);
		}
		if (iwc.getParameter(this.conPrm) != null) {
			this.sGlobalStatus = (iwc.getParameter(this.conPrm));
			iwc.setSessionAttribute(this.sessConPrm, this.sGlobalStatus);
		}
		else if (iwc.getSessionAttribute(this.sessConPrm) != null) {
			this.sGlobalStatus = ((String) iwc.getSessionAttribute(this.sessConPrm));
		}
		T.add(statusForm(), 1, 2);
		T.add(getContractTable(iwc, this.iCategoryId), 1, 3);
		Form F = new Form();
		F.add(T);
		add(F);
		//  add(Edit.formatText(iwrb.getLocalizedString("access_denied","Access denied")));
		//add(String.valueOf(iSubjectId));
	}
	
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	
	private PresentationObject getAdminPart(int iCategoryId, boolean enableDelete, boolean newObjInst, boolean info, IWContext iwc) {
		Table T = new Table(3, 1);
		T.setCellpadding(2);
		T.setCellspacing(2);
		IWBundle core = iwc.getIWMainApplication().getBundle(IW_CORE_BUNDLE_IDENTIFIER);
		if (iCategoryId > 0) {
			Link ne = new Link(core.getImage("/shared/create.gif", "create"));
			ne.setWindowToOpen(ContractEditorWindow.class);
			ne.addParameter(ContractEditorWindow.prmCategory, iCategoryId);
			T.add(ne, 1, 1);
			T.add(Table.getTransparentCell(iwc), 1, 1);
			Link text = new Link(core.getImage("/shared/text.gif", "text"));
			text.setWindowToOpen(ContractTextWindow.class);
			text.addParameter(ContractTextSetter.prmCategoryId, iCategoryId);
			T.add(text, 1, 1);
			T.add(Table.getTransparentCell(iwc), 1, 1);
			Link change = new Link(core.getImage("/shared/edit.gif", "edit"));
			change.setWindowToOpen(ContractEditorWindow.class);
			change.addParameter(ContractEditorWindow.prmCategory, iCategoryId);
			change.addParameter(ContractEditorWindow.prmObjInstId, getICObjectInstanceID());
			T.add(change, 1, 1);
			if (enableDelete) {
				T.add(Table.getTransparentCell(iwc), 1, 1);
				Link delete = new Link(core.getImage("/shared/delete.gif"));
				delete.setWindowToOpen(ContractEditorWindow.class);
				delete.addParameter(ContractEditorWindow.prmDelete, iCategoryId);
				T.add(delete, 3, 1);
			}
		}
		if (newObjInst) {
			Link newLink = new Link(core.getImage("/shared/create.gif"));
			newLink.setWindowToOpen(ContractEditorWindow.class);
			if (newObjInst) {
				newLink.addParameter(ContractEditorWindow.prmObjInstId, getICObjectInstanceID());
			}
			T.add(newLink, 2, 1);
		}
		T.setWidth("100%");

		return T;
	}
	
	public PresentationObject makeLinkTable(int menuNr) {
		Table LinkTable = new Table(6, 1);

		return LinkTable;
	}
	
	private Form subjectForm() {
		Form myForm = new Form();
		DropdownMenu status = statusDrop(this.conPrm, this.sGlobalStatus);
		status.setToSubmit();
		Edit.setStyle(status);
		myForm.add(status);

		return myForm;
	}
	
	private PresentationObject statusForm() {
		DropdownMenu status = statusDrop(this.conPrm, this.sGlobalStatus);
		status.setToSubmit();
		Edit.setStyle(status);
		Table T = new Table(3, 1);
		T.add(Edit.formatText(this.iwrb.getLocalizedString("status", "Status")), 1, 1);
		T.add(status, 2, 1);
		T.setCellpadding(1);
		T.setCellspacing(0);

		return T;
	}
	
	private PresentationObject getContractTable(IWContext iwc, int iCategoryId) {
		Collection L = ContractFinder.listOfStatusContracts(this.sGlobalStatus, iCategoryId);
		List tags = ContractFinder.listOfContractTagsInList(iCategoryId);
		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, iwc.getCurrentLocale());
		Contract C = null;
		ContractTag tag;
		boolean hasTags = tags != null;
		int tagCount = hasTags ? tags.size() : 0;
		Table T = new Table();
		T.setCellspacing(0);
		T.setCellpadding(2);
		String field;
		if (L != null) {
			int len = L.size();
			Iterator iter = L.iterator();
			T = new Table(5 + tagCount, len + 2);
			T.setCellspacing(1);
			T.setCellpadding(0);
			int row = 1;
			int col = 1;
			T.add(Edit.titleText(this.iwrb.getLocalizedString("nr", "Nr")), col++, row);
			col++;
			T.add(Edit.titleText(this.iwrb.getLocalizedString("validfrom", "Valid from")), col++, row);
			T.add(Edit.titleText(this.iwrb.getLocalizedString("validfrom", "Valid to")), col++, row);
			T.add(Edit.titleText(this.iwrb.getLocalizedString("changed", "Changed")), col++, row);
			String[] tagKeys = new String[tagCount];
			for (int k = 0; k < tagCount; k++) {
				tag = (ContractTag) tags.get(k);
				T.add(Edit.titleText(tag.getName()), col++, row);
				tagKeys[k] = String.valueOf(tag.getID());
			}
			row++;
			col = 1;
			Image propImage = this.core.getImage("/shared/edit.gif");
			int i = 1;
			while(iter.hasNext()){
				C = (Contract) iter.next();
				T.add(Edit.formatText(i ++), col++, row);
				if (this.isAdmin) {
					T.add(getPropertyLink(propImage, C), col, row);
				}
				col++;
				T.add(Edit.formatText(df.format(C.getValidFrom())), col++, row);
				T.add(Edit.formatText(df.format(C.getValidTo())), col++, row);
				T.add(Edit.formatText(df.format(C.getStatusDate())), col++, row);
				for (int k = 0; k < tagCount; k++) {
					field = C.getMetaData(tagKeys[k]);
					T.add(Edit.formatText(field), col++, row);
				}
				row++;
				col = 1;
			}
			col = 4;
			T.setHorizontalZebraColored(Edit.colorLightBlue, Edit.colorWhite);
			T.setRowColor(1, Edit.colorBlue);
			T.setRowColor(row, Edit.colorRed);
			T.mergeCells(1, row, 8, row);
			T.setWidth(1, "15");
			T.add(Edit.formatText(" "), 1, row);
			T.setColumnAlignment(1, "left");
			T.setHeight(row, Edit.bottomBarThickness);
			T.setWidth("100%");
		}
		else {
			T.add(Edit.formatText(this.iwrb.getLocalizedString("no_contracts", "No contracts")));
		}
		return T;
	}
	private Link getPropertyLink(PresentationObject obj, Contract C) {
		Link L = new Link(obj);
		L.setWindowToOpen(ContractEditorWindow.class);
		L.addParameter(ContractEditorWindow.prmContractId, C.getPrimaryKey().toString());
		return L;
	}
	private String getStatus(String status) {
		String r = "";
		char c = status.charAt(0);
		switch (c) {
			case 'C' :
				r = this.iwrb.getLocalizedString("created", "Created");
				break;
			case 'P' :
				r = this.iwrb.getLocalizedString("printed", "Printed");
				break;
			case 'S' :
				r = this.iwrb.getLocalizedString("signed", "Signed");
				break;
			case 'R' :
				r = this.iwrb.getLocalizedString("rejected", "Rejected");
				break;
			case 'T' :
				r = this.iwrb.getLocalizedString("terminated", "Terminated");
				break;
			case 'E' :
				r = this.iwrb.getLocalizedString("ended", "Ended");
				break;
		}
		return r;
	}
	private DropdownMenu statusDrop(String name, String selected) {
		DropdownMenu drp = new DropdownMenu(name);
		drp.addMenuElement("C", getStatus("C"));
		drp.addMenuElement("P", getStatus("P"));
		drp.addMenuElement("S", getStatus("S"));
		drp.addMenuElement("R", getStatus("R"));
		drp.addMenuElement("T", getStatus("T"));
		drp.addMenuElement("E", getStatus("E"));
		drp.setSelectedElement(selected);
		return drp;
	}
	public boolean deleteBlock(int iObjectInstanceId) {
		return ContractBusiness.deleteBlock(iObjectInstanceId);
	}
	public void main(IWContext iwc) {
		this.isAdmin = iwc.hasEditPermission(this);
		this.core = iwc.getIWMainApplication().getBundle(IW_CORE_BUNDLE_IDENTIFIER);
		control(iwc);
	}
}
