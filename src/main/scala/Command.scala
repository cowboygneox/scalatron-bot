case class Direction(x: Int, y: Int)

sealed trait Command

sealed trait ServerCommand extends Command
case class Welcome(name: String, apocalypse: Int, round: Int) extends ServerCommand
case class Goodbye(energy: Int) extends ServerCommand
case class React(generation: Int, name: String, time: Int, view: String, energy: Int, master: Option[Direction] = None, collision: Option[Direction] = None, userProperties: Option[Map[String, String]] = None) extends ServerCommand

sealed trait ClientCommand extends Command
case class Move(direction: Direction) extends ClientCommand
case class Explode(size: Int) extends ClientCommand
case class SetProperties(userProperties: Map[String, String]) extends ClientCommand
case class Spawn(direction: Direction, name: String, energy: Int, userProperties: Option[Map[String, String]] = None) extends ClientCommand

sealed trait NeutralCommand extends ClientCommand
case class Say(text: String) extends NeutralCommand
case class Status(text: String) extends NeutralCommand
case class Log(text: String) extends NeutralCommand
case class MarkCell(position: Direction, color: String) extends NeutralCommand
case class DrawLine(from: Direction, to: Direction, color: String) extends NeutralCommand