package com.idega.block.school.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.business.SchoolContentBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolArea;
import com.idega.block.school.data.SchoolCategory;
import com.idega.business.IBOLookup;
import com.idega.core.builder.data.ICPage;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;

/**
 * @author gimmi
 */
public class SchoolAreaSelector extends Block {
  public static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.school";
  
 	private String PARAMETER_SCHOOL_TYPE_ID = SchoolTypeSelector.PARAMETER_SCHOOL_TYPE_ID;
 	private String PARAMETER_SCHOOL_ID = "";
 	public static String PARAMETER_SCHOOL_AREA_ID = "schar_arid";
	private int _schoolTypeId = -1;
	private int _schoolAreaId = -1;
	private int _schoolId = -1;

	private boolean _maintainSchoolTypeId = true;
	private boolean _expandSchools = true;
	private boolean _displayWithoutTypeId = true;
	private String _fontStyle;
	private String _fontColor;
	private String _selStyle;
	private String _selColor;
	private String _expandedFontStyle;
	private String _expandedFontColor;
	private String _expandedSelStyle;
	private String _expandedSelColor;
	private String _spaceBetween = "5";
	private ICPage _page;
	private int _spaceBeforeExpanded = 2;
	private boolean _isHighSchool = false;
	private boolean _showSchoolArea = true;
	
	private IWResourceBundle iwrb = null;

	public void main(IWContext iwc) throws RemoteException{
		iwrb = getResourceBundle(iwc);
		init(iwc);
		
	if (!_displayWithoutTypeId && _schoolTypeId == -1) {
			/** Does Nothing */
		}else {
			if (!getShowSchoolArea()) { 
					drawNoSchoolAreaList(iwc);
				}
			else {
			drawList(iwc);
			}
		}
	}

	private void init(IWContext iwc) throws RemoteException {
		PARAMETER_SCHOOL_ID = ((SchoolContentBusiness) IBOLookup.getSessionInstance(iwc, SchoolContentBusiness.class)).getParameterSchoolId() ;
		if (iwc.isParameterSet(PARAMETER_SCHOOL_AREA_ID)) {
			try {
				_schoolAreaId = Integer.parseInt(iwc.getParameter(PARAMETER_SCHOOL_AREA_ID));
			}catch (NumberFormatException n) {
				n.printStackTrace(System.err);
			}	
		}

		if (iwc.isParameterSet(PARAMETER_SCHOOL_TYPE_ID)) {
			try {
				_schoolTypeId = Integer.parseInt(iwc.getParameter(PARAMETER_SCHOOL_TYPE_ID));
			}catch (NumberFormatException n) {
				n.printStackTrace(System.err);
			}	
		}
		if (iwc.isParameterSet(PARAMETER_SCHOOL_ID)) {
			try {
				_schoolId = Integer.parseInt(iwc.getParameter(PARAMETER_SCHOOL_ID));
			}catch (NumberFormatException n) {
				n.printStackTrace(System.err);
			}	
		}	}

	public String getBundleIdentifier() {
			return IW_BUNDLE_IDENTIFIER;
	}	

private void drawNoSchoolAreaList(IWContext iwc) throws RemoteException {
			SchoolBusiness sb = (SchoolBusiness) IBOLookup.getServiceInstance( iwc, SchoolBusiness.class);
			//SchoolArea sArea;
			//int iAreaId;
		
			Collection coll;
			SchoolCategory highSchoolCategory = sb.getCategoryHighSchool();
			SchoolCategory elementarySchoolCategory = sb.getCategoryElementarySchool();
			
			if (_isHighSchool) {
				coll = sb.findAllSchoolsByCategory(highSchoolCategory.getCategory());
			}  else {
				coll = sb.findAllSchoolsByCategory(elementarySchoolCategory.getCategory());
			}			
			
			School school;
			int iSchoolId;
		
			Table table = new Table();
			table.setCellpaddingAndCellspacing(0);

			int row = 0;
			int col = 1;
			
			Collection collSchools = sb.getHomeCommuneSchools(coll);
			Iterator iter = collSchools.iterator(); //collSchools = collection with all schools for a specific category and the home commune
			Hashtable hash = new Hashtable();
			while (iter.hasNext()) {
				++row;		
				table.setWidth(col, row, _spaceBetween);

				++row;
									
				if (coll != null) {
						school = (School) iter.next();
						String pk = school.getPrimaryKey().toString();
						//System.err.println("checking school "+pk.toString());
						boolean invisibleForCitizen = false;
						invisibleForCitizen = school.getInvisibleForCitizen();
						if (!hash.containsKey(pk) && !invisibleForCitizen) {
							iSchoolId = ((Integer) school.getPrimaryKey()).intValue();
							table.add(getExpandedLink(school.getName(), Integer.toString(school.getSchoolAreaId()), Integer.toString(iSchoolId)), col, row);
						}
	
				}
		  }
			add(table);
	}
	
