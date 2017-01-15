package my.bookrepo.model

import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.Id
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import scala.beans.BeanProperty

@Entity
@Table(name="authors")
class Author {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty
  var id: Long = _

  @BeanProperty
  var firstName: String = _
  @BeanProperty
  var middleName: String = _
  @BeanProperty
  var lastName: String = _

  def this(first: String, middle: String, last: String) {
    this()
    firstName = first
    middleName = middle
    lastName = last
  }

  override def toString = List(Option(firstName), Option(middleName), Option(lastName)).flatten.mkString(" ")
}
