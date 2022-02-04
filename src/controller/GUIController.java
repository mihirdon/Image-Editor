package controller;

import controller.commands.ExtraCreditFactory;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import model.application.ILayeredApplication;
import model.picture.IPicture;
import model.pixel.IPixel;
import model.application.GUIModel;
import model.application.IModelObserver;
import view.IGUIView;

/**
 * A {@code GUIController} acts as a controller between the application and a {@code IGUIView}.
 */
public class GUIController extends Controller implements ActionListener {

  protected IModelObserver<IPicture<IPixel>> model;
  protected IGUIView guiView;

  /**
   * Creates a {@code GUIController} with the given application and {@code IGUIView}.
   *
   * @param model the model used to query for information (may not be used)
   * @param view  the GUI-supporting view
   * @throws IllegalArgumentException if the model or view is null
   */
  public GUIController(ILayeredApplication model, IGUIView view) throws
      IllegalArgumentException {
    super(model, view);
    this.model = new GUIModel(model);
    this.guiView = view;
    this.guiView.setActionListener(this);
  }

  /**
   * Updates the {@code IGUIView} to display the application's top-most visible layer's image.
   */
  protected void displayTopMostVisibleImage() {
    guiView.displayImage(model.createBufferedImage(model.getTopMostVisible()));
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    try {
      switch (e.getActionCommand()) {
        case "Create Options":
          guiView.switchCurrentCreate();
          break;
        case "Create Button":
          startApp(guiView.createLayer(), new ExtraCreditFactory());
          displayTopMostVisibleImage();
          break;
        case "Apply Button":
          startApp(guiView.applyFilter(), new ExtraCreditFactory());
          displayTopMostVisibleImage();
          break;
        case "Save Button":
          startApp(guiView.openSaveFile(), new ExtraCreditFactory());
          break;
        case "Layer Options":
          startApp(guiView.getCurrentLayer(), new ExtraCreditFactory());
          displayTopMostVisibleImage();
          break;
        case "Open Button":
          guiView.openFiles();
          break;
        case "Load Button":
          startApp(guiView.loadImage(), new ExtraCreditFactory());
          displayTopMostVisibleImage();
          break;
        case "Script Button":
          try {
            startApp(guiView.getScript(), new ExtraCreditFactory());
          } catch (FileNotFoundException exception) {
            exception.printStackTrace();
          }
          break;
        case "Extra Credit Options":
          guiView.switchCurrentExtraCredit();
          break;
        case "Apply Function":
          startApp(guiView.applyExtraCredit(), new ExtraCreditFactory());
          displayTopMostVisibleImage();
          break;
        default:
          throw new IllegalArgumentException("Invalid action event");
      }
    } catch (IllegalArgumentException exception) {
      guiView.renderMessage(exception.getMessage());
    }
  }
}