package org.eclipse.recommenders.tests.args.recommenders

import org.eclipse.recommenders.tests.args.util.CodeBuilder

class PreciseParamRecommenderTestCode {
	def static test1() {
		CodeBuilder::classbody('''
			private Browser browserField;

			public void test(Browser browser, ToolBar toolbar){
				Browser localBrowser = null;
				
				ToolItem itemBack = new ToolItem(toolbar, SWT.PUSH);
				itemBack.setText("Back");
				ToolItem itemForward = new ToolItem(toolbar, SWT.PUSH);
				itemForward.setText("Forward");
			
				itemBack.setEnabled(browser.isBackEnabled());
				itemForward.setEnabled($);
			}
		''');
	}
	
	def static test2(){
		CodeBuilder::classbody('''
				Image images[];
		        
		        public void handleEvent(Event e) {
					Rectangle rect = images[0].getBounds();
					Point pt = ((Canvas)e.widget).getSize();
					
					e.gc.drawImage($, 0, 0, rect.width, rect.height, 0, 0, pt.x, pt.y);
				}
		''');
		/* expected: images[] */
	}
	
	def static test3(){
		CodeBuilder::classbody('''
				Image images[];
		        
		        public void handleEvent(Event e) {
					Rectangle rect = images[0].getBounds();
					Point pt = ((Canvas)e.widget).getSize();
					
					e.gc.drawImage(images[index], 0, 0, rect.width, rect.height, 0, 0, $, pt.y);
				}
		''');
		/* expected: pt.x */
	}
	
	def static test4(){
		CodeBuilder::classbody('''
			public void test(Canvas canvas){
				StyledTextDropTargetEffect effect = new StyledTextDropTargetEffect($);
			}
		''');
		/* expected: (StyledText)canvas */
	}
}