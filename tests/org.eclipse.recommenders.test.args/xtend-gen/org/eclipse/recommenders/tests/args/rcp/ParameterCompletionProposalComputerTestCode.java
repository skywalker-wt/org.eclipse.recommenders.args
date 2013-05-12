package org.eclipse.recommenders.tests.args.rcp;

import org.eclipse.recommenders.tests.args.util.CodeBuilder;
import org.eclipse.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public class ParameterCompletionProposalComputerTestCode {
  public static CharSequence test1() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public void test(Browser browser, ToolBar toolbar){");
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
    _builder.append("public void test(){");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Label label = new Label(null, $);");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    CharSequence _classbody = CodeBuilder.classbody(_builder);
    return _classbody;
  }
  
  public static CharSequence test3() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public void test(Composite parent, boolean top){");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("new FormAttachment(status, -5, $);");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    CharSequence _classbody = CodeBuilder.classbody(_builder);
    return _classbody;
  }
  
  public static CharSequence test4() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public void test(){");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Display display = new Display();");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Shell shell = new Shell(display);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("shell.setText($);");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    CharSequence _classbody = CodeBuilder.classbody(_builder);
    return _classbody;
  }
  
  public static CharSequence test5() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public void test(){");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("new FormAttachment(100, $);");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    CharSequence _classbody = CodeBuilder.classbody(_builder);
    return _classbody;
  }
  
  public static CharSequence test6() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public void test(){");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Text locationBar = new Text(parent, $);");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    CharSequence _classbody = CodeBuilder.classbody(_builder);
    return _classbody;
  }
  
  public static CharSequence test7() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public void test(){");
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
