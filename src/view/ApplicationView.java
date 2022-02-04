package view;

import java.io.IOException;
import model.application.IApplication;

/**
 * Represents an {@code IApplicationView} which displays any given messages and any commands that
 * have been performed.
 */
public class ApplicationView implements IApplicationView {

  IApplication<?> application;
  Appendable output;

  /**
   * Constructs an {@code ApplicationView} with the given application and appendable.
   *
   * @param application given application
   * @param output      given appendable
   */
  public ApplicationView(IApplication<?> application, Appendable output) {
    if (application == null || output == null) {
      throw new IllegalArgumentException("Application and appendable cannot be null");
    }

    this.application = application;
    this.output = output;
  }

  @Override
  public void renderMessage(String s) {
    try {
      output.append(String.format("%s\n", s));
    } catch (IOException i) {
      System.out.println(s);
    }
  }

  @Override
  public void visualizeCommands() {
    StringBuilder s;
    s = new StringBuilder();

    s.append("create layer \"nameOfLayer\": Creates a layer of the given name\n");
    s.append("current \"nameOfLayer\": Sets the current layer to the layer with the given name\n");
    s.append(
        "load \"filePathOfImage\": Loads an image from the given file path to the current layer\n");
    s.append("blur: Blurs the current layer's image\n");
    s.append("sharpen: Sharpens the current layer's image\n");
    s.append("sepia: Makes the current layer's image sepia tone\n");
    s.append("monochrome: Makes the current layer's image monochrome\n");
    s.append("checkerboard \"tileSize\" \"numOfTiles\": Creates a checkerboard with each tile's "
        + "side length be 'tileSize' pixels "
        + "and the side length of the board be 'numOfTiles' in tiles\n");
    s.append("save \"fileName\" \"fileType\": Saves the top-most visible layer as an image "
        + "with the given name. Image type is 'fileType' (.ppm, .png, or .jpeg)\n");
    s.append(
        "saveAll \"fileName\" \"fileType\": Saves the current layered image"
            + "as a text file with the given name."
            + " Each layer's image is saved as the given"
            + "file type (.ppm, .png, or .jpeg).\n");
    s.append("invisible: Sets the current layer to be invisible\n");
    s.append("visible: Sets the current layer to be visible\n");
    s.append("q or Q: Stops running the application\n");

    this.renderMessage(s.toString());
  }
}