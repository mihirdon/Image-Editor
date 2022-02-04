package controller.commands;

import model.application.ILayeredApplication;
import view.IApplicationView;

/**
 * {@code ICommands} object in charge of setting the application's current image's current layer's
 * image to be visible.
 */
public class VisibleCommand extends AbstractCommand {

  /**
   * Constructs a {@code VisibleCommand} with the given arguments.
   *
   * @param command the string determining the outcome of this command's behavior
   * @param app     the application which the command object operates on.
   */
  public VisibleCommand(String command, ILayeredApplication app) {
    super(command, app);
  }

  @Override
  public void apply(IApplicationView view) throws IllegalArgumentException {
    super.conditionsMet(1, app);

    app.setVisibility(true);

    view.renderMessage("Current layer is visible");
  }
}