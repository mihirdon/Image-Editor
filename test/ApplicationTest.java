import static org.junit.Assert.assertEquals;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import model.application.Application;
import model.application.IApplication;
import model.picture.IPicture;
import model.picture.Picture;
import model.pixel.IPixel;
import model.pixel.Pixel;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for the {@code Application} class. Ensures application is constructed correctly with
 * all valid restrictions. Also tests functionality of getters and all image processing operations.
 */
public class ApplicationTest {

  IApplication<IPicture<IPixel>> application;

  // Sets up a test fixture used for testing.
  // Constructs a valid application with an empty list of images
  @Before
  public void setupTestFixture() {
    application = new Application();
  }

  // Tests application can be constructed with given list of images and without arguments.
  @Test
  public void testConstructors() {
    IPixel[][] pixelSequence = new Pixel[2][2];

    pixelSequence[0][0] = new Pixel(new Point(), 16, 32, 64);
    pixelSequence[0][1] = new Pixel(new Point(0, 1), 16, 32, 64);
    pixelSequence[1][0] = new Pixel(new Point(1, 0), 16, 32, 64);
    pixelSequence[1][1] = new Pixel(new Point(1, 1), 16, 32, 64);

    IPicture<IPixel> picture = new Picture(2, 2, 255, pixelSequence);

    List<IPicture<IPixel>> imageList = new ArrayList<>();
    imageList.add(picture);

    IApplication<IPicture<IPixel>> application = new Application(imageList);

    assertEquals(picture, application.getImage(0));

    IApplication<IPicture<IPixel>> emptyApplication = new Application();

    assertEquals(0, emptyApplication.getImages().size());
  }

  // Tests an exception is thrown when constructor is given a null image list.
  @Test(expected = IllegalArgumentException.class)
  public void testApplicationConstructorException() {
    new Application(null);
  }

  // Tests getImages() method returns a copy of an application's list of images.
  // Also tests mutating result does not mutate original list.
  @Test
  public void testGetImages() {
    IPicture<IPixel> image = application.createCheckerBoard(1, 2);

    application.addImage(image);

    List<IPicture<IPixel>> list = new ArrayList<>();

    list.add(image);

    assertEquals(list, application.getImages());

    application.getImages().clear();

    assertEquals(list, application.getImages());
  }

  // Tests getImage() and addImage() methods work appropriately
  // (tested together since they are interlinked).
  @Test
  public void testGetImageAndAddImage() {
    IPixel[][] pixelSequence = new Pixel[2][2];

    pixelSequence[0][0] = new Pixel(new Point(), 16, 32, 64);
    pixelSequence[0][1] = new Pixel(new Point(0, 1), 16, 32, 64);
    pixelSequence[1][0] = new Pixel(new Point(1, 0), 16, 32, 64);
    pixelSequence[1][1] = new Pixel(new Point(1, 1), 16, 32, 64);

    IPicture<IPixel> picture = new Picture(2, 2, 255, pixelSequence);

    IPixel[][] pixelSequence2 = new Pixel[2][2];

    pixelSequence2[0][0] = new Pixel(new Point(), 100, 75, 50);
    pixelSequence2[0][1] = new Pixel(new Point(0, 1), 100, 75, 50);
    pixelSequence2[1][0] = new Pixel(new Point(1, 0), 100, 75, 50);
    pixelSequence2[1][1] = new Pixel(new Point(1, 1), 100, 75, 50);

    IPicture<IPixel> picture2 = new Picture(2, 2, 255, pixelSequence2);

    application.addImage(picture);
    application.addImage(picture2);

    assertEquals(picture, application.getImage(0));
    assertEquals(picture2, application.getImage(1));
  }

  // Tests getImage() method throws an exception when given a negative index.
  @Test(expected = IllegalArgumentException.class)
  public void testGetImageNegative() {
    application.getImage(-1);
  }

  // Tests getImage() method throws an exception if given out of bounds index.
  @Test(expected = IllegalArgumentException.class)
  public void testGetImageOutOfBounds() {
    application.getImage(0);
  }

  // Tests addImage() method throws an exception when given a null image.
  @Test(expected = IllegalArgumentException.class)
  public void testAddingIllegalImage() {
    application.addImage(null);
  }

  // Tests removeImage() method correctly removes specified image.
  @Test
  public void removeImage() {
    IPicture<IPixel> image = application.createCheckerBoard(1, 2);

    application.addImage(image);

    assertEquals(1, application.getImages().size());

    application.removeImage(0);

    assertEquals(0, application.getImages().size());
  }

