package is.idega.idegaweb.member;

import is.idega.idegaweb.member.block.importer.business.PinLookupToGroupImportHandler;
import is.idega.idegaweb.member.presentation.ClubMemberExchangeWindowPlugin;
import is.idega.idegaweb.member.presentation.UpdateClubDivisionTemplate;
import is.idega.idegaweb.member.presentation.UserFamilyTab;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;
import com.idega.repository.data.ImplementorRepository;
import com.idega.user.app.ToolbarElement;
import com.idega.user.handler.plugin.UserPinLookupToGroupImportHandler;
import com.idega.user.plugin.FamilyTab;
import com.idega.user.plugin.UserUpdateClubDivisionTemplate;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Jun 10, 2004
 */
public class IWBundleStarter implements IWBundleStartable {

	public void start(IWBundle starterBundle) {
		// add implementors for the com.idega.user bundle
		ImplementorRepository repository =  ImplementorRepository.getInstance();
		repository.addImplementor(FamilyTab.class, UserFamilyTab.class);
		repository.addImplementor(UserPinLookupToGroupImportHandler.class,PinLookupToGroupImportHandler.class);
		repository.addImplementor(UserUpdateClubDivisionTemplate.class, UpdateClubDivisionTemplate.class);
		repository.addImplementor(ToolbarElement.class, ClubMemberExchangeWindowPlugin.class);
	}
	
	public void stop(IWBundle starterBundle) {
		// nothing to do
	}
}
