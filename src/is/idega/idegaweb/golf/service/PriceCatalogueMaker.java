package is.idega.idegaweb.golf.service;

import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.PriceCatalogue;
import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.entity.UnionHome;

import java.io.IOException;
import java.sql.SQLException;
import java.util.StringTokenizer;

import javax.ejb.FinderException;

import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.Window;

/**
*@author <a href="mailto:aron@idega.is">Aron Birkir</a>
*@version 1.0
*/
public class PriceCatalogueMaker extends com.idega.presentation.PresentationObjectContainer {

  private String union_id,unionName,unionAbbrev;
  private int un_id;
  private Union union;
  private String[][] Values;
  private Member thisMember;
  private Member[] unionMembers;
  private PriceCatalogue[] Catalogs;
  private String MenuColor,ItemColor,HeaderColor,LightColor,DarkColor,OtherColor;
  private boolean isAdmin = false;
  private java.util.Locale currentLocale;
  private Link sidan, UpdateLink, PriceLink;
  private Window window;
  private Member[][] mbsArray;
  private Integer[][] totals;
  private int cellspacing = 1, cellpadding = 2;
  private Thread payThread = null;
  private String tablewidth = "308";
  private int  numOfCat, inputLines, saveCount,count,memberCount=0;
  private String extra_catal_action = "";
	private IWResourceBundle iwrb;
	private final static String IW_BUNDLE_IDENTIFIER="com.idega.projects.golf.tariff";


  public PriceCatalogueMaker(){

    HeaderColor = "#336660";
    LightColor = "#CEDFD0";
    DarkColor = "#ADCAB1";
    OtherColor = "#6E9073 ";

    setMenuColor("#ADCAB1");//,"#CEDFD0"
    setItemColor("#CEDFD0");//"#D0F0D0"
    setInputLines(15);
    currentLocale = java.util.Locale.getDefault();
  }

	public String getBundleIdentifier(){
	  return IW_BUNDLE_IDENTIFIER;
	}

  private Table makeMainTable(int menuNr){
    Link PiLink = new Link(new Image("/pics/tarif/pi.gif"),"/tarif/pi.jsp");

    Text HeaderText = new Text("  F\u00e9lagsgj\u00f6ld  "+ unionAbbrev);
    HeaderText.setFontColor("#FFFFFF");
    Table HeaderTable = new Table(1,1);
    HeaderTable.setColor(HeaderColor );
    HeaderTable.setWidth(tablewidth);
    HeaderTable.add(HeaderText,1,1);

    Table MainTable = new Table(1,5);
    MainTable.setCellpadding(0);
    MainTable.setCellspacing(0);
    MainTable.setWidth(tablewidth);

    MainTable.add(makeLinkTable(menuNr),1,1);
    MainTable.add(HeaderTable,1,2);
    //MainTable.add(PiLink,1,5);
    return MainTable;
  }

   private Table makeLinkTable(int menuNr){
    Table LinkTable = new Table(1,1);

    LinkTable.setWidth(tablewidth);
    LinkTable.setCellpadding(0);
    LinkTable.setCellspacing(0);

    Link sidan = new Link(iwrb.getImage(menuNr == 1?("ratelist.gif"):("ratelist1.gif")));//,"/tarif/tarif.jsp");
    sidan.addParameter("extra_catal_action","main");
    sidan.addParameter("union_id",union_id);

    Link UpdateLink = new Link(iwrb.getImage(menuNr == 2?("change.gif"):("change1.gif")));
    UpdateLink.addParameter("extra_catal_action","change");
    UpdateLink.addParameter("union_id",union_id);

    Link ViewLink = new Link(iwrb.getImage(menuNr == 3?("look.gif"):("look1.gif")));
    ViewLink.addParameter("extra_catal_action","view");
    ViewLink.addParameter("union_id",union_id);

    Link SaveLink = new Link(iwrb.getImage(menuNr == 4?("createratelist.gif"):("createratelist1.gif")));
    SaveLink.addParameter("extra_catal_action","save");
    SaveLink.addParameter("union_id",union_id);

    LinkTable.add(sidan,1,1);
    if(isAdmin){
      LinkTable.add(UpdateLink,1,1);
      LinkTable.add(ViewLink,1,1);
      LinkTable.add(SaveLink,1,1);
      LinkTable.add(PriceLink,1,1);
    }
    return LinkTable;
  }

