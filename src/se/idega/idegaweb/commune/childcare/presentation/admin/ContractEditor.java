package se.idega.idegaweb.commune.childcare.presentation.admin;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.EJBException;

import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;
import se.idega.idegaweb.commune.childcare.data.ChildCareContract;
import se.idega.idegaweb.commune.childcare.data.ChildCareContractHome;
import se.idega.idegaweb.commune.childcare.data.EmploymentType;
import se.idega.idegaweb.commune.childcare.event.ChildCareEventListener;
import se.idega.idegaweb.commune.childcare.presentation.ChildCareBlock;

import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;

/**
 * @author gimmi
 */
public class ContractEditor extends ChildCareBlock {
	
	private ChildCareApplication application;
	
	private static String ACTION = "ce_a";
	private static String ACTION_EDIT = "ce_e";
	private static String ACTION_UPDATE = "ce_u";
	private static String ACTION_DELETE = "ce_d";
	
	private static String PARAMETER_CONTRACT_ID = "p_ci";
	private static String PARAMETER_FROM_DATE = "p_fd";
	private static String PARAMETER_CANCELLED_DATE = "p_cd";
	private static String PARAMETER_CARE_TIME = "p_ct";
	private static String PARAMETER_EMPLOYMENT_TYPE = "p_et";
	
	public void init(IWContext iwc) throws Exception {
		int applId = session.getApplicationID();
		if (applId > 0) {
			application = getBusiness().getApplication(applId);
			
			String action = iwc.getParameter(ACTION);
			if (ACTION_EDIT.equals(action)) {
				displayEditor(iwc);
			} else if (ACTION_UPDATE.equals(action)) {
				if (handleUpdate(iwc)) {
					displayContracts(iwc);
				} else {
					displayEditor(iwc);
				}
			} else if (ACTION_DELETE.equals(action)) { 
				deleteContract(iwc);
				displayContracts(iwc);
			} else {
				displayContracts(iwc);
			}
		} else {
			add(" No application selected ");
		}
		add(Text.getBreak());
		GenericButton back = (GenericButton) getStyledInterface(new GenericButton("back",localize("child_care.select_new_application","Select new application")));
		back.setPageToOpen(getBackPage());
		add(back);
		
	}
	
	private void deleteContract(IWContext iwc) {
		try {
			ChildCareContractHome contractHome = (ChildCareContractHome) IDOLookup.getHome(ChildCareContract.class);
			ChildCareContract contract = contractHome.findByPrimaryKey(new Integer(iwc.getParameter(PARAMETER_CONTRACT_ID)));
			getBusiness().removeContract(contract, iwc.getCurrentUser());
			add(getLocalizedSmallHeader("child_care.delete_failed", "Application successfully deleted"));
		} catch (Exception e) {
			add(getLocalizedSmallHeader("child_care.delete_failed", "Application was NOT deleted"));
		}
	}
	
	private boolean handleUpdate(IWContext iwc) {
		try {
			ChildCareContractHome contractHome = (ChildCareContractHome) IDOLookup.getHome(ChildCareContract.class);
			ChildCareContract contract = contractHome.findByPrimaryKey(new Integer(iwc.getParameter(PARAMETER_CONTRACT_ID)));
			
			String fromDate = iwc.getParameter(PARAMETER_FROM_DATE);
			String toDate = iwc.getParameter(PARAMETER_CANCELLED_DATE);
			String careTime = iwc.getParameter(PARAMETER_CARE_TIME);
			String empType = iwc.getParameter(PARAMETER_EMPLOYMENT_TYPE);
			
			int iCareTime = -1;
			int iEmpType = -1;
			Date dFromDate = null;
			Date dToDate = null;
			
			try {
				iCareTime = Integer.parseInt(careTime);
				iEmpType = Integer.parseInt(empType);
				dFromDate = (new IWTimestamp(fromDate)).getDate();
				try {
					dToDate = (new IWTimestamp(toDate)).getDate();
				} catch (Exception ignore) {}
				
			} catch (Exception e) {
				logError("Invalid input");
			}
			boolean success = false;
			if ( dFromDate != null && iCareTime > 0) {
				success = getBusiness().alterContract(contract, iCareTime, dFromDate, dToDate, iwc.getCurrentLocale(), iwc.getCurrentUser());
				//if (iEmpType > 0) {
					contract.setEmploymentType(iEmpType);
					contract.store();
				//}
			}
			
			add(getLocalizedSmallHeader("child_care.update_success", "Update success"));
			return success;
		} catch (Exception e) {
			e.printStackTrace();
			add(getLocalizedSmallHeader("child_care.update_failed", "Update failed"));
			return false;
		}
	}
	
