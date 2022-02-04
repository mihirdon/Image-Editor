import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.picture.ILayer;
import model.picture.ILayeredPicture;
import model.picture.IPicture;
import model.picture.Layer;
import model.picture.LayeredPicture;
import model.picture.Picture;
import model.pixel.ColorChannel;
import model.pixel.IPixel;
import model.pixel.Pixel;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for the {@code LayeredPicture} class. Ensures layered images are constructed correctly
 * with all valid restrictions. Also tests functionality of getters, setters, and manipulating
 * layers.
 */
public class LayeredPictureTest {

  IPicture<IPixel> subImage;
  ILayer<IPicture<IPixel>> layer;
  ILayeredPicture<ILayer<IPicture<IPixel>>> image;

  // Sets up a test fixture used for testing.
  // Constructs an empty layered image and a layer with a simple image.
  @Before
  public void setupTestFixture() {
    IPixel[][] pixels = new IPixel[1][1];
    pixels[0][0] = new Pixel(new Point(0, 0), 255, 0, 1, 2);

    subImage = new Picture(1, 1, 255, pixels);

    layer = new Layer("test", subImage);

    image = new LayeredPicture(1, 1, 255, Collections.singletonList(layer));
  }

  // Tests layered images are correctly constructed through another layered image.
  @Test
  public void testConstructLayeredImageThroughImage() {
    ILayeredPicture<ILayer<IPicture<IPixel>>> image1 = new LayeredPicture(image);

    assertEquals(image1.getLayers(), image.getLayers());
    assertEquals(image1.getCurrentLayer(), image.getCurrentLayer());
    assertEquals(image1.getHeight(), image.getHeight());
    assertEquals(image1.getWidth(), image.getWidth());
    assertEquals(image1.getMaxVal(), image.getMaxVal());
  }

  // Tests an exception is thrown when attempting to construct a layered image with null.
  @Test(expected = IllegalArgumentException.class)
  public void testConstructingLayeredImageThroughImageButImageIsNull() {
    new LayeredPicture(null);
  }

  // Tests layered images are correctly constructed through a width, height, and maximum value.
  @Test
  public void testConstructWithAllArgsExceptLayers() {
    ILayeredPicture<ILayer<IPicture<IPixel>>> image1 =
        new LayeredPicture(1, 1, 255);

    assertEquals(new ArrayList<ILayer<IPicture<IPixel>>>(), image1.getLayers());
    assertNull(image1.getCurrentLayer());
    assertEquals(1, image1.getWidth());
    assertEquals(1, image1.getHeight());
    assertEquals(255, image1.getMaxVal());
  }

  // Tests an exception is thrown when attempting to construct a layered image with
  // negative width/height and a negative maximum value.
  @Test(expected = IllegalArgumentException.class)
  public void testConstructWithZeroWidthHeight() {
    new LayeredPicture(0, 0, -1);
  }

  // Tests an exception is thrown when attempting to construct a layered image with
  // negative width/height and a valid maximum value.
  @Test(expected = IllegalArgumentException.class)
  public void testConstructWithNegativeWidthHeightM() {
    new LayeredPicture(-1, -1, 0);
  }

  // Tests layered images are correctly constructed through a list of names.
  @Test
  public void testConstructWithNames() {
    ILayeredPicture<ILayer<IPicture<IPixel>>> image1 =
        new LayeredPicture(1, 1, 255, "Hi");

    assertEquals(
        new ArrayList<ILayer<IPicture<IPixel>>>(Collections.singletonList(new Layer("Hi"))),
        image1.getLayers());
    assertNull(image1.getCurrentLayer());
    assertEquals(1, image1.getWidth());
    assertEquals(1, image1.getHeight());
    assertEquals(255, image1.getMaxVal());
  }

  // Tests an exception is thrown when attempting to construct a layered image with a null name.
  @Test(expected = IllegalArgumentException.class)
  public void testConstructWithNullName() {
    new LayeredPicture(1, 1, 0, "hello", null);
  }

