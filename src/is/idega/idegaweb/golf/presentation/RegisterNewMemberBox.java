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
		
		Table table = new Table();
		table.setCellpaddingAndCellspacing(0);
		int row = 1;

		table.mergeCells(1, row, 3, row);
		table.add(getSmallHeader(localize("new.members", "New Members")), 1, row);
		table.setRowStyleClass(row, getHeaderRowClass());
		

		Text texti = getSmallText(localize("new.members.text", "Only golfclub members can register."));
		Text social = getSmallText(localize("new.members.socialsecuritynumber", "ssn :"));

		++row;
		table.add(texti, 1, row);
		table.mergeCells(1, row, 3, row);
		
		TextInput kt = (TextInput) getStyledInterface(new TextInput(RegisterNewMember.PARAMETER_PERSONAL_ID));

		++row;
		table.add(social, 1, row);
		table.add(kt, 2, row);
		table.add(new SubmitButton(getResourceBundle().getLocalizedString("register", "Register")), 3, row);

		form.add(table);
		add(form);
	}
	
	public void setResponsePage(ICPage page) {
		this.responsePage = page;
	}

}
