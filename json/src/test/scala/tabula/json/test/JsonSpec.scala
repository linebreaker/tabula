package tabula.json.test

import tabula._
import tabula.json._
import tabula.test._
import org.specs._
import org.json4s._
import shapeless.HList._
import scala.xml._

object MyJSON extends JSON {
  implicit object NodeSeqFormatter extends Formatter[NodeSeq] {
    type Local = JString
    def apply(cell: Cell[NodeSeq]) = JString(cell.value.map(_ \\ "title").map(_.toString).getOrElse(""))
  }
}

class JsonSpec extends Specification {
  import ShowcaseSpec._
  "a JSON output" should {
    "produce JSON" in {
      val jos = Purchases.*.map(purchase => JArray(rowF(purchase).map(MyJSON).toList))
      println(jos)
    }
  }
}
