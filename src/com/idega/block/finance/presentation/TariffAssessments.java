package com.idega.block.finance.presentation;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.ejb.EJBException;
import javax.ejb.FinderException;

import com.idega.block.finance.business.AssessmentBusiness;
import com.idega.block.finance.business.AssessmentTariffPreview;
import com.idega.block.finance.business.FinanceHandler;
import com.idega.block.finance.data.Account;
import com.idega.block.finance.data.AccountEntry;
import com.idega.block.finance.data.AccountEntryHome;
import com.idega.block.finance.data.AccountInfo;
import com.idega.block.finance.data.AccountUser;
import com.idega.block.finance.data.AccountUserHome;
import com.idega.block.finance.data.AssessmentRound;
import com.idega.block.finance.data.AssessmentStatus;
import com.idega.block.finance.data.TariffGroup;
import com.idega.core.user.data.User;
import com.idega.data.IDOException;
import com.idega.idegaweb.presentation.BusyBar;
import com.idega.presentation.CollectionNavigator;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.util.Edit;
import com.idega.util.IWCalendar;
import com.idega.util.IWTimestamp;
/**
 * Title: Description: Copyright: Copyright (c) 2000-2001 idega.is All Rights
 * Reserved Company: idega
 * 
 * @author <a href="mailto:aron@idega.is">Aron Birkir </a>
 * @version 1.1
 */
public class TariffAssessments extends Finance {
	private static final String PRM_ACCOUNT_ID = "ass_acc_id";
	private static final String PRM_ROUND_ID = "ass_round_id";
	private static final String PRM_GROUP_ID = "tass_grp";
	private static final String PRM_SHOW_ALL = "shw_all";
	protected static final int ACT1 = 1, ACT2 = 2, ACT3 = 3, ACT4 = 4, ACT5 = 5, ACT6 = 6, ACT7 = 7,ACT8=8;
	public static final String PRM_ACTION = "tt_action";
	protected boolean isAdmin = false;
	private Integer groupID = null;
	
	private FinanceHandler handler = null;
	private Date fromDate = null, toDate = null;
	private Collection groups;
	private TariffGroup group;
	
	private Integer accountID = null;
	private Integer roundID = null;
	
	
	private Collection accountEntries = null;
	private Collection accounts = null;
	private Collection assessments = null;
	
	private boolean showAll = false;
	
	private CollectionNavigator collectionNavigator =null;
	
	//  private int iCategoryId = -1;
	//private StatusBar status;
	public TariffAssessments() {
	}
	public String getLocalizedNameKey() {
		return "assessment";
	}
	public String getLocalizedNameValue() {
		return "Assessment";
	}
	protected void control(IWContext iwc) {
		if (isAdmin) {
			initAccounts(iwc);
			initDates(iwc);
			initGroups(iwc);
			
		
			
			//setButtonPanel(makeLinkTable(1));
			
			try {
				
				if (iwc.getParameter(PRM_ACTION) == null) {
					initCollections(iwc);
					setMainPanel(getTableOfAssessments(iwc));
				}
				if (iwc.getParameter(PRM_ACTION) != null) {
					String sAct = iwc.getParameter(PRM_ACTION);
					int iAct = Integer.parseInt(sAct);
					switch (iAct) {
						case ACT1 :
							initCollections(iwc);
							setMainPanel(  getTableOfAssessments(iwc));
							break;
						case ACT2 :
							initCollections(iwc);
							setMainPanel( doMainTable(iwc));
							break;
						case ACT3 :
							doAssess(iwc);
							initCollections(iwc);
							setMainPanel(getTableOfAssessments(iwc));
							break;
						case ACT4 :
							initCollections(iwc);
							setInfoPanel(getAssessmentInfoTable(iwc));
							setMainPanel( getTableOfAssessmentAccounts(iwc));
							break;
						case ACT5 :
							doRollback(iwc);
							initCollections(iwc);
							setMainPanel(  getTableOfAssessments(iwc));
							
							break;
						case ACT6 :
							initCollections(iwc);
							setMainPanel(  getPreviewTable(iwc));
							break;
						case ACT7 :
							initCollections(iwc);
							setInfoPanel(getAssessmentInfoTable(iwc));
							setInfoPanel(Text.getBreak());
							setInfoPanel(getAccountInfoTable(iwc));
							setMainPanel(getTableOfAssessmentAccountEntries(iwc));
							
							break;
						case ACT8 :
							doPublish(iwc);
							initCollections(iwc);
							setMainPanel(  getTableOfAssessments(iwc));
							
							break;
						default :
							initCollections(iwc);
							setMainPanel( getTableOfAssessments(iwc));
							
							break;
					}
				}
				
				//T.add(getGroupLinks(iwc), 1, 2);
				if (group != null) {
					//T.add(makeLinkTable(1), 1, 3);
				}
				setLocalizedTitle("assessments","Assessments");
				//setSearchPanel(makeLinkTable(1));
				
			} catch (Exception S) {
				S.printStackTrace();
			}
			
			setTabPanel(getGroupLinks(iwc));
			setNavigationPanel(collectionNavigator);
			Link showAllLink = new Link(getText(localize("showall","Show all")));
			showAllLink.addParameter(PRM_SHOW_ALL,"true");
			showAllLink.maintainParameter(PRM_ROUND_ID,iwc);
			showAllLink.maintainParameter(PRM_GROUP_ID,iwc);
			showAllLink.maintainParameter(PRM_ACTION,iwc);
			setNavigationPanel(showAllLink);
			setSearchPanel(getActionButtonsTable(iwc));
		} else
			add(localize("access_denied", "Access denies"));
	}
	