  // Tests layered images are correctly constructed through a list of layers.
  @Test
  public void testConstructWithLayers() {
    List<ILayer<IPicture<IPixel>>> layerList = new ArrayList<>();

    layerList.add(new Layer("Hi"));
    layerList.add(new Layer("Hello", subImage));

    ILayeredPicture<ILayer<IPicture<IPixel>>> image1 =
        new LayeredPicture(1, 1, 255, layerList);

    assertArrayEquals(layerList.toArray(), image1.getLayers().toArray());
    assertNull(image1.getCurrentLayer());
    assertEquals(1, image1.getWidth());
    assertEquals(1, image1.getHeight());
    assertEquals(255, image1.getMaxVal());
  }

  // Tests an exception is thrown when attempting to construct a layered image with a null list.
  @Test(expected = IllegalArgumentException.class)
  public void testConstructWithNullLayers() {
    new LayeredPicture(1, 1, 0, (List<ILayer<IPicture<IPixel>>>) null);
  }

  // Tests an exception is thrown when attempting to construct a layered image with a
  // null layer within the list.
  @Test(expected = IllegalArgumentException.class)
  public void testConstructWithSingleNullLayerInMiddleOfLayers() {
    List<ILayer<IPicture<IPixel>>> layerList = new ArrayList<>();
    layerList.add(new Layer("Hi"));
    layerList.add(new Layer("Hello", subImage));
    layerList.add(null);

    new LayeredPicture(1, 1, 255, layerList);
  }

  // Tests getWidth() method returns the correct int.
  @Test
  public void testGetWidth() {
    assertEquals(1, image.getWidth());
  }

  // Tests getHeight() method returns the correct int.
  @Test
  public void testGetHeight() {
    assertEquals(1, image.getHeight());
  }

  // Tests getMaxVal() method returns the correct int.
  @Test
  public void testGetMaxVal() {
    assertEquals(255, image.getMaxVal());
  }

  // Tests getPixelSequence() method returns a copy of the current layer's image's pixel sequence.
  // and prevents them from being mutated.
  @Test
  public void testGetPixelSequence() {
    image.setCurrentLayer("test");

    IPixel[][] pixels = new IPixel[1][1];
    pixels[0][0] = new Pixel(new Point(0, 0), 255, 0, 1, 2);

    assertArrayEquals(pixels, image.getPixelSequence());

    image.getPixelSequence()[0][0].setChannelValue(ColorChannel.RED, -1);

    assertArrayEquals(pixels, image.getPixelSequence());
  }

  // Tests getPixelSequence() method throws exception when layer hasn't been selected.
  // (Current layer is null).
  @Test(expected = IllegalStateException.class)
  public void testGetNullPixelSequence() {
    image.getPixelSequence();
  }

  // Tests getChannelValues() method returns a copy of the current layer's image's pixel
  // color channel values and prevents them from being mutated.
  @Test
  public void testGetChannelValues() {
    image.setCurrentLayer("test");

    int[][] expected = {{0}};

    assertArrayEquals(expected, image.getChannelValues(ColorChannel.RED));

    image.getChannelValues(ColorChannel.RED)[0][0] = -1;

    assertArrayEquals(expected, image.getChannelValues(ColorChannel.RED));
  }

  // Tests getChannelValues() method throws exception when layer hasn't been selected.
  // (Current layer is null).
  @Test(expected = IllegalStateException.class)
  public void testGetNullChannelValues() {
    image.getChannelValues(ColorChannel.RED);
  }

  // Tests getImageSubset() method returns a copy of a subset of the current layer's image's
  // color channel values and prevents them from being mutated.
  @Test
  public void testGetImageSubset() {
    image.setCurrentLayer("test");

    int[][] expected = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};

    assertArrayEquals(expected, image.getImageSubset(3, new Point(), ColorChannel.RED));

    image.getImageSubset(3, new Point(), ColorChannel.RED)[0][0] = -1;

