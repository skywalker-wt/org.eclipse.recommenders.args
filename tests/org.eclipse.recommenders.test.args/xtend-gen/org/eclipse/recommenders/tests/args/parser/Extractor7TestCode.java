package org.eclipse.recommenders.tests.args.parser;

import org.eclipse.recommenders.tests.args.util.CodeBuilder;
import org.eclipse.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public class Extractor7TestCode {
  public static CharSequence test1() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public void test(){");
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
    _builder.append("File f = new File(\"/tmp\");");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("f.open();");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("f.createTempFile(\"pre\", \"suffix\");");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("f.createTempFile(\"pre\", \"suffix\");");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("f.close();");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    CharSequence _classbody = CodeBuilder.classbody(_builder);
    return _classbody;
  }
  
  public static CharSequence test3() {
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
    _builder.append("itemForward.setEnabled(browser.isForwardEnabled());");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    CharSequence _classbody = CodeBuilder.classbody(_builder);
    return _classbody;
  }
  
  public static CharSequence test4() {
    CharSequence _xblockexpression = null;
    {
      final String className = CodeBuilder.classname();
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("public ");
      _builder.append(className, "");
      _builder.append("(Browser browser, ToolBar toolbar){");
      _builder.newLineIfNotEmpty();
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
      _builder.append("itemForward.setEnabled(browser.isForwardEnabled());");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      CharSequence _classbody = CodeBuilder.classbody(className, _builder);
      _xblockexpression = (_classbody);
    }
    return _xblockexpression;
  }
  
  public static CharSequence test5() {
    CharSequence _xblockexpression = null;
    {
      final String className = CodeBuilder.classname();
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("public ");
      _builder.append(className, "");
      _builder.append("(Browser browser, ToolBar toolbar){");
      _builder.newLineIfNotEmpty();
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
      _builder.append("itemForward.setEnabled(browser.isForwardEnabled());");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("itemBack.setEnabled(true);");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("itemForward.setEnabled(true);");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      CharSequence _classbody = CodeBuilder.classbody(className, _builder);
      _xblockexpression = (_classbody);
    }
    return _xblockexpression;
  }
}