  // Tests blur() method correctly creates a new image with the blur filter applied.
  @Test
  public void testBlur() {
    IPixel[][] pixelSequence = new Pixel[2][2];

    pixelSequence[0][0] = new Pixel(new Point(), 16, 32, 64);
    pixelSequence[0][1] = new Pixel(new Point(0, 1), 16, 32, 64);
    pixelSequence[1][0] = new Pixel(new Point(1, 0), 16, 32, 64);
    pixelSequence[1][1] = new Pixel(new Point(1, 1), 16, 32, 64);

    IPixel[][] blurredPixelSequence = new Pixel[2][2];

    blurredPixelSequence[0][0] = new Pixel(new Point(), 9, 18, 36);
    blurredPixelSequence[0][1] = new Pixel(new Point(0, 1), 9, 18, 36);
    blurredPixelSequence[1][0] = new Pixel(new Point(1, 0), 9, 18, 36);
    blurredPixelSequence[1][1] = new Pixel(new Point(1, 1), 9, 18, 36);

    application.addImage(new Picture(2, 2, 255, pixelSequence));

    assertEquals(new Picture(2, 2, 255, blurredPixelSequence),
        application.blur(0));
  }

  // Tests blur() method throws an exception when given a negative index.
  @Test(expected = IllegalArgumentException.class)
  public void testBlurNegativeIndex() {
    IPixel[][] pixelSequence = new Pixel[2][2];

    pixelSequence[0][0] = new Pixel(new Point(), 16, 32, 64);
    pixelSequence[0][1] = new Pixel(new Point(0, 1), 16, 32, 64);
    pixelSequence[1][0] = new Pixel(new Point(1, 0), 16, 32, 64);
    pixelSequence[1][1] = new Pixel(new Point(1, 1), 16, 32, 64);

    try {
      application.addImage(new Picture(2, 2, 255, pixelSequence));
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("Unexpected exception thrown");
    }

    application.blur(-1);
  }

  // Tests blur() method throws an exception when given an out of bounds index.
  @Test(expected = IllegalArgumentException.class)
  public void testBlurOutOfBoundsIndex() {
    application.blur(0);
  }

  // Tests sharpen() method correctly creates a new image with the sharpen filter applied.
  @Test
  public void testSharpen() {
    double[][] sharpenKernel = {{-0.125, -0.125, -0.125, -0.125, -0.125},
        {-0.125, 0.25, 0.25, 0.25, -0.125},
        {-0.125, 0.25, 1, 0.25, -0.125},
        {-0.125, 0.25, 0.25, 0.25, -0.125},
        {-0.125, -0.125, -0.125, -0.125, -0.125}};

    IPixel[][] pixelSequence = new Pixel[2][2];

    pixelSequence[0][0] = new Pixel(new Point(), 16, 32, 64);
    pixelSequence[0][1] = new Pixel(new Point(0, 1), 16, 32, 64);
    pixelSequence[1][0] = new Pixel(new Point(1, 0), 16, 32, 64);
    pixelSequence[1][1] = new Pixel(new Point(1, 1), 16, 32, 64);

    application.addImage(new Picture(2, 2, 255, pixelSequence));

    assertEquals(new Picture(2, 2, 255, pixelSequence).filter(sharpenKernel),
        application.sharpen(0));
  }

  // Tests sharpen() method throws an exception when given a negative index.
  @Test(expected = IllegalArgumentException.class)
  public void testSharpenNegativeIndex() {
    IPixel[][] pixelSequence = new Pixel[2][2];

    pixelSequence[0][0] = new Pixel(new Point(), 16, 32, 64);
    pixelSequence[0][1] = new Pixel(new Point(0, 1), 16, 32, 64);
    pixelSequence[1][0] = new Pixel(new Point(1, 0), 16, 32, 64);
    pixelSequence[1][1] = new Pixel(new Point(1, 1), 16, 32, 64);

    application.addImage(new Picture(2, 2, 255, pixelSequence));
    application.sharpen(-1);
  }

  // Tests sharpen() method throws an exception when given an out of bounds index.
  @Test(expected = IllegalArgumentException.class)
  public void testSharpenOutOfBoundsIndex() {
    application.sharpen(0);
  }

