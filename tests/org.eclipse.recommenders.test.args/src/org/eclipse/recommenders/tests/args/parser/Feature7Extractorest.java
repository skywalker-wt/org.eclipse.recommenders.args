package org.eclipse.recommenders.tests.args.parser;

import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.recommenders.args.completion.rcp.entities.BaseVariable;
import org.eclipse.recommenders.args.completion.rcp.parser.Feature3forRecExtractor;
import org.eclipse.recommenders.args.completion.rcp.parser.Feature7Extractor;
import org.eclipse.recommenders.tests.args.util.CodeBuilder;
import org.eclipse.recommenders.tests.args.util.TestResource;
import org.eclipse.recommenders.tests.jdt.JavaProjectFixture;
import org.junit.Before;
import org.junit.Test;

public class Feature7Extractorest {
	private JavaProjectFixture fixture;

	@Before
	public void before() {
		fixture = TestResource.getJavaProjectFixture();
	}

	
	@Test
	public void testFeature0Extractor_0(){
		String code = CodeBuilder.testCode().toString();
		Feature3forRecExtractor extractor = new Feature3forRecExtractor();
		CompilationUnit cu = fixture.parse(code);
		cu.accept(extractor);
		
		Map<Integer,Map<BaseVariable,List<String>>> result = extractor.getPos2AllInvocations();
		assertTrue(result.size() > 0);
	}
	
	@Test
	public void testFeature0Extractor_1(){
		String code = Extractor3forRecTestCode.test1().toString();
		Feature3forRecExtractor extractor = new Feature3forRecExtractor();
		CompilationUnit cu = fixture.parse(code);
		cu.accept(extractor);
		
		Map<Integer,Map<BaseVariable,List<String>>> result = extractor.getPos2AllInvocations();
		assertTrue(result.size() == 0);
	}
	
	@Test
	public void testFeature0Extractor_2(){
		String code = Extractor3forRecTestCode.test2().toString();
		
		Set<String> methods = new HashSet<String>();
		methods.add("createTempFile&open&");
		methods.add("open&");
		
		exercise(code, methods);
	}
	
	@Test
	public void testFeature0Extractor_3(){
		System.out.println();
		String code = Extractor3forRecTestCode.test3().toString();
		
		Set<String> methods = new HashSet<String>();
		methods.add("setText&");
		methods.add("");
		
		exercise(code, methods);
	}
	
	@Test
	public void testFeature0Extractor_4(){
		System.out.println();
		String code = Extractor3forRecTestCode.test4().toString();
		
		Set<String> methods = new HashSet<String>();
		methods.add("setText&");
		methods.add("");
		
		exercise(code, methods);
	}
	
	@Test
	public void testFeature0Extractor_5(){
//		System.out.println();
//		String code = Extractor3forRecTestCode.test5().toString();
//		
//		Set<String> methods = new HashSet<String>();
//		methods.add("setText&");
//		methods.add("");
//		methods.add("setEnabled&setText&");
//		
//		exercise(code, methods);
	}

	private void exercise(String code, Set<String> methods) {
		Feature7Extractor extractor = new Feature7Extractor();
		CompilationUnit cu = fixture.parse(code);
		cu.accept(extractor);
		
		Map<Integer, List<String>> result = extractor.getResult();
		
		for (Entry<Integer, List<String>> entry : result.entrySet()) {
			String tmp = "";
			
			Collections.sort(entry.getValue());
			for (String s : entry.getValue())
				tmp += s + "&";
			
			assertTrue(methods.contains(tmp));
		}
	}
}
