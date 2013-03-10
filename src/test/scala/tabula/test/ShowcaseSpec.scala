package tabula.test

import tabula._
import Tabula._
import shapeless.HList._
import org.specs._
import com.github.nscala_time.time.Imports._
import org.apache.commons.lang3.text.WordUtils.capitalize
// a pretend data model

case class UselessItem(name: String, price: Double)
case class PretentiousPurveyor(name: String, location: String)
case class Purchase(item: UselessItem, date: Option[DateTime], from: PretentiousPurveyor)

// some test data

object Items {
  val justSomeCoatRack = UselessItem("honest abe", 90.39)
  val cheeseParkingSpot = UselessItem("fancy cheese board", 39.95)
  val whatIsThis = UselessItem("faux professional tool pouch", 48.00)
}

object PlacesNormalPeopleDoNotGo {
  val SchizophrenicMonkey = PretentiousPurveyor("Tinkering Monkey", "SF Bay Area")
  val BrooklynSlateWtf = PretentiousPurveyor("Brooklyn Slate Co.", "Brokelyn")
  val HeritageInsanityCo = PretentiousPurveyor("Heritage Leather Co.", "Somewhere in Cali")
}

object Purchases {
  import Items._
  import PlacesNormalPeopleDoNotGo._

  val * = {
    Purchase(item = justSomeCoatRack, date = Some(DateTime.now), from = SchizophrenicMonkey) ::
      Purchase(item = cheeseParkingSpot, date = None, from = BrooklynSlateWtf) ::
      Purchase(item = whatIsThis, date = Some(DateTime.now), from = HeritageInsanityCo) ::
      Nil
  }
}

// column descriptions

// what we bought
object ItemName extends Column((p: Purchase) => p.item.name)

// how much we paid
object ItemPrice extends Column((p: Purchase) => p.item.price)

// where we bought it
object PurchaseLocation extends Column((p: Purchase) => p.from.location)

// date of purchase
object DateOfPurchase extends Column((p: Purchase) => p.date)

// transformer column: capitalize words
object Capitalize extends Column(capitalize)

// let's do it!
class ShowcaseSpec extends Specification {
  "a purchase history" should {
    val columns =
      "Item Name" -> (ItemName | Capitalize) |:
        "Item Price" -> ItemPrice |:
        "Bought At" -> PurchaseLocation |:
        "Date of Purchase" -> DateOfPurchase

    println(columns)

    val rowF = row(columns)

    "print out a list of things we've bought" in {
      for {
        purchase <- Purchases.*
        cells = rowF(purchase)
      } println(cells.map(CSV).toList.mkString(","))
    }
  }
}