  // Tests monochrome() method correctly creates a new image converted to monochrome (greyscale).
  @Test
  public void testMonochrome() {
    IPixel[][] pixelSequence = new Pixel[2][2];

    pixelSequence[0][0] = new Pixel(new Point(), 100, 75, 50);
    pixelSequence[0][1] = new Pixel(new Point(0, 1), 100, 75, 50);
    pixelSequence[1][0] = new Pixel(new Point(1, 0), 100, 75, 50);
    pixelSequence[1][1] = new Pixel(new Point(1, 1), 100, 75, 50);

    IPixel[][] monochromePixelSequence = new Pixel[2][2];

    monochromePixelSequence[0][0] = new Pixel(new Point(), 78, 78, 78);
    monochromePixelSequence[0][1] = new Pixel(new Point(0, 1), 78, 78, 78);
    monochromePixelSequence[1][0] = new Pixel(new Point(1, 0), 78, 78, 78);
    monochromePixelSequence[1][1] = new Pixel(new Point(1, 1), 78, 78, 78);

    application.addImage(new Picture(2, 2, 255, pixelSequence));

    assertEquals(new Picture(2, 2, 255, monochromePixelSequence),
        application.monochrome(0));
  }

  // Tests monochrome() method throws an exception when given a negative index.
  @Test(expected = IllegalArgumentException.class)
  public void testMonochromeNegativeIndex() {
    IPixel[][] pixelSequence = new Pixel[2][2];

    pixelSequence[0][0] = new Pixel(new Point(), 16, 32, 64);
    pixelSequence[0][1] = new Pixel(new Point(0, 1), 16, 32, 64);
    pixelSequence[1][0] = new Pixel(new Point(1, 0), 16, 32, 64);
    pixelSequence[1][1] = new Pixel(new Point(1, 1), 16, 32, 64);

    application.addImage(new Picture(2, 2, 255, pixelSequence));
    application.monochrome(-1);
  }

  // Tests monochrome() method throws an exception when given an out of bounds index.
  @Test(expected = IllegalArgumentException.class)
  public void testMonochromeOutOfBoundsIndex() {
    application.monochrome(0);
  }

  // Tests sepiaTone() method correctly creates a new image converted to sepia tone.
  @Test
  public void testSepiaTone() {
    IPixel[][] pixelSequence = new Pixel[2][2];

    pixelSequence[0][0] = new Pixel(new Point(), 100, 75, 50);
    pixelSequence[0][1] = new Pixel(new Point(0, 1), 100, 75, 50);
    pixelSequence[1][0] = new Pixel(new Point(1, 0), 100, 75, 50);
    pixelSequence[1][1] = new Pixel(new Point(1, 1), 100, 75, 50);

    IPixel[][] sepiaTonePixelSequence = new Pixel[2][2];

    sepiaTonePixelSequence[0][0] = new Pixel(new Point(), 106, 94, 73);
    sepiaTonePixelSequence[0][1] = new Pixel(new Point(0, 1), 106, 94, 73);
    sepiaTonePixelSequence[1][0] = new Pixel(new Point(1, 0), 106, 94, 73);
    sepiaTonePixelSequence[1][1] = new Pixel(new Point(1, 1), 106, 94, 73);

    application.addImage(new Picture(2, 2, 255, pixelSequence));

    assertEquals(new Picture(2, 2, 255, sepiaTonePixelSequence),
        application.sepiaTone(0));
  }

  // Tests sepiaTone() method throws an exception when given a negative index.
  @Test(expected = IllegalArgumentException.class)
  public void testSepiaNegativeIndex() {
    IPixel[][] pixelSequence = new Pixel[2][2];

    pixelSequence[0][0] = new Pixel(new Point(), 16, 32, 64);
    pixelSequence[0][1] = new Pixel(new Point(0, 1), 16, 32, 64);
    pixelSequence[1][0] = new Pixel(new Point(1, 0), 16, 32, 64);
    pixelSequence[1][1] = new Pixel(new Point(1, 1), 16, 32, 64);

    application.addImage(new Picture(2, 2, 255, pixelSequence));
    application.sepiaTone(-1);
  }

  // Tests sepiaTone() method throws an exception when given an out of bounds index.
  @Test(expected = IllegalArgumentException.class)
  public void testSepiaOutOfBoundsIndex() {
    application.sepiaTone(0);
  }

