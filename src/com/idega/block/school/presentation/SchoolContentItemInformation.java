package com.idega.block.school.presentation;

import com.idega.block.text.data.LocalizedText;
import com.idega.data.IDORelationshipException;
import com.idega.presentation.PresentationObject;

/**
 * @author gimmi
 */
public class SchoolContentItemInformation extends SchoolContentItem {
	private String _width = null;
	private String _height = null;

	private boolean _showImages = false;

	/**
	 * @see com.idega.block.school.presentation.SchoolItem#getObject()
	 */
	protected PresentationObject getObject(){
		LocalizedText lText = null;
		try {
			lText = _school.getLocalizedText(_iwc.getCurrentLocaleId());
		} catch (IDORelationshipException e) {
			debug("LocalizedText not found for School");
		}
		
		/*Table table = new Table();
		table.setCellpaddingAndCellspacing(0);
		
		if (lText != null) {
			if (_showImages) {
				SchoolContentItemImage image = new SchoolContentItemImage();
				image.setHorizontalAlignment(Table.HORIZONTAL_ALIGN_RIGHT);
				table.add(image);	
			}
			table.add(getText(lText.getBody()));
		}
		
		setAttributes(table);*/
		
		return getText(lText.getBody());		
	}
	
	/*private void setAttributes(Table table) {
		if (_width != null) {
			table.setWidth(_width);
		}
		if (_height != null) {
			table.setHeight(_height);	
		}
	}
	
	public void setWidth(String width) {
		_width = width;	
	}
	
	public void setHeigth(String height) {
		_height = height;	
	}
	
	public void setShowImages(boolean showImages) {
		_showImages = showImages;
	}*/

}
