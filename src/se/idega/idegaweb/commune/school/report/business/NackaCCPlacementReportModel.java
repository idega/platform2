/*
 * $Id: NackaCCPlacementReportModel.java,v 1.9 2004/03/15 11:21:56 anders Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.school.report.business;

import java.rmi.RemoteException;

/** 
 * Report model for child care placements in Nacka.
 * <p>
 * Last modified: $Date: 2004/03/15 11:21:56 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.9 $
 */
public class NackaCCPlacementReportModel extends ReportModel {

	private final static int ROW_SIZE = 22;
	private final static int COLUMN_SIZE = 1;
	
	private final static int ROW_METHOD_PRE_SCHOOL_MANAGEMENT_PRE_SCHOOL = 1;
	private final static int ROW_METHOD_PRE_SCHOOL_MANAGEMENT_PRE_SCHOOL_COMMUNE = 2;
	private final static int ROW_METHOD_PRE_SCHOOL_MANAGEMENT_PRE_SCHOOL_PRIVATE = 3;
	private final static int ROW_METHOD_PRE_SCHOOL_MANAGEMENT_FAMILY_AFTER_SCHOOL = 4;
	private final static int ROW_METHOD_PRE_SCHOOL_MANAGEMENT_FAMILY_AFTER_SCHOOL_COMMUNE = 5;
	private final static int ROW_METHOD_PRE_SCHOOL_MANAGEMENT_FAMILY_AFTER_SCHOOL_PRIVATE = 6;
	
	private final static int ROW_METHOD_SCHOOL_CHILDREN_CARE_6_AFTER_SCHOOL = 8;
	private final static int ROW_METHOD_SCHOOL_CHILDREN_CARE_6_AFTER_SCHOOL_COMMUNE = 9;
	private final static int ROW_METHOD_SCHOOL_CHILDREN_CARE_6_AFTER_SCHOOL_PRIVATE = 10;
	private final static int ROW_METHOD_SCHOOL_CHILDREN_CARE_6_FAMILY_DAYCARE = 11;
	private final static int ROW_METHOD_SCHOOL_CHILDREN_CARE_6_FAMILY_DAYCARE_COMMUNE = 12;
	private final static int ROW_METHOD_SCHOOL_CHILDREN_CARE_6_FAMILY_DAYCARE_PRIVATE = 13;
	
	private final static int ROW_METHOD_SCHOOL_CHILDREN_CARE_7_9_AFTER_SCHOOL = 15;
	private final static int ROW_METHOD_SCHOOL_CHILDREN_CARE_7_9_AFTER_SCHOOL_COMMUNE = 16;
	private final static int ROW_METHOD_SCHOOL_CHILDREN_CARE_7_9_AFTER_SCHOOL_PRIVATE = 17;
	private final static int ROW_METHOD_SCHOOL_CHILDREN_CARE_7_9_FAMILY_AFTER_SCHOOL = 18;
	private final static int ROW_METHOD_SCHOOL_CHILDREN_CARE_7_9_FAMILY_AFTER_SCHOOL_COMMUNE = 19;
	private final static int ROW_METHOD_SCHOOL_CHILDREN_CARE_7_9_FAMILY_AFTER_SCHOOL_PRIVATE = 20;
	
	private final static int ROW_METHOD_SUM = 22;
	private final static int ROW_METHOD_TOTAL =23;

	private final static int COLUMN_METHOD_DATE = 101;

	private final static String QUERY_CHILD_CARE = "child_care";
	
	private final static int PRE_SCHOOL = 1;
	private final static int FAMILY_DAYCARE = 2;
	private final static int AFTER_SCHOOL_6 = 3;
	private final static int FAMILY_DAYCARE_6 = 4;
	private final static int AFTER_SCHOOL_7_9 = 5;
	private final static int FAMILY_AFTER_SCHOOL_7_9 = 6;

	private final static int COMMUNE = 1;
	private final static int PRIVATE = 2;
	
	private final static String KEY_REPORT_TITLE = KP + "title_nacka_child_care_placements";

