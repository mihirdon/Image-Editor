package model.application;

import java.awt.Point;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import model.picture.IPicture;
import model.picture.Picture;
import model.pixel.IPixel;
import model.pixel.Pixel;

/**
 * This class contains utility methods to import a PPM image from a file and create an {@code
 * IPicture} with the appropriate fields.
 */
public class ImageUtil {

  /**
   * Creates a new {@code IPicture} from the file specified by the given file path. Only accepts a
   * PPM file, otherwise an exception is thrown. An exception is also thrown when a file is not
   * found.
   *
   * @param filename the path of the file.
   * @throws FileNotFoundException    if given file path does not lead to a file
   * @throws IllegalArgumentException if given file path does not lead to a PPM file
   */
  public static IPicture<IPixel> importPPM(String filename)
      throws FileNotFoundException, IllegalArgumentException {
    if (filename == null) {
      throw new IllegalArgumentException("File is null or illegal");
    }

    Scanner sc;

    sc = new Scanner(new FileInputStream(filename));

    StringBuilder builder = new StringBuilder();

    // read the file line by line, and populate a string. This will throw away any comment lines
    while (sc.hasNextLine()) {
      String s = sc.nextLine();
      if (s.charAt(0) != '#') {
        builder.append(s).append(System.lineSeparator());
      }
    }

    // now set up the scanner to read from the string we just built
    sc = new Scanner(builder.toString());

    String token;

    token = sc.next();

    if (!token.equals("P3")) {
      throw new IllegalArgumentException("Given file is not in PPM format");
    }

    int width = sc.nextInt();
    int height = sc.nextInt();
    int maxValue = sc.nextInt();

    IPixel[][] sequence = new IPixel[height][width];

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int r = sc.nextInt();
        int g = sc.nextInt();
        int b = sc.nextInt();
        sequence[i][j] = new Pixel(new Point(j, i), r, g, b);
      }
    }

    return new Picture(width, height, maxValue, sequence);
  }
}