    assertArrayEquals(expected, image.getImageSubset(3, new Point(), ColorChannel.RED));
  }

  // Tests getImageSubset() method throws exception when layer hasn't been selected.
  // (Current layer is null).
  @Test(expected = IllegalStateException.class)
  public void testGetNullImageSubset() {
    image.getImageSubset(3, new Point(), ColorChannel.RED);
  }

  // Tests getLayers() method returns the correct list of layers.
  @Test
  public void testGetLayers() {
    List<ILayer<IPicture<IPixel>>> initial = new ArrayList<>();
    initial.add(layer);

    assertEquals(initial, image.getLayers());

    List<ILayer<IPicture<IPixel>>> expected = new ArrayList<>();
    expected.add(new Layer("Hi", subImage));
    expected.add(layer);
    expected.add(new Layer("Hello"));

    image.addLayer("Hello");
    image.addLayer(new Layer("Hi", subImage));

    assertEquals(expected, image.getLayers());
  }

  // Tests getCurrentLayer() method returns the correct layer.
  @Test
  public void testGetCurrentLayer() {
    assertNull(image.getCurrentLayer());

    image.addLayer("hi");
    image.setCurrentLayer("hi");

    assertEquals(new Layer("hi"), image.getCurrentLayer());
  }

  // Tests filter() method correctly updates the current layer's image.
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

    IPicture<IPixel> testImage = new Picture(3, 3, 255, originalPixels);

    image.addLayer(new Layer("filter", testImage));
    image.setCurrentLayer("filter");

    assertArrayEquals(finalValues, image.filter(kernel).getChannelValues(ColorChannel.RED));
    assertArrayEquals(finalValues, image.filter(kernel).getChannelValues(ColorChannel.GREEN));
    assertArrayEquals(finalValues, image.filter(kernel).getChannelValues(ColorChannel.BLUE));
  }

  // Tests filter() method throws an exception if given kernel is even.
  @Test(expected = IllegalArgumentException.class)
  public void testEvenKernel() {
    try {
      image.setCurrentLayer("test");
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("Unexpected exception thrown");
    }

    image.filter(new double[2][2]);
  }

  // Tests filter() method throws an exception if given kernel is not square.
  @Test(expected = IllegalArgumentException.class)
  public void testNonSquareKernel() {
    try {
      image.setCurrentLayer("test");
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("Unexpected exception thrown");
    }

    image.filter(new double[3][5]);
  }

  // Tests filter() method throws an exception when attempting to filter
  // when a layer hasn't been selected.
  @Test(expected = IllegalStateException.class)
  public void testInvalidFilter() {
    image.filter(new double[3][3]);
  }

  // colorTransform() (Invalid layer)

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

    IPicture<IPixel> testImage = new Picture(3, 3, 255, originalPixels);

    image.addLayer(new Layer("colorTransform", testImage));
    image.setCurrentLayer("colorTransform");

    assertArrayEquals(finalRedValues,
        image.colorTransform(matrix).getChannelValues(ColorChannel.RED));
    assertArrayEquals(finalGreenValues,
        image.colorTransform(matrix).getChannelValues(ColorChannel.GREEN));
    assertArrayEquals(finalBlueValues,
        image.colorTransform(matrix).getChannelValues(ColorChannel.BLUE));
  }

  // Tests colorTransform() method throws an exception if given matrix does not have the same
  // dimensions as the number of color channels.
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidMatrix() {
    try {
      image.setCurrentLayer("test");
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("Unexpected exception thrown");
    }

    image.colorTransform(new double[2][4]);
  }

  // Tests colorTransform() method throws an exception when attempting to filter
  // when a layer hasn't been selected.
  @Test(expected = IllegalStateException.class)
  public void testInvalidColorTransform() {
    image.colorTransform(new double[3][3]);
  }

  // Tests setCurrentLayer() method correctly changes to the specified layer.
  @Test
  public void testSetCurrentLayer() {
    assertNull(image.getCurrentLayer());

    image.setCurrentLayer("test");

    assertEquals(layer, image.getCurrentLayer());
  }

  // Tests setCurrentLayer() method throws an exception when given a null layer.
  @Test(expected = IllegalArgumentException.class)
  public void testSetNullCurrentLayer() {
    image.setCurrentLayer(null);
  }

  // Tests setCurrentLayer() method throws an exception when selecting a layer that doesn't exist.
  @Test(expected = IllegalArgumentException.class)
  public void testSetInvalidCurrentLayer() {
    image.setCurrentLayer("hi");
  }

  // Tests addLayer() correctly adds an empty layer with the given name.
  @Test
  public void testAddLayerWithName() {
    assertFalse(image.getLayers().contains(new Layer("added")));

    image.addLayer("added");

    assertTrue(image.getLayers().contains(new Layer("added")));
  }

  // Tests addLayer() throws an exception when given a null name.
  @Test(expected = IllegalArgumentException.class)
  public void testAddNullName() {
    image.addLayer((String) null);
  }

  // Tests addLayer() throws an exception when adding with a pre-existing layer's name.
  @Test(expected = IllegalArgumentException.class)
  public void testAddExistingName() {
    image.addLayer("test");
  }

  // Tests addLayer() correctly adds the given layer.
  @Test
  public void testAddLayerWithLayer() {
    assertFalse(image.getLayers().contains(new Layer("added")));

    image.addLayer(new Layer("added"));

    assertTrue(image.getLayers().contains(new Layer("added")));
  }

  // Tests addLayer() throws an exception when given a null name.
  @Test(expected = IllegalArgumentException.class)
  public void testAddNullLayer() {
    image.addLayer((ILayer<IPicture<IPixel>>) null);
  }

  // Tests addLayer() throws an exception when adding with a pre-existing layer's name.
  @Test(expected = IllegalArgumentException.class)
  public void testAddExistingLayer() {
    image.addLayer(new Layer("test"));
  }

  // Tests removeLayer() correctly removes the layer with the given name.
  @Test
  public void testRemoveLayerWithName() {
    assertTrue(image.getLayers().contains(layer));

    image.removeLayer("test");

    assertFalse(image.getLayers().contains(layer));
  }

  // Tests removeLayer() throws an exception when given a null name.
  @Test(expected = IllegalArgumentException.class)
  public void testRemoveNullName() {
    image.removeLayer((String) null);
  }

  // Tests removeLayer() throws an exception when given a name
  // that doesn't belong to any of the layers.
  @Test(expected = IllegalArgumentException.class)
  public void testRemoveNonExistentName() {
    image.removeLayer("doesn't exist");
  }

  // Tests removeLayer() correctly removes the given layer.
  @Test
  public void testRemoveLayerWithLayer() {
    assertTrue(image.getLayers().contains(layer));

    image.removeLayer(layer);

    assertFalse(image.getLayers().contains(layer));
  }

  // Tests removeLayer() throws an exception when given a null layer.
  @Test(expected = IllegalArgumentException.class)
  public void testRemoveNullLayer() {
    image.removeLayer((ILayer<IPicture<IPixel>>) null);
  }

  // Tests removeLayer() throws an exception when given a layer that doesn't exist.
  @Test(expected = IllegalArgumentException.class)
  public void testRemoveNonExistentLayer() {
    image.removeLayer(new Layer("doesn't exist"));
  }

  // Tests setVisibility() method correctly changes the current layer's visibility.
  @Test
  public void testSetVisibility() {
    image.setCurrentLayer("test");

    assertTrue(image.getCurrentLayer().getVisibility());

    image.setVisibility(false);

    assertFalse(image.getCurrentLayer().getVisibility());

    image.setVisibility(true);

    assertTrue(image.getCurrentLayer().getVisibility());
  }

  // Tests setVisibility() method throws an exception when a layer hasn't been selected.
  @Test(expected = IllegalStateException.class)
  public void testInvalidSetVisibility() {
    image.setVisibility(true);
  }

  // Tests toString() method correctly represents a layer as a string.
  @Test
  public void testToString() {
    String expected = "Width: 1\n"
        + "Height: 1\n"
        + "Maximum value: 255\n"
        + "Current layer: null\n"
        + layer.toString() + "\n";

    assertEquals(expected, image.toString());
  }

  // Tests equals() method correctly compares the same reference, equivalent layered images,
  // non-layered images, and different layered images.
  @Test
  public void testEquals() {
    ILayeredPicture<ILayer<IPicture<IPixel>>> equivalent = new LayeredPicture(image);
    ILayeredPicture<ILayer<IPicture<IPixel>>> different =
        new LayeredPicture(1, 2, 100);

    assertEquals(image, image);
    assertEquals(equivalent, image);
    assertNotEquals(different, image);
    assertNotEquals(null, image);
  }

  // Tests hashCode() creates matching integers for equivalent layered images.
  @Test
  public void testHashCode() {
    assertEquals(new LayeredPicture(image).hashCode(), image.hashCode());
  }
}