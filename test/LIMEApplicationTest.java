import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.application.Application;
import model.application.ILayeredApplication;
import model.application.ImportExportUtil.FileType;
import model.application.LIMEApplication;
import model.picture.ILayer;
import model.picture.ILayeredPicture;
import model.picture.IPicture;
import model.picture.Layer;
import model.picture.LayeredPicture;
import model.picture.Picture;
import model.pixel.IPixel;
import model.pixel.Pixel;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for the {@code LIMEApplication} class. Ensures application is constructed correctly
 * with all valid restrictions. Also tests functionality of getters and all image processing
 * operations.
 */
public class LIMEApplicationTest {

  IPicture<IPixel> subImage;
  ILayer<IPicture<IPixel>> layer;
  ILayeredPicture<ILayer<IPicture<IPixel>>> image;
  ILayeredApplication application;

  // Sets up a test fixture used for testing.
  // Constructs a valid application with an empty list of layered images as well as an
  // example layered image with a layer and subimage.
  @Before
  public void setupTestFixture() {
    IPixel[][] pixels = new IPixel[1][1];
    pixels[0][0] = new Pixel(new Point(0, 0), 255, 0, 1, 2);

    subImage = new Picture(1, 1, 255, pixels);

    layer = new Layer("test", subImage);

    image = new LayeredPicture(1, 1, 255, Collections.singletonList(layer));

    application = new LIMEApplication();
  }

  // Tests application can be constructed with given list of layered images and without arguments.
  @Test
  public void testConstructors() {
    List<ILayeredPicture<ILayer<IPicture<IPixel>>>> imageList = new ArrayList<>();
    imageList.add(image);

    ILayeredApplication application = new LIMEApplication(imageList);

    assertEquals(image, application.getImage(0));

    ILayeredApplication emptyApplication = new LIMEApplication();

    assertEquals(0, emptyApplication.getImages().size());
  }

  // Tests an exception is thrown when constructor is given a null image list.
  @Test(expected = IllegalArgumentException.class)
  public void testApplicationConstructorException() {
    new LIMEApplication(null);
  }

  // Tests getImages() method returns a copy of an application's list of images.
  // Also tests mutating result does not mutate original list.
  @Test
  public void testGetImages() {
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
    application.addImage(image);

    assertEquals(image, application.getImage(0));
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

    ILayeredPicture<ILayer<IPicture<IPixel>>> originalImage =
        new LayeredPicture(2, 2, 255,
            Collections.singletonList(new Layer("test",
                new Picture(2, 2, 255, pixelSequence))));

    ILayeredPicture<ILayer<IPicture<IPixel>>> blurredImage =
        new LayeredPicture(2, 2, 255,
            Collections.singletonList(new Layer("test",
                new Picture(2, 2, 255, blurredPixelSequence))));

    application.addImage(originalImage);
    application.setCurrentPicture(originalImage);
    application.setCurrentLayer("test");

    assertEquals(blurredImage, application.blur(0));
  }

  // Tests blur() method throws an exception when given a negative index.
  @Test(expected = IllegalArgumentException.class)
  public void testBlurNegativeIndex() {
    application.blur(-1);
  }

  // Tests blur() method throws an exception when given an out of bounds index.
  @Test(expected = IllegalArgumentException.class)
  public void testBlurOutOfBoundsIndex() {
    application.addImage(image);
    application.blur(1);
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

    ILayeredPicture<ILayer<IPicture<IPixel>>> originalImage =
        new LayeredPicture(2, 2, 255,
            Collections.singletonList(new Layer("testLayer",
                new Picture(2, 2, 255, pixelSequence))));

    ILayeredPicture<ILayer<IPicture<IPixel>>> sharpenedImage =
        new LayeredPicture(2, 2, 255,
            Collections.singletonList(new Layer("testLayer",
                new Picture(2, 2, 255, pixelSequence).filter(sharpenKernel))));

    application.addImage(originalImage);
    application.setCurrentPicture(originalImage);
    application.setCurrentLayer("testLayer");

    assertEquals(sharpenedImage, application.sharpen(0));
  }

  // Tests sharpen() method throws an exception when given a negative index.
  @Test(expected = IllegalArgumentException.class)
  public void testSharpenNegativeIndex() {
    application.sharpen(-1);
  }

