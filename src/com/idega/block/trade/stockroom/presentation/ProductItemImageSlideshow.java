package com.idega.block.trade.stockroom.presentation;

import java.util.Collection;
import java.util.Vector;
import com.idega.presentation.IWContext;
import com.idega.presentation.ImageSlideShow;


/**
 * @author gimmi
 */
public class ProductItemImageSlideshow extends ProductItem {

	private int heigth = 0;
	private int width = 0;
	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		drawObject();
	}
	
	private void drawObject() throws Exception {
		if (_product != null) {
			Collection coll = super._product.getICFile();
			if (coll != null && !coll.isEmpty()) {
				ImageSlideShow iss = new ImageSlideShow();
				iss.setFiles(new Vector(coll));

				if (heigth > 0) {
					iss.setHeight(heigth);
				}
				if (width > 0) {
					iss.setWidth(width);
				}
							
				add(iss);
			}
		} else {
			add(_iwrb.getLocalizedString("product.image_slideshow","Image Slideshow"));
		}
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setHeigth(int heigth) {
		this.heigth = heigth;
	}

}
