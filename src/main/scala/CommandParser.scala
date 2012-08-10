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

  private def parseCommand(input: String): ServerCommand = {
    val parenLocation = input.indexOf("(")
    val commandString = input.substring(0, parenLocation)

    val argString = input.substring(parenLocation + 1, input.length - 1)
    val args = argString.split(",")

    var name = ""
    var apocalypse = 0
    var round = 0
    var energy = 0
    var generation = 0
    var time = 0
    var view = ""
    var master: Direction = null
    var collision: Direction = null

    args.map {
      arg =>
        val (a, v) = arg.splitAt(arg.indexOf("=") + 1)
        a match {
          case "name=" => name = v
          case "apocalypse=" => apocalypse = v.toInt
          case "round=" => round = v.toInt
          case "energy=" => energy = v.toInt
          case "generation=" => generation = v.toInt
          case "time=" => time = v.toInt
          case "view=" => view = v
          case "master=" => master = parseDirection(v)
          case "collision=" => collision = parseDirection(v)
        }
    }

    commandString match {
      case "Welcome" => Welcome(name, apocalypse, round)
      case "Goodbye" => Goodbye(energy)
      case "React" => React(generation, name, time, view, energy, master, collision)
    }
  }

  private def parseDirection(string: String): Direction = {
    val strings = string.split(':')
    Direction(strings(0).toInt, strings(1).toInt)
  }
}
