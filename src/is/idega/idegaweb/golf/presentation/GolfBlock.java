/*
 * Created on 4.3.2004
 */
package is.idega.idegaweb.golf.presentation;

import is.idega.idegaweb.golf.block.login.business.AccessControl;
import is.idega.idegaweb.golf.entity.Member;

import java.sql.Connection;
import java.sql.SQLException;

import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.util.database.ConnectionBroker;

/**
 * @author laddi
 */
public abstract class GolfBlock extends Block {

  private final static String IW_BUNDLE_IDENTIFIER="com.idega.idegaweb.golf";
  
  private IWResourceBundle _iwrb;
  private IWBundle _iwb;
  
  private boolean _isAdmin = false;
  private boolean _isClubWorker = false;
  private boolean _isClubAdmin = false;
  private boolean _isDeveloper = false;
  private boolean _isUser = false;
  
  private Member _member;

	public void _main(IWContext modinfo) throws Exception {
		_iwrb = getResourceBundle(modinfo);
		_iwb = getBundle(modinfo);
		
		_isAdmin = isAdmin(modinfo);
		_isClubWorker = isClubWorker(modinfo);
		_isClubAdmin = isClubAdmin(modinfo);
		_isDeveloper = isDeveloper(modinfo);
		_isUser = isUser(modinfo);
		
		_member = getMember(modinfo);

		super._main(modinfo);
	}
	
  /**
   * @return The default DatabaseConnection
   */
  public Connection getConnection() {
    return ConnectionBroker.getConnection();
  }

  public void freeConnection(Connection conn) {
    ConnectionBroker.freeConnection(conn);
  }

  public IWResourceBundle getResourceBundle() {
		return _iwrb;
	}
	
	public IWBundle getBundle() {
		return _iwb;
	}
	
	public abstract void main(IWContext modinfo) throws Exception;
  
  public Member getMember(IWContext modinfo){
		return (Member) modinfo.getSessionAttribute("member_login");
	}

  private boolean isAdmin(IWContext modinfo) {
    try {
      return AccessControl.isAdmin(modinfo);
    }
    catch(SQLException E) {
    	return false;
    }
  }

  private boolean isClubAdmin(IWContext modinfo) {
    return AccessControl.isClubAdmin(modinfo);
  }

  private boolean isClubWorker(IWContext modinfo) {
    try {
      return AccessControl.isClubWorker(modinfo);
    }
    catch(java.sql.SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public String getUnionID(IWContext modinfo){
    return (String)modinfo.getSessionAttribute("golf_union_id");
  }

  public void removeUnionIdSessionAttribute(IWContext modinfo){
    modinfo.removeSessionAttribute("golf_union_id");
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }
	/**
	 * @return Returns the isAdmin.
	 */
	public boolean isAdmin() {
		return this._isAdmin;
	}
	/**
	 * @return Returns the isClubAdmin.
	 */
	public boolean isClubAdmin() {
		return this._isClubAdmin;
	}
	/**
	 * @return Returns the isClubWorker.
	 */
	public boolean isClubWorker() {
		return this._isClubWorker;
	}
	/**
	 * @return Returns the isDeveloper.
	 */
	public boolean isDeveloper() {
		return this._isDeveloper;
	}
	/**
	 * @return Returns the isUser.
	 */
	public boolean isUser() {
		return this._isUser;
	}
	/**
	 * @return Returns the member.
	 */
	public Member getMember() {
		return this._member;
	}
}