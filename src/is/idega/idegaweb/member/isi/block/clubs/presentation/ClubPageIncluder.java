/*
 * Created on Mar 28, 2004
 */
package is.idega.idegaweb.member.isi.block.clubs.presentation;

import com.idega.core.builder.data.ICPage;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Page;
import com.idega.presentation.ui.PageIncluder;


/**
 * This class simply checks the member.isi bundle for a bundle property 
 * ROOT_CLUB_ID to get the group id of the club it is getting info from.
 * 
 * @author <a href="eiki@idega.is>Eirikur Hrafnsson</a>
 */
public class ClubPageIncluder extends PageIncluder {
	
	public static String BUNDLE_PROPERTY_ROOT_CLUB_ID = "ROOT_CLUB_ID";
	public static String PARAM_ROOT_CLUB_ID = "RO_CL_ID";
	public static String PARAM_CALLING_PAGE_ID = "CL_PA_ID";
	public static String PARAM_CALLING_DOMAIN = "CL_DM";
	public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
	
	private Page parentPage;
	private String menuStyleSrc = "cssmenu/CSSMultiLevelMenu.css";
	private ICPage page;
	private String rootClubParamName = null; // id of club is sent with this parametere (if not null)
	private String _callingDomain = null;
	
	public ClubPageIncluder() {
		super();
	}
	
	public ClubPageIncluder(String URL) {
		super(URL);
	}
	
	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		
		parentPage = this.getParentPage();
		parentPage.addStyleSheetURL(iwc.getIWMainApplication().getCoreBundle().getVirtualPathWithFileNameString(menuStyleSrc));
	}
	
	protected String finalizeLocationString(String location, IWContext iwc) {
		IWBundle bundle = this.getBundle(iwc);
		String groupId = bundle.getProperty(BUNDLE_PROPERTY_ROOT_CLUB_ID);
		StringBuffer finalUrl = new StringBuffer(location);
		
		if(groupId!=null) {
			if(location.endsWith("/")){//check if the url ends with a slash
				finalUrl.append("?");
			}
			else{//no slash at end
				if( finalUrl.indexOf("?")==-1 ){//check if the url contains a ?
					if(finalUrl.indexOf("/",8)!=-1){//check if the url contains a slash
						finalUrl.append("?");
					}
					else{
						finalUrl.append("/?");
					}
				}
				else{//just add to the parameters
					finalUrl.append("&");
				}
			}
			//add the extra parameters
			//the division id
			finalUrl.append(PARAM_ROOT_CLUB_ID).append("=").append(groupId);
			if(rootClubParamName!=null) {
				finalUrl.append("&").append(rootClubParamName).append("=").append(groupId);
			}
			
			if(page!=null) {
				finalUrl.append("&").append(PARAM_CALLING_PAGE_ID).append("=").append( page.getPrimaryKey().toString());
			}
			
			if(_callingDomain!=null) {
				finalUrl.append("&").append(PARAM_CALLING_DOMAIN).append("=").append( _callingDomain );
			}
			/*else {
			 //the page the includer is currently on
			  IWApplicationContext iwac = iwc.getIWMainApplication().getIWApplicationContext();
			  BuilderService bs;
			  try {
			  bs = BuilderServiceFactory.getBuilderService(iwac);
			  finalUrl.append("&").append(PARAM_CALLING_PAGE_ID).append("=").append(bs.getCurrentPageId(iwc));
			  } catch (Exception e) {
			  e.printStackTrace();
			  }
			  }*/
		}
		String strFinalUrl = finalUrl.toString();
		System.out.println("ClubPageIncluder: finalized url is \"" + strFinalUrl + "\"");
		return strFinalUrl;
	}
	
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	
	public void setRootClubParamName(String value) {
		rootClubParamName = value;
	}
	
	public void setCallingPage(ICPage page) {
		this.page = page;
	}
	
	public void setCallingDomain(String callingDomain) {
		_callingDomain = callingDomain;
	}
}
