package is.idega.idegaweb.golf.moduleobject;



import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.PresentationObjectContainer;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;





public class GolfDialog extends PresentationObjectContainer{



private String header;

private Table mainTable;

private String headerColor;

private String mainColor;



private String width = "100%";





	public GolfDialog(){

		this("");

	}



	public GolfDialog(String header){

		setHeader(header);

		//form = new Form();

		//super.add(form);





		mainTable = new Table(1,2);

                //mainTable.setBorder(1);

		mainTable.setCellpadding(7);

		mainTable.setAlignment(1,2,"center");

		mainTable.setWidth(width);

		mainTable.setHeight("300");

		mainTable.setHeight(1,"20");

		mainTable.setHeight(2,"280");

		//form.add(mainTable);

                super.add(mainTable);

		//mainTable.mergeCells(1,1,2,1);

		mainTable.add(new Text(getHeader()),1,1);



		setDefaultValues();

	}





	public GolfDialog(Image headerImage){

		//setHeader(header);

		//form = new Form();

		//super.add(form);



		mainTable = new Table(1,2);

                //mainTable.setBorder(1);

		mainTable.setCellpadding(7);

		mainTable.setAlignment(1,2,"center");

		mainTable.setWidth(width);

		mainTable.setHeight("300");

		mainTable.setHeight(1,1,"30");

		mainTable.setHeight(1,2,"270");

		//form.add(mainTable);

                super.add(mainTable);



		//mainTable.setColumnColor(1,"20A000");



		//mainTable.mergeCells(1,1,2,1);

		mainTable.add(headerImage,1,1);



		setDefaultValues();





	}



	private void setDefaultValues(){

		//mainColor="#99CC99";

		//headerColor="#336666";



                //mainTable.setColumnColor(1,mainColor);

		//mainTable.setRowColor(1,headerColor);

	}



	public void setHeader(String header){

		this.header=header;

	}



	public String getHeader(){

		return header;

	}



	public void add(PresentationObject objectToAdd){

		mainTable.add(objectToAdd,1,2);

	}



        public void addMessage(String message){

          add(message);

        }



}

