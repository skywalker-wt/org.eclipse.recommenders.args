package org.eclipse.recommenders.tests.args.parser;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.eclipse.recommenders.jayes.util.Pair;
import org.eclipse.recommenders.tests.args.util.CodeBuilder;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Conversions;

@SuppressWarnings("all")
public class Extractor1TestCode {
  public static Pair<String,Set<String>> test1() {
    Pair<String,Set<String>> _xblockexpression = null;
    {
      HashSet<String> _hashSet = new HashSet<String>();
      final HashSet<String> result = _hashSet;
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("public void test(){");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      final CharSequence code = CodeBuilder.classbody(_builder);
      String _string = code.toString();
      Pair<String,Set<String>> _pair = new Pair<String,Set<String>>(_string, result);
      _xblockexpression = (_pair);
    }
    return _xblockexpression;
  }
  
  public static Pair<String,Set<String>> test2() {
    Pair<String,Set<String>> _xblockexpression = null;
    {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("File");
      _builder.newLine();
      String _string = _builder.toString();
      String _trim = _string.trim();
      String _replaceAll = _trim.replaceAll("\\s*", "");
      String[] _split = _replaceAll.split("&");
      HashSet<String> _hashSet = new HashSet<String>(((Collection<? extends String>)Conversions.doWrapArray(_split)));
      final HashSet<String> result = _hashSet;
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("public void test(){");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("File f = new File(\"/tmp\");");
      _builder_1.newLine();
      _builder_1.append("}");
      _builder_1.newLine();
      final CharSequence code = CodeBuilder.classbody(_builder_1);
      String _string_1 = code.toString();
      Pair<String,Set<String>> _pair = new Pair<String,Set<String>>(_string_1, result);
      _xblockexpression = (_pair);
    }
    return _xblockexpression;
  }
  
  public static Pair<String,Set<String>> test3() {
    Pair<String,Set<String>> _xblockexpression = null;
    {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("createTempFile");
      _builder.newLine();
      String _string = _builder.toString();
      String _trim = _string.trim();
      String _replaceAll = _trim.replaceAll("\\s*", "");
      String[] _split = _replaceAll.split("&");
      HashSet<String> _hashSet = new HashSet<String>(((Collection<? extends String>)Conversions.doWrapArray(_split)));
      final HashSet<String> result = _hashSet;
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("public void test(File file){");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("file.createTempFile(\"pre\", \"suffix\");");
      _builder_1.newLine();
      _builder_1.append("}");
      _builder_1.newLine();
      final CharSequence code = CodeBuilder.classbody(_builder_1);
      String _string_1 = code.toString();
      Pair<String,Set<String>> _pair = new Pair<String,Set<String>>(_string_1, result);
      _xblockexpression = (_pair);
    }
    return _xblockexpression;
  }
  
  public static Pair<String,Set<String>> test4() {
    Pair<String,Set<String>> _xblockexpression = null;
    {
      HashSet<String> _hashSet = new HashSet<String>();
      final HashSet<String> result = _hashSet;
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("public void test(Label label){");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("label.addNotify();");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      final CharSequence code = CodeBuilder.classbody(_builder);
      String _string = code.toString();
      Pair<String,Set<String>> _pair = new Pair<String,Set<String>>(_string, result);
      _xblockexpression = (_pair);
    }
    return _xblockexpression;
  }
  
  public static Pair<String,Set<String>> test5() {
    Pair<String,Set<String>> _xblockexpression = null;
    {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("checkImage");
      _builder.newLine();
      String _string = _builder.toString();
      String _trim = _string.trim();
      String _replaceAll = _trim.replaceAll("\\s*", "");
      String[] _split = _replaceAll.split("&");
      HashSet<String> _hashSet = new HashSet<String>(((Collection<? extends String>)Conversions.doWrapArray(_split)));
      final HashSet<String> result = _hashSet;
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("public void test(Label label){");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("label.checkImage(null, 100, 100, null);");
      _builder_1.newLine();
      _builder_1.append("}");
      _builder_1.newLine();
      final CharSequence code = CodeBuilder.classbody(_builder_1);
      String _string_1 = code.toString();
      Pair<String,Set<String>> _pair = new Pair<String,Set<String>>(_string_1, result);
      _xblockexpression = (_pair);
    }
    return _xblockexpression;
  }
  
  public static Pair<String,Set<String>> test6() {
    Pair<String,Set<String>> _xblockexpression = null;
    {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("addFocusListener");
      _builder.newLine();
      String _string = _builder.toString();
      String _trim = _string.trim();
      String _replaceAll = _trim.replaceAll("\\s*", "");
      String[] _split = _replaceAll.split("&");
      HashSet<String> _hashSet = new HashSet<String>(((Collection<? extends String>)Conversions.doWrapArray(_split)));
      final HashSet<String> result = _hashSet;
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("public void test(Label label){");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("label.addFocusListener(new FocusListener() {");
      _builder_1.newLine();
      _builder_1.append("\t\t");
      _builder_1.append("@Override");
      _builder_1.newLine();
      _builder_1.append("\t\t");
      _builder_1.append("public void focusGained(FocusEvent e) {");
      _builder_1.newLine();
      _builder_1.append("\t\t");
      _builder_1.append("}");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.newLine();
      _builder_1.append("\t\t");
      _builder_1.append("@Override");
      _builder_1.newLine();
      _builder_1.append("\t\t");
      _builder_1.append("public void focusLost(FocusEvent e) {");
      _builder_1.newLine();
      _builder_1.append("\t\t");
      _builder_1.append("}");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("});");
      _builder_1.newLine();
      _builder_1.append("}");
      _builder_1.newLine();
      final CharSequence code = CodeBuilder.classbody(_builder_1);
      String _string_1 = code.toString();
      Pair<String,Set<String>> _pair = new Pair<String,Set<String>>(_string_1, result);
      _xblockexpression = (_pair);
    }
    return _xblockexpression;
  }
}
