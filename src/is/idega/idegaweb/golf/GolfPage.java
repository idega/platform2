package is.idega.idegaweb.golf;

import com.idega.presentation.*;


public class GolfPage extends Page{


	private Table mainTable;
	
	public GolfPage(){
		this("");
	}
	
	public GolfPage(String title){
		super(title);
		
		
		//Page myPage = new Page("Rástímaskráning");
		mainTable = new Table(1,3);
		Table topHeader = new Table(2, 1);
		
		mainTable.add(topHeader,1,1);
		super.add(mainTable);
		
		topHeader.setWidth("100%");
//		topHeader.setBorder(1);
		topHeader.setCellpadding(0);
		topHeader.setCellspacing(0);

		topHeader.setAttribute(1,1,"background","/pics/rastimask/topHeader/topHeader_01.gif");
		topHeader.setAttribute(2,1,"background","/pics/rastimask/topHeader/ToppTiler_02.gif");
		topHeader.setWidth(1, "488");
		topHeader.setHeight("126");
		topHeader.setAlignment(2, 1, "right");
		


		this.setMarginWidth(0);
		this.setMarginHeight(0);
		this.setLeftMargin(0);
		this.setTopMargin(0);
		this.setBackgroundImage("/pics/rastimask/topHeader/topHeader_04.gif");
//		myPage.setBackgroundImage("pics/rastimask/RastimaTafla/Rastimatafla2_05.gif");


		// Stillingar á töflunum mainTable

//		mainTable.setBorder(3);
		mainTable.setAlignment("center");
		mainTable.setAlignment(1,2,"center");
		mainTable.setCellspacing(0);
		mainTable.setCellpadding(0);
		mainTable.setWidth("100%");
		mainTable.setHeight("100%");
		mainTable.setHeight(3,"100");
		mainTable.setColor(1,3,"green");
		mainTable.setHeight(1,"126");

		
		
		
		
		
	
	}
	
	
	public void add(PresentationObject objectToAdd){
		mainTable.add(objectToAdd,1,2);
	}


}
