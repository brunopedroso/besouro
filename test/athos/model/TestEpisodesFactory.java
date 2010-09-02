package athos.model;
import java.io.File;

import jess.Rete;
import athos.model.refactor.RefactorOperator;
import athos.model.refactor.RefactorSubjectType;
import athos.model.refactor.UnaryRefactorAction;


public class TestEpisodesFactory {
	
	  private static File productionFile = new File("C:\\cvs\\work\\example\\Triangle.java");  
	  private static File testFile = new File("C:\\cvs\\work\\example\\TestTriangle.java");
	  
	  public static void addTDDType1Facts(Rete engine, Clock clock) throws Exception {
	    // Add test method
	    UnaryRefactorAction unaryAction = new UnaryRefactorAction(clock, testFile);
	    unaryAction.setOperator(RefactorOperator.ADD);
	    unaryAction.setSubjectType(RefactorSubjectType.METHOD);
	    unaryAction.setSubjectName("void testEquilateral()");
	    unaryAction.assertJessFact(1, engine);
	    
	    // Edit on test
	    EditAction editAction = new EditAction(clock, testFile);
	    
//		ignorng duration
//	    editAction.setDuration(123);
	    
	    editAction.setIsTestEdit(true);
	    editAction.assertJessFact(2, engine);

	    // Compile error on test
	    CompilationAction compilationAction = new CompilationAction(clock, testFile);
	    compilationAction.setErrorMessage("Unknown data type");
	    compilationAction.assertJessFact(3, engine);

	    // Work on production code
	    editAction = new EditAction(clock, productionFile);
	    
//		ignorng duration
//	    editAction.setDuration(200);
	    
	    editAction.setIsTestEdit(false);
	    editAction.setFileSizeIncrease(10);
	    editAction.assertJessFact(4, engine);

	    // Unit test failue
	    UnitTestAction unitTestAction = new UnitTestAction(clock, testFile); 
	    unitTestAction.setFailureMessage("Failed to import");
	    unitTestAction.assertJessFact(5, engine);
	 
	    // Edit on prodction code
	    editAction = new EditAction(clock, productionFile);
	    
//		ignorng duration
//	    editAction.setDuration(199);
	    
	    editAction.setIsTestEdit(false);
	    editAction.setFileSizeIncrease(30);
	    editAction.assertJessFact(6, engine);

	    // Unit test pass
	    unitTestAction = new UnitTestAction(clock, testFile); 
	    unitTestAction.assertJessFact(7, engine);
	  }
	  
	  public static void addTDDType2Facts(Rete engine, Clock clock) throws Exception {
	    // Add test method
	    UnaryRefactorAction unaryAction = new UnaryRefactorAction(clock, testFile);
	    unaryAction.setOperator(RefactorOperator.ADD);
	    unaryAction.setSubjectType(RefactorSubjectType.METHOD);
	    unaryAction.setSubjectName("void testEquilateral()");
	    unaryAction.assertJessFact(1, engine);
	    
	    // Edit on test
	    EditAction editAction = new EditAction(clock, testFile);
	    
//		ignorng duration
//	    editAction.setDuration(123);
	    
	    editAction.setIsTestEdit(true);
	    editAction.setFileSizeIncrease(11);
	    editAction.assertJessFact(2, engine);

	    // Compile error on test
	    CompilationAction compilationAction = new CompilationAction(clock, testFile);
	    compilationAction.setErrorMessage("Unknown data type");
	    compilationAction.assertJessFact(3, engine);

	    // Work on production code
	    editAction = new EditAction(clock, productionFile);
	    
//		ignorng duration
//	    editAction.setDuration(200);
	    
	    editAction.setIsTestEdit(false);
	    editAction.setFileSizeIncrease(26);
	    editAction.assertJessFact(4, engine);

	    // Unit test pass
	    UnitTestAction unitTestAction = new UnitTestAction(clock, testFile); 
	    unitTestAction.assertJessFact(5, engine);
	  }
	  
	  public static void addTDDType3Facts(Rete engine, Clock clock) throws Exception {
	    // Add test method
	    UnaryRefactorAction unaryAction = new UnaryRefactorAction(clock, testFile);
	    unaryAction.setOperator(RefactorOperator.ADD);
	    unaryAction.setSubjectType(RefactorSubjectType.METHOD);
	    unaryAction.setSubjectName("void testEquilateral()");
	    unaryAction.assertJessFact(1, engine);
	    
	    // Edit on test
	    EditAction editAction = new EditAction(clock, testFile);
	    
//		ignorng duration
//	    editAction.setDuration(123);
	    
	    editAction.setIsTestEdit(true);
	    editAction.setFileSizeIncrease(10);
	    editAction.assertJessFact(2, engine);

	    // Work on production code
	    editAction = new EditAction(clock, productionFile);
	    
//		ignorng duration
//	    editAction.setDuration(200);
	    
	    editAction.setIsTestEdit(true);
	    editAction.assertJessFact(4, engine);

	    // Unit test failue
	    UnitTestAction unitTestAction = new UnitTestAction(clock, testFile); 
	    unitTestAction.setFailureMessage("Failed to import");
	    unitTestAction.assertJessFact(5, engine);
	 
	    // Edit on prodction code
	    editAction = new EditAction(clock, productionFile);
	    
//		ignorng duration
//	    editAction.setDuration(123);
	    
	    editAction.setIsTestEdit(false);
	    editAction.setFileSizeIncrease(90);
	    editAction.assertJessFact(6, engine);

	    // Unit test pass
	    unitTestAction = new UnitTestAction(clock, testFile); 
	    unitTestAction.assertJessFact(7, engine);
	  }
	  
	  public static void addTestCodeRefactoFacts(Rete engine, Clock clock) throws Exception {

		// Edit on test code    
	    EditAction action = new EditAction(clock, testFile);
	    action.setIsTestEdit(true);
	    action.setFileSizeIncrease(27);
	    action.setMethodIncrease(2);
	    action.assertJessFact(1, engine);
	    
	    // Edit on test code
	    action = new EditAction(clock, testFile);
	    action.setIsTestEdit(true);
	    action.setFileSizeIncrease(50);
	    action.setMethodIncrease(2);
	    action.assertJessFact(2, engine);

	    // Unit test fail on test code
	    UnitTestAction unitTestAction = new UnitTestAction(clock, testFile); 
	    unitTestAction.setFailureMessage("Cannot instantiate it");
	    unitTestAction.assertJessFact(3, engine);

	    // Edit on test code
//	    action = new EditAction(clock, testFile);
//	    action.setIsTestEdit(true);
//	    action.setFileSizeIncrease(13);
//	    action.assertJessFact(2, engine); //TODO [rule] index 2 again ??!!

	    // Unit test pass
	    unitTestAction = new UnitTestAction(clock, testFile);
	    unitTestAction.assertJessFact(4, engine);
	    
	  }
	  
	  public static void addProductionCodeRefactoFacts(Rete engine, Clock clock) throws Exception {
	    // Edit on production code    
	    EditAction action = new EditAction(clock, productionFile);
	    
//		ignorng duration
//	    action.setDuration(300);
	    
	    action.setIsTestEdit(false);
	    action.setMethodIncrease(-1);
	    action.setStatementIncrease(10);
	    action.setFileSize(122);
	    action.assertJessFact(1, engine);
	    
	    // Unit test fail on test code
	    UnitTestAction unitTestAction = new UnitTestAction(clock, testFile); 
	    unitTestAction.setFailureMessage("Cannot instantiate it");
	    unitTestAction.assertJessFact(2, engine);

	    // Edit on production code
	    action = new EditAction(clock, testFile);
	    
//		ignorng duration
//	    action.setDuration(200);
	    
	    action.setIsTestEdit(false);
	    action.setMethodIncrease(0);
	    action.setStatementIncrease(0);
	    action.setFileSize(89);
	    action.assertJessFact(3, engine);
	    
	    // Unit test pass
	    unitTestAction = new UnitTestAction(clock, testFile);
	    unitTestAction.assertJessFact(4, engine);
	  }
}
