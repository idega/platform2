package com.idega.block.building.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.core.file.data.ICFile;
import com.idega.data.IDORelationshipException;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */
public class BuildingBMPBean extends com.idega.block.text.data.TextEntityBMPBean  implements com.idega.block.building.data.Building {
	
	protected static final String SERIE = "serie";
	protected static final String STREET_NUMBER = "street_number";
	protected static final String STREET = "street";
	protected static final String IC_IMAGE_ID = "ic_image_id";
	protected static final String BU_COMPLEX_ID = "bu_complex_id";
	protected static final String INFO = "info";
	protected static final String NAME = "name";
	protected static final String BU_BUILDING = "bu_building";
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(NAME, "Name", true, true, java.lang.String.class);
		addAttribute(INFO, "Info", true, true, java.lang.String.class);
		addAttribute(IC_IMAGE_ID, "Photo", true, true, java.lang.Integer.class,MANY_TO_ONE,ICFile.class);
		addAttribute(BU_COMPLEX_ID,"Complex",true,true,java.lang.Integer.class,MANY_TO_ONE,Complex.class);
		addAttribute(STREET, "Street", true, true, java.lang.String.class);
		addAttribute(STREET_NUMBER, "Streetnumber", true, true, java.lang.Integer.class);
		addAttribute(SERIE, "Serie", true, true, java.lang.String.class, 2);
		super.setMaxLength(INFO, 4000);
	}
	public String getEntityName() {
		return BU_BUILDING;
	}
	
	public String getName() {
		return getStringColumnValue(NAME);
	}
	public void setName(String name) {
		setColumn(NAME, name);
	}
	public String getInfo() {
		return getStringColumnValue(INFO);
	}
	public void setInfo(String info) {
		setColumn(INFO, info);
	}
	public int getComplexId() {
		return getIntColumnValue(BU_COMPLEX_ID);
	}
	public Complex getComplex(){
		return (Complex) getColumnValue(BU_COMPLEX_ID);
	}
	public void setComplexId(int complex_id) {
		setColumn(BU_COMPLEX_ID, complex_id);
	}
	public int getImageId() {
		return getIntColumnValue(IC_IMAGE_ID);
	}
	public void setImageId(int image_id) {
		setColumn(IC_IMAGE_ID, image_id);
	}
	public void setImageId(Integer image_id) {
		setColumn(IC_IMAGE_ID, image_id);
	}
	public String getStreet() {
		return getStringColumnValue(STREET);
	}
	public void setStreet(String street) {
		setColumn(STREET, street);
	}
	public String getStreetNumber() {
		return getStringColumnValue(STREET_NUMBER);
	}
	public void setStreetNumber(String street_number) {
		setColumn(STREET_NUMBER, street_number);
	}
	public String getSerie() {
		return getStringColumnValue(SERIE);
	}
	public void setSerie(String serie) {
		setColumn(SERIE, serie);
	}
	
	public Collection ejbFindAll() throws FinderException{
		return idoFindAllIDsBySQL();
	}
	
	public Collection ejbFindByComplex(Integer complexID)throws FinderException{
		Table building =new Table(this);
		SelectQuery query =new SelectQuery(building);
		query.addColumn(new WildCardColumn(building));
		query.addCriteria(new MatchCriteria(building,BU_COMPLEX_ID,MatchCriteria.EQUALS,complexID.intValue()));
		return idoFindPKsBySQL(query.toString());	
	}
	
	public Collection ejbHomeGetImageFilesByComplex(Integer complexID)throws FinderException{
		try {
			Table building =new Table(this);
			Table file = new Table(ICFile.class);
			SelectQuery query =new SelectQuery(file);
			query.addColumn(new WildCardColumn(file));
			query.addJoin(building,file);
			query.addCriteria(new MatchCriteria(building,BU_COMPLEX_ID,MatchCriteria.EQUALS,complexID.intValue()));
			return idoGetRelatedEntitiesBySQL(ICFile.class,query.toString());
		}
		catch (IDORelationshipException e) {
			throw new FinderException(e.getMessage());
		}
	}
	
	public Collection getFloors(){
		try {
			return super.idoGetRelatedEntities(Floor.class);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error in getFloors() : " + e.getMessage());
		}
	}
	
	
	public Collection ejbFindByComplex(Complex complex) throws FinderException{
		return ejbFindByComplex((Integer)complex.getPrimaryKey());
	}
}
