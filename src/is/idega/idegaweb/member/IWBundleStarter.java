package is.idega.idegaweb.member;

import is.idega.block.nationalregister.business.NationalRegisterFileImportHandler;
import is.idega.block.nationalregister.data.NationalRegisterImportFile;
import is.idega.idegaweb.member.block.importer.business.PinLookupToGroupImportHandler;
import is.idega.idegaweb.member.isi.block.accounting.presentation.CashierWindow;
import is.idega.idegaweb.member.isi.block.reports.presentation.WorkReportWindow;
import is.idega.idegaweb.member.presentation.ClubMemberExchangeWindow;
import is.idega.idegaweb.member.presentation.UpdateClubDivisionTemplate;
import is.idega.idegaweb.member.presentation.UserFamilyTab;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;
import com.idega.repository.data.ImplementorRepository;
import com.idega.user.handler.UserNationalRegisterFileImportHandler;
import com.idega.user.handler.UserNationalRegisterImportFile;
import com.idega.user.handler.UserPinLookupToGroupImportHandler;
import com.idega.user.presentation.FamilyTab;
import com.idega.user.presentation.UserCashierWindow;
import com.idega.user.presentation.UserClubMemberExchangeWindow;
import com.idega.user.presentation.UserUpdateClubDivisionTemplate;
import com.idega.user.presentation.UserWorkReportWindow;
import com.idega.user.util.ICUserConstants;

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
		// changing constants, setting the right help bundle in the com.idega.user bundle
		ICUserConstants.HELP_BUNDLE_IDENTFIER = "is.idega.idegaweb.member.isi";
		// add implementors for the com.idega.user bundle
		ImplementorRepository repository =  ImplementorRepository.getInstance();
		repository.addImplementor(FamilyTab.class, UserFamilyTab.class);
		repository.addImplementor(UserPinLookupToGroupImportHandler.class,PinLookupToGroupImportHandler.class);
		repository.addImplementor(UserUpdateClubDivisionTemplate.class, UpdateClubDivisionTemplate.class);
		repository.addImplementor(UserCashierWindow.class, CashierWindow.class);
		repository.addImplementor(UserClubMemberExchangeWindow.class, ClubMemberExchangeWindow.class);
		repository.addImplementor(UserNationalRegisterImportFile.class,NationalRegisterImportFile.class);
		repository.addImplementor(UserNationalRegisterFileImportHandler.class, NationalRegisterFileImportHandler.class);
		repository.addImplementor(UserWorkReportWindow.class, WorkReportWindow.class);
	}
	
	public void stop(IWBundle starterBundle) {
		// nothing to do
	}
}
