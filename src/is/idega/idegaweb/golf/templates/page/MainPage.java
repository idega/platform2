package is.idega.idegaweb.golf.templates.page;

import com.idega.presentation.IWContext;
import com.idega.presentation.Page;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;

public class MainPage extends Page{


	private Table mainTable;
        private String mainTableWidth;

        private Table contentTable;

        private String PageBackgroundImage;
        private String PageBackgroundColor;

        private String MainHeight;
        private String MainHeightRow1;
        private String MainHeightRow2;
        private String MainHeightRow3;

        private String MainWidth;
        private String Width;
        private String LeftWidth;
        private String CenterWidth;
        private String RightWidth;

        private String MainBackColor;
        private String MainBackColor1;
        private String MainBackColor2;
        private String MainBackColor3;



        // #######  Smiðir  #########

	public MainPage(){
		this("");
	}

	public MainPage(String title){
		super(title);
                // frumstilling a Aðaltöflu
                mainTable = new Table(1,3);
//                mainTable.setBorder(2);

        	super.add(mainTable);
	}



        // #########      private föll       ############

        private void checkSettings(){


                // stillingar a Page objectinu


                if (PageBackgroundImage != null){
                  this.setBackgroundImage(PageBackgroundImage);
                  } else{
                    this.setBackgroundImage("");}
                if (PageBackgroundColor != null){
                  this.setBackgroundColor(PageBackgroundColor);
                  } else{
                    this.setBackgroundColor("#FFFFFF");}






                if (Width != null)
                  contentTable.setWidth( Width );
                if (LeftWidth != null)
                  contentTable.setWidth(1, LeftWidth);
                if (CenterWidth != null)
                  contentTable.setWidth(2, CenterWidth);
                if (RightWidth != null)
                  contentTable.setWidth(3, RightWidth);



		//stillingar a Aðaltöflunni

                if (MainWidth != null)
                  mainTable.setWidth( MainWidth );
                if (mainTableWidth != null){
		  mainTable.setWidth(mainTableWidth);}
		mainTable.setAlignment("center");
		mainTable.setAlignment(1,2,"center");
		mainTable.setVerticalAlignment(1,2,"top");
		mainTable.setCellspacing(0);
		mainTable.setCellpadding(0);

                if (MainHeight != null){
    	          mainTable.setHeight(MainHeight);
                }else{
                  mainTable.setHeight("100%");}
                if (MainHeightRow1 != null){
    	          mainTable.setHeight(1,MainHeightRow1);}       // gæti þurft tomt texta object til að stjorna litilli hæð
                if (MainHeightRow2 != null){
    	          mainTable.setHeight(2,MainHeightRow2);}
                if (MainHeightRow3 != null){
    	          mainTable.setHeight(3,MainHeightRow3);}

                if (MainBackColor != null){
                  mainTable.setColor(MainBackColor);}
                if (MainBackColor1 != null){
                  mainTable.setColor(1,3, MainBackColor1);}
                if (MainBackColor2 != null){
                  mainTable.setColor(1,3, MainBackColor2);}
                if (MainBackColor3 != null){
                  mainTable.setColor(1,3, MainBackColor3);}

        }




        //  #######  public föll  ############

        public void setWidth( String width ){
            MainWidth = width;
        }


        public void setContentWidth( String width ){
            Width = width;
        }

        public void setWidth( int Xpos, String Width ){
          switch (Xpos){
            case 1:
              LeftWidth = Width;
              break;
            case 2:
              CenterWidth = Width;
              break;
            case 3:
              RightWidth = Width;
              break;
          }
        }


        public void setHeight( String height){
          MainHeight = height;
        }

        public void setTopHeight( String height){
          MainHeightRow1 = height;
        }

        public void setMiddleHeight( String height){
          MainHeightRow2 = height;
        }

        public void setBottomHeight( String height){
          MainHeightRow3 = height;
        }



	public void add(PresentationObject objectToAdd){
		mainTable.add(objectToAdd,1,2);
	}

 	public void add(String position, PresentationObject objectToAdd){
                if (position.equals("top"))
		  mainTable.add(objectToAdd,1,1);
                if (position.equals("middle"))
		  mainTable.add(objectToAdd,1,2);


                if (position.equals("bottom"))
		  mainTable.add(objectToAdd,1,3);
	}



	public void add(PresentationObject Left, PresentationObject Center, PresentationObject Right){
                contentTable = new Table(3,1);

                contentTable.setCellpadding(0);
                contentTable.setCellspacing(0);
		contentTable.setVerticalAlignment("top");
                contentTable.setHeight("100%");

                contentTable.add(Left, 1,1);
                contentTable.setAlignment(1,1,"left");
                contentTable.setVerticalAlignment(1,1,"top");
                contentTable.setHeight(1,1,"100%");

                contentTable.add(Center, 2,1);
                contentTable.setAlignment(2,1,"center");
                contentTable.setVerticalAlignment(2,1,"top");
                contentTable.setHeight(2,1,"100%");

                contentTable.add(Right, 3,1);
                contentTable.setAlignment(3,1,"right");
                contentTable.setVerticalAlignment(3,1,"top");
                contentTable.setHeight(3,1,"100%");

		mainTable.add(contentTable,1,2);
	}



	public void add(PresentationObject Left,  PresentationObject Right ){
                contentTable = new Table(2,1);

                contentTable.setCellpadding(0);
                contentTable.setCellspacing(0);

                contentTable.add(Left, 1,1);
                contentTable.add(Right, 2,1);

		mainTable.add(contentTable,1,2);
	}



        public void print(IWContext modinfo)throws Exception{
          checkSettings();
          super.print(modinfo);
        }


} // class MainPage
