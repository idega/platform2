package se.idega.idegaweb.commune.presentation;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Text;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
/**
 * @author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */
public class ManagerView extends CommuneBlock {
	private final static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune";
	private final static int ACTION_VIEW_MANAGER = 1;
	
	final static String PARAM_MANAGER_ID = "USC_MANAGER_ID";
	private Table mainTable = null;
	private int manager_id = -1;
	public ManagerView() {
	}
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	public void main(IWContext iwc) {
		this.setResourceBundle(getResourceBundle(iwc));
		try {
			int action = parseAction(iwc);
			switch (action) {
				case ACTION_VIEW_MANAGER :
					viewManagerInfo(iwc);
				default :
					break;
			}
			super.add(mainTable);
		} catch (Exception e) {
			super.add(new ExceptionWrapper(e, this));
		}
	}
	public void add(PresentationObject po) {
		if (mainTable == null) {
			mainTable = new Table(2,1);
			mainTable.setCellpadding(14);
			mainTable.setCellspacing(0);
			//mainTable.setColor(getBackgroundColor());
			mainTable.setWidth(400);
			mainTable.setHeight(500);
		}
		mainTable.add(po);
	}
	private int parseAction(IWContext iwc) {
		int action = ACTION_VIEW_MANAGER;
		return action;
	}
	private void viewManagerInfo(IWContext iwc) throws Exception {
		//add(getLocalizedHeader("managerview.my_cases", "My cases"));
		IWResourceBundle iwrb = this.getResourceBundle(iwc);
		add(new Break(2));
		User manager = null;
			boolean managerSelected= false;
			try{
				manager = getSelectedManager(iwc);	
				managerSelected=true;
			}
			catch(Exception e){
			}
			if (managerSelected) {
				
				
				
				Table leftTable = new Table(1,7);
				mainTable.add(leftTable,1,1);
				
				int userImageID = manager.getSystemImageID();
				PresentationObject picture=null;
				
				if(userImageID==-1){
					Table fakeImageTable = new Table(1,1);
					fakeImageTable.setAlignment(1,1,Table.HORIZONTAL_ALIGN_CENTER);
					fakeImageTable.setBorder(4);
					fakeImageTable.setHeight(140);
					fakeImageTable.setWidth(100);
					String fakeImageColor = "#CCCCCC";
					Text photoText = new Text(iwrb.getLocalizedString("managerview.photo_text","Photo"));
					photoText.setBold();
					photoText.setFontColor(fakeImageColor);
					photoText.setFontSize(Text.FONT_SIZE_12_STYLE_TAG);
					fakeImageTable.add(photoText,1,1);
					fakeImageTable.setBorderColor(fakeImageColor);
					picture = fakeImageTable;
				}
				else{
					Image image = new Image();
					image.setImageID(userImageID);
					image.setWidth(100);
					picture=image;
				}
				mainTable.add(picture,2,1);
				
				String sManagerName = manager.getNameLastFirst();
				Text tManagerName = getSmallText(sManagerName);
				leftTable.add(tManagerName,1,1);
				
				String sWorkGroup = getWorkGroupName(manager,iwc);
				Text tWorkGroup = getSmallText(sWorkGroup);
				leftTable.add(tWorkGroup,1,2);
				
				String sWorkGroupArea = "";
				Text tWorkGroupArea = getSmallText(sWorkGroupArea);
				leftTable.add(tWorkGroupArea,1,3);
				
				String sWorkGroupDescription = getWorkGroupDescription(manager,iwc);
				Text tWorkGroupDescription = getSmallText(sWorkGroupDescription);
				leftTable.add(tWorkGroupDescription,1,4);
				
				String sManagerEmail = getManagerEmail(manager,iwc);
				Text tManagerEmail = getSmallText(sManagerEmail);
				leftTable.add(tManagerEmail,1,6);
				
				String sManagerTelephone = getManagerTelephone(manager,iwc);
				Text tManagerTelephone = getSmallText(sManagerTelephone);
				leftTable.add(tManagerTelephone,1,7);
				
				
			} else {
				add(getSmallText(localize("managerview.no_manager_selected", "No manager selected")));
			}
		
		//f.addParameter(PARAM_SHOW_DELETE_INFO,"true");
	}

	/**
	 * Method getManagerTelephone.
	 * @param manager
	 * @return String
	 */
	private String getManagerTelephone(User manager,IWContext iwc) {
		return "-";
	}


	/**
	 * Method getManagerEmail.
	 * @param manager
	 * @return String
	 */
	private String getManagerEmail(User manager,IWContext iwc) {
		try{
			getUserBusiness(iwc).getUsersMainAddress(manager);
			return "-";
		}
		catch(Exception e){
			//e.printStackTrace();
		}
		return null;
	}


	/**
	 * Method getWorkGroupDescription.
	 * @param manager
	 * @return String
	 */
	private String getWorkGroupDescription(User manager,IWContext iwc) {
		try
		{
			return getWorkGroup(manager,iwc).getDescription();
		} catch (Exception e)
		{
			return "-";
		}
	}


	/**
	 * Method getWorkGroupName.
	 * @param manager
	 * @return String
	 */
	private Group getWorkGroup(User manager,IWContext iwc) {
		try
		{
			return manager.getPrimaryGroup();
		} catch (Exception e)
		{
			throw new RuntimeException("No workgroup found for manager "+manager);
		}
	}
	

	/**
	 * Method getWorkGroupName.
	 * @param manager
	 * @return String
	 */
	private String getWorkGroupName(User manager,IWContext iwc) {
		try
		{
			return getWorkGroup(manager,iwc).getName();
		} catch (Exception e)
		{
			return "-";
		}
	}


	/**
	 * Method getSelectedManager.
	 * @param iwc
	 * @return User
	 */
	private User getSelectedManager(IWContext iwc) throws Exception{
		int iManagerID = getManagerID();
		try{
			String pUserID = iwc.getParameter(PARAM_MANAGER_ID);
			iManagerID = Integer.parseInt(pUserID);
			setManager(iManagerID);
		}
		catch(Exception e){}
		if(iManagerID!=-1){
			return getUserBusiness(iwc).getUser(iManagerID);
		}
		else{
			throw new Exception("No manager selected");	
		}
	}

	private UserBusiness getUserBusiness(IWContext iwc) throws Exception {
		return (UserBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, UserBusiness.class);
	}

	private GroupBusiness getGroupBusiness(IWContext iwc) throws Exception {
		return (GroupBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, GroupBusiness.class);
	}

	public void setManager(User manager) {
		try{
			manager_id = ((Integer)manager.getPrimaryKey()).intValue();
		}
		catch(Exception e){
			System.out.println("["+this.getClass().getName()+"] Exception getting primary key from user: "+e.getMessage());	
		}
	}
	public void setManager(int user_id) {
		manager_id = user_id;
	}
	public int getManagerID() {
		return manager_id;
	}
}
