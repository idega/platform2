/*
 * Created on 16.4.2004
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
package is.idega.idegaweb.golf.clubs.presentation;

import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.entity.UnionHome;
import is.idega.idegaweb.golf.entity.UnionImage;
import is.idega.idegaweb.golf.presentation.GolfBlock;
import is.idega.idegaweb.golf.presentation.GolfImage;

import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;

/**
 * @author laddi
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class ClubOverview extends GolfBlock {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.presentation.PresentationObject#main(com.idega.presentation.IWContext)
	 */
	public void main(IWContext modinfo) throws Exception {
		String union_id = (String) modinfo.getSessionAttribute("golf_union_id");
		Union union = ((UnionHome) IDOLookup.getHomeLegacy(Union.class)).findByPrimaryKeyLegacy(Integer.parseInt(union_id));

		Text headline1 = new Text("Velkomin á klúbbasíðu ");
		headline1.setFontSize(4);
		headline1.setBold();
		Text headline2 = new Text(union.getAbbrevation());
		headline2.setFontSize(4);
		headline2.setBold();

		Table myTable = new Table(2, 2);
		myTable.setWidth("100%");
		myTable.mergeCells(1, 2, 2, 2);
		myTable.setAlignment(1, 2, "left");

		UnionImage[] union2 = (UnionImage[]) ((UnionImage) IDOLookup.instanciateEntity(UnionImage.class)).findAllByColumn("union_id", union_id);

		if (union2.length > 0) {
			Image unionImage = new GolfImage(union2[0].getImageId());
			unionImage.setMaxImageWidth(50);

			myTable.add(unionImage, 1, 1);

			myTable.add(headline1, 2, 1);
			//myTable.addBreak(2,1);
			myTable.add(headline2, 2, 1);
		}
		else {
			myTable.mergeCells(1, 1, 2, 1);
			myTable.add(headline1, 1, 1);
			//myTable.addBreak(1,1);
			myTable.add(headline2, 1, 1);
		}
		
		add(myTable);
	}

}