/*
 * $Id: ChildCareChildrenInfo.java,v 1.2 2005/03/30 07:57:26 laddi Exp $
 * Created on 31.1.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import se.idega.idegaweb.commune.care.data.ChildCareContract;
import se.idega.idegaweb.commune.childcare.event.ChildCareEventListener;
import se.idega.idegaweb.commune.childcare.presentation.admin.ContractEditor;
import se.idega.idegaweb.commune.childcare.presentation.admin.ContractEditorWindow;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.builder.data.ICPage;
import com.idega.idegaweb.IWUserContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.business.UserSession;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.text.Name;


/**
 * Last modified: $Date: 2005/03/30 07:57:26 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
 */
public class ChildCareChildrenInfo extends ChildCareBlock {
	
	private static final String PARAMETER_CHILD = "child_id";
	private static final String PARAMETER_CONTRACT_ID = "contract_id";
	
	private ICPage iPage;
	private Image iCheckImage;
	private Image iNoCheckImage;
	
	public void init(IWContext iwc) throws Exception {
		parse(iwc);
		
		User user = getUserSession(iwc).getUser();
		if (user != null) {
			Collection children = getUserBusiness(iwc).getChildrenForUser(user);
			if (children != null) {
				Iterator iter = children.iterator();
				while (iter.hasNext()) {
					User child = (User) iter.next();
					presentChildInfo(iwc, child);
					
					if (iter.hasNext()) {
						add(new Break());
					}
				}
			}
		}
	}
	
