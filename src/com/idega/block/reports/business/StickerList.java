package com.idega.block.reports.business;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class StickerList extends ArrayList {

  /** This is the sticker height. */
  private float height = 1.0f;
  /** This is the sticker width. */
  private float width = 1.0f;
  /** This is the sticker indent. */
  private float indent = 10f;
  /** Do we want to rotate the page */
  private boolean rotate = false;
  /** This is Rectangle object presenting the page size. */
  private Rectangle paperSize = PageSize.A4;

  /** This represents the status of the 4 sides of the rectangle. */
  protected int border = Rectangle.UNDEFINED;

  /** This represents the margin of the page. */
  protected float margin = 0.0f;

  protected PdfPCell defaultCell = new PdfPCell((Phrase)null);


  public StickerList(){
    super(10);
    this.defaultCell.setIndent(10);
    this.defaultCell.setBorder(Rectangle.BOX);
    this.defaultCell.setLeading(1.1f,1.2f);
    this.border = Rectangle.BOX;
  }

  /** Gets the default <CODE>PdfPCell</CODE> that will be used as
     * reference for all the <CODE>addCell</CODE> methods except
     * <CODE>addCell(PdfPCell)</CODE>.
     * @return default <CODE>PdfPCell</CODE>
     */
    public PdfPCell getDefaultCell() {
        return this.defaultCell;
    }

  /**
   * Returns the exact type of the border.
   *
   * @return	a value
   */
    public final int border() {
        return this.border;
    }

  /**
   * Returns the exact type of the margin.
   *
   * @return	a value
   */
    public final float margin() {
        return this.margin;
    }

    /**
   * Sets the overall margin of the stickers page.
   *
   * @param	margin		the sticker height
   * @return	<CODE>void</CODE>
   */
  public void setOverAllMargin(float margin){
    this.margin = margin;
  }

  /**
   * Sets the heigth of these stickers.
   *
   * @param	height		the sticker height
   * @return	<CODE>void</CODE>
   */
  public void setStickerHeight(float height){
    this.height = height;
  }

  /**
   * Sets the heigth of these stickers.
   *
   * @param	width		the sticker height
   * @return	<CODE>void</CODE>
   */
  public void setStickerWidth(float width){
    this.width = width;
  }

   /**
   * Sets the heigth of these stickers.
   *
   * @param	width		the sticker height
   * @return	<CODE>void</CODE>
   */
  public void setBorder(int border){
    this.border = border;
  }

  /**
   * Sets the Rectangle object representing the pageSize of these stickers.
   *
   * @param	pagesize	the page Rectangle
   * @return	<CODE>void</CODE>
   */
  public void setPageSize(Rectangle pagesize){
    this.paperSize = pagesize;
  }

  /**
   * Sets the <CODE>Rectangle</CODE> object representing the pageSize of these stickers.
   *
   * @param		urx			upper right x
   * @param		ury			upper right y
   * @return	<CODE>void</CODE>
   */
  public void setPageSize(float urx,float ury){
    this.paperSize = new Rectangle(urx,ury);
  }

  /**
   * Gets the rotation flag of the page.
   *
   * @param	rotate	the rotation flag
   * @return	<CODE>boolean</CODE>
   */
  public void setRotation(boolean rotate){
    this.rotate = rotate;
  }

  /**
   * Gets the heigth of these stickers.
   *
   * @return	<CODE>float</CODE>
   */
  public float getStickerHeight(){
    return this.height;
  }

  /**
   * Gets the heigth of these stickers.
   *
   * @return	<CODE>float</CODE>
   */
  public float getStickerWidth(){
    return this.width;
  }

   /**
   * Gets the heigth of these stickers.
   *
   * @return	<CODE>Rectangle</CODE>
   */
  public Rectangle getPageSize(){
    return this.paperSize;
  }

  /**
   * Gets the rotation flag of the page.
   *
   * @return	<CODE>boolean</CODE>
   */
  public boolean getRotation(){
    return this.rotate;
  }

  /**
   * Adds a <CODE>Paragraph</CODE>  to this <CODE>StickerList</CODE>.
   *
   * @param	object	an object of type <CODE>Paragraph</CODE>
   * @return	a boolean
   * @throws	ClassCastException	when you try to add something that isn't a <CODE>Chunk</CODE>, <CODE>Anchor</CODE> or <CODE>Phrase</CODE>
   */
  public boolean add(Object o){
    try {
      return super.add(o);
    }
    catch(ClassCastException cce) {
        throw new ClassCastException("Insertion of illegal Element: " + cce.getMessage());
    }
  }

  /**
   * Adds a <CODE>Paragraph</CODE> to this <CODE>ArrayList</CODE>.
   *
   * @param	index	index at which the specified element is to be inserted
   * @param	object	an object of type <CODE>Paragraph</CODE>
   * @return	<CODE>void</CODE>
   * @throws	ClassCastException	when you try to add something that isn't a <CODE>Paragraph</CODE>
   */
  public void add(int index,Object o){
    try {
      super.add(index,o);
    }
    catch(ClassCastException cce) {
        throw new ClassCastException("Insertion of illegal Element: " + cce.getMessage());
    }
  }

  /**
   * Adds a collection of <CODE>Paragraph</CODE>s to this <CODE>Phrase</CODE>.
   *
   * @param	collection	a collection of <CODE>Paragraph</CODE>s
   * @return	<CODE>true</CODE> if the action succeeded, <CODE>false</CODE> if not.
   * @throws	ClassCastException	when you try to add something that isn't a <CODE>Paragraph</CODE>
   */
  public boolean addAll(Collection collection) {
    for (Iterator iterator = collection.iterator(); iterator.hasNext(); ) {
      this.add(iterator.next());
    }
    return true;
  }

}
