package se.idega.idegaweb.commune.presentation;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.text.Name;
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
			int action = parseAction();
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
			mainTable = new Table(3,1);
			mainTable.setCellpadding(0);
			mainTable.setCellspacing(0);
			mainTable.setWidth(2, "20");
			mainTable.setWidth(400);
			mainTable.setVerticalAlignment(1, 1, Table.VERTICAL_ALIGN_TOP);
		}
		mainTable.add(po);
	}
	private int parseAction() {
		int action = ACTION_VIEW_MANAGER;
		return action;
	}
	private void viewManagerInfo(IWContext iwc) throws Exception {
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
				mainTable.add(picture,3,1);
				
				Name name = new Name(manager.getFirstName(), manager.getMiddleName(), manager.getLastName());
				String sManagerName = name.getName(iwc.getApplicationSettings().getDefaultLocale(), true);
				Text tManagerName = getSmallHeader(sManagerName);
				leftTable.add(tManagerName,1,1);
				
				String sWorkGroup = getWorkGroupName(manager);
				Text tWorkGroup = getSmallText(sWorkGroup);
				leftTable.add(tWorkGroup,1,2);
				
				String sWorkGroupArea = "";
				Text tWorkGroupArea = getSmallText(sWorkGroupArea);
				leftTable.add(tWorkGroupArea,1,3);
				
				String sManagerDescription = getManagerDescription(manager);
				Text tManagerDescription = getSmallText(sManagerDescription);
				leftTable.add(tManagerDescription,1,4);
				
				String sManagerEmail = getManagerEmail(manager,iwc);
				Text tManagerEmail=null;
				if(sManagerEmail.equals("")){
					tManagerEmail = getSmallText(sManagerEmail);
				}
				else{
					tManagerEmail = getLink(sManagerEmail);
					Link lManagerEmail = (Link)tManagerEmail;
					lManagerEmail.setURL("mailto:"+sManagerEmail);
				}
			
				leftTable.add(tManagerEmail,1,6);
				
				String sManagerTelephone = getManagerTelephone(manager,iwc);
				Text tManagerTelephone = getSmallText(localize("managerview.tel","Tel")+": "+sManagerTelephone);
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
		try{
			int managerId = ((Integer)manager.getPrimaryKey()).intValue();
			Phone[] phones = getUserBusiness(iwc).getUserPhones(managerId);
			//Try to take the first phone
			Phone phone = phones[0];
			if(phone!=null){
				String sPhone = phone.getNumber();
				if(sPhone!=null || !sPhone.equals("")){
					return sPhone;
				}
			}
			return "-";
		}
		catch(Exception e){
			e.printStackTrace();
			return "-";
		}
	}


	/**
	 * Method getManagerEmail.
	 * @param manager
	 * @return String
	 */
	private String getManagerEmail(User manager,IWContext iwc) {
		try{
			Email email = getUserBusiness(iwc).getUserMail(manager);
			if(email!=null){
				String sEmail = email.getEmailAddress();
				if(sEmail!=null || !sEmail.equals("")){
					return sEmail;
				}
			}
			return "-";
		}
		catch(Exception e){
			e.printStackTrace();
			return "-";
		}
	}


	/* Commented out since it is never used...	 * Method getWorkGroupDescription.
	 * @param manager
	 * @return String
	private String getWorkGroupDescription(User manager,IWContext iwc) {
		try
		{	
			String s = getWorkGroup(manager,iwc).getDescription();
			if(s!=null){
				return s;
			}
		} catch (Exception e)
		{
			return "-";
		}
		return "-";
	}*/

	/**
	 * Method getWorkGroupDescription.
	 * @param manager
	 * @return String
	 */
	private String getManagerDescription(User manager) {
		try
		{	
			String s =manager.getDescription();
			if(s!=null){
				return s;
			}
		} catch (Exception e)
		{
			return "-";
		}
		return "-";
	}

	/**
	 * Method getWorkGroupName.
	 * @param manager
	 * @return String
	 */
	private Group getWorkGroup(User manager) {
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
	private String getWorkGroupName(User manager) {
		try
		{
			return getWorkGroup(manager).getName();
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

	/* Commented out since it is never used...
	private GroupBusiness getGroupBusiness(IWContext iwc) throws Exception {
		return (GroupBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, GroupBusiness.class);
	}*/

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
