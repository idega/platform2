package com.idega.block.book.business;

import javax.ejb.*;
import com.idega.util.IWTimestamp;
import com.idega.presentation.ui.SelectionBox;
import com.idega.presentation.ui.DropdownMenu;
import java.util.*;
import com.idega.data.*;
import java.sql.SQLException;
import java.rmi.RemoteException;
import com.idega.presentation.Image;
import com.idega.block.book.data.*;
import com.idega.block.category.business.CategoryFinder;
import com.idega.block.category.data.ICCategory;

public class BookBusiness {

  public static final int AUTHOR_VIEW = 1;
  public static final int PUBLISHER_VIEW = 2;
  public static final int BOOK_VIEW = 3;
  public static final int BOOK_COLLECTION = 4;
  public static final int CATEGORY_COLLECTION = 5;
  public static final int BOOK_CATEGORY_COLLECTION = 6;
  public static final int REVIEW_VIEW = 7;
  public static final int SEARCH = 8;
  public static final int NEWEST_BOOKS = 9;

  public static final String PARAMETER_AUTHOR_ID = "bo_a_id";
  public static final String PARAMETER_BOOK_ID = "bo_b_id";
  public static final String PARAMETER_CATEGORY_ID = "bo_c_id";
  public static final String PARAMETER_PUBLISHER_ID = "bo_p_id";
  public static final String PARAMETER_REVIEW_ID = "bo_r_id";
  public static final String PARAMETER_STATE = "bo_st";

  public static final String PARAMETER_MODE = "bo_mode";
  public static final String PARAMETER_NEW = "new";
  public static final String PARAMETER_DELETE = "delete";
  public static final String PARAMETER_EDIT = "edit";
  public static final String PARAMETER_SAVE = "save";
  public static final String PARAMETER_CLOSE = "close";

  public static final String PARAMETER_NAME = "bo_name";
  public static final String PARAMETER_DESCRIPTION = "bo_desc";
  public static final String PARAMETER_YEAR = "bo_year";
  public static final String PARAMETER_CATEGORIES = "bo_cat";
  public static final String PARAMETER_AUTHORS = "bo_aut";
  public static final String PARAMETER_IMAGE_ID = "bo_i_id";
  public static final String PARAMETER_RATING = "bo_rat";

  private static BookBusiness instance;

  private BookBusiness(){
  }

  public static BookBusiness getBookBusinessInstace(){
    if(instance==null){
      instance = new BookBusiness();
    }
    return instance;
  }

  public Book getBook(int bookID) {
    try {
      return getBookHome().findByPrimaryKey(new Integer(bookID));
    }
    catch (FinderException e) {
      return null;
    }
    catch (RemoteException e) {
      return null;
    }
  }

  public Author getAuthor(int authorID) {
    try {
      return getAuthorHome().findByPrimaryKey(new Integer(authorID));
    }
    catch (FinderException e) {
      return null;
    }
    catch (RemoteException e) {
      return null;
    }
  }

  public Publisher getPublisher(int publisherID) {
    try {
      return getPublisherHome().findByPrimaryKey(new Integer(publisherID));
    }
    catch (FinderException e) {
      return null;
    }
    catch (RemoteException e) {
      return null;
    }
  }

  public Review getReview(int reviewID) {
    try {
      return getReviewHome().findByPrimaryKey(new Integer(reviewID));
    }
    catch (FinderException e) {
      return null;
    }
    catch (RemoteException e) {
      return null;
    }
  }

  public AuthorHome getAuthorHome(){
    try {
      return (AuthorHome) IDOLookup.getHome(Author.class);
    }
    catch (RemoteException rme) {
      return null;
    }
  }

  public BookHome getBookHome(){
    try {
      return (BookHome) IDOLookup.getHome(Book.class);
    }
    catch (RemoteException rme) {
      return null;
    }
  }

  public PublisherHome getPublisherHome(){
    try {
      return (PublisherHome) IDOLookup.getHome(Publisher.class);
    }
    catch (RemoteException rme) {
      return null;
    }
  }

  public ReviewHome getReviewHome(){
    try {
      return (ReviewHome) IDOLookup.getHome(Review.class);
    }
    catch (RemoteException rme) {
      return null;
    }
  }

