/*
 * $Id$
 *
 * Copyright (C) 2000-2003 Idega Software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega Software.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.block.forum.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.block.forum.business.CommuneForumBusiness;
import com.idega.block.category.business.CategoryFinder;
import com.idega.block.category.data.ICCategory;
import com.idega.block.category.data.ICCategoryHome;
import com.idega.block.category.data.ICCategoryTranslation;
import com.idega.block.category.presentation.CategoryWindow;
import com.idega.business.IBOLookup;
import com.idega.core.localisation.presentation.ICLocalePresentation;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;
import com.idega.user.presentation.UserChooserBrowser;

/**
 * @author palli
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class CommuneCategoryWindow extends CategoryWindow {
	protected String prmUserId = "usrBrwsrId";
	protected CommuneForumBusiness cfb;
	
	public CommuneCategoryWindow() {
		super();
		setName("CommuneCategoryWindow");
	}
	
	public void main(IWContext iwc) throws Exception{
			cfb = getCommuneForumBusiness(iwc);
			super.main(iwc);
	}

	protected void getCategoryFields(IWContext iwc, int iCategoryId) throws RemoteException {
		//System.out.println("Commune getCategoryFields()");
		int parent = iwc.isParameterSet(prmParentID) ? Integer.parseInt(iwc.getParameter(prmParentID)) : -1;

		Link newLink = new Link(core.getImage("/shared/create.gif"));
		newLink.addParameter(prmCategoryId, -1);
		newLink.addParameter(prmObjInstId, iObjectInstanceId);
		newLink.addParameter(actForm, "true");
		/** @todo  permission handling */
		//List L = CategoryFinder.getInstance().listOfCategories(sType);
		Collection L = null;
		try {
			L = catServ.getCategoryHome().findRootsByType(sType);
		}
		catch (Exception ex) {
		}
		if (L != null) { // Gimmi 17.08.2002
			/** @todo laga comparatorinn */
			//Collections.sort(L, new CategoryComparator());
		}
		Collection coll = CategoryFinder.getInstance().collectCategoryIntegerIds(iObjectInstanceId);
		int chosenId = iCategoryId;

		Table T = new Table();
		T.setCellpadding(0);
		T.setCellspacing(0);
		row = 1;
		DropdownMenu LocaleDrop = ICLocalePresentation.getLocaleDropdownIdKeyed(prmLocale);
		LocaleDrop.setToSubmit();
		LocaleDrop.setSelectedElement(Integer.toString(iLocaleId));
		T.add(LocaleDrop, 1, row);
		T.mergeCells(1, row, 3, row);
		row++;
		T.add(Text.getBreak(), 1, row);
		row++;
		T.add(formatText(iwrb.getLocalizedString("use", "Use")), 1, row);
		T.add(formatText(iwrb.getLocalizedString("name", "Name")), 2, row);
		T.add(formatText(iwrb.getLocalizedString("info", "Info")), 3, row);
		if (allowOrdering) {
			T.add(formatText("  " + iwrb.getLocalizedString("order", "Order")), 4, row);
		}
		//T.add(formatText("  " + iwrb.getLocalizedString("add_child", "Add child") + "  "), 5, row);
		T.add(formatText("  " + iwrb.getLocalizedString("delete", "Delete") + "  "), 6, row);
		T.add(formatText("  " + iwrb.getLocalizedString("moderator", "Moderator") + "  "), 7, row);
		row++;
		TextInput name = new TextInput("name");
		TextInput info = new TextInput("info");
		TextInput order = new TextInput("order");
		order.setSize(3);
		setStyle(name);
		setStyle(info);
		setStyle(order);
		formAdded = false;
		if (L != null)
			fillTable(L.iterator(), T, chosenId, coll, name, info, order, 0);
		if (!formAdded) {
			T.add(Text.getBreak(), 1, row++);
			T.mergeCells(2, row, 6, row);
			if (parent > 0) {
				ICCategory cat = CategoryFinder.getInstance().getCategory(parent);
				T.add(formatText(iwrb.getLocalizedString("create_child_category_under", "Create child under") + " " + cat.getName()), 2, row);
			}
			else {
				T.add(formatText(iwrb.getLocalizedString("create_root_category", "Create new root category")), 2, row);
			}
			row++;
			T.add(name, 2, row);
			T.add(info, 3, row);
		}
		else {
			Link li = new Link(iwrb.getLocalizedImageButton("new", "New"));
			addParametersToLink(li);
			T.add(Text.getBreak(), 2, row);
			T.add(li, 2, row);
		}
		addLeft(iwrb.getLocalizedString("categories", "Categories"), T, true, false);
		addBreak();

		SubmitButton save = new SubmitButton(iwrb.getLocalizedImageButton("save", "Save"), actSave);
		SubmitButton close = new SubmitButton(iwrb.getLocalizedImageButton("close", "Close"), actClose);
		addSubmitButton(save);
		addSubmitButton(close);
		addHiddenInput(new HiddenInput(prmCategoryType, sType));
		addHiddenInput(new HiddenInput(prmObjInstId, String.valueOf(iObjectInstanceId)));
		addHiddenInput(new HiddenInput(prmParentID, String.valueOf(parent)));
		addHiddenInput(new HiddenInput(actForm, "true"));
		if (allowOrdering) {
			addHiddenInput(new HiddenInput(prmOrder, "true"));
		}
		if (multi) {
			addHiddenInput(new HiddenInput(prmMulti, "true"));
		}
		this.maintainClearCacheKeyInForm(iwc);

		T.setColumnAlignment(4, Table.HORIZONTAL_ALIGN_CENTER);
		T.setAlignment(4, 1, Table.HORIZONTAL_ALIGN_LEFT);
		//T.setColumnAlignment(5, T.HORIZONTAL_ALIGN_CENTER);
		//T.setAlignment(5, 1, T.HORIZONTAL_ALIGN_LEFT);
		T.setColumnAlignment(6, Table.HORIZONTAL_ALIGN_CENTER);
		T.setAlignment(6, 1, Table.HORIZONTAL_ALIGN_LEFT);
		T.setColumnAlignment(7, Table.HORIZONTAL_ALIGN_CENTER);
		T.setAlignment(7, 1, Table.HORIZONTAL_ALIGN_LEFT);

	}

	protected void fillTable(Iterator iter, Table T, int chosenId, Collection coll, TextInput name, TextInput info, TextInput order, int level) throws RemoteException {
		//System.out.println("Commune fillTable()");
		if (iter != null) {

			ICCategory cat;
			ICCategoryTranslation trans = null;
			String catName, catInfo;
			CheckBox box;
			RadioButton rad;
			Link deleteLink;
			int id;
			int ownerGroupId = -1;
			int iOrder = 0;
			while (iter.hasNext()) {
				cat = (ICCategory) iter.next();
				id = ((Integer) cat.getPrimaryKey()).intValue();
				ownerGroupId = cat.getOwnerGroupId();
				try {
					trans = catServ.getCategoryTranslationHome().findByCategoryAndLocale(id, iLocaleId);
				}
				catch (FinderException ex) {
				}
				if (trans != null) {
					catName = trans.getName();
					catInfo = trans.getDescription();
				}
				else {
					catName = cat.getName();
					catInfo = cat.getDescription();
				}
				if (allowOrdering) {
					try {
						iOrder = CategoryFinder.getInstance().getCategoryOrderNumber(cat, this.objectInstance);
					}
					catch (Exception e) {
						e.printStackTrace(System.err);
					}
				}
				if (level > 0) {
					for (int i = 0; i < level; i++) {
						T.add(tree_image_T, 2, row);
					}
					if (iter.hasNext())
						T.add(tree_image_M, 2, row);
					else
						T.add(tree_image_L, 2, row);
				}
				if (id == chosenId) {

					name.setContent(catName);
					if (catInfo != null)
						info.setContent(catInfo);
					T.add(name, 2, row);
					T.add(info, 3, row);
					if (allowOrdering) {
						T.add(order, 4, row);
						order.setContent(Integer.toString(iOrder));
					}
					T.add(new HiddenInput(prmCategoryId, String.valueOf(id)));
					formAdded = true;
					UserChooserBrowser ucb = new UserChooserBrowser(prmUserId);
					if (ownerGroupId > 0) {
						try {
							UserHome uHome = (UserHome) IDOLookup.getHome(User.class);
							User user;
							user = uHome.findByPrimaryKey(new Integer(ownerGroupId));
							ucb.setSelectedUser(Integer.toString(ownerGroupId), user.getName());
						} catch (FinderException e) {
							ucb.setSelectedUser("-1", "User not found");
							e.printStackTrace(System.err);
						}
					}
					T.add(ucb,7,row);
				}
				else {
					Link Li = new Link(formatText(catName));
					Li.addParameter(prmCategoryId, id);
					Li.addParameter("edit", "true");
					T.add(Li, 2, row);
					T.add(formatText(catInfo), 3, row);
					Link childLink = new Link(core.getImage("/shared/create.gif"));
					childLink.addParameter(prmParentID, id);
					deleteLink = new Link(core.getImage("/shared/delete.gif"));
					deleteLink.addParameter(actDelete, "true");
					deleteLink.addParameter(prmCategoryId, id);
					deleteLink.addParameter(actForm, "true");
					addParametersToLink(childLink);
					addParametersToLink(deleteLink);
					addParametersToLink(Li);
					if (allowOrdering) {
						T.add(formatText(Integer.toString(iOrder)), 4, row);
					}
					//T.add(childLink, 5, row);
					T.add(deleteLink, 6, row);
					User user = cfb.getModerator(cat);
					if (user != null) {
						T.add(formatText(user.getName()), 7, row);
					}
					//T.add(new UserChooserBrowser("browser"),7,row);

				}
				if (multi) {
					box = new CheckBox("id_box", String.valueOf(((Integer)cat.getPrimaryKey()).intValue()));
					box.setChecked(coll != null && coll.contains(new Integer(((Integer)cat.getPrimaryKey()).intValue())));
					//setStyle(box);
					T.add(box, 1, row);
				}
				else {
					rad = new RadioButton("id_box", String.valueOf(((Integer)cat.getPrimaryKey()).intValue()));
					if (coll != null && coll.contains(new Integer(((Integer)cat.getPrimaryKey()).intValue())))
						rad.setSelected();
					//setStyle(rad);
					T.add(rad, 1, row);
				}
				row++;
				if (cat.getChildCount() > 0)
					fillTable(cat.getChildrenIterator(), T, chosenId, coll, name, info, order, level + 1);
			}
			trans = null;
		}
	}
	
	protected void postSave(IWContext iwc, int iCategoryId) throws RemoteException {
		//System.out.println("Commune postSave()");
		String sUsrId = iwc.getParameter(prmUserId);
		try {
			int userId = Integer.parseInt(sUsrId);
			try {
				ICCategoryHome cHome = (ICCategoryHome) IDOLookup.getHome(ICCategory.class);
				ICCategory cat = cHome.findByPrimaryKey(new Integer(iCategoryId));
				if (userId > 0) {
					cat.setOwnerGroupId(userId);
					cat.store();	
				}

			} catch (FinderException e) {
				e.printStackTrace(System.err);
			}
		}catch (NumberFormatException n) {}

	}
	
	protected CommuneForumBusiness getCommuneForumBusiness(IWContext iwc) throws RemoteException {
		return (CommuneForumBusiness) IBOLookup.getServiceInstance(iwc, CommuneForumBusiness.class);
	}

}