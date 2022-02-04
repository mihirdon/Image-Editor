import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import model.application.Application;
import model.application.GUIModel;
import model.application.ILayeredApplication;
import model.application.IModelObserver;
import model.application.LIMEApplication;
import model.picture.ILayer;
import model.picture.ILayeredPicture;
import model.picture.IPicture;
import model.picture.Layer;
import model.picture.LayeredPicture;
import model.pixel.ColorChannel;
import model.pixel.IPixel;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@code GUIModel} ensuring functionality works as expected.
 */
public class GUIModelTest {
  IPicture<IPixel> image1;
  IPicture<IPixel> image2;
  ILayer<IPicture<IPixel>> layer1;
  ILayer<IPicture<IPixel>> layer2;
  ILayer<IPicture<IPixel>> layer3;
  ILayeredPicture<ILayer<IPicture<IPixel>>> layered;
  ILayeredApplication app;
  IModelObserver<IPicture<IPixel>> model;

  // Test fixture used for testing purposes.
  // Constructs a GUIModel with an application containing layers with several
  // different visible or invisible images.
  @Before
  public void setupTestFixture() {
    image1 = new Application().createCheckerBoard(1, 2);
    image2 = new Application().createCheckerBoard(2, 2);
    layer1 = new Layer("board", image1);
    layer2 = new Layer("hedgehog", image2);
    layer3 = new Layer("empty");
    layered = new LayeredPicture(0, 0, 255,
        Arrays.asList(layer1, layer2, layer3));
    app = new LIMEApplication();
    app.addImage(layered);
    app.setCurrentPicture(layered);
    model = new GUIModel(app);
  }

  // Tests getTopMostVisible() method correctly returns the top-most visible image when
  // there are two visible images, one empty image, and none are currently selected.
  @Test
  public void testTwoVisible() {
    assertEquals(image2, model.getTopMostVisible());
  }

  // Tests getTopMostVisible() method correctly returns the top-most visible image when
  // there are two visible images, one empty image, and the empty is currently selected.
  @Test
  public void testSelectedEmpty() {
    app.setCurrentLayer(layer3);
    assertEquals(image2, model.getTopMostVisible());
  }

  // Tests getTopMostVisible() method correctly returns the top-most visible image when
  // there are two visible images, one empty image, and a non-empty is currently selected.
  @Test
  public void testVisibleSelected() {
    app.setCurrentLayer(layer1);
    assertEquals(image1, model.getTopMostVisible());
  }

  // Tests getTopMostVisible() method correctly returns the top-most visible image when
  // there is one visible image, one invisible image, one empty image,
  // and the invisible is currently selected.
  @Test
  public void testVisibleAndInvisible() {
    app.setCurrentLayer(layer2);
    app.setVisibility(false);
    assertEquals(image1, model.getTopMostVisible());
  }

  // Tests getTopMostVisible() method correctly returns the top-most visible image when
  // there are two invisible images, one empty image, and none are currently selected.
  @Test
  public void testInvisible() {
    app.setCurrentLayer(layer1);
    app.setVisibility(false);
    app.setCurrentLayer(layer2);
    app.setVisibility(false);
    assertNull(model.getTopMostVisible());
  }

  // Tests createBufferedImage() returns the correct buffered image representing
  // the top-most visible layer.
  @Test
  public void createBufferedImage() {
    BufferedImage expected = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
    Image bf = model.createBufferedImage(image1);

    for (int h = 0; h < 2; h++) {
      for (int w = 0; w < 2; w++) {
        int red = image1.getChannelValues(ColorChannel.RED)[h][w];
        int green = image1.getChannelValues(ColorChannel.GREEN)[h][w];
        int blue = image1.getChannelValues(ColorChannel.BLUE)[h][w];

        Color c = new Color(red, green, blue);

        expected.setRGB(w, h, c.getRGB());

        assertEquals(expected.getRGB(w, h), ((BufferedImage) bf).getRGB(w, h));
      }
    }
  }

  // Tests createBufferedImage() returns null when given a null image.
  @Test
  public void testNullImage() {
    assertNull(model.createBufferedImage(null));
  }
}