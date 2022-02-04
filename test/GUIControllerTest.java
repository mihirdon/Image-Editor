import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import model.application.Application;
import model.application.ILayeredApplication;
import model.application.LIMEApplication;
import model.picture.ILayer;
import model.picture.ILayeredPicture;
import model.picture.IPicture;
import model.picture.Layer;
import model.picture.LayeredPicture;
import model.pixel.IPixel;
import org.junit.Before;
import org.junit.Test;
import view.GUIView;

/**
 * Test class for {@code GUIController} ensuring the controller receives the correct action events
 * and performs the correct operations.
 */
public class GUIControllerTest extends GUIView {

  MockGUIController controller;

  // Test fixture used for testing.
  // Constructs a controller containing an application.
  @Before
  public void setupTestFixture() {
    IPicture<IPixel> image = new Application().createCheckerBoard(1, 2);
    ILayer<IPicture<IPixel>> layer = new Layer("test", image);
    ILayeredPicture<ILayer<IPicture<IPixel>>> layered =
        new LayeredPicture(0, 0, 255,
            Collections.singletonList(layer));
    ILayeredApplication app = new LIMEApplication();
    app.addImage(layered);
    app.setCurrentPicture(layered);
    app.setCurrentLayer(layer);

    controller = new MockGUIController(app, this);
  }

  // Tests actionPerformed() method changes the layer creation format through the drop-down.
  @Test
  public void testCreateOptions() {
    assertEquals("Switch create format", controller.getActionPerformed("Create Options"));
  }

  // Tests actionPerformed() method provides the correct "create" command.
  @Test
  public void testCreateButton() {
    assertEquals("Regular Layer", createOptions.getSelectedItem());
    layerCreationName.setText("layerName");
    assertEquals("layerName", layerCreationName.getText());

    assertEquals("create layer layerName", controller.getActionPerformed("Create Button"));
    assertTrue(layerCreationName.getText().isEmpty());

    createOptions.setSelectedItem("Checkerboard Layer");
    tileSize.setText("1");
    numTiles.setText("2");
    assertEquals("1", tileSize.getText());
    assertEquals("2", numTiles.getText());

    assertEquals("checkerboard 1 2", controller.getActionPerformed("Create Button"));
    assertTrue(tileSize.getText().isEmpty());
    assertTrue(numTiles.getText().isEmpty());
  }

  // Tests actionPerformed() method provides the correct "apply" command.
  @Test
  public void testApplyButton() {
    assertEquals("Blur", operationOptions.getSelectedItem());
    assertEquals("blur", controller.getActionPerformed("Apply Button"));

    operationOptions.setSelectedItem("Sharpen");
    assertEquals("Sharpen", operationOptions.getSelectedItem());
    assertEquals("sharpen", controller.getActionPerformed("Apply Button"));

    operationOptions.setSelectedItem("Monochrome");
    assertEquals("Monochrome", operationOptions.getSelectedItem());
    assertEquals("monochrome", controller.getActionPerformed("Apply Button"));

    operationOptions.setSelectedItem("Sepia");
    assertEquals("Sepia", operationOptions.getSelectedItem());
    assertEquals("sepia", controller.getActionPerformed("Apply Button"));
  }

  // Tests actionPerformed() method saves an image.
  @Test
  public void testSaveButton() {
    assertEquals("Save file", controller.getActionPerformed("Save Button"));
  }

  // Tests actionPerformed() method provides the correct "current" command.
  @Test
  public void testLayerOptions() {
    layerOptions.addItem("layerName");
    layerOptions.setSelectedItem("layerName");
    assertEquals("layerName", layerOptions.getSelectedItem());
    assertEquals("current layerName", controller.getActionPerformed("Layer Options"));
  }

  // Tests actionPerformed() method opens a file.
  @Test
  public void testOpenButton() {
    assertEquals("Open image", controller.getActionPerformed("Open Button"));
  }

  // Tests actionPerformed() method provides the correct "load" command.
  @Test
  public void testLoadButton() {
    openedFilePath.setText("res/hedgehog.ppm");
    assertEquals("load res/hedgehog.ppm", controller.getActionPerformed("Load Button"));
  }

  // Tests actionPerformed() method executes a script.
  @Test
  public void testScriptButton() {
    assertEquals("Execute script", controller.getActionPerformed("Script Button"));
  }
}