	private void initAccounts(IWContext iwc){
		String id = iwc.getParameter(PRM_ROUND_ID);
		String acc_id = iwc.getParameter(PRM_ACCOUNT_ID);
		if(id!=null)
			roundID = Integer.valueOf(id);
		if(acc_id !=null)
			accountID = Integer.valueOf(acc_id);
			
	}
	
	private void initCollections(IWContext iwc){
		if(groupID!=null){
			// assessment list
			if(roundID==null && accountID==null){
				
				try {
					int assessmentCount = getFinanceService().getAssessmentRoundHome().getCountByCategoryAndTariffGroup(getFinanceCategoryId(),groupID,this.fromDate,this.toDate,null);
					setCollectionSize(assessmentCount);
				} catch (RemoteException e1) {
					e1.printStackTrace();
				} catch (IDOException e1) {
					e1.printStackTrace();
				}
				
				this.collectionNavigator = getCollectionNavigator(iwc);
				collectionNavigator.addMaintainParameter(PRM_GROUP_ID);
				collectionNavigator.addMaintainParameter(PRM_ACTION);
				
				try {
					assessments = getFinanceService().getAssessmentRoundHome().findByCategoryAndTariffGroup(getFinanceCategoryId(),groupID,this.fromDate,this.toDate,null,getCollectionViewSize(),getCollectionIndex());
					
				} catch (RemoteException e) {
					e.printStackTrace();
				} catch (FinderException e) {
					e.printStackTrace();
				};
				
				
			}
			// account viewing
			if(roundID!=null){
				 try {
					int accountCount = getFinanceService().getAccountHome().countByAssessmentRound(roundID);
					 setCollectionSize(accountCount);
				} catch (RemoteException e1) {
					e1.printStackTrace();
				} catch (IDOException e1) {
					e1.printStackTrace();
				}
				
				if(accountID!=null || iwc.isParameterSet("accpg"))
					setCollectionViewSize( 1);
					
				this.collectionNavigator = getCollectionNavigator(iwc);
				collectionNavigator.addMaintainParameter(PRM_GROUP_ID);
				collectionNavigator.addMaintainParameter(PRM_ROUND_ID);
				collectionNavigator.addMaintainParameter(PRM_ACTION);
				
				
				if(accountID!=null ){
					collectionNavigator.addMaintainParameter("accpg");
					try {
						accountEntries = getFinanceService().getAccountEntryHome().findByAccountAndAssessmentRound(accountID, roundID);
						//setCollectionSize(accountEntries.size());
					} catch (RemoteException e) {
						e.printStackTrace();
					} catch (FinderException e) {
						e.printStackTrace();
					}
					
					
					
				}
				
				else {
					
					 if(iwc.isParameterSet(collectionNavigator.getParameterName())){
						try {
							Collection accs = getFinanceService().getAccountHome().findByAssessmentRound(roundID, 1,getCollectionIndex());
							if(accs!=null && !accs.isEmpty()){
								Account account = (Account)accs.iterator().next();
								accountEntries = getFinanceService().getAccountEntryHome().findByAccountAndAssessmentRound((Integer)account.getPrimaryKey(), roundID);
								accountID = (Integer)account.getPrimaryKey();
							}
						} catch (RemoteException e) {
							e.printStackTrace();
						} catch (EJBException e) {
							e.printStackTrace();
						} catch (FinderException e) {
							e.printStackTrace();
						}
						
						
					}	
				
						try {
							if(iwc.isParameterSet(PRM_SHOW_ALL))
								accounts = getFinanceService().getAccountHome().findByAssessmentRound(roundID,-1,-1);
							else
								accounts = getFinanceService().getAccountHome().findByAssessmentRound(roundID, getCollectionViewSize(),getCollectionIndex());
						} catch (RemoteException e) {
							e.printStackTrace();
						} catch (FinderException e) {
							e.printStackTrace();
						}
				
					
				}
				
			}
			
		}
	}
	
