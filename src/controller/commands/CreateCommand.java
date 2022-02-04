package controller.commands;

import model.application.ILayeredApplication;
import model.picture.LayeredPicture;
import view.IApplicationView;

/**
 * A command that is used to create an image and create layers within that image.
 */
public class CreateCommand extends AbstractCommand {

  /**
   * Creates an object of CreateCommand using the abstracted constructor from AbstractCommand.
   *
   * @param command a string to be split into a string array containing the elements for the create
   *                method, mainly a name for the file
   * @param app the model to use to create and store the newly created layers
   */
  public CreateCommand(String command, ILayeredApplication app) {
    super(command, app);
  }

  @Override
  public void apply(IApplicationView view) throws IllegalArgumentException {
    if (command.length != 3) {
      throw new IllegalArgumentException("Create command length is too short");
    }

    if (!command[1].equals("layer")) {
      throw new IllegalArgumentException("Can only create layers as of now");
    }

    if (app.getImages().size() == 0) {
      app.addImage(new LayeredPicture());
      app.setCurrentPicture(0);
    }

    app.createLayer(command[2]);
    view.renderMessage("Created layer " + command[2]);
  }
}