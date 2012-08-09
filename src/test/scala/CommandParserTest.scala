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
  }
}
