/*
 * Created on 17.5.2004
 */
package is.idega.idegaweb.golf.field.presentation;

import java.util.Collection;
import java.util.Iterator;

import is.idega.idegaweb.golf.entity.Field;
import is.idega.idegaweb.golf.entity.FieldHome;
import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.entity.UnionHome;
import is.idega.idegaweb.golf.presentation.GolfBlock;

import javax.ejb.FinderException;

import com.idega.core.builder.data.ICPage;
import com.idega.data.IDOLookup;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Page;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;


/**
 * Title: FieldList
 * Description:
 * Copyright: Copyright (c) 2004
 * Company: idega Software
 * @author 2004 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version 1.0
 */
public class FieldList extends GolfBlock {

	private Table _table;
	private ICPage _fieldOverviewPage = null;
	
	/**
	 * 
	 */
	public FieldList() {
		super();
		// TODO Auto-generated constructor stub
		_table = new Table();
	}
	
	public void initializeInMain(IWContext iwc) throws Exception {
		super.initializeInMain(iwc);
		this.add(_table);
	}
	
	
	public void main(IWContext iwc) throws Exception {
		_table.empty();
		Page page = this.getParentPage();
		if(page != null) {
			int rootPageID = page.getDPTRootPageID();
			if(rootPageID != -1) {
				try {
					Group gr = ((GroupHome)IDOLookup.getHome(Group.class)).findByHomePageID(rootPageID);
					Union union = ((UnionHome)IDOLookup.getHome(Union.class)).findUnionByIWMemberSystemGroup(gr);
					
					Collection fields = ((FieldHome)IDOLookup.getHome(Field.class)).findByUnion(union);
					int rowIndex = 1;
					for (Iterator iter = fields.iterator(); iter.hasNext();) {
						Field field = (Field) iter.next();
						Link l = getLink(field.getName()); 
						if(_fieldOverviewPage!=null) {
							l.setPage(_fieldOverviewPage);
						}
						l.addParameter(FieldOverview.PRM_FIELD_ID,field.getPrimaryKey().toString());
						_table.add(l,1,rowIndex++);
					}
				} catch (FinderException e) {
					// No Group found
					System.out.println("["+this.getClassName()+"]: no Group has this page("+rootPageID+") as homepage");
				}
			} else {
//
			}
		}
	}
	
	
	public void setCellpadding(int padding) {
		_table.setCellpadding(padding);
	}
	
	public void setCellspacing(int spacing) {
		_table.setCellspacing(spacing);
	}
	
	public void setWidth(String width) {
		_table.setWidth(width);
	}


	/**
	 * @param overviewPage The _fieldOverviewPage to set.
	 */
	public void setFieldOverviewPage(ICPage fieldOverviewPage) {
		_fieldOverviewPage = fieldOverviewPage;
	}
}
