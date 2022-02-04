package controller.commands;

import model.application.ILayeredApplication;
import model.application.ImportExportUtil.FileType;
import view.IApplicationView;

/**
 * {@code ICommands} object in charge of exporting the given application's current layered image.
 */
public class SaveAllCommand extends AbstractCommand {

  /**
   * Constructs a {@code SaveAllCommand} with the given arguments.
   *
   * @param command the string determining the outcome of this command's behavior
   * @param app     the application which the command object operates on.
   */
  public SaveAllCommand(String command, ILayeredApplication app) {
    super(command, app);
  }

  @Override
  public void apply(IApplicationView view) throws IllegalArgumentException {
    super.conditionsMet(command.length, app);

    StringBuilder filepath = new StringBuilder();

    for (int i = 1; i < command.length - 1; i++) {
      if (i != command.length - 2) {
        filepath.append(String.format("%s ", command[i]));
      } else {
        filepath.append(command[i]);
      }
    }

    if (command[command.length - 1].contains(".ppm")) {
      app.exportImage(app.getCurrentImage(), filepath.toString(), FileType.PPM);
    } else if (command[command.length - 1].contains(".png")) {
      app.exportImage(app.getCurrentImage(), filepath.toString(), FileType.PNG);
    } else if (command[command.length - 1].contains(".jpeg")) {
      app.exportImage(app.getCurrentImage(), filepath.toString(), FileType.JPEG);
    } else {
      throw new IllegalArgumentException("Given command is not one of the defined types, "
          + "png, jpeg, or ppm");
    }

    view.renderMessage("Successfully exported to: " + filepath);
  }
}