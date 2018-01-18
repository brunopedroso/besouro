package besouro.classification;

import org.junit.Assert;
import org.junit.Test;

import besouro.model.Episode;

public class TDDMeasureTest {

	@Test
	public void contextIndependentCase() throws Exception {
		
		TDDMeasure measure = new TDDMeasure();
		
		Episode e1 = new Episode();
		e1.setDuration(10);
		e1.setIsTDD(true);
		measure.addEpisode(e1);
		
		Episode e2 = new Episode();
		e2.setDuration(10);
		e2.setIsTDD(true);
		measure.addEpisode(e2);
		
		Episode e3 = new Episode();
		e3.setDuration(30);
		e3.setIsTDD(false);
		measure.addEpisode(e3);
		
		Episode e4 = new Episode();
		e4.setDuration(10);
		e4.setIsTDD(true);
		measure.addEpisode(e4);
		
		Assert.assertEquals(0.75, measure.getTDDPercentageByNumber(), 0.0001);
		Assert.assertEquals(0.5, measure.getTDDPercentageByDuration(), 0.0001);
		
	}
}
