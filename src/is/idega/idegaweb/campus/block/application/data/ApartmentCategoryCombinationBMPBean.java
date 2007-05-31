package is.idega.idegaweb.campus.block.application.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.building.data.ApartmentCategory;
import com.idega.data.GenericEntity;

public class ApartmentCategoryCombinationBMPBean extends GenericEntity
		implements ApartmentCategoryCombination {

	private static final String ENTITY_NAME = "cam_category_combination";
	
	private static final String COLUMN_CATEGORY1 = "category1";
	
	private static final String COLUMN_CATEGORY2 = "category2";
	
	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addManyToOneRelationship(COLUMN_CATEGORY1, ApartmentCategory.class);
		addManyToOneRelationship(COLUMN_CATEGORY2, ApartmentCategory.class);
	}

	//getters
	public ApartmentCategory getCategory1() {
		return (ApartmentCategory) getColumnValue(COLUMN_CATEGORY1);
	}
	
	public ApartmentCategory getCategory2() {
		return (ApartmentCategory) getColumnValue(COLUMN_CATEGORY2);
	}

	//setters
	public void setCategory1(ApartmentCategory category) {
		setColumn(COLUMN_CATEGORY1, category);
	}
	
	public void setCategory2(ApartmentCategory category) {
		setColumn(COLUMN_CATEGORY2, category);
	}

	//ejb
	public Collection ejbFindAll() throws FinderException {
		return super.idoFindAllIDsBySQL();
	}	
}