/*
 * Created on Mar 28, 2004
*/
package is.idega.idegaweb.member.isi.block.clubs.presentation;

import com.idega.idegaweb.IWBundle;
import com.idega.presentation.IWContext;
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
	public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
    
    public ClubPageIncluder() {
        super();
    }

    public ClubPageIncluder(String URL) {
        super(URL);
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
            finalUrl.append(PARAM_ROOT_CLUB_ID).append("=").append(groupId);
            
        }
        
        return finalUrl.toString();
    }
    
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

}
