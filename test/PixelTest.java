import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.awt.Point;
import model.pixel.ColorChannel;
import model.pixel.IPixel;
import model.pixel.Pixel;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for the {@code Pixel} class. Ensures pixels are constructed correctly with all valid
 * restrictions. Also tests functionality of getters and setters.
 */
public class PixelTest {

  IPixel pixel;

  // Sets up a test fixture used for testing.
  // Constructs a valid pixel with default values of
  // position (0,0), maximum value of 255, and color channel values of 0.
  @Before
  public void setupTestFixture() {
    pixel = new Pixel(new Point(), 0, 0, 0);
  }

  // Tests that a pixel can be constructed by provided another pixel or through different arguments.
  @Test
  public void testConstructors() {
    IPixel constructed = new Pixel(new Point(), 0, 1, 2);

    assertEquals(new Point(0, 0), constructed.getCoordinates());
    assertEquals(255, constructed.getMaxVal());
    assertEquals(0, constructed.getChannelValue(ColorChannel.RED));
    assertEquals(1, constructed.getChannelValue(ColorChannel.GREEN));
    assertEquals(2, constructed.getChannelValue(ColorChannel.BLUE));

    IPixel noMaxVal = new Pixel(constructed);

    assertEquals(new Point(0, 0), noMaxVal.getCoordinates());
    assertEquals(255, noMaxVal.getMaxVal());
    assertEquals(0, noMaxVal.getChannelValue(ColorChannel.RED));
    assertEquals(1, noMaxVal.getChannelValue(ColorChannel.GREEN));
    assertEquals(2, noMaxVal.getChannelValue(ColorChannel.BLUE));

    IPixel withMaxVal = new Pixel(new Point(), 255, 0, 1, 2);

    assertEquals(new Point(0, 0), withMaxVal.getCoordinates());
    assertEquals(255, withMaxVal.getMaxVal());
    assertEquals(0, withMaxVal.getChannelValue(ColorChannel.RED));
    assertEquals(1, withMaxVal.getChannelValue(ColorChannel.GREEN));
    assertEquals(2, withMaxVal.getChannelValue(ColorChannel.BLUE));
  }

  // Tests exception is thrown when constructor is given a null point.
  @Test(expected = IllegalArgumentException.class)
  public void testNullCoordinates() {
    new Pixel(null, 0, 0, 0);
  }

  // Tests exception is thrown when constructor is given a point with negative values.
  @Test(expected = IllegalArgumentException.class)
  public void testNegativeCoordinates() {
    new Pixel(new Point(-1, -1), 0, 0, 0);
  }

  // Tests exception is thrown when constructor is given a negative maximum value.
  @Test(expected = IllegalArgumentException.class)
  public void testNegativeMaxVal() {
    new Pixel(new Point(), -1, 0, 0, 0);
  }

  // Tests provided rgb values are automatically set between 0 and the provided maximum value.
  @Test
  public void testClamp() {
    IPixel test = new Pixel(new Point(), 255, 0, -1, 256);

    assertEquals(0, test.getChannelValue(ColorChannel.RED));
    assertEquals(0, test.getChannelValue(ColorChannel.GREEN));
    assertEquals(255, test.getChannelValue(ColorChannel.BLUE));
  }

  // Tests exception is thrown when constructor is given a null pixel.
  @Test(expected = IllegalArgumentException.class)
  public void testNullPixel() {
    new Pixel(null);
  }

  // Tests getCoordinates() method provides the correct point and that the pixel's
  // point cannot be mutated.
  @Test
  public void testGetCoordinates() {
    assertEquals(new Point(0, 0), pixel.getCoordinates());

    pixel.getCoordinates().setLocation(new Point(1, 1));

    assertEquals(new Point(0, 0), pixel.getCoordinates());
  }

  // Tests getMaxVal() method provides the correct value.
  @Test
  public void testGetMaxVal() {
    assertEquals(255, pixel.getMaxVal());
  }

  // Tests getChannelValues() method provides the correct values for each color channel.
  @Test
  public void testGetChannelValues() {
    IPixel test = new Pixel(new Point(), 255, 0, 1, 2);

    assertEquals(0, test.getChannelValue(ColorChannel.RED));
    assertEquals(1, test.getChannelValue(ColorChannel.GREEN));
    assertEquals(2, test.getChannelValue(ColorChannel.BLUE));
  }

  // Tests getChannelValues() method throws an exception when given a null color channel.
  @Test(expected = IllegalArgumentException.class)
  public void testGetNullChannelValues() {
    pixel.getChannelValue(null);
  }

  // Tests setChannelValues() method changes each channel correctly.
  @Test
  public void testSetChannelValues() {
    pixel.setChannelValue(ColorChannel.RED, 0);
    pixel.setChannelValue(ColorChannel.GREEN, 1);
    pixel.setChannelValue(ColorChannel.BLUE, 2);

    assertEquals(0, pixel.getChannelValue(ColorChannel.RED));
    assertEquals(1, pixel.getChannelValue(ColorChannel.GREEN));
    assertEquals(2, pixel.getChannelValue(ColorChannel.BLUE));
  }

  // Tests setChannelValues() method throws an exception when given a null color channel.
  @Test(expected = IllegalArgumentException.class)
  public void testSetNullChannelValues() {
    pixel.setChannelValue(null, 0);
  }

  // Tests setChannelValues() method automatically clamps color channel values between 0 and maxVal.
  @Test
  public void testClampSet() {
    pixel.setChannelValue(ColorChannel.RED, 0);
    pixel.setChannelValue(ColorChannel.GREEN, -1);
    pixel.setChannelValue(ColorChannel.BLUE, 256);

    assertEquals(0, pixel.getChannelValue(ColorChannel.RED));
    assertEquals(0, pixel.getChannelValue(ColorChannel.GREEN));
    assertEquals(255, pixel.getChannelValue(ColorChannel.BLUE));
  }

  // Tests toString() method correctly represents pixel as a string.
  @Test
  public void testToString() {
    String expected = "Coordinates: java.awt.Point[x=0,y=0]\n"
        + "Maximum Value: 255\n"
        + "Red Channel Value: 0\n"
        + "Green Channel Value: 0\n"
        + "Blue Channel Value: 0\n";

    assertEquals(expected, pixel.toString());
  }

  // Tests equals() method returns true when given object is the same reference or an equivalent
  // pixel. Also returns false when given a different pixel or another non-pixel object.
  @Test
  public void testEquals() {
    IPixel equivalent = new Pixel(new Point(), 0, 0, 0);
    IPixel different = new Pixel(new Point(), 1, 2, 3);

    assertEquals(equivalent, pixel);
    assertEquals(pixel, pixel);
    assertNotEquals(different, pixel);
    assertNotEquals(null, pixel);
  }

  // Tests hashCode() method produces the same hashCode for equivalent pixels.
  @Test
  public void testHashCode() {
    assertEquals(pixel.hashCode(), new Pixel(new Point(), 0, 0, 0).hashCode());
  }
}