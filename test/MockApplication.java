import java.io.IOException;
import model.application.Application;
import model.picture.IPicture;
import model.pixel.IPixel;

/**
 * Mock for the {@code Application} class used for testing the output of its export() method. Takes
 * in an {@code Appendable} in order to read the string used to create a file.
 */
public class MockApplication extends Application {

  private final Appendable output;

  /**
   * Constructor for {@code MockApplication} taking in an appendable to be used to read output.
   */
  public MockApplication(Appendable output) {
    super();
    this.output = output;
  }

  // Instead of creating a new file, appends the string that would have been used to this
  // mock's appendable.
  @Override
  public void exportImage(int index, String name)
      throws IllegalArgumentException {
    if (index < 0 || index >= images.size()) {
      throw new IllegalArgumentException("Index cannot be negative or out of bounds");
    } else if (name == null) {
      throw new IllegalArgumentException("Name cannot be null");
    }

    IPicture<IPixel> image = images.get(index);

    try {
      output.append(buildPPMString(image));
    } catch (IOException e) {
      throw new IllegalStateException("I/O error occurred");
    }
  }
}