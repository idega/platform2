/*
 * Created on 14.5.2004
 */
package com.idega.block.school.presentation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import com.idega.block.school.business.SchoolComparator;
import com.idega.block.school.business.SchoolContentBusiness;
import com.idega.block.school.data.School;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.builder.data.ICPage;
import com.idega.idegaweb.IWUserContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Lists;
import com.idega.presentation.text.Text;

/**
 * @author laddi
 */
public class SchoolListViewer extends SchoolBlock {
	
	private static final String STYLENAME_SCHOOL_LIST = "schoolList";
	
	private String iSchoolCategory;
	private ICPage iResponsePage;
	
	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.school.music.presentation.MusicSchoolBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		List schools = null;
		if (this.iSchoolCategory != null) {
			schools = new ArrayList(getBusiness().findAllSchoolsByCategory(this.iSchoolCategory));
		}
		else {
			schools = new ArrayList(getBusiness().findAllSchools());
		}
		Collections.sort(schools, new SchoolComparator(iwc.getCurrentLocale()));
		
		Lists list = new Lists();
		list.setStyleClass(STYLENAME_SCHOOL_LIST);
		
		Iterator iter = schools.iterator();
		while (iter.hasNext()) {
			School school = (School) iter.next();
			if (this.iResponsePage != null) {
				Link link = new Link(school.getSchoolName());
				link.setPage(this.iResponsePage);
				link.addParameter(getSchoolContentBusiness(iwc).getParameterSchoolId(), school.getPrimaryKey().toString());
				list.add(link);
			}
			else {
				Text text = new Text(school.getSchoolName());
				list.add(text);
			}
		}
		add(list);
	}
	
	private SchoolContentBusiness getSchoolContentBusiness(IWUserContext iwuc) {
		try {
			return (SchoolContentBusiness) IBOLookup.getSessionInstance(iwuc, SchoolContentBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}
	
	public void setSchoolCategory(String category) {
		this.iSchoolCategory = category;
	}

	public void setResponsePage(ICPage page) {
		this.iResponsePage = page;
	}
}