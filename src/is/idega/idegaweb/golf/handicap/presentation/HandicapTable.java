/*
 * Created on 3.3.2004
 */
package is.idega.idegaweb.golf.handicap.presentation;

import is.idega.idegaweb.golf.entity.Field;
import is.idega.idegaweb.golf.entity.FieldHome;
import is.idega.idegaweb.golf.entity.Tee;
import is.idega.idegaweb.golf.entity.TeeColor;
import is.idega.idegaweb.golf.entity.TeeColorHome;
import is.idega.idegaweb.golf.presentation.GolfBlock;
import is.idega.idegaweb.golf.templates.page.GolfWindow;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.util.text.TextSoap;

/**
 * @author laddi
 */
public class HandicapTable extends GolfWindow {

  public HandicapTable() {
    setWidth(800);
    setHeight(600);
    setResizable(true);
    setScrollbar(true);
    add(new HandicapTableViewer());
  }

  public class HandicapTableViewer extends GolfBlock {

    public void main(IWContext modinfo) throws Exception {
      String fieldID = modinfo.getParameter("field_id");
      if (fieldID == null) {
        fieldID = "50";
      }

      getAllTables(modinfo, Integer.parseInt(fieldID));
    }

    public void getAllTables(IWContext modinfo, int field_id) {
      Table myTable = new Table();
      myTable.setCellspacing(6);
      myTable.setCellpadding(6);
      myTable.setAlignment("center");

      IWResourceBundle iwrb = getResourceBundle();

      try {
        Field field = ((FieldHome) IDOLookup.getHomeLegacy(Field.class))
            .findByPrimaryKey(field_id);
        getParentPage().setTitle(
            field.getName()
                + " - "
                + iwrb.getLocalizedString("handicap.handicap_tables",
                    "Handicap tables"));

        TeeColor[] teeGender = (TeeColor[]) ((TeeColor) IDOLookup
            .instanciateEntity(TeeColor.class))
            .findAll("select distinct gender from tee, tee_color where tee.tee_color_id = tee_color.tee_color_id and field_id = "
                + field_id + " order by gender desc");

        for (int a = 0; a < teeGender.length; a++) {

          String gender = teeGender[a].getGender();
          if (gender.equalsIgnoreCase("m")) {
            gender = iwrb.getLocalizedString("handicap.men", "Men");
          } else if (gender.equalsIgnoreCase("f")) {
            gender = iwrb.getLocalizedString("handicap.women", "Women");
          } else {
            gender = "Teigar";
          }

          Tee[] tee = (Tee[]) ((Tee) IDOLookup.instanciateEntity(Tee.class))
              .findAll("select distinct tee_color.tee_color_id,slope,course_rating from tee, tee_color where tee.tee_color_id = tee_color.tee_color_id and gender = '"
                  + teeGender[a].getGender() + "' and field_id = " + field_id);

          Text genderText = new Text(gender);
          genderText.setFontSize(4);
          genderText.setBold();

          Table genderTable = new Table();
          int column = 0;

          for (int b = 0; b < tee.length; b++) {

            column++;
            int slope = tee[b].getSlope();
            float CR = tee[b].getCourseRating();
            int fieldPar = field.getFieldPar();
            String teeName = ((TeeColorHome) IDOLookup
                .getHomeLegacy(TeeColor.class)).findByPrimaryKey(
                tee[b].getTeeColorID()).getName();

            Table outerTable = new Table(1, 1);
            outerTable.setCellpadding(0);
            outerTable.setCellspacing(1);
            outerTable.setColor("#000000");
            outerTable.setColor(1, 1, getTeeColor(tee[b].getTeeColorID()));
            outerTable.setWidth("150");

            Table teeInfoTable = new Table(2, 3);
            teeInfoTable.mergeCells(1, 1, 2, 1);
            teeInfoTable.setColumnAlignment(2, "center");
            teeInfoTable.setRowAlignment(1, "center");
            teeInfoTable.setCellspacing(6);
            outerTable.add(teeInfoTable);
            teeInfoTable.setWidth("100%");

            Text teeNameText = new Text(teeName);
            teeNameText.setFontSize(3);
            teeNameText.setBold();
            Text teeCRText = new Text("CR");
            teeCRText.setBold();
            Text teeSlopeText = new Text("Slope");
            teeSlopeText.setBold();

            teeInfoTable.add(teeNameText, 1, 1);
            teeInfoTable.add(teeCRText, 1, 2);
            teeInfoTable.add(teeSlopeText, 1, 3);
            teeInfoTable.add(CR + "", 2, 2);
            teeInfoTable.add(slope + "", 2, 3);

            Table teeTable = getTeeTable(modinfo, (double) slope, (double) CR,
                fieldPar);
            genderTable.add(outerTable, column, 2);
            genderTable.add(teeTable, column, 3);

          }

          genderTable.mergeCells(1, 1, genderTable.getColumns(), 1);
          genderTable.setAlignment(1, 1, "center");
          genderTable.setRowVerticalAlignment(3, "top");
          genderTable.add(genderText, 1, 1);

          myTable.add(genderTable, a + 1, 1);

        }

        myTable.setRowVerticalAlignment(1, "top");

      } catch (Exception e) {
        e.printStackTrace(System.err);
      }

      add(myTable);

    }