  public void saveBook(int bookID,String bookName,String bookDescription,String bookYear,String bookImageID,String bookPublisher,String[] bookAuthors,String[] bookCategories) {
    try {
      Book book = getBookHome().create();
      if ( bookID != -1 )
	book = getBookHome().findByPrimaryKey(new Integer(bookID));

      book.setName(bookName);
      book.setDescription(bookDescription);
      if ( bookID == -1 )
	book.setDateAdded(new IWTimestamp().getTimestamp());

      try {
	book.setYear(Integer.parseInt(bookYear));
      }
      catch (NumberFormatException e) {
	book.setYear(0);
      }

      try {
	int imageID = Integer.parseInt(bookImageID);
	if ( imageID != -1 )
	  book.setImageID(imageID);
      }
      catch (NumberFormatException e) {
      }

      try {
	int publisherID = Integer.parseInt(bookPublisher);
	if ( publisherID != -1 )
	  book.setPublisherID(publisherID);
      }
      catch (NumberFormatException e) {
      }

      book.store();

      book.removeFromAuthor();
      book.removeFromCategory();

      addAuthorsToBook(book,bookAuthors);
      addCategoriesToBook(book,bookCategories);
    }
    catch (CreateException ce) {
      ce.printStackTrace(System.err);
    }
    catch (FinderException fe) {
      fe.printStackTrace(System.err);
    }
    catch (RemoteException rme) {
      rme.printStackTrace(System.err);
    }
    catch (IDOException ie) {
      ie.printStackTrace(System.err);
    }
  }

  public void deleteBook(int bookID) {
    try {
      getBook(bookID).remove();
    }
    catch (RemoveException re) {
      re.printStackTrace(System.err);
    }
    catch (Exception re) {
      re.printStackTrace(System.err);
    }
  }

  public void savePublisher(int publisherID,String name,String description,String image) {
    try {
      Publisher publisher = getPublisherHome().create();
      if ( publisherID != -1 )
	publisher = getPublisherHome().findByPrimaryKey(new Integer(publisherID));

      publisher.setName(name);
      publisher.setDescription(description);

      try {
	int imageID = Integer.parseInt(image);
	if ( imageID != -1 )
	  publisher.setImageID(imageID);
      }
      catch (NumberFormatException e) {
      }

      publisher.store();
    }
    catch (CreateException ce) {
      ce.printStackTrace(System.err);
    }
    catch (FinderException fe) {
      fe.printStackTrace(System.err);
    }
    catch (RemoteException rme) {
      rme.printStackTrace(System.err);
    }
  }

  public void deletePublisher(int publisherID) {
    try {
      getPublisher(publisherID).remove();
    }
    catch (IDORemoveException ire) {
      ire.printStackTrace(System.err);
    }
    catch (RemoveException re) {
      re.printStackTrace(System.err);
    }
  }

  public void saveAuthor(int authorID,String name,String description,String image) {
    try {
      Author author = getAuthorHome().create();
      if ( authorID != -1 )
	author = getAuthorHome().findByPrimaryKey(new Integer(authorID));

      author.setName(name);
      author.setDescription(description);
      if ( authorID == -1 )
	author.setDateAdded(new IWTimestamp().getTimestamp());

      try {
	int imageID = Integer.parseInt(image);
	if ( imageID != -1 )
	  author.setImageID(imageID);
      }
      catch (NumberFormatException e) {
      }

      author.store();
    }
    catch (CreateException ce) {
      ce.printStackTrace(System.err);
    }
    catch (FinderException fe) {
      fe.printStackTrace(System.err);
    }
    catch (RemoteException rme) {
      rme.printStackTrace(System.err);
    }
  }

  public void deleteAuthor(int authorID) {
    try {
      getAuthor(authorID).remove();
    }
    catch (IDORemoveException ire) {
      ire.printStackTrace(System.err);
    }
    catch (RemoveException re) {
      re.printStackTrace(System.err);
    }
  }

  public void saveReview(int reviewID,int bookID,String name,String bookReview,String rating) {
    try {
      Review review = getReviewHome().create();
      if ( reviewID != -1 )
	review = getReviewHome().findByPrimaryKey(new Integer(reviewID));

      review.setName(name);
      review.setReview(bookReview);
      if ( reviewID == -1 )
	review.setDateAdded(new IWTimestamp().getTimestamp());
      if ( bookID != -1 )
	review.setBookID(bookID);

      try {
	review.setRating(Integer.parseInt(rating));
      }
      catch (NumberFormatException ex) {
	review.setRating(5);
      }

      review.store();
    }
    catch (CreateException ce) {
      ce.printStackTrace(System.err);
    }
    catch (FinderException fe) {
      fe.printStackTrace(System.err);
    }
    catch (RemoteException rme) {
      rme.printStackTrace(System.err);
    }
  }

  public void deleteReview(int reviewID) {
    try {
      getReview(reviewID).remove();
    }
    catch (RemoveException re) {
      re.printStackTrace(System.err);
    }
    catch (Exception re) {
      re.printStackTrace(System.err);
    }
  }

  public int getNumberOfBooksInCategory(int categoryID) {
    try {
      return getBookHome().getNumberOfBooks(categoryID);
    }
    catch (RemoteException rme) {
      return 0;
    }
    catch (IDOException ie) {
      return 0;
    }
  }

  public long getLastAddedTime(int categoryID) {
    try {
      Vector vector = new Vector(getBookHome().findNewestBookByCategory(categoryID));
      if ( vector != null && vector.size() > 0 )
	return ((Book) vector.get(0)).getDateAdded().getTime();
      return 0;
    }
    catch (FinderException fe) {
      return 0;
    }
    catch (RemoteException rme) {
      return 0;
    }
  }

  public Image getImage(int imageID) {
    try {
      return new Image(imageID);
    }
    catch (SQLException e) {
      return null;
    }
  }

  public void addCategoriesToBook(Book book,String[] categoryIDs) throws IDOException,RemoteException {
    if ( categoryIDs != null && categoryIDs.length > 0 ) {
      for ( int a = 0; a < categoryIDs.length; a++ ) {
	try {
	  book.addToCategory(CategoryFinder.getInstance().getCategory(Integer.parseInt(categoryIDs[a])));
	}
	catch (IDOException ide) {
	}
      }
    }
  }

  public void addAuthorsToBook(Book book,String[] authors) throws IDOException,RemoteException {
    if ( authors != null && authors.length > 0 ) {
      for ( int a = 0; a < authors.length; a++ ) {
	try {
	  book.addToAuthor(getAuthor(Integer.parseInt(authors[a])));
	}
	catch (IDOException ide) {
	}
      }
    }
  }

  public DropdownMenu getPublisherMenu() throws FinderException,RemoteException {
    Vector collection = new Vector(getPublisherHome().findAllPublishers());
    DropdownMenu menu = new DropdownMenu(PARAMETER_PUBLISHER_ID);
      menu.addMenuElement(-1,"");

    if ( collection != null ) {
      Collections.sort(collection,new BookComparator(BookComparator.PUBLISHER_NAME));
      Iterator iter = collection.iterator();
      while (iter.hasNext()) {
	Publisher publisher = (Publisher) iter.next();
	menu.addMenuElement(((Integer)publisher.getPrimaryKey()).intValue(),publisher.getName());
      }
    }

    return menu;
  }

  public SelectionBox getAuthorMenu(int bookID) throws FinderException,RemoteException {
    Vector collection = new Vector(getAuthorHome().findAllAuthors());
    Collection authors = getAuthorHome().findAllAuthorsByBook(bookID);
    SelectionBox menu = new SelectionBox(PARAMETER_AUTHORS);

    if ( collection != null ) {
      Collections.sort(collection,new BookComparator(BookComparator.AUTHOR_NAME));
      Iterator iter = collection.iterator();
      while (iter.hasNext()) {
	Author author = (Author) iter.next();
	menu.addMenuElement(((Integer)author.getPrimaryKey()).intValue(),author.getName());
	if ( authors != null && authors.contains(author) )
	  menu.setSelectedElement(String.valueOf(((Integer)author.getPrimaryKey()).intValue()));
      }
    }

    return menu;
  }

  public SelectionBox getCategoryMenu(Book book) throws IDOException,FinderException,RemoteException {
    List collection = CategoryFinder.getInstance().listOfCategories("book");
    Collection categories = null;
    if ( book != null ) categories = book.findRelatedCategories();
    SelectionBox menu = new SelectionBox(PARAMETER_CATEGORIES);

    if ( collection != null ) {
      Collections.sort(collection,new BookComparator(BookComparator.CATEGORY_NAME));
      Iterator iter = collection.iterator();
      while (iter.hasNext()) {
	ICCategory category = (ICCategory) iter.next();
	menu.addMenuElement(((Integer)category.getPrimaryKey()).intValue(),category.getName());
	if ( categories != null && categories.contains(category) )
	  menu.setSelectedElement(String.valueOf(((Integer)category.getPrimaryKey()).intValue()));
      }
    }

    return menu;
  }
}