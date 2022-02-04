package model.application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.application.ImportExportUtil.FileType;
import model.picture.IPicture;
import model.pixel.IPixel;

/**
 * Represents an improved version of an application which supports importing and exporting multiple
 * file formats.
 */
public class MultipleFormatApplication extends Application
    implements IMultipleFormatApplication<IPicture<IPixel>> {

  /**
   * Constructs a {@code MultipleFormatApplication} which contains the provided list of images.
   *
   * @param images the list of images the application will store
   * @throws IllegalArgumentException if given list is null
   */
  public MultipleFormatApplication(List<IPicture<IPixel>> images) throws IllegalArgumentException {
    super(images);
  }

  /**
   * Constructs a {@code MultipleFormatApplication} with no stored images.
   */
  public MultipleFormatApplication() {
    super();
  }

  @Override
  public List<IPicture<IPixel>> getImages() {
    return new ArrayList<>(images);
  }

  @Override
  public IPicture<IPixel> importImage(String filename) {
    return importImage(filename, FileType.PPM);
  }

  @Override
  public IPicture<IPixel> importImage(String fileName, FileType type) {
    try {
      return ImportExportUtil.importImageRegular(fileName, type);
    } catch (IOException e) {
      throw new IllegalArgumentException("IO exception occurred");
    }
  }

  @Override
  public void exportImage(int index, String name) {
    if (index < 0 || index >= images.size()) {
      throw new IllegalArgumentException("Index cannot be negative or out of bounds");
    }
    exportImage(images.get(index), name, FileType.PPM);
  }

  @Override
  public void exportImage(IPicture<IPixel> image, String name, FileType type)
      throws IllegalArgumentException {
    if (name == null || name.isEmpty() || name.charAt(0) == '.') {
      throw new IllegalArgumentException("File name cannot be null, empty, or start with '.'");
    }

    try {
      ImportExportUtil.exportImageRegular(image, name, type);
    } catch (IOException e) {
      throw new IllegalArgumentException("IO exception occurred");
    }
  }
}