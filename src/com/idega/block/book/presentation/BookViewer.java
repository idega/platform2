package com.idega.block.book.presentation;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.ejb.FinderException;
import com.idega.block.book.business.BookBusiness;
import com.idega.block.book.business.BookComparator;
import com.idega.block.book.data.Author;
import com.idega.block.book.data.Book;
import com.idega.block.book.data.Publisher;
import com.idega.block.book.data.Review;
import com.idega.block.category.business.CategoryFinder;
import com.idega.block.category.data.ICCategory;
import com.idega.block.category.presentation.CategoryBlock;
import com.idega.core.builder.data.ICPage;
import com.idega.data.IDOException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.block.presentation.Builderaware;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.util.text.TextSoap;


public class BookViewer extends CategoryBlock implements Builderaware {

  private boolean _isAdmin = false;
  private Table _myTable;

  private int _state = BookBusiness.CATEGORY_COLLECTION;
  private int _initialState = BookBusiness.CATEGORY_COLLECTION;
  private int _authorID = -1;
  private int _bookID = -1;
  private int _categoryID = -1;
  private int _publisherID = -1;
  private int _objectID = -1;

  private boolean _styles = true;
  private String _width;
  private String _textStyle;
  private String _headerStyle;
  private String _categoryHeadingStyle;
  private String _headingStyle;
  private String _informationStyle;
  private String _ratingStyle;
  private int _numberOfShown = 2;
  private boolean _showAuthorList = false;
  private boolean _showPublisherList = false;

  private String _linkStyle;
  private String _linkHoverStyle;
  private String _linkName;

  private BookBusiness _bookBusiness;
  private Image _divider;
  private ICPage _page;

  private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.book";
  protected IWResourceBundle _iwrb;
  protected IWBundle _iwb;
  protected IWBundle _iwcb;

  public BookViewer() {
    setDefaultValues();
  }

  public void main(IWContext iwc) throws Exception {
    this._iwrb = getResourceBundle(iwc);
    this._iwb = getBundle(iwc);
    this._iwcb = iwc.getIWMainApplication().getBundle(IW_CORE_BUNDLE_IDENTIFIER);

    this._isAdmin = iwc.hasEditPermission(this);
    this._objectID = getICObjectInstanceID();
    this._bookBusiness = BookBusiness.getBookBusinessInstace();
    this._divider = this._iwb.getImage("shared/dotted.gif");
    getParameters(iwc);

    this._myTable = new Table();
    this._myTable.setCellpadding(0);
    this._myTable.setCellspacing(0);
    this._myTable.setBorder(0);
    this._myTable.setWidth(this._width);

    int row = 1;
    if(this._isAdmin){
      this._myTable.add(getAdminPart(iwc),1,row);
      row++;
    }

    this._myTable.add(getBookViewer(iwc),1,row);
    add(this._myTable);
  }

  private Table getBookViewer(IWContext iwc) throws FinderException,RemoteException,IDOException {
    Table table = null;
    setStyles();

    switch (this._state) {
      case BookBusiness.AUTHOR_VIEW:
	table = getAuthorView(iwc);
	break;
      case BookBusiness.BOOK_VIEW:
	table = getBookView(iwc);
	break;
      case BookBusiness.PUBLISHER_VIEW:
	table = getPublisherView(iwc);
	break;
      case BookBusiness.BOOK_COLLECTION:
	table = getBookCollection(iwc);
	break;
      case BookBusiness.CATEGORY_COLLECTION:
	table = getCategoryCollection(iwc);
	break;
      case BookBusiness.BOOK_CATEGORY_COLLECTION:
	table = getBookCategoryCollection(iwc);
	break;
      case BookBusiness.NEWEST_BOOKS:
	table = getNewestBooksCollection(iwc);
	break;
      //* todo: implement later *//
      case BookBusiness.REVIEW_VIEW:
	break;
      case BookBusiness.SEARCH:
	break;
    }
    return table;
  }

