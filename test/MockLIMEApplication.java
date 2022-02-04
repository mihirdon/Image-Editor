import java.io.IOException;
import model.application.ImportExportUtil.FileType;
import model.application.LIMEApplication;
import model.picture.ILayer;
import model.picture.ILayeredPicture;
import model.picture.IPicture;
import model.pixel.IPixel;

/**
 * Mock for the {@code LIMEApplication} class used for testing the output of its export() method.
 * Takes in an {@code Appendable} in order to read the string used to create a file.
 */
public class MockLIMEApplication extends LIMEApplication {

  private final Appendable output;

  /**
   * Constructor for {@code MockMultipleFormatApplication} taking in an appendable to be used to
   * read output.
   */
  public MockLIMEApplication(Appendable output) {
    super();
    this.output = output;
  }

  // Instead of creating a new file, appends the string that would have been used to this
  // mock's appendable.
  @Override
  public void exportImage(int index, String name) {
    if (name == null || name.isEmpty() || name.charAt(0) == '.') {
      throw new IllegalArgumentException("File name cannot be null, empty, or start with '.'");
    }

    ILayeredPicture<ILayer<IPicture<IPixel>>> image = images.get(index);

    exportImage(image, name, FileType.PPM);
  }

  // Instead of creating a new file, appends the string that would have been used to this
  // mock's appendable. Only works with PPM format since PNG and JPEG rely on ImageIO.
  @Override
  public void exportImage(
      ILayeredPicture<ILayer<IPicture<IPixel>>> image, String filename, FileType type) {
    if (image == null) {
      throw new IllegalArgumentException("Given image cannot be null");
    } else if (filename == null || filename.isEmpty() || filename.charAt(0) == '.') {
      throw new IllegalArgumentException("File name cannot be null, empty, or start with '.'");
    } else if (type == null) {
      throw new IllegalArgumentException("File type cannot be null");
    }

    new MockImportExportUtil(output).exportLayeredImage(image, filename, type);
  }

  // Instead of creating a new file, appends the string that would have been used to this
  // mock's appendable. Only works with PPM format since PNG and JPEG rely on ImageIO.
  @Override
  public void exportTopMostVisibleLayer(String filepath, FileType type) {
    ILayer<IPicture<IPixel>> exportLayer = null;

    for (ILayer<IPicture<IPixel>> layer : getCurrentImage().getLayers()) {
      if (layer.getVisibility()) {
        exportLayer = layer;
        break;
      }
    }

    if (exportLayer == null) {
      throw new IllegalStateException("Current image has no visible layers");
    }

    try {
      new MockImportExportUtil(output).exportRegularImage(
          exportLayer.getImage(), filepath);
    } catch (IOException e) {
      throw new IllegalArgumentException("IO exception occurred");
    }
  }
}