package besouro.integration;

import static org.mockito.Mockito.when;
import junit.framework.Assert;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.junit.model.ITestElement.Result;
import org.junit.Test;

import besouro.listeners.mock.JUnitEventFactory;
import besouro.listeners.mock.JavaStructureChangeEventFactory;
import besouro.listeners.mock.ResourceChangeEventFactory;


public class RefactoringRecognition extends IntegrationTestBaseClass {

	@Test 
	public void refactoringCategory1A() throws Exception {
		
		addRefactoring1A_Actions();
		
		Assert.assertEquals(1, stream.getEpisodes().length);
		Assert.assertEquals("refactoring", stream.getEpisodes()[0].getCategory());
		Assert.assertEquals("1A", stream.getEpisodes()[0].getSubtype());
	}

	
	@Test 
	public void refactoringCategory1A_2() throws Exception {
		
		addRefactoringCategory1a_2_events();
		
		
		Assert.assertEquals(1, stream.getEpisodes().length);
		// two refactorings - one on each edit (because they precede test-pass)
		Assert.assertEquals("refactoring", stream.getEpisodes()[0].getCategory());
		Assert.assertEquals("1A", stream.getEpisodes()[0].getSubtype());
		
//		Assert.assertEquals("refactoring", stream.getTDDMeasure().getRecognizedEpisodes().get(1).getCategory());
//		Assert.assertEquals("1A", stream.getTDDMeasure().getRecognizedEpisodes().get(1).getSubtype());
	}


	
	@Test 
	public void refactoringCategory1B() throws Exception {
		
		addRefactoringCategory1B_events();
		
		Assert.assertEquals(1, stream.getEpisodes().length);
		Assert.assertEquals("refactoring", stream.getEpisodes()[0].getCategory());
		Assert.assertEquals("1B", stream.getEpisodes()[0].getSubtype());
		
		//TODO [rule]  redundancy: refactoring and regression
		//			  does it influence the metric?
//		Assert.assertEquals("refactoring", stream.getTDDMeasure().getRecognizedEpisodes().get(1).getCategory());
//		Assert.assertEquals("3", stream.getTDDMeasure().getRecognizedEpisodes().get(1).getSubtype());
//		
//		Assert.assertEquals("refactoring", stream.getTDDMeasure().getRecognizedEpisodes().get(2).getCategory());
//		Assert.assertEquals("2B", stream.getTDDMeasure().getRecognizedEpisodes().get(2).getSubtype());
//		
//		Assert.assertEquals("regression", stream.getTDDMeasure().getRecognizedEpisodes().get(3).getCategory());
//		Assert.assertEquals("1", stream.getTDDMeasure().getRecognizedEpisodes().get(3).getSubtype());
	}
	
	
	
	@Test 
	public void refactoringCategory2A() throws Exception {
		
		 addRefactoringCategory2A_events();
		
		Assert.assertEquals(1, stream.getEpisodes().length);
		Assert.assertEquals("refactoring", stream.getEpisodes()[0].getCategory());
		Assert.assertEquals("2A", stream.getEpisodes()[0].getSubtype());
	    
	}


	
	@Test 
	public void refactoringCategory2B() throws Exception {
		
		addRefactoringCategory2B_events();
		
		Assert.assertEquals(1, stream.getEpisodes().length);
		Assert.assertEquals("refactoring", stream.getEpisodes()[0].getCategory());
		Assert.assertEquals("2B", stream.getEpisodes()[0].getSubtype());

		//TODO [rule]  redundancy: refactoring and regression
		//			  does it influence the metric?
//		Assert.assertEquals("regression", stream.getTDDMeasure().getRecognizedEpisodes().get(1).getCategory());
//		Assert.assertEquals("2", stream.getTDDMeasure().getRecognizedEpisodes().get(1).getSubtype());
//		
//		Assert.assertEquals("refactoring", stream.getTDDMeasure().getRecognizedEpisodes().get(2).getCategory());
//		Assert.assertEquals("2B", stream.getTDDMeasure().getRecognizedEpisodes().get(2).getSubtype());
		
	}



	@Test 
	public void refactoringCategory3_1() throws Exception {
		
		 addRefactoringCategory3_1_events();
		
		Assert.assertEquals(1, stream.getEpisodes().length);
		Assert.assertEquals("refactoring", stream.getEpisodes()[0].getCategory());
		Assert.assertEquals("3", stream.getEpisodes()[0].getSubtype());
		
	}


	
	@Test 
	public void refactoringCategory3_2() throws Exception {
		
		addRefactoringCategory3_2_events();
		
		//TODO [rule]  redundancy: 2 refactorings
		Assert.assertEquals(1, stream.getEpisodes().length);
		Assert.assertEquals("refactoring", stream.getEpisodes()[0].getCategory());
		Assert.assertEquals("3", stream.getEpisodes()[0].getSubtype());
		
//		Assert.assertEquals("refactoring", stream.getTDDMeasure().getRecognizedEpisodes().get(1).getCategory());
//		Assert.assertEquals("3", stream.getTDDMeasure().getRecognizedEpisodes().get(1).getSubtype());
		
	}
	
}
