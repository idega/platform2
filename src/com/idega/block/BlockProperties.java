package com.idega.block;

import com.idega.jmodule.object.*;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class BlockProperties {

  public static String BlockName = null;

  public String[] IS = null;
  public String[] EN = null;
  public String[] DK = null;
  public String[] DEFAULT = null;

  public static String ImagePrefix = "/pics/";
  public static String slash = "/";
 /* Þarf að setja  languageChecked = false í lokin ef klasinn er geymdur
  private String Language;
  private boolean languageChecked;
  private boolean languageSame;

  public boolean isLanguageSame(ModuleInfo modinfo){
    if (!languageChecked)
      checkLanguage(modinfo);

    return languageSame;
  }

  private void checkLanguage(ModuleInfo modinfo){
    String currentLanguage = modinfo.getSpokenLanguage();
    if (currentLanguage.equalsIgnoreCase(language))
      languageSame = true;
    else{
      languageSame = false;
      language = currentLanguage;
    }
  }
*/



  public BlockProperties() {
    DEFAULT = IS;
//    languageChecked = false;
//    languageSame false;
  }

  public void setBlockName(String name){
    BlockName = name;
  }


  /**
   * Returns array of strings needed for the presentation of the module.
   *
   * <p>Chooses the right language by asking the getSpokenLanguage() method in ModuleInfo
   */
  public String[] getStringCollection(ModuleInfo modinfo){
    String language = modinfo.getSpokenLanguage();
    if (language.equals("IS") && IS != null)  // modinfo.isIcelandic()
      return IS;
    if(language.equals("EN") && EN  != null)
      return EN;
    if (language.equals("DK") && DK != null)
      return DK;

    return DEFAULT;
  }

  public void setEnglishAsDefaultLanguage(){
    DEFAULT = EN;
  }

  public void setIclandicAsDefaultLanguage(){
    DEFAULT = IS;
  }

  public void setDanishAsDefaultLanguage(){
    DEFAULT = DK;
  }

  public String[] getEnglishStringCollection(){
    return EN;
  }

  public String[] getIcelandicStringCollection(){
    return IS;
  }

  public String[] getDanishStringCollection(){
    return DK;
  }

  /**
   * Used to generate image url with regard to language
   *
   * <p>extended and used as followes:
   *
   * <p>  String HeaderImageUrl = "?.gif";
   *
   * <p>  public String getHeaderImageUrl(ModuleInfo modinfo){
   * <br>   return getImageUrl(modinfo, HeaderImageName);
   * <br> }
   */

  public String getImageUrl(ModuleInfo modinfo, String imageName){
    StringBuffer toReturn =  new StringBuffer(ImagePrefix);
    toReturn.append(modinfo.getSpokenLanguage());
    toReturn.append(slash);
//    if (BlockName != null){
      toReturn.append(BlockName.toLowerCase());
      toReturn.append(slash);
//    }
    toReturn.append(imageName);

    return toReturn.toString();
  }


  public Image getImage(ModuleInfo modinfo, String imageName, String ImagePublicName){
    return new Image(getImageUrl(modinfo, imageName), ImagePublicName);
  }


}