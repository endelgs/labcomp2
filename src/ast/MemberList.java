/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ast;

import java.util.ArrayList;

/**
 *
 * @author endel
 */
public abstract class MemberList {
  private ArrayList<Member> memberList;

  public MemberList(ArrayList<Member> memberList) {
    this.memberList = memberList;
  }
  public MemberList(){
    
  }
  
  public void genK(PW pw){
    
  }
}