  private Table getAuthorView(IWContext iwc) throws FinderException,RemoteException {
    Table table = new Table();
      table.setWidth(Table.HUNDRED_PERCENT);
      table.setWidth(1,Table.HUNDRED_PERCENT);

    if ( this._authorID != -1 ) {
      Author author = this._bookBusiness.getAuthor(this._authorID);
      if ( author != null ) {
	int row = 1;
	table.add(formatText(author.getName(),this._headerStyle),1,row++);
	if ( author.getImage() != -1 ) {
	  Image image = this._bookBusiness.getImage(author.getImage());
	    image.setHorizontalSpacing(8);
	    image.setVerticalSpacing(4);
	    image.setAlignment(Image.ALIGNMENT_RIGHT);
	  table.add(image,1,row);
	}
	table.add(formatText(TextSoap.formatText(author.getDescription())),1,row++);
	table.add(getBackLink(),1,row++);

	if ( this._isAdmin ) {
		table.add(getAdminButtons(AuthorEditor.class,BookBusiness.PARAMETER_AUTHOR_ID,this._authorID),1,row++);
	}

	table.setHeight(row++,"16");

	List books = new Vector(this._bookBusiness.getBookHome().findAllBooksByAuthor(this._authorID));
	if ( books != null ) {
	  Collections.sort(books,new BookComparator(BookComparator.PUBLISH_YEAR));
	  Table booksTable = new Table(1,3);
	    booksTable.setCellpaddingAndCellspacing(0);
	    booksTable.setWidth(Table.HUNDRED_PERCENT);
	  booksTable.add(formatText(this._iwrb.getLocalizedString("books_by_author","Books by author")+":",this._categoryHeadingStyle),1,1);
	  booksTable.setBackgroundImage(1,2,this._divider);

	  Table bookTable = new Table(3,books.size());
	  Text divider = formatText("|",this._categoryHeadingStyle);
	  int bookRow = 1;

	  Iterator iter = books.iterator();
	  while (iter.hasNext()) {
	    Book book = (Book) iter.next();
	    bookTable.add(formatText(String.valueOf(book.getYear()),this._informationStyle),1,bookRow);
	    bookTable.add(divider,2,bookRow);
	    bookTable.add(getBookLink(book),3,bookRow);
	    bookTable.setRowVerticalAlignment(bookRow++,Table.VERTICAL_ALIGN_BOTTOM);
	  }
	  booksTable.add(bookTable,1,3);
	  table.add(booksTable,1,row);
	}
      }
	else {
		return getCategoryCollection(iwc);
	}
    }
    return table;
  }

  private Table getPublisherView(IWContext iwc) throws FinderException,RemoteException {
    Table table = new Table();
      table.setWidth(Table.HUNDRED_PERCENT);

    if ( this._publisherID != -1 ) {
      int row = 1;
      Publisher publisher = this._bookBusiness.getPublisher(this._publisherID);
      if ( publisher != null ) {
	table.add(formatText(publisher.getName(),this._headerStyle),1,row++);
	if ( publisher.getImage() != -1 ) {
	  Image image = this._bookBusiness.getImage(publisher.getImage());
	    image.setHorizontalSpacing(8);
	    image.setVerticalSpacing(4);
	    image.setAlignment(Image.ALIGNMENT_RIGHT);
	  table.add(image,1,row);
	}
	table.add(formatText(TextSoap.formatText(publisher.getDescription())),1,row++);
	table.add(getBackLink(),1,row++);

	if ( this._isAdmin ) {
		table.add(getAdminButtons(PublisherEditor.class,BookBusiness.PARAMETER_PUBLISHER_ID,this._publisherID),1,row);
	}
      }
	else {
		return getCategoryCollection(iwc);
	}
    }
    return table;
  }

