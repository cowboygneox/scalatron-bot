import org.specs2.mutable.Specification

class CommandParserTest extends Specification {
  val parser = new CommandParser

  "A Command Parser" should {
    "Parse the Welcome command" in {
      val string = "Welcome(name=testBot,apocalypse=1005,round=3)"
      parser.parseInput(string) must beEqualTo(Set(Welcome("testBot", 1005, 3)))
    }

    "Parse the Goodbye command" in {
      val string = "Goodbye(energy=45)"
      parser.parseInput(string) must beEqualTo(Set(Goodbye(45)))
    }

    "Parse the React command" in {
      val string = "React(generation=10,name=Bill,time=50,view=View,energy=100,master=1:2,collision=2:3)"
      parser.parseInput(string) must beEqualTo(Set(React(10, "Bill", 50, "View", 100, Direction(1, 2), Direction(2, 3))))
    }

    "Parse multiple server commands" in {
      val string = "Welcome(name=testBot,apocalypse=1005,round=3)|Goodbye(energy=45)"
      parser.parseInput(string) must beEqualTo(Set(Goodbye(45), Welcome("testBot", 1005, 3)))
    }
  }
}
