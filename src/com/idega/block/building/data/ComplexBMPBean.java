package com.idega.block.building.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.text.data.TextEntityBMPBean;
import com.idega.core.builder.data.ICPage;
import com.idega.data.IDOQuery;

/**
 * Title: Description: Copyright: Copyright (c) 2001 Company: idega multimedia
 * 
 * @author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */
public class ComplexBMPBean extends TextEntityBMPBean implements Complex {

	protected static final String ENTITY_NAME = "bu_complex";

	protected static final String COLUMN_NAME = "name";

	protected static final String COLUMN_INFO = "info";

	protected static final String COLUMN_IMAGE = "ic_image_id";

	protected static final String COLUMN_FLASH_PAGE = "flash_page_id";

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_NAME, "Name", String.class);
		addAttribute(COLUMN_INFO, "Info", String.class, 4000);
		addAttribute(COLUMN_IMAGE, "Map", Integer.class);
		addManyToOneRelationship(COLUMN_FLASH_PAGE, ICPage.class);
	}

	public String getEntityName() {
		return ENTITY_NAME;
	}

	// getters
	public String getName() {
		return getStringColumnValue(COLUMN_NAME);
	}

	public String getInfo() {
		return getStringColumnValue(COLUMN_INFO);
	}

	public int getImageId() {
		return getIntColumnValue(COLUMN_IMAGE);
	}

	public int getFlashPageID() {
		return getIntColumnValue(COLUMN_FLASH_PAGE);
	}
	
	public ICPage getFlashPage() {
		return (ICPage) getColumnValue(COLUMN_FLASH_PAGE);
	}
	
	// setters
	public void setName(String name) {
		setColumn(COLUMN_NAME, name);
	}

	public void setInfo(String info) {
		setColumn(COLUMN_INFO, info);
	}

	public void setImageId(int image_id) {
		setColumn(COLUMN_IMAGE, image_id);
	}

	public void setImageId(Integer image_id) {
		setColumn(COLUMN_IMAGE, image_id);
	}
	
	public void setFlashPageID(int pageID) {
		setColumn(COLUMN_FLASH_PAGE, pageID);
	}
	
	public void setFlashPage(ICPage page) {
		setColumn(COLUMN_FLASH_PAGE, page);
	}

	// ejb
	public Collection ejbFindAll() throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);
		query.appendOrderBy(COLUMN_NAME);

		System.out.println("query = " + query.toString());
		
		return this.idoFindPKsByQuery(query);
	}

	public Collection getBuildings() {
		try {
			return super.idoGetRelatedEntities(Building.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error in getBuildings() : "
					+ e.getMessage());
		}
	}
}