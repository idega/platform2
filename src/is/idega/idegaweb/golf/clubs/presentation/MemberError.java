/*
 * Created on 4.3.2004
 */
package is.idega.idegaweb.golf.clubs.presentation;

import java.util.Enumeration;
import java.util.Vector;

import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.Form;
import is.idega.idegaweb.golf.presentation.GolfBlock;

/**
 * @author laddi
 */
public class MemberError extends GolfBlock {

	public void main(IWContext modinfo) throws Exception {
		Vector vError = (Vector) modinfo.getRequest().getSession().getAttribute("error");
		String errorString = "";
		BackButton back = new BackButton("Til baka");
		Table table = null;
		Form form = new Form();

		if (vError != null) {
			table = new Table(1, vError.size() + 4);
			int i = 0;
			table.addText("<h4>Eftirfarandi upplýsingar vantar: ", 1, 1);
			for (Enumeration enumer = vError.elements(); enumer.hasMoreElements();) {
				errorString = (String) enumer.nextElement();
				table.addText(errorString, 1, i + 3);
				i++;
			}
			table.add(back, 1, i + 4);
		}
		//modinfo.getSession().removeAttribute("error");
		form.add(table);
		add(form);
	}
}