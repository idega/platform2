package com.idega.jmodule.object.interfaceobject;

import com.idega.jmodule.object.ModuleObjectContainer;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.ModuleObject;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.textObject.Link;
import com.idega.jmodule.object.Image;

import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;

/**
 * Title:        idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */

public abstract class AbstractChooser extends ModuleObjectContainer{

  private static final String chooserText = "abstractchooser.buttonText";

  public static final String DEFAULT_CHOOSER_PARAMETER = "iw_chooser_par";
  static final String CHOOSER_SELECTION_PARAMETER="iw_chooser_sel_par";
  public String chooserParameter=DEFAULT_CHOOSER_PARAMETER;

  static final String DISPLAYSTRING_PARAMETER = "chooser_displaystring";
  static final String VALUE_PARAMETER = "chooser_value";

  static final String DISPLAYSTRING_PARAMETER_NAME = "chooser_displaystr_n";
  static final String VALUE_PARAMETER_NAME = "chooser_value_n";

  static final String SCRIPT_PREFIX_PARAMETER="iw_chooser_prefix";
  static final String SCRIPT_SUFFIX_PARAMETER="iw_chooser_suffix";

  private boolean addForm=true;
  private Form form = null;
  private Image buttonImage = null;

  public AbstractChooser() {
  }

  public abstract Class getChooserWindowClass();

  public String getChooserParameter(){
    return chooserParameter;
  }

  public void setChooserParameter(String parameterName){
    this.chooserParameter=parameterName;
  }


  public void main(ModuleInfo modinfo){
    IWBundle bundle = getBundle(modinfo);
    if(addForm){
      form = new Form();
      form.setWindowToOpen(getChooserWindowClass());
      add(form);
      form.add(getTable(modinfo,bundle));
    }
    else{
      add(getTable(modinfo,bundle));
      form = this.getParentForm();
    }

  }

  public ModuleObject getTable(ModuleInfo modinfo,IWBundle bundle){
    Table table = new Table(2,1);
    TextInput input = new TextInput(DISPLAYSTRING_PARAMETER);
    String valueParameterName = "";
    Parameter value = new Parameter(VALUE_PARAMETER,"");
    table.add(value);
    table.add(new Parameter(VALUE_PARAMETER_NAME,value.getName()));
    //GenericButton button = new GenericButton("chooserbutton",bundle.getResourceBundle(modinfo).getLocalizedString(chooserText,"Choose"));
    if(addForm){
      SubmitButton button = new SubmitButton("Choose");
      table.add(button,2,1);
      form.addParameter(CHOOSER_SELECTION_PARAMETER,getChooserParameter());
      form.addParameter(SCRIPT_PREFIX_PARAMETER,"window.opener.document."+form.getID()+".");
      form.addParameter(SCRIPT_SUFFIX_PARAMETER,"value");
    }
    else{
      Link link;
      if( buttonImage == null ) link = new Link("Choose");
      else link = new Link(buttonImage);

      link.setWindowToOpen(getChooserWindowClass());
      link.addParameter(CHOOSER_SELECTION_PARAMETER,getChooserParameter());
      //debug skiiiiiiiiiiiiiiiiiiiitamix getParentForm ekki að virka??
      link.addParameter(SCRIPT_PREFIX_PARAMETER,"window.opener.document."+getParentObject().getParentObject().getID()+".");
      link.addParameter(SCRIPT_SUFFIX_PARAMETER,"value");
      link.addParameter(DISPLAYSTRING_PARAMETER_NAME,input.getName());
      link.addParameter(VALUE_PARAMETER_NAME,value.getName());
      table.add(link,2,1);
    }

    //button.setOnClick("chooser = "+Window.getCallingScriptString(getChooserWindowClass()));
    //button.setOnClick(DISPLAYSTRING_PARAMETER_NAME+" = this.form."+DISPLAYSTRING_PARAMETER_NAME+"");
    //button.setOnClick(VALUE_PARAMETER_NAME+" = this.form."+VALUE_PARAMETER_NAME+"");
    //button.setOnClick("chooser."+DISPLAYSTRING_PARAMETER_NAME+" = "+DISPLAYSTRING_PARAMETER_NAME);
    //button.setOnClick("chooser."+VALUE_PARAMETER_NAME+" = "+VALUE_PARAMETER_NAME);

    table.add(input,1,1);
    table.add(new Parameter(DISPLAYSTRING_PARAMETER_NAME,input.getName()));
    return table;
  }

  public void addForm(boolean addForm){
   this.addForm = addForm;
  }

  public void setChooseButtonImage(Image buttonImage){
   this.buttonImage = buttonImage;
  }
}