package org.eclipse.recommenders.tests.args.recommenders;

import org.eclipse.recommenders.tests.args.util.CodeBuilder;
import org.eclipse.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public class PreciseParamRecommenderTestCode {
  public static CharSequence test1() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("private Browser browserField;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("public void test(Browser browser, ToolBar toolbar){");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Browser localBrowser = null;");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("ToolItem itemBack = new ToolItem(toolbar, SWT.PUSH);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("itemBack.setText(\"Back\");");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("ToolItem itemForward = new ToolItem(toolbar, SWT.PUSH);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("itemForward.setText(\"Forward\");");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("itemBack.setEnabled(browser.isBackEnabled());");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("itemForward.setEnabled($);");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    CharSequence _classbody = CodeBuilder.classbody(_builder);
    return _classbody;
  }
  
  public static CharSequence test2() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Image images[];");
    _builder.newLine();
    _builder.append("\t\t        ");
    _builder.newLine();
    _builder.append("\t\t        ");
    _builder.append("public void handleEvent(Event e) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Rectangle rect = images[0].getBounds();");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Point pt = ((Canvas)e.widget).getSize();");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("e.gc.drawImage($, 0, 0, rect.width, rect.height, 0, 0, pt.x, pt.y);");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    CharSequence _classbody = CodeBuilder.classbody(_builder);
    return _classbody;
  }
  
  public static CharSequence test3() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Image images[];");
    _builder.newLine();
    _builder.append("\t\t        ");
    _builder.newLine();
    _builder.append("\t\t        ");
    _builder.append("public void handleEvent(Event e) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Rectangle rect = images[0].getBounds();");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Point pt = ((Canvas)e.widget).getSize();");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("e.gc.drawImage(images[index], 0, 0, rect.width, rect.height, 0, 0, $, pt.y);");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    CharSequence _classbody = CodeBuilder.classbody(_builder);
    return _classbody;
  }
  
  public static CharSequence test4() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public void test(Canvas canvas){");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("StyledTextDropTargetEffect effect = new StyledTextDropTargetEffect($);");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    CharSequence _classbody = CodeBuilder.classbody(_builder);
    return _classbody;
  }
}
