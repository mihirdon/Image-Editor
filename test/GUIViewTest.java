import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import controller.GUIController;
import java.awt.image.BufferedImage;
import java.io.StringReader;
import java.util.Collections;
import java.util.Scanner;
import javax.swing.Icon;
import javax.swing.ImageIcon;
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
 * Test class for {@code GUIView} verifying public functionality works as expected.
 */
public class GUIViewTest extends GUIView {

  GUIController controller;

  // Test fixture used for testing purposes.
  // Constructs a GUIView.
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

    controller = new GUIController(app, this);
  }

  // Tests setActionListener() method correctly adds the given listener to each component.
  @Test
  public void testSetActionListener() {
    setActionListener(controller);

    assertEquals(controller, createOptions.getActionListeners()[0]);
    assertEquals(controller, createLayerButton.getActionListeners()[0]);
    assertEquals(controller, applyOperationButton.getActionListeners()[0]);
    assertEquals(controller, saveButton.getActionListeners()[0]);
    assertEquals(controller, layerOptions.getActionListeners()[0]);
    assertEquals(controller, openButton.getActionListeners()[0]);
    assertEquals(controller, loadButton.getActionListeners()[0]);
    assertEquals(controller, scriptButton.getActionListeners()[0]);
  }

  // Tests renderMessage() method correctly adds the given string into the correct text area.
  @Test
  public void testRenderMessage() {
    assertTrue(display.getText().isEmpty());

    renderMessage("Not empty");

    assertEquals("Not empty\n", display.getText());
  }

  // Tests displayImage() method correctly sets the
  // image label component to the given buffered image.
  @Test
  public void testDisplayImage() {
    BufferedImage bf = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
    Icon expected = new ImageIcon(bf);

    assertNull(image.getIcon());

    displayImage(bf);

    assertEquals(expected.getIconWidth(), image.getIcon().getIconWidth());
    assertEquals(expected.getIconHeight(), image.getIcon().getIconHeight());
  }

  // Tests createLayer() method correctly provides a readable with the "create" command.
  @Test
  public void testCreateLayer() {
    assertEquals("Regular Layer", createOptions.getSelectedItem());
    layerCreationName.setText("layerName");
    assertEquals("layerName", layerCreationName.getText());

    String expected = new Scanner(new StringReader("create layer layerName")).nextLine();

    assertEquals(expected, new Scanner(createLayer()).nextLine());
    assertTrue(layerCreationName.getText().isEmpty());

    createOptions.setSelectedItem("Checkerboard Layer");
    tileSize.setText("1");
    numTiles.setText("2");
    assertEquals("1", tileSize.getText());
    assertEquals("2", numTiles.getText());

    expected = new Scanner(new StringReader("checkerboard 1 2")).nextLine();

    assertEquals(expected, new Scanner(createLayer()).nextLine());
    assertTrue(tileSize.getText().isEmpty());
    assertTrue(numTiles.getText().isEmpty());
  }

  // Tests applyFilter() method correctly provides a readable with the "blur" command.
  @Test
  public void testApplyFilter() {
    String expected = new Scanner(new StringReader("blur")).nextLine();
    assertEquals("Blur", operationOptions.getSelectedItem());
    assertEquals(expected, new Scanner(applyFilter()).nextLine());

    expected = new Scanner(new StringReader("sharpen")).nextLine();
    operationOptions.setSelectedItem("Sharpen");
    assertEquals("Sharpen", operationOptions.getSelectedItem());
    assertEquals(expected, new Scanner(applyFilter()).nextLine());

    expected = new Scanner(new StringReader("monochrome")).nextLine();
    operationOptions.setSelectedItem("Monochrome");
    assertEquals("Monochrome", operationOptions.getSelectedItem());
    assertEquals(expected, new Scanner(applyFilter()).nextLine());

    expected = new Scanner(new StringReader("sepia")).nextLine();
    operationOptions.setSelectedItem("Sepia");
    assertEquals("Sepia", operationOptions.getSelectedItem());
    assertEquals(expected, new Scanner(applyFilter()).nextLine());
  }

  // Tests getCurrentLayer() method correctly provides a readable with the "current" command.
  @Test
  public void testGetCurrentLayer() {
    layerOptions.addItem("layerName");
    layerOptions.setSelectedItem("layerName");
    assertEquals("layerName", layerOptions.getSelectedItem());

    String expected = new Scanner(new StringReader("current layerName")).nextLine();
    assertEquals(expected, new Scanner(getCurrentLayer()).nextLine());
  }

  // Tests loadImage() method correctly provides a readable with the "load" command.
  @Test
  public void testLoadImage() {
    openedFilePath.setText("res/hedgehog.ppm");

    String expected = new Scanner(new StringReader("load res/hedgehog.ppm")).nextLine();
    assertEquals(expected, new Scanner(loadImage()).nextLine());
  }
}