  private Table makeSubTable(){
   int tablelines = 1;
      if(Values != null)
       tablelines += Values.length;
      Table T = new Table(3,tablelines);
      T.setBorder(0);
      T.setWidth(tablewidth);
      T.setColumnAlignment(1, "center");
      T.setColumnAlignment(3, "right");

      T.setHorizontalZebraColored(DarkColor,LightColor);
      T.setCellpadding(cellpadding);
      T.setCellspacing(cellspacing) ;

      T.add("Nr",1,1);
      T.add("L\u00fdsing",2,1);
      T.add("Upph\u00e6\u00f0",3,1);

      if(Values != null){
        java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
        for (int i = 0; i < Values.length; i++) {
          for (int j = 0; j < 3; j++) {
            T.add(Values[i][j],j+1,i+2 );
          }
        }
      }
      return T;
  }

  public void setMenuColor(String MenuColor){
    this.MenuColor = MenuColor;
  }
  public void setItemColor(String ItemColor){
    this.ItemColor = ItemColor;
  }

  public void setInputLines(int inputlines){
    this.inputLines = inputlines;
  }
  private void control(IWContext modinfo) throws IOException{
		iwrb = getResourceBundle(modinfo);
    try{
      if(modinfo.getRequest().getParameter("union_id") != null){
         union_id = modinfo.getRequest().getParameter("union_id");
      }
      else{
         union_id = (String)  modinfo.getSession().getAttribute("golf_union_id");
      }
      un_id = Integer.parseInt(union_id)  ;
      union = ((UnionHome) IDOLookup.getHomeLegacy(Union.class)).findByPrimaryKey(un_id);
      unionName = union.getName();
      unionAbbrev = union.getAbbrevation() ;

      boolean hasSomeValues = false;

      if(modinfo.getRequest().getParameter("extra_catal_action") == null){
        doMain(modinfo);
      }
      if(modinfo.getRequest().getParameter("extra_catal_action") != null){
        extra_catal_action = modinfo.getRequest().getParameter("extra_catal_action");

        if(extra_catal_action.equals("main")){ doMain(modinfo); }
        if(extra_catal_action.equals("change")){ doChange(modinfo); }
        if(extra_catal_action.equals("update")){ doUpdate(modinfo); }
        if(extra_catal_action.equals("view")){ doView(modinfo); }
        if(extra_catal_action.equals("save")){ doSave(modinfo); }
        if(extra_catal_action.equals("list")){ doList(modinfo); }

      }
    }
    catch(SQLException S){S.printStackTrace();}
    catch(FinderException IO){IO.printStackTrace();}
   // catch(Exception E){ E.printStackTrace(); add("<br> villa "+E.toString()  );}
}

    private void doMain(IWContext modinfo) throws SQLException {

      PriceCatalogue[] Catalogs = (PriceCatalogue[]) ((PriceCatalogue) IDOLookup.instanciateEntity(PriceCatalogue.class)).findAll("select * from price_catalogue where union_id = '"+union_id+"' and in_use = 'Y' and is_independent = 'Y'");
      java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
      Table MainTable = makeMainTable(1);
      int count = Catalogs.length;
      Table catalTable = new Table(3,count+1);
      catalTable.setWidth(tablewidth);
      catalTable.setHorizontalZebraColored(DarkColor,LightColor);
      catalTable.setCellpadding(cellpadding);
      catalTable.setCellspacing(cellspacing) ;
      catalTable.setColumnAlignment(3, "right");
      catalTable.add(iwrb.getLocalizedString("nr","Nr"),1,1);
      catalTable.add(iwrb.getLocalizedString("description","Description"),2,1);
      catalTable.add(iwrb.getLocalizedString("amount","Amount"),3,1);
      if(isAdmin){
        if(count > 0){

          for (int i = 0;i < count;i++){
            catalTable.add(String.valueOf(i+1),1,i+2);
            catalTable.add(Catalogs[i].getName(),2,i+2);
            //catalTable.add(String.valueOf(Catalogs[i].getPrice())+" kr",3,i+2);
            catalTable.add(nf.format(Catalogs[i].getPrice())+iwrb.getLocalizedString("currency","Kr"),3,i+2);
          }
          Values = fetchValues(union_id);
          setValues( modinfo , fetchValues(union_id) );
          setValuesCount( modinfo , count );
        }
      }

      MainTable.add(catalTable,1,3);
      MainTable.setWidth(tablewidth);
      add(MainTable);
    }

