package is.idega.demo.textviewer.presentation;

import com.idega.presentation.text.Text;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      Idega hf
 * @author <a href="mail:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */

public class ScrambledText extends Text {

  private static String IW_BUNDLE_IDENTIFIER="is.idega.demo.textviewer";
  private boolean scambled=false;
  private String setText;
  private String scrambledText;

  public ScrambledText() {
  }

  public String getBundleIdentifier(){
      return this.IW_BUNDLE_IDENTIFIER;
  }

  public boolean isScrambled(){
    return this.scambled;
  }

  public void setScrambled(boolean isScrambled){
    this.scambled=isScrambled;
    if(scambled){
      if(setText!=null){
        setText(setText);
      }
    }
  }

  public void setText(String text){
    if(text!=null){
      this.setText=text;
      if(this.isScrambled()){
        this.scrambledText=this.getScrambledText(text);
        super.setText(scrambledText);
      }
      else{
        this.scrambledText=text;
        super.setText(scrambledText);
      }
    }
  }

  protected String getScrambledText(String text){
    StringBuffer sbuf = new StringBuffer();
    if(text!=null){
      for (int i = 0; i < text.length(); i++) {
        char c = text.charAt(i);
        if (Character.isLetter(c)) {
          int intval = (int)c;
          intval++;
          c=(char)intval;
        }
        sbuf.append(c);
      }
      return sbuf.toString();
    }
    else{
      return null;
    }
  }

  public String getText(){
    if(this.isScrambled()){
      if(scrambledText!=null)
        return this.scrambledText;
      else
        return "";
    }
    else{
      if(setText!=null)
        return this.setText;
      else
        return "";
    }
  }

/*
  public void print(IWContext iwc)throws Exception{
    if(iwc.getLanguage().equals("HTML")){
      println("<pre>"+getText()+"</pre>");
    }
    else{
      println(getText());
    }
  }
*/
}