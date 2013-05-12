package org.eclipse.recommenders.tests.args.parser;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.recommenders.args.completion.rcp.entities.BaseVariable;
import org.eclipse.recommenders.args.completion.rcp.parser.Feature3forRecExtractor;
import org.eclipse.recommenders.tests.args.util.CodeBuilder;
import org.eclipse.recommenders.tests.args.util.TestResource;
import org.eclipse.recommenders.tests.jdt.JavaProjectFixture;
import org.junit.Before;
import org.junit.Test;

public class Feature3forRecExtractorTest {
	private JavaProjectFixture fixture;

	@Before
	public void before() {
		fixture = TestResource.getJavaProjectFixture();
	}

	
	@Test
	public void testFeature3Extractor_0(){
		String code = CodeBuilder.testCode().toString();
		Feature3forRecExtractor extractor = new Feature3forRecExtractor();
		CompilationUnit cu = fixture.parse(code);
		cu.accept(extractor);
		
		Map<Integer,Map<BaseVariable,List<String>>> result = extractor.getPos2AllInvocations();
		assertTrue(result.size() > 0);
	}
	
	@Test
	public void testFeature3Extractor_1(){
		String code = Extractor3forRecTestCode.test1().toString();
		Feature3forRecExtractor extractor = new Feature3forRecExtractor();
		CompilationUnit cu = fixture.parse(code);
		cu.accept(extractor);
		
		Map<Integer,Map<BaseVariable,List<String>>> result = extractor.getPos2AllInvocations();
		assertTrue(result.size() == 0);
	}
	
	@Test
	public void testFeature3Extractor_2(){
		String code = Extractor3forRecTestCode.test2().toString();
		
		Set<String> variables = new HashSet<String>();
		Set<String> methods = new HashSet<String>();
		variables.add("f (java.io.File)");
		methods.add("createTempFile");
		methods.add("open");
		
		exercise(code, variables, methods);
	}
	
	@Test
	public void testFeature3Extractor_3(){
		String code = Extractor3forRecTestCode.test3().toString();
		
		Set<String> variables = new HashSet<String>();
		Set<String> methods = new HashSet<String>();
		variables.add("browser (org.eclipse.swt.browser.Browser)");
		variables.add("itemBack (org.eclipse.swt.widgets.ToolItem)");
		variables.add("itemForward (org.eclipse.swt.widgets.ToolItem)");
		methods.add("isBackEnabled");
		methods.add("setEnabled");
		methods.add("setText");
		
		exercise(code, variables, methods);
	}
	
	@Test
	public void testFeature3Extractor_4(){
		String code = Extractor3forRecTestCode.test4().toString();
		
		Set<String> variables = new HashSet<String>();
		Set<String> methods = new HashSet<String>();
		variables.add("browser (org.eclipse.swt.browser.Browser)");
		variables.add("itemBack (org.eclipse.swt.widgets.ToolItem)");
		variables.add("itemForward (org.eclipse.swt.widgets.ToolItem)");
		methods.add("isBackEnabled");
		methods.add("setEnabled");
		methods.add("setText");
		
		exercise(code, variables, methods);
	}

	private void exercise(String code, Set<String> variables, Set<String> methods) {
		Feature3forRecExtractor extractor = new Feature3forRecExtractor();
		CompilationUnit cu = fixture.parse(code);
		cu.accept(extractor);
		
		Map<Integer,Map<BaseVariable,List<String>>> result = extractor.getPos2AllInvocations();
		
		for (Entry<Integer, Map<BaseVariable,List<String>>> entry : result.entrySet()) {
			for (Entry<BaseVariable,List<String>> e : entry.getValue().entrySet()) {
				assertTrue(variables.contains(e.getKey().toString()));
				assertTrue(methods.containsAll(e.getValue()));
			}
		}
	}
}
