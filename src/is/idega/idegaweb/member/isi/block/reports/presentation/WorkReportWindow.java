package is.idega.idegaweb.member.isi.block.reports.presentation;

import is.idega.idegaweb.member.business.MemberUserBusiness;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.LinkContainer;
import com.idega.presentation.text.Lists;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
/**
 * This window is used to work with a clubs work reports.
 * Copyright : Idega Software 2003
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */


public class WorkReportWindow extends IWAdminWindow {

	private MemberUserBusiness memBiz;
	private GroupBusiness groupBiz;
	private UserBusiness userBiz;

	public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
	
	public static final String PARAMETER_CLUB_ID = "iwme_wr_club_id";

	protected static final String ACTION = "iwme_wr_act";

	
	protected static final String ACTION_WORK_ON_REPORT ="iwme_wr_act_b1";
	protected static final String ACTION_EDIT_MEMBER_LIST ="iwme_wr_act_b2";
	protected static final String ACTION_EDIT_ACCOUNT ="iwme_wr_act_b3";
	protected static final String ACTION_EDIT_BOARD ="iwme_wr_act_b4";
	protected static final String ACTION_UPLOAD_REPORT ="iwme_wr_act_b5"; //b6 is useless
	protected static final String ACTION_IMPORT_MEMBERS ="iwme_wr_act_b7";
	protected static final String ACTION_IMPORT_ACCOUNT ="iwme_wr_act_b8";
	protected static final String ACTION_IMPORT_BOARD ="iwme_wr_act_b9";
	protected static final String ACTION_REPORT_OVERVIEW ="iwme_wr_act_b10";
	protected static final String ACTION_CLOSE_REPORT ="iwme_wr_act_b11";
	protected static final String ACTION_STATISTICS ="iwme_wr_act_b12";
	protected static final String ACTION_CREATE_REPORTS ="iwme_wr_act_b13";
	
	private IWResourceBundle iwrb;


	public WorkReportWindow() {
		setHeight(800);
		setWidth(600);	
		setResizable(true);
	}
	
	
	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		iwrb = getResourceBundle(iwc);
		memBiz = getMemberUserBusiness(iwc);
		
		setTitle(iwrb.getLocalizedString("workreportwindow.title", "Work Reports"));
		String action = iwc.getParameter(ACTION);
			
		Table table = new Table(2,1);
		//table.mergeCells(1,2,3,1);
		table.setWidthAndHeightToHundredPercent();
		table.setColumnColor(1,this.MENU_COLOR);
		table.setColumnWidth(1,"200");
		table.setVerticalAlignment(1,1,Table.VERTICAL_ALIGN_TOP);
		table.setVerticalAlignment(2,1,Table.VERTICAL_ALIGN_TOP);
		
		table.add(getMenuTable(iwc),1,1);
		
	
			
		add(table);
		