    private String[][] fetchValues(String unionID) throws SQLException{
   // PriceCatalogue[] Catalogs = (PriceCatalogue[])(new PriceCatalogue()).findAllByColumn("union_id",unionID,"in_use","Y");
      PriceCatalogue[] Catalogs = (PriceCatalogue[]) ((PriceCatalogue) IDOLookup.instanciateEntity(PriceCatalogue.class)).findAll("select * from price_catalogue where union_id = '"+union_id+"' and in_use = 'Y' and is_independent = 'Y'");
      int count = Catalogs.length;
      String activeCats[][] = new String[count][8];
      if(count > 0){
        //String activeCats[][] = new String[count][8];  // used to store in_use, extra_info strings
        StringTokenizer st;
        for (int i = 0;i < count;i++){
        st = new StringTokenizer(Catalogs[i].getExtraInfo(),"#");
            int a =0;
            while ((st.hasMoreTokens()) && a < 8) {
              activeCats[i][a]= st.nextToken();
              a++;
            }
          }

      }
       return activeCats;
    }

    private void doChange(IWContext modinfo) throws SQLException{
      Form myForm = new Form();
      myForm.maintainAllParameters();
      Values = this.getValues(modinfo)  ;
      int count = this.getValuesCount(modinfo);

      Table inputTable =  new Table(3,inputLines+1);
      inputTable.setWidth(tablewidth);
      inputTable.setCellpadding(2);
      inputTable.setCellspacing(1);
      inputTable.setColumnAlignment(1,"right");
      inputTable.setHorizontalZebraColored(DarkColor,LightColor);
			inputTable.add(iwrb.getLocalizedString("nr","Nr"),1,1);
      inputTable.add(iwrb.getLocalizedString("description","Description"),2,1);
      inputTable.add(iwrb.getLocalizedString("amount","Amount"),3,1);


      for (int i = 1; i < inputLines+1 ;i++){
        String rownum = String.valueOf(i);
        String s = "";
        TextInput textInput, priceInput, ageFromInput, ageToInput, membForInput;
        DropdownMenu drpGender, drpExtra;
        String genderValue,extraValue;

        String tmp;

        if(Values != null && i <= Values.length ){
          textInput  = new TextInput("extra_catal_text"+i,(Values[i-1][1].equalsIgnoreCase("null")?"":Values[i-1][1]));
          priceInput = new TextInput("extra_catal_price"+i,(Values[i-1][2].equalsIgnoreCase("null")?"":Values[i-1][2]));
        }
        else{
          textInput  = new TextInput("extra_catal_text"+i);
          priceInput = new TextInput("extra_catal_price"+i);
        }
        textInput.setSize(20);
        priceInput.setSize(4);
        priceInput.setAsIntegers();

        inputTable.add(rownum,1,i+1);
        inputTable.add(textInput,2,i+1);
        inputTable.add(priceInput,3,i+1);
      }
      myForm.add(new HiddenInput("numofcatal", String.valueOf(inputLines) ));
      myForm.add(new HiddenInput("extra_catal_action","update" ));
      myForm.add(inputTable);
      myForm.add(new SubmitButton(iwrb.getImage("update.gif")));

      Table MainTable = makeMainTable(2);
      MainTable.add(myForm,1,3);
      add(MainTable);
    }

    private void doUpdate(IWContext modinfo) throws SQLException{
      int number = Integer.parseInt(modinfo.getRequest().getParameter("numofcatal"));
      int cols = 3;
      boolean hasNull = false;
      if (Values == null)
        Values = new String[number][cols];
      String text;
      String price;

      for (int i = 1; i < number+1 ;i++){
        count = i;
        text = modinfo.getRequest().getParameter("extra_catal_text"+i );
        price = modinfo.getRequest().getParameter("extra_catal_price"+i);

        if(text.equalsIgnoreCase("")){
           text = "null";
           if(!hasNull){
            numOfCat = i-1;
            hasNull = true;
           }
        }

        if(price.equalsIgnoreCase("") ) price = "null";

        Values[i-1][0] = String.valueOf(i);
        Values[i-1][1] = text;
        Values[i-1][2] = price;

      }// for lykkja
      String[][] parsedValues = new String[numOfCat][cols];
      for(int k = 0; k < numOfCat; k++){
        for(int m = 0; m < Values[k].length;m++){
          parsedValues[k][m] = Values[k][m];
        }
      }
      Values = parsedValues;
      this.setValues( modinfo , Values );
      this.setValuesCount(modinfo,numOfCat) ;

      Table MainTable = makeMainTable(3);
      MainTable.setWidth(tablewidth);
      Text T = new Text("<H3>"+iwrb.getLocalizedString("pcm.info","Veldu VISTA til að vista gjaldskrá í gagnagrunn")+"</H3>");
      MainTable.add(makeSubTable(),1,3);
      MainTable.add(T ,1,3);
      add(MainTable);
    }

