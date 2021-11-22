package Lab7
import com.cra.figaro.language._
import com.cra.figaro.library.compound._
import com.cra.figaro.algorithm.sampling._
import com.cra.figaro.library.atomic.discrete._

object Departments {

  class ResearchAndDevelopment {
    val state = FromRange(0, 101)
  }

  class HumanResources {
    val state = FromRange(0, 101)
  }

  class Production(val rd: ResearchAndDevelopment, val hr: HumanResources) {
    val productQuality = Chain( rd.state,
                                (rd: Symbol) =>
                                if (rd < 50) Constant("low")
                                else if (rd < 90) Constant("ok")
                                else Constant("high")  
        )
    val noWorker = Chain( hr.state,
                                (hr: Symbol) =>
                                if (hr < 50) Constant("poorly")
                                else if (rd < 80) Constant("risky")
                                else Constant("enough") 
    val state = RichCPD( productQuality, noWorker,
                      (*, OneOf("poorly")) -> FromRange(0, 5),
                      (OneOf("low"), *) -> FromRange(5, 21),
                      (OneOf("ok"), OneOf("risky", "enough")) -> FromRange(21, 50),
                      (OneOf("high"), OneOf("risky")) -> FromRange(50, 86),
                      (OneOf("high"), OneOf("enough")) -> FromRange(86, 101)
        )       
  }

  class Sales(val p: Production) {
    val state = Chain( p.state,
                                (p: Symbol) =>
                                if (p < 4) Constant("poorly")
                                else if (p < 50) Constant("low")
                                else if (p < 86) Constant("ok")   
                                else Constant("high")
        )
  }

  class Finance(val hr: HumanResources, val s: Sales) {
    val noWorker = Chain( hr.state,
                                (hr: Symbol) =>
                                if (hr < 50) Constant("poorly")
                                else if (rd < 80) Constant("risky")
                                else Constant("enough") 
    val state = RichCPD( noWorker, s.state,
                      (*, OneOf("poorly")) -> FromRange(0, 5),
                      (OneOf("poorly"), OneOf("low")) -> FromRange(10, 31),
                      (OneOf("risky", "enough"), OneOf("low")) -> FromRange(5, 10),
                      (*, OneOf("ok")) -> FromRange(31, 50),
                      (OneOf("enough"), OneOf("high")) -> FromRange(50, 86),
                      (OneOf("low", "risky"), OneOf("high")) -> FromRange(86, 101)
        )
  }

  class Firm(val rd: ResearchAndDevelopment, val hr: HumanResources, val p: Production, val s: Sales, val f: Finance) {
    val noWorker = Chain( hr.state,
                                (hr: Symbol) =>
                                if (hr < 50) Constant("poorly")
                                else if (rd < 80) Constant("risky")
                                else Constant("enough")
    val productQuality = Chain( rd.state,
                                (rd: Symbol) =>
                                if (rd < 50) Constant("low")
                                else if (rd < 90) Constant("ok")
                                else Constant("high")  
        )                             
    val health = 
  }

  def main(args: Array[String]) {
    val rd = new ResearchAndDevelopment()
    val hr = new HumanResources()
    val p = new Production(rd, hr)
    val s = new Sales(p)
    val f = new Finance(hr, s)
    val firm = new Firm(rd, hr, p, s, f)

  }
}