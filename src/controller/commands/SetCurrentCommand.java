package controller.commands;

import model.application.ILayeredApplication;
import view.IApplicationView;

/**
 * {@code ICommands} object in charge of setting the given application's current image's current
 * layer.
 */
public class SetCurrentCommand extends AbstractCommand {

  /**
   * Constructs a {@code SetCurrentCommand} with the given arguments.
   *
   * @param command the string determining the outcome of this command's behavior
   * @param app     the application which the command object operates on.
   */
  public SetCurrentCommand(String command, ILayeredApplication app) {
    super(command, app);
  }

  @Override
  public void apply(IApplicationView view) throws IllegalArgumentException {
    if (command.length != 2) {
      throw new IllegalArgumentException("command length is too short");
    }

    if (app.getImages().size() == 0) {
      throw new IllegalArgumentException("Create a picture or a layer in the class first");
    }

    app.setCurrentLayer(command[1]);
    view.renderMessage("Set current layer to: " + command[1]);
  }
}