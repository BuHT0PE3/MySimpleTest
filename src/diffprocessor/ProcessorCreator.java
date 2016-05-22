package diffprocessor;

public class ProcessorCreator {
	public Processor createProcessor(long limit) {
		return new Processor(limit);
	}
}