    public Table getTeeTable(IWContext modinfo, double slope,
        double course_rating, int par) {
      IWResourceBundle iwrb = getResourceBundle();

      double grunn = 36.0;
      Hashtable ollBil = new Hashtable();

      while (grunn > -4) {

        long leik = handicap(grunn, slope, course_rating, par);
        MinMax bil = (MinMax) ollBil.get(Long.toString(leik));
        if (bil == null) {
          bil = new MinMax();
          bil.max = grunn;
          bil.min = grunn;
          ollBil.put(Long.toString(leik), bil);
        } else {
          if (bil.max < grunn) bil.max = grunn;
          if (bil.min > grunn) bil.min = grunn;
        }

        grunn -= 0.1;
      }

      Enumeration e = ollBil.keys();

      Vector sort = new Vector();
      while (e.hasMoreElements()) {
        String key = (String) e.nextElement();
        if (sort.size() == 0)
          sort.add(key);
        else {
          int i = 0;
          for (; i < sort.size(); i++) {
            String tmpKey = (String) sort.elementAt(i);
            if (Integer.parseInt(key) < Integer.parseInt(tmpKey)) {
              sort.add(i, key);
              break;
            }
          }
          if (i == sort.size()) sort.add(key);
        }
      }

      Iterator i = sort.iterator();

      Table myTable = new Table();
      myTable.setBorder(0);
      myTable.setWidth("100%");
      myTable.setCellpadding(3);
      myTable.setCellspacing(1);
      myTable.setColor("#000000");

      Text grunnText = new Text(iwrb.getLocalizedString("handicap.handicap",
          "Handicap"));
      grunnText.setFontSize(1);
      grunnText.setBold();
      grunnText.setFontFace("Verdana,Arial,sans-serif");
      Text leikText = new Text(iwrb.getLocalizedString(
          "handicap.course_handicap", "Course"));
      leikText.addBreak();
      leikText.addToText(iwrb.getLocalizedString("handicap.handicap_lowercase",
          "handicap"));
      leikText.setFontSize(1);
      leikText.setBold();
      leikText.setFontFace("Verdana,Arial,sans-serif");

      myTable.add(grunnText, 1, 1);
      myTable.add(leikText, 2, 1);
      myTable.setRowAlignment(1, "center");
      myTable.setRowColor(1, "#FFFFFF");

      int a = 0;

      while (i.hasNext()) {
        a++;
        String key = (String) i.next();
        MinMax m = (MinMax) ollBil.get(key);
        double min = m.min;
        double max = m.max;

        Text minText = new Text(TextSoap.singleDecimalFormat(min));
        minText.setFontSize(1);
        minText.setFontFace("Verdana,Arial,sans-serif");
        Text maxText = new Text(TextSoap.singleDecimalFormat(max));
        maxText.setFontSize(1);
        maxText.setFontFace("Verdana,Arial,sans-serif");
        Text keyText = new Text(key);
        keyText.setFontSize(1);
        keyText.setFontFace("Verdana,Arial,sans-serif");
        keyText.setBold();

        myTable.add(minText, 1, a + 1);
        myTable.add(" - ", 1, a + 1);
        myTable.add(maxText, 1, a + 1);
        myTable.add(keyText, 2, a + 1);
        myTable.setRowColor(a + 1, "#FFFFFF");
        myTable.setRowAlignment(a + 1, "center");
      }

      return myTable;

    }

    public long handicap(double grunn, double slope, double cr, int par) {
      double tmp = grunn * slope / 113.0 + (cr - par);
      long h = java.lang.Math.round(tmp);

      return (h);
    }

    class MinMax {

      public double max = -100;

      public double min = 100;

      public MinMax() {
      }
    }

    public String getTeeColor(int teeColorID) throws IOException {
      String litur = "";
      switch (teeColorID) {
      case 1:
        litur = "FFFFFF";
        break;
      case 2:
        litur = "FFFF67";
        break;
      case 3:
        litur = "5757FF";
        break;
      case 4:
        litur = "FF5757";
        break;
      case 5:
        litur = "5757FF";
        break;
      case 6:
        litur = "FF5757";
        break;
      }

      return litur;
    }
  }
}