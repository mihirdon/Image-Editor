import controller.GUIController;
import java.util.Scanner;
import model.application.ILayeredApplication;
import view.IGUIView;

/**
 * Mock for {@code GUIController} used to verify that controller receives the correct readable for
 * each action event.
 */
public class MockGUIController extends GUIController {

  /**
   * Creates a {@code MockGUIController} with the given application and {@code IGUIView}.
   *
   * @param model the model used to query for information (may not be used)
   * @param view  the GUI-supporting view
   * @throws IllegalArgumentException if the model or view is null
   */
  public MockGUIController(ILayeredApplication model, IGUIView view) throws
      IllegalArgumentException {
    super(model, view);
  }

  /**
   * Returns a string representing the result of each action event.
   * Either the contents of the given readable or a message
   * saying a method would have been invoked.
   *
   * @param action a String representing the given action event
   * @return the string representing the result of the given action
   */
  public String getActionPerformed(String action) {
    switch (action) {
      case "Create Options":
        return "Switch create format";
      case "Create Button":
        return new Scanner(guiView.createLayer()).nextLine();
      case "Apply Button":
        return new Scanner(guiView.applyFilter()).nextLine();
      case "Save Button":
        return "Save file";
      case "Layer Options":
        return new Scanner(guiView.getCurrentLayer()).nextLine();
      case "Open Button":
        return "Open image";
      case "Load Button":
        return new Scanner(guiView.loadImage()).nextLine();
      case "Script Button":
        return "Execute script";
      default:
        throw new IllegalArgumentException("Invalid action event");
    }
  }
}