package controller.commands;

import model.application.Application;
import model.application.ILayeredApplication;
import model.picture.Layer;
import model.picture.LayeredPicture;
import view.IApplicationView;

/**
 * Creates a checkerboard command that creates a checkerboard of tileSize (the second command
 * string) and of numOfTiles (the third command string) using the given model's createCheckerboard
 * method.
 */
public class CheckerboardCommand extends AbstractCommand {

  /**
   * Creates a CheckerboardCommand object.
   *
   * @param command the strings to be split
   * @param app the application it is sending the methods to
   */
  public CheckerboardCommand(String command, ILayeredApplication app) {
    super(command, app);
  }

  @Override
  public void apply(IApplicationView view) throws IllegalArgumentException {
    if (command.length != 3) {
      throw new IllegalArgumentException("Command length is invalid");
    }

    if (app.getImages().size() == 0) {
      app.addImage(new LayeredPicture());
      app.setCurrentPicture(0);
    }

    int tileSize = Integer.parseInt(command[1]);
    int numOfTiles = Integer.parseInt(command[2]);
    app.createLayer(new Layer("Checkerboard",
        new Application().createCheckerBoard(tileSize, numOfTiles)));
    view.renderMessage("Application created board of size: "
        + tileSize * numOfTiles + "x" + tileSize * numOfTiles + " in pixels");
  }
}