//idega 2000 - Tryggvi Larusson

package is.idega.idegaweb.golf.entity;

//import java.util.*;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.query.Column;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;

public class TeeBMPBean extends GenericEntity implements Tee{

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("slope", "Vægi", true, true, "java.lang.Integer");
		addAttribute("handicap", "Holuforgjöf", true, true, "java.lang.Float");
		addAttribute("course_rating", "Vallarmat", true, true, "java.lang.Float");
		addAttribute("hole_name", "Nafn holu", true, true, "java.lang.String");
		addAttribute("par", "Par", true, true, "java.lang.Integer");
		addAttribute("hole_number", "Númer holu", true, true, "java.lang.Integer");
		addAttribute("field_id","Völlur", true, true, "java.lang.Integer","one-to-many","is.idega.idegaweb.golf.entity.Field");
		addAttribute("tee_color_id","Teigur", true, true, "java.lang.Integer","one-to-many","is.idega.idegaweb.golf.entity.TeeColor");
		addAttribute("tee_length","Lengd teigar",true,true,"java.lang.Integer");
	}
	

	public int getTeeLength() {
		return getIntColumnValue("tee_length");
	}

	public void setTeeLength(int length) {
		setColumn("tee_length",length);
	}

	public int getTeeColorID() {
		return getIntColumnValue("tee_color_id");
	}

	public void setTeeColorID(int tee_color_id) {
		setColumn("tee_color_id",tee_color_id);
	}

	public int getFieldID() {
		return getIntColumnValue("field_id");
	}

	public void setFieldID( int field_id) {
		setColumn("field_id",field_id);
	}

	public int getHoleNumber() {
		return getIntColumnValue("hole_number");
	}

	public void setHoleNumber( int number) {
		setColumn("hole_number",number);
	}

	public int getPar(){
		return getIntColumnValue("par");
	}

	public void setPar(int par) {
		setColumn("par",par);
	}

	public String getName(){
		return getStringColumnValue("hole_name");
	}

	public void setHoleName(String name) {
		setColumn("hole_name",name);
	}

	public float getCourseRating(){
		return getFloatColumnValue("course_rating");
	}

	public void setCourseRating(float cr) {
		setColumn("course_rating",cr);
	}

	public void setCourseRating(String cr) {
		setColumn("course_rating",Float.parseFloat(cr));
	}

	public float getHandicap(){
		return getFloatColumnValue("handicap");
	}

	public void setHandicap(float handicap) {
		setColumn("handicap",handicap);
	}
	public void setHandicap(String handicap) {
		setColumn("handicap",Float.parseFloat(handicap));
	}

	public int getSlope(){
		return getIntColumnValue("slope");
	}

	public void setSlope(String slope) {
		setColumn("slope",Integer.parseInt(slope));
	}
	public void setSlope(int slope) {
		setColumn("slope",slope);
	}

	public String getEntityName(){
		return "tee";
	}

	public Collection ejbFindByFieldAndHoleNumber(int fieldID, int holeNumber) throws FinderException{
		Table table = new Table(this);
		Column colFieldID = new Column(table, "field_id");
		Column colHoleNumber = new Column(table, "hole_number");
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn(table));
		query.addCriteria(new MatchCriteria(colFieldID, MatchCriteria.EQUALS, fieldID));
		query.addCriteria(new MatchCriteria(colHoleNumber, MatchCriteria.EQUALS, holeNumber));
		
		return this.idoFindIDsBySQL(query.toString());		
	}

	public Integer ejbFindByFieldAndTeeColorAndHoleNumber(int fieldID, int teeColorID, int holeNumber) throws FinderException {
		Table table = new Table(this);
		Column colFieldID = new Column(table, "field_id");
		Column colTeeColorID = new Column(table, "tee_color_id");
		Column colHoleNumber = new Column(table, "hole_number");
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn(table));
		query.addCriteria(new MatchCriteria(colFieldID, MatchCriteria.EQUALS, fieldID));
		query.addCriteria(new MatchCriteria(colTeeColorID, MatchCriteria.EQUALS, teeColorID));
		query.addCriteria(new MatchCriteria(colHoleNumber, MatchCriteria.EQUALS, holeNumber));
		
		return (Integer) this.idoFindOnePKBySQL(query.toString());		
	}
}