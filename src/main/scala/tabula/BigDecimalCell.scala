package tabula

import java.math.{MathContext, RoundingMode}
import java.text.DecimalFormat
import com.novus.salat.annotations._

object BigDecimalCell {
  val DefaultRoundingMode = RoundingMode.HALF_UP
  val DefaultMathContext = new MathContext(14, DefaultRoundingMode)

  val NakedOne = BigDecimal(1, DefaultMathContext)
  val NakedZero = BigDecimal(0, DefaultMathContext)

  val One = Some(NakedOne)
  val Zero = Some(NakedZero)

  def apply(i: Int): BigDecimalCell = BigDecimalCell(Some(BigDecimal(i)))
  def Fractional = new DecimalFormat("#,##0.00;-#,##0.00")
  def Whole = new DecimalFormat("#,##0;-#,##0")
  def Scientific = new DecimalFormat("0.###E0;-0.###E0")
}

case class BigDecimalCell(value: Option[BigDecimal],
                            @Ignore mathContext: MathContext = BigDecimalCell.DefaultMathContext,
                            multiplier: Option[BigDecimal] = BigDecimalCell.One,
                            @Ignore formatter: DecimalFormat = BigDecimalCell.Fractional) extends Cell with HasValue with LinkableCell {
  import BigDecimalCell._

  lazy val scaled = multiplier.flatMap(m => value.map(v => m * v)).map(_(mathContext))

  def format: String = scaled.map(_.underlying.toPlainString).getOrElse("0.0")

  lazy val human = formatter.format(scaled.getOrElse(NakedZero))
  lazy val scientific = BigDecimalCell.Scientific.format(scaled.getOrElse(NakedZero))

  def linkTo(u: String, t: Option[String] = None, cs: List[String] = Nil) = {
    new BigDecimalCell(value, mathContext, multiplier, formatter) with LinkedCell {
      val url = u
      val text = t
      val classes = cs
    }
  }
}