	private void initDates(IWContext iwc){
		//IWCalendar cal = new IWCalendar();
		IWTimestamp Today = IWTimestamp.RightNow();
	
		IWTimestamp from = IWTimestamp.RightNow();
		from.addMonths(-6);
		fromDate = from.getDate();
		IWTimestamp to = new IWTimestamp(1,Today.getMonth(),Today.getYear() );
		to.addMonths(1);
		toDate = to.getDate();
	}
	
	private void initGroups(IWContext iwc) {
		groupID = new Integer(-1);
		try {
			groups = getFinanceService().getTariffGroupHome().findByCategory(getFinanceCategoryId());
		} catch (RemoteException e1) {
			e1.printStackTrace();
		} catch (FinderException e1) {
			e1.printStackTrace();
		}
		group = null;
		if (iwc.isParameterSet(PRM_GROUP_ID))
			groupID = Integer.valueOf(iwc.getParameter(PRM_GROUP_ID));
		//add("group "+iGroupId);
		if (groupID != null && groupID.intValue() > 0) {
			try {
				//group =
				// FinanceFinder.getInstance().getTariffGroup(iGroupId);
				group = getFinanceService().getTariffGroupHome().findByPrimaryKey(groupID);
			} catch (RemoteException e2) {
				e2.printStackTrace();
			} catch (FinderException e2) {
				e2.printStackTrace();
			}
		} else if (groups != null && !groups.isEmpty()) {
			group = (TariffGroup) groups.iterator().next();
			groupID = ((Integer) group.getPrimaryKey());
		}
		if (group != null) {
			groupID = ((Integer) group.getPrimaryKey());
			if (group.getHandlerId() > 0) {
				try {
					//handler =
					// FinanceFinder.getInstance().getFinanceHandler(group.getHandlerId());
					handler = getFinanceService().getFinanceHandler(new Integer(group.getHandlerId()));
				} catch (RemoteException e2) {
				}
			}
		}
	}
	private PresentationObject getGroupLinks(IWContext iwc) {
		Table T = new Table();
		T.setCellpadding(0);
		T.setCellspacing(0);
		T.setWidth("100%");
		int col = 1;
		if (groups != null) {
			java.util.Iterator I = groups.iterator();
			TariffGroup group;
			Link tab;
			while (I.hasNext()) {
				group = (TariffGroup) I.next();
				tab = new Link(iwb.getImageTab(group.getName(), false));
				tab.addParameter(Finance.getCategoryParameter(iCategoryId));
				tab.addParameter(PRM_GROUP_ID, group.getPrimaryKey().toString());
				T.add(tab, col++, 1);
			}
		}
		T.setWidth(col, 1, "100%");
		//T.add(status, ++col, 1);
		T.setAlignment(col, 1, "right");
		return T;
	}
	private void doRollback(IWContext iwc) {
		String sRoundId = iwc.getParameter("rollback");
		if (sRoundId != null) {
			Integer roundId = Integer.valueOf(sRoundId);
			try {
				//boolean rb = false;
				if (handler != null) {
					handler.rollbackAssessment(iwc,roundId);
				} else {
					AssessmentBusiness assBuiz = (AssessmentBusiness) com.idega.business.IBOLookup.getServiceInstance(
							iwc, AssessmentBusiness.class);
					assBuiz.rollBackAssessment(roundId);
				}
				//AssessmentBusiness.rollBackAssessment(iRoundId);
				/*if (rb)
					status.setMessage(localize("rollback_success", "Rollback was successfull"));
				else
					status.setMessage(localize("rollback_unsuccess", "Rollback was unsuccessfull"));
					*/
			} catch (Exception ex) {
				ex.printStackTrace();
				//status.setMessage(localize("rollback_illegal", "Rollback was illegal"));
			}
		}
	}
	private void doPublish(IWContext iwc) {
		String sRoundId = iwc.getParameter("publish");
		if (sRoundId != null) {
			Integer roundId = Integer.valueOf(sRoundId);
			try {
				//boolean rb = false;
				if (handler != null) {
					handler.publishAssessment(iwc,roundId);
				} else {
					AssessmentBusiness assBuiz = (AssessmentBusiness) com.idega.business.IBOLookup.getServiceInstance(
							iwc, AssessmentBusiness.class);
					assBuiz.publishAssessment(roundId);
				}
				//AssessmentBusiness.rollBackAssessment(iRoundId);
				/*if (rb)
					status.setMessage(localize("rollback_success", "Rollback was successfull"));
				else
					status.setMessage(localize("rollback_unsuccess", "Rollback was unsuccessfull"));
					*/
			} catch (Exception ex) {
				ex.printStackTrace();
				//status.setMessage(localize("rollback_illegal", "Rollback was illegal"));
			}
		}
	}
	private void doAssess(IWContext iwc)throws RemoteException {
		//add(handler.getClass().getName());
		//PresentationObject MO = new Text("failed");
		if (iwc.getParameter("pay_date") != null) {
			String date = iwc.getParameter("pay_date");
			String start = iwc.getParameter("start_date");
			String end = iwc.getParameter("end_date");
			String roundName = iwc.getParameter("round_name");
			String accountKeyId = iwc.getParameter("account_key_id");
			Integer excessRoundID = Integer.valueOf(iwc.getParameter("excess_round_id"));
			//add(accountKeyId);
			if (date != null && date.length() == 10) {
				if (roundName != null && roundName.trim().length() > 1) {
					roundName = roundName == null ? "" : roundName;
					Integer accKeyId = accountKeyId != null ? Integer.valueOf(accountKeyId) :null;
					IWTimestamp paydate = new IWTimestamp(date);
					IWTimestamp startdate = new IWTimestamp(start);
					IWTimestamp enddate = new IWTimestamp(end);
					//add(paydate.getISLDate());
					debug("Starting Execution " + IWTimestamp.RightNow().toString());
					handler.executeAssessment(iwc,getFinanceCategoryId(), groupID, roundName,new Integer( 1),
							accKeyId, paydate, startdate, enddate,excessRoundID);
					debug("Ending Execution " + IWTimestamp.RightNow().toString());
					/*if (assessed) {
						status.setMessage(localize("assessment_sucess", "Assessment succeded"));
					} else {
						status.setMessage(localize("assessment_failure", "Assessment failed"));
					}*/
					//MO = getTableOfAssessments(iwc);
				} else {
					add(localize("no_name_error", "No name entered"));
					//MO = doMainTable(iwc);
				}
			} else {
				//MO = doMainTable(iwc);
			}
		} else
			System.err.println("did not have pay_date");
		//return MO;
	}
	protected PresentationObject getActionButtonsTable(IWContext iwc) {
		Table LinkTable = new Table(4, 1);
		if (isAdmin) {
			//int last = 4;
			LinkTable.setWidth("100%");
			LinkTable.setWidth(4,"100%");
			LinkTable.setCellpadding(getCellpadding());
			LinkTable.setCellspacing(getCellspacing());
			
			
			Link Link1 =new Link(getHeader(localize("view", "View")));
			
			Link1.addParameter(this.PRM_ACTION, String.valueOf(this.ACT1));
			Link1.addParameter(PRM_GROUP_ID, groupID.toString());
			
			Link Link2 = new Link(getHeader(localize("new", "New")));
			
			Link2.addParameter(this.PRM_ACTION, String.valueOf(this.ACT2));
			Link2.addParameter(PRM_GROUP_ID, groupID.toString());
			
			Link Link3 = new Link(getHeader(localize("preview", "Preview")));
			
			Link3.addParameter(this.PRM_ACTION, String.valueOf(this.ACT6));
			Link3.addParameter(PRM_GROUP_ID, groupID.toString());
		
		
			//status.setMessageCaller(Link1, localize("view_assessments", "View assessments"));
			//status.setMessageCaller(Link2, localize("make_new_assessment", "Create new assessment"));
			//status.setMessageCaller(Link3, localize("preview_assessment", "Preview assessment"));
			LinkTable.add(Link1, 1, 1);
			LinkTable.add(Link2, 2, 1);
			LinkTable.add(Link3, 3, 1);
		}
		return LinkTable;
	}
	private PresentationObject getTableOfAssessments(IWContext iwc) throws RemoteException{
		Table T = new Table();
		int row = 2;
		//List L = Finder.listOfAssessments();
		//List L =
		// FinanceFinder.getInstance().listOfAssessmentInfo(iCategoryId,iGroupId);
		
		if (assessments != null && !assessments.isEmpty()) {
			BusyBar busy = new BusyBar("busyguy");
			String sRollBack = localize("rollback", "Rollback");
			T.add(getHeader(localize("assessment_name", "Assessment name")), 1, 1);
			T.add(getHeader(localize("assessment_stamp", "Timestamp")), 2, 1);
			T.add(getHeader(localize("totals", "Total amount")), 3, 1);
			T.add(getHeader(localize("status", "Status")), 4, 1);
			T.add(getHeader(localize("totals", "Total amount")), 5, 1);
			T.add(getHeader(sRollBack), 6, 1);
			//int col = 1;
			row = 2;
			AssessmentRound AR;
			AccountEntryHome eHome = getFinanceService().getAccountEntryHome();
			java.text.DateFormat df = getDateTimeFormat(iwc.getCurrentLocale());
			float total = 0;
			Integer rndID;
			for (Iterator iter = assessments.iterator(); iter.hasNext();) {
				AR = (AssessmentRound) iter.next();
				rndID = (Integer )AR.getPrimaryKey();
				T.add(getRoundLink(AR.getName(),rndID, groupID), 1, row);
				T.add(getText(df.format(AR.getRoundStamp())), 2, row);
			
				try {
					double subtotal  = eHome.getTotalSumByAssessmentRound(rndID);
					T.add(getAmountText(subtotal), 3, row);
					total += subtotal;
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
				T.add(getText((localize("assessmentstatus."+AR.getStatus(),AR.getStatus()))), 4, row);
				if(AR.getStatus().equals(AssessmentStatus.ASSESSED)){
					Link R = getRollbackLink(AR);
					//status.setMessageCaller(R, localize("rollback", "Rolls back this assessment"));
					busy.setLinkObject(R);
					T.add(getPublishLink(AR),5,row);
					T.add(R,6, row);
				
				}
				
				row++;
			}
			T.add(busy, 2, row);
			T.add(getAmountText((total)), 3, row);
			T.setWidth(Table.HUNDRED_PERCENT);
			T.setCellpadding(2);
			T.setCellspacing(1);
			T.setHorizontalZebraColored(getZebraColor1(), getZebraColor2());
			T.setRowColor(1, getHeaderColor());
			T.setColumnAlignment(3, Table.HORIZONTAL_ALIGN_RIGHT);
			T.setColumnAlignment(4, Table.HORIZONTAL_ALIGN_CENTER);
			row++;
		} else
			T.add(localize("no_assessments", "No assessments"), 1, row);
		return T;
	}
	private Link getRollbackLink(AssessmentRound AR) {
		Link R = new Link(iwb.getImage("rollback.gif","roll"));
		R.addParameter("rollback", AR.getPrimaryKey().toString());
		R.addParameter(PRM_ACTION, ACT5);
		R.addParameter(PRM_GROUP_ID, groupID.toString());
		return R;
	}
	private Link getPublishLink(AssessmentRound AR) {
		Link R = new Link(iwb.getImage("publish.gif","publish"));
		R.addParameter("publish", AR.getPrimaryKey().toString());
		R.addParameter(PRM_ACTION, ACT8);
		R.addParameter(PRM_GROUP_ID, groupID.toString());
		return R;
	}
	private PresentationObject getPreviewTable(IWContext iwc) throws java.rmi.RemoteException {
		Table T = new Table();
		T.setWidth(Table.HUNDRED_PERCENT);
		if (handler != null && group != null) {
			Collection L = handler.listOfAssessmentTariffPreviews(iwc,((Integer) group.getPrimaryKey()), new IWTimestamp(fromDate),new IWTimestamp(toDate));
			if (L != null) {
				int row = 1;
				float totals = 0;
				T.add(localize("tariff_name", "Tariff name"), 1, row);
				T.add(localize("account_count", "Account count"), 2, row);
				T.add(localize("total_amount", "Total amount"), 3, row);
				row++;
				Iterator I = L.iterator();
				AssessmentTariffPreview preview;
				double amount;
				while (I.hasNext()) {
					preview = (AssessmentTariffPreview) I.next();
					T.add(getText(preview.getName()), 1, row);
					T.add(getText(String.valueOf(preview.getAccounts())), 2, row);
					amount = preview.getTotals();
					T.add(getText(Double.toString(amount)), 3, row);
					totals += amount;
					row++;
				}
				T.add(getText(Double.toString(totals)), 3, row);
				T.setColumnAlignment(3, "right");
			}
		}
		return T;
	}
	private Map mapOfAccountUsers(Integer assessmentRoundId) {
		//List L =
		// FinanceFinder.getInstance().listOfAccountUsersByRoundId(iAssessmentRoundId);
		Collection users = null;
		try {
			users = getFinanceService().getAccountUserHome().findByAssessmentRound(assessmentRoundId);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		if (users != null) {
			Iterator iter = users.iterator();
			Map map = new java.util.HashMap();
			AccountUser u;
			while (iter.hasNext()) {
				u = (AccountUser) iter.next();
				map.put((Integer) u.getPrimaryKey(), u);
			}
			return map;
		}
		return null;
	}
	private PresentationObject getTableOfAssessmentAccounts(IWContext iwc) throws RemoteException {
		Table T = new Table();
		
		if (roundID != null) {
			
		
				//List L = null
				// ;//CampusAccountFinder.listOfContractAccountApartmentsInAssessment(Integer.parseInt(id));
				if (accounts != null) {
					//Map users = mapOfAccountUsers(ID);
					T.add(getHeader(localize("account_name", "Account name")), 1, 1);
					T.add(getHeader(localize("user_name", "User name")), 2, 1);
					T.add(getHeader(localize("personal_id", "Personal ID")), 3, 1);
					
					//T.add(Edit.titleText(localize("account_stamp","Last
					// updated")),2,1);
					//T.add(getText(localize("last_updated", "Last updated")), 4, 1);
					T.add(getHeader(localize("amount", "Amount")), 4, 1);
					int row = 2;
					Account A;
					
					float total = 0;
					//String username;
					AccountUserHome uHome = getFinanceService().getAccountUserHome();
					AccountEntryHome eHome = getFinanceService().getAccountEntryHome();
					int count = 0;
					Integer accID;
					for (Iterator iter = accounts.iterator(); iter.hasNext();) {
						A = (Account) iter.next();
						accID = (A.getAccountId());
						T.add(getAccountEntryLink(A.getAccountName(), roundID, accID,count++), 1, row);
						try {
							User user = uHome.findByPrimaryKey(new Integer(A.getUserId()));
							T.add(getText(user.getName()), 2, row);
							T.add(getText(user.getPersonalID()), 3, row);
						} catch (FinderException e) {
							e.printStackTrace();
						}
						
						//T.add(getText(new IWTimestamp(A.getLastUpdated()).getLocaleDate(iwc)), 4, row);
						try {
							double balance = eHome.getTotalSumByAccountAndAssessmentRound(accID,roundID);
							T.add(getAmountText(balance), 4, row);
							total += balance;
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
						
						row++;
					}
					T.add(getAmountText(total), 4, row);
					T.setWidth(Table.HUNDRED_PERCENT);
					T.setCellpadding(getCellpadding());
					T.setCellspacing(getCellspacing());
					T.setHorizontalZebraColored(getZebraColor1(), getZebraColor2());
					T.setRowColor(1,getHeaderColor());
					T.setColumnAlignment(4, Table.HORIZONTAL_ALIGN_RIGHT);
					row++;
				} else
					add(localize("no_accounts_found","No accounts found !"));
		
		}
		return T;
	}
	private Link getAccountEntryLink(String label, Integer roundID, Integer accountID,int number) {
		Link li = getLink(label);
		li.addParameter(PRM_ROUND_ID, roundID.toString());
		li.addParameter(PRM_ACCOUNT_ID, accountID.toString());
		li.addParameter(PRM_GROUP_ID,groupID.toString());
		li.addParameter(PRM_ACTION, ACT7);
		li.addParameter("accpg","on");
		li.addParameter(CollectionNavigator.getParameterName(),getCollectionIndex()*getCollectionViewSize()+number);
		return li;
	}
	
	private Table getAssessmentInfoTable(IWContext iwc){
		Table T = new Table();
		T.setWidth(Table.HUNDRED_PERCENT);
		T.setCellspacing(getCellspacing());
		T.setCellpadding(getCellpadding());
		if(roundID!=null){
			AssessmentRound assessment = null;
			try {
				 assessment = getFinanceService().getAssessmentRoundHome().findByPrimaryKey(roundID);
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (FinderException e) {
				e.printStackTrace();
			}
			if(assessment!=null){
				T.add(getHeader(localize("assessment_label","Assessment label")),1,1);
				T.add(getHeader(localize("assessment_date","Assessment date")),1,2);
				T.add(getText(assessment.getName()),2,1);
				T.add(getText(getDateTimeFormat(iwc.getCurrentLocale()).format(assessment.getRoundStamp())),2,2);
				T.add(getHeader(localize("assessment_total","Assessment total")),4,1);
				try {
					double roundtotal  = getFinanceService().getAccountEntryHome().getTotalSumByAssessmentRound(roundID);
					T.add(getAmountText(roundtotal), 5, 1);
					
				} catch (Exception e) {
					T.add(getErrorText(localize("amount_not_available","Amount not available")), 5, 1);
				} 
				
			}
			
			T.setAlignment(2,1,T.HORIZONTAL_ALIGN_RIGHT);
			T.setAlignment(2,2,T.HORIZONTAL_ALIGN_RIGHT);
			T.setAlignment(4,1,T.HORIZONTAL_ALIGN_RIGHT);
			T.setAlignment(4,2,T.HORIZONTAL_ALIGN_RIGHT);
			T.setWidth(1,"100");
			T.setWidth(2,"100");
			T.setWidth(3,"50");
			T.setWidth(4,"100");
			T.setWidth(5,"100");
			
			T.setHorizontalZebraColored(getZebraColor2(),getZebraColor2());
		}
		return T;
	}
	
	private Table getAccountInfoTable(IWContext iwc){
		Table accountInfoTable = new Table();
		accountInfoTable.setWidth(Table.HUNDRED_PERCENT);
		accountInfoTable.setCellpadding(getCellpadding());
		accountInfoTable.setCellspacing(getCellspacing());
		
		if (roundID != null && accountID!=null) {
			
		AccountUser accountOwner = null;
		AccountInfo account = null;
		//List L =
		// FinanceFinder.getInstance().listOfAssessmentAccountEntries(iAccountId,Integer.parseInt(id));
		
		try {
			account = getFinanceService().getAccountInfoHome().findByPrimaryKey(accountID);
			accountOwner = getFinanceService().getAccountUserHome().findByPrimaryKey(new Integer(account.getUserId()));
			
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		
		accountInfoTable.add(getHeader(localize("account_owner", "Account owner") ), 1,1);
		accountInfoTable.add(getHeader(localize("account_owner_pid", "Personal Id")),	1, 2);
		if (accountOwner != null) {
			accountInfoTable.add(getText(accountOwner.getName()), 2, 1);
			String pid = accountOwner.getPersonalID();
			if (pid == null)
				pid = localize("missing.personal_id", "Personal ID missing");
			accountInfoTable.add(getText(pid), 2,2);
		}
		accountInfoTable.add(getHeader(localize("account_balance", "Balance")), 4, 1);
		accountInfoTable.add(getHeader(localize("account_last_updated", "Last updated")), 4, 2);
		accountInfoTable.setAlignment(5,1,Table.HORIZONTAL_ALIGN_RIGHT);
		accountInfoTable.setAlignment(5,2,Table.HORIZONTAL_ALIGN_RIGHT);
		if (account != null) {
			accountInfoTable.add(getAmountText((account.getBalance())), 5, 1);
			accountInfoTable.add(getText(getDateTimeFormat(iwc.getCurrentLocale()).format(account.getLastUpdated())), 5,2);
		}
		accountInfoTable.setAlignment(2,1,Table.HORIZONTAL_ALIGN_RIGHT);
		accountInfoTable.setAlignment(2,2,Table.HORIZONTAL_ALIGN_RIGHT);
		accountInfoTable.setHorizontalZebraColored(getZebraColor2(),getZebraColor2());
		accountInfoTable.setWidth(1,"100");
		accountInfoTable.setWidth(2,"100");
		accountInfoTable.setWidth(3,"50");
		accountInfoTable.setWidth(4,"100");
		accountInfoTable.setWidth(5,"100");
		}
		return accountInfoTable;
		
	}
	
	private PresentationObject getTableOfAssessmentAccountEntries(IWContext iwc) {
		Table T = new Table();
			
			if (accountEntries != null) {
				
				int row = 1;
				T.add(getHeader(localize("entry_name", "Entry name")), 1, row);
				T.add(getHeader(localize("entry_info", "Info name")), 2, row);
				//T.add(Edit.titleText(localize("account_stamp","Last
				// updated")),2,1);
				T.add(getHeader(localize("payment_date", "Payment day")), 3, row);
				T.add(getHeader(localize("last_updated", "Last updated")), 4, row);
				T.add(getHeader(localize("totals", "Balance")), 5, row);
				row++;
				AccountEntry A;
				float total = 0;
				for (Iterator iter = accountEntries.iterator(); iter.hasNext();) {
					A = (AccountEntry) iter.next();
					
					T.add(getText(A.getName()), 1, row);
					if (A.getInfo() != null)
						T.add(getText(A.getInfo()), 2, row);
					
					T.add(getText(new IWTimestamp(A.getPaymentDate()).getLocaleDate(iwc)), 3, row);
					T.add(getText(new IWTimestamp(A.getLastUpdated()).getLocaleDate(iwc)), 4, row);
					T.add(getAmountText((A.getTotal())), 5, row);
					total += A.getTotal();
					row++;
				}
				T.add(getAmountText((total)), 5, row);
				T.setWidth(T.HUNDRED_PERCENT);
				T.setCellpadding(2);
				T.setCellspacing(1);
				T.setHorizontalZebraColored(getZebraColor1(), getZebraColor2());
				T.setRowColor(1,getHeaderColor());
				T.setColumnAlignment(5, "right");
				row++;
			} 
		
		return T;
	}
	private PresentationObject doMainTable(IWContext iwc) {
	
		DataTable T = getDataTable();
		T.setUseBottom(false);
		int iAccountCount = 0;
		if (handler != null) {
			try {
				iAccountCount = getFinanceService().getAccountHome().countByTypeAndCategory(handler.getAccountType(),
						getFinanceCategoryId());
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (IDOException e) {
				e.printStackTrace();
			}
		}
		int row = 1;
		T.add(getHeader(localize("number_of_accounts", "Number of accounts")), 1, row);
		T.add(getText(String.valueOf(iAccountCount)), 2, row);
		row++;
		DateInput di = new DateInput("pay_date", true);
		di.setStyleAttribute("type", Edit.styleAttribute);
		IWTimestamp today = IWTimestamp.RightNow();
		di.setYearRange(today.getYear() - 2, today.getYear() + 3);
		today.addMonths(1);
		di.setDate(new IWTimestamp(1, today.getMonth(), today.getYear()).getSQLDate());
		DateInput start = new DateInput("start_date", true);
		start.setStyleAttribute("type", Edit.styleAttribute);
		IWTimestamp today1 = IWTimestamp.RightNow();
		start.setYearRange(today1.getYear() - 2, today1.getYear() + 3);
		today1.addMonths(1);
		start.setDate(new IWTimestamp(1, today.getMonth(), today.getYear()).getSQLDate());
		DateInput end = new DateInput("end_date", true);
		end.setStyleAttribute("type", Edit.styleAttribute);
		IWTimestamp today2 = IWTimestamp.RightNow();
		end.setYearRange(today2.getYear() - 2, today2.getYear() + 3);
		today2.addMonths(1);
		IWCalendar cal = new IWCalendar();
		int day = cal.getLengthOfMonth(today2.getMonth(), today2.getYear());
		end.setDate(new IWTimestamp(day, today2.getMonth(), today2.getYear()).getSQLDate());
		TextInput rn = new TextInput("round_name");
		Edit.setStyle(rn);
		SubmitButton sb = new SubmitButton("commit", localize("commit", "Commit"));
		Edit.setStyle(sb);
		Collection keys = null;
		try {
			keys = getFinanceService().getAccountKeyHome().findByCategory(getFinanceCategoryId());
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		DropdownMenu drpAccountKeys = drpAccountKeys(keys, "account_key_id");
		Edit.setStyle(drpAccountKeys);
		T.add(getHeader(localize("date_of_payment", "Date of payment")), 1, row);
		T.add(di, 2, row);
		row++;
		T.add(getHeader(localize("start_date", "Start date")), 1, row);
		T.add(start, 2, row);
		row++;
		T.add(getText(localize("end_date", "End date")), 1, row);
		T.add(end, 2, row);
		row++;
		T.add(getHeader(localize("account_key", "Account key")), 1, row);
		T.add(drpAccountKeys, 2, row);
		row++;
		T.add(getHeader(localize("name_of_round", "Assessment name")), 1, row);
		T.add(rn, 2, row);
		row++;
		T.add(sb, 2, row);
		row++;
		try {
			T.add(getHeader(localize("excess_round","Excess round")),1,row);
			DropdownMenu excessRounds = new DropdownMenu("excess_round_id");
			Collection rounds = getFinanceService().getAssessmentRoundHome().findByCategoryAndTariffGroup(getFinanceCategoryId(),groupID,this.fromDate,this.toDate,null,-1,-1);
			excessRounds.addMenuElement(-1,localize("old_batches","Old batches"));
			DateFormat df = getShortDateFormat(iwc.getCurrentLocale());
			for (Iterator iter = rounds.iterator(); iter.hasNext();) {
				AssessmentRound round = (AssessmentRound) iter.next();
				Date from = round.getPeriodFromDate();
				Date to = round.getPeriodToDate();
				String period = "";
				if(from!=null && to!=null)
					period = " ( "+df.format(round.getPeriodFromDate())+" - "+df.format(round.getPeriodToDate())+")";
				excessRounds.addMenuElement(round.getPrimaryKey().toString(),round.getName()+period);
			}
			T.add(excessRounds,2,row++);
		} catch (RemoteException e1) {
			e1.printStackTrace();
		} catch (EJBException e1) {
			e1.printStackTrace();
		} catch (FinderException e1) {
			e1.printStackTrace();
		}
		
		
		//sb.setOnClick("this.disabled = true");
		//status.setMessageCaller(sb, localize("assessment_time", "Assessment takes time"));
		sb.setOnClick("this.form.submit()");
		BusyBar bb = new BusyBar("busyguy");
		bb.setInterfaceObject(sb);
		T.add(bb, 2, row);
		row++;
		
		T.setWidth("100%");
		T.add(new HiddenInput(this.PRM_ACTION, String.valueOf(this.ACT3)));
		T.add(new HiddenInput(PRM_GROUP_ID, String.valueOf(groupID)));
		return T;
	}

	private DropdownMenu drpAccountKeys(Collection AK, String name) {
		DropdownMenu drp = new DropdownMenu(name);
		drp.addMenuElement(-1, "--");
		if (AK != null) {
			drp.addMenuElements(AK);
		}
		return drp;
	}
	private Link getRoundLink(String name, Integer id, Integer iGroupId) {
		Link L = new Link(name);
		L.addParameter(PRM_ACTION, ACT4);
		L.addParameter(PRM_ROUND_ID, id.toString());
		L.addParameter(PRM_GROUP_ID, iGroupId.toString());
		L.setFontSize(Edit.textFontSize);
		return L;
	}
	
	private String getLocalizedAssessmentStatus(String status){
		return localize("assessmentstatus."+status,status);
	}
	
	public void main(IWContext iwc) {
		/*if (status == null)
			status = new StatusBar("ass_status");
		status
				.setStyle("color: #ff0000;  font-style: normal; font-family: verdana; font-weight: normal; font-size:14px;");
				*/
		//isStaff = com.idega.core.accesscontrol.business.AccessControl
		isAdmin = iwc.hasEditPermission(this);
		control(iwc);
	}
	public Object clone() {
		TariffAssessments obj = null;
		try {
			obj = (TariffAssessments) super.clone();
		} catch (Exception ex) {
			ex.printStackTrace(System.err);
		}
		return obj;
	}
}
