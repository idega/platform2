package com.idega.block.news.presentation;



import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.PresentationObjectContainer;
import com.idega.presentation.Table;



public class NewsTable extends PresentationObjectContainer {



  public static final int SINGLE_FILE_LAYOUT = 1;

  public static final int NEWS_SITE_LAYOUT = 2;

  public static final int NEWS_PAPER_LAYOUT = 3;

  private int iLayout = NEWS_SITE_LAYOUT;

  private int iObjectCount = 0;

  private int iUndividedCount = 1;

  private int iDividedColumnCount = 2;

  private int iPlannedObjectCount = 1;



  private int linejump = 1;

  protected int tableRows;

  protected int tableColumns;

  protected int rowToAddIn;

  protected int colToAddIn;

	private int cellPadding = 0;

	private int cellSpacing = 0;

	private String firstColor = null;

	private String secondColor = null;

	private String color = "";



  private String sAlign = "left";

	boolean zebracolored = false,usecolor = false;;



  private Table table = null;



  public NewsTable(){

    this.iLayout = NEWS_SITE_LAYOUT;

  }



  public NewsTable(int iLayout){

    this.iLayout = iLayout ;

  }



  public NewsTable(int iLayout,int iNumberOfObjects){

    this.iLayout = iLayout ;

    this.iPlannedObjectCount = iNumberOfObjects;

  }



	public NewsTable(int iLayout,int cellPadding,int cellSpacing,String firstColor,String secondColor){

    this.iLayout = iLayout ;

		this.cellPadding = cellPadding;

		this.cellSpacing = cellSpacing;

		this.firstColor = firstColor;

		this.secondColor = secondColor;

		if(firstColor != null ){

			if(secondColor != null) {
				this.zebracolored =true;
			}



			this.color = firstColor;

			this.usecolor = true;

		}



  }



  private void init(){

    this.tableRows = 1;

    this.tableColumns = 1;

    this.rowToAddIn = 1;

    this.colToAddIn = 1;

    if(this.iLayout == NEWS_SITE_LAYOUT){

      int rows = 1;

      // calculate rows needed

      if(this.iPlannedObjectCount > this.iDividedColumnCount ){

        int left = this.iPlannedObjectCount-this.iUndividedCount;

        rows = this.iUndividedCount + (left/this.iDividedColumnCount);

        if((left%this.iDividedColumnCount)>0) {
			rows++;
		}

      }

      this.table = new Table(this.iDividedColumnCount,rows);

    }

    else{

      this.table = new Table(1,this.iPlannedObjectCount);

    }

    this.table.setWidth("100%");

		this.table.setCellpadding(this.cellPadding);

		this.table.setCellspacing(this.cellSpacing);

    this.table.setResizable(true);

    //table.setBorder(1);

  }



  // Stilla t�flu vegna ��kve�innar st�r�ar

  private void finite(){

    if(this.table != null){

      for (int i = 1; i <= this.table.getColumns(); i++) {

        int percent = 100/this.iDividedColumnCount ;

        this.table.setWidth(i,percent+"%");

        this.table.setColumnVerticalAlignment(i,"top");

      }

    }

  }



  public void add(PresentationObject Mo,boolean useSetDivison,String sAlign){

    if(this.table == null) {
		init();
	}



    if(useSetDivison && this.iLayout == NEWS_SITE_LAYOUT){

      if(this.iObjectCount < this.iUndividedCount){

        this.table.mergeCells(1,this.rowToAddIn ,2,this.rowToAddIn );

        this.table.add(Mo,this.colToAddIn,this.rowToAddIn);

        this.table.setVerticalAlignment(this.colToAddIn,this.rowToAddIn,"top");

				if(this.usecolor) {
					this.table.setColor(this.colToAddIn,this.rowToAddIn,this.color);
				}

        this.iObjectCount++;

        this.rowToAddIn++;

      }

      else if(this.colToAddIn < this.iDividedColumnCount){

        this.table.add(Mo,this.colToAddIn,this.rowToAddIn);

        this.table.setVerticalAlignment(this.colToAddIn,this.rowToAddIn,"top");

				if(this.usecolor) {
					this.table.setColor(this.colToAddIn,this.rowToAddIn,this.color);
				}

        this.colToAddIn++;

        this.iObjectCount++;

      }

      else{

        this.table.add(Mo,this.colToAddIn,this.rowToAddIn);

        this.table.setVerticalAlignment(this.colToAddIn,this.rowToAddIn,"top");

				if(this.usecolor) {
					this.table.setColor(this.colToAddIn,this.rowToAddIn,this.color);
				}

        this.colToAddIn--;

        this.rowToAddIn++;

        this.iObjectCount++;

      }

    }

    else{

      if(this.colToAddIn <= this.iDividedColumnCount  && this.colToAddIn > this.iUndividedCount){

        this.rowToAddIn++;

      }

      this.colToAddIn = 1;

      this.table.mergeCells(1,this.rowToAddIn ,2,this.rowToAddIn );

      this.table.setAlignment(1,this.rowToAddIn,sAlign);

			if(this.usecolor) {
				this.table.setColor(this.colToAddIn,this.rowToAddIn,this.color);
			}

      this.table.add(Mo,1,this.rowToAddIn);

      this.rowToAddIn++;

      this.iObjectCount++;

    }



		if(this.zebracolored){

		  if(this.color.equals(this.firstColor)) {
			this.color = this.secondColor;
		}
		else {
			this.color = this.firstColor;
		}

		}



  }



  public void add(PresentationObject Mo){

   add(Mo,false,this.sAlign);

  }



  public void add(PresentationObject Mo,String sAlign){

   add(Mo,false,sAlign);

  }



  public void main(IWContext iwc){

    finite();

    super.add(this.table);

  }



} // Class ListTable
