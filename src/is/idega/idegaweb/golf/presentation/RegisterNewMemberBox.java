package is.idega.idegaweb.golf.presentation;

import com.idega.core.builder.data.ICPage;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;


/**
 * @author gimmi
 */
public class RegisterNewMemberBox extends GolfBlock {

	private ICPage responsePage = null;

	public void main(IWContext modinfo) throws Exception {
		
		Form form = new Form();
		if (responsePage != null) {
			form.setPageToSubmitTo(responsePage);
		}
		
		Table table = new Table(2, 2);
		table.setCellpadding(3);
		table.setCellspacing(0);
		int row = 1;
		table.mergeCells(1, row, 2, row);
		table.setAlignment(2, 2, Table.HORIZONTAL_ALIGN_RIGHT);

		Text social = getSmallText(localize("new.members.social_security_number", "Enter social security number") + ":");

		table.add(social, 1, row++);
		
		TextInput kt = (TextInput) getStyledInterface(new TextInput(RegisterNewMember.PARAMETER_PERSONAL_ID));

		table.add(kt, 1, row);
		table.add(getButton(new SubmitButton(getResourceBundle().getLocalizedString("register", "Register"))), 2, row);

		form.add(table);
		add(form);
	}
	
	public void setResponsePage(ICPage page) {
		this.responsePage = page;
	}

}
