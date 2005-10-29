/*
 * Created on Jun 8, 2004
 */
package is.idega.idegaweb.member.isi.block.reports.presentation.inputhandler;

import java.util.Collection;

import com.idega.business.InputHandler;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.TextInput;
import com.idega.util.text.TextSoap;

/**
 * @author Sigtryggur
 */

public class PersonalIDTextInput extends TextInput implements InputHandler {

	public Object convertSingleResultingObjectToType(Object value, String className) {
		return null;
	}

	public String getDisplayForResultingObject(Object value, IWContext iwc) {
		return (String)value;
	}

	public PresentationObject getHandlerObject(String name,	Collection values, IWContext iwc) {
		return null;
	}

	public PresentationObject getHandlerObject(String name,	String value, IWContext iwc) {
		this.setName(name);
		if (value != null) {
			this.setValue(value);
		}
		return this;
	}

	public Object getResultingObject(String[] value, IWContext iwc)	throws Exception {
		String personalID = null;
		if (value != null && value.length == 1) {
			personalID = value[0];
			personalID = TextSoap.findAndCut(personalID, "-");
			personalID = TextSoap.removeWhiteSpace(personalID);
		}
		return personalID;
	}
}
