package besouro.integration;

import junit.framework.Assert;

import org.junit.Test;

import besouro.classification.zorro.ZorroTDDMeasure;

public class TDDMeasureTest extends IntegrationTestBaseClass {

	@Test
	public void testIncrementalMeasure() throws Exception {
	
		addTestFirst1Actions();
		Assert.assertEquals(1, stream.getTDDMeasure().getTDDPercentageByNumber(), 0.01);
		
		addRefactoring1A_Actions();
		Assert.assertEquals(1, stream.getTDDMeasure().getTDDPercentageByNumber(), 0.01);
		
		addTestLast1Actions();
		Assert.assertEquals(2f/3f, stream.getTDDMeasure().getTDDPercentageByNumber(), 0.01);
		
		addRefactoring1A_Actions();
		Assert.assertEquals(2f/4f, stream.getTDDMeasure().getTDDPercentageByNumber(), 0.01);
		
		
	}

	
}
