package is.idega.idegaweb.member.isi;

import is.idega.idegaweb.member.isi.block.accounting.presentation.CashierWindow;
import is.idega.idegaweb.member.isi.block.reports.presentation.WorkReportWindowPlugin;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;
import com.idega.repository.data.ImplementorRepository;
import com.idega.user.app.ToolbarElement;
import com.idega.user.plugin.UserCashierWindow;
import com.idega.user.presentation.UserConstants;
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
		UserConstants.HELP_BUNDLE_IDENTFIER = "is.idega.idegaweb.member.isi";
		// changing constants, setting the right help bundle in the com.idega.core.bundle
		ICUserConstants.HELP_BUNDLE_IDENTFIER = "is.idega.idegaweb.member.isi";
		// add implementors for the com.idega.user bundle
		ImplementorRepository repository =  ImplementorRepository.getInstance();
		repository.addImplementor(UserCashierWindow.class, CashierWindow.class);
		repository.addImplementor(ToolbarElement.class, WorkReportWindowPlugin.class);
	}
	
	public void stop(IWBundle starterBundle) {
		// nothing to do
	}
}
