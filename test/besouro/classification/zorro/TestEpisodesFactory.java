package besouro.classification.zorro;
import java.util.Date;

import besouro.model.CompilationAction;
import besouro.model.EditAction;
import besouro.model.RefactoringAction;
import besouro.model.UnitTestAction;
import besouro.model.UnitTestCaseAction;



public class TestEpisodesFactory {
	
	  private static String productionFile;  
	  private static String testFile;
	  
	  static {
		  productionFile = "C:\\cvs\\work\\example\\Triangle.java";  
		  testFile = "C:\\cvs\\work\\example\\TestTriangle.java";
	  }
	  
	  public static void addTDDType1Facts(ZorroEpisodeClassification engine, Date clock) throws Exception {
	    // Add test method
	    RefactoringAction unaryAction = new RefactoringAction(clock, testFile);
	    unaryAction.setOperator("ADD");
	    unaryAction.setSubjectType("METHOD");
	    unaryAction.setSubjectName("void testEquilateral()");
//	    unaryAction.assertJessFact(1, engine);
	    engine.assertJessFact(1, unaryAction);
	    
	    // Edit on test
	    EditAction editAction = new EditAction(clock, testFile);
	    
//		ignorng duration
//	    editAction.setDuration(123);
	    
	    editAction.setIsTestEdit(true);
//	    editAction.assertJessFact(2, engine);
	    engine.assertJessFact(2, editAction);

	    // Compile error on test
	    CompilationAction compilationAction = new CompilationAction(clock, testFile);
	    compilationAction.setErrorMessage("Unknown data type");
//	    compilationAction.assertJessFact(3, engine);
	    engine.assertJessFact(3, compilationAction);

	    // Work on production code
	    editAction = new EditAction(clock, productionFile);
	    
//		ignorng duration
//	    editAction.setDuration(200);
	    
	    editAction.setIsTestEdit(false);
	    editAction.setFileSizeIncrease(10);
//	    editAction.assertJessFact(4, engine);
	    engine.assertJessFact(4, editAction);
	    

	    // Unit test failue
	    UnitTestCaseAction unitTestAction = new UnitTestCaseAction(clock, testFile); 
	    unitTestAction.setFailureMessage("Failed to import");
//	    unitTestAction.assertJessFact(5, engine);
	    engine.assertJessFact(5, unitTestAction);
	 
	    // Edit on prodction code
	    editAction = new EditAction(clock, productionFile);
	    
//		ignorng duration
//	    editAction.setDuration(199);
	    
	    editAction.setIsTestEdit(false);
	    editAction.setFileSizeIncrease(30);
//	    editAction.assertJessFact(6, engine);
	    engine.assertJessFact(6, editAction);

	    // Unit test pass
	    unitTestAction = new UnitTestCaseAction(clock, testFile); 
	    engine.assertJessFact(7, unitTestAction);
//	    unitTestAction.assertJessFact(7, engine);
	  }
	  
	  public static void addTDDType2Facts(ZorroEpisodeClassification engine, Date clock) throws Exception {
	    // Add test method
	    RefactoringAction unaryAction = new RefactoringAction(clock, testFile);
	    unaryAction.setOperator("ADD");
	    unaryAction.setSubjectType("METHOD");
	    unaryAction.setSubjectName("void testEquilateral()");
//	    unaryAction.assertJessFact(1, engine);
	    engine.assertJessFact(1, unaryAction);
	    
	    // Edit on test
	    EditAction editAction = new EditAction(clock, testFile);
	    
//		ignorng duration
//	    editAction.setDuration(123);
	    
	    editAction.setIsTestEdit(true);
	    editAction.setFileSizeIncrease(11);
//	    editAction.assertJessFact(2, engine);
	    engine.assertJessFact(2, editAction);

	    // Compile error on test
	    CompilationAction compilationAction = new CompilationAction(clock, testFile);
	    compilationAction.setErrorMessage("Unknown data type");
//	    compilationAction.assertJessFact(3, engine);
	    engine.assertJessFact(3, compilationAction);

	    // Work on production code
	    editAction = new EditAction(clock, productionFile);
	    
//		ignorng duration
//	    editAction.setDuration(200);
	    
	    editAction.setIsTestEdit(false);
	    editAction.setFileSizeIncrease(26);
	    engine.assertJessFact(4, editAction);
//	    editAction.assertJessFact(4, engine);

	    // Unit test pass
	    UnitTestAction unitTestAction = new UnitTestCaseAction(clock, testFile); 
	    engine.assertJessFact(5, unitTestAction);
//	    unitTestAction.assertJessFact(5, engine);
	  }
	  
