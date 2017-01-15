package my.bookrepo.util

import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration
import org.hibernate.Session

object HibernateUtil {
  lazy val sessionFactory: SessionFactory = {
    try {
      new Configuration()
        .configure
        .buildSessionFactory;
    } catch {
      case ex: Throwable => {
        println("Failed to create sessionFactory object." + ex)
        throw new ExceptionInInitializerError(ex);
      }
    }
  }

  // utility wrapper for session and transaction per request
  def withTransaction[T](f: Session => T) = {
    val session = HibernateUtil.sessionFactory.openSession
    val t = session.beginTransaction
    try {
      val result = f(session)
      t.commit()
      result
    } catch {
      case ex: RuntimeException => println("Exception: " + ex); t.rollback(); throw ex
    } finally {
      session.close()
    }
  }
}
