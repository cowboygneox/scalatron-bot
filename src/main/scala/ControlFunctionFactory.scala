class ControlFunctionFactory {
  val parser = new CommandParser

  def create = { input: String =>
    parser.encodeOutput(handleCommands(parser.parseInput(input)))
  }

  def handleCommands(commands: Set[ServerCommand]): Set[ClientCommand] = {
    null
  }
}