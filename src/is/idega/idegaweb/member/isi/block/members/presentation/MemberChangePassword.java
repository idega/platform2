package is.idega.idegaweb.member.isi.block.members.presentation;

import java.util.HashMap;
import java.util.Map;
import com.idega.core.accesscontrol.business.LoginBusinessBean;
import com.idega.core.accesscontrol.business.NotLoggedOnException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.PasswordInput;
import com.idega.presentation.ui.StyledButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.data.User;


/**
 * @author gimmi
 */
public class MemberChangePassword extends Block {
	
	User currentUser;
	IWResourceBundle iwrb;
	
	public static final String STYLENAME_BIGTEXT = "memberName";
	public static final String STYLENAME_INTERFACE_OBJECT = "interfaceObject";
	public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
	
	private static final String PARAMETER_PASS_1 = "p1";
	private static final String PARAMETER_PASS_2 = "p2";

	public void main(IWContext iwc) {
		init(iwc);
		
		Table table = new Table();
		table.setWidth("100%");
		table.setBorder(0);
		table.setCellpaddingAndCellspacing(0);
		int row = 1;
		
		String message = handleInput(iwc);
		table.setRowStyleClass(row, getStyleName(MemberOverview.STYLENAME_COLUMN_ROW));
		row = addHeader(table, row);
		row = addForm(table, row, message);
		
		add(table);
	}
	
	private void init(IWContext iwc) {
		try {
			currentUser = iwc.getCurrentUser();
		} catch (NotLoggedOnException ignore) {}
		iwrb = getResourceBundle(iwc);
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	
	private int addHeader(Table outerTable, int row) {
		Table table = getTable();
		table.add(getText(iwrb.getLocalizedString("member_logged_in_as", "Logged in as")+":"), 1, 1);
		if (currentUser != null) {
			table.add(getBigText(currentUser.getName()), 1, 2);
		} else {
			table.add(getBigText("Not logged on"), 1, 2);
		}
		outerTable.add(table, 1, row++);
		return row;
	}
	
	private int addForm(Table outerTable, int outerRow, String message) {
		Form form = new Form();
		Table table = getTable();
		table.setStyleClass(getStyleName(MemberOverview.STYLENAME_HEADING_ROW));
		
		if ( currentUser != null) {
			int row = 1;
			
			if (message != null && !"".equals(message)) {
				Text text = new Text(message);
				text.setStyleClass(getStyleName(MemberOverview.STYLENAME_HEADING));
				table.add(text, 1, row++);
				++row;
			}
			
			PasswordInput pass1 = (PasswordInput) getStyleObject(new PasswordInput(PARAMETER_PASS_1), STYLENAME_INTERFACE_OBJECT);
			PasswordInput pass2 = (PasswordInput) getStyleObject(new PasswordInput(PARAMETER_PASS_2), STYLENAME_INTERFACE_OBJECT);
			StyledButton save = new StyledButton(new SubmitButton(iwrb.getLocalizedString("member_save", "Save")));
			
			table.add(getText(iwrb.getLocalizedString("member_password_at_least_six_character_long", "Password (at least 6 characters long)")), 1, row++);
			table.add(pass1, 1, row++);
			table.add(getText(iwrb.getLocalizedString("member_password_confirm", "Confirm password")), 1, row++);
			table.add(pass2, 1, row++);
			table.add(save, 1, row++);
		}
		
		form.add(table);
		outerTable.add(form, 1, outerRow++);
		return outerRow;
	}
	
	private String handleInput(IWContext iwc) {
		String p1 = iwc.getParameter(PARAMETER_PASS_1);
		String p2 = iwc.getParameter(PARAMETER_PASS_2);
		
		if (p1 != null && p2 != null) {
			if ("".equals(p1) && "".equals(p2) ) {
				return null;
			} else if (!p1.equals(p2)) {
				return iwrb.getLocalizedString("member_passwords_must_be_the_same", "Passwords must be the same");
			} else if ( p1.length() < 6 ) {
				return iwrb.getLocalizedString("member_passwords_must_be_at_least_6_characters", "Passwords must be at least 6 characters");
			} else {
				try {
					LoginBusinessBean.changeUserPassword(currentUser, p1);
					return iwrb.getLocalizedString("member_password_change_success", "Password changed");
				}
				catch (Exception e) {
					e.printStackTrace();
					return iwrb.getLocalizedString("member_password_change_failed", "Could not change password");
				}
			}
		}
		return null;
	}

	private Table getTable() {
		Table table = new Table();
		table.setWidth("100%");
		table.setBorder(0);
		return table;
	}
	
	private Text getText(String content) {
		Text text = new Text(content);
		text.setStyleClass(getStyleName(MemberOverview.STYLENAME_TEXT));
		return text;
	}
	
	private Text getBigText(String content) {
		Text text = new Text(content);
		text.setStyleClass(getStyleName(STYLENAME_BIGTEXT));
		return text;
	}
	
	public Map getStyleNames() {
		Map map = new HashMap();
		map.put(STYLENAME_BIGTEXT, "font-family: Arial,Helvetica,sans-serif;font-size: 16px;font-weight: bold;color: #828282;");
		map.put(STYLENAME_INTERFACE_OBJECT, null);
		return map;
	}	
}
