// idega 2000 - Gimmi

package com.idega.jmodule.banner.presentation;

//import com.idega.projects.golf.entity.*;
import com.idega.jmodule.banner.data.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.*;
import java.sql.SQLException;
//import com.idega.projects.golf.entity.*;



public class BannerContainer extends JModuleObject{
        private String attributeName;
        private int attributeId = -1;
        private boolean isAdmin = false;
        private String URI = "/";

/*	public BannerContainer(boolean isAdmin) {
          this.isAdmin = isAdmin;
	}
*/
	public BannerContainer() {
	}

        public void setConnectionAttributes(String attributeName, int attributeId) {
            this.attributeName = attributeName;
            this.attributeId = attributeId;
        }


        private Table drawContainer(ModuleInfo modinfo, boolean isAdmin) throws SQLException{

            BannerAttributes[] attribs = (BannerAttributes[]) (new BannerAttributes()).findAllByColumn("attribute_name",attributeName,"attribute_id",""+attributeId);
            Ad ad;
            int row = 1;


            Table myTable = new Table();
              myTable.setBorder(0);
              myTable.setCellpadding(0);
              myTable.setCellspacing(0);
              myTable.setWidth("100%");
              myTable.setHeight("100%");

            Link link;
            if (attribs != null) {


                for (int i = 0 ; i < attribs.length ; i++) {
                    ad = new Ad(attribs[i].getAdId());
                      myTable.setAlignment(1,row,"center");
                      myTable.add( (new InsertAd(ad.getID())),1,row);
                      row+=2;

                    if (isAdmin) {
                      link = new Link("Henda",URI);
                      link.addParameter("sub_action","Henda");
                      link.addParameter("action","admin");
                      link.addParameter("banner_attributes_id",""+attribs[i].getID());
                      myTable.add(link,1,row);
                    }
                }


            }
            return myTable;
        }

        private void adminsOnly(ModuleInfo modinfo, boolean useSubAction) throws SQLException{
            boolean isAdmin = true;
            String sub_action = modinfo.getRequest().getParameter("sub_action");
              if (sub_action == null) {
                  sub_action = "";
              }

            if (!useSubAction) {
              sub_action = "";
            }



          if (sub_action.equals("")) {
            Form myForm = new Form();
              Table myTable = new Table();
              myForm.add(myTable);
              myTable.add("Skyrktaraðilastjóri",1,1);
              myTable.add(new SubmitButton("sub_action","Bæta við "),1,2);
              myTable.add(new HiddenInput("action","admin"));

              myTable.add(drawContainer(modinfo,isAdmin),1,3);

            add(myForm);
          }
          else if (sub_action.equals("Bæta við ")) {
              addBanner(modinfo);
          }
          else if (sub_action.equals("Bæta við")) {
              saveAd(modinfo);
          }
          else if (sub_action.equals("Henda")) {
              deleteAd(modinfo);
          }

        }

        private void deleteAd(ModuleInfo modinfo) throws SQLException {
            String b_a_id = modinfo.getRequest().getParameter("banner_attributes_id");

            try {
              BannerAttributes attrib = new BannerAttributes(Integer.parseInt(b_a_id));
                attrib.delete();
            }
            catch (Exception e) {
            }


            adminsOnly(modinfo, false);


        }

        private void saveAd(ModuleInfo modinfo)  throws SQLException {
            String[] ad_id = modinfo.getRequest().getParameterValues("ad_id");

            if (ad_id != null)
            for (int i = 0 ; i < ad_id.length ; i++) {
                try {
                  BannerAttributes attrib = new BannerAttributes();
                    attrib.setAttributeName(attributeName);
                    attrib.setAttributeId(attributeId);
                    attrib.setAdId(Integer.parseInt(ad_id[i]));
                    attrib.insert();
                }
                catch (NumberFormatException n) {
                }

            }

            adminsOnly(modinfo,false);

        }

        private void addBanner(ModuleInfo modinfo) throws SQLException{

            Form myForm = new Form();
            Table myTable = new Table();
              myForm.add(myTable);
            myTable.add("Bæta við auglýsingu",1,1);

            SelectionBox select_box = new SelectionBox("ad_id");
            Ad[] ads = (Ad[])(new Ad()).findAll();
            if (ads != null)
              for (int i = 0 ; i < ads.length ; i++) {
                select_box.addMenuElement(ads[i].getID(),ads[i].getBannerName());
              }

            myTable.add(select_box,1,2);
            myTable.add(new SubmitButton("sub_action","Bæta við"),1,3);
            myTable.add(new HiddenInput("action","admin"));
            add(myForm);

        }


	public void main(ModuleInfo modinfo)throws SQLException, Exception{
            String temp_attributeName = modinfo.getRequest().getParameter("attribute_name");
            String temp_attributeId =  modinfo.getRequest().getParameter("attribute_id");

            if ( (attributeName == null) || (attributeId == -1) ) {
              if ( (temp_attributeName != null) && (temp_attributeId != null) ) {
                  attributeName = temp_attributeName;
                  attributeId = Integer.parseInt(temp_attributeId);
              }
            }


            URI = getRequest().getRequestURI();
            String action = getRequest().getParameter("action");

            if (action == null) {
                action = "";
            }

            if (action.equals("")) {
                if ((attributeName != null) && (attributeId != -1) ) {
                  add(drawContainer(modinfo, false));
                }
            }
            else if (action.equals("admin")) {
                adminsOnly(modinfo,true);
            }

	}
}
