package is.idega.idegaweb.member;

import com.idega.block.cal.presentation.AttendantChooser;
import com.idega.block.cal.presentation.CalPropertyWindow;
import com.idega.block.staff.presentation.StaffApplication;
import com.idega.development.presentation.DeveloperHomepageGenerator;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;
import com.idega.repository.data.ImplementorRepository;
import com.idega.user.app.UserApplication;
import com.idega.user.block.homepage.presentation.HomePageGenerator;
import com.idega.user.presentation.GroupChooser;
import com.idega.user.presentation.UserPropertyWindow;
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
		// changing constants, setting the right help bundle
		ICUserConstants.HELP_BUNDLE_IDENTFIER = "is.idega.idegaweb.member.isi";
	}
	
	public void stop(IWBundle starterBundle) {
		// nothing to do
	}
}
