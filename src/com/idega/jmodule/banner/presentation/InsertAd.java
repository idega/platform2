// idega 2000 - Gimmi

package com.idega.jmodule.banner.presentation;

import com.idega.jmodule.banner.data.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.presentation.*;
import java.sql.SQLException;



public class InsertAd extends Block{

	boolean isAdmin;
	int ad_id;
	Image image;


	public InsertAd(int ad_id) {
		this.ad_id= ad_id;
		this.isAdmin = false;

	}
	public InsertAd(int banner_id, boolean isAdmin) {
		this.isAdmin = isAdmin;
		this.ad_id = ad_id;
	}

	public void main(IWContext iwc)throws SQLException {
                Ad ad;
		try {
			ad = new Ad(ad_id);
		}
		catch (Exception s) {
			ad = new Ad(1);
		}
                if( ad!=null ){
		//image = new Image(ad.getImageID(true));
                  image = new Image(ad.getImageID(false));//skip impressions
                  add(new Link(image,"/ads/bannerClicked.jsp?ad_id="+ad_id));
                }

/*		if (isAdmin) {
			Form adForm = new Form(new Window("gimmi",600,430,"/ads/banner.jsp"));
				adForm.setMethod("get");
//				adForm.add(new HiddenInput("banner",Integer.toString(banner_id)));
				adForm.add(new HiddenInput("ad_id",Integer.toString(ad_id)));
				adForm.add(new SubmitButton("submit","Bannerstjóri"));
			add(adForm);
		}
*/	}
}
