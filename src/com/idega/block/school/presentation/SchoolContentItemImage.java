package com.idega.block.school.presentation;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

import com.idega.core.file.data.ICFile;
import com.idega.data.IDORelationshipException;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;

/**
 * @author gimmi
 */
public class SchoolContentItemImage extends SchoolContentItem {
	private int _maxImageWidth = -1;	
	private int _cellSpacing = 0;
	private String _horizontalAlignment;
	/**
	 * @see com.idega.block.school.presentation.SchoolContentItem#getObject()
	 */
	protected PresentationObject getObject() {
		try {
			Collection images = _school.getImages();
			if (images != null && images.size() > 0) {
				Table table = new Table(1, images.size());
				table.setCellpadding(0);
				table.setCellspacing(_cellSpacing);
				if (_horizontalAlignment != null) {
					table.setHorizontalAlignment(_horizontalAlignment);
				}
				Image image;
				
				ICFile file;
				int rowCounter = 0;
				Iterator iter = images.iterator();
				while (iter.hasNext()) {
					file = (ICFile) iter.next();
					try {
						image = new Image(((Integer)file.getPrimaryKey()).intValue());
						setupImage(image);
						
						++rowCounter;
						table.add(image, 1, rowCounter);
					} catch (SQLException e) {
						e.printStackTrace(System.err);
					}

				}
				return table;
			}
		} catch (IDORelationshipException e) {
			e.printStackTrace(System.err);
		}
		
		return new Table();
	}
	
	private void setupImage(Image image) {
		if (_maxImageWidth > 0 ) {
			image.setMaxImageWidth(_maxImageWidth);
		}
	}
	
	public void setMaxImageWidth(int maxImageWidth) {
		_maxImageWidth = maxImageWidth;	
	}
	
	public void setSpaceBetweenImage(int spaceBetweenImages) {
		_cellSpacing = spaceBetweenImages;	
	}

	public void setHorizontalAlignment(String alignment) {
		_horizontalAlignment = alignment;	
	}
}
