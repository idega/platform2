package is.idega.idegaweb.member.presentation;

import com.idega.core.user.presentation.UserTab;
import com.idega.presentation.IWContext;

/**
 * @author Laddi
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class UserHistoryTab extends UserTab {

  public UserHistoryTab() {
    super();
    super.setName("History");
  }

	/**
	 * @see com.idega.core.user.presentation.UserTab#initializeFieldNames()
	 */
	public void initializeFieldNames() {
	}

	/**
	 * @see com.idega.core.user.presentation.UserTab#initializeFieldValues()
	 */
	public void initializeFieldValues() {
	}

	/**
	 * @see com.idega.core.user.presentation.UserTab#updateFieldsDisplayStatus()
	 */
	public void updateFieldsDisplayStatus() {
	}

	/**
	 * @see com.idega.core.user.presentation.UserTab#initializeFields()
	 */
	public void initializeFields() {
	}

	/**
	 * @see com.idega.core.user.presentation.UserTab#initializeTexts()
	 */
	public void initializeTexts() {
	}

	/**
	 * @see com.idega.core.user.presentation.UserTab#lineUpFields()
	 */
	public void lineUpFields() {
	}

	/**
	 * @see com.idega.util.datastructures.Collectable#collect(IWContext)
	 */
	public boolean collect(IWContext iwc) {
		return true;
	}

	/**
	 * @see com.idega.util.datastructures.Collectable#store(IWContext)
	 */
	public boolean store(IWContext iwc) {
		return true;
	}

	/**
	 * @see com.idega.core.user.presentation.UserTab#initFieldContents()
	 */
	public void initFieldContents() {
	}
	
	/**
	 * @see com.idega.presentation.PresentationObject#getBundleIdentifier()
	 */
	public String getBundleIdentifier() {
		return "is.idega.idegaweb.member";
	}
}
