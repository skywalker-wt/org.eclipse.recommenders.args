package org.eclipse.recommenders.test.args.rcp

import org.eclipse.recommenders.test.args.util.CodeBuilder

class ParameterCompletionProposalComputerTestCode {
	def static test1() {
		CodeBuilder::classbody('''
			public void test(Browser browser, ToolBar toolbar){
				ToolItem itemBack = new ToolItem(toolbar, SWT.PUSH);
				itemBack.setText("Back");
				ToolItem itemForward = new ToolItem(toolbar, SWT.PUSH);
				itemForward.setText("Forward");
			
				itemBack.setEnabled(browser.isBackEnabled());
				itemForward.setEnabled($);
			}
		''');
	}
	
	def static test2() {
		CodeBuilder::classbody('''
			public void test(){
				Label label = new Label(null, $);
			}
		''');
	}
	
	def static test3() {
		CodeBuilder::classbody('''
			public void test(Composite parent, boolean top){
				new FormAttachment(status, -5, $);
			}
		''');
	}
	
	def static test4() {
		CodeBuilder::classbody('''
			public void test(){
				Display display = new Display();
				Shell shell = new Shell(display);
				shell.setText($);
			}
		''');
	}
	
	def static test5() {
		CodeBuilder::classbody('''
			public void test(){
				new FormAttachment(100, $);
			}
		''');
	}
	
	def static test6() {
		CodeBuilder::classbody('''
			public void test(){
				Text locationBar = new Text(parent, $);
			}
		''');
	}
}