package se.idega.idegaweb.commune.presentation;

import com.idega.user.data.User;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Text;

/**
 * @author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */
public class CitizenPageHeader extends Text
{
	private static final String IW_BUNDLE_IDENTIFIER=CommuneBlock.IW_BUNDLE_IDENTIFIER;
	
	public void main(IWContext iwc){
	
		String userString;
		User user = iwc.getCurrentUser();
		if(user==null){
			userString = "";
		}
		else{
			userString = user.getName();	
		}
		this.setBold();
		String beginning = getResourceBundle(iwc).getLocalizedString("citizen_page_header.header","Citizen Account");
		this.setText(beginning+" - "+userString);
	}
	
	public String getBundleIdentifier(){
		return IW_BUNDLE_IDENTIFIER;
	}
	
}
