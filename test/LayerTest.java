import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.awt.Point;
import model.picture.ILayer;
import model.picture.IPicture;
import model.picture.Layer;
import model.picture.Picture;
import model.pixel.IPixel;
import model.pixel.Pixel;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for the {@code Layer} class. Ensures layers are constructed correctly with all valid
 * restrictions. Also tests functionality of getters and setters.
 */
public class LayerTest {

  ILayer<IPicture<IPixel>> layer;
  IPicture<IPixel> image;

  // Sets up a test fixture used for testing.
  // Constructs an empty layer with the name 'test' and a simple image.
  @Before
  public void setupTestFixture() {
    layer = new Layer("test");

    IPixel[][] pixels = new IPixel[1][1];
    pixels[0][0] = new Pixel(new Point(0, 0), 255, 0, 1, 2);

    image = new Picture(1, 1, 255, pixels);
  }

  // Tests layers are correctly constructed through another layer, a name and an image, or a name.
  @Test
  public void testConstructors() {
    ILayer<IPicture<IPixel>> layer1 = new Layer("layer1");

    assertEquals("layer1", layer1.getName());
    assertNull(layer1.getImage());
    assertTrue(layer1.getVisibility());

    ILayer<IPicture<IPixel>> layer2 = new Layer("layer2", image);

    assertEquals("layer2", layer2.getName());
    assertEquals(image, layer2.getImage());
    assertTrue(layer2.getVisibility());

    ILayer<IPicture<IPixel>> layer3 = new Layer(layer2);

    assertEquals("layer2", layer3.getName());
    assertEquals(image, layer3.getImage());
    assertTrue(layer3.getVisibility());

    ILayer<IPicture<IPixel>> layer4 = new Layer("layer4", false, image);

    assertEquals("layer4", layer4.getName());
    assertEquals(image, layer4.getImage());
    assertFalse(layer4.getVisibility());
  }

  // Tests exception is thrown when constructing with a null layer.
  @Test(expected = IllegalArgumentException.class)
  public void testNullLayer() {
    ILayer<IPicture<IPixel>> nullLayer = null;

    new Layer(nullLayer);
  }

  // Tests exception is thrown when constructing with a null name.
  @Test(expected = IllegalArgumentException.class)
  public void testNullName() {
    new Layer(null, image);
  }

  // Tests exception is thrown when constructing with an empty name.
  @Test(expected = IllegalArgumentException.class)
  public void testEmptyName() {
    new Layer("", image);
  }

  // Tests exception is NOT thrown when constructing with a null image.
  @Test
  public void testNullImage() {
    ILayer<IPicture<IPixel>> test = new Layer("name", null);

    assertNull(test.getImage());
  }

  // Tests getName() method returns the correct string.
  @Test
  public void testGetName() {
    assertEquals("test", layer.getName());
  }

  // Tests getVisibility() method returns the correct boolean.
  @Test
  public void testGetVisibility() {
    assertTrue(layer.getVisibility());

    layer.setVisibility(false);

    assertFalse(layer.getVisibility());
  }

  // Tests getImage() returns a copy of a layer's image (cannot mutate the original).
  @Test
  public void testGetImage() {
    layer.setImage(image);

    assertEquals(image, layer.getImage());

    double[][] kernel =
        {{0, 0, 0},
            {10, 10, 10},
            {100, 100, 100}};

    layer.getImage().filter(kernel);

    assertEquals(image, layer.getImage());
  }

  // Tests setVisibility() method correctly sets this layer's visibility.
  @Test
  public void testSetVisibility() {
    assertTrue(layer.getVisibility());

    layer.setVisibility(false);

    assertFalse(layer.getVisibility());
  }

  // Tests setImage() correctly sets this layer's image (and accepts null).
  @Test
  public void testSetImage() {
    assertNull(layer.getImage());

    layer.setImage(image);

    assertEquals(image, layer.getImage());

    layer.setImage(null);

    assertNull(layer.getImage());
  }

  // Tests toString() provides the correct string representation of an image.
  @Test
  public void testToString() {
    String expected = "Name: test\n"
        + "Visible: true\n"
        + image.toString() + "\n";

    layer.setImage(image);

    assertEquals(expected, layer.toString());
  }

  // Tests equals() method correctly determines whether a layer is equal to another object based on
  // reference or equivalent values.
  @Test
  public void testEquals() {
    assertEquals(layer, layer);
    assertEquals(new Layer("test"), layer);
    assertNotEquals(new Layer("not test"), layer);
    assertNotEquals(null, layer);
  }

  // Tests hashCode() method produces the same hashcode for equivalent layers.
  @Test
  public void testHashCode() {
    assertEquals(new Layer("test").hashCode(), layer.hashCode());
  }
}