package my.bookrepo.dao

import my.bookrepo.model.Author
import java.util.List

import my.bookrepo.util.HibernateUtil

trait AuthorDao {
  def save(author: Author)
  def getAuthor(id: Long)
  def getAuthors(): List[Author]
  def remove(author: Author)
  def update(author: Author)
  def saveOrUpdate(author: Author)
}