	private void parse(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_CONTRACT_ID)) {
			try {
				getBusiness().removeContract(Integer.parseInt(iwc.getParameter(PARAMETER_CONTRACT_ID)), iwc.getCurrentUser());
			}
			catch (RemoteException re) {
				log(re);
			}
		}
	}
	
	private boolean isSelectedChild(IWContext iwc, User child) {
		if (iwc.isParameterSet(PARAMETER_CHILD)) {
			String childPK = iwc.getParameter(PARAMETER_CHILD);
			try {
				getSession().setChildID(Integer.parseInt(childPK));
			}
			catch (RemoteException re) {
				log(re);
			}
			
			return childPK.equals(child.getPrimaryKey().toString());
		}
		return false;
	}
	
	public void presentChildInfo(IWContext iwc, User child) {
		Form form = new Form();
		
		Table outerTable = new Table(1, 2);
		outerTable.setWidth(getWidth());
		outerTable.setCellpadding(5);
		outerTable.setCellspacing(0);
		outerTable.setCellBorder(1, 1, 1, "#cccccc", "solid");
		outerTable.setAlignment(1, 2, Table.HORIZONTAL_ALIGN_RIGHT);
		
		Table headerTable = new Table(2, 1);
		headerTable.setCellpadding(0);
		headerTable.setCellspacing(0);
		headerTable.setAlignment(2, 1, Table.HORIZONTAL_ALIGN_RIGHT);
		
		Name name = new Name();
		name.setFirstName(child.getFirstName());
		name.setLastName(child.getLastName());
		headerTable.add(getText(PersonalIDFormatter.format(child.getPersonalID(), iwc.getCurrentLocale()) + " - " + name.getName(iwc.getCurrentLocale(), true)), 1, 1);
		
		try {
			if (getBusiness().getCheckBusiness().hasChildApprovedCheck(((Integer) child.getPrimaryKey()).intValue()) != -1 && iCheckImage != null) {
				headerTable.add(iCheckImage, 2, 1);
			}
			else if (iNoCheckImage != null) {
				headerTable.add(iNoCheckImage, 2, 1);
			}
		}
		catch (RemoteException re) {
			log(re);
		}
		
		outerTable.add(headerTable, 1, 1);
		outerTable.add(new Break(), 1, 1);
		
		Table table = new Table();
		table.setWidth(getWidth());
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.setColumns(9);
		table.setRowColor(1, getHeaderColor());
		int column = 1;
		int row = 1;
			
		table.add(getLocalizedSmallHeader("child_care.provider","Provider"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.created","Created"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.valid_from","Valid from"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.removed","Removed"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.care_time","Care time"), column++, row++);
		
		try {
			if (isSelectedChild(iwc, child)) {
				Collection contracts = getBusiness().getContractsByChild(((Integer) child.getPrimaryKey()).intValue());
				Iterator iter = contracts.iterator();
				while (iter.hasNext()) {
					ChildCareContract contract = (ChildCareContract) iter.next();
					row = addContractToTable(iwc, table, contract, row);
				}
				
				if (iPage != null) {
					GenericButton button = getButton(new GenericButton("to_central_contract_creation", localize("child_care.to_central_contract_creation", "To central contract creation")));
					button.setPageToOpen(iPage);
					outerTable.add(button, 1, 2);
				}
			}
			else {
				outerTable.add(getSmallHeader(localize("child_care.latest_placement", "Latest placement")), 1, 1);
				outerTable.add(new Break());

				ChildCareContract contract = getBusiness().getLatestContract(((Integer) child.getPrimaryKey()).intValue());
				if (contract != null) {
					row = addContractToTable(iwc, table, contract, row);
				}
				
				GenericButton button = getButton(new GenericButton("view_all_placements", localize("child_care.view_all_placements", "View all placements")));
				button.setPageToOpen(getParentPageID());
				button.addParameterToPage(PARAMETER_CHILD, child.getPrimaryKey().toString());
				outerTable.add(button, 1, 2);
			}
		}
		catch (RemoteException re) {
			log(re);
		}
		
		outerTable.add(table, 1, 1);
		form.add(outerTable);
		add(form);
	}
	
	private int addContractToTable(IWContext iwc, Table table, ChildCareContract contract, int row) {
		int column = 1;
		
		SchoolClassMember member = contract.getSchoolClassMember();
		if (member == null) {
			return row;
		}
		School school = member.getSchoolClass().getSchool();
		IWTimestamp created = new IWTimestamp(contract.getCreatedDate());
		IWTimestamp validFrom = new IWTimestamp(contract.getValidFromDate());
		IWTimestamp validTo = contract.getTerminatedDate() != null ? new IWTimestamp(contract.getTerminatedDate()) : null;
		String careTime = getCareTime(contract.getCareTime());
		
		IWTimestamp dateNow = new IWTimestamp();
		boolean isNotYetActive = false;
		if (dateNow.isEarlierThan(validFrom))
			isNotYetActive = true;
		else
			isNotYetActive = false;
		
		table.add(getSmallText(school.getName()), column++, row);
		table.add(getSmallText(created.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), column++, row);
		table.add(getSmallText(validFrom.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), column++, row);
		if (validTo != null) {
			table.add(getSmallText(validTo.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), column++, row);
		}
		else {
			table.add(getSmallText("-"), column++, row);
		}
		table.add(getSmallText(careTime), column++, row);
		table.add(getPDFLink(contract.getContractFileID(),localize("child_care.view_contract","View contract")), column++, row);
		
		Image image = getBundle().getImage("shared/change.gif");
		image.setToolTip(localize("child_care.edit_contract", "Edit contract"));
		
		Link editLink = new Link(image);
		editLink.setWindowToOpen(ContractEditorWindow.class);
		editLink.addParameter(ContractEditor.PARAMETER_APPLICATION_ID, contract.getApplicationID());
		editLink.addParameter(ContractEditor.PARAMETER_CONTRACT_ID, contract.getPrimaryKey().toString());
		table.add(editLink, column++, row);

		Link alterCareTime;
		if (isNotYetActive) {
			alterCareTime = new Link(this.getEditIcon(localize("child_care.alter_placement_date_for_child","Alter the placement date for this child.")));
			alterCareTime.addParameter(ChildCareAdminWindow.PARAMETER_METHOD, ChildCareAdminWindow.METHOD_ALTER_VALID_FROM_DATE);
		}
		else {
			alterCareTime = new Link(this.getEditIcon(localize("child_care.alter_contract_or_schooltype_for_child","Alter the contract/schooltype for this child.")));
			alterCareTime.addParameter(ChildCareAdminWindow.PARAMETER_METHOD, ChildCareAdminWindow.METHOD_ALTER_CARE_TIME);
		}
		alterCareTime.setWindowToOpen(ChildCareWindow.class);
		alterCareTime.addParameter(ChildCareAdminWindow.PARAMETER_APPLICATION_ID, contract.getApplicationID());
		alterCareTime.addParameter(ChildCareAdminWindow.PARAMETER_PAGE_ID, getParentPageID());
		table.add(alterCareTime, column++, row);

		if (!isNotYetActive) {
			Link delete = new Link(getDeleteIcon(localize("child_care.delete_from_childcare", "Remove child from child care and cancel contract.")));
			try {
				delete.setEventListener(ChildCareEventListener.class);
				delete.addParameter(getSession().getParameterChildCareID(), contract.getApplication().getProviderId());
			}
			catch (RemoteException re) {
				log(re);
			}
			delete.setWindowToOpen(ChildCareWindow.class);
			delete.addParameter(ChildCareAdminWindow.PARAMETER_METHOD, String.valueOf(ChildCareAdminWindow.METHOD_CANCEL_CONTRACT));
			delete.addParameter(ChildCareAdminWindow.PARAMETER_PAGE_ID, getParentPageID());
			delete.addParameter(ChildCareAdminWindow.PARAMETER_USER_ID, member.getClassMemberId());
			table.add(delete, column++, row++);
		}
		else {
			SubmitButton delete = new SubmitButton(getDeleteIcon(localize("child_care.delete_from_childcare", "Remove child from child care and cancel contract.")), PARAMETER_CONTRACT_ID, contract.getPrimaryKey().toString());
			delete.setSubmitConfirm(localize("child_care.remove_contract_request", "Do you really want to remove the contract?"));
			table.add(delete, column++, row++);
		}

		return row;
	}
	
	protected UserSession getUserSession(IWUserContext iwuc) {
		try {
			return (UserSession) IBOLookup.getSessionInstance(iwuc, UserSession.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}
	
	public void setPage(ICPage page) {
		iPage = page;
	}
	
	public void setCheckImage(Image checkImage) {
		iCheckImage = checkImage;
	}
	
	public void setNoCheckImage(Image noCheckImage) {
		iNoCheckImage = noCheckImage;
	}
}