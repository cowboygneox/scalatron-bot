class CommandParser {
  def parseInput(input: String): Set[ServerCommand] = {
    val index = input.indexOf('|')
    if (index > 0) {
      val split = input.split('|')
      split.map {
        parseCommand(_)
      }.toSet
    }
    else
      Set(parseCommand(input))
  }

  def encodeOutput(clientCommands: Set[ClientCommand]) = {
    clientCommands.map { clientCommand =>
      clientCommand match {
        case Move(direction) => "Move(direction=%d:%d)".format(direction.x, direction.y)
        case Explode(size) => "Explode(size=%d)".format(size)
        case SetProperties(userProperties) => "Set(%s)".format(userProperties.map { a => "%s=%s".format(a._1, a._2) }.mkString(","))
        case Spawn(direction, name, energy, properties) => {
          "Spawn(direction=%s:%s,name=%s,energy=%s,%s".format(direction.x, direction.y, name, energy, properties.map { props => props.map { a => "%s=%s".format(a._1, a._2) }.mkString(",")}.get)
        }
      }
    }.mkString(",")
  }

  private def parseCommand(input: String): ServerCommand = {
    val parenLocation = input.indexOf("(")
    val commandString = input.substring(0, parenLocation)

    val argString = input.substring(parenLocation + 1, input.length - 1)
    val map = extractMap(argString)

    commandString match {
      case "Welcome" => Welcome(map("name"), map("apocalypse").toInt, map("round").toInt)
      case "Goodbye" => Goodbye(map("energy").toInt)
      case "React" => {
        val remainingMap = map - ("generation", "name", "time", "view", "energy", "master", "collision")
        val userProperties = remainingMap match {
          case _ if (remainingMap.isEmpty) => None
          case _ => Some(remainingMap)
        }

        React(map("generation").toInt,
          map("name"),
          map("time").toInt,
          map("view"),
          map("energy").toInt,
          parseDirection(map.get("master")),
          parseDirection(map.get("collision")),
          userProperties
        )
      }
    }
  }

  def extractMap(keyValueString: String): Map[String, String] = {
    val regex = """(\w+)=(\w+[:\w]*)""".r
    regex.findAllIn(keyValueString).map { m =>
      m match {
        case regex(key, value) => Map(key -> value)
      }
    }.foldLeft(Map[String, String]())((a, b) => a ++ b)
  }

  private def parseDirection(string: Option[String]): Option[Direction] = {
    string.map { s =>
      val strings = s.split(':')
      Direction(strings(0).toInt, strings(1).toInt)
    }
  }
}