	private void displayEditor(IWContext iwc) {
		Form form = new Form();
		Table table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		form.add(table);
		
		int row = 1;
		
		
		try {
			ChildCareContractHome contractHome = (ChildCareContractHome) IDOLookup.getHome(ChildCareContract.class);
			ChildCareContract contract = contractHome.findByPrimaryKey(new Integer(iwc.getParameter(PARAMETER_CONTRACT_ID)));
			
			
			DateInput from = (DateInput) getStyledInterface(new DateInput(PARAMETER_FROM_DATE));
			IWTimestamp stamp = IWTimestamp.RightNow();
			from.setYearRange(stamp.getYear()-2, stamp.getYear()+3);
			if (contract.getValidFromDate() != null) {
				from.setDate(contract.getValidFromDate());
			}
			DateInput cancelled = (DateInput) getStyledInterface(new DateInput(PARAMETER_CANCELLED_DATE));
			cancelled.setYearRange(stamp.getYear()-2, stamp.getYear()+3);
			if (contract.getTerminatedDate() != null) {
				cancelled.setDate(contract.getTerminatedDate());
			}
			TextInput careTime = (TextInput) getStyledInterface(new TextInput(PARAMETER_CARE_TIME));
			careTime.setSize(10);
			if (contract.getCareTime() > 0) {
				careTime.setContent (Integer.toString(contract.getCareTime()));
			}
			
			
			table.add(getSmallHeader(contract.getChild().getName()+" - "+contract.getApplication().getProvider().getName()), 1, row);
			table.mergeCells(1, row, 2, row++);
			
			table.add(new HiddenInput(PARAMETER_CONTRACT_ID, contract.getPrimaryKey().toString()));
			table.add(getLocalizedSmallText("child_care.valid_from","Valid from"), 1, row);
			table.add(from, 2, row++);
			table.add(getLocalizedSmallText("child_care.terminated","Terminated"), 1, row);
			table.add(cancelled, 2, row++);
			table.add(getLocalizedSmallText("child_care.care_time","Care time"), 1, row);
			table.add(careTime, 2, row++);
			table.add(getLocalizedSmallText("child_care.employment", "Employment"), 1, row);
			EmploymentType et = contract.getEmploymentType();
			int etId = -1;
			if (et != null) {
				etId = new Integer(et.getPrimaryKey().toString()).intValue();
			}
			table.add(getEmploymentTypes(PARAMETER_EMPLOYMENT_TYPE, etId), 2, row++);
			
			Link back = new Link(getResourceBundle().getLocalizedImageButton("child_care.cancel","Cancel"));
			table.add(back, 1, row);
			
			SubmitButton update = new SubmitButton(getResourceBundle().getLocalizedImageButton("child_care.update", "Update"), ACTION, ACTION_UPDATE);
			table.add(update, 2, row);
			table.setAlignment(2, row, Table.HORIZONTAL_ALIGN_RIGHT);
			
		} catch (Exception e) {
			logError(e.getMessage());
		}
		
		
//		table.add();
		table.setHorizontalZebraColored(getZebraColor1(), getZebraColor2());
		table.setRowColor(1, getHeaderColor());
		
		add(form);
	}
	