	/**
	 * Constructs this report model.
	 * @param reportBusiness the report business instance for calculating cell values
	 */
	public NackaCCPlacementReportModel(ReportBusiness reportBusiness) {
		super(reportBusiness);
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#initReportSize()
	 */
	protected void initReportSize() {
		setReportSize(ROW_SIZE, COLUMN_SIZE);
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#buildRowHeaders()
	 */
	protected Header[] buildRowHeaders() {
		Header[] headers = new Header[4];
		
		Header h = new Header(KEY_PRE_SCHOOL_OPERATION, Header.HEADERTYPE_ROW_HEADER, 7);
		Header child0 = new Header(KEY_PRE_SCHOOL, Header.HEADERTYPE_ROW_NORMAL);
		Header child1 = new Header(KEY_OF_WHICH_COMMUNE_MANAGEMENT, Header.HEADERTYPE_ROW_NORMAL);
		Header child2 = new Header(KEY_OF_WHICH_PRIVATE_MANAGEMENT, Header.HEADERTYPE_ROW_NORMAL);
		Header child3 = new Header(KEY_FAMILY_DAYCARE, Header.HEADERTYPE_ROW_NORMAL);
		Header child4 = new Header(KEY_OF_WHICH_COMMUNE_MANAGEMENT, Header.HEADERTYPE_ROW_NORMAL);
		Header child5 = new Header(KEY_OF_WHICH_PRIVATE_MANAGEMENT, Header.HEADERTYPE_ROW_NORMAL);
		Header child6 = new Header(KEY_SUM, Header.HEADERTYPE_ROW_SUM);
		h.setChild(0, child0);
		h.setChild(1, child1);
		h.setChild(2, child2);
		h.setChild(3, child3);
		h.setChild(4, child4);
		h.setChild(5, child5);
		h.setChild(6, child6);
		headers[0] = h;
		
		h = new Header(KEY_SCHOOL_CHILDREN_CARE_6_YEAR, Header.HEADERTYPE_ROW_HEADER, 7);
		child0 = new Header(KEY_AFTER_SCHOOL_CENTRE, Header.HEADERTYPE_ROW_NORMAL);
		child1 = new Header(KEY_OF_WHICH_COMMUNE_MANAGEMENT, Header.HEADERTYPE_ROW_NORMAL);
		child2 = new Header(KEY_OF_WHICH_PRIVATE_MANAGEMENT, Header.HEADERTYPE_ROW_NORMAL);
		child3 = new Header(KEY_FAMILY_DAYCARE, Header.HEADERTYPE_ROW_NORMAL);
		child4 = new Header(KEY_OF_WHICH_COMMUNE_MANAGEMENT, Header.HEADERTYPE_ROW_NORMAL);
		child5 = new Header(KEY_OF_WHICH_PRIVATE_MANAGEMENT, Header.HEADERTYPE_ROW_NORMAL);
		child6 = new Header(KEY_SUM, Header.HEADERTYPE_ROW_SUM);
		h.setChild(0, child0);
		h.setChild(1, child1);
		h.setChild(2, child2);
		h.setChild(3, child3);
		h.setChild(4, child4);
		h.setChild(5, child5);
		h.setChild(6, child6);
		headers[1] = h;
		
		h = new Header(KEY_SCHOOL_CHILDREN_CARE_7_9_YEAR, Header.HEADERTYPE_ROW_HEADER, 7);
		child0 = new Header(KEY_AFTER_SCHOOL_CENTRE, Header.HEADERTYPE_ROW_NORMAL);
		child1 = new Header(KEY_OF_WHICH_COMMUNE_MANAGEMENT, Header.HEADERTYPE_ROW_NORMAL);
		child2 = new Header(KEY_OF_WHICH_PRIVATE_MANAGEMENT, Header.HEADERTYPE_ROW_NORMAL);
		child3 = new Header(KEY_FAMILY_AFTER_SCHOOL_CENTRE, Header.HEADERTYPE_ROW_NORMAL);
		child4 = new Header(KEY_OF_WHICH_COMMUNE_MANAGEMENT, Header.HEADERTYPE_ROW_NORMAL);
		child5 = new Header(KEY_OF_WHICH_PRIVATE_MANAGEMENT, Header.HEADERTYPE_ROW_NORMAL);
		child6 = new Header(KEY_SUM, Header.HEADERTYPE_ROW_SUM);
		h.setChild(0, child0);
		h.setChild(1, child1);
		h.setChild(2, child2);
		h.setChild(3, child3);
		h.setChild(4, child4);
		h.setChild(5, child5);
		h.setChild(6, child6);
		headers[2] = h;

		h = new Header(KEY_TOTAL, Header.HEADERTYPE_ROW_HEADER, 1);
		child0 = new Header(KEY_SUM_CHILDREN, Header.HEADERTYPE_ROW_NORMAL);
		h.setChild(0, child0);
		headers[3] = h;
		
		return headers;
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#buildColumnHeaders()
	 */
	protected Header[] buildColumnHeaders() {
		Header[] headers = new Header[1];
		
//		IWTimestamp now = IWTimestamp.RightNow();
//		String date = now.getDateString("yy-MM-dd");
		
		headers[0] = new Header(KEY_NUMBER_OF_CHILDREN, Header.HEADERTYPE_COLUMN_HEADER);
		
		return headers;
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#buildCells()
	 */
	protected void buildCells() {
		for (int column = 0; column < getColumnSize(); column++) {
			int row = 0;
			int columnMethod = 0;
			Object columnParameter = null;
			switch (column) {
				case 0:
					columnMethod = COLUMN_METHOD_DATE;
					columnParameter = null;
					break;
			}

			Object rowParameter = null;
			
			Cell cell = new Cell(this, row, column, ROW_METHOD_PRE_SCHOOL_MANAGEMENT_PRE_SCHOOL,
					columnMethod, rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row++, column, cell);
			
			cell = new Cell(this, row, column, ROW_METHOD_PRE_SCHOOL_MANAGEMENT_PRE_SCHOOL_COMMUNE,
					columnMethod, rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row++, column, cell);

			cell = new Cell(this, row, column, ROW_METHOD_PRE_SCHOOL_MANAGEMENT_PRE_SCHOOL_PRIVATE,
					columnMethod, rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row++, column, cell);

			cell = new Cell(this, row, column, ROW_METHOD_PRE_SCHOOL_MANAGEMENT_FAMILY_AFTER_SCHOOL,
					columnMethod, rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row++, column, cell);

			cell = new Cell(this, row, column, ROW_METHOD_PRE_SCHOOL_MANAGEMENT_FAMILY_AFTER_SCHOOL_COMMUNE,
					columnMethod, rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row++, column, cell);

			cell = new Cell(this, row, column, ROW_METHOD_PRE_SCHOOL_MANAGEMENT_FAMILY_AFTER_SCHOOL_PRIVATE,
					columnMethod, rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row++, column, cell);

			cell = new Cell(this, row, column, ROW_METHOD_SUM,
					columnMethod, rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row++, column, cell);

			cell = new Cell(this, row, column, ROW_METHOD_SCHOOL_CHILDREN_CARE_6_AFTER_SCHOOL,
					columnMethod, rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row++, column, cell);

			cell = new Cell(this, row, column, ROW_METHOD_SCHOOL_CHILDREN_CARE_6_AFTER_SCHOOL_COMMUNE,
					columnMethod, rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row++, column, cell);

			cell = new Cell(this, row, column, ROW_METHOD_SCHOOL_CHILDREN_CARE_6_AFTER_SCHOOL_PRIVATE,
					columnMethod, rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row++, column, cell);

			cell = new Cell(this, row, column, ROW_METHOD_SCHOOL_CHILDREN_CARE_6_FAMILY_DAYCARE,
					columnMethod, rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row++, column, cell);

			cell = new Cell(this, row, column, ROW_METHOD_SCHOOL_CHILDREN_CARE_6_FAMILY_DAYCARE_COMMUNE,
					columnMethod, rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row++, column, cell);

			cell = new Cell(this, row, column, ROW_METHOD_SCHOOL_CHILDREN_CARE_6_FAMILY_DAYCARE_PRIVATE,
					columnMethod, rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row++, column, cell);

			cell = new Cell(this, row, column, ROW_METHOD_SUM,
					columnMethod, rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row++, column, cell);

			cell = new Cell(this, row, column, ROW_METHOD_SCHOOL_CHILDREN_CARE_7_9_AFTER_SCHOOL,
					columnMethod, rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row++, column, cell);

			cell = new Cell(this, row, column, ROW_METHOD_SCHOOL_CHILDREN_CARE_7_9_AFTER_SCHOOL_COMMUNE,
					columnMethod, rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row++, column, cell);

			cell = new Cell(this, row, column, ROW_METHOD_SCHOOL_CHILDREN_CARE_7_9_AFTER_SCHOOL_PRIVATE,
					columnMethod, rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row++, column, cell);

			cell = new Cell(this, row, column, ROW_METHOD_SCHOOL_CHILDREN_CARE_7_9_FAMILY_AFTER_SCHOOL,
					columnMethod, rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row++, column, cell);

			cell = new Cell(this, row, column, ROW_METHOD_SCHOOL_CHILDREN_CARE_7_9_FAMILY_AFTER_SCHOOL_COMMUNE,
					columnMethod, rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row++, column, cell);

			cell = new Cell(this, row, column, ROW_METHOD_SCHOOL_CHILDREN_CARE_7_9_FAMILY_AFTER_SCHOOL_PRIVATE,
					columnMethod, rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row++, column, cell);

			cell = new Cell(this, row, column, ROW_METHOD_SUM,
					columnMethod, rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row++, column, cell);

			cell = new Cell(this, row, column, ROW_METHOD_TOTAL,
					columnMethod, rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row++, column, cell);
		}
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#calculate()
	 */
	protected float calculate(Cell cell) throws RemoteException {
		float value = 0f;
		int row = cell.getRow();
		int column = cell.getColumn();

		switch (cell.getRowMethod()) {
			case ROW_METHOD_PRE_SCHOOL_MANAGEMENT_PRE_SCHOOL:
				value = getCell(1, column).getFloatValue() + getCell(2, column).getFloatValue();
				break;
			case ROW_METHOD_PRE_SCHOOL_MANAGEMENT_PRE_SCHOOL_COMMUNE:
				value = getChildCarePlacementCount(PRE_SCHOOL, COMMUNE);
				break;
			case ROW_METHOD_PRE_SCHOOL_MANAGEMENT_PRE_SCHOOL_PRIVATE:
				value = getChildCarePlacementCount(PRE_SCHOOL, PRIVATE);
				break;
			case ROW_METHOD_PRE_SCHOOL_MANAGEMENT_FAMILY_AFTER_SCHOOL:
				value = getCell(4, column).getFloatValue() + getCell(5, column).getFloatValue();
				break;
			case ROW_METHOD_PRE_SCHOOL_MANAGEMENT_FAMILY_AFTER_SCHOOL_COMMUNE:
				value = getChildCarePlacementCount(FAMILY_DAYCARE, COMMUNE);
				break;
			case ROW_METHOD_PRE_SCHOOL_MANAGEMENT_FAMILY_AFTER_SCHOOL_PRIVATE:
				value = getChildCarePlacementCount(FAMILY_DAYCARE, PRIVATE);
				break;
			case ROW_METHOD_SCHOOL_CHILDREN_CARE_6_AFTER_SCHOOL:
				value = getCell(8, column).getFloatValue() + getCell(9, column).getFloatValue();
				break;
			case ROW_METHOD_SCHOOL_CHILDREN_CARE_6_AFTER_SCHOOL_COMMUNE:
				value = getChildCarePlacementCount(AFTER_SCHOOL_6, COMMUNE);
				break;
			case ROW_METHOD_SCHOOL_CHILDREN_CARE_6_AFTER_SCHOOL_PRIVATE:
				value = getChildCarePlacementCount(AFTER_SCHOOL_6, PRIVATE);
				break;
			case ROW_METHOD_SCHOOL_CHILDREN_CARE_6_FAMILY_DAYCARE:
				value = getCell(11, column).getFloatValue() + getCell(12, column).getFloatValue();
				break;
			case ROW_METHOD_SCHOOL_CHILDREN_CARE_6_FAMILY_DAYCARE_COMMUNE:
				value = getChildCarePlacementCount(FAMILY_DAYCARE_6, COMMUNE);
				break;
			case ROW_METHOD_SCHOOL_CHILDREN_CARE_6_FAMILY_DAYCARE_PRIVATE:
				value = getChildCarePlacementCount(FAMILY_DAYCARE_6, PRIVATE);
				break;
			case ROW_METHOD_SCHOOL_CHILDREN_CARE_7_9_AFTER_SCHOOL:
				value = getCell(15, column).getFloatValue() + getCell(16, column).getFloatValue();
				break;
			case ROW_METHOD_SCHOOL_CHILDREN_CARE_7_9_AFTER_SCHOOL_COMMUNE:
				value = getChildCarePlacementCount(AFTER_SCHOOL_7_9, COMMUNE);
				break;
			case ROW_METHOD_SCHOOL_CHILDREN_CARE_7_9_AFTER_SCHOOL_PRIVATE:
				value = getChildCarePlacementCount(AFTER_SCHOOL_7_9, PRIVATE);
				break;
			case ROW_METHOD_SCHOOL_CHILDREN_CARE_7_9_FAMILY_AFTER_SCHOOL:
				value = getCell(18, column).getFloatValue() + getCell(19, column).getFloatValue();
				break;
			case ROW_METHOD_SCHOOL_CHILDREN_CARE_7_9_FAMILY_AFTER_SCHOOL_COMMUNE:
				value = getChildCarePlacementCount(FAMILY_AFTER_SCHOOL_7_9, COMMUNE);
				break;
			case ROW_METHOD_SCHOOL_CHILDREN_CARE_7_9_FAMILY_AFTER_SCHOOL_PRIVATE:
				value = getChildCarePlacementCount(FAMILY_AFTER_SCHOOL_7_9, PRIVATE);
				break;
			case ROW_METHOD_SUM:
				for (int i = row - 6; i < row; i += 3) {
					value += getCell(i, column).getFloatValue();
				}
				break;
			case ROW_METHOD_TOTAL:
				value = getCell(6, column).getFloatValue() + 
						getCell(13, column).getFloatValue() +
						getCell(20, column).getFloatValue();
				break;
		}
		
		return value;
	}

	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#getReportTitleLocalizationKey()
	 */
	public String getReportTitleLocalizationKey() {
		return KEY_REPORT_TITLE;
	}
	
	/**
	 * Returns the number of student placements for the specified high school and school year.
	 */
	protected int getChildCarePlacementCount(int schoolType, int managementType) throws RemoteException {
		ReportBusiness rb = getReportBusiness();
		
		int schoolType1 = 0;
		int schoolType2 = 0;
		int schoolType3 = 0;
		int schoolType4 = 0;
		
		switch (schoolType) {
			case PRE_SCHOOL:
				schoolType1 = rb.getPreSchoolTypeId(); 
				schoolType2 = rb.getGeneralPreSchoolTypeId();
				schoolType3 = schoolType1;
				schoolType4 = schoolType2;
				break;
			case FAMILY_DAYCARE:
				schoolType1 = rb.getFamilyDayCareSchoolTypeId();
				schoolType2 = rb.getGeneralFamilyDaycareSchoolTypeId();
				schoolType3 = schoolType1;
				schoolType4 = schoolType2;
				break;
			case AFTER_SCHOOL_6:
				schoolType1 = rb.getAfterSchool6TypeId();
				schoolType2 = schoolType1;
				schoolType3 = schoolType1;
				schoolType4 = schoolType1;
				break;
			case FAMILY_DAYCARE_6:
				schoolType1 = rb.getFamilyAfterSchool6TypeId();
				schoolType2 = schoolType1;
				schoolType3 = schoolType1;
				schoolType4 = schoolType1;
				break;
			case AFTER_SCHOOL_7_9:
				schoolType1 = rb.getAfterSchool7_9TypeId();
				schoolType2 = schoolType1;
				schoolType3 = schoolType1;
				schoolType4 = schoolType1;
				break;
			case FAMILY_AFTER_SCHOOL_7_9:
				schoolType1 = rb.getFamilyAfterSchool7_9TypeId();
				schoolType2 = schoolType1;
				schoolType3 = schoolType1;
				schoolType4 = schoolType1;
				break;
		}
		
		String managementType1 = null;
		String managementType2 = null;
		String managementType3 = null;
		String managementType4 = null;
		
		switch (managementType) {
			case COMMUNE:
				managementType1 = "COMMUNE";
				managementType2 = managementType1;
				managementType3 = managementType1;
				managementType4 = managementType1;
				break;
			case PRIVATE:
				managementType1 = "COMPANY";
				managementType2 = "FOUNDATION";
				managementType3 = "OTHER";
				managementType4 = "COOPERATIVE_COMMUNE_LIABILITY";
				break;
		}
		
		PreparedQuery query = null;
		query = getQuery(QUERY_CHILD_CARE);
		if (query == null) {
			query = new PreparedQuery(getConnection());
			query.setSelectCount();
			query.setChildCarePlacements();
			query.setFourSchoolTypes(); // parameter 1-4
			query.setFourManagementTypes(); // parameter 5-8
			query.prepare();
			setQuery(QUERY_CHILD_CARE, query);
		}
		query.setInt(1, schoolType1);
		query.setInt(2, schoolType2);
		query.setInt(3, schoolType3);
		query.setInt(4, schoolType4);
		
		query.setString(5, managementType1);
		query.setString(6, managementType2);
		query.setString(7, managementType3);
		query.setString(8, managementType4);
		
		return query.execute();
	}
}
