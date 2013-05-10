package org.eclipse.recommenders.test.args.parser

import java.util.HashSet
import org.eclipse.recommenders.jayes.util.Pair
import java.util.Set
import org.eclipse.recommenders.test.args.util.CodeBuilder

class Extractor2TestCode {
	def static test1() {
		val result = new HashSet<String>();
		
		val code = CodeBuilder::classbody('''
			public void test1(){
			}
		''');
		
		new Pair<String, Set<String>>(code.toString, result);
	}
	
	def static test2() {
		val className = CodeBuilder::classname();
		val result = new HashSet<String>('''
			.«className».test
		'''.toString().trim().replaceAll("\\s*", "").split("&"));
		
		val code = CodeBuilder::classbody(className, '''
			public void test(){
				File f = new File("/tmp");
			}
		''');
		
		new Pair<String, Set<String>>(code.toString, result);
	}
	
	def static test3() {
		val className = CodeBuilder::classname();
		val result = new HashSet<String>('''
			.«className».«className»
		'''.toString().trim().replaceAll("\\s*", "").split("&"));
		
		val code = CodeBuilder::classbody(className, '''
			public «className»(){
				File f = new File("/tmp");
			}
		''');
		
		new Pair<String, Set<String>>(code.toString, result);
	}
	
	def static test4() {
		val className = CodeBuilder::classname();
		val result = new HashSet<String>('''
			.«className».«className»
		'''.toString().trim().replaceAll("\\s*", "").split("&"));
		
		val code = CodeBuilder::classbody(className, '''
			public «className»(){
				File f = new File("/tmp");
				file.createTempFile("pre", "suffix");
			}
		''');
		
		new Pair<String, Set<String>>(code.toString, result);
	}
}