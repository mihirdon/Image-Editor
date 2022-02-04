package view;

/**
 * Interface representing a view for an image-processing application. Provides the ability to render
 * messages and display any commands that have been performed.
 */
public interface IApplicationView {

  /**
   * Represents this view as a string.
   */
  @Override
  String toString();

  /**
   * Outputs the given string to this view's appendable.
   */
  void renderMessage(String s);

  /**
   * Provides a message detailing all the possible commands that can be used on the application.
   */
  void visualizeCommands();
}
