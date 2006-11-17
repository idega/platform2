package is.idega.idegaweb.campus.block.application.business;

import com.idega.idegaweb.IWApplicationContext;
import com.idega.block.application.business.ReferenceNumberHandler;
import com.idega.util.CypherText;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author <br>
 *         <a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class ReferenceNumberFinder {

	public static int LENGTH = 7;

	private static ReferenceNumberFinder finder;

	private String key = "";

	private CypherText cypherText;

	private ReferenceNumberHandler refHandler;

	private ReferenceNumberFinder(IWApplicationContext iwac) {
		// System.err.println("Creating Finder");
		refHandler = new ReferenceNumberHandler();
		cypherText = new CypherText(iwac);
		key = refHandler.getCypherKey(iwac);
		String refLen = iwac.getApplicationSettings().getProperty(
				"CAMPUS_REFERENCE_LENGTH", "7");
		LENGTH = new Integer(refLen).intValue();
	}

	public static ReferenceNumberFinder getInstance(IWApplicationContext iwac) {
		if (finder == null) {
			finder = new ReferenceNumberFinder(iwac);
		}
		return finder;
	}

	public String lookup(int applicationId) {
		String id = Integer.toString(applicationId);
		while (id.length() < LENGTH)
			id = "0" + id;
		String refNum = cypherText.doCyper(id, key);

		return refNum;
	}
}