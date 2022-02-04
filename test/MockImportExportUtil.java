import java.io.IOException;
import model.application.IApplication;
import model.application.ImportExportUtil;
import model.picture.ILayer;
import model.picture.ILayeredPicture;
import model.picture.IPicture;
import model.pixel.IPixel;

/**
 * Mock for the {@code ImportExportUtil} class used for testing the output of its export() methods.
 * Takes in an {@code Appendable} in order to read the string used to create a file.
 */
public class MockImportExportUtil extends ImportExportUtil {

  private final Appendable output;

  /**
   * Constructor for {@code MockImportExportUtil} taking in an appendable to be used to read
   * output.
   */
  public MockImportExportUtil(Appendable output) {
    this.output = output;
  }

  /**
   * Instead of creating a new file, appends the string that would have been used to this mock's
   * appendable. Only works for PPM format since JPEG and PNG rely on ImageIO.
   */
  public void exportRegularImage(IPicture<IPixel> image, String filename)
      throws IOException {
    IApplication<IPicture<IPixel>> app = new MockApplication(output);
    app.addImage(image);
    app.exportImage(0, filename);
  }

  /**
   * Instead of creating new files, appends the string that would have been used to make the text
   * file to this mock's appendable.
   */
  public void exportLayeredImage(ILayeredPicture<ILayer<IPicture<IPixel>>> image,
      String filename, FileType type) {
    StringBuilder textFile = new StringBuilder();

    textFile.append(filename).append(".txt\n");

    textFile.append(
        String.format("%d %d %d\n", image.getWidth(), image.getHeight(), image.getMaxVal()));

    for (ILayer<IPicture<IPixel>> layer : image.getLayers()) {
      String imagePath = String.format("res/%s%s", layer.getName(), type.getExtension());

      textFile.append(
          String.format("%s %b %s\n", layer.getName(), layer.getVisibility(), imagePath));
    }

    try {
      output.append(textFile.toString());
    } catch (IOException e) {
      throw new IllegalStateException("Unexpected exception thrown");
    }
  }
}