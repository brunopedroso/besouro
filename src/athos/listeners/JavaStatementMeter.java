package athos.listeners;


import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * Implements a meter to measure several aspects such as number of methods, statements, test 
 * methods, assertion statements if applicable. Number of test methods, assertion statements is
 * reported to assist Test-Driven Development study.
 * 
 * @author Hongbing Kou
 */
public class JavaStatementMeter extends ASTVisitor {
  /** Name of this compilation unit. */
  private String name; 
  
  private int numOfMethods = 0;
  private int numOfStatements = 0;
  
  private int numOfTestMethods = 0;
  private int numOfTestAssertions = 0;
  
  public void reset() {
	  name = null; 
	  numOfMethods = 0;
	  numOfStatements = 0;
	  numOfTestMethods = 0;
	  numOfTestAssertions = 0;
	  
  }
  
  public boolean visit(TypeDeclaration td) {
    if (td.getName() != null) {
      this.name = td.getName().getIdentifier();
    }
    
    return true;
  }

  
  private boolean isJUnit4Test(MethodDeclaration md) {
    List modifiers = md.modifiers();
    for (Iterator i = modifiers.iterator(); i.hasNext();) {
      IExtendedModifier modifer = (IExtendedModifier) i.next();
      if (modifer.isAnnotation()) {
        Annotation annotation = (Annotation) modifer;
        if ("Test".equals(annotation.getTypeName().getFullyQualifiedName())) {
          return true;
        }
      }
    }
    
    return false;
  }
  
  
  public boolean visit(MethodDeclaration md) {
    if (md.getName() != null) {
      this.numOfMethods++;
      
      if (md.getName().getIdentifier().startsWith("test")) {
        this.numOfTestMethods++;
      }
      else if (isJUnit4Test(md)) {
        this.numOfTestMethods++;
      }
      
      // Check test method body to look for assertion statement. 
      Block methodBody = md.getBody();
      if (methodBody != null && methodBody.statements() != null) {
        List stmts = methodBody.statements();
        this.numOfStatements += stmts.size();
        // Looks through all statements in this method body.
        for (Iterator i = stmts.iterator(); i.hasNext();) {
          Statement stmt = (Statement) i.next();  //NOPMD
          // MethodInvocation is one kind of expression statement.
          if (stmt instanceof ExpressionStatement) {
            ExpressionStatement estmt = (ExpressionStatement) stmt;
            checkAssertions(estmt);
          }
        }
      }
    }
        
    //No need to visit child nodes anymore.
    return false;
  }

  private void checkAssertions(ExpressionStatement estmt) {
    if (estmt.getExpression() instanceof MethodInvocation) {
      MethodInvocation mi = (MethodInvocation) estmt.getExpression();
      // Increment number of test assertions.
      if (mi.getName() != null && mi.getName().getIdentifier().startsWith("assert")) {
        this.numOfTestAssertions++;
      }
    }
  }

  public int getNumOfMethods() {
    return this.numOfMethods;
  }
  

  public int getNumOfStatements() {
    return this.numOfStatements;
  }
  
  public int getNumOfTestMethods() {
    return this.numOfTestMethods;
  }
  
  public int getNumOfTestAssertions() {
    return this.numOfTestAssertions;
  }
  
  public boolean hasTest() {
    return this.numOfTestMethods > 0 || this.numOfTestAssertions > 0;   
  }
  
  public String toString() {
    StringBuffer buf = new StringBuffer(200);
    buf.append("*****  ").append(this.name).append("   *****\nMethods     : ")
    .append(this.numOfMethods).append("\nStatements  : ").append(this.numOfStatements);
        
    // Appends test info if there is any.
    if (this.hasTest()) {
       buf.append("\nTests       : ").append(this.numOfTestMethods);
       buf.append("\nAssertions  : ").append(this.numOfTestAssertions);
    }
    
    return buf.toString();
  }
  
  
	public void measureJavaFile(IFile file) {
		// Compute number of tests and assertions to this file.
		ICompilationUnit cu = (ICompilationUnit) JavaCore.create(file);
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(cu);
		parser.setResolveBindings(true);

		ASTNode root = parser.createAST(null);
		root.accept(this);
	}
}
