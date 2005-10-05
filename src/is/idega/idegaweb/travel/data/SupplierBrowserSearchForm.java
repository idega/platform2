/*
 * $Id: SupplierBrowserSearchForm.java,v 1.2 2005/10/05 22:43:48 gimmi Exp $
 * Created on Aug 19, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.travel.data;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.text.Paragraph;
import com.idega.presentation.ui.Label;


public class SupplierBrowserSearchForm {

	private String localizationKey = null;
	private String pageID = null;
//	private Collection texts = null;
//	private Collection ios = null;
	private Collection ps = null;
	
	public SupplierBrowserSearchForm(String localizationKey, String pageID) {
		this.localizationKey = localizationKey;
		this.pageID = pageID;
	}
	
//	public void setTexts(Collection texts) {
//		this.texts = texts;
//	}
//	
//	public void setInterfaceObjects(Collection ios) {
//		this.ios = ios;
//	}
//	
//	public Collection getTexts() {
//		return texts;
//	}
//	
//	public Collection getInterfaceObjects() {
//		return ios;
//	}
	
	public void setPararaphs(Collection paragraphs) {
		this.ps = paragraphs;
	}
	
	public Collection getParagraphs() {
		return ps;
	}
	
	public String getPageID() {
		return pageID;
	}
	
	public String getLocalizationKey() {
		return localizationKey;
	}
	
	public Collection getParameters() {
		Collection colls = getParagraphs();
		Collection parameters = new Vector();
		Iterator iter = colls.iterator();
		while (iter.hasNext()) {
			Paragraph p = (Paragraph) iter.next();
			Collection c = p.getChildren();
			Iterator cIter = c.iterator();
			while (cIter.hasNext()) {
				Object obj = cIter.next();
				if (!(obj instanceof Label)) {
					parameters.add(((PresentationObject)obj).getName());
				}
			}
		}
		return parameters;
	}
	
}
