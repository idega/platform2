//idega 2001 - Tryggvi Larusson
/*
*Copyright 2001 idega.is All Rights Reserved.
*/

package com.idega.jmodule.object;

//import com.idega.jmodule.*;
import java.io.*;
import java.util.List;
import java.util.Iterator;


/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.2
*UNIMPLEMENTED
*/
public class FrameSet extends Page{

  private int alignment;
  private int numberOfFrames=0;

  private static final int ALIGNMENT_VERTICAL=1;
  private static final int ALIGNMENT_HORIZONTAL=2;
  private static final String COLS_PROPERTY="cols";

  private static final String star = "*";
  private static final String PERCENTSIGN = "%";

  public FrameSet(){
    //add(new Page("Flott"));
    //add(new Page("Blank"));
    setFrameBorder(0);
    setBorder(0);
    setFrameSpacing(0);
    setVertical();
  }

  public Page getFirstFrame(){
    return (Page)this.getAllContainingObjects().get(0);
  }


  /**
   * adds the Object to the First Page;
   */
  public void add(ModuleObject obj){
    if(obj instanceof Page){
      add((Page)obj);
    }
    else{
       getFirstFrame().add(obj);
    }
  }


  public void add(Page page){
    super.add(numberOfFrames++,page);
    setFrameName(numberOfFrames,page.getID());
    setAllMargins(numberOfFrames,0);
  }

  /**
   * adds the Object to the First Page;
   */
  public void add(String string){
    getFirstFrame().add(string);
  }




    public void _main(ModuleInfo modinfo) throws Exception {
      //if (!goneThroughMain) {
        initVariables(modinfo);
      //}
      main(modinfo);
    }

    protected void setFrameSetProperty(String name,String value){
      setAttribute(name,value);
    }

    protected String getFrameSetPropertiesString(){
      return getAttributeString();
    }

    public void main(ModuleInfo modinfo)throws Exception{
      setSpanAttribute();

      List list = this.getAllContainingObjects();
      if(list!=null){
        Iterator iter = list.iterator();
        int i = 0;
        while (iter.hasNext()) {
          i++;
          Page item = (Page)iter.next();
          if(getFrameSource(i)==null){
            setFrameSource(i,getFrameURI(item,modinfo));
          }
          //item.empty();
          this.storePage(item,modinfo);
        }
      }
    }

    public void print(ModuleInfo modinfo) throws Exception{
      //goneThroughMain = false;
      initVariables(modinfo);
      StringBuffer buf = new StringBuffer();
      buf.append(getStartTag());
      buf.append(getMetaInformation(modinfo));
      buf.append("<title>"+getTitle()+"</title>");
      buf.append("\n<frameset ");
      buf.append(getFrameSetPropertiesString());
      buf.append(" >\n");

      List list = this.getAllContainingObjects();
      if(list!=null){
        int counter = 1;
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
          Page item = (Page)iter.next();
          buf.append("<frame ");
          buf.append(getFramePropertiesString(counter));
          counter++;
          //buf.append("<frame name=\"");
          //buf.append(item.getID());
          //buf.append("\" src=\"");
          //buf.append(getFrameURI(item,modinfo));
          buf.append("\" >\n");
        }
      }

