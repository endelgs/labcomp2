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
     pw.println("{");
     pw.currentIndent = 1;
     // Imprimindo a lista de variaveis
     if(instanceVariableList != null){
        instanceVariableList.genK(pw);
     }
     // Imprimindo a lista de metodos publicos
     if(publicMethodList != null){
        publicMethodList.genK(pw);
     }
     // Imprimindo a lista de metodos privados
     if(privateMethodList != null){
      privateMethodList.genK(pw);
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
    return this.getMethod(name,false,true,true);
  }
  public MethodDec checkOverrideMethod(String name){
    MethodDec m = null;
    if(superclass == null)
      m = getMethod(name);
    if(superclass != null)
      return superclass.checkOverrideMethod(name);
    
    
    return m;
  }
  public MethodDec getMethod(String name, boolean includePrivateMethods, boolean includeSuperclassMethods, boolean includeStatic){
    MethodDec methodDec;
    for(int i = 0; i < publicMethodList.getSize();i++){
      methodDec = publicMethodList.get(i);
      if(!includeStatic && methodDec.isIsStatic()) 
        continue;
      if(methodDec.getName().equals(name)){
        return methodDec;
      }
    }
    if(includePrivateMethods){
      for(int i = 0; i < privateMethodList.getSize();i++){
       methodDec = privateMethodList.get(i);
        if(methodDec.getName().equals(name)){
          return methodDec;
        }
      }
    }
    if(includeSuperclassMethods && superclass != null){
      return superclass.getMethod(name);
    }
    return null;
  }
  public InstanceVariable getVariable(String name){
    InstanceVariable variable;
    for(int i = 0; i < instanceVariableList.getSize(); i++){
       variable = instanceVariableList.get(i);
      if(variable.getName().equals(name)){
        return variable;
      }
    }
    return null;
  }
  public boolean isChildOf(String name){
    if(this.name.equals(name))
      return true;
    if(superclass == null)
      return false;
    return superclass.isChildOf(name);
  }
  private String name;
  private ClassDec superclass;
  private InstanceVariableList instanceVariableList;
  private MethodList publicMethodList, privateMethodList;
 // m�todos p�blicos get e set para obter e iniciar as vari�veis acima,
 // entre outros m�todos

  
}
