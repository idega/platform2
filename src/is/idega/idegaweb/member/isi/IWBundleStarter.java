package is.idega.idegaweb.member.isi;

import is.idega.block.nationalregister.business.FamilyLogic;
import is.idega.idegaweb.member.business.MemberFamilyLogicBean;
import is.idega.idegaweb.member.isi.block.accounting.presentation.CashierWindow;
import is.idega.idegaweb.member.isi.block.reports.presentation.WorkReportWindow;
import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;
import com.idega.repository.data.ImplementorRepository;
import com.idega.user.presentation.UserCashierWindow;
import com.idega.user.presentation.UserWorkReportWindow;

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
		// services registration
		IBOLookup.registerImplementationForBean(FamilyLogic.class, MemberFamilyLogicBean.class);
		// add implementors for the com.idega.user bundle
		ImplementorRepository repository =  ImplementorRepository.getInstance();
		repository.addImplementor(UserCashierWindow.class, CashierWindow.class);
		repository.addImplementor(UserWorkReportWindow.class, WorkReportWindow.class);
	}
	
	public void stop(IWBundle starterBundle) {
		// nothing to do
	}
}
