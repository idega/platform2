//idega 2000 - Tryggvi Larusson
/*
*Copyright 2000 idega.is All Rights Reserved.
*/

package is.idega.idegaweb.golf.templates;

import java.sql.SQLException;

import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import is.idega.idegaweb.golf.entity.Member;



/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.0
*/
public abstract class TournamentAdmin extends JmoduleWindowModule{

  private final static String IW_BUNDLE_IDENTIFIER="com.idega.idegaweb.golf";

	public void _main(IWContext modinfo)throws Exception{
	  super._main(modinfo);
	  getPage().setTitle("Mótastjóri");
	}

	public Member getMember(){
		return (Member)getModuleInfo().getSession().getAttribute("member_login");
	}



        public boolean isAdmin() {

          try{
            return com.idega.jmodule.login.business.AccessControl.isAdmin(getModuleInfo());
          }catch (SQLException E) {
            /*
            out.print("SQLException: " + E.getMessage());
            out.print("SQLState:     " + E.getSQLState());
            out.print("VendorError:  " + E.getErrorCode());*/
          }catch (Exception E) {
		E.printStackTrace();
	  }finally {
	  }
          return false;
        }



        public boolean isDeveloper() {
            return com.idega.jmodule.login.business.AccessControl.isDeveloper(getModuleInfo());
       }

        public boolean isClubAdmin() {
            return com.idega.jmodule.login.business.AccessControl.isClubAdmin(getModuleInfo());
        }

        public boolean isUser() {
            return com.idega.jmodule.login.business.AccessControl.isUser(getModuleInfo());
        }

  public IWResourceBundle getResourceBundle(){
     return getResourceBundle(getModuleInfo());
  }

  public IWBundle getBundle(){
    return getBundle(getModuleInfo());
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

}  // class
