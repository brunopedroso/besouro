package besouro.listeners;

import static org.mockito.Mockito.mock;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import besouro.listeners.mock.ResourceChangeEventFactory;
import besouro.measure.JavaStatementMeter;
import besouro.model.EditAction;

public class JavaMeasurerTest {

	private JavaStatementMeter metric;
	
	private String file;
	private EditAction action1;
	private EditAction action2;
	private Date clock;

	
	@Before
	public void setup() throws Exception {

		file = "afile.any";
		
		clock = new Date();
		action1 = new EditAction(clock, file);
		action2 = new EditAction(clock, file);
		action2.setPreviousAction(action1);
		
		metric = mock(JavaStatementMeter.class);
		

	}
	
	@Test
	public void shouldRecognizeProductionEditsWhenNoTestWordIsFoundInTheNameOfTheClass() throws Exception {
		metric = new JavaStatementMeter();
		metric.visit(ResourceChangeEventFactory.createClassDeclaration("ProductionClassName"));
		Assert.assertFalse(metric.isTest());
	}

	@Test
	public void shouldRecognizeTestEditsByTesInTheNameOfTheClass() throws Exception {
		metric = new JavaStatementMeter();
		metric.visit(ResourceChangeEventFactory.createClassDeclaration("TestClassName"));
		Assert.assertTrue(metric.isTest());		
	}
	

	@Test
	public void shouldRecognizeTestClassByPackageName() throws Exception {
		metric = new JavaStatementMeter();
		metric.visit(ResourceChangeEventFactory.createClassDeclaration("AnOrdinaryClassName"));
		metric.visit(ResourceChangeEventFactory.createPackageDeclaration("package.with.test.word"));
		Assert.assertTrue(metric.isTest());
	}
	
	@Test
	public void shouldRecognizeWithPackageNameNull() throws Exception {
		metric = new JavaStatementMeter();
		metric.visit(ResourceChangeEventFactory.createClassDeclaration("TestClassName"));
		metric.visit(ResourceChangeEventFactory.createPackageDeclaration(null));
		Assert.assertTrue(metric.isTest());
	}
	
}
