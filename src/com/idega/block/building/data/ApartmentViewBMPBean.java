/*
 * Created on Jan 24, 2004
 *
 */
package com.idega.block.building.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericView;

/**
 * ApartmentViewBMPBean
 * @author aron 
 * @version 1.0
 */
public class ApartmentViewBMPBean extends GenericView implements ApartmentView{
	
	protected static final String APARTMENT_VIEW = "V_APARTMENTS";
	protected static final String COMPLEX_ID ="COMPLEX_ID";
	protected static final String COMPLEX_NAME = "COMPLEX_KEY";
	protected static final String BUILDING_ID = "BUILDING_ID";
	protected static final String BUILDING_NAME ="BUILDING_NAME";
	protected static final String FLOOR_ID ="FLOOR_ID";
	protected static final String FLOOR_NAME ="FLOOR_NAME";
	protected static final String APARTMENT_ID = "APARTMENT_ID";
	protected static final String APARTMENT_NAME ="APARTMENT_NAME";
	protected static final String TYPE_ID = "TYPE_ID";
	protected static final String TYPE_NAME = "TYPE_NAME";
	protected static final String CATEGORY_ID ="CATEGORY_ID";
	protected static final String CATEGORY_NAME ="CATEGORY_NAME";
	protected static final String COLUMN_SUBCATEGORY = "subcategory_id";
	
	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	public String getEntityName() {
		return APARTMENT_VIEW;
	}
	
	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#getIDColumnName()
	 */
	public String getIDColumnName() {
		return APARTMENT_ID;
	}
	
	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(COMPLEX_ID,"Complex id",true,true,Integer.class,ONE_TO_ONE,Complex.class);
		addAttribute(COMPLEX_NAME,"Complex name",String.class);
		addAttribute(BUILDING_ID,"Building id",true,true,Integer.class,ONE_TO_ONE,Building.class);
		addAttribute(BUILDING_NAME,"Building name",String.class);
		addAttribute(FLOOR_ID,"Floor id",true,true,Integer.class,ONE_TO_ONE,Floor.class);
		addAttribute(FLOOR_NAME,"Floor name",String.class);
		addAttribute(APARTMENT_ID,"Apartment id",true,true,Integer.class,ONE_TO_ONE,Apartment.class);
		addAttribute(APARTMENT_NAME,"Apartment name",String.class);
		addAttribute(CATEGORY_ID,"Category id",true,true,Integer.class,ONE_TO_ONE,ApartmentCategory.class);
		addAttribute(CATEGORY_NAME,"Category name",String.class);
		addAttribute(TYPE_ID,"Type id",true,true,Integer.class,ONE_TO_ONE,ApartmentType.class);
		addAttribute(TYPE_NAME,"Type name",String.class);
		addManyToOneRelationship(COLUMN_SUBCATEGORY, ApartmentSubcategory.class);
		setAsPrimaryKey(APARTMENT_ID,true);
	}
	/* (non-Javadoc)
	 * @see com.idega.data.IDOView#getCreationSQL()
	 */
	public String getCreationSQL() {
		StringBuffer sql =new StringBuffer();
		sql.append(" CREATE VIEW ").append(getViewName()).append(" ( ");
		sql.append(COMPLEX_ID).append(", ");
		sql.append(COMPLEX_NAME).append(", ");
		sql.append(BUILDING_ID).append(", ");
		sql.append(BUILDING_NAME).append(", ");
		sql.append(FLOOR_ID).append(", ");
		sql.append(FLOOR_NAME).append(", ");
		sql.append(APARTMENT_ID).append(", ");
		sql.append(APARTMENT_NAME).append(", ");
		sql.append(CATEGORY_ID).append(", ");
		sql.append(CATEGORY_NAME).append(", ");
		sql.append(TYPE_ID).append(", ");
		sql.append(TYPE_NAME).append(", ");
		sql.append(COLUMN_SUBCATEGORY);
		sql.append("  ) AS ");
		sql.append(" select c.bu_complex_id,c.name,b.bu_building_id, b.name,f.bu_floor_id,f.name,a.bu_apartment_id,a.name, y.bu_aprt_cat_id,y.name, t.bu_aprt_type_id,t.name, s.bu_aprt_sub_cat_id ");
		sql.append(" from bu_complex c, bu_building b, bu_floor f, bu_apartment a, bu_aprt_cat y, bu_aprt_type t, bu_aprt_sub_cat s ");
		sql.append(" where c.bu_complex_id = b.bu_complex_id ");
		sql.append(" and b.bu_building_id = f.bu_building_id ");
		sql.append(" and f.bu_floor_id = a.bu_floor_id ");
		sql.append(" and a.bu_aprt_type_id = t.bu_aprt_type_id ");
		sql.append(" and t.bu_aprt_subcat = s.bu_aprt_sub_cat_id ");
		sql.append(" and s.aprt_cat = y.bu_aprt_cat_id ");
		return sql.toString();
	}
	
	public Integer getComplexID(){
		return getIntegerColumnValue(COMPLEX_ID);
	}
	
	public Complex getComplex(){
		return (Complex)getColumnValue(COMPLEX_ID);
	}
	
	public Integer getBuildingID(){
		return getIntegerColumnValue(BUILDING_ID);
	}
	
	public Building getBuilding(){
		return (Building)getColumnValue(BUILDING_ID);
	}
	
	public Integer getFloorID(){
		return getIntegerColumnValue(FLOOR_ID);
	}
	
	public Floor getFloor(){
		return (Floor)getColumnValue(FLOOR_ID);
	}
	
	public Integer getApartmentID(){
		return getIntegerColumnValue(APARTMENT_ID);
	}
	
	public Apartment getApartment(){
		return (Apartment)super.getColumnValue(APARTMENT_ID);
	}
	
	public Integer getCategoryID(){
		return getIntegerColumnValue(CATEGORY_ID);
	}
	
	public ApartmentCategory getCategory(){
		return (ApartmentCategory)getColumnValue(CATEGORY_ID);
	}
	
	public Integer getTypeID(){
		return getIntegerColumnValue(TYPE_ID);
	}
	
	public ApartmentType getType(){
		return (ApartmentType)getColumnValue(TYPE_ID);
	}
	
	public String getComplexName(){
		return getStringColumnValue(COMPLEX_NAME);
	}
	public String getBuildingName(){
		return getStringColumnValue(BUILDING_NAME);
	}
	public String getFloorName(){
		return getStringColumnValue(FLOOR_NAME);
	}
	public String getApartmentName(){
		return getStringColumnValue(APARTMENT_NAME);
	}
	public String getCategoryName(){
		return getStringColumnValue(CATEGORY_NAME);
	}
	public String getTypeName(){
		return getStringColumnValue(TYPE_NAME);
	}
	
	public ApartmentSubcategory getSubcategory() {
		return (ApartmentSubcategory) getColumnValue(COLUMN_SUBCATEGORY);
	}
	
	public int getSubcategoryID() {
		return getIntColumnValue(COLUMN_SUBCATEGORY);
	}
	
	public Collection ejbFindByComplex(Integer complexID)throws FinderException{
		return idoFindPKsByQuery(super.idoQueryGetSelect().appendWhereEquals(COMPLEX_ID,complexID));
	}
	public Collection ejbFindByBuilding(Integer buildingID)throws FinderException{
		return idoFindPKsByQuery(super.idoQueryGetSelect().appendWhereEquals(BUILDING_ID,buildingID));
	}
	public Collection ejbFindByFloor(Integer floorID)throws FinderException{
		return idoFindPKsByQuery(super.idoQueryGetSelect().appendWhereEquals(FLOOR_ID,floorID));
	}
	public Collection ejbFindByCategory(Integer categoryID)throws FinderException{
		return idoFindPKsByQuery(super.idoQueryGetSelect().appendWhereEquals(CATEGORY_ID,categoryID));
	}
	public Collection ejbFindByType(Integer typeID)throws FinderException{
		return idoFindPKsByQuery(super.idoQueryGetSelect().appendWhereEquals(TYPE_ID,typeID));
	}
	/*public Collection ejbFindByTypeAndComplex(Integer typeID,Integer complexID)throws FinderException{
		return idoFindPKsByQuery(idoQueryGetSelect().appendWhereEquals(TYPE_ID,typeID).appendAndEquals(COMPLEX_ID,complexID));
	}*/
	public Collection ejbFindBySubcategory(Integer subcategoryID)throws FinderException{
		return idoFindPKsByQuery(super.idoQueryGetSelect().appendWhereEquals(COLUMN_SUBCATEGORY,subcategoryID));
	}
	
	public String getApartmentString(String delimiter){
		StringBuffer S = new StringBuffer();
		
		S.append(getApartmentName());S.append(delimiter);
		S.append(getFloorName());S.append(delimiter);
		S.append(getBuildingName());S.append(delimiter);
		S.append(getComplexName());
		return S.toString();
	}
	
	public Collection ejbFindByApartmentName(String name)throws FinderException{
		return idoFindPKsByQuery(idoQueryGetSelect().appendWhereEquals(APARTMENT_NAME,name));
	}
	
	public Collection ejbFindAll()throws FinderException{
		return idoFindAllIDsBySQL();
	}
	
	

}