  // Tests sharpen() method throws an exception when given an out of bounds index.
  @Test(expected = IllegalArgumentException.class)
  public void testSharpenOutOfBoundsIndex() {
    application.addImage(image);
    application.sharpen(1);
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

    ILayeredPicture<ILayer<IPicture<IPixel>>> originalImage =
        new LayeredPicture(2, 2, 255,
            Collections.singletonList(new Layer("testLayer",
                new Picture(2, 2, 255, pixelSequence))));

    ILayeredPicture<ILayer<IPicture<IPixel>>> monochromeImage =
        new LayeredPicture(2, 2, 255,
            Collections.singletonList(new Layer("testLayer",
                new Picture(2, 2, 255, monochromePixelSequence))));

    application.addImage(originalImage);
    application.setCurrentPicture(originalImage);
    application.setCurrentLayer("testLayer");

    assertEquals(monochromeImage, application.monochrome(0));
  }

  // Tests monochrome() method throws an exception when given a negative index.
  @Test(expected = IllegalArgumentException.class)
  public void testMonochromeNegativeIndex() {
    application.monochrome(-1);
  }

  // Tests monochrome() method throws an exception when given an out of bounds index.
  @Test(expected = IllegalArgumentException.class)
  public void testMonochromeOutOfBoundsIndex() {
    application.addImage(image);
    application.monochrome(1);
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

    ILayeredPicture<ILayer<IPicture<IPixel>>> originalImage =
        new LayeredPicture(2, 2, 255,
            Collections.singletonList(new Layer("testLayer",
                new Picture(2, 2, 255, pixelSequence))));

    ILayeredPicture<ILayer<IPicture<IPixel>>> sepiaImage =
        new LayeredPicture(2, 2, 255,
            Collections.singletonList(new Layer("testLayer",
                new Picture(2, 2, 255, sepiaTonePixelSequence))));

    application.addImage(originalImage);
    application.setCurrentPicture(originalImage);
    application.setCurrentLayer("testLayer");

    assertEquals(sepiaImage, application.sepiaTone(0));
  }

  // Tests sepiaTone() method throws an exception when given a negative index.
  @Test(expected = IllegalArgumentException.class)
  public void testSepiaNegativeIndex() {
    application.sepiaTone(-1);
  }

  // Tests sepiaTone() method throws an exception when given an out of bounds index.
  @Test(expected = IllegalArgumentException.class)
  public void testSepiaOutOfBoundsIndex() {
    application.addImage(image);
    application.sepiaTone(1);
  }

  // Tests createCheckerboard() method correctly creates a checkerboard image programmatically.
  @Test
  public void testCreateCheckerboard() {
    IPixel[][] checkerboard = new Pixel[2][2];

    checkerboard[0][0] = new Pixel(new Point(), 255, 255, 255);
    checkerboard[0][1] = new Pixel(new Point(1, 0), 0, 0, 0);
    checkerboard[1][0] = new Pixel(new Point(0, 1), 0, 0, 0);
    checkerboard[1][1] = new Pixel(new Point(1, 1), 255, 255, 255);

    IPicture<IPixel> boardImage = new Picture(2, 2, 255, checkerboard);

    ILayeredPicture<ILayer<IPicture<IPixel>>> testImage =
        new LayeredPicture(2, 2, 255,
            Collections.singletonList(new Layer("Checkerboard", boardImage)));

    assertEquals(testImage, application.createCheckerBoard(1, 2));
  }

  // Tests createCheckerboard() method throws an exception when given negative arguments.
  @Test(expected = IllegalArgumentException.class)
  public void testCreateCheckerboardExceptions() {
    application.createCheckerBoard(-1, -2);
  }