  private Table getBookView(IWContext iwc) throws FinderException,RemoteException,IDOException {
    Table table = new Table();
      table.setWidth(Table.HUNDRED_PERCENT);
      table.setWidth(1,Table.HUNDRED_PERCENT);

    if ( this._bookID != -1 ) {
      Book book = this._bookBusiness.getBook(this._bookID);
      if ( book != null ) {
	int row = 1;

	table.add(formatText(book.getName(),this._headerStyle),1,row++);
	if ( book.getImage() != -1 ) {
	  Image image = this._bookBusiness.getImage(book.getImage());
	    image.setHorizontalSpacing(8);
	    image.setVerticalSpacing(4);
	    image.setAlignment(Image.ALIGNMENT_RIGHT);
	  table.add(image,1,row);
	}
	table.add(formatText(TextSoap.formatText(book.getDescription())),1,row);
	table.add(new Break(2),1,row);
	table.add(getBookInfo(book),1,row++);

	Image reviewImage = this._iwb.getImage("shared/review.gif");
	  reviewImage.setAlignment(Image.ALIGNMENT_ABSOLUTE_MIDDLE);
	  reviewImage.setHorizontalSpacing(4);
	Link link = new Link(this._iwrb.getLocalizedString("write_review","Write review"));
	  link.setStyle(this._linkName);
	  link.setWindowToOpen(ReviewEditor.class);
	  link.addParameter(BookBusiness.PARAMETER_MODE,BookBusiness.PARAMETER_NEW);
	  link.addParameter(BookBusiness.PARAMETER_BOOK_ID,((Integer)book.getPrimaryKey()).intValue());
	table.add(reviewImage,1,row);
	table.add(link,1,row++);

	if ( this._isAdmin ) {
		table.add(getAdminButtons(BookEditor.class,BookBusiness.PARAMETER_BOOK_ID,this._bookID),1,row++);
	}

	table.setHeight(row++,"16");

	Table reviewTable = getReviewTable(iwc,book);
	if ( reviewTable != null ) {
	  table.add(reviewTable,1,row);
	}

      }
	else {
		return getCategoryCollection(iwc);
	}
    }
    return table;
  }

  private Table getBookInfo(Book book) throws FinderException, RemoteException,IDOException {
    Table table = new Table(2,4);
    table.add(formatText(this._iwrb.getLocalizedString("authors","Author/s")+":",this._headingStyle),1,1);

    Collection authors = this._bookBusiness.getAuthorHome().findAllAuthorsByBook(this._bookID);
    if ( authors != null ) {
      int count = 1;
      Iterator iter = authors.iterator();
      while (iter.hasNext()) {
	Author author = (Author) iter.next();
	if ( count > 1 ) {
		table.add(formatText(", "),2,1);
	}
	table.add(getAuthorLink(author),2,1);
	count++;
      }
    }

    table.add(formatText(this._iwrb.getLocalizedString("published","Published")+":",this._headingStyle),1,2);
    if ( book.getYear() != 0 ) {
      table.add(formatText(String.valueOf(book.getYear())),2,2);
    }

    table.add(formatText(this._iwrb.getLocalizedString("publisher","Publisher")+":",this._headingStyle),1,3);
    Publisher publisher = this._bookBusiness.getPublisher(book.getPublisherID());
    if ( publisher != null ) {
      table.add(getPublisherLink(publisher),2,3);
    }

    table.add(formatText(this._iwrb.getLocalizedString("rating","Rating")+":",this._headingStyle),1,4);
    int total = this._bookBusiness.getReviewHome().getRatingTotal(((Integer)book.getPrimaryKey()).intValue());
    int votes = this._bookBusiness.getReviewHome().getNumberOfReviews(((Integer)book.getPrimaryKey()).intValue());
    if ( total > 0 ) {
      double average = (double) total / (double) votes;
      NumberFormat format = NumberFormat.getInstance();
      format.setMaximumFractionDigits(1);
      table.add(formatText(format.format(average)),2,4);
      table.add(formatText(" ("+String.valueOf(votes)+" "+this._iwrb.getLocalizedString("votes","votes")+")",this._informationStyle),2,4);
    }
    else {
      table.add(formatText(this._iwrb.getLocalizedString("no_rating","No rating available")),2,4);
    }

    return table;
  }

