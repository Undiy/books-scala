package my.bookrepo.dao

import my.bookrepo.model.Book
import java.util.List

trait BookDao {
  def save(book: Book)
  def getBook(id: Long)
  def getBooks(titleFilter: String = ""): List[Book]
  def remove(book: Book)
  def update(book: Book)
  def saveOrUpdate(book: Book)
}
