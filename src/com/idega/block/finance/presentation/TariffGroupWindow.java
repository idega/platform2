package com.idega.block.finance.presentation;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.finance.business.FinanceService;
import com.idega.block.finance.data.TariffGroup;
import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;

/**
 * Title: Description: Copyright: Copyright (c) 2000-2001 idega.is All Rights
 * Reserved Company: idega
 * 
 * @author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class TariffGroupWindow extends IWAdminWindow {

	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.finance";

	private static final String prefix = "tgrp_";

	public static final String prmCategory = prefix + "cat";

	public static final String prmGroup = prefix + "group";

	private static final String actSave = prefix + "save";

	private FinanceService finServ = null;

	private IWBundle core;

	private IWResourceBundle iwrb;

	public TariffGroupWindow() {
		setWidth(500);
		setHeight(500);
		setResizable(true);
		setUnMerged();
	}

	private void control(IWContext iwc) throws Exception {
		debugParameters(iwc);
		Integer iCategoryId = new Integer(Finance.parseCategoryId(iwc));
		finServ = getFinanceService(iwc);
		if (iCategoryId.intValue() > 0) {
			Integer groupId = null;
			if (iwc.isParameterSet(prmGroup))
				groupId = Integer.valueOf(iwc.getParameter(prmGroup));
			if (iwc.isParameterSet(actSave) || iwc.isParameterSet(actSave + ".x")) {
				groupId = processCategoryForm(iwc, iCategoryId, groupId);
			}
			if (groupId != null) {
				addCategoryFields(finServ.getTariffGroupHome().findByPrimaryKey(groupId), iCategoryId);
			}
			else {
				addCategoryFields(null, iCategoryId);
			}
		}
		else {
			add("no category ");
		}
	}

	private Integer processCategoryForm(IWContext iwc, Integer categoryID, Integer groupId) {
		String sName = iwc.getParameter("cat_name");
		String sInfo = iwc.getParameter("cat_info");
		boolean UseIndex = iwc.isParameterSet("use_index");
		Integer handlerId = Integer.valueOf(iwc.getParameter("fhandler"));
		TariffGroup group = null;
		try {
			group = finServ.createOrUpdateTariffGroup(groupId, sName, sInfo, handlerId, UseIndex, categoryID);
			return (Integer) group.getPrimaryKey();
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (CreateException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void addCategoryFields(TariffGroup group, Integer iCategoryId) {

		String sGroup = iwrb.getLocalizedString("tariffgroup", "Tariffgroup");
		String sName = iwrb.getLocalizedString("name", "Name");
		String sDesc = iwrb.getLocalizedString("description", "Description");
		String sHandlers = iwrb.getLocalizedString("handlers", "Handlers");
		String sIndex = iwrb.getLocalizedString("useindices", "Use indices");
		boolean hasCategory = group != null ? true : false;

		Link newLink = new Link(core.getImage("/shared/create.gif"));
		newLink.addParameter(prmCategory, -1);

		Collection L = null;
		Collection L2 = null;
		try {
			L = finServ.getTariffGroupHome().findByCategory(iCategoryId);
			L2 = finServ.getFinanceHandlerInfoHome().findAll();
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		// FinanceFinder.getInstance().listOfTariffGroups(iCategoryId);
		DropdownMenu groups = new DropdownMenu(L, prmGroup);
		groups.addMenuElementFirst("-1", sGroup);
		groups.setToSubmit();

		// FinanceFinder.getInstance().listOfFinanceHandlers();
		DropdownMenu handlers = new DropdownMenu(L2, "fhandler");
		handlers.addMenuElementFirst("-1", sHandlers);

		TextInput tiName = new TextInput("cat_name");
		tiName.setLength(40);
		tiName.setMaxlength(255);

		TextArea taDesc = new TextArea("cat_info", 65, 5);

		CheckBox useIndexes = new CheckBox("use_index", "true");

		Table catTable = new Table(5, 1);
		catTable.setCellpadding(0);
		catTable.setCellspacing(0);
		setStyle(groups);
		catTable.add(groups, 1, 1);
		catTable.add(newLink, 3, 1);
		catTable.setWidth(2, 1, "20");
		catTable.setWidth(4, 1, "20");

		addLeft(sGroup, catTable, true, false);
		addLeft(sName, tiName, true);
		addLeft(sDesc, taDesc, true);
		setStyle(handlers);
		addLeft(sIndex, useIndexes, true);
		addLeft(sDesc, handlers, true);
		addLeft(Finance.getCategoryParameter(iCategoryId.intValue()));
		if (hasCategory) {
			Integer id = (Integer) group.getPrimaryKey();
			if (group.getName() != null)
				tiName.setContent(group.getName());
			if (group.getInfo() != null)
				taDesc.setContent(group.getInfo());
			groups.setSelectedElement(id.toString());
			useIndexes.setChecked(group.getUseIndex());
			handlers.setSelectedElement(String.valueOf(group.getHandlerId()));
		}
		SubmitButton save = new SubmitButton(iwrb.getLocalizedImageButton("save", "Save"), actSave);
		addSubmitButton(save);

	}

	public void main(IWContext iwc) throws Exception {
		super.main(iwc);

		core = iwc.getIWMainApplication().getBundle(iwc.getIWMainApplication().CORE_BUNDLE_IDENTIFIER);
		iwrb = getResourceBundle(iwc);
		addTitle(iwrb.getLocalizedString("tariff_group_editor", "Tariffgroup Editor"));
		control(iwc);
	}

	public FinanceService getFinanceService(IWApplicationContext iwac) throws RemoteException {
		return (FinanceService) IBOLookup.getServiceInstance(iwac, FinanceService.class);
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
}