	private void drawList(IWContext iwc) throws RemoteException {
		SchoolBusiness sb = (SchoolBusiness) IBOLookup.getServiceInstance( iwc, SchoolBusiness.class);
		
		Collection coll = null;
		if (_schoolTypeId != -1) {
			coll = sb.findAllSchoolAreasByType( _schoolTypeId)	;
		} else {
			coll = sb.findAllSchoolAreas();	
		}
		
		if (coll != null) {
			SchoolArea sArea;
			int iAreaId;
			
			School school;
			int iSchoolId;
			
			Table table = new Table();
			table.setCellpaddingAndCellspacing(0);

			int row = 0;
			int col = 1;
			Hashtable hash = new Hashtable();
			Iterator iter = coll.iterator();	
			while (iter.hasNext()) {
				++row;		
				table.setWidth(col, row, _spaceBetween);
				++row;
				
				sArea = (SchoolArea) iter.next() ;
				iAreaId = ((Integer) sArea.getPrimaryKey()).intValue();
				if (iAreaId == _schoolAreaId) {
					table.add(getText(sArea.getName(), true), col, row);	
					if ( _expandSchools && _schoolTypeId != -1) {
						Collection schools	= sb.findAllSchoolsByAreaAndType(_schoolAreaId, _schoolTypeId);
						schools = sb.getHomeCommuneSchools(schools);
						if (schools != null) {
							String indent = "";
							for (int i = 0; i < _spaceBeforeExpanded; i++) {
								indent = indent + Text.NON_BREAKING_SPACE;	
							}

							Iterator sIter = schools.iterator();
							while (sIter.hasNext()) {
								++row;
								school = (School) sIter.next();
								iSchoolId = ((Integer) school.getPrimaryKey()).intValue();
								String pk = school.getPrimaryKey().toString();
								//System.err.println("checking school "+pk.toString());
								boolean invisibleForCitizen = false;
								invisibleForCitizen = school.getInvisibleForCitizen();
								if (!hash.containsKey(pk) && !invisibleForCitizen) {
									if (iSchoolId == _schoolId) {
										table.add(getExpandedText(indent+school.getName(), true), col, row);
									}else {
										table.add(getExpandedText(indent, false), col, row);
										table.add(getExpandedLink(school.getName(), Integer.toString(iAreaId), Integer.toString(iSchoolId)), col, row);
									}
								}
//								table.add(getText(school.getName(), false), col, row);
							}	
						}
					}else if (_expandSchools && _schoolTypeId == -1) {
						++row;
						table.add(getText(iwrb.getLocalizedString("school.school_type_not_defined", "Choose school type."), false), col, row);
					}
				}else {
					table.add(getLink(sArea.getName(), sArea.getPrimaryKey().toString() ), col, row);
				}
			}

			add(table);
		}else {
			add("No areas found");	
		}
			
	}
	
	private Link getLink(String content, String primaryKey) {
		Link link = new Link(getText(content, false));
		link.addParameter(PARAMETER_SCHOOL_AREA_ID, primaryKey);
		if (_maintainSchoolTypeId) {
			link.addParameter( PARAMETER_SCHOOL_TYPE_ID, _schoolTypeId);	
		}
		if (_schoolId != -1) {
			link.addParameter( PARAMETER_SCHOOL_ID, _schoolId);	
		}
		return link;
	}
	
	private Link getExpandedLink(String content, String schoolAreaId, String schoolId) {
		Link link = new Link(getExpandedText(content, false));
		link.addParameter(PARAMETER_SCHOOL_AREA_ID, schoolAreaId);
		if (_maintainSchoolTypeId) {
			link.addParameter( PARAMETER_SCHOOL_TYPE_ID, _schoolTypeId);	
		}
		if (_page != null) {
			link.setPage(_page);	
		}
		link.addParameter(PARAMETER_SCHOOL_ID, schoolId);
		return link;
	}

	private Text getExpandedText(String content, boolean selected) {
		Text text = new Text(content);
		if (selected) {
			if (_expandedSelColor != null) {
				text.setFontColor(_expandedSelColor);
			}	
			if (_expandedSelStyle != null) {
				text.setFontStyle(_expandedSelStyle);	
			}
		}else {
			if (_expandedFontColor != null) {
				text.setFontColor(_expandedFontColor);
			}	
			if (_expandedFontStyle != null) {
				text.setFontStyle(_expandedFontStyle);	
			}
		}
		return text;
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

/*	public void setHorizontalView(boolean horizontal) {
		_horizontal = horizontal;	
	}
	
	public void setVerticalView(boolean vertical) {
		_horizontal = !vertical;	
	}*/
	
	public void setSchoolsToExpand(boolean expandSchools) {
		_expandSchools = expandSchools;	
	}
	
	public void setUseSchoolTypeIdParameter(boolean maintain) {
		_maintainSchoolTypeId = maintain;	
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

	public void setExpandedFontStyle(String style) {
		_expandedFontStyle = style;	
	}
	
	public void setExpandedFontColor(String color) {
		_expandedFontColor = color;	
	}
	
	public void setExpandedSelectedFontStyle(String style) {
		_expandedSelStyle = style;	
	}
	
	public void setExpandedSelectedFontColor(String color) {
		_expandedSelColor	= color;
	}	
	
	public void setDisplayWithoutSchoolTypeId(boolean display) {
		_displayWithoutTypeId = display;
	}
	
	public void setSpaceBeforeExpanded(int numberOfSpaces) {
		_spaceBeforeExpanded = numberOfSpaces;	
	}
	
	public void setPage(ICPage page) {
		_page = page;	
	}
	public void setIsHighSchool(boolean isHighSchool){
		_isHighSchool = isHighSchool;	
	}
	
	public boolean getIsHighSchool(){
			return _isHighSchool;	
	}
	
	public void setShowSchoolArea(boolean showSchoolArea){
		_showSchoolArea = showSchoolArea;	
	}
	
	public boolean getShowSchoolArea(){
		return _showSchoolArea;	
	}
}