    private void doView(IWContext modinfo) throws SQLException{
      Values = getValues( modinfo);
      Table MainTable = this.makeMainTable(3);
      MainTable.add(makeSubTable(),1,3);
      add(MainTable);
    }

    private void doSave(IWContext modinfo) throws SQLException{
      Values = this.getValues( modinfo );
      Text messageText;
      if(Values != null){
      	makeAllUnUsable();
      	saveCatalogs();
      	messageText = new Text("<H3>"+iwrb.getLocalizedString("pcm.info2"," Gjaldskrá var vistuð !") +"</H3> ");
      }
      else{
      	messageText = new Text("<H3>"+iwrb.getLocalizedString("pcm.info"," Ekkert vistað ! ") +"</H3> ");
      }
      Table MainTable = makeMainTable(4);
      MainTable.setWidth(tablewidth);
      MainTable.add(messageText,1,3);
      add(MainTable);
    }

    private void doList(IWContext modinfo) throws SQLException{

    }

  private void saveCatalogs()throws SQLException{
    PriceCatalogue pcl;
    for(int i = 0; i < Values.length; i++){
      String text = Values[i][1];
      String price = Values[i][2];
      if(!(text.equalsIgnoreCase("null")) && !(price.equalsIgnoreCase("null"))){
        pcl = (PriceCatalogue) IDOLookup.createLegacy(PriceCatalogue.class);
        pcl.setUnion_id(new Integer(union_id));
        pcl.setName(text);
        pcl.setPrice( Integer.valueOf(price));
        StringBuffer SB = new StringBuffer();
        for(int k = 0; k < Values[i].length ; k++){
          SB.append(Values[i][k]);
          SB.append("#");
        }
        SB.append("null#null#null#null#null#");
        pcl.setExtraInfo(SB.toString()) ;
        pcl.setInUse(true);
        pcl.setIndependent(true);
        pcl.insert();

      }
    }
  }

  private void makeAllUnUsable() throws SQLException{
  //PriceCatalogue[] pcls = (PriceCatalogue[]) (new PriceCatalogue()).findAllByColumn("union_id",union_id,"in_use","Y");
    PriceCatalogue[] pcls= (PriceCatalogue[]) ((PriceCatalogue) IDOLookup.instanciateEntity(PriceCatalogue.class)).findAll("select * from price_catalogue where union_id = '"+union_id+"' and in_use = 'Y' and is_independent = 'Y'");
    if(pcls != null){
    	for(int i = 0; i < pcls.length; i++){
          pcls[i].setInUse(false);
          pcls[i].setIndependent(false);
          pcls[i].update();
    	}
    }
  }

  private void setValues(IWContext modinfo , String[][] values){
    modinfo.getSession().setAttribute("extra_catalog_tarifs", values);
  }
  private String[][] getValues(IWContext modinfo){
    String S[][] = (String[][]) modinfo.getSession().getAttribute("extra_catalog_tarifs");
    return S;
  }

  private void setValuesCount(IWContext modinfo , int count){
    modinfo.getSession().setAttribute("extra_catalog_count", new Integer(count));
  }
  private int getValuesCount(IWContext modinfo){
    if(modinfo.getSession().getAttribute("extra_catalog_count")!= null){
      Integer I = (Integer)modinfo.getSession().getAttribute("extra_catalog_count");
      return I.intValue();
    }
    else return 0;
  }

  public void main(IWContext modinfo) throws IOException {
    //isAdmin = 
    /** @todo: fixa Admin*/
    isAdmin = true;
    control(modinfo);
  }
}// class PriceCatalogueMaker