  private Table getReviewTable(IWContext iwc,Book book) throws FinderException,RemoteException {
    Table table = new Table();
      table.setColumns(1);
      table.setCellpaddingAndCellspacing(0);
      table.setHeight(1,"16");
      table.setHeight(2,"1");
      table.setWidth(Table.HUNDRED_PERCENT);

    Table reviewTable;

    table.add(formatText(this._iwrb.getLocalizedString("reviews","Reviews"),this._categoryHeadingStyle),1,1);
    table.setBackgroundImage(1,2,this._divider);

    Collection collection = this._bookBusiness.getReviewHome().findAllReviewsForBook(((Integer)book.getPrimaryKey()).intValue());
    if ( collection != null && collection.size() > 0 ) {
      table.setRows((collection.size()*2)+1);
      int row = 3;

      Iterator iter = collection.iterator();
      while (iter.hasNext()) {
	reviewTable = new Table();
	reviewTable.setWidth(Table.HUNDRED_PERCENT);
	reviewTable.setAlignment(1,3,Table.HORIZONTAL_ALIGN_RIGHT);

	Review review = (Review) iter.next();
	reviewTable.add(formatText(review.getName(),this._informationStyle),1,1);
	reviewTable.add(formatText(","+Text.NON_BREAKING_SPACE,this._informationStyle),1,1);
	reviewTable.add(formatText(getThreadDate(iwc,review.getDateAdded().getTime()),this._informationStyle),1,1);
	reviewTable.add(formatText(TextSoap.formatText(review.getReview())),1,2);
	reviewTable.add(formatText(this._iwrb.getLocalizedString("rating","Rating")+":"+Text.NON_BREAKING_SPACE+Integer.toString(review.getRating()),this._ratingStyle),1,3);
	if ( this._isAdmin ) {
		reviewTable.add(getAdminButtons(ReviewEditor.class,BookBusiness.PARAMETER_REVIEW_ID,((Integer)review.getPrimaryKey()).intValue()),1,4);
	}

	table.add(reviewTable,1,row++);
	table.setHeight(row,"1");
	table.setBackgroundImage(1,row++,this._divider);
      }

      return table;
    }
    else {
      return null;
    }
  }

  private Table getNewestBooksCollection(IWContext iwc) throws FinderException,RemoteException {
    Collection collection = this._bookBusiness.getBookHome().findAllNewestBooks(getCategoryIds(),this._numberOfShown);

    if ( collection != null && collection.size() > 0 ) {
      Table table = new Table();
	table.setWidth(Table.HUNDRED_PERCENT);
      int row = 1;

      Iterator iter = collection.iterator();
      while (iter.hasNext()) {
	Book book = (Book) iter.next();

	table.add(formatText(book.getName(),this._headerStyle),1,row++);
	if ( book.getImage() != -1 ) {
	  Image image = this._bookBusiness.getImage(book.getImage());
	    image.setBorder(1);
	    image.setAlignment(Image.ALIGNMENT_RIGHT);
	    image.setHorizontalSpacing(4);
	  table.add(image,1,row);
	}
	String desc = book.getDescription();
	if ( desc.length() > 512 ) {
		desc = desc.substring(0,512) + "...";
	}
	table.add(formatText(TextSoap.formatText(desc)),1,row++);
	table.add(getMoreLink(((Integer)book.getPrimaryKey()).intValue()),1,row++);
	table.setHeight(row++,"8");
      }

      return table;
    }
    else {
      return new Table();
    }
  }

  private Table getBookCollection(IWContext iwc) throws FinderException,RemoteException {
    return getBookCategory(iwc,this._categoryID);
  }

  private Table getBookCategoryCollection(IWContext iwc) throws FinderException,RemoteException {
    int[] categories = getCategoryIds();
    Table table = new Table();
      table.setCellpaddingAndCellspacing(0);
      table.setWidth(Table.HUNDRED_PERCENT);
    int row = 1;

    for ( int a = 0; a < categories.length; a++ ) {
      table.add(getBookCategory(iwc,categories[a]),1,row++);
      table.setHeight(row++,"16");
    }
    return table;
  }

