/*
 * Created on 6.6.2004
 */
package is.idega.idegaweb.golf.field.presentation;

import is.idega.idegaweb.golf.entity.Field;
import is.idega.idegaweb.golf.entity.FieldHome;

import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Text;


/**
 * @author laddi
 */
public class FieldNameText extends Text {

	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#main(com.idega.presentation.IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
		String field_id = iwc.getParameter("field_id");
		if (field_id == null)
			field_id = "50";

		Field field = ((FieldHome) IDOLookup.getHomeLegacy(Field.class)).findByPrimaryKey(Integer.parseInt(field_id));
		if (field != null) {
			setText(field.getName());
		}
		else {
			setText("No field found");
		}
	}
}