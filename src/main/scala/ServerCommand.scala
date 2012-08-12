case class Direction(x: Int, y: Int)

sealed trait Command

sealed trait ServerCommand extends Command
case class Welcome(name: String, apocalypse: Int, round: Int) extends ServerCommand
case class Goodbye(energy: Int) extends ServerCommand
case class React(generation: Int, name: String, time: Int, view: String, energy: Int, master: Option[Direction], collision: Option[Direction]) extends ServerCommand