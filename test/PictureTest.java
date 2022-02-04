import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotEquals;

import java.awt.Point;
import model.picture.IPicture;
import model.picture.Picture;
import model.pixel.ColorChannel;
import model.pixel.IPixel;
import model.pixel.Pixel;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for the {@code Picture} class. Ensures images are constructed correctly with all valid
 * restrictions. Also tests functionality of getters and setters.
 */
public class PictureTest {

  IPicture<IPixel> picture;
  IPixel[][] pixels;

  // Sets up a test fixture used for testing.
  // Constructs a valid picture with a width and height of 4, a maximum value of 255,
  // and a 2D array of pixels with each pixel having a red channel value of 0, green channel
  // value of 1, and blue channel value of 2.
  @Before
  public void setupTestFixture() {
    pixels = new Pixel[4][4];

    for (int h = 0; h < 4; h++) {
      for (int w = 0; w < 4; w++) {
        pixels[h][w] = new Pixel(new Point(w, h), 0, 1, 2);
      }
    }

    picture = new Picture(4, 4, 255, pixels);
  }

  // Tests images can be constructed by providing another image or through arguments.
  @Test
  public void testConstructors() {
    IPicture<IPixel> constructed = new Picture(picture);

    assertEquals(4, constructed.getWidth());
    assertEquals(4, constructed.getHeight());
    assertEquals(255, constructed.getMaxVal());
    assertArrayEquals(pixels, constructed.getPixelSequence());

    IPicture<IPixel> withAllArgs = new Picture(4, 4, 255, pixels);

    assertEquals(4, withAllArgs.getWidth());
    assertEquals(4, withAllArgs.getHeight());
    assertEquals(255, withAllArgs.getMaxVal());
    assertArrayEquals(pixels, withAllArgs.getPixelSequence());
  }

  // Tests an exception is thrown when constructor is provided with a negative width/height.
  @Test(expected = IllegalArgumentException.class)
  public void testNegativeWidthAndHeight() {
    new Picture(-1, -1, 255, pixels);
  }

  // Tests an exception is thrown when constructor is provided with zero for width/height.
  @Test(expected = IllegalArgumentException.class)
  public void testZeroWidthAndHeight() {
    new Picture(0, 0, 255, pixels);
  }

  // Tests an exception is thrown when constructor is provided with a negative maximum value.
  @Test(expected = IllegalArgumentException.class)
  public void testNegativeMaxVal() {
    new Picture(1, 1, -1, pixels);
  }

  // Tests an exception is thrown when constructor is provided with a null pixel sequence.
  @Test(expected = IllegalArgumentException.class)
  public void testNullPixels() {
    new Picture(1, 1, 255, null);
  }

  // Tests an exception is thrown when constructor is provided with a pixel sequence
  // with different dimensions than the given width and height.
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidPixels() {
    new Picture(1, 1, 255, new IPixel[2][3]);
  }

  // Tests an exception is thrown when constructor is provided with a null image.
  @Test(expected = IllegalArgumentException.class)
  public void testNullPicture() {
    new Picture(null);
  }

  // Tests getWidth() method provides the correct value.
  @Test
  public void testGetWidth() {
    assertEquals(4, picture.getWidth());
  }

  // Tests getHeight() method provides the correct value.
  @Test
  public void testGetHeight() {
    assertEquals(4, picture.getHeight());
  }

  // Tests getMaxVal() method provides the correct value.
  @Test
  public void testGetMaxVal() {
    assertEquals(255, picture.getMaxVal());
  }

  // Tests getPixelSequence() method provides the correct sequence of pixels.
  @Test
  public void testGetPixelSequence() {
    assertArrayEquals(pixels, picture.getPixelSequence());
  }

  // Tests getChannelValues() method provides the correct sequence of pixel color channel values.
  @Test
  public void testGetChannelValues() {
    int[][] reds = new int[4][4];
    int[][] greens = new int[4][4];
    int[][] blues = new int[4][4];

    for (int r = 0; r < 4; r++) {
      for (int c = 0; c < 4; c++) {
        reds[r][c] = pixels[r][c].getChannelValue(ColorChannel.RED);
        greens[r][c] = pixels[r][c].getChannelValue(ColorChannel.GREEN);
        blues[r][c] = pixels[r][c].getChannelValue(ColorChannel.BLUE);
      }
    }

    assertArrayEquals(reds, picture.getChannelValues(ColorChannel.RED));
    assertArrayEquals(greens, picture.getChannelValues(ColorChannel.GREEN));
    assertArrayEquals(blues, picture.getChannelValues(ColorChannel.BLUE));
  }

