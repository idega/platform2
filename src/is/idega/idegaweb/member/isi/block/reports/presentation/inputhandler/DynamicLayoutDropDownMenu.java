package is.idega.idegaweb.member.isi.block.reports.presentation.inputhandler;

import java.sql.SQLException;
import java.util.Iterator;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.core.file.data.ICFile;
import com.idega.core.file.data.ICFileHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.DropDownMenuInputHandler;

/**
 * <p>
 * DropDownMenu so it users can have a range of JasperReportLayouts to select from.
 * </p>
 *  Last modified: $Date: 2005/11/26 17:10:18 $ by $Author: sigtryggur $
 * 
 * @author <a href="mailto:sigtryggur@idega.com">sigtryggur</a>
 * @version $Revision: 1.1.2.1 $
 */
public class DynamicLayoutDropDownMenu extends DropDownMenuInputHandler {


	protected static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
	protected static final String LAYOUT_FOLDER_NAME = "layout";
	private ICFileHome fileHome;
	
	public DynamicLayoutDropDownMenu() {
		super();
	}

    public PresentationObject getHandlerObject(String name, String value, IWContext iwc) {
    	IWResourceBundle resourceBundle = getResourceBundle(iwc);
    	this.setName(name);
    	String defaultLayout = resourceBundle.getLocalizedString("DynamicLayoutDropDownMenu.default_layout","Default layout");
		this.addMenuElement("-1", defaultLayout);
		ICFile layoutFolder = lookUpLayoutFolder(value);
		if(layoutFolder!=null){
			Iterator iterator = layoutFolder.getChildrenIterator();
			if (iterator != null) {
				while (iterator.hasNext())	{
					ICFile file = (ICFile) iterator.next();
					if (!file.isFolder()) {
						String display = file.getName();
						int fileID = file.getNodeID();
						String fileIDValue = Integer.toString(fileID);
						this.addMenuElement(fileIDValue, display);
					}
				}
  			}
		}
		return this;
    }

	public String getDisplayForResultingObject(Object value, IWContext iwc) {
		if (value != null) {
			if (value.equals("-1")) {
				IWResourceBundle resourceBundle = getResourceBundle(iwc);
				return resourceBundle.getLocalizedString("DynamicLayoutDropDownMenu.default_layout","Default layout");
			}
			try {
	    		ICFile file = getFileHome().findByPrimaryKey(value);
	    		return file.getName();	
	    	}
	    	catch (FinderException e) {
	    		return value.toString();
	    	}
		} else {
			return "";
		}
	}

	private ICFile lookUpLayoutFolder(String userDefinedLayoutFolderName)  {
		ICFile layoutFolder = null;
		try{
			try {
	    		layoutFolder = getFileHome().findByFileName(LAYOUT_FOLDER_NAME);
			}
			catch (FinderException findEx) {	
	    		layoutFolder = createAndReturnLayoutFolder(LAYOUT_FOLDER_NAME);
	    	}
			if (userDefinedLayoutFolderName != null && !userDefinedLayoutFolderName.equals("")) {
				ICFile userDefinedLayoutFolder = null;
				try {
					userDefinedLayoutFolder = getFileHome().findByFileName(userDefinedLayoutFolderName);
				}
				catch (FinderException findEx) {	
					userDefinedLayoutFolder = createAndReturnLayoutFolder(userDefinedLayoutFolderName);
					layoutFolder.addChild(userDefinedLayoutFolder);
		    	}
				return userDefinedLayoutFolder;
			}
			return layoutFolder;
		}
		catch (CreateException createEx) {
			logError("[ReportQueryOverview] Could create file");
			log(createEx);
		}
		catch (SQLException sqlEx) {
			logError("[ReportQueryOverview] Could create child file");
			log(sqlEx);
		}
    	return null;
	}

	private ICFile createAndReturnLayoutFolder(String name) throws CreateException {
		ICFile layoutFolder = getFileHome().create();
		layoutFolder.setName(name);
		layoutFolder.setMimeType(com.idega.core.file.data.ICMimeTypeBMPBean.IC_MIME_TYPE_FOLDER);
		layoutFolder.store();
		return layoutFolder;
	}

	private ICFileHome getFileHome(){
	    if (fileHome==null) {
	    	try{
	    		fileHome = (ICFileHome)IDOLookup.getHome(ICFile.class);
	    	}
		    catch (IDOLookupException lookupEx) {
		    	logError("[ReportQueryOverview] Could not look up home of ICFile");
		    	log(lookupEx);
		    }
	    }
	    return fileHome;
	  }

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
}