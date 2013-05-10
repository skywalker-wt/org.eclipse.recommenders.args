package org.eclipse.recommenders.test.args.parser;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.eclipse.recommenders.jayes.util.Pair;
import org.eclipse.recommenders.test.args.util.CodeBuilder;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Conversions;

@SuppressWarnings("all")
public class Extractor2TestCode {
  public static Pair<String,Set<String>> test1() {
    Pair<String,Set<String>> _xblockexpression = null;
    {
      HashSet<String> _hashSet = new HashSet<String>();
      final HashSet<String> result = _hashSet;
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("public void test1(){");
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
      final String className = CodeBuilder.classname();
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(".");
      _builder.append(className, "");
      _builder.append(".test");
      _builder.newLineIfNotEmpty();
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
      final CharSequence code = CodeBuilder.classbody(className, _builder_1);
      String _string_1 = code.toString();
      Pair<String,Set<String>> _pair = new Pair<String,Set<String>>(_string_1, result);
      _xblockexpression = (_pair);
    }
    return _xblockexpression;
  }
  
  public static Pair<String,Set<String>> test3() {
    Pair<String,Set<String>> _xblockexpression = null;
    {
      final String className = CodeBuilder.classname();
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(".");
      _builder.append(className, "");
      _builder.append(".");
      _builder.append(className, "");
      _builder.newLineIfNotEmpty();
      String _string = _builder.toString();
      String _trim = _string.trim();
      String _replaceAll = _trim.replaceAll("\\s*", "");
      String[] _split = _replaceAll.split("&");
      HashSet<String> _hashSet = new HashSet<String>(((Collection<? extends String>)Conversions.doWrapArray(_split)));
      final HashSet<String> result = _hashSet;
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("public ");
      _builder_1.append(className, "");
      _builder_1.append("(){");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("\t");
      _builder_1.append("File f = new File(\"/tmp\");");
      _builder_1.newLine();
      _builder_1.append("}");
      _builder_1.newLine();
      final CharSequence code = CodeBuilder.classbody(className, _builder_1);
      String _string_1 = code.toString();
      Pair<String,Set<String>> _pair = new Pair<String,Set<String>>(_string_1, result);
      _xblockexpression = (_pair);
    }
    return _xblockexpression;
  }
  
  public static Pair<String,Set<String>> test4() {
    Pair<String,Set<String>> _xblockexpression = null;
    {
      final String className = CodeBuilder.classname();
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(".");
      _builder.append(className, "");
      _builder.append(".");
      _builder.append(className, "");
      _builder.newLineIfNotEmpty();
      String _string = _builder.toString();
      String _trim = _string.trim();
      String _replaceAll = _trim.replaceAll("\\s*", "");
      String[] _split = _replaceAll.split("&");
      HashSet<String> _hashSet = new HashSet<String>(((Collection<? extends String>)Conversions.doWrapArray(_split)));
      final HashSet<String> result = _hashSet;
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("public ");
      _builder_1.append(className, "");
      _builder_1.append("(){");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("\t");
      _builder_1.append("File f = new File(\"/tmp\");");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("file.createTempFile(\"pre\", \"suffix\");");
      _builder_1.newLine();
      _builder_1.append("}");
      _builder_1.newLine();
      final CharSequence code = CodeBuilder.classbody(className, _builder_1);
      String _string_1 = code.toString();
      Pair<String,Set<String>> _pair = new Pair<String,Set<String>>(_string_1, result);
      _xblockexpression = (_pair);
    }
    return _xblockexpression;
  }
}
