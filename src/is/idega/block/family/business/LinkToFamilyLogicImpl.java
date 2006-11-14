/*
 * Created on Nov 13, 2006
 */
package is.idega.block.family.business;


import java.util.Collection;
import com.idega.presentation.IWContext;
import com.idega.user.data.User;
import com.idega.user.presentation.LinkToFamilyLogic;


public class LinkToFamilyLogicImpl implements LinkToFamilyLogic {

	public Collection getCustodiansFor(User user, IWContext iwc) {
		Collection custodians = null;
		FamilyLogic familyLogic = null;
		try {
			familyLogic = (FamilyLogic) com.idega.business.IBOLookup.getServiceInstance(iwc, FamilyLogic.class);
			custodians = familyLogic.getCustodiansFor(user);
		}
		catch (NoCustodianFound e) {
			//No custodian found for user. Ignoring exception 
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return custodians;
	}
}
