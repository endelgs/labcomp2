/**
 * 379930 Endel Guimaraes Silva
 * 400564 Felipe Augusto Rosa
 */
package ast;


public class Parameter extends Variable {

    public Parameter( String name, Type type ) {
        super(name, type);
    }
    public void genC(PW pw){
      pw.print(getType().getCName()+" "+super.getCName());
    }

}