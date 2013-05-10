package org.eclipse.recommenders.test.args.parser;

import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.recommenders.args.completion.rcp.parser.Feature1Extractor;
import org.eclipse.recommenders.args.completion.rcp.parser.Feature2Extractor;
import org.eclipse.recommenders.jayes.util.Pair;
import org.eclipse.recommenders.test.args.util.CodeBuilder;
import org.eclipse.recommenders.test.args.util.TestResource;
import org.eclipse.recommenders.tests.jdt.JavaProjectFixture;
import org.junit.Before;
import org.junit.Test;

public class Feature2ExtractorTest {
	private JavaProjectFixture fixture;

	@Before
	public void before() {
		fixture = TestResource.getJavaProjectFixture();
	}

	
	@Test
	public void testFeature2Extractor_0(){
		String code = CodeBuilder.testCode().toString();
		Feature2Extractor extractor = new Feature2Extractor();
		CompilationUnit cu = fixture.parse(code);
		cu.accept(extractor);
		
		Map<Integer, String> result = extractor.getResult();
		String className = fixture.findClassName(code);
		String packageName = fixture.findPackageName(code);
		assertTrue(result.size() > 0);
		int min = 2000;
		for (Entry<Integer, String> entry : result.entrySet()) {
			String s = entry.getValue().toString();
			if (s.equals("")) continue;
			assertTrue(s.startsWith(packageName + "." + className));
		}
	}

	@Test
	public void testFeature2Extractor_1() {
		exercise(Extractor2TestCode.test1());
	}
	
	@Test
	public void testFeature2Extractor_2() {
		exercise(Extractor2TestCode.test2());
	}
	
	@Test
	public void testFeature2Extractor_3() {
		exercise(Extractor2TestCode.test3());
	}
	
	@Test
	public void testFeature2Extractor_4() {
		exercise(Extractor2TestCode.test4());
	}

	private void exercise(Pair<String, Set<String>> pair) {
		String code = pair.getFirst();
		Set<String> exprectedResult = pair.getSecond();

		Feature2Extractor extractor = new Feature2Extractor();
		CompilationUnit cu = fixture.parse(code);
		String className = fixture.findClassName(code);
		String packageName = fixture.findPackageName(code);
		cu.accept(extractor);

		Map<Integer, String> result = extractor.getResult();

		for (Entry<Integer, String> entry : result.entrySet()) {
			String s = entry.getValue().toString();
			if (s.equals("")) continue; //would it be changed to the class name
			
			assertTrue(s.startsWith(packageName + "." + className));
			assertTrue(exprectedResult.contains(s));
		}
	}
}
