package besouro.measure;
import java.io.File;
import java.io.IOException;

import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.analysis.ICoverageVisitor;
import org.jacoco.core.data.ExecutionDataStore;

public class CoverageMeter implements ICoverageVisitor {
	private final Analyzer analyzer;

	 public CoverageMeter() {
		analyzer = new Analyzer(new ExecutionDataStore(),this);
	}
	 
	 public void execute(String file) throws IOException {
		//InputStream in = new FileInputStream(new File(file));
		//analyzer.analyzeClass(in, "/Users/dfucci/Desktop/runtime-EclipseApplication/My project/");
		 int result = analyzer.analyzeAll(new File(file));
		 System.out.println(result);
	 }

		
	 public void visitCoverage(final IClassCoverage coverage) {
		 	System.out.println("visitCoverage");
			coverage.getName();
			coverage.getId();
			coverage.getInstructionCounter().getTotalCount();
			coverage.getComplexityCounter().getTotalCount();
		}


}