  // Tests getChannelValues() method throws an exception when given a null channel.
  @Test(expected = IllegalArgumentException.class)
  public void testGetNullChannelValues() {
    picture.getChannelValues(null);
  }

  // Tests getImageSubset() method returns the correct int[][], representing the subset of the
  // this image's sequence of pixels' specified color channel values.
  @Test
  public void testGetImageSubset() {
    IPixel[][] pixels = {{new Pixel(new Point(), 10, 11, 12),
        new Pixel(new Point(0, 1), 1, 1, 2)},
        {new Pixel(new Point(1, 0), 2, 3, 4),
            new Pixel(new Point(1, 1), 3, 4, 5)}};

    IPicture<IPixel> image = new Picture(2, 2, 255, pixels);

    int[][] subsetRed = image.getImageSubset(3, new Point(), ColorChannel.RED);
    assertEquals(0, subsetRed[0][0]);
    assertEquals(0, subsetRed[0][1]);
    assertEquals(0, subsetRed[0][2]);
    assertEquals(0, subsetRed[1][0]);
    assertEquals(10, subsetRed[1][1]);
    assertEquals(1, subsetRed[1][2]);
    assertEquals(0, subsetRed[2][0]);
    assertEquals(2, subsetRed[2][1]);
    assertEquals(3, subsetRed[2][2]);

    int[][] subsetGreen = image.getImageSubset(3, new Point(), ColorChannel.GREEN);
    assertEquals(0, subsetGreen[0][0]);
    assertEquals(0, subsetGreen[0][1]);
    assertEquals(0, subsetGreen[0][2]);
    assertEquals(0, subsetGreen[1][0]);
    assertEquals(11, subsetGreen[1][1]);
    assertEquals(1, subsetGreen[1][2]);
    assertEquals(0, subsetGreen[2][0]);
    assertEquals(3, subsetGreen[2][1]);
    assertEquals(4, subsetGreen[2][2]);

    int[][] subsetBlue = image.getImageSubset(3, new Point(), ColorChannel.BLUE);
    assertEquals(0, subsetBlue[0][0]);
    assertEquals(0, subsetBlue[0][1]);
    assertEquals(0, subsetBlue[0][2]);
    assertEquals(0, subsetBlue[1][0]);
    assertEquals(12, subsetBlue[1][1]);
    assertEquals(2, subsetBlue[1][2]);
    assertEquals(0, subsetBlue[2][0]);
    assertEquals(4, subsetBlue[2][1]);
    assertEquals(5, subsetBlue[2][2]);
  }

  // Tests getImageSubset() throws an exception if the given channel is null.
  @Test(expected = IllegalArgumentException.class)
  public void getImageSubsetNullChannel() throws IllegalArgumentException {
    picture.getImageSubset(3, new Point(), null);
  }

  // Test getImageSubset() throws an exception if the point is null.
  @Test(expected = IllegalArgumentException.class)
  public void getImageSubsetNullPoint() {
    picture.getImageSubset(3, null, ColorChannel.RED);
  }

  // Test getImageSubset() throws an exception if the dimension is even.
  @Test(expected = IllegalArgumentException.class)
  public void getImageSubsetIllegalDimensionEven() {
    picture.getImageSubset(2, new Point(), ColorChannel.RED);
  }

  // Test getImageSubset() throws an exception if the dimension is negative.
  @Test(expected = IllegalArgumentException.class)
  public void getImageSubsetIllegalDimensionNegative() {
    picture.getImageSubset(-1, new Point(), ColorChannel.RED);
  }

  // Test getImageSubset() throws an exception if the dimension is zero.
  @Test(expected = IllegalArgumentException.class)
  public void getImageSubsetIllegalDimensionZero() {
    picture.getImageSubset(0, new Point(), ColorChannel.RED);
  }

  // Tests filter() method results in an image updating its pixel sequence correctly.
  // Also tests that values are automatically clamped between 0 and this image's maximum value.
  @Test
  public void testFilter() {
    double[][] kernel = {
        {0, -1, 0},
        {100, 0, 100},
        {0, -1, 0}};

    int[][] originalValues = {
        {0, 1, 2},
        {6, 7, 8},
        {3, 0, 5}};

    int[][] finalValues = {
        {94, 193, 92},
        {255, 255, 255},
        {0, 255, 0}};

    IPixel[][] originalPixels = new IPixel[3][3];

    for (int r = 0; r < 3; r++) {
      for (int c = 0; c < 3; c++) {
        originalPixels[r][c] = new Pixel(new Point(c, r), 255,
            originalValues[r][c], originalValues[r][c], originalValues[r][c]);
      }
    }

    IPicture<IPixel> test = new Picture(3, 3, 255, originalPixels);

    assertArrayEquals(finalValues, test.filter(kernel).getChannelValues(ColorChannel.RED));
    assertArrayEquals(finalValues, test.filter(kernel).getChannelValues(ColorChannel.GREEN));
    assertArrayEquals(finalValues, test.filter(kernel).getChannelValues(ColorChannel.BLUE));
  }

