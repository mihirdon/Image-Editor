import static org.junit.Assert.assertEquals;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import model.application.IMultipleFormatApplication;
import model.application.ImportExportUtil.FileType;
import model.application.MultipleFormatApplication;
import model.picture.IPicture;
import model.picture.Picture;
import model.pixel.IPixel;
import model.pixel.Pixel;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for the {@code MultipleFormatApplication} class. Ensures application is constructed
 * correctly with all valid restrictions. Also tests functionality of getters and all image
 * processing operations.
 */
public class MultipleFormatApplicationTest {

  IMultipleFormatApplication<IPicture<IPixel>> application;

  // Sets up a test fixture used for testing.
  // Constructs a valid application with an empty list of images
  @Before
  public void setupTestFixture() {
    application = new MultipleFormatApplication();
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

    IMultipleFormatApplication<IPicture<IPixel>> application =
        new MultipleFormatApplication(imageList);

    assertEquals(picture, application.getImage(0));

    IMultipleFormatApplication<IPicture<IPixel>> emptyApplication = new MultipleFormatApplication();

    assertEquals(0, emptyApplication.getImages().size());
  }

  // Tests an exception is thrown when constructor is given a null image list.
  @Test(expected = IllegalArgumentException.class)
  public void testApplicationConstructorException() {
    new MultipleFormatApplication(null);
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

  // Tests original importImage() method correctly creates an image from the given file path.
  @Test
  public void testInitialImportImage() {
    assertEquals(application.createCheckerBoard(1, 2),
        application.importImage("res/SmallBoard.ppm"));
  }

  // Tests original importImage() method throws an exception when given a null path.
  @Test(expected = IllegalArgumentException.class)
  public void testInitialImportImageNullPath() {
    application.importImage(null);
  }

  // Tests original importImage() method throws an exception when given an incorrect path.
  @Test(expected = IllegalArgumentException.class)
  public void testInitialImportImageIncorrectPath() {
    application.importImage("Doesn'tExists.ppm");
  }

  // Tests new importImage() method correctly creates an image from the given file path and type
  // for each supported file type.
  @Test
  public void testNewImportImage() {
    assertEquals(application.createCheckerBoard(1, 2),
        application.importImage("res/SmallBoard.ppm", FileType.PPM));

    assertEquals(application.createCheckerBoard(1, 2),
        application.importImage("res/SmallBoard.png", FileType.PNG));

    // Compression slightly changes RGB values, making JPEG tests inaccurate.
  }

  // Tests new importImage() method throws an exception when given a null path.
  @Test(expected = IllegalArgumentException.class)
  public void testNewImportImageNullPath() {
    application.importImage(null, FileType.PPM);
  }

  // Tests new importImage() method throws an exception when given an incorrect path.
  @Test(expected = IllegalArgumentException.class)
  public void testNewImportImageIncorrectPath() {
    application.importImage("Doesn'tExists.ppm", FileType.PPM);
  }

  // Tests new importImage() method throws an exception when given a null file type.
  @Test(expected = IllegalArgumentException.class)
  public void testNewImportImageNullType() {
    application.importImage("res/SmallBoard.ppm", null);
  }

  // Tests original export() method constructs a string with
  // the correct format to create a PPM file.
  @Test
  public void testInitialExportImage() throws FileNotFoundException {
    Appendable output = new StringBuilder();

    IMultipleFormatApplication<IPicture<IPixel>> mock = new MockMultipleFormatApplication(output);

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

  // Tests original exportImage() method throws exception when given a negative index.
  @Test(expected = IllegalArgumentException.class)
  public void testInitialExportNegativeIndex() throws FileNotFoundException {
    try {
      application.addImage(application.createCheckerBoard(1, 1));
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("Unexpected exception occurred");
    }

    application.exportImage(-1, "name");
  }

  // Tests original exportImage() method throws exception when given an out of bounds index.
  @Test(expected = IllegalArgumentException.class)
  public void testInitialExportOutOfBoundsIndex() throws FileNotFoundException {
    try {
      application.addImage(application.createCheckerBoard(1, 1));
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("Unexpected exception occurred");
    }

    application.exportImage(1, "name");
  }

  // Tests original exportImage() method throws exception when given a null name.
  @Test(expected = IllegalArgumentException.class)
  public void testInitialExportNullName() throws FileNotFoundException {
    try {
      application.addImage(application.createCheckerBoard(1, 1));
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("Unexpected exception occurred");
    }

    application.exportImage(0, null);
  }

  // Tests original exportImage() method throws exception when given an empty name.
  @Test(expected = IllegalArgumentException.class)
  public void testInitialExportEmptyName() throws FileNotFoundException {
    try {
      application.addImage(application.createCheckerBoard(1, 1));
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("Unexpected exception occurred");
    }

    application.exportImage(0, "");
  }

  // Tests original exportImage() method throws exception when given a name beginning with ".".
  @Test(expected = IllegalArgumentException.class)
  public void testInitialExportDotName() throws FileNotFoundException {
    try {
      application.addImage(application.createCheckerBoard(1, 1));
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("Unexpected exception occurred");
    }

    application.exportImage(0, ".name.ppm");
  }

  // Tests new export() method constructs a string with the correct format.
  // Only testing PPM format since JPEG and PNG rely on ImageIO.
  @Test
  public void testNewExportImage() {
    Appendable output = new StringBuilder();

    IMultipleFormatApplication<IPicture<IPixel>> mock = new MockMultipleFormatApplication(output);

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

    mock.exportImage(mock.getImage(0), "Doesn'tExist", FileType.PPM);

    assertEquals(expected, output.toString());
  }

  // Tests new exportImage() method throws exception when given a null image.
  @Test(expected = IllegalArgumentException.class)
  public void testNewExportNullImage() {
    try {
      application.addImage(application.createCheckerBoard(1, 1));
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("Unexpected exception occurred");
    }

    application.exportImage(null, "name", FileType.PPM);
  }

  // Tests new exportImage() method throws exception when given a null name.
  @Test(expected = IllegalArgumentException.class)
  public void testNewExportNullName() {
    try {
      application.addImage(application.createCheckerBoard(1, 1));
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("Unexpected exception occurred");
    }

    application.exportImage(application.getImage(0), null, FileType.PPM);
  }

  // Tests new exportImage() method throws exception when given an empty name.
  @Test(expected = IllegalArgumentException.class)
  public void testNewExportEmptyName() {
    try {
      application.addImage(application.createCheckerBoard(1, 1));
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("Unexpected exception occurred");
    }

    application.exportImage(application.getImage(0), "", FileType.PPM);
  }

  // Tests new exportImage() method throws exception when given a name beginning with ".".
  @Test(expected = IllegalArgumentException.class)
  public void testNewExportDotName() {
    try {
      application.addImage(application.createCheckerBoard(1, 1));
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("Unexpected exception occurred");
    }

    application.exportImage(application.getImage(0), ".name", FileType.PPM);
  }

  // Tests new exportImage() method throws exception when given a null type.
  @Test(expected = IllegalArgumentException.class)
  public void testNewExportNullType() {
    try {
      application.addImage(application.createCheckerBoard(1, 1));
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("Unexpected exception occurred");
    }

    application.exportImage(application.getImage(0), "name", null);
  }
}