  // Tests createCheckerboard() method correctly creates a checkerboard image programmatically.
  @Test
  public void testCreateCheckerboard() {
    IPixel[][] checkerboard = new Pixel[2][2];

    checkerboard[0][0] = new Pixel(new Point(), 255, 255, 255);
    checkerboard[0][1] = new Pixel(new Point(1, 0), 0, 0, 0);
    checkerboard[1][0] = new Pixel(new Point(0, 1), 0, 0, 0);
    checkerboard[1][1] = new Pixel(new Point(1, 1), 255, 255, 255);

    IPicture<IPixel> expected = new Picture(2, 2, 255, checkerboard);

    assertEquals(expected, application.createCheckerBoard(1, 2));
  }

  // Tests createCheckerboard() method throws an exception when given negative arguments.
  @Test(expected = IllegalArgumentException.class)
  public void testCreateCheckerboardExceptions() {
    IPixel[][] checkerboard = new Pixel[2][2];

    checkerboard[0][0] = new Pixel(new Point(), 255, 255, 255);
    checkerboard[0][1] = new Pixel(new Point(0, 1), 0, 0, 0);
    checkerboard[1][0] = new Pixel(new Point(1, 0), 0, 0, 0);
    checkerboard[1][1] = new Pixel(new Point(1, 1), 255, 255, 255);

    IPicture<IPixel> expected = new Picture(2, 2, 255, checkerboard);

    assertEquals(expected, application.createCheckerBoard(-1, -2));
  }

  // Tests export() method constructs a string with the correct format to create a PPM file.
  @Test
  public void testExportImage() throws FileNotFoundException {
    Appendable output = new StringBuilder();

    IApplication<IPicture<IPixel>> mock = new MockApplication(output);

    mock.addImage(application.createCheckerBoard(1, 2));

    String expected = "P3\n"
        + "2 2\n"
        + "255\n"
        + "255\n"
        + "255\n"
        + "255\n"
        + "0\n"
        + "0\n"
        + "0\n"
        + "0\n"
        + "0\n"
        + "0\n"
        + "255\n"
        + "255\n"
        + "255\n";

    mock.exportImage(0, "Doesn'tExist");

    assertEquals(expected, output.toString());
  }

  // Tests exportImage() method throws exception when given a negative index.
  @Test(expected = IllegalArgumentException.class)
  public void testNegativeIndex() throws FileNotFoundException {
    try {
      application.addImage(application.createCheckerBoard(1, 1));
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("Unexpected exception occurred");
    }

    application.exportImage(-1, "name");
  }

  // Tests exportImage() method throws exception when given an out of bounds index.
  @Test(expected = IllegalArgumentException.class)
  public void testOutOfBoundsIndex() throws FileNotFoundException {
    try {
      application.addImage(application.createCheckerBoard(1, 1));
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("Unexpected exception occurred");
    }
    application.exportImage(1, "name");
  }

  // Tests exportImage() method throws exception when given a null name.
  @Test(expected = IllegalArgumentException.class)
  public void testNullName() throws FileNotFoundException {
    try {
      application.addImage(application.createCheckerBoard(1, 1));
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("Unexpected exception occurred");
    }
    application.exportImage(0, null);
  }

  // Tests exportImage() method throws exception when given an empty name.
  @Test(expected = IllegalArgumentException.class)
  public void testEmptyName() throws FileNotFoundException {
    try {
      application.addImage(application.createCheckerBoard(1, 1));
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("Unexpected exception occurred");
    }
    application.exportImage(0, "");
  }

  // Tests exportImage() method throws exception when given a name beginning with ".".
  @Test(expected = IllegalArgumentException.class)
  public void testDotName() throws FileNotFoundException {
    try {
      application.addImage(application.createCheckerBoard(1, 1));
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("Unexpected exception occurred");
    }
    application.exportImage(0, ".name.ppm");
  }

  // Tests importImage() method correctly creates an image from the given file path.
  @Test
  public void testImportImage() {
    assertEquals(application.createCheckerBoard(1, 2),
        application.importImage("res/SmallBoard.ppm"));
  }

  // Tests importImage() method throws an exception when given a null path.
  @Test(expected = IllegalArgumentException.class)
  public void testImportImageNullPath() {
    application.importImage(null);
  }

  // Tests importImage() method throws an exception when given an incorrect path.
  @Test(expected = IllegalArgumentException.class)
  public void testImportImageIncorrectPath() {
    application.importImage("Doesn'tExists.ppm");
  }
}