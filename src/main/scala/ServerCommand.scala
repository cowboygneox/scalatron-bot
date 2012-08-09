case class Direction(x: Int, y: Int)

sealed trait Command

sealed trait ServerCommand extends Command
case class Welcome(name: String, apocalypse: Int, round: Int) extends ServerCommand
case class Goodbye(energy: Int) extends ServerCommand