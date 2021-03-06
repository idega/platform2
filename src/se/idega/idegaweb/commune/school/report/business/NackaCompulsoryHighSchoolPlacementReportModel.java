/*
 * $Id: NackaCompulsoryHighSchoolPlacementReportModel.java,v 1.7 2004/02/17 21:24:52 anders Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.school.report.business;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import com.idega.block.school.data.School;

/** 
 * Report model for placements in Nacka compulsory high schools.
 * <p>
 * Last modified: $Date: 2004/02/17 21:24:52 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.7 $
 */
public class NackaCompulsoryHighSchoolPlacementReportModel extends ReportModel {

	private final static int COLUMN_SIZE = 6;
	
	private final static int ROW_METHOD_SCHOOL = 1;
	private final static int ROW_METHOD_TOTAL = 2;

	private final static int COLUMN_METHOD_SCHOOL_YEAR = 101;
	private final static int COLUMN_METHOD_TOTAL_1_4 = 102;
	private final static int COLUMN_METHOD_OTHER_COMMUNE_CITIZENS = 103;

	private final static String QUERY_NACKA_COMMUNE = "nacka_commune";
	private final static String QUERY_OTHER_COMMUNES = "other_communes";
	
	private final static String KEY_REPORT_TITLE = KP + "title_nacka_compulsory_high_school_placements";

	/**
	 * Constructs this report model.
	 * @param reportBusiness the report business instance for calculating cell values
	 */
	public NackaCompulsoryHighSchoolPlacementReportModel(ReportBusiness reportBusiness) {
		super(reportBusiness);
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#initReportSize()
	 */
	protected void initReportSize() {
		try {
			Collection schools = getReportBusiness().getCompulsoryHighSchools();
			int rowSize = 0;
			rowSize += schools.size() + 1; // Total row
			setReportSize(rowSize, COLUMN_SIZE);
		} catch (RemoteException e) {
			log(e.getMessage());
		}
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#buildRowHeaders()
	 */
	protected Header[] buildRowHeaders() {
		Header[] headers = null;
		
		try {
			ReportBusiness rb = getReportBusiness();
			Collection schools = rb.getCompulsoryHighSchools();
			headers = new Header[schools.size() + 1];
			Iterator iter = schools.iterator();
			int headerIndex = 0;
			while (iter.hasNext()) {
				School school = (School) iter.next();
				headers[headerIndex] = new Header(school.getName(), Header.HEADERTYPE_ROW_NONLOCALIZED_HEADER);
				headerIndex++;
			}
			Header header = new Header(KEY_TOTAL, Header.HEADERTYPE_ROW_TOTAL);
			headers[headerIndex] = header;
		} catch (RemoteException e) {
			log(e.getMessage());
		}
		
		return headers;
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#buildColumnHeaders()
	 */
	protected Header[] buildColumnHeaders() {
		Header[] headers = new Header[3];
		
		Header h = new Header(KEY_SCHOOL_YEAR, Header.HEADERTYPE_COLUMN_HEADER, 4);
		Header child0 = new Header(KEY_SCHOOL_YEAR_1, Header.HEADERTYPE_COLUMN_NORMAL);
		Header child1 = new Header(KEY_SCHOOL_YEAR_2, Header.HEADERTYPE_COLUMN_NORMAL);
		Header child2 = new Header(KEY_SCHOOL_YEAR_3, Header.HEADERTYPE_COLUMN_NORMAL);
		Header child3 = new Header(KEY_SCHOOL_YEAR_4, Header.HEADERTYPE_COLUMN_NORMAL);
		h.setChild(0, child0);
		h.setChild(1, child1);
		h.setChild(2, child2);
		h.setChild(3, child3);
		headers[0] = h;

		h = new Header(KEY_TOTAL, Header.HEADERTYPE_COLUMN_HEADER, 1);
		child0 = new Header(KEY_TOTAL_1_4, Header.HEADERTYPE_COLUMN_NORMAL);
		h.setChild(0, child0);
		headers[1] = h;
		
		h = new Header(KEY_OTHER_COMMUNE_CITIZENS, Header.HEADERTYPE_COLUMN_NORMAL);
		headers[2] = h;
		
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
					columnMethod = COLUMN_METHOD_SCHOOL_YEAR;
					columnParameter = "GS1";
					break;
				case 1:
					columnMethod = COLUMN_METHOD_SCHOOL_YEAR;
					columnParameter = "GS2";
					break;
				case 2:
					columnMethod = COLUMN_METHOD_SCHOOL_YEAR;
					columnParameter = "GS3";
					break;
				case 3:
					columnMethod = COLUMN_METHOD_SCHOOL_YEAR;
					columnParameter = "GS4";
					break;
				case 4:
					columnMethod = COLUMN_METHOD_TOTAL_1_4;
					columnParameter = null;
					break;
				case 5:
					columnMethod = COLUMN_METHOD_OTHER_COMMUNE_CITIZENS;
					columnParameter = null;
					break;
			}

			try {
				ReportBusiness rb = getReportBusiness();
				Collection schools = rb.getCompulsoryHighSchools();
				Iterator iter = schools.iterator();
				while (iter.hasNext()) {
					School school = (School) iter.next();
					Object rowParameter = school.getPrimaryKey();
					Cell cell = new Cell(this, row, column, ROW_METHOD_SCHOOL,
							columnMethod, rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
					setCell(row, column, cell);
					row++;
				}
				Cell cell = new Cell(this, row, column, ROW_METHOD_TOTAL,
						columnMethod, null, columnParameter, Cell.CELLTYPE_TOTAL);
				setCell(row, column, cell);
			} catch (RemoteException e) {
				log(e.getMessage());
			}
		}
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#calculate()
	 */
	protected float calculate(Cell cell) throws RemoteException {
		float value = 0f;
		int schoolId = -1;
		if (cell.getRowParameter() != null) {
			schoolId = ((Integer) cell.getRowParameter()).intValue();
		}
		String schoolYearName = (String) cell.getColumnParameter();
		
		if (cell.getColumnMethod() == COLUMN_METHOD_SCHOOL_YEAR) {
			switch (cell.getRowMethod()) {
				case ROW_METHOD_SCHOOL:
					value = getCompulsoryHighSchoolPlacementCount(schoolId, schoolYearName);
					break;
				case ROW_METHOD_TOTAL:
					int rowIndex = cell.getRow() - 1;
					while (rowIndex >= 0) {
						Cell c = getCell(rowIndex, cell.getColumn());
						if (c.getCellType() == Cell.CELLTYPE_NORMAL) {
							value += c.getFloatValue();
						}
						rowIndex--;
					}
					break;
			}
		} else {
			switch (cell.getColumnMethod()) {
				case COLUMN_METHOD_TOTAL_1_4:
					value = getCell(cell.getRow(), 0).getFloatValue() +
							getCell(cell.getRow(), 1).getFloatValue() +
							getCell(cell.getRow(), 2).getFloatValue();
					break;
				case COLUMN_METHOD_OTHER_COMMUNE_CITIZENS:
					value = getCompulsoryHighSchoolOCCPlacementCount(schoolId);
					break;
			}
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
	protected int getCompulsoryHighSchoolPlacementCount(int schoolId, String schoolYearName) throws RemoteException {
		PreparedQuery query = null;
		ReportBusiness rb = getReportBusiness();
		query = getQuery(QUERY_NACKA_COMMUNE);
		if (query == null) {
			query = new PreparedQuery(getConnection());
			query.setSelectCountDistinctUsers();
			query.setPlacements(rb.getSchoolSeasonId());
			query.setSchoolTypeCompulsoryHighSchool();
			query.setSchool(); // parameter 1
			query.setSchoolYearName(); // parameter 2
			query.prepare();
			setQuery(QUERY_NACKA_COMMUNE, query);
		}
		query.setInt(1, schoolId);
		query.setString(2, schoolYearName);
		return query.execute();
	}
	
	/**
	 * Returns the number of student placements for the specified high school and school year
	 * for citizens in other communes.
	 */
	protected int getCompulsoryHighSchoolOCCPlacementCount(int schoolId) throws RemoteException {
		PreparedQuery query = null;
		ReportBusiness rb = getReportBusiness();
		query = getQuery(QUERY_OTHER_COMMUNES);
		if (query == null) {
			query = new PreparedQuery(getConnection());
			query.setSelectCountDistinctUsers();
			query.setPlacements(rb.getSchoolSeasonId());
			query.setNotNackaCitizens();
			query.setSchoolTypeCompulsoryHighSchool();
			query.setSchool(); // parameter 1
			query.prepare();
			setQuery(QUERY_OTHER_COMMUNES, query);
		}
		query.setInt(1, schoolId);
		return query.execute();
	}
}