      buf.append("\n</frameset>\n");
      buf.append(getEndTag());
      print(buf.toString());
    }

    private String getFrameURI(Page page,ModuleInfo modinfo){
      String uri = modinfo.getRequestURI()+"?"+this.IW_FRAME_STORAGE_PARMETER+"="+page.getID();
      return uri;
    }

    public void setFrameBorder(int width){
      setFrameSetProperty("frameborder",Integer.toString(width));
    }

    public void setBorder(int width){
      setFrameSetProperty("border",Integer.toString(width));
    }

    public void setFrameSpacing(int width){
      setFrameSetProperty("framespacing",Integer.toString(width));
    }

    public void setVertical(){
      alignment=ALIGNMENT_VERTICAL;
    };

    public void setHorizontal(){
      alignment=ALIGNMENT_HORIZONTAL;
    };

    /**
     * Sets the span (in percent) for each of the Frame Objects. frameIndex starts at 1.
     */
    public void setSpanPercent(int frameIndex,int percent){
      setFrameProperty(frameIndex,ROWS_PROPERTY,Integer.toString(percent)+PERCENTSIGN);
    }

    /**
     * Sets the span (in pixels) for each of the Frame Objects. frameIndex starts at 1.
     */
    public void setSpanPixels(int frameIndex,int pixels){
      setFrameProperty(frameIndex,ROWS_PROPERTY,Integer.toString(pixels));
    }

    private String getSpan(int frameIndex){
      String frameProperty = getFrameProperty(ROWS_PROPERTY);
      if(frameProperty==null){
        frameProperty=star;
      }
      return frameProperty;
    }

    public void setNoresize(int frameIndex,boolean ifResize){
      if(ifResize){
        setFrameProperty(frameIndex,"noresize");
      }
    }

    public void setBorder(int frameIndex,int borderWidth){
        setFrameProperty(frameIndex,"border",Integer.toString(borderWidth));
    }

    public void setBorder(int frameIndex,boolean ifBorder){
      if(ifBorder){
        setFrameProperty(frameIndex,"border","yes");
      }
      else{
        setFrameProperty(frameIndex,"border","no");
      }
    }

    public void setScrollingAuto(int frameIndex){
        setFrameProperty(frameIndex,"scrolling","auto");
    }


    public void setMarginWidth(int frameIndex,int width) {
      setFrameProperty(frameIndex,"marginwidth",Integer.toString(width));
      getPage(frameIndex).setMarginWidth(width);
    }

    public void setMarginHeight(int frameIndex,int height) {
      setFrameProperty(frameIndex,"marginheight",Integer.toString(height));
      getPage(frameIndex).setMarginHeight(height);
    }

    public void setLeftMargin(int frameIndex,int leftmargin) {
      setFrameProperty(frameIndex,"leftmargin",Integer.toString(leftmargin));
      getPage(frameIndex).setMarginWidth(leftmargin);
    }

    public void setTopMargin(int frameIndex,int topmargin) {
      setFrameProperty(frameIndex,"topmargin",Integer.toString(topmargin));
      getPage(frameIndex).setTopMargin(topmargin);
    }

    public void setAllMargins(int frameIndex,int allMargins) {
      setMarginWidth(frameIndex,allMargins);
      setMarginHeight(frameIndex,allMargins);
      setLeftMargin(frameIndex,allMargins);
      setTopMargin(frameIndex,allMargins);
    }

    public void setFrameName(int frameIndex,String name){
      setFrameProperty(frameIndex,"name",name);
    }

    public void setFrameSource(int frameIndex,String URL){
      setFrameProperty(frameIndex,"src",URL);
    }

    public String getFrameSource(int frameIndex){
      return getFrameProperty(frameIndex,"src");
    }

    protected void setFrameProperty(int frameIndex,String propertyName,String propertyValue){
      getPage(frameIndex).setFrameProperty(propertyName,propertyValue);
    }

    protected void setFrameProperty(int frameIndex,String propertyName){
      getPage(frameIndex).setFrameProperty(propertyName);
    }

    protected String getFrameProperty(int frameIndex,String propertyName){
      return getPage(frameIndex).getFrameProperty(propertyName);
    }

    protected String getFramePropertiesString(int frameIndex){
      return getPage(frameIndex).getFramePropertiesString();
    }

    public Page getPage(int frameIndex){
      return (Page)this.getAllContainingObjects().get(frameIndex-1);
    }

    public void setSpanAttribute(){
      setSpan();

      String property = "";
      String comma = ",";
      int i;
      for (i = 1; i < numberOfFrames; i++) {
        property += getSpan(i);
        property += comma;
      }
      property += getSpan(i);
      String propertyName;
      if(isVertical()){
        propertyName = ROWS_PROPERTY;
      }
      else{
        propertyName = COLS_PROPERTY;
      }
      setFrameSetProperty(propertyName,property);
    }

    public boolean isVertical(){
      if(ALIGNMENT_VERTICAL==alignment){
        return true;
      }
      return false;
    }

    public boolean isHorizontal(){
      if(ALIGNMENT_HORIZONTAL==alignment){
        return true;
      }
      return false;
    }

    public void setSpan(){

      boolean nothingset=true;

      for (int i = 1; i <= numberOfFrames ; i++) {

        String span= getSpan(i);
        if(!span.equals(star)){
          nothingset=false;
          System.out.println("YES");
        }
      }

      if(nothingset){
        int thePercent = (int)(100/numberOfFrames);
        for (int i = 1; i <= numberOfFrames ; i++) {
            setSpanPercent(i,thePercent);
        }

      }

    }

}//End class
