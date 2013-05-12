package org.eclipse.recommenders.tests.args.parser

import org.eclipse.recommenders.tests.args.util.CodeBuilder
import java.util.HashSet
import org.eclipse.recommenders.jayes.util.Pair
import java.util.Set

class Extractor0TestCode {
	def static test1() {
		val result = new HashSet<String>('''
		"test"
		'''.toString().trim().replaceAll("\\s*", "").split("&"));
		
		val code = CodeBuilder::classbody('''
			public void test(){
				File f  = new File("test");
			}
		''');
		
		new Pair<String, Set<String>>(code.toString, result);
	}

	def static test2() {
		val result = new HashSet<String>();
		
		val code = CodeBuilder::classbody('''
			public void test(){
			}
		''');
		
		new Pair<String, Set<String>>(code.toString, result);
	}
	
	def static test3() {
		val result = new HashSet<String>('''
		"test"&"pre"&"suffix"
		'''.toString().trim().replaceAll("\\s*", "").split("&"));
		
		val code = CodeBuilder::classbody('''
			private void test() throws IOException {
				File f = new File("test");
				f.createTempFile("pre", "suffix");
			}
		''');
		
		new Pair<String, Set<String>>(code.toString, result);
	}
	
	def static test4() {
		val result = new HashSet<String>('''
		null&SWT.NONE
		'''.toString().trim().replaceAll("\\s*", "").split("&"));
		
		val code = CodeBuilder::classbody('''
			private void test() {
				Label label = new Label(null, SWT.NONE);
			}
		''');
		
		new Pair<String, Set<String>>(code.toString, result);
	}
	
	def static test5() {
		val result = new HashSet<String>('''
		10&20&100&150
		'''.toString().trim().replaceAll("\\s*", "").split("&"));
		
		val code = CodeBuilder::classbody('''
			private void test(Label label) {
				label.setBounds(10, 20, 100, 150);
			}
		''');
		
		new Pair<String, Set<String>>(code.toString, result);
	}
	
	def static test6() {
		val result = new HashSet<String>('''
		10&20&100&150&null&SWT.NONE
		'''.toString().trim().replaceAll("\\s*", "").split("&"));
		
		val code = CodeBuilder::classbody('''
			private void test() {
				Label label = new Label(null, SWT.NONE);
				label.setBounds(10, 20, 100, 150);
			}
		''');
		
		new Pair<String, Set<String>>(code.toString, result);
	}
	
}

