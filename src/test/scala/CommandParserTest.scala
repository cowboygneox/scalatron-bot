import org.specs2.mutable.Specification

class CommandParserTest extends Specification {
  val parser = new CommandParser

  "The Command Parser" should {
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

    "Parse the React command with user defined properties" in {
      val string = "React(generation=10,name=Bill,time=50,view=View,energy=100,userProperty1=aValue,userProperty2=anotherValue)"
      parser.parseInput(string) must beEqualTo(Set(React(10, "Bill", 50, "View", 100, userProperties = Some(Map("userProperty1" -> "aValue", "userProperty2" -> "anotherValue")))))
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

  "The Command Encoder" should {
    "Encode the move command" in {
      val move = Move(Direction(-1,1))
      parser.encodeOutput(Set(move)) must beEqualTo("Move(direction=-1:1)")
    }

    "Encode the Explode command" in {
      val explode = Explode(5)
      parser.encodeOutput(Set(explode)) must beEqualTo("Explode(size=5)")
    }

    "Encode the Set command" in {
      val setCommand = SetProperties(Map("user1" -> "v1", "user2" -> "v2", "user3" -> "5:3:2"))
      parser.encodeOutput(Set(setCommand)) must beEqualTo("Set(user1=v1,user2=v2,user3=5:3:2)")
    }

    "Encode a simple Spawn command" in {
      val spawn = Spawn(Direction(1,2), "unit1", 100)
      parser.encodeOutput(Set(spawn)) must beEqualTo("Spawn(direction=1:2,name=unit1,energy=100")
    }

    "Encode the Spawn command" in {
      val spawn = Spawn(Direction(1,2), "unit1", 100, Some(Map("user1" -> "value1", "key" -> "value")))
      parser.encodeOutput(Set(spawn)) must beEqualTo("Spawn(direction=1:2,name=unit1,energy=100,user1=value1,key=value")
    }

    "Encode the Say command" in {
      val say = Say("Text Here")
      parser.encodeOutput(Set(say)) must beEqualTo("Say(text=Text Here)")
    }

    "Encode the Status command" in {
      val status = Status("I'm Thinking...")
      parser.encodeOutput(Set(status)) must beEqualTo("Status(text=I'm Thinking...)")
    }

    "Encode the Log command" in {
      val log = Log("Error - Bad stuff happened")
      parser.encodeOutput(Set(log)) must beEqualTo("Log(text=Error - Bad stuff happened)")
    }

    "Encode the MarkCell command" in {
      val markCell = MarkCell(Direction(-2, 4), "#ff8800")
      parser.encodeOutput(Set(markCell)) must beEqualTo("MarkCell(position=-2:4,color=#ff8800")
    }

    "Encode multiple commands" in {
      val move = Move(Direction(-1,1))
      val explode = Explode(5)
      parser.encodeOutput(Set(move, explode)) must beEqualTo("Move(direction=-1:1),Explode(size=5)")
    }
  }
}
