package is.idega.block.nationalregister;

import is.idega.block.nationalregister.business.NationalRegisterFileImportHandler;
import is.idega.block.nationalregister.data.NationalRegisterImportFile;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;
import com.idega.repository.data.ImplementorRepository;
import com.idega.user.handler.UserNationalRegisterFileImportHandler;
import com.idega.user.handler.UserNationalRegisterImportFile;

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
		repository.addImplementor(UserNationalRegisterImportFile.class,NationalRegisterImportFile.class);
		repository.addImplementor(UserNationalRegisterFileImportHandler.class, NationalRegisterFileImportHandler.class);
	}
	
	public void stop(IWBundle starterBundle) {
		// nothing to do
	}
}