	private void displayContracts(IWContext iwc) throws RemoteException {
		Collection contracts = null;
		try {
			contracts = getBusiness().getContractsByApplication(Integer.parseInt(application.getPrimaryKey().toString()));
		} catch (EJBException e) {
			logError(e.getMessage());
		}
		
		
		Table table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.setColumns(11);
		
		
		int row = 1;
		int column = 1;

		
		table.mergeCells(1, row, 5, row);
		table.add(getSmallHeader(application.getProvider().getName()), 1, row);

		++row;
		table.add(getLocalizedSmallHeader("child_care.name","Name"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.personal_id","Personal ID"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.created","Created"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.valid_from","Valid from"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.terminated","Terminated"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.status","Status"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.care_time","Care time"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.employment","Employment"), column++, row);
		table.setRowColor(row++, getHeaderColor());
		
		boolean showComment = false;
		boolean showNotActiveComment = false;
		boolean showRemovedComment = false;
		
		if (contracts != null) {
			ChildCareContract contract;
			User child;
			IWTimestamp created;
			IWTimestamp validFrom;
			IWTimestamp cancelledDate;
			EmploymentType et;
			Link editLink = null;
			Link deleteLink = null;
			Link viewContract;
			Link archive;
			boolean isCancelled;
			boolean isNotYetActive;
			boolean hasComment = false;
			IWTimestamp dateNow = new IWTimestamp();
			
			Iterator iter = contracts.iterator();
			while (iter.hasNext()) {
//				contract = getBusiness().getValidContract(((Integer)application.getPrimaryKey()).intValue());
				contract = (ChildCareContract) iter.next();
				child = application.getChild();
				hasComment = true;
				column = 1;
	
				if (row % 2 == 0)
					table.setRowColor(row, getZebraColor1());
				else
					table.setRowColor(row, getZebraColor2());
				
				if (contract != null) {
					created = new IWTimestamp(contract.getCreatedDate());
					if (contract.getValidFromDate() != null) {
						validFrom = new IWTimestamp(contract.getValidFromDate());
					} else {
						validFrom = null;
					}
					if (contract.getTerminatedDate() != null) {
						cancelledDate = new IWTimestamp(contract.getTerminatedDate());
						isCancelled = cancelledDate.isEarlierThan(dateNow);
					}
					else {
						cancelledDate = null;
						isCancelled = false;
					}
					
					if (validFrom != null) {
						if (dateNow.isEarlierThan(validFrom))
							isNotYetActive = true;
						else
							isNotYetActive = false;
					}
					else {
						isNotYetActive = false;
						isCancelled = true;
					}
					
					viewContract = new Link(getPDFIcon(localize("child_care.view_contract","View contract")));
					viewContract.setFile(contract.getContractFileID());
					viewContract.setTarget(Link.TARGET_NEW_WINDOW);
					
					editLink = new Link(getEditIcon(localize("child_care.edit_contract", "Edit contract")));
					editLink.addParameter(ACTION, ACTION_EDIT);
					editLink.addParameter(PARAMETER_CONTRACT_ID, contract.getPrimaryKey().toString());

					deleteLink = new Link(getDeleteIcon(localize("child_care.delete_contract","Delete contract")));
					deleteLink.addParameter(ACTION, ACTION_DELETE);
					deleteLink.addParameter(PARAMETER_CONTRACT_ID, contract.getPrimaryKey().toString());
					
					if (isNotYetActive) {
						showComment = true;
						hasComment = true;
						showNotActiveComment = true;
						table.add(getSmallErrorText("*"), column, row);
					}
					if (application.getRejectionDate() != null) {
						showComment = true;
						hasComment = true;
						showRemovedComment = true;
						table.add(getSmallErrorText("+"), column, row);
					}
					
					if (hasComment)
						table.add(getSmallErrorText(Text.NON_BREAKING_SPACE), column, row);
					if (getResponsePage() != null) {
						archive = getSmallLink(child.getNameLastFirst(true));
						archive.setEventListener(ChildCareEventListener.class);
						archive.addParameter(getSession().getParameterUserID(), application.getChildId());
						archive.addParameter(getSession().getParameterApplicationID(), ((Integer)application.getPrimaryKey()).intValue());
						archive.setPage(getResponsePage());
						table.add(archive, column++, row);
					}
					else
						table.add(getSmallText(child.getNameLastFirst(true)), column++, row);
	
					table.add(getSmallText(PersonalIDFormatter.format(child.getPersonalID(), iwc.getCurrentLocale())), column++, row);
					table.add(getSmallText(created.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), column++, row);
					if (validFrom != null)
						table.add(getSmallText(validFrom.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), column++, row);
					else
						table.add(getSmallText("-"), column++, row);
					if (cancelledDate != null)
						table.add(getSmallText(cancelledDate.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), column++, row);
					else
						table.add(getSmallText("-"), column++, row);
					if (isCancelled)
						table.add(getSmallText(localize("child_care.status_cancelled","Cancelled")), column, row);
					else
						table.add(getSmallText(localize("child_care.status_active","Active")), column, row);
					column++;
					table.add(getSmallText(String.valueOf(contract.getCareTime())), column++, row);
					
					et = contract.getEmploymentType();
					if (et != null) {
						table.add(getSmallText(localize(et.getLocalizationKey(), et.getLocalizationKey())), column, row);
					} else {
						table.add(getSmallText("-"), column, row);
					}
					column++;
					
					table.setWidth(column, row, 12);
					table.add(viewContract, column++, row);
					
					table.setWidth(column, row, 12);
					table.add(editLink, column++, row);
					table.add(Text.NON_BREAKING_SPACE);
					table.setWidth(column, row, 12);
					table.add(deleteLink, column++, row);
				}
				else {
					table.add(getSmallText(child.getNameLastFirst(true)), column++, row);
					table.add(getSmallText(PersonalIDFormatter.format(child.getPersonalID(), iwc.getCurrentLocale())), column++, row);
				}
				row++;
			}
			table.setColumnAlignment(2, Table.HORIZONTAL_ALIGN_CENTER);
			table.setColumnAlignment(3, Table.HORIZONTAL_ALIGN_CENTER);
			table.setColumnAlignment(4, Table.HORIZONTAL_ALIGN_CENTER);
			table.setColumnAlignment(5, Table.HORIZONTAL_ALIGN_CENTER);
			table.setColumnAlignment(6, Table.HORIZONTAL_ALIGN_CENTER);
		}
		
		if (showComment) {
			table.setHeight(row++, 2);
			if (showNotActiveComment) {
				table.mergeCells(1, row, table.getColumns(), row);
				table.add(getSmallErrorText("*"), 1, row);
				table.add(getSmallText(Text.NON_BREAKING_SPACE + localize("child_care.not_yet_active_placing","Placing not yet active")), 1, row++);
			}
			if (showRemovedComment) {
				table.mergeCells(1, row, table.getColumns(), row);
				table.add(getSmallErrorText("+"), 1, row);
				table.add(getSmallText(Text.NON_BREAKING_SPACE + localize("child_care.contract_ended","Contract has termination date")), 1, row++);
			}
		}
		
	//}
		//contract.get
		add(table);
	}
	
}
