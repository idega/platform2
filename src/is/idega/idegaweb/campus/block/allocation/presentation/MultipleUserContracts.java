/*
 * Created on 16.6.2004
 *
 * Copyright (C) 2004 Idega hf. All Rights Reserved.
 *
 *  This software is the proprietary information of Idega hf.
 *  Use is subject to license terms.
 */
package is.idega.idegaweb.campus.block.allocation.presentation;

import is.idega.idegaweb.campus.block.allocation.business.ContractService;
import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.presentation.CampusBlock;

import java.text.DateFormat;
import java.util.Collection;
import java.util.Vector;

import com.idega.block.building.data.Apartment;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DatePicker;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.data.User;
import com.idega.util.IWCalendar;
import com.idega.util.IWTimestamp;

/**
 * @author aron
 *
 * MultipleAssessmentAccounts TODO Describe this type
 */
public class MultipleUserContracts extends CampusBlock {
	
	private final static String PRM_PERIOD_FROM = "PRD_FM",PRM_PERIOD_TO="PRD_TO";
	private IWTimestamp periodFrom, periodTo;
	
	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#main(com.idega.presentation.IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
		
		init(iwc);
		DatePicker dpFrom = new DatePicker(PRM_PERIOD_FROM,getInterfaceStyle(),iwc.getCurrentLocale());
		dpFrom.setDate(periodFrom.getDate());
		dpFrom.setInputStyle(getInterfaceStyle());
		DatePicker dpTo = new DatePicker(PRM_PERIOD_TO,getInterfaceStyle(),iwc.getCurrentLocale());
		dpTo.setDate(periodTo.getDate());
		dpTo.setInputStyle(getInterfaceStyle());
		SubmitButton btnSearch = (SubmitButton) getStyledInterface(new SubmitButton(localize("contracts.search","Search")));
		Form searchForm = new Form();
		Table searchTable = new Table();
		searchTable.setWidth(Table.HUNDRED_PERCENT);
		searchTable.add(getHeader(localize("search_period","Search period")),1,1);
		searchTable.add(dpFrom,2,1);
		searchTable.add(dpTo,4,1);
		searchTable.add(btnSearch,6,1);
		
		Table outerTable = new Table();
		outerTable.add(searchTable,1,1);
		ContractService conService = getCampusService(iwc).getContractService();
		Collection allcontracts = conService.getContractHome().findByStatusAndOverLapPeriodMultiples(conService.getRentableStatuses(),periodFrom.getDate(),periodTo.getDate());
		Table userTable = new Table();
		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT,iwc.getCurrentLocale());
		//DateFormat tf = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT,iwc.getCurrentLocale());
		Image editIcon = getBundle().getImage("list.gif");
		int row = 1;
		userTable.add(getHeader(localize("contract","Contract")),1,row);
		userTable.add(getHeader(localize("contract_period","Period")),5,row);
		userTable.add(getHeader(localize("status","status")),6,row);
		userTable.add(getHeader(localize("status_changed","Status changed")),7,row);
		userTable.add(getHeader(localize("change_contract","Change contract")),8,row);
		row++;
		int lastUserID = -1,nextUserID = -1,userID = -1;
		IWTimestamp lastToDate = IWTimestamp.RightNow();
		Vector userContracts = new Vector();

		Vector contracts = new Vector(allcontracts);
		int size = contracts.size();
		for (int i = 0; i < size;i++) {
			Contract contract = (Contract) contracts.get(i);
			userID = contract.getUserId().intValue();
			lastUserID = -1;
			nextUserID = -1;
			Contract lastContract = null;
			Contract nextContract = null;
			if(i>0){
				lastContract = (Contract)contracts.get(i-1);
				lastUserID = lastContract.getUserId().intValue();
			}
			if((i+1)<size){
				nextContract = (Contract)contracts.get(i+1);
				nextUserID = nextContract.getUserId().intValue();
			}
			if(lastUserID!=userID){
				row++;
				
				User user = contract.getUser();
				userTable.add(getHeader(user.getPersonalID()),1,row);
				userTable.add(getHeader(user.getName()),4,row);
				userTable.mergeCells(1,row,3,row);
				userTable.mergeCells(4,row,5,row);
				userTable.setRowColor(row,this.getHeaderColor());
				
				row++;
			}
			
			Apartment apartment = contract.getApartment();
			userTable.add(getText(apartment.getName()+" "+apartment.getFloor().getBuilding().getName()),2,row);
			Text periodFromText = getText(df.format(contract.getValidFrom()));
			Text periodToText = getText(df.format(contract.getValidTo()));
			if(lastUserID==userID &&  lastContract.getValidTo().getTime()>contract.getValidFrom().getTime())
				periodFromText.setFontColor("red");
			if(nextUserID==userID && contract.getValidTo().getTime()> nextContract.getValidFrom().getTime())
				periodToText.setFontColor("red");
			userTable.add(periodFromText,5,row);
			userTable.add(getText(" - "),5,row);
			userTable.add(periodToText,5,row);
			userTable.add(getText(conService.getLocalizedStatus(getResourceBundle(),contract.getStatus())),6,row);
			userTable.add(getText(df.format(contract.getStatusDate())),7,row);
			userTable.add(CampusContracts.getEditLink(editIcon,((Integer)contract.getPrimaryKey()).intValue()),8,row);
			//userTable.add(getText(String.valueOf(userContractCount)),8,row);
			userTable.mergeCells(2,row,4,row);
			row++;
			
		}
		userTable.setColumnAlignment(7,Table.HORIZONTAL_ALIGN_RIGHT);
		userTable.setColumnAlignment(8,Table.HORIZONTAL_ALIGN_CENTER);
		outerTable.add(userTable);
		searchForm.add(outerTable);
		add(searchForm);
		
	}

	/**
	 * @param iwc
	 */
	private void init(IWContext iwc) {
		if(iwc.isParameterSet(PRM_PERIOD_FROM)){
			periodFrom = new IWTimestamp(iwc.getParameter(PRM_PERIOD_FROM));
		}
		else{
			periodFrom = IWTimestamp.RightNow();
			periodFrom.setDay(1);
		}
		if(iwc.isParameterSet(PRM_PERIOD_TO)){
			periodTo = new IWTimestamp(iwc.getParameter(PRM_PERIOD_TO));
		}
		else{
			IWCalendar cal = new IWCalendar();
			periodTo = IWTimestamp.RightNow();
			periodTo.setDay(cal.getLengthOfMonth(periodTo.getMonth(),periodTo.getYear()));
		}
	}
	
	
	
	

}
