package is.idega.idegaweb.golf.block.boxoffice.presentation;

import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.Window;

public class BoxToolbar extends Block{

private String language = "EN";
private String[] Lang = {"Málaflokkar","Undirflokkar","Skjalaflokkar","Nýtt skjal"};
private String[] IS = {"Málaflokkar","Undirflokkar","Skjalaflokkar","Nýtt skjal"};
private String[] EN = {"Issues","Subjects","Filetypes","New Document"};

private Table myTable = new Table(4, 1);


public BoxToolbar(){
  //add(getToolbarTable());
}


public Table getToolbarTable(){

	Window issueWindow = new Window("Gluggi", 420,370, "/boxoffice/boxissue.jsp");
	Window categoryWindow = new Window("Gluggi", 420, 370, "/boxoffice/boxcategory.jsp");
	Window contentWindow = new Window("Gluggi", 420, 370, "/boxoffice/boxcontent.jsp");

	Form issueForm = new Form(issueWindow);
	Form categoryForm = new Form(categoryWindow);
	Form contentForm = new Form(contentWindow);
	Form newNewsForm =  new Form("/boxoffice/boxadmin.jsp");

	Table issueTable = new Table(1,1);
	Table categoryTable = new Table(1, 1);
	Table contentTable = new Table(1, 1);
	Table newNewsTable = new Table(1 ,1);


	issueTable.add(new SubmitButton(Lang[0]), 1, 1);
	categoryTable.add(new SubmitButton(Lang[1]), 1, 1);
	contentTable.add(new SubmitButton(Lang[2]), 1, 1);
	newNewsTable.add(new SubmitButton(Lang[3]), 1, 1);

	issueForm.add(issueTable);
	categoryForm.add(categoryTable);
	contentForm.add(contentTable);
	newNewsForm.add(newNewsTable);

	myTable.add(newNewsForm, 1, 1);
	myTable.add(issueForm, 2, 1);
	myTable.add(categoryForm, 3, 1);
	myTable.add(contentForm, 4, 1);


	myTable.setWidth("100%");
	myTable.setAlignment("center");


	return myTable;
}


private void setSpokenLanguage(IWContext modinfo){
  language = modinfo.getSpokenLanguage();
  if (language.equalsIgnoreCase("IS")){
    Lang = IS;
  }else{
    Lang = EN;
  }
}

public void main(IWContext modinfo){
  setSpokenLanguage(modinfo);
  if(this.isEmpty())
    add(getToolbarTable());
}

/*
public void print(ModuleInfo modinfo)throws IOException{
	initVariables(modinfo);
	if( doPrint(modinfo) ){
		if (getLanguage().equals("HTML")){
				getToolbarTable().print(modinfo);
		}
	}
}*/

}
