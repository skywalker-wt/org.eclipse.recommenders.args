package org.eclipse.recommenders.tests.args.util;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.Arrays.asList;
import static org.eclipse.recommenders.utils.Throws.throwUnhandledException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.recommenders.tests.jdt.JavaProjectFixture;

public class JavaProjectWithLibraryFixture extends JavaProjectFixture {
	
	public JavaProjectWithLibraryFixture(IWorkspace workspace, final String projectName, final String urls[]) {
		super(workspace, projectName);
		
		final IWorkspaceRunnable populate = new IWorkspaceRunnable() {
			@Override
			public void run(IProgressMonitor monitor) throws CoreException {
				IProject project = getJavaProject().getProject();
				String path = "platform:/plugin/";
				URL fileURL;
				final Set<IClasspathEntry> entries = newHashSet();
				final IClasspathEntry[] rawClasspath = getJavaProject().getRawClasspath();
                final IClasspathEntry defaultJREContainerEntry = JavaRuntime.getDefaultJREContainerEntry();
                entries.addAll(asList(rawClasspath));
                entries.add(defaultJREContainerEntry);
				try {
					for (String url : urls) {
						fileURL = new URL(path + url);
						InputStream fileInputStream = fileURL.openConnection().getInputStream();
						IFile file = project.getFile(url.hashCode() + ".jar");
						if (!file.exists())
							file.create(fileInputStream, true, null);
						
						entries.add(JavaCore.newLibraryEntry(new Path("/" + projectName + "/" + url.hashCode() + ".jar"), null, null, false));
					}
					
	                final IClasspathEntry[] entriesArray = entries.toArray(new IClasspathEntry[entries.size()]);
	                getJavaProject().setRawClasspath(entriesArray, null);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		
		try {
            workspace.run(populate, null);
        } catch (final Exception e) {
            throwUnhandledException(e);
        }
	}

}
