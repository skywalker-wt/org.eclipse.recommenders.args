package org.eclipse.recommenders.tests.args.parser;

import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.recommenders.args.completion.rcp.parser.Feature0Extractor;
import org.eclipse.recommenders.jayes.util.Pair;
import org.eclipse.recommenders.tests.args.util.CodeBuilder;
import org.eclipse.recommenders.tests.args.util.TestResource;
import org.eclipse.recommenders.tests.jdt.JavaProjectFixture;
import org.junit.Before;
import org.junit.Test;

public class Feature0ExtractorTest {
	private JavaProjectFixture fixture;

	@Before
	public void before() {
		fixture = TestResource.getJavaProjectFixture();
	}

	
	 @Test
	 public void testFeature0Extractor_0(){
		 String code = CodeBuilder.testCode().toString();
		 Feature0Extractor extractor = new Feature0Extractor();
		 CompilationUnit cu = fixture.parse(code);
		 cu.accept(extractor);
		
		 Map<Integer, Expression> result = extractor.getResult();
		 assertTrue(result.size() > 0);
	 }
	
	@Test
	public void testFeature0Extractor_1() {
		exercise(Extractor0TestCode.test1());
	}
	
	@Test
	public void testFeature0Extractor_2() {
		exercise(Extractor0TestCode.test2());
	}
	
	@Test
	public void testFeature0Extractor_3() {
		exercise(Extractor0TestCode.test3());
	}

	@Test
	public void testFeature0Extractor_4() {
		exercise(Extractor0TestCode.test4());
	}
	
	@Test
	public void testFeature0Extractor_5() {
		exercise(Extractor0TestCode.test5());
	}
	
	@Test
	public void testFeature0Extractor_6() {
		exercise(Extractor0TestCode.test6());
	}

	private void exercise(Pair<String, Set<String>> pair) {
		String code = pair.getFirst();
		Set<String> exprectedResult = pair.getSecond();

		Feature0Extractor extractor = new Feature0Extractor();
		CompilationUnit cu = fixture.parse(code);
		cu.accept(extractor);

		Map<Integer, Expression> result = extractor.getResult();

		assertTrue(result.size() == exprectedResult.size());
		for (Entry<Integer, Expression> entry : result.entrySet()) {
			String s = entry.getValue().toString();
			assertTrue(exprectedResult.contains(s));
		}
	}

}