		if(action!=null){
			
		
			if( action.equals(ACTION_WORK_ON_REPORT) ){
				table.add(new WorkReportSelector(),2,1);	
			}
			else if( action.equals(ACTION_EDIT_MEMBER_LIST) ){
				table.add(new WorkReportSelector(),2,1);	
			}
			else if( action.equals(ACTION_EDIT_ACCOUNT) ){
				table.add(new WorkReportSelector(),2,1);	
			}
			else if( action.equals(ACTION_EDIT_BOARD) ){
				table.add(new WorkReportSelector(),2,1);	
			}
			else if( action.equals(ACTION_UPLOAD_REPORT) ){
				table.add(new WorkReportSelector(),2,1);	
			}
			else if( action.equals(ACTION_IMPORT_MEMBERS) ){
				table.add(new WorkReportMemberImporter(),2,1);
			}
			else if( action.equals(ACTION_IMPORT_ACCOUNT) ){
				table.add(new WorkReportImporter(),2,1);
			}
			else if( action.equals(ACTION_IMPORT_BOARD) ){
				table.add(new WorkReportImporter(),2,1);
			}
			else if( action.equals(ACTION_REPORT_OVERVIEW) ){
				table.add(new WorkReportSelector(),2,1);	
			}
			else if( action.equals(ACTION_CLOSE_REPORT) ){
				table.add(new WorkReportSelector(),2,1);	
			}
			else if( action.equals(ACTION_STATISTICS) ){
				table.add(new WorkReportSelector(),2,1);	
			}
		
		}

	}
	
		
	private Table getMenuTable(IWContext iwc) {
	
		Table menu = new Table(1,11);
		menu.setWidth(Table.HUNDRED_PERCENT);
		
		Text operations = formatHeadline(iwrb.getLocalizedString("workreportwindow.operations","Operations"));
				
		//B.1
		LinkContainer workOnReport = new LinkContainer();
		workOnReport.add(formatText(iwrb.getLocalizedString("workreportwindow.select_report","Select report"),true));
		workOnReport.addParameter(ACTION,ACTION_WORK_ON_REPORT);
		Lists editList = new Lists();
		//B.2
		LinkContainer editMemberList = new LinkContainer();
		editMemberList.add(formatText(iwrb.getLocalizedString("workreportwindow.edit_members","Edit member list")));
		editMemberList.addParameter(ACTION,ACTION_EDIT_MEMBER_LIST);
		editList.add(editMemberList);
		//B.3
		editList.add(formatText(iwrb.getLocalizedString("workreportwindow.edit_account","Edit account info")));
		//B.4
		editList.add(formatText(iwrb.getLocalizedString("workreportwindow.edit_board","Edit board info")));
		
		//B.5		
		LinkContainer uploadReport = new LinkContainer();
		uploadReport.add(formatText(iwrb.getLocalizedString("workreportwindow.upload_report","Upload report"),true));
		uploadReport.addParameter(ACTION,ACTION_UPLOAD_REPORT);
		Lists uploadList = new Lists();
		//B.7
		LinkContainer importMemberList = new LinkContainer();
		importMemberList.add(formatText(iwrb.getLocalizedString("workreportwindow.import_members","Import member list")));
		importMemberList.addParameter(ACTION,ACTION_IMPORT_MEMBERS);
		uploadList.add(importMemberList);
		//B.8
		LinkContainer importAccount = new LinkContainer();
		importAccount.add(formatText(iwrb.getLocalizedString("workreportwindow.import_account","Import account info")));
		importAccount.addParameter(ACTION,ACTION_IMPORT_ACCOUNT);
		uploadList.add(importAccount);
		//B.9
		LinkContainer importBoard = new LinkContainer();
		importBoard.add(formatText(iwrb.getLocalizedString("workreportwindow.import_board","Import board info")));
		importBoard.addParameter(ACTION,ACTION_IMPORT_BOARD);
		uploadList.add(importBoard);
		
		//B.10
		LinkContainer reportsOverview = new LinkContainer();
		reportsOverview.add(formatText(iwrb.getLocalizedString("workreportwindow.report_overview","Reports overview"),true));
		reportsOverview.addParameter(ACTION,ACTION_REPORT_OVERVIEW);
		
		//B.11
		LinkContainer closeReport = new LinkContainer();
		closeReport.add(formatText(iwrb.getLocalizedString("workreportwindow.close_report","Close report"),true));
		reportsOverview.addParameter(ACTION,ACTION_CLOSE_REPORT);
		
		//B.12
		Text statistics = formatHeadline(iwrb.getLocalizedString("workreportwindow.statistics","Statistics"));
		
		Lists statsList = new Lists();
		statsList.add(formatText(iwrb.getLocalizedString("workreportwindow.leagues","Leagues")));
		statsList.add(formatText(iwrb.getLocalizedString("workreportwindow.regional_unions","Regional unions")));
		statsList.add(formatText(iwrb.getLocalizedString("workreportwindow.clubs","Clubs")));
		statsList.add(formatText(iwrb.getLocalizedString("workreportwindow.accounts","Accounts")));
		statsList.add(formatText(iwrb.getLocalizedString("workreportwindow.reports_list","Reports list")));
		statsList.add(formatText(iwrb.getLocalizedString("workreportwindow.misc","Misc")));		
		
		//B.13
		LinkContainer createReports = new LinkContainer();
		createReports.add(formatText(iwrb.getLocalizedString("workreportwindow.create_reports","Create reports"),true));
		createReports.addParameter(ACTION,ACTION_CREATE_REPORTS);
		
		//add to window
		menu.add(operations,1,1);
		menu.add(createReports,1,2);
		menu.add(uploadReport,1,3);
		menu.add(uploadList,1,4);
		menu.add(workOnReport,1,5);
		menu.add(editList,1,6);
		menu.add(reportsOverview,1,7);
		menu.add(closeReport,1,8);
		menu.add(statistics,1,10);
		menu.add(statsList,1,11);
		
		
		return menu;
			
	}
	
	public MemberUserBusiness getMemberUserBusiness(IWApplicationContext iwc) {
		if (memBiz == null) {
			try {
				memBiz = (MemberUserBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, MemberUserBusiness.class);
			}
			catch (java.rmi.RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return memBiz;
	}
	
	public String getBundleIdentifier(){
		return this.IW_BUNDLE_IDENTIFIER;
	}
	

	
}
