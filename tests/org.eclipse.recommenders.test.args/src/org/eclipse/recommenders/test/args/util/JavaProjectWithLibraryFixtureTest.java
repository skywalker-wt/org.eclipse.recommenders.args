package org.eclipse.recommenders.test.args.util;

import static org.junit.Assert.assertTrue;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.recommenders.tests.jdt.JavaProjectFixture;
import org.junit.Before;
import org.junit.Test;

public class JavaProjectWithLibraryFixtureTest {
	private JavaProjectFixture fixture;

	@Before
	public void before() {
		fixture = TestResource.getJavaProjectFixture();
	}
	
	@Test
	public void JavaProjectWithLibraryFixture() throws JavaModelException{
		IProject project = fixture.getJavaProject().getProject();
		
		IClasspathEntry[] classEntries = fixture.getJavaProject().getRawClasspath();
		assertTrue(classEntries.length > 2);
		for (IClasspathEntry entry : classEntries){
			if (entry.getEntryKind() == IClasspathEntry.CPE_LIBRARY) {
				IPath path  = entry.getPath();
				IFile file = project.getWorkspace().getRoot().getFile(path);
				assertTrue(file.exists());
			}
		}
	}
}