  private Table getBookCategory(IWContext iwc,int categoryID) throws FinderException,RemoteException {
    Table table = new Table(1,3);
      table.setCellpaddingAndCellspacing(0);
      table.setWidth(Table.HUNDRED_PERCENT);
      table.setHeight(1,"16");
      table.setHeight(2,"1");

    if ( categoryID != -1 ) {
      ICCategory category = CategoryFinder.getInstance().getCategory(categoryID);
      table.add(formatText(category.getName(),this._headingStyle),1,1);
      table.setBackgroundImage(1,2,this._divider);

      List collection = new Vector(this._bookBusiness.getBookHome().findAllBooksByCategory(categoryID));

      if ( collection != null ) {
	Table bookTable = new Table(2,collection.size());
	  bookTable.setWidth(Table.HUNDRED_PERCENT);
	  bookTable.setColumns(2);
	  bookTable.setColumnAlignment(2,Table.HORIZONTAL_ALIGN_RIGHT);

	Collections.sort(collection,new BookComparator(BookComparator.BOOK_NAME));
	Iterator iter = collection.iterator();
	int row = 1;

	while (iter.hasNext()) {
	  Book book = (Book) iter.next();
	  bookTable.add(getBookLink(book),1,row);

	  List authors = new Vector(this._bookBusiness.getAuthorHome().findAllAuthorsByBook(((Integer)book.getPrimaryKey()).intValue()));
	  if ( authors != null ) {
	    Collections.sort(authors,new BookComparator(BookComparator.AUTHOR_NAME));

	    Iterator iter2 = authors.iterator();
	    int count = 1;
	    while (iter2.hasNext()) {
	      Author author = (Author) iter2.next();
	      if ( count > 1 ) {
		bookTable.add(formatText(", "),2,row);
	      }
	      bookTable.add(getAuthorLink(author),2,row);
	      count++;
	    }
	  }
	  row++;
	}
	table.add(bookTable,1,3);
      }
    }

    return table;
  }

  private Table getCategoryCollection(IWContext iwc) throws FinderException,RemoteException {
    List collection = new Vector(getCategories());

    if ( collection != null && collection.size() > 0 ) {
      Collections.sort(collection,new BookComparator(BookComparator.CATEGORY_NAME));
      Table table = new Table(3,collection.size()+2);
	table.setCellpadding(0);
	table.setCellspacing(2);
	table.setWidth(Table.HUNDRED_PERCENT);
	table.setWidth(2,"50");
	table.setWidth(3,"90");
	table.setColumnAlignment(2,Table.HORIZONTAL_ALIGN_CENTER);
	table.setColumnAlignment(3,Table.HORIZONTAL_ALIGN_RIGHT);

      int row = 1;

      table.add(formatText(this._iwrb.getLocalizedString("categories","Categories"),this._categoryHeadingStyle),1,row);
      table.add(formatText(this._iwrb.getLocalizedString("number_of_books","Books"),this._categoryHeadingStyle),2,row);
      table.add(formatText(this._iwrb.getLocalizedString("last_added","Last added"),this._categoryHeadingStyle),3,row++);
      table.setBackgroundImage(1,row,this._divider);
      table.mergeCells(1,row,3,row++);

      Iterator iter = collection.iterator();
      while (iter.hasNext()) {
	ICCategory category = (ICCategory) iter.next();

	table.add(getCategoryLink(category),1,row);
	table.add(formatText(String.valueOf(this._bookBusiness.getNumberOfBooksInCategory(category.getID()))),2,row);
	long time = this._bookBusiness.getLastAddedTime(category.getID());
	if ( time > 0 ) {
		table.add(formatText(getThreadDate(iwc,time)),3,row++);
	}
	else {
		row++;
	}
      }

      if ( this._isAdmin || this._showAuthorList ) {
	table.resize(table.getColumns(),table.getRows()+2);
	table.setHeight(row++,"16");
	table.mergeCells(1,row,3,row);
	table.add(getAuthorList(iwc),1,row++);
      }

      if ( this._isAdmin || this._showPublisherList ) {
	table.resize(table.getColumns(),table.getRows()+2);
	table.setHeight(row++,"16");
	table.mergeCells(1,row,3,row);
	table.add(getPublisherList(iwc),1,row);
      }

      return table;
    }
    else {
      return new Table();
    }
  }

