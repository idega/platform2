package is.idega.idegaweb.member.presentation;

import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.IntegerInput;
import com.idega.user.data.Group;
import com.idega.user.presentation.UserGroupTab;

/**
 *@author     <a href="mailto:thomas@idega.is">Thomas Hilbig</a>
 *@version    1.0
 */
public class GroupAgeGenderTab extends UserGroupTab {
  
  private Text genderText;
  private Text lowerAgeLimitText;
  private Text upperAgeLimitText;
  private Group group;
  
  public GroupAgeGenderTab(Group group) {
    this.group = group;
  }
    
	/**
	 * @see com.idega.user.presentation.UserGroupTab#initializeFieldNames()
	 */
	public void initializeFieldNames() {
	}
	/**
	 * @see com.idega.user.presentation.UserGroupTab#initializeFieldValues()
	 */
	public void initializeFieldValues() {
	}
	/**
	 * @see com.idega.user.presentation.UserGroupTab#updateFieldsDisplayStatus()
	 */
	public void updateFieldsDisplayStatus() {
	}
	/**
	 * @see com.idega.user.presentation.UserGroupTab#initializeFields()
	 */
	public void initializeFields() {
	}
	/**
	 * @see com.idega.user.presentation.UserGroupTab#initializeTexts()
	 */
	public void initializeTexts() {
    genderText = new Text("Gender:");
    lowerAgeLimitText = new Text("Lower age limit:");
    upperAgeLimitText = new Text("Upper age limit:");
	}
	/**
	 * @see com.idega.user.presentation.UserGroupTab#lineUpFields()
	 */
	public void lineUpFields() {
	}
	/**
	 * @see com.idega.util.datastructures.Collectable#collect(com.idega.presentation.IWContext)
	 */
	public boolean collect(IWContext iwc) {
    initFieldContents();
		return true;
	}
	/**
	 * @see com.idega.util.datastructures.Collectable#store(com.idega.presentation.IWContext)
	 */
	public boolean store(IWContext iwc) {
		return true;
	}
	/**
	 * @see com.idega.user.presentation.UserGroupTab#initFieldContents()
	 */
	public void initFieldContents() {
    this.empty();
    
    Table table = new Table(2,3);
    table.add(genderText, 1, 1);
    table.add(lowerAgeLimitText,1,2);
    //String integerErrorWarning = getLocalizedString("input_must_be_an_integer", "The input must be an integer", iwuc);
    String integerErrorWarning = "The input must be an integer";
    IntegerInput input = new IntegerInput("hello", 1, integerErrorWarning);
    // set size and maxLength
    input.setSize(2);
    input.setMaxlength(2);
    table.add(input, 1,3);
    // set size and maxLength
    add(table);
  }
}

