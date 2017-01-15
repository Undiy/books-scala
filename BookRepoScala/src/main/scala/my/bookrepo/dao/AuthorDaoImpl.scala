package my.bookrepo.dao

import my.bookrepo.model.Author
import java.util.List

import my.bookrepo.util.HibernateUtil

object AuthorDaoImpl extends AuthorDao {
  def save(author: Author) = HibernateUtil.withTransaction(_.save(author))
  def getAuthor(id: Long) = HibernateUtil.withTransaction(_.load(classOf[Author], id))
  def getAuthors(): List[Author] = HibernateUtil.withTransaction(
    _.createQuery("from Author").list.asInstanceOf[List[Author]])
  def remove(author: Author) = HibernateUtil.withTransaction(_.delete(author))
  def update(author: Author) = HibernateUtil.withTransaction(_.update(author))
  def saveOrUpdate(author: Author) = HibernateUtil.withTransaction(_.saveOrUpdate(author))
}
