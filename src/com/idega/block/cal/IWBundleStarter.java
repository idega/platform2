package com.idega.block.cal;

import com.idega.block.cal.presentation.AttendantChooser;
import com.idega.block.cal.presentation.CalPropertyWindow;
import com.idega.block.cal.presentation.CalendarWindowPlugin;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;
import com.idega.repository.data.ImplementorRepository;
import com.idega.user.app.ToolbarElement;
import com.idega.user.presentation.GroupChooser;
import com.idega.user.presentation.UserPropertyWindow;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Aug 26, 2004
 */
public class IWBundleStarter implements IWBundleStartable {

	public void start(IWBundle starterBundle) {
		
		ImplementorRepository repository = ImplementorRepository.getInstance();
		// add implementor for the com.idega.user bundle
		repository.addImplementor(CalPropertyWindow.class, UserPropertyWindow.class);
		repository.addImplementor(AttendantChooser.class, GroupChooser.class);
		repository.addImplementor(ToolbarElement.class, CalendarWindowPlugin.class);
	}
	
	public void stop(IWBundle starterBundle) {
		// nothing to do
	}
}
