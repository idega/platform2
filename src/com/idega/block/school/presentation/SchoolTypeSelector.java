package com.idega.block.school.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolTypeHome;
import com.idega.business.IBOLookup;
import com.idega.data.IDOLookup;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;

/**
 * @author gimmi
 */

public class SchoolTypeSelector extends Block {
  public static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.school";
  
 	public static String PARAMETER_SCHOOL_TYPE_ID = "schsel_schtyid";
	private int _schoolTypeId = -1;

	private boolean _horizontal = true;
	private String _fontStyle;
	private String _fontColor;
	private String _selStyle;
	private String _selColor;
	private String _spaceBetween = "5";


	public void main(IWContext iwc) throws RemoteException{
		init(iwc);
		
		drawList(iwc);
	}

	private void init(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_SCHOOL_TYPE_ID)) {
			try {
				_schoolTypeId = Integer.parseInt(iwc.getParameter(PARAMETER_SCHOOL_TYPE_ID));
			}catch (NumberFormatException n) {
				n.printStackTrace(System.err);
			}	
		}
	}

	public String getBundleIdentifier() {
			return IW_BUNDLE_IDENTIFIER;
	}	
	private void drawList(IWContext iwc) throws RemoteException {
		SchoolBusiness stb = (SchoolBusiness) IBOLookup.getServiceInstance( iwc, SchoolBusiness.class);
		Collection coll = stb.findAllSchoolTypes();
		
		if (coll != null) {
			SchoolType sType;
			SchoolTypeHome stHome = (SchoolTypeHome) IDOLookup.getHome(SchoolType.class);
			Table table = new Table();
			int sTypeId;
			table.setCellpaddingAndCellspacing(0);
			Link link;
			int row = 1;
			int col = 1;
			if (_horizontal) {
				col = 0;		
			}else {
				row = 0;	
			}
			
			Iterator iter = coll.iterator();	
			while (iter.hasNext()) {
					if (_horizontal) {
						++col;		
						table.setWidth(col, row, _spaceBetween);
						++col;
					}else {
						++row;	
						table.setHeight(col, row, _spaceBetween);
						++row;
					}
					
					sType = (SchoolType) iter.next() ;
					sTypeId = ((Integer) sType.getPrimaryKey()).intValue();
					if (sTypeId == _schoolTypeId) {
						table.add(getText(sType.getName(), true), col, row);	
					}else {
						table.add(getLink(sType.getName(), sType.getPrimaryKey().toString() ), col, row);
					}
			}
			add(table);
		}else {
			add("No types found");	
		}
			
	}
	
	private Link getLink(String content, String primaryKey) {
		Link link = new Link(getText(content, false));
		link.addParameter(PARAMETER_SCHOOL_TYPE_ID, primaryKey);
		return link;
	}
	
	private Text getText(String content, boolean selected) {
		Text text = new Text(content);
		if (selected) {
			if (_selColor != null) {
				text.setFontColor(_selColor);
			}	
			if (_selStyle != null) {
				text.setFontStyle(_selStyle);	
			}
		}else {
			if (_fontColor != null) {
				text.setFontColor(_fontColor);
			}	
			if (_fontStyle != null) {
				text.setFontStyle(_fontStyle);	
			}
		}
		return text;
	}
	
	
	/** Setters */
	
	public void setHorizontalView(boolean horizontal) {
		_horizontal = horizontal;	
	}
	
	public void setVerticalView(boolean vertical) {
		_horizontal = !vertical;	
	}
	
	public void setSpaceBetween(int spaceBetween) {
		_spaceBetween = Integer.toString( spaceBetween) ;
	}
	
	public void setFontStyle(String style) {
		_fontStyle = style;	
	}
	
	public void setFontColor(String color) {
		_fontColor = color;	
	}
	
	public void setSelectedFontStyle(String style) {
		_selStyle = style;	
	}
	
	public void setSelectedFontColor(String color) {
		_selColor	= color;
	}
	
}
