package is.idega.idegaweb.member.presentation;

import is.idega.idegaweb.member.business.GroupApplicationBusiness;

import java.rmi.RemoteException;

import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.PostalCodeDropdownMenu;
import com.idega.user.data.Group;
import com.idega.user.presentation.GroupSelectionDoubleBox;

/**
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 *
 */
public class GroupApplicationFormHandler extends Block {

	public static final String USER_NAME_PARAM = "user_name";
	public static final String PIN_PARAM = "pin";
	public static final String PHONE_PARAM = "phone";
	public static final String PHONE2_PARAM = "phone2";
	public static final String EMAIL_PARAM = "email";
	public static final String EMAIL2_PARAM = "email2";
	
	public static final String ADDRESS_PARAM = "address";	
	public static final String COMMENT_PARAM = "comment";
	public static final String ADMIN_COMMENT_PARAM = "admin_comment";
	
	public static final String GROUPS_PARAM = GroupSelectionDoubleBox.selectedGroupsParameterDefaultValue;//hack!
	public static final String POSTAL_CODE_PARAM = PostalCodeDropdownMenu.IW_POSTAL_CODE_MENU_PARAM_NAME;//hack!
	
	public static final String GENDER_PARAM = "gender";
	

	private Group applicationGroup = null;
	
		
	public GroupApplicationFormHandler(){
		super();	
	}



	/**
	 * @see com.idega.presentation.PresentationObject#main(IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
	
		if( applicationGroup!=null ){
			if( iwc.isParameterSet(USER_NAME_PARAM) && iwc.isParameterSet(PIN_PARAM) ){
				
				GroupApplicationBusiness biz = this.getGroupApplicationBusiness(iwc);

				
				String name = iwc.getParameter(USER_NAME_PARAM);
				String pin = iwc.getParameter(PIN_PARAM);
				String gender = iwc.getParameter(GENDER_PARAM);
				String email = iwc.getParameter(EMAIL_PARAM);
				String email2 = iwc.getParameter(EMAIL2_PARAM);
				String address = iwc.getParameter(ADDRESS_PARAM);
				String postal = iwc.getParameter(POSTAL_CODE_PARAM);
				String phone = iwc.getParameter(PHONE_PARAM);
				String phone2 = iwc.getParameter(PHONE2_PARAM);
				String comment = iwc.getParameter(COMMENT_PARAM);
				String adminComment = iwc.getParameter(ADMIN_COMMENT_PARAM);
				
				//KR hack
				if(adminComment==null){
					String paymentType = iwc.getParameter("payment_type");
					String validMonth = iwc.getParameter("valid_month");
					String validYear = iwc.getParameter("valid_year");
					String nameOnCard = iwc.getParameter("name_on_credit_card");
					String pinOnCard = iwc.getParameter("credit_card_pin");
					String caretakerName = iwc.getParameter("caretaker_name");
					String caretakerPin = iwc.getParameter("caretaker_pin");
					String caretakerEmail = iwc.getParameter("caretaker_email");	
					String cardNumber = iwc.getParameter("credit_card_number");			
					boolean credit = false;
					
					if( paymentType!=null ){
						if( paymentType.equals("C") ){
							credit = true;		
						}
						else if( paymentType.equals("M") ){
							credit = false;	
						}						
					}
					
					if( credit && cardNumber!=null ){
						adminComment = "Vill borga með kredit korti:\n"
										+"Kortanúmer : "+cardNumber+"\n"
										+"Gildir til : "+validMonth+"/"+validYear+"\n"
										+"Korthafi : "+nameOnCard+"\n"
										+"Kennitala korthafa : "+pinOnCard+"\n";						
					}
					else if( !credit ){
						adminComment = "Vill stadgreiða\n";
					}
					else {
						adminComment = "Vill borga med korti en kortanúmerið vantar!\n";	
					}
					
					if( caretakerName!=null ){
						adminComment += "Forráðamaður : "+caretakerName+"\n"
									+"Kennitala forráðamanns : "+caretakerPin+"\n"
									+"Netfang forráðamanns : "+caretakerEmail+"\n";
					}
					
					
				}

				
				String[] groups = iwc.getParameterValues(GROUPS_PARAM);
				if( groups == null ) System.err.println("GROUPS are Null!");
				
				
				try {
					biz.createGroupApplication(applicationGroup,name,pin,gender,email,email2,address,postal,phone,phone2,comment,adminComment,groups);
				
				} catch (Exception e) {
					add("Error : Application creation failed!");
					e.printStackTrace();
				}
				

			
			}
			else add("Error : No name and PIN!");
			
					
		}
		else{
			add("The application group parameter has not been set");	
		}		
			
	}
	
	public void setApplicationGroup(Group group){
		this.applicationGroup = group;	
	}
		
		
		
	private GroupApplicationBusiness getGroupApplicationBusiness(IWApplicationContext iwac) throws RemoteException {
		return (GroupApplicationBusiness) IBOLookup.getServiceInstance(iwac, GroupApplicationBusiness.class);	
	}
	

	  
	/**
	 * @see com.idega.presentation.PresentationObject#getBundleIdentifier()
	 */
	public String getBundleIdentifier() {
		return "is.idega.idegaweb.member";
	}
	
	



	/**
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		GroupApplicationFormHandler obj = (GroupApplicationFormHandler) super.clone();
		obj.applicationGroup = this.applicationGroup;
		return obj;
	}

}
