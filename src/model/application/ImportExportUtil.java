package model.application;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.imageio.ImageIO;
import model.picture.ILayer;
import model.picture.ILayeredPicture;
import model.picture.IPicture;
import model.picture.Layer;
import model.picture.LayeredPicture;
import model.picture.Picture;
import model.pixel.ColorChannel;
import model.pixel.IPixel;
import model.pixel.Pixel;

/**
 * Utility class providing the ability to import and export images in popular file formats (in
 * addition to ppm).
 */
public class ImportExportUtil {

  /**
   * Enumeration listing the supported file formats. Each value contains a string representing it's
   * extension.
   */
  public enum FileType {
    JPEG(".jpeg"), PNG(".png"), PPM(".ppm");

    String extension;

    FileType(String extension) {
      this.extension = extension;
    }

    public String getExtension() {
      return extension;
    }
  }

  /**
   * Imports the image of the specified file type at the specified file path by converting its data
   * into the {@code IPicture<IPixel>} implementation of an image.
   *
   * @param filename path to the desired image with extension
   * @param type     supported file type in {@code FileType} enumeration
   * @return the {@code IPicture<IPixel>} representation of desired image
   * @throws IllegalArgumentException if given file path is null or type is null
   * @throws IOException              if an IO exception occurs
   */
  public static IPicture<IPixel> importImageRegular(String filename, FileType type)
      throws IllegalArgumentException, IOException {
    if (filename == null) {
      throw new IllegalArgumentException("File path cannot be null");
    } else if (type == null) {
      throw new IllegalArgumentException("File type cannot be null");
    }

    switch (type) {
      case PPM:
        try {
          return ImageUtil.importPPM(filename);
        } catch (FileNotFoundException e) {
          throw new IllegalArgumentException(e.getMessage());
        }
      case PNG:
      case JPEG:
        BufferedImage bf = ImageIO.read(new FileInputStream(filename));

        int width = bf.getWidth();
        int height = bf.getHeight();

        IPixel[][] pixels = new IPixel[height][width];

        for (int h = 0; h < height; h++) {
          for (int w = 0; w < width; w++) {
            Color rgb = new Color(bf.getRGB(w, h));

            int red = rgb.getRed();
            int blue = rgb.getBlue();
            int green = rgb.getGreen();

            IPixel pixel = new Pixel(new Point(w, h), 255, red, green, blue);
            pixels[h][w] = pixel;
          }
        }

        return new Picture(width, height, 255, pixels);
      default:
        throw new IllegalArgumentException("Invalid type");
    }
  }

  /**
   * Exports the given image with the given name as the given file type.
   *
   * @param image    image to export
   * @param filename name to give to the exported image without the extension
   * @param type     supported file type in {@code FileType} enumeration
   * @throws IllegalArgumentException if image is null, name is null, empty, or starts with '.', or
   *                                  type is null
   * @throws IOException              if an IO exception occurs
   */
  public static void exportImageRegular(IPicture<IPixel> image, String filename, FileType type)
      throws IOException {
    if (image == null) {
      throw new IllegalArgumentException("Given image cannot be null");
    } else if (filename == null || filename.isEmpty() || filename.charAt(0) == '.') {
      throw new IllegalArgumentException("File name cannot be null, empty, or start with '.'");
    } else if (type == null) {
      throw new IllegalArgumentException("File type cannot be null");
    }

    switch (type) {
      case PPM:
        IApplication<IPicture<IPixel>> app = new Application();
        app.addImage(image);
        app.exportImage(0, filename);
        break;
      case PNG:
        writeFile(image, filename, "png");
        break;
      case JPEG:
        writeFile(image, filename, "jpeg");
        break;
      default:
        throw new IllegalArgumentException("Invalid file type");
    }
  }

  // Helper which creates the buffered image representing the desired image and exporting it
  // with the given name as the given file type.
  private static void writeFile(IPicture<IPixel> image, String name, String filetype)
      throws IOException {
    BufferedImage bf =
        new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

    IPixel[][] pixelSequence = image.getPixelSequence();

    for (int h = 0; h < image.getHeight(); h++) {
      for (int w = 0; w < image.getWidth(); w++) {
        IPixel pixel = pixelSequence[h][w];

        int red = pixel.getChannelValue(ColorChannel.RED);
        int green = pixel.getChannelValue(ColorChannel.GREEN);
        int blue = pixel.getChannelValue(ColorChannel.BLUE);

        Color c = new Color(red, green, blue);
        bf.setRGB(w, h, c.getRGB());
      }
    }

    ImageIO.write(bf, filetype, new File(String.format("%s.%s", name, filetype)));
  }

  /**
   * Imports the layered image of the specified file type by reading the specified text file's
   * location and converting its data into the {@code ILayeredPicture<IPicture<IPixel>>}
   * implementation of an image.
   *
   * @param filename name of the text file containing the layered image data with extension
   * @param type     type supported file type in {@code FileType} enumeration
   * @return the {@code ILayeredPicture} representation of the desired image
   * @throws IllegalArgumentException if filename is null or type is null
   */
  public static ILayeredPicture<ILayer<IPicture<IPixel>>> importImageLayered(String filename,
      FileType type) {
    if (filename == null) {
      throw new IllegalArgumentException("File path cannot be null");
    } else if (type == null) {
      throw new IllegalArgumentException("File type cannot be null");
    }

    List<ILayer<IPicture<IPixel>>> layers = new ArrayList<>();

    try {
      Scanner scan = new Scanner(new FileInputStream(filename));

      int width;
      int height;
      int maxVal;

      try {
        width = scan.nextInt();
        height = scan.nextInt();
        maxVal = scan.nextInt();

        while (scan.hasNext()) {
          String layerName = scan.next();

          boolean visibility = scan.nextBoolean();

          IPicture<IPixel> image = importImageRegular(scan.next(), type);

          layers.add(new Layer(layerName, visibility, image));
        }
      } catch (IOException e) {
        throw new IllegalArgumentException("IO exception occurred");
      }

      return new LayeredPicture(width, height, maxVal, layers);
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("File wasn't found");
    }
  }

  /**
   * Exports the given layered image as several regular images for each layer's image and as the
   * given file type and creates a text file with the layered image's data and the location of each
   * layer's picture.
   *
   * @param image    layered image to export
   * @param filename the name of the text file containing the layered image data without extension
   * @param type     type supported file type in {@code FileType} enumeration
   * @throws IllegalArgumentException if image is null, name is null, empty, or starts with '.', or
   *                                  type is null
   */
  public static void exportImageLayered(ILayeredPicture<ILayer<IPicture<IPixel>>> image,
      String filename, FileType type) {
    if (image == null) {
      throw new IllegalArgumentException("Given image cannot be null");
    } else if (filename == null || filename.isEmpty() || filename.charAt(0) == '.') {
      throw new IllegalArgumentException("File name is invalid");
    } else if (type == null) {
      throw new IllegalArgumentException("File type cannot be null");
    }

    StringBuilder textFile = new StringBuilder();

    textFile.append(
        String.format("%d %d %d\n", image.getWidth(), image.getHeight(), image.getMaxVal()));

    for (ILayer<IPicture<IPixel>> layer : image.getLayers()) {
      String imagePath = String.format("%s%s", layer.getName(), type.getExtension());

      textFile.append(
          String.format("%s %b %s\n", layer.getName(), layer.getVisibility(), imagePath));

      try {
        exportImageRegular(layer.getImage(), String.format("%s", layer.getName()), type);
      } catch (IOException e) {
        throw new IllegalArgumentException("IO exception occurred");
      }
    }

    byte[] bytes = textFile.toString().getBytes();

    try {
      FileOutputStream result = new FileOutputStream(filename + ".txt");
      result.write(bytes);
      result.close();
    } catch (IOException e) {
      throw new IllegalArgumentException("IO exception occurred");
    }
  }
}