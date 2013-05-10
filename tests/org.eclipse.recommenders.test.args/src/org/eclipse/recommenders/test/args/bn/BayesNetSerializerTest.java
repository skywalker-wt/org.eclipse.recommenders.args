package org.eclipse.recommenders.test.args.bn;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.recommenders.args.completion.rcp.bn.BayesNetSerializer;
import org.eclipse.recommenders.commons.bayesnet.BayesianNetwork;
import org.eclipse.recommenders.jayes.BayesNet;
import org.eclipse.recommenders.jayes.BayesNode;
import org.eclipse.recommenders.test.args.util.TestResource;
import org.junit.Test;

/**
 * With some sample bn_model file to test BayesNetSerializer.
 * 
 * @author Tong
 *
 */
public class BayesNetSerializerTest {
	
	
	/**
	 * Test case of generate BayesNet from bn_model file.
	 * @throws IOException
	 */
	@Test
	public final void testBuildBayesNet() throws IOException {
		String[] bnFiles = TestResource.getBNFiles();
		
		for (String bnFile : bnFiles) {
			assertBayesNetSerializer(bnFile);
		}
	}
	
	/**
	 * Assert BayesNetSerializer can generate BayesNet from model file. Also check
	 * the KeyNodes of the BayesNet are generated.
	 * 
	 * @param bnFile - file URL in eclipse platform
	 */
	private void assertBayesNetSerializer(final String bnFile) throws IOException {
		URL fileURL =  new URL(bnFile);
		InputStream fileInputStream = fileURL.openConnection().getInputStream();
		
		BayesianNetwork bNet = BayesNetSerializer.readBayesNet(fileInputStream);
		BayesNet net = BayesNetSerializer.readNetwork(bNet);
		assertNotNull(net);
		
		BayesNode mctxNode = net.getNode("MethodContextNode");
		BayesNode mLocalVariableNode = net.getNode("LocalVariableNode");
		BayesNode mReceiverNode = net.getNode("ReceiverNode");
		BayesNode mTopNode = net.getNode("TopNode");
		
		assertNotNull(mctxNode);
		assertNotNull(mLocalVariableNode);
		assertNotNull(mReceiverNode);
		assertNotNull(mTopNode);
	}
}
