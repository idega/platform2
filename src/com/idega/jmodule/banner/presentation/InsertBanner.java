// idega 2000 - Gimmi

package com.idega.jmodule.banner.presentation;

import com.idega.jmodule.banner.data.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.*;
import java.sql.SQLException;



public class InsertBanner extends JModuleObject{

	boolean isAdmin;
	int banner_id;
	Image image;
        private String adminButtonURL = null;


	public InsertBanner(int banner_id) {
		this.banner_id= banner_id;
		this.isAdmin = false;

	}

        /**
         * @deprecated replaced with the banner_id constructor
         */
	public InsertBanner(int banner_id, boolean isAdmin) {
		this.isAdmin = isAdmin;
		this.banner_id = banner_id;
	}

	public void main(ModuleInfo modinfo)throws Exception {
                this.isAdmin=isAdministrator(modinfo);
		Banner banner;
		int ad_id;
		try {
			banner = new Banner(banner_id);
		}
		catch (SQLException s) {
			banner = new Banner(1);
		}
		ad_id = banner.getAdID();
		Ad ad = new Ad(ad_id);
		image = new Image(ad.getImageID(true));
                Link theLink = new Link(image,"/ads/bannerClicked.jsp?ad_id="+ad_id);
                    theLink.setTarget("_blank");
		add(theLink);

		if (isAdmin) {
			Form adForm = new Form(new Window("gimmi",600,430,"/ads/banner.jsp"));
				adForm.setMethod("get");
//				adForm.add(new HiddenInput("banner",Integer.toString(banner_id)));
				adForm.add(new HiddenInput("banner_id",Integer.toString(banner_id)));
				if ( adminButtonURL != null ) {
                                  adForm.add(new SubmitButton(new Image(adminButtonURL)));
				}
                                else {
                                  adForm.add(new SubmitButton("submit","Bannerstjóri"));
                                }
			add(adForm);
		}
	}

        public void setAdminButtonURL(String adminButtonURL){
            this.adminButtonURL=adminButtonURL;
        }
}
