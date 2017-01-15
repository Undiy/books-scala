package my.bookrepo.model

import scala.beans.BeanProperty
import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.Id
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.ManyToOne
import javax.persistence.JoinColumn


@Entity
@Table(name = "books")
class Book {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty
  var id: Long = _

  @BeanProperty
  var title: String = _

  @BeanProperty
  @ManyToOne
  @JoinColumn(name = "author_id", nullable = false)
  var author: Author = _

  def this(title: String, author: Author) {
    this()
    this.title = title
    this.author = author
  }
}