  private Table getAuthorList(IWContext iwc) throws FinderException,RemoteException {
    Table table = new Table(1,3);
      table.setCellpaddingAndCellspacing(0);
      table.setWidth(Table.HUNDRED_PERCENT);
      table.setHeight(1,"16");
      table.setHeight(2,"1");

    table.add(formatText(this._iwrb.getLocalizedString("authors","Authors"),this._categoryHeadingStyle),1,1);
    table.setBackgroundImage(1,2,this._divider);

    List authors = new Vector(this._bookBusiness.getAuthorHome().findAllAuthors());
    if ( authors != null ) {
      Table authorTable = new Table();
	authorTable.setColumns(2);
	authorTable.setWidth(Table.HUNDRED_PERCENT);
	authorTable.setWidth(1,"50%");
	authorTable.setWidth(2,"50%");

      Collections.sort(authors,new BookComparator(BookComparator.AUTHOR_NAME));
      int row = 1;
      int column = 1;
      int size = authors.size();
      int switchColumn = size / 2;
      if ( size % 2 > 0 ) {
		switchColumn++;
	}

      Iterator iter = authors.iterator();
      while (iter.hasNext()) {
	authorTable.add(getAuthorLink((Author)iter.next()),column,row);
	if ( column == switchColumn ) {
	  column++;
	  row = 1;
	}
	else {
	  row++;
	}
      }
      table.add(authorTable,1,3);
    }
    return table;
  }

  private Table getPublisherList(IWContext iwc) throws FinderException,RemoteException {
    Table table = new Table(1,3);
      table.setCellpaddingAndCellspacing(0);
      table.setWidth(Table.HUNDRED_PERCENT);
      table.setHeight(1,"16");
      table.setHeight(2,"1");

    table.add(formatText(this._iwrb.getLocalizedString("publishers","Publishers"),this._categoryHeadingStyle),1,1);
    table.setBackgroundImage(1,2,this._divider);

    List publishers = new Vector(this._bookBusiness.getPublisherHome().findAllPublishers());
    if ( publishers != null ) {
      Table publisherTable = new Table();
	publisherTable.setColumns(2);
	publisherTable.setWidth(Table.HUNDRED_PERCENT);
	publisherTable.setWidth(1,"50%");
	publisherTable.setWidth(2,"50%");

      Collections.sort(publishers,new BookComparator(BookComparator.PUBLISHER_NAME));
      int row = 1;
      int column = 1;
      int size = publishers.size();
      int switchColumn = size / 2;
      if ( size % 2 > 0 ) {
		switchColumn++;
	}

      Iterator iter = publishers.iterator();
      while (iter.hasNext()) {
	publisherTable.add(getPublisherLink((Publisher)iter.next()),column,row);
	if ( column == switchColumn ) {
	  column++;
	  row = 1;
	}
	else {
	  row++;
	}
      }
      table.add(publisherTable,1,3);
    }
    return table;
  }

