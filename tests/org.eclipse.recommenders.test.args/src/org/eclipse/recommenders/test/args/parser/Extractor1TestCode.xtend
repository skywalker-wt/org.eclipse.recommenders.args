package org.eclipse.recommenders.test.args.parser

import org.eclipse.recommenders.test.args.util.CodeBuilder
import java.util.HashSet
import org.eclipse.recommenders.jayes.util.Pair
import java.util.Set

class Extractor1TestCode {
	def static test1() {
		val result = new HashSet<String>();
		
		val code = CodeBuilder::classbody('''
			public void test(){
			}
		''');
		
		new Pair<String, Set<String>>(code.toString, result);
	}
	
	def static test2() {
		val result = new HashSet<String>('''
		File
		'''.toString().trim().replaceAll("\\s*", "").split("&"));
		
		val code = CodeBuilder::classbody('''
			public void test(){
				File f = new File("/tmp");
			}
		''');
		
		new Pair<String, Set<String>>(code.toString, result);
	}
	
	def static test3() {
		val result = new HashSet<String>('''
		createTempFile
		'''.toString().trim().replaceAll("\\s*", "").split("&"));
		
		val code = CodeBuilder::classbody('''
			public void test(File file){
				file.createTempFile("pre", "suffix");
			}
		''');
		
		new Pair<String, Set<String>>(code.toString, result);
	}
	
	def static test4() {
		val result = new HashSet<String>();
		
		val code = CodeBuilder::classbody('''
			public void test(Label label){
				label.addNotify();
			}
		''');
		
		new Pair<String, Set<String>>(code.toString, result);
	}
	
	def static test5() {
		val result = new HashSet<String>('''
		checkImage
		'''.toString().trim().replaceAll("\\s*", "").split("&"));
		
		val code = CodeBuilder::classbody('''
			public void test(Label label){
				label.checkImage(null, 100, 100, null);
			}
		''');
		
		new Pair<String, Set<String>>(code.toString, result);
	}
	
	def static test6() {
		val result = new HashSet<String>('''
		addFocusListener
		'''.toString().trim().replaceAll("\\s*", "").split("&"));
		
		val code = CodeBuilder::classbody('''
			public void test(Label label){
				label.addFocusListener(new FocusListener() {
					@Override
					public void focusGained(FocusEvent e) {
					}
	
					@Override
					public void focusLost(FocusEvent e) {
					}
				});
			}
		''');
		
		new Pair<String, Set<String>>(code.toString, result);
	}
}