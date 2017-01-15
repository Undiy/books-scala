package my.bookrepo.dao

import my.bookrepo.model.Book
import my.bookrepo.util.HibernateUtil
import org.hibernate.criterion.Restrictions
import org.hibernate.StaleStateException
import java.util.List

object BookDaoImpl extends BookDao {
  def save(book: Book) = HibernateUtil.withTransaction(session => {
      session.saveOrUpdate(book.author)
      session.save(book)
  })
  def getBook(id: Long) = HibernateUtil.withTransaction(_.load(classOf[Book], id))

  def getBooks(titleFilter: String): List[Book] = HibernateUtil.withTransaction(session => {
    // if titleFilter is empty or null - select all
    (if (Option(titleFilter).getOrElse("").isEmpty) {
      session.createQuery("from Book").list
    } else {
      session.createCriteria(classOf[Book])
        .add(Restrictions.ilike("title", titleFilter)).list
    }).asInstanceOf[List[Book]]
  })

  def remove(book: Book) = {
    try {
      HibernateUtil.withTransaction(_.delete(book))
    } catch {
      //StateStaleException means object already does not exist in DB
      case ex: StaleStateException => ()
    }
  }

  def update(book: Book) = HibernateUtil.withTransaction(_.update(book))
  def saveOrUpdate(book: Book) = HibernateUtil.withTransaction(session => {
      // since book.author has NotNull constriction, author instance has to be saved before book referencing it
      session.saveOrUpdate(book.author)
      session.saveOrUpdate(book)
  })
}
