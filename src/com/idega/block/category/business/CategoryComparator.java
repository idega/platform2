package com.idega.block.category.business;
import java.util.Comparator;
import com.idega.block.category.data.ICCategory;
import com.idega.block.category.presentation.CategoryBlock;
import com.idega.util.IsCollator;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public class CategoryComparator implements Comparator {
	private String blockId = null;
	boolean orderManually = false;
	
	public CategoryComparator(CategoryBlock block) {
		orderManually = block.getOrderManually();
		if(orderManually) {
			blockId = Integer.toString(block.getICObjectInstanceID());
		}
	}
	
	public int compare(Object o1, Object o2) {
		ICCategory p1 = (ICCategory) o1;
		ICCategory p2 = (ICCategory) o2;
		
		if(orderManually) {
			try {
				int n1 = CategoryFinder.getInstance().getCategoryOrderNumber(p1, blockId);
				int n2 = CategoryFinder.getInstance().getCategoryOrderNumber(p2, blockId);
				if(n1!=n2) {
					return n1-n2;
				}
			} catch(Exception e) {
				System.out.println("Error comparing category order by number");
				e.printStackTrace();
			}
		}
		
		String one = p1.getName();
		String two = p2.getName();
		
		return IsCollator.getIsCollator().compare(one, two);
	}
	
	public boolean equals(Object obj) {
		return true;
	}
}
