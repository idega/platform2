package se.idega.idegaweb.commune.presentation;
import com.idega.business.IBOLookup;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.user.data.User;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObjectContainer;
import com.idega.user.business.UserBusiness;
/**
 * @author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */
public class CitizenAddressInfo extends PresentationObjectContainer
{
	public CitizenAddressInfo()
	{
		//super(2);
	}
	private static final String IW_BUNDLE_IDENTIFIER = CommuneBlock.IW_BUNDLE_IDENTIFIER;
	public void main(IWContext iwc)
	{
		ColumnList columnList = new ColumnList(2);
		add(columnList);
		IWResourceBundle iwrb = this.getResourceBundle(iwc);
		String addressString = null;
		String postalCodeString = null;
		String addressHeader = iwrb.getLocalizedString("citizen_addr_info.addr_header","Delivery address");
		String postalCodeHeader = iwrb.getLocalizedString("citizen_addr_info.postal_code_header","Postal code and area");
		User user = iwc.getCurrentUser();
		if (user == null)
		{
			addressString = "-";
			postalCodeString = "-";
			//System.out.println("User==null");
		}
		else
		{
			//System.out.println("User!=null");
			try
			{
				//int userID = ((Integer) user.getPrimaryKey()).intValue();
				Address addr = getUserBusiness(iwc).getUsersMainAddress(user);
				PostalCode code = null;
				if (addr != null)
				{
					addressString = addr.getStreetAddress();
					code = addr.getPostalCode();
				}
				else
				{
					addressString = "-";
				}
				if (code != null)
				{
					postalCodeString = code.getPostalAddress();
				}
				else
				{
					postalCodeString = "-";
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		columnList.setHeader(columnList.getSmallText(addressHeader),1);
		columnList.setHeader(columnList.getSmallText(postalCodeHeader),2);
		
		columnList.add(columnList.getSmallText(addressString));
		columnList.add(columnList.getSmallText(postalCodeString));

	}
	public String getBundleIdentifier()
	{
		return IW_BUNDLE_IDENTIFIER;
	}
	public UserBusiness getUserBusiness(IWContext iwc) throws Exception
	{
		return (UserBusiness) IBOLookup.getServiceInstance(iwc, UserBusiness.class);
	}
}
