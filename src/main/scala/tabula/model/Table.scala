/**
 */
package tabula.model

import com.mongodb.casbah.Imports._
import org.apache.poi.hssf.usermodel.HSSFWorkbook

case class Table(_id: ObjectId = new ObjectId,
                 tableSetId: Option[ObjectId] = None,
                 name: String,
                 header: Row,
                 rows: List[Row],
                 idx: Option[Int] = None) extends AsCSV with AsXLS {

  override def hashCode = name.hashCode
  lazy val asCSV = (header :: rows ::: Nil).map(_.asCSV).mkString("\n")
  lazy val asXLS: HSSFWorkbook = asXLS(false)
  def asXLS(autoSize: Boolean = false) =
    TableSet(name = name, tables = List(this)).asXLS(autoSize)
}