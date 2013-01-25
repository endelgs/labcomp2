/**
 * 379930 Endel Guimaraes Silva
 * 400564 Felipe Augusto Rosa
 */
package ast;

public class BooleanType extends Type {

  public BooleanType() {
    super("bool");
  }

  @Override
  public String getCName() {
    return "int";
  }
}
