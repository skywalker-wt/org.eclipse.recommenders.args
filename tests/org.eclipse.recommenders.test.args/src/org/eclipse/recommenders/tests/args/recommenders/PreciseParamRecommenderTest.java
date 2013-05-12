/**
 * 
 */
package org.eclipse.recommenders.tests.args.recommenders;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.recommenders.args.completion.rcp.ParameterCompletionProposalComputer;
import org.eclipse.recommenders.tests.args.util.TestResource;
import org.eclipse.recommenders.tests.jdt.JavaContentAssistContextMock;
import org.eclipse.recommenders.tests.jdt.JavaProjectFixture;
import org.eclipse.recommenders.tests.jdt.RecommendersCompletionContextFactoryMock;
import org.eclipse.recommenders.utils.Tuple;
import org.junit.Before;
import org.junit.Test;

/**
 * NOTE: the results of the test here are dependent on specific
 * models, so we may generalize the assertions to make them more
 * reusable.
 * 
 * @author ChengZhang
 *
 */
public class PreciseParamRecommenderTest {
	private JavaProjectFixture fixture;

	@Before
	public void before() {
		fixture = TestResource.getJavaProjectFixture();
	}
	
	@Test
	public void testRecommendationOfMethodInvocation_1() throws CoreException{
		List<List<ICompletionProposal>> result = 
				exercise(PreciseParamRecommenderTestCode.test1());
		
		assertTrue(result.size() == 1);
		List<ICompletionProposal> proposals = result.get(0);
		assertTrue(proposals.size() == 6);
		
		Set<String> expectResult = new HashSet<String>();
		expectResult.add("browser.isForwardEnabled() - "); 
		expectResult.add("browser.isBackEnabled() - ");
		expectResult.add("browserField.isForwardEnabled() - ");
		expectResult.add("browserField.isBackEnabled() - ");
		expectResult.add("localBrowser.isBackEnabled() - ");
		expectResult.add("localBrowser.isForwardEnabled() - ");
		
		for (int i = 0; i < result.get(0).size(); ++i) {
			assertTrue(expectResult.contains(result.get(0).get(i).getDisplayString()));
		}
	}
	
	@Test
	public void testRecommendationOfArrayAccess_1() throws CoreException{
		List<List<ICompletionProposal>> result = 
				exercise(PreciseParamRecommenderTestCode.test2());
		
		assertTrue(result.size() == 1);
		List<ICompletionProposal> proposals = result.get(0);
		assertTrue(proposals.size() == 1);
		
		Set<String> expectResult = new HashSet<String>();
		expectResult.add("images[] - "); 
		
		for (int i = 0; i < result.get(0).size(); ++i) {
			assertTrue(expectResult.contains(result.get(0).get(i).getDisplayString()));
		}
	}
	
	@Test
	public void testRecommendationOfQualifiedName_1() throws CoreException{
		List<List<ICompletionProposal>> result = 
				exercise(PreciseParamRecommenderTestCode.test3());
		
		assertTrue(result.size() == 1);
		List<ICompletionProposal> proposals = result.get(0);
		assertTrue(proposals.size() == 1);
		
		Set<String> expectResult = new HashSet<String>();
		expectResult.add("pt.x - "); 
		
		for (int i = 0; i < result.get(0).size(); ++i) {
			assertTrue(expectResult.contains(result.get(0).get(i).getDisplayString()));
		}
	}
	
	@Test
	public void testRecommendationOfTypeCast_1() throws CoreException{
		List<List<ICompletionProposal>> result = 
				exercise(PreciseParamRecommenderTestCode.test4());
		
		assertTrue(result.size() == 1);
		List<ICompletionProposal> proposals = result.get(0);
		assertTrue(proposals.size() == 1);
		
		Set<String> expectResult = new HashSet<String>();
		expectResult.add("(StyledText)canvas - "); 
		
		for (int i = 0; i < result.get(0).size(); ++i) {
			assertTrue(expectResult.contains(result.get(0).get(i).getDisplayString()));
		}
	}
	private List<List<ICompletionProposal>> exercise(CharSequence code) throws CoreException {
		List<List<ICompletionProposal>> result = new LinkedList<List<ICompletionProposal>>();
		Tuple<ICompilationUnit, Set<Integer>> struct = 
				fixture.createFileAndPackageAndParseWithMarkers(code);
	
		ICompilationUnit cu = struct.getFirst();
		Set<Integer> completionIndexs = struct.getSecond();
		
		for (Integer index : completionIndexs) {
			JavaContentAssistContextMock ctx = new JavaContentAssistContextMock(cu, index);
			ParameterCompletionProposalComputer sut  = 
					new ParameterCompletionProposalComputer(new RecommendersCompletionContextFactoryMock());
			
			sut.sessionStarted();
			sut.computeCompletionProposals(ctx, null);
			final List<ICompletionProposal> proposals = sut.computeCompletionProposals(ctx, null);
			result.add(proposals);
			
			assertTrue(proposals.size() > 0);
		}
		
		return result;
	}
}