  // Tests new importImage() method correctly creates an image from the given file path and type
  // for each supported file type.
  @Test
  public void testNewImportImage() {
    assertEquals(application.createCheckerBoard(1, 2),
        application.importImage("res/PPMLayeredBoard.txt", FileType.PPM));

    assertEquals(application.createCheckerBoard(1, 2),
        application.importImage("res/PNGLayeredBoard.txt", FileType.PNG));

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




  // Tests new export() method constructs a string with the correct format.
  // Only testing PPM format since JPEG and PNG rely on ImageIO.
  @Test
  public void testNewExportImage() {
    Appendable output = new StringBuilder();

    ILayeredApplication mock = new MockLIMEApplication(output);

    mock.addImage(application.createCheckerBoard(1, 2));

    String expected = "Doesn'tExist.txt\n"
        + "2 2 255\n"
        + "Checkerboard true res/Checkerboard.ppm\n";

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

  // Tests getCurrentImage() returns the currently selected image.
  @Test
  public void testGetCurrentImage() {
    assertNull(application.getCurrentImage());

    application.addImage(image);
    application.setCurrentPicture(image);

    assertEquals(image, application.getCurrentImage());
  }

  // Tests createCurrentLayer() correctly create a new layer in the current image given a name.
  @Test
  public void testCreateLayerWithName() {
    application.addImage(image);
    application.setCurrentPicture(image);

    ILayer<IPicture<IPixel>> testLayer = new Layer("testLayer");

    assertFalse(image.getLayers().contains(testLayer));

    application.createLayer("testLayer");

    assertTrue(image.getLayers().contains(testLayer));
  }

  // Tests createCurrentLayer() correctly create a new layer in the current image given a name.
  @Test
  public void testCreateLayerWithLayer() {
    application.addImage(image);
    application.setCurrentPicture(image);

    ILayer<IPicture<IPixel>> testLayer = new Layer("testLayer");

    assertFalse(image.getLayers().contains(testLayer));

    application.createLayer(testLayer);

    assertTrue(image.getLayers().contains(testLayer));
  }

  // Tests setCurrentLayer() correctly changes the current image's current layer
  // given a layer's name.
  @Test
  public void testSetCurrentLayerWithName() {
    application.addImage(image);
    application.setCurrentPicture(image);

    assertNull(image.getCurrentLayer());

    application.setCurrentLayer("test");

    assertEquals(layer, image.getCurrentLayer());
  }

  // Tests setCurrentPicture() method correctly selects the desired image given an index.
  @Test
  public void testSetCurrentPictureWithIndex() {
    ILayeredPicture<ILayer<IPicture<IPixel>>> testImage =
        new LayeredPicture(1, 1, 255);
    application.addImage(image);
    application.addImage(testImage);

    assertEquals(image, application.getCurrentImage());

    application.setCurrentPicture(1);

    assertEquals(testImage, application.getCurrentImage());
  }

  // Tests setCurrentPicture() method correctly selects the desired image given an image.
  @Test
  public void testSetCurrentPictureWithImage() {
    ILayeredPicture<ILayer<IPicture<IPixel>>> testImage =
        new LayeredPicture(1, 1, 255);
    application.addImage(image);
    application.addImage(testImage);

    assertEquals(image, application.getCurrentImage());

    application.setCurrentPicture(testImage);

    assertEquals(testImage, application.getCurrentImage());
  }

  // Tests setCurrentLayer() correctly changes the current image's current layer
  // given a layer.
  @Test
  public void testSetCurrentLayerWithLayer() {
    application.addImage(image);
    application.setCurrentPicture(image);

    assertNull(image.getCurrentLayer());

    application.setCurrentLayer(layer);

    assertEquals(layer, image.getCurrentLayer());
  }

  // Tests removeLayer() method removes the desired layer given a name.
  @Test
  public void testRemoveLayerWithName() {
    application.addImage(image);
    application.setCurrentPicture(image);

    assertTrue(image.getLayers().contains(layer));

    application.removeLayer("test");

    assertFalse(image.getLayers().contains(layer));
  }

  // Tests removeLayer() method removes the desired layer given a layer.
  @Test
  public void testRemoveLayerWithLayer() {
    application.addImage(image);
    application.setCurrentPicture(image);

    assertTrue(image.getLayers().contains(layer));

    application.removeLayer(layer);

    assertFalse(image.getLayers().contains(layer));
  }

  // Tests setVisibility() method correctly updates the current image's current layer's visibility.
  @Test
  public void testSetVisibility() {
    application.addImage(image);
    application.setCurrentPicture(image);
    application.setCurrentLayer(layer);

    assertTrue(layer.getVisibility());

    application.setVisibility(false);

    assertFalse(layer.getVisibility());

    application.setVisibility(true);

    assertTrue(layer.getVisibility());
  }

  // Tests exportTopMostVisibleLayer() method exports the correct layer.
  @Test
  public void testExportTopMostVisibleLayer() {
    StringBuilder output = new StringBuilder();
    MockLIMEApplication mock = new MockLIMEApplication(output);

    mock.addImage(image);
    mock.setCurrentPicture(image);
    mock.setCurrentLayer(layer.getName());

    assertTrue(layer.getVisibility());

    mock.setVisibility(false);

    assertFalse(layer.getVisibility());

    ILayer<IPicture<IPixel>> testLayer = new Layer("testLayer",
        new Application().createCheckerBoard(1, 2));

    mock.createLayer(testLayer);

    assertTrue(testLayer.getVisibility());

    mock.exportTopMostVisibleLayer("res/testLayer", FileType.PPM);

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

    assertEquals(expected, output.toString());
  }
}