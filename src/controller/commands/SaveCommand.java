package controller.commands;

import model.application.ILayeredApplication;
import model.application.ImportExportUtil.FileType;
import view.IApplicationView;

/**
 * {@code ICommands} object in charge of exporting the top-most visible layer's image of the given
 * application's current image.
 */
public class SaveCommand extends AbstractCommand {

  /**
   * Constructs a {@code SaveCommand} with the given arguments.
   *
   * @param command the string determining the outcome of this command's behavior
   * @param app     the application which the command object operates on.
   */
  public SaveCommand(String command, ILayeredApplication app) {
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

    if (command[command.length - 1].equals(FileType.PPM.getExtension())) {
      app.exportTopMostVisibleLayer(filepath.toString(), FileType.PPM);
    } else if (command[command.length - 1].equals(FileType.PNG.getExtension())) {
      app.exportTopMostVisibleLayer(filepath.toString(), FileType.PNG);
    } else if (command[command.length - 1].equals(FileType.JPEG.getExtension())) {
      app.exportTopMostVisibleLayer(filepath.toString(), FileType.JPEG);
    } else {
      throw new IllegalArgumentException("Given command is not one of the defined types, "
          + "png, jpeg, or ppm");
    }

    view.renderMessage("Successfully exported to: " + filepath);
  }
}