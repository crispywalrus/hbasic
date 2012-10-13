package net.crispywalrus.drivers.hbase

import org.hbase.async._
import com.stumbleupon.async.{ Callback, Deferred }
import com.twitter.util.{ Future, Promise, Return, Throw, Eval }

trait Exists 
case class TableExists(table:String) extends Exists
case class TableFamilyExists(table:String,ns:String) extends Exists

case class Put(key: String, ns: String, columns: List[Tuple2[String, String]]) {
  def request(table: String): PutRequest = {
    val qualifiers: List[Array[Byte]] = columns map { t => t._1.getBytes() }
    val values: List[Array[Byte]] = columns map { t => t._2.getBytes() }
    new PutRequest(table.getBytes, key.getBytes, ns.getBytes, qualifiers.toArray, values.toArray)
  }
}

case class Get(key: String, ns: Option[String]) {
  def request(table: String): GetRequest = {
    val get = new GetRequest(table, key)
    ns match {
      case Some(family) => { get.family(family); get }
      case None => get
    }
  }
}

case class Delete(key: String, ns: Option[String] = None ) {
  def request(table: String): DeleteRequest = ns match {
    case None => new DeleteRequest(table, key)
    case Some(family) => new DeleteRequest(table, key, family)
  }
}

class HBaseManager(val table: String, val quorum: String = "localhost") {

  val client = new HBaseClient(quorum)

  /**
   * This let's us pass inline functions to the deferred
   * object returned by most asynchbase methods
   */
  implicit def conv[A, B](f: B ⇒ A): Callback[A, B] = {
    new Callback[A, B]() {
      def call(b: B) = f(b)
    }
  }

  /** Converts a Deferred into a twitter Future.  */
  implicit def futureFromDeferred[A](d: Deferred[A]): Future[A] = {
    val promise = new Promise[A]

    d.addBoth(new Callback[Unit, A] {
      def call(arg: A) = promise() = arg match {
        case e: Throwable => Throw(e)
        case _ => Return(arg)
      }
    })

    promise
  }

  def get(get: Get): Future[java.util.List[KeyValue]] = client.get(get.request(table))

  def put(put: Put) = try { Left(client.put(put.request(table)).map({ r => println("" + r) })) } catch { case t => Right(t) }

  def delete(delete:Delete) = try { Left(client.delete(delete.request(table))) } catch { case t => Right(t) }

  def exists(exists:Exists) = exists match {
    case e:TableExists => try { Left(client.ensureTableExists(e.table)) } catch { case t => Right(t) }
    case x:TableFamilyExists => try { Left(client.ensureTableFamilyExists(x.table,x.ns)) } catch { case t => Right(t) }
  }

  // check to see that our tables are created and contain the family columns we need
  // client.ensureTableFamilyExists("test-files", "family").addCallback({
  //   o: Object ⇒ println("Exists succeeded")
  // }).addErrback({
  //   e: Exception ⇒
  //     {
  //       println("Table Assertion Error")
  //       println(e.getMessage())
  //     }
  // }).join // wait for the assertion to continue

}
