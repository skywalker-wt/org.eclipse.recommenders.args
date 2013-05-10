package org.eclipse.recommenders.test.args.util;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.Arrays.asList;
import static org.eclipse.recommenders.utils.Throws.throwUnhandledException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.recommenders.tests.jdt.JavaProjectFixture;

public final class TestResource {
	
	private TestResource() {
		
	}
	
	public static final String TEST_BUDDLE_ID = 
			"org.eclipse.recommenders.test.args";
	
	public static final String TEST_RESOURCE_URL = 
			"platform:/plugin/org.eclipse.recommenders.test.args/test_resource/";
	
	public static final String TEST_JSON_FILE1 = 
			"org.eclipse.swt.widgets.Label_setText_1_java.lang.String_java.lang.String,.json";
	public static final String TEST_JSON_FILE2 = 
			"GC_drawImage_1_Image_Image,int,int,int,int,int,int,int,int,226780122.json";
	public static final String TEST_JSON_FILE3 = 
			"org.eclipse.swt.widgets.Display_getSystemColor_1_int_int,.json";

	public static final String TEST_BN_FILE1 = 
			"org.eclipse.swt.widgets.Label_setText_1_java.lang.String_java.lang.String,";
	public static final String TEST_BN_FILE2 = 
			"GC_drawImage_1_Image_Image,int,int,int,int,int,int,int,int,226780122";
	public static final String TEST_BN_FILE3 = 
			"org.eclipse.swt.widgets.Display_getSystemColor_1_int_int,";
	
	public static final String TEST_PROJECT_NAME = "test_args";

	
	public static String[] getJSONFiles() {
		return new String[] {
				TEST_RESOURCE_URL + TEST_JSON_FILE1,
				TEST_RESOURCE_URL + TEST_JSON_FILE2,
				TEST_RESOURCE_URL + TEST_JSON_FILE3
				};
	}
	
	public static String[] getBNFiles() {
		return new String[] {
				TEST_RESOURCE_URL + TEST_BN_FILE1,
				TEST_RESOURCE_URL + TEST_BN_FILE2,
				TEST_RESOURCE_URL + TEST_BN_FILE3
				};
	}
	
	public static String[] getLibrarys() {
		return new String[] {
				TEST_BUDDLE_ID + "/lib/org.eclipse.swt.win32.win32.x86_64_3.100.0.v4233d.jar"
		};
	}
	
	public static JavaProjectFixture getJavaProjectFixture() {
		return new JavaProjectWithLibraryFixture(ResourcesPlugin.getWorkspace(), TEST_PROJECT_NAME, getLibrarys());
	}
}