  private String getThreadDate(IWContext iwc,long time) {
    DateFormat format = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT,iwc.getCurrentLocale());
    Date date = new Date(time);
    return format.format(date);
  }

  private Link getCategoryLink(ICCategory category) {
    //try {
      Link link = new Link(category.getName());
	link.addParameter(BookBusiness.PARAMETER_STATE,BookBusiness.BOOK_COLLECTION);
	link.addParameter(BookBusiness.PARAMETER_CATEGORY_ID,((Integer)category.getPrimaryKey()).intValue());
	if ( this._styles ) {
		link.setStyle(this._linkName);
	}
      return link;
    //}
    //catch (java.io.IOException e) {
    //  return null;
    //}
  }

  private Link getAuthorLink(Author author) {
    try {
      Link link = new Link(author.getName());
	link.addParameter(BookBusiness.PARAMETER_STATE,BookBusiness.AUTHOR_VIEW);
	link.addParameter(BookBusiness.PARAMETER_AUTHOR_ID,((Integer)author.getPrimaryKey()).intValue());
	if ( this._styles ) {
		link.setStyle(this._linkName);
	}
      return link;
    }
    catch (RemoteException e) {
      return null;
    }
  }

  private Link getBookLink(Book book) {
    try {
      Link link = new Link(book.getName());
	link.addParameter(BookBusiness.PARAMETER_STATE,BookBusiness.BOOK_VIEW);
	link.addParameter(BookBusiness.PARAMETER_BOOK_ID,((Integer)book.getPrimaryKey()).intValue());
	if ( this._styles ) {
		link.setStyle(this._linkName);
	}
      return link;
    }
    catch (RemoteException e) {
      return null;
    }
  }

  private Link getPublisherLink(Publisher publisher) {
    try {
      Link link = new Link(publisher.getName());
	link.addParameter(BookBusiness.PARAMETER_STATE,BookBusiness.PUBLISHER_VIEW);
	link.addParameter(BookBusiness.PARAMETER_PUBLISHER_ID,((Integer)publisher.getPrimaryKey()).intValue());
	link.setStyle(this._linkName);
      return link;
    }
    catch (RemoteException e) {
      return null;
    }
  }

  private void setStyles() {
    if ( this._linkName == null ) {
      this._linkName = "bookLink_"+this._objectID;
    }

    if ( getParentPage() != null ) {
      getParentPage().setStyleDefinition("A."+this._linkName,this._linkStyle);
      getParentPage().setStyleDefinition("A."+this._linkName+":hover",this._linkHoverStyle);
    }
    else {
      this._styles = false;
    }
  }

  private Text formatText(String textString) {
    return formatText(textString,this._textStyle);
  }

  private Text formatText(String textString,String style) {
    Text text = new Text(textString);
      text.setFontStyle(style);
    return text;
  }

  private Table getAdminPart(IWContext iwc) {
    Table table = new Table(5,1);
      table.setCellpaddingAndCellspacing(0);
      table.setWidth(2,"6");

    Image categoryImage = this._iwcb.getImage("shared/edit.gif");
      categoryImage.setAlt(this._iwrb.getLocalizedString("categories","Categories"));
    Link categoryLink = this.getCategoryLink();
      categoryLink.setPresentationObject(categoryImage);
    table.add(categoryLink,1,1);

    Image bookImage = this._iwb.getImage("shared/book.gif");
      bookImage.setAlt(this._iwrb.getLocalizedString("add_book","Add book"));
    Image authorImage = this._iwb.getImage("shared/author.gif");
      authorImage.setAlt(this._iwrb.getLocalizedString("add_author","Add author"));
    Image publisherImage = this._iwb.getImage("shared/publisher.gif");
      publisherImage.setAlt(this._iwrb.getLocalizedString("add_publisher","Add publisher"));

    Link bookLink = new Link(bookImage);
      bookLink.setWindowToOpen(BookEditor.class);
      bookLink.addParameter(BookBusiness.PARAMETER_MODE,BookBusiness.PARAMETER_NEW);
    table.add(bookLink,3,1);

    Link authorLink = new Link(authorImage);
      authorLink.setWindowToOpen(AuthorEditor.class);
      authorLink.addParameter(BookBusiness.PARAMETER_MODE,BookBusiness.PARAMETER_NEW);
    table.add(authorLink,4,1);

    Link publisherLink = new Link(publisherImage);
      publisherLink.setWindowToOpen(PublisherEditor.class);
      publisherLink.addParameter(BookBusiness.PARAMETER_MODE,BookBusiness.PARAMETER_NEW);
    table.add(publisherLink,5,1);

    return table;
  }

  private Link getBackLink() {
    Link link = new Link(this._iwrb.getLocalizedString("back","Back"));
      link.setAsBackLink();
      link.setStyle(this._linkName);
    return link;
  }

  private Link getMoreLink(int bookID) {
    Link link = new Link(this._iwrb.getLocalizedString("more","More"));
      link.addParameter(BookBusiness.PARAMETER_STATE,BookBusiness.BOOK_VIEW);
      link.addParameter(BookBusiness.PARAMETER_BOOK_ID,bookID);
      if ( this._page != null ) {
		link.setPage(this._page);
	}
      link.setStyle(this._linkName);
    return link;
  }

  private Table getAdminButtons(Class classToOpen,String idName,int id) {
    Table table = new Table(2,1);
    table.setCellpaddingAndCellspacing(0);

    Image editImage = this._iwcb.getImage("shared/edit.gif");
      editImage.setAlt(this._iwrb.getLocalizedString("edit","Edit"));
    Link editLink = new Link(editImage);
      editLink.setWindowToOpen(classToOpen);
      editLink.addParameter(BookBusiness.PARAMETER_MODE,BookBusiness.PARAMETER_EDIT);
      editLink.addParameter(idName,id);
    Image deleteImage = this._iwcb.getImage("shared/delete.gif");
      deleteImage.setAlt(this._iwrb.getLocalizedString("delete","Delete"));
    Link deleteLink = new Link(this._iwcb.getImage("shared/delete.gif"));
      deleteLink.setWindowToOpen(classToOpen);
      deleteLink.addParameter(BookBusiness.PARAMETER_MODE,BookBusiness.PARAMETER_DELETE);
      deleteLink.addParameter(idName,id);
    table.add(editLink,1,1);
    table.add(deleteLink,2,1);

    return table;
  }

  private void getParameters(IWContext iwc) {
    iwc.removeSessionAttribute(BookBusiness.PARAMETER_IMAGE_ID);

    try {
      this._state = Integer.parseInt(iwc.getParameter(BookBusiness.PARAMETER_STATE));
    }
    catch (NumberFormatException e) {
      this._state = this._initialState;
    }

    try {
      this._authorID = Integer.parseInt(iwc.getParameter(BookBusiness.PARAMETER_AUTHOR_ID));
    }
    catch (NumberFormatException e) {
      this._authorID = -1;
    }

    try {
      this._bookID = Integer.parseInt(iwc.getParameter(BookBusiness.PARAMETER_BOOK_ID));
    }
    catch (NumberFormatException e) {
      this._bookID = -1;
    }

    try {
      this._categoryID = Integer.parseInt(iwc.getParameter(BookBusiness.PARAMETER_CATEGORY_ID));
    }
    catch (NumberFormatException e) {
      this._categoryID = -1;
    }

    try {
      this._publisherID = Integer.parseInt(iwc.getParameter(BookBusiness.PARAMETER_PUBLISHER_ID));
    }
    catch (NumberFormatException e) {
      this._publisherID = -1;
    }
  }

  public void setTextStyle(String style) {
    this._textStyle = style;
  }

  public void setHeaderStyle(String style) {
    this._headerStyle = style;
  }

  public void setLinkStyle(String style,String hoverStyle) {
    this._linkStyle = style;
    this._linkHoverStyle = hoverStyle;
  }

  public void setWidth(String width) {
    this._width = width;
  }

  public void setCategoryHeadingStyle(String style) {
    this._categoryHeadingStyle = style;
  }

  public void setHeadingStyle(String style) {
    this._headingStyle = style;
  }

  public void setInformationStyle(String style) {
    this._informationStyle = style;
  }

  public void setLayout(int layout) {
    this._state = layout;
    this._initialState = layout;
  }

  public void setPage(ICPage page) {
    this._page = page;
  }

  public void setNumberOfShown(int numberOfShown) {
    this._numberOfShown = numberOfShown;
  }

  public void setShowAuthorList(boolean showList) {
    this._showAuthorList = showList;
  }

  public void setShowPublisherList(boolean showList) {
    this._showPublisherList = showList;
  }

  private void setDefaultValues() {
    this._width = Table.HUNDRED_PERCENT;

    this._textStyle = "font-family: Arial,Helvetica,sans-serif; font-size: 11px;";
    this._headerStyle = "font-family: Verdana,Arial,Helvetica,sans-serif; font-size: 11px; font-weight: bold;";
    this._categoryHeadingStyle = "font-family: Arial,Helvetica,sans-serif; font-size: 11px; font-weight: bold; color: #000000;";
    this._headingStyle = "font-family: Arial,Helvetica,sans-serif; font-size: 11px; font-weight: bold; color: #000000;";
    this._informationStyle = "font-family: Arial,Helvetica,sans-serif; font-size: 10px;";
    this._linkStyle = "font-family: Arial,Helvetica,sans-serif; font-size: 11px; text-decoration: underline; color: #000000;";
    this._linkHoverStyle = "font-family: Arial,Helvetica,sans-serif; font-size: 11px; text-decoration: underline; color: #000000;";
    this._ratingStyle = "font-family: Verdana,Arial,Helvetica,sans-serif; font-size: 10px; font-weight: bold;";
  }

  public synchronized Object clone() {
    BookViewer obj = null;
    try {
      obj = (BookViewer)super.clone();

      if (this._myTable != null) {
	obj._myTable=(Table)this._myTable.clone();
      }
      if (this._divider != null) {
	obj._divider=(Image)this._divider.clone();
      }
    }
    catch(Exception ex) {
      ex.printStackTrace(System.err);
    }
    return obj;
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public String getCategoryType() {
    return "book";
  }

  public boolean getMultible() {
    return true;
  }
}