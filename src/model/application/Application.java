package model.application;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import model.picture.IPicture;
import model.picture.Picture;
import model.pixel.ColorChannel;
import model.pixel.IPixel;
import model.pixel.Pixel;

/**
 * Represents an Image Processing Application which can hold multiple images, generate mutated
 * versions of these images, create a checkerboard programmatically, and import/export images in PPM
 * format.
 */
public class Application implements IApplication<IPicture<IPixel>> {

  protected final List<IPicture<IPixel>> images;

  /**
   * Constructs an {@code Application} which contains the provided list of images.
   *
   * @param images the list of images the application will store
   * @throws IllegalArgumentException if given list is null
   */
  public Application(List<IPicture<IPixel>> images) throws IllegalArgumentException {
    if (images == null) {
      throw new IllegalArgumentException("List of images cannot be null");
    }

    this.images = images;
  }

  /**
   * Constructs an {@code Application} with no stored images.
   */
  public Application() {
    this(new ArrayList<>());
  }

  @Override
  public List<IPicture<IPixel>> getImages() {
    return new ArrayList<>(images);
  }

  @Override
  public IPicture<IPixel> getImage(int index) throws IllegalArgumentException {
    if (index < 0 || index >= images.size()) {
      throw new IllegalArgumentException("Index cannot be negative or out of bounds");
    }

    return new Picture(images.get(index));
  }

  @Override
  public void addImage(IPicture<IPixel> image) throws IllegalArgumentException {
    if (image == null) {
      throw new IllegalArgumentException("Image cannot be null");
    }

    images.add(image);
  }

  @Override
  public void removeImage(int index) throws IllegalArgumentException {
    if (index < 0 || index >= images.size()) {
      throw new IllegalArgumentException("Index cannot be negative or out of bounds");
    }

    images.remove(index);
  }

  @Override
  public IPicture<IPixel> blur(int index) throws IllegalArgumentException {
    if (index < 0 || index >= images.size()) {
      throw new IllegalArgumentException("Index cannot be negative or out of bounds");
    }

    double[][] blurKernel = {{0.0625, 0.125, 0.0625},
        {0.125, 0.25, 0.125},
        {0.0625, 0.125, 0.0625}};

    return images.get(index).filter(blurKernel);
  }

  @Override
  public IPicture<IPixel> sharpen(int index) throws IllegalArgumentException {
    if (index < 0 || index >= images.size()) {
      throw new IllegalArgumentException("Index cannot be negative or out of bounds");
    }

    double[][] sharpenKernel = {{-0.125, -0.125, -0.125, -0.125, -0.125},
        {-0.125, 0.25, 0.25, 0.25, -0.125},
        {-0.125, 0.25, 1, 0.25, -0.125},
        {-0.125, 0.25, 0.25, 0.25, -0.125},
        {-0.125, -0.125, -0.125, -0.125, -0.125}};

    return images.get(index).filter(sharpenKernel);
  }

  @Override
  public IPicture<IPixel> monochrome(int index) throws IllegalArgumentException {
    if (index < 0 || index >= images.size()) {
      throw new IllegalArgumentException("Index cannot be negative or out of bounds");
    }

    double[][] monochromeMatrix = {{0.2126, 0.7152, 0.0722},
        {0.2126, 0.7152, 0.0722},
        {0.2126, 0.7152, 0.0722}};

    return images.get(index).colorTransform(monochromeMatrix);
  }

  @Override
  public IPicture<IPixel> sepiaTone(int index) throws IllegalArgumentException {
    if (index < 0 || index >= images.size()) {
      throw new IllegalArgumentException("Index cannot be negative or out of bounds");
    }

    double[][] monochromeMatrix = {{0.393, 0.769, 0.189},
        {0.349, 0.686, 0.168},
        {0.272, 0.534, 0.131}};

    return images.get(index).colorTransform(monochromeMatrix);
  }

  @Override
  public IPicture<IPixel> createCheckerBoard(int tileSize, int numTiles)
      throws IllegalArgumentException {
    if (tileSize <= 0 || numTiles <= 0) {
      throw new IllegalArgumentException("Number of pixels and tiles must be positive");
    }

    int pixelDimension = tileSize * numTiles;

    IPixel[][] sequence = new IPixel[pixelDimension][pixelDimension];

    boolean white = true;

    for (int r = 0; r < pixelDimension; r++) {
      if (r != 0 && r % tileSize == 0) {
        white = !white;
      }

      for (int c = 0; c < pixelDimension; c++) {
        if (c != 0 && c % tileSize == 0) {
          white = !white;
        }

        if (white) {
          sequence[r][c] = new Pixel(new Point(c, r), 255, 255, 255, 255);
        } else {
          sequence[r][c] = new Pixel(new Point(c, r), 255, 0, 0, 0);
        }
      }

      if (numTiles % 2 == 0) {
        white = !white;
      }
    }

    return new Picture(pixelDimension, pixelDimension, 255, sequence);
  }

  @Override
  public IPicture<IPixel> importImage(String filename)
      throws IllegalArgumentException {
    if (filename == null) {
      throw new IllegalArgumentException("File path cannot be null");
    }

    try {
      return ImageUtil.importPPM(filename);
    } catch (FileNotFoundException f) {
      throw new IllegalArgumentException("File not found");
    }
  }

  @Override
  public void exportImage(int index, String name)
      throws IllegalArgumentException, FileNotFoundException {
    if (index < 0 || index >= images.size()) {
      throw new IllegalArgumentException("Index cannot be negative or out of bounds");
    } else if (name == null || name.isEmpty() || name.charAt(0) == '.') {
      throw new IllegalArgumentException("Name is invalid");
    }

    FileOutputStream file;
    IPicture<IPixel> image = images.get(index);

    if (name.contains(".ppm")) {
      file = new FileOutputStream(name);
    } else {
      file = new FileOutputStream(name + ".ppm");
    }

    try {
      file.write(buildPPMString(image).getBytes(StandardCharsets.US_ASCII));
      file.close();
    } catch (IOException i) {
      throw new IllegalArgumentException("The File has an IO error.");
    }
  }

  /**
   * Builds the string to be used for creating a PPM file with its contents being "P3", followed by
   * the dimensions of the image, the maximum value for the pixel color channels, and each color
   * channel value for each pixel.
   *
   * @param image the image you wish to convert into a PPM formatted string
   * @return String describing the image in a ppm format style
   */
  protected String buildPPMString(IPicture<IPixel> image) {
    StringBuilder contents = new StringBuilder("P3\n");

    contents.append(String.format("%d %d\n", image.getWidth(), image.getHeight()));
    contents.append(String.format("%d\n", image.getMaxVal()));

    for (IPixel[] pixelSequence : image.getPixelSequence()) {
      for (IPixel pixel : pixelSequence) {
        contents.append(String.format("%d\n", pixel.getChannelValue(ColorChannel.RED)));
        contents.append(String.format("%d\n", pixel.getChannelValue(ColorChannel.GREEN)));
        contents.append(String.format("%d\n", pixel.getChannelValue(ColorChannel.BLUE)));
      }
    }

    return contents.toString();
  }
}