package my.bookrepo.controller

import javax.faces.bean.ManagedBean
import javax.faces.bean.SessionScoped
import javax.faces.model.ListDataModel
import my.bookrepo.dao.{ AuthorDaoImpl, BookDaoImpl }
import my.bookrepo.model.{ Author, Book }
import scala.beans.BeanProperty

import collection.JavaConversions._

@ManagedBean
@SessionScoped
@SerialVersionUID(1L)
class BookController extends Serializable {

  @BeanProperty
  var titleFilter: String = ""
  @BeanProperty
  var exactMatch: Boolean = false
  @BeanProperty
  val booksDataModel = new ListDataModel[Book]()
  @BeanProperty
  var book: Book = _
  @BeanProperty
  var firstName: String = _
  @BeanProperty
  var middleName: String = _
  @BeanProperty
  var lastName: String = _
  @BeanProperty
  var authorIndex = 0

  // a map to cache authors (index -> author)
  private var authorsMap: Map[Int, Author] = Map()

  // convert map to java map Entry<Integer, Author> set to populate selectOneMenu
  def getAuthorsMapEntries = mapAsJavaMap(authorsMap).entrySet

  private def goToIndex = "index?faces-redirect=true"

  private def goToEdit = {
    firstName = ""
    middleName = ""
    lastName = ""
    "edit??faces-redirect=true"
  }

  // update booksDataModel - query books table
  def updateBooks: String = {
    println("titleFilter=" + titleFilter)

    val correctedFilter =
      if (!exactMatch) {
        titleFilter.split(" ").filter(!_.isEmpty).mkString("%", "%", "%")
      } else {
        titleFilter
      }

    val bookList = BookDaoImpl.getBooks(correctedFilter)
    booksDataModel.setWrappedData(bookList)
    goToIndex
  }
  updateBooks

  // query authors table and create authors map cache from result
  def updateAuthors: String = {
    // convert to scala list
    val authors = AuthorDaoImpl.getAuthors.toList
    // start indexes from 1; 0 would mark that nothing is selected
    authorsMap = (authors.indices.map(_ + 1) zip authors).toMap
    authorIndex = 0
    goToEdit
  }

  // add a new author instance (it is not saved to db yet, just added to map cache)
  def addAuthor: String = {
    val author = new Author(firstName, middleName, lastName)
    // get next index
    val key = if (authorsMap.isEmpty) 1 else authorsMap.keys.max + 1
    authorsMap += key -> author
    authorIndex = key
    goToEdit
  }


  def editBook(book: Book) = {
    this.book = book
    val result = updateAuthors
    // lookup author index for book author
    authorIndex = authorsMap.find(_._2.id == book.author.id).map(_._1).getOrElse(0)
    result
  }

  def editNewBook = {
    this.book = new Book()
    authorIndex = 0
    updateAuthors
  }

  def saveOrUpdateBook(book: Book) = {
    book.author = authorsMap(authorIndex)
    BookDaoImpl.saveOrUpdate(book)
    updateBooks
  }

  def removeBook(book: Book) = {
    BookDaoImpl.remove(book)
    updateBooks
  }
}