  // Tests filter() method throws an exception if given kernel is even.
  @Test(expected = IllegalArgumentException.class)
  public void testEvenKernel() {
    picture.filter(new double[2][2]);
  }

  // Tests filter() method throws an exception if given kernel is not square.
  @Test(expected = IllegalArgumentException.class)
  public void testNonSquareKernel() {
    picture.filter(new double[3][5]);
  }

  // Tests colorTransform() results in an image updating its pixel sequence correctly.
  // Also tests that values are automatically clamped between 0 and this image's maximum value.
  @Test
  public void testColorTransform() {
    double[][] matrix = {
        {-1, -1, -1},
        {100, 100, 100},
        {1, 2, 3}};

    int[][] originalValues = {
        {1, 2, 3},
        {1, 2, 3},
        {1, 2, 3}};

    int[][] finalRedValues = {
        {0, 0, 0},
        {0, 0, 0},
        {0, 0, 0}};

    int[][] finalGreenValues = {
        {255, 255, 255},
        {255, 255, 255},
        {255, 255, 255}};

    int[][] finalBlueValues = {
        {6, 12, 18},
        {6, 12, 18},
        {6, 12, 18}};

    IPixel[][] originalPixels = new IPixel[3][3];

    for (int r = 0; r < 3; r++) {
      for (int c = 0; c < 3; c++) {
        originalPixels[r][c] = new Pixel(new Point(c, r), 255,
            originalValues[r][c], originalValues[r][c], originalValues[r][c]);
      }
    }

    IPicture<IPixel> test = new Picture(3, 3, 255, originalPixels);

    assertArrayEquals(finalRedValues,
        test.colorTransform(matrix).getChannelValues(ColorChannel.RED));
    assertArrayEquals(finalGreenValues,
        test.colorTransform(matrix).getChannelValues(ColorChannel.GREEN));
    assertArrayEquals(finalBlueValues,
        test.colorTransform(matrix).getChannelValues(ColorChannel.BLUE));
  }

  // Tests colorTransform() method throws an exception if given matrix does not have the same
  // dimensions as the number of color channels.
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidMatrix() {
    picture.colorTransform(new double[2][4]);
  }

  // Tests toString() method correctly represents this image as a string.
  @Test
  public void testToString() {
    IPixel[][] pixels = {{new Pixel(new Point(), 0, 0, 0),
        new Pixel(new Point(0, 1), 0, 0, 0)},
        {new Pixel(new Point(1, 0), 0, 0, 0),
            new Pixel(new Point(1, 1), 0, 0, 0)}};

    IPicture<IPixel> picture = new Picture(2, 2, 255, pixels);

    StringBuilder expected;
    expected = new StringBuilder();

    expected.append("Dimensions: 2x2\n");
    expected.append("Maximum Value: 255\n");
    expected.append(pixels[0][0].toString()).append("\n");
    expected.append(pixels[0][1].toString()).append("\n");
    expected.append(pixels[1][0].toString()).append("\n");
    expected.append(pixels[1][1].toString()).append("\n");

    assertEquals(expected.toString(), picture.toString());
  }

  // Tests equals() method returns true when given object is the same reference or an equivalent
  // image. Also returns false when given a different image or another non-image object.
  @Test
  public void testEquals() {
    IPixel[][] pixels = {{new Pixel(new Point(), 0, 1, 2),
        new Pixel(new Point(0, 1), 0, 1, 2)},
        {new Pixel(new Point(1, 0), 0, 1, 2),
            new Pixel(new Point(1, 1), 0, 1, 2)}};

    IPicture<IPixel> equivalent = new Picture(4, 4, 255, this.pixels);
    IPicture<IPixel> different = new Picture(2, 2, 0, pixels);

    assertEquals(picture, picture);
    assertEquals(equivalent, picture);
    assertNotEquals(different, picture);
    assertNotEquals(null, picture);
  }

  // Tests hashCode() method produces the same hashCode for equivalent images.
  @Test
  public void testHashCode() {
    assertEquals(picture.hashCode(),
        new Picture(4, 4, 255, this.pixels).hashCode());
  }
}