	  public static void addTDDType3Facts(ZorroEpisodeClassification engine, Date clock) throws Exception {
	    // Add test method
	    RefactoringAction unaryAction = new RefactoringAction(clock, testFile);
	    unaryAction.setOperator("ADD");
	    unaryAction.setSubjectType("METHOD");
	    unaryAction.setSubjectName("void testEquilateral()");
	    engine.assertJessFact(1, unaryAction);
	    
	    // Edit on test
	    EditAction editAction = new EditAction(clock, testFile);
	    
//		ignorng duration
//	    editAction.setDuration(123);
	    
	    editAction.setIsTestEdit(true);
	    editAction.setFileSizeIncrease(10);
	    engine.assertJessFact(2, editAction);

	    // Work on production code
	    editAction = new EditAction(clock, productionFile);
	    
//		ignorng duration
//	    editAction.setDuration(200);
	    
	    editAction.setIsTestEdit(true);
	    engine.assertJessFact(4, editAction);

	    // Unit test failue
	    UnitTestCaseAction unitTestAction = new UnitTestCaseAction(clock, testFile); 
	    unitTestAction.setFailureMessage("Failed to import");
	    engine.assertJessFact(5, unitTestAction);
	 
	    // Edit on prodction code
	    editAction = new EditAction(clock, productionFile);
	    
//		ignorng duration
//	    editAction.setDuration(123);
	    
	    editAction.setIsTestEdit(false);
	    editAction.setFileSizeIncrease(90);
	    engine.assertJessFact(6, editAction);

	    // Unit test pass
	    unitTestAction = new UnitTestCaseAction(clock, testFile); 
	    engine.assertJessFact(7, unitTestAction);
	  }
	  
	  public static void addFactsForTestCodeRefactoringWithTestFailureDuringChange(ZorroEpisodeClassification engine, Date clock) throws Exception {

		// Edit on test code    
	    EditAction action = new EditAction(clock, testFile);
	    action.setIsTestEdit(true);
	    action.setFileSizeIncrease(27);
	    action.setMethodIncrease(2);
	    engine.assertJessFact(1, action);
	    
	    //TODO [rule] we dont need these two next acitons for the test to pass
	    // Edit on test code
	    action = new EditAction(clock, testFile);
	    action.setIsTestEdit(true);
	    action.setFileSizeIncrease(50);
	    action.setMethodIncrease(2);
	    engine.assertJessFact(2, action);

	    // Unit test fail on test code
	    UnitTestCaseAction unitTestAction = new UnitTestCaseAction(clock, testFile); 
	    unitTestAction.setFailureMessage("Cannot instantiate it");
	    engine.assertJessFact(3, unitTestAction);

	    // Edit on test code
	    action = new EditAction(clock, testFile);
	    action.setIsTestEdit(true);
	    action.setFileSizeIncrease(13);
	    engine.assertJessFact(4, action);

	    // Unit test pass
	    unitTestAction = new UnitTestCaseAction(clock, testFile);
	    engine.assertJessFact(5, unitTestAction);
	    
	  }
	  
	  public static void addFactsForTestSimpleCodeRefactoring(ZorroEpisodeClassification engine, Date clock) throws Exception {
		  
		  // Edit on test code    
		  EditAction action = new EditAction(clock, testFile);
		  action.setIsTestEdit(true);
		  action.setFileSizeIncrease(27);
		  action.setMethodIncrease(2);
		  engine.assertJessFact(1, action);
		  
		  // Unit test pass
		  UnitTestCaseAction unitTestAction = new UnitTestCaseAction(clock, testFile);
		  engine.assertJessFact(5, unitTestAction);
		  
	  }
	  
	  public static void addProductionCodeRefactoFacts(ZorroEpisodeClassification engine, Date clock) throws Exception {
		  
	    // Edit on production code    
	    EditAction action = new EditAction(clock, productionFile);
	    action.setIsTestEdit(false);
	    action.setMethodIncrease(-1);
	    action.setStatementIncrease(10);
	    action.setFileSize(122);
	    engine.assertJessFact(1, action);
	    
	    // Unit test fail on test code
	    UnitTestCaseAction unitTestAction = new UnitTestCaseAction(clock, testFile); 
	    unitTestAction.setFailureMessage("Cannot instantiate it");
	    engine.assertJessFact(2, unitTestAction);

	    // Edit on production code
	    action = new EditAction(clock, testFile);
	    action.setIsTestEdit(false);
	    action.setMethodIncrease(0);
	    action.setStatementIncrease(0);
	    action.setFileSize(89);
	    engine.assertJessFact(3, action);
	    
	    // Unit test pass
	    unitTestAction = new UnitTestCaseAction(clock, testFile);
	    engine.assertJessFact(4, unitTestAction);
	    
	  }
}
