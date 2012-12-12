package ast;

public class ClassDec extends Type {
   public ClassDec( String name ) {
      super(name);
      this.name = name;
      instanceVariableList = new InstanceVariableList();
      publicMethodList = new MethodList();
      privateMethodList = new MethodList();
   }
   public void genK(PW pw){
     pw.print("class "+name);
     if(superclass != null)
       pw.print(" extends "+superclass.getName());
     
     // Imprimindo a lista de variaveis
     if(instanceVariableList != null){
      while(instanceVariableList.elements().hasNext()){
        InstanceVariable i = instanceVariableList.elements().next();
        i.genK(pw);
      }
     }
     // Imprimindo a lista de metodos publicos
     if(publicMethodList != null){
      while(publicMethodList.elements().hasNext()){
        MethodDec i = publicMethodList.elements().next();
        i.genK(pw);
      }
     }
     // Imprimindo a lista de metodos privados
     if(privateMethodList != null){
      while(privateMethodList.elements().hasNext()){
        MethodDec i = privateMethodList.elements().next();
        i.genK(pw);
      }
     }
     pw.print("}");
   }
   public String getCname() {
      return getName();
   }
   public ClassDec getSuperclass() {
    return superclass;
  }

  public void setSuperclass(ClassDec superclass) {
    this.superclass = superclass;
  }

  public InstanceVariableList getInstanceVariableList() {
    return instanceVariableList;
  }
  public void setInstanceVariableList(InstanceVariableList instanceVariableList) {
    this.instanceVariableList = instanceVariableList;
  }
  public MethodList getPublicMethodList() {
    return publicMethodList;
  }

  public void setPublicMethodList(MethodList publicMethodList) {
    this.publicMethodList = publicMethodList;
  }

  public MethodList getPrivateMethodList() {
    return privateMethodList;
  }

  public void setPrivateMethodList(MethodList privateMethodList) {
    this.privateMethodList = privateMethodList;
  }
  public MethodDec getMethod(String name){
    MethodDec methodDec;
    while(publicMethodList.elements().hasNext()){
       methodDec = publicMethodList.elements().next();
      if(methodDec.getName() == name){
        return methodDec;
      }
    }
    return null;
  }
  public InstanceVariable getVariable(String name){
    InstanceVariable variable;
    while(instanceVariableList.elements().hasNext()){
       variable = instanceVariableList.elements().next();
      if(variable.getName() == name){
        return variable;
      }
    }
    return null;
  }
  private String name;
  private ClassDec superclass;
  private InstanceVariableList instanceVariableList;
  private MethodList publicMethodList, privateMethodList;
 // m�todos p�blicos get e set para obter e iniciar as vari�veis acima,
 // entre outros m�todos

  
}
