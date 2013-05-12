package org.eclipse.recommenders.tests.args.rcp;

import static org.junit.Assert.assertTrue;

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

public class ParameterCompletionProposalComputerTest {
	private JavaProjectFixture fixture;

	@Before
	public void before() {
		fixture = TestResource.getJavaProjectFixture();
	}
	
	@Test
	public void testParameterCompletionProposalComputer_1() throws CoreException{
		List<List<ICompletionProposal>> result = exercise(ParameterCompletionProposalComputerTestCode.test1());
		
		assertTrue(result.size() == 1);
		List<String> expectResult = new LinkedList<String>();
		expectResult.add("browser.isForwardEnabled() - ");
		expectResult.add("browser.isBackEnabled() - ");
		
		for (int i = 0; i < result.get(0).size(); ++i) {
			assertTrue(expectResult.get(i).equals(result.get(0).get(i).getDisplayString()));
		}
	}
	
	@Test
	public void testParameterCompletionProposalComputer_2() throws CoreException{
		List<List<ICompletionProposal>> result = exercise(ParameterCompletionProposalComputerTestCode.test2());
		
		assertTrue(result.size() > 0);
	}
	
	@Test
	public void testParameterCompletionProposalComputer_3() throws CoreException{
		List<List<ICompletionProposal>> result = exercise(ParameterCompletionProposalComputerTestCode.test3());
		
		assertTrue(result.size() > 0);
	}
	
	@Test
	public void testParameterCompletionProposalComputer_4() throws CoreException{
		List<List<ICompletionProposal>> result = exercise(ParameterCompletionProposalComputerTestCode.test4());
		
		assertTrue(result.size() > 0);
	}
	
	@Test
	public void testParameterCompletionProposalComputer_5() throws CoreException{
		List<List<ICompletionProposal>> result = exercise(ParameterCompletionProposalComputerTestCode.test5());
		
		assertTrue(result.size() > 0);
	}
	
	@Test
	public void testParameterCompletionProposalComputer_6() throws CoreException{
		List<List<ICompletionProposal>> result = exercise(ParameterCompletionProposalComputerTestCode.test6());
		
		assertTrue(result.size() > 0);
	}
	
	@Test
	public void testParameterCompletionProposalComputer_7() throws CoreException{
		List<List<ICompletionProposal>> result = exercise(ParameterCompletionProposalComputerTestCode.test7());
		
		assertTrue(result.size() > 0);
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
			
//			assertTrue(proposals.size() > 0);
		}
		
		return result;
	}
}
