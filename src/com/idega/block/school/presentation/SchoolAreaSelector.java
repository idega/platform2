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

	private boolean _communeSchools = true;

	private int _outsideSchoolAreaID = -1;

	private IWResourceBundle iwrb = null;

	public void main(IWContext iwc) throws RemoteException {
		this.iwrb = getResourceBundle(iwc);
		init(iwc);

		if (!this._displayWithoutTypeId && this._schoolTypeId == -1) {
			/** Does Nothing */
		} else {
			if (!getShowSchoolArea()) {
				drawNoSchoolAreaList(iwc);
			} else {
				drawList(iwc);
			}
		}
	}

	private void init(IWContext iwc) throws RemoteException {
		this.PARAMETER_SCHOOL_ID = ((SchoolContentBusiness) IBOLookup
				.getSessionInstance(iwc, SchoolContentBusiness.class))
				.getParameterSchoolId();
		if (iwc.isParameterSet(PARAMETER_SCHOOL_AREA_ID)) {
			try {
				this._schoolAreaId = Integer.parseInt(iwc
						.getParameter(PARAMETER_SCHOOL_AREA_ID));
			} catch (NumberFormatException n) {
				n.printStackTrace(System.err);
			}
		}

		if (iwc.isParameterSet(this.PARAMETER_SCHOOL_TYPE_ID)) {
			try {
				this._schoolTypeId = Integer.parseInt(iwc
						.getParameter(this.PARAMETER_SCHOOL_TYPE_ID));
			} catch (NumberFormatException n) {
				n.printStackTrace(System.err);
			}
		}
		if (iwc.isParameterSet(this.PARAMETER_SCHOOL_ID)) {
			try {
				this._schoolId = Integer.parseInt(iwc
						.getParameter(this.PARAMETER_SCHOOL_ID));
			} catch (NumberFormatException n) {
				n.printStackTrace(System.err);
			}
		}
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	private void drawNoSchoolAreaList(IWContext iwc) throws RemoteException {
		SchoolBusiness sb = (SchoolBusiness) IBOLookup.getServiceInstance(iwc,
				SchoolBusiness.class);
		//SchoolArea sArea;
		//int iAreaId;

		Collection coll;
		SchoolCategory highSchoolCategory = sb.getCategoryHighSchool();
		SchoolCategory elementarySchoolCategory = sb
				.getCategoryElementarySchool();

		if (this._isHighSchool) {
			coll = sb
					.findAllSchoolsByCategory(highSchoolCategory.getCategory());
		} else if (this._schoolTypeId == -1) {
			coll = sb.findAllSchoolsByCategory(elementarySchoolCategory
					.getCategory());
		} else {
			coll = sb.findAllSchoolsByType(this._schoolTypeId);
		}

		School school;
		int iSchoolId;

		Table table = new Table();
		table.setCellpaddingAndCellspacing(0);

		int row = 0;
		int col = 1;
		Collection collSchools = null;

		if (this._communeSchools) {
			collSchools = sb.getHomeCommuneSchools(coll);
		}
		else {
			collSchools = coll;
		}
		Iterator iter = collSchools.iterator(); //collSchools = collection with
												// all schools for a specific
												// category and the home commune
		Hashtable hash = new Hashtable();
		while (iter.hasNext()) {
			++row;
			table.setWidth(col, row, this._spaceBetween);

			++row;

			if (coll != null) {
				school = (School) iter.next();
				String pk = school.getPrimaryKey().toString();
				//System.err.println("checking school "+pk.toString());
				boolean invisibleForCitizen = false;
				invisibleForCitizen = school.getInvisibleForCitizen();
				if (!hash.containsKey(pk) && !invisibleForCitizen) {
					iSchoolId = ((Integer) school.getPrimaryKey()).intValue();
					table.add(getExpandedLink(school.getName(), Integer
							.toString(school.getSchoolAreaId()), Integer
							.toString(iSchoolId)), col, row);
				}

			}
		}
		add(table);
	}

	private void drawList(IWContext iwc) throws RemoteException {
		SchoolBusiness sb = (SchoolBusiness) IBOLookup.getServiceInstance(iwc,
				SchoolBusiness.class);

		Collection coll = null;
		if (this._schoolTypeId != -1) {
			coll = sb.findAllSchoolAreasByType(this._schoolTypeId);
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
				table.setWidth(col, row, this._spaceBetween);
				++row;

				sArea = (SchoolArea) iter.next();
				iAreaId = ((Integer) sArea.getPrimaryKey()).intValue();
				if (iAreaId != this._outsideSchoolAreaID){
				if (iAreaId == this._schoolAreaId) {
					table.add(getText(sArea.getName(), true), col, row);
					if (this._expandSchools && this._schoolTypeId != -1) {
						Collection schools = sb.findAllSchoolsByAreaAndType(
								this._schoolAreaId, this._schoolTypeId);
						schools = sb.getHomeCommuneSchools(schools);
						if (schools != null) {
							String indent = "";
							for (int i = 0; i < this._spaceBeforeExpanded; i++) {
								indent = indent + Text.NON_BREAKING_SPACE;
							}

							Iterator sIter = schools.iterator();
							while (sIter.hasNext()) {
								++row;
								school = (School) sIter.next();
								iSchoolId = ((Integer) school.getPrimaryKey())
										.intValue();
								String pk = school.getPrimaryKey().toString();
								//System.err.println("checking school
								// "+pk.toString());
								boolean invisibleForCitizen = false;
								invisibleForCitizen = school
										.getInvisibleForCitizen();
								if (!hash.containsKey(pk)
										&& !invisibleForCitizen) {
									if (iSchoolId == this._schoolId) {
										table.add(getExpandedText(indent
												+ school.getName(), true), col,
												row);
									} else {
										table.add(
												getExpandedText(indent, false),
												col, row);
										table
												.add(
														getExpandedLink(
																school
																		.getName(),
																Integer
																		.toString(iAreaId),
																Integer
																		.toString(iSchoolId)),
														col, row);
									}
								}
								//								table.add(getText(school.getName(), false),
								// col, row);
							}
						}
					} else if (this._expandSchools && this._schoolTypeId == -1) {
						++row;
						table.add(getText(this.iwrb.getLocalizedString(
								"school.school_type_not_defined",
								"Choose school type."), false), col, row);
					}
				} else {
					table.add(getLink(sArea.getName(), sArea.getPrimaryKey()
							.toString()), col, row);
				}
				
				} //end if _outsideSchoolAreaID
			}

			add(table);
		} else {
			add("No areas found");
		}

	}

	private Link getLink(String content, String primaryKey) {
		Link link = new Link(getText(content, false));
		link.addParameter(PARAMETER_SCHOOL_AREA_ID, primaryKey);
		if (this._maintainSchoolTypeId) {
			link.addParameter(this.PARAMETER_SCHOOL_TYPE_ID, this._schoolTypeId);
		}
		if (this._schoolId != -1) {
			link.addParameter(this.PARAMETER_SCHOOL_ID, this._schoolId);
		}
		return link;
	}

	private Link getExpandedLink(String content, String schoolAreaId,
			String schoolId) {
		Link link = new Link(getExpandedText(content, false));
		link.addParameter(PARAMETER_SCHOOL_AREA_ID, schoolAreaId);
		if (this._maintainSchoolTypeId) {
			link.addParameter(this.PARAMETER_SCHOOL_TYPE_ID, this._schoolTypeId);
		}
		if (this._page != null) {
			link.setPage(this._page);
		}
		link.addParameter(this.PARAMETER_SCHOOL_ID, schoolId);
		return link;
	}

	private Text getExpandedText(String content, boolean selected) {
		Text text = new Text(content);
		if (selected) {
			if (this._expandedSelColor != null) {
				text.setFontColor(this._expandedSelColor);
			}
			if (this._expandedSelStyle != null) {
				text.setFontStyle(this._expandedSelStyle);
			}
		} else {
			if (this._expandedFontColor != null) {
				text.setFontColor(this._expandedFontColor);
			}
			if (this._expandedFontStyle != null) {
				text.setFontStyle(this._expandedFontStyle);
			}
		}
		return text;
	}

	private Text getText(String content, boolean selected) {
		Text text = new Text(content);
		if (selected) {
			if (this._selColor != null) {
				text.setFontColor(this._selColor);
			}
			if (this._selStyle != null) {
				text.setFontStyle(this._selStyle);
			}
		} else {
			if (this._fontColor != null) {
				text.setFontColor(this._fontColor);
			}
			if (this._fontStyle != null) {
				text.setFontStyle(this._fontStyle);
			}
		}
		return text;
	}

	/** Setters */

	/*
	 * public void setHorizontalView(boolean horizontal) { _horizontal =
	 * horizontal; }
	 * 
	 * public void setVerticalView(boolean vertical) { _horizontal = !vertical; }
	 */

	public void setSchoolsToExpand(boolean expandSchools) {
		this._expandSchools = expandSchools;
	}

	public void setUseSchoolTypeIdParameter(boolean maintain) {
		this._maintainSchoolTypeId = maintain;
	}

	public void setSpaceBetween(int spaceBetween) {
		this._spaceBetween = Integer.toString(spaceBetween);
	}

	public void setFontStyle(String style) {
		this._fontStyle = style;
	}

	public void setFontColor(String color) {
		this._fontColor = color;
	}

	public void setSelectedFontStyle(String style) {
		this._selStyle = style;
	}

	public void setSelectedFontColor(String color) {
		this._selColor = color;
	}

	public void setExpandedFontStyle(String style) {
		this._expandedFontStyle = style;
	}

	public void setExpandedFontColor(String color) {
		this._expandedFontColor = color;
	}

	public void setExpandedSelectedFontStyle(String style) {
		this._expandedSelStyle = style;
	}

	public void setExpandedSelectedFontColor(String color) {
		this._expandedSelColor = color;
	}

	public void setDisplayWithoutSchoolTypeId(boolean display) {
		this._displayWithoutTypeId = display;
	}

	public void setSpaceBeforeExpanded(int numberOfSpaces) {
		this._spaceBeforeExpanded = numberOfSpaces;
	}

	public void setPage(ICPage page) {
		this._page = page;
	}

	public void setIsHighSchool(boolean isHighSchool) {
		this._isHighSchool = isHighSchool;
	}

	public boolean getIsHighSchool() {
		return this._isHighSchool;
	}

	public void setShowSchoolArea(boolean showSchoolArea) {
		this._showSchoolArea = showSchoolArea;
	}

	public boolean getShowSchoolArea() {
		return this._showSchoolArea;
	}

	public void setShowOnlyCommuneSchools(boolean communeSchools) {
		this._communeSchools = communeSchools;
	}

	public void setOutsideSchoolAreaId(int outsideSchoolAreaID) {
		this._outsideSchoolAreaID = outsideSchoolAreaID;
	}
}
