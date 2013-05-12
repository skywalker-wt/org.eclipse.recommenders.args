package org.eclipse.recommenders.tests.args.parser

import java.util.HashSet
import org.eclipse.recommenders.jayes.util.Pair
import java.util.Set
import org.eclipse.recommenders.tests.args.util.CodeBuilder

class Extractor3forRecTestCode {
	def static test1() {
		CodeBuilder::classbody('''
			public void test(){
			}
		''');
	}
	
	def static test2() {
		CodeBuilder::classbody('''
			public void test(){
				File f = new File("/tmp");
				f.open();
				f.createTempFile("pre", "suffix");
				f.close();
			}
		''');
	}
	
	def static test3() {
		CodeBuilder::classbody('''
			public void test(Browser browser, ToolBar toolbar){
				ToolItem itemBack = new ToolItem(toolbar, SWT.PUSH);
				itemBack.setText("Back");
				ToolItem itemForward = new ToolItem(toolbar, SWT.PUSH);
				itemForward.setText("Forward");
			
				itemBack.setEnabled(browser.isBackEnabled());
				itemForward.setEnabled(browser.isForwardEnabled());
			}
		''');
	}
	
	def static test4() {
		val className = CodeBuilder::classname();
		
		CodeBuilder::classbody(className, '''
			public «className»(Browser browser, ToolBar toolbar){
				ToolItem itemBack = new ToolItem(toolbar, SWT.PUSH);
				itemBack.setText("Back");
				ToolItem itemForward = new ToolItem(toolbar, SWT.PUSH);
				itemForward.setText("Forward");
			
				itemBack.setEnabled(browser.isBackEnabled());
				itemForward.setEnabled(browser.isForwardEnabled());
			}
		''');
	}
}