package org.eclipse.recommenders.tests.args.parser;

import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.recommenders.args.completion.rcp.parser.Feature1Extractor;
import org.eclipse.recommenders.jayes.util.Pair;
import org.eclipse.recommenders.tests.args.util.CodeBuilder;
import org.eclipse.recommenders.tests.args.util.TestResource;
import org.eclipse.recommenders.tests.jdt.JavaProjectFixture;
import org.junit.Before;
import org.junit.Test;

public class Feature1ExtractorTest {
	private JavaProjectFixture fixture;

	@Before
	public void before() {
		fixture = TestResource.getJavaProjectFixture();
	}

	
	@Test
	public void testFeature1Extractor_0(){
		String code = CodeBuilder.testCode().toString();
		Feature1Extractor extractor = new Feature1Extractor();
		CompilationUnit cu = fixture.parse(code);
		cu.accept(extractor);
		
		Map<Integer, String> result = extractor.getResult();
		assertTrue(result.size() > 0);
	}
	
	@Test
	public void testFeature1Extractor_1() {
		exercise(Extractor1TestCode.test1());
	}
	
	@Test
	public void testFeature1Extractor_2() {
		exercise(Extractor1TestCode.test2());
	}
	
	@Test
	public void testFeature1Extractor_3() {
		exercise(Extractor1TestCode.test3());
	}
	
	@Test
	public void testFeature1Extractor_4() {
		exercise(Extractor1TestCode.test4());
	}
	
	@Test
	public void testFeature1Extractor_5() {
		Pair<String, Set<String>> pair = Extractor1TestCode.test5();
		String code = pair.getFirst();
		Set<String> exprectedResult = pair.getSecond();
		
		Feature1Extractor extractor = new Feature1Extractor();
		CompilationUnit cu = fixture.parse(code);
		cu.accept(extractor);

		Map<Integer, String> result = extractor.getResult();
		assertTrue(result.size() == 4);
		
		for (Entry<Integer, String> entry : result.entrySet()) {
			String s = entry.getValue().toString();
			assertTrue(exprectedResult.contains(s));
		}
	}

	@Test
	public void testFeature0Extractor_6() {
		exercise(Extractor1TestCode.test6());
	}

	private void exercise(Pair<String, Set<String>> pair) {
		String code = pair.getFirst();
		Set<String> exprectedResult = pair.getSecond();

		Feature1Extractor extractor = new Feature1Extractor();
		CompilationUnit cu = fixture.parse(code);
		cu.accept(extractor);

		Map<Integer, String> result = extractor.getResult();

		for (Entry<Integer, String> entry : result.entrySet()) {
			String s = entry.getValue().toString();
			assertTrue(exprectedResult.contains(s));
		}
	}
}
