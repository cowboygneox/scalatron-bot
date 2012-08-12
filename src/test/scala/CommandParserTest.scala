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
      parser.parseInput(string) must beEqualTo(Set(React(10, "Bill", 50, "View", 100, Some(Direction(1, 2)), Some(Direction(2, 3)))))
    }

    "Parse the React command without a master param" in {
      val string = "React(generation=10,name=Bill,time=50,view=View,energy=100,collision=2:3)"
      parser.parseInput(string) must beEqualTo(Set(React(10, "Bill", 50, "View", 100, None, Some(Direction(2, 3)))))
    }

    "Parse the React command without a collision param" in {
      val string = "React(generation=10,name=Bill,time=50,view=View,energy=100,master=1:2)"
      parser.parseInput(string) must beEqualTo(Set(React(10, "Bill", 50, "View", 100, Some(Direction(1, 2)), None)))
    }

    "Parse the React command without a master or collision param" in {
      val string = "React(generation=10,name=Bill,time=50,view=View,energy=100)"
      parser.parseInput(string) must beEqualTo(Set(React(10, "Bill", 50, "View", 100)))
    }

    "Parse multiple server commands" in {
      val string = "Welcome(name=testBot,apocalypse=1005,round=3)|Goodbye(energy=45)|React(generation=10,name=Bill,time=50,view=View,energy=100)"
      parser.parseInput(string) must beEqualTo(Set(Goodbye(45), Welcome("testBot", 1005, 3), React(10, "Bill", 50, "View", 100)))
    }

    "Extract the map" in {
      val answer = parser.extractMap("key1=value1,key2=value2,key3=4:5,key4=value4")
      answer must beEqualTo(Map("key1" -> "value1", "key2" -> "value2", "key3" -> "4:5", "key4" -> "value4"))
    }
  }
}
