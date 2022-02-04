package controller.commands;

import model.application.ILayeredApplication;
import view.IApplicationView;

/**
 * An {@code InvisibleCommand} is a command that sets the current layer to invisible.
 */
public class InvisibleCommand extends AbstractCommand {

  /**
   * Creates an InvisibleCommand using the abstract constructor provided by AbstractCommand.
   *
   * @param command the string of the command it is being asked to do
   * @param app     the application in which to call the appropriate method
   */
  public InvisibleCommand(String command, ILayeredApplication app) {
    super(command, app);
  }

  @Override
  public void apply(IApplicationView view) throws IllegalArgumentException {
    super.conditionsMet(1, app);

    app.setVisibility(false);

    view.renderMessage("Layer is now invisible");
  }
}