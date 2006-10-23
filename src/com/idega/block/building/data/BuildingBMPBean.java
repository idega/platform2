package com.idega.block.building.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.text.data.TextEntityBMPBean;
import com.idega.core.file.data.ICFile;
import com.idega.data.IDOQuery;
import com.idega.data.IDORelationshipException;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;

/**
 * Title: Description: Copyright: Copyright (c) 2001 Company: idega multimedia
 * 
 * @author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */
public class BuildingBMPBean extends TextEntityBMPBean implements Building {

	protected final static String ENTITY_NAME = "bu_building";

	protected static final String COLUMN_SERIE = "serie";

	protected static final String COLUMN_STREET_NUMBER = "street_number";

	protected static final String COLUMN_STREET = "street";

	protected static final String COLUMN_IMAGE = "ic_image_id";

	protected static final String COLUMN_COMPLEX = "bu_complex_id";

	protected static final String COLUMN_INFO = "info";

	protected static final String COLUMN_NAME = "name";

	protected static final String COLUMN_DIVISION = "division";

	protected static final String COLUMN_POSTAL_CODE = "postal_code";

	protected static final String COLUMN_POSTAL_ADDRESS = "postal_address";

	protected static final String COLUMN_LOCKED = "locked";

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_NAME, "Name", String.class);
		addAttribute(COLUMN_INFO, "Info", String.class);
		setMaxLength(COLUMN_INFO, 4000);
		addAttribute(COLUMN_STREET, "Street", String.class);
		addAttribute(COLUMN_STREET_NUMBER, "Streetnumber", Integer.class);
		addAttribute(COLUMN_SERIE, "Serie", String.class, 2);
		addAttribute(COLUMN_DIVISION, "Division", String.class, 2);
		addAttribute(COLUMN_POSTAL_CODE, "Postal code", String.class);
		addAttribute(COLUMN_POSTAL_ADDRESS, "Postal address", String.class);
		addAttribute(COLUMN_LOCKED, "Locked", Boolean.class);

		addManyToOneRelationship(COLUMN_IMAGE, ICFile.class);
		addManyToOneRelationship(COLUMN_COMPLEX, Complex.class);
	}

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public String getName() {
		return getStringColumnValue(COLUMN_NAME);
	}

	public void setName(String name) {
		setColumn(COLUMN_NAME, name);
	}

	public String getInfo() {
		return getStringColumnValue(COLUMN_INFO);
	}

	public void setInfo(String info) {
		setColumn(COLUMN_INFO, info);
	}

	public int getComplexId() {
		return getIntColumnValue(COLUMN_COMPLEX);
	}

	public Complex getComplex() {
		return (Complex) getColumnValue(COLUMN_COMPLEX);
	}

	public void setComplexId(int complex_id) {
		setColumn(COLUMN_COMPLEX, complex_id);
	}

	public int getImageId() {
		return getIntColumnValue(COLUMN_IMAGE);
	}

	public void setImageId(int image_id) {
		setColumn(COLUMN_IMAGE, image_id);
	}

	public void setImageId(Integer image_id) {
		setColumn(COLUMN_IMAGE, image_id);
	}

	public String getStreet() {
		return getStringColumnValue(COLUMN_STREET);
	}

	public void setStreet(String street) {
		setColumn(COLUMN_STREET, street);
	}

	public String getStreetNumber() {
		return getStringColumnValue(COLUMN_STREET_NUMBER);
	}

	public void setStreetNumber(String street_number) {
		setColumn(COLUMN_STREET_NUMBER, street_number);
	}

	public String getSerie() {
		return getStringColumnValue(COLUMN_SERIE);
	}

	public void setSerie(String serie) {
		setColumn(COLUMN_SERIE, serie);
	}

	public String getDivision() {
		return getStringColumnValue(COLUMN_DIVISION);
	}

	public void setDivision(String division) {
		setColumn(COLUMN_DIVISION, division);
	}

	public String getPostalCode() {
		return getStringColumnValue(COLUMN_POSTAL_CODE);
	}

	public void setPostalCode(String postalCode) {
		setColumn(COLUMN_POSTAL_CODE, postalCode);
	}

	public String getPostalAddress() {
		return getStringColumnValue(COLUMN_POSTAL_ADDRESS);
	}

	public void setPostalAddress(String postalAddress) {
		setColumn(COLUMN_POSTAL_ADDRESS, postalAddress);
	}

	public boolean getLocked() {
		return getBooleanColumnValue(COLUMN_LOCKED, false);
	}

	public void setLocked(boolean locked) {
		setColumn(COLUMN_LOCKED, locked);
	}

	public Collection ejbFindAll() throws FinderException {
		return idoFindPKsByQuery(idoQueryGetSelect().appendOrderBy(COLUMN_NAME));
	}

	public Collection ejbFindAllIncludingLocked() throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);
		query.appendWhere();
		query.appendLeftParenthesis();
		query.append(COLUMN_LOCKED);
		query.append(" is null");
		query.appendOr();
		query.appendEquals(COLUMN_LOCKED, false);
		query.appendRightParenthesis();
		query.appendOrderBy(COLUMN_NAME);

		return this.idoFindPKsByQuery(query);
	}

	public Collection ejbFindByComplex(Integer complexID)
			throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);
		query.appendWhereEquals(COLUMN_COMPLEX, complexID);
		query.appendOrderBy(COLUMN_NAME);

		return idoFindPKsByQuery(query);
	}

	public Collection ejbHomeGetImageFilesByComplex(Integer complexID)
			throws FinderException {
		try {
			Table building = new Table(this);
			Table file = new Table(ICFile.class);
			SelectQuery query = new SelectQuery(file);
			query.addColumn(new WildCardColumn(file));
			query.addJoin(building, file);
			query.addCriteria(new MatchCriteria(building, COLUMN_COMPLEX,
					MatchCriteria.EQUALS, complexID.intValue()));
			return idoGetRelatedEntitiesBySQL(ICFile.class, query.toString());
		} catch (IDORelationshipException e) {
			throw new FinderException(e.getMessage());
		}
	}

	public Collection getFloors() {
		try {
			return super.idoGetRelatedEntities(Floor.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error in getFloors() : "
					+ e.getMessage());
		}
	}

	public Collection ejbFindByComplex(Complex complex) throws FinderException {
		return ejbFindByComplex((Integer) complex.getPrimaryKey());
	}
}