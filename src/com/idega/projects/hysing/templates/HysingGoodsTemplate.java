package com.idega.projects.hysing.templates;

public class HysingGoodsTemplate extends HysingTemplate{

  public void initializePage(){
        HysingPage page = new HysingPage();
        page.setHeaderImageURL("/pics/headers/vorur&thjonusta/V&Th_Topp.jpg");
        page.setMiddleImageURL("/pics/headers/vorur&thjonusta/2v&th1a.gif");
        page.setClickedMenuItem("goods");
        page.setMainBoxHeader("vörur & þjónusta");
        page.setLeftBoxHeader("vörur & þjónusta");
        page.setRightBoxHeader("fylgiskjöl");
        setPage(page);
  }




}
