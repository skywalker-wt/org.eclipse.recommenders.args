/**
 * Copyright (c) 2011, 2012 Shanghai Jiao Tong University.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cheng Zhang - initial API and implementation.
 */
package org.eclipse.recommenders.args.completion.rcp.bn;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.recommenders.commons.bayesnet.BayesianNetwork;
import org.eclipse.recommenders.commons.bayesnet.Node;
import org.eclipse.recommenders.jayes.BayesNet;
import org.eclipse.recommenders.jayes.BayesNode;

/**
 * This class is the utility class of file operations for Bayes Network model.
 * Store and read model from file.
 * 
 * @author ChengZhang
 *
 */
public final class BayesNetSerializer {
	
	/**
	 * This is a utility class.
	 */
	private BayesNetSerializer() { }
	
	/**
	 * Export the BayesNet model into file. 
	 * Note this method may not used for recommend but for backup.
	 * 
	 * @param net - the BayesNet model
	 * @param path - the path of the new file.
	 */
	public static void storeBayesNet(final BayesNet net, final String path) {
		BayesianNetwork bNet = createNetwork(net);
		try {
			FileOutputStream out = new FileOutputStream(path);
			BayesianNetwork.write(bNet, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Build the BayesianNetwork from InputStream.
	 * @param inputStream - the input stream of the model file. 
	 * @return the reference of BayesianNetwork.
	 */
	public static BayesianNetwork readBayesNet(final InputStream inputStream) {
		BayesianNetwork networkin = null;
		try {
//			FileInputStream in = new FileInputStream(inputStream);
			networkin = BayesianNetwork.read(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
    	return networkin;
	}
	
	/**
	 * Build the BayesianNetwork from file.
	 * @param path - the path of the model file.
	 * @return the reference of BayesianNetwork.
	 */
	public static BayesianNetwork readBayesNet(final String path) {
		BayesianNetwork networkin = null;
		try {
			FileInputStream in = new FileInputStream(path);
			networkin = BayesianNetwork.read(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
    	return networkin;
	}
	
	/**
	 * This method transfer the BayesNet to BayesianNetwork.
	 * 
	 * Note BayesianNetwork and BayesNet almost the same here. 
	 * But BayesianNetwork is used for store and  BayesNet are
	 * used for handle the model.
	 * 
	 * @param net - the original input.
	 * @param network - the BayesianNetwork would get the copy of net.
	 */
    private static void initializeNodes(final BayesNet net, final BayesianNetwork network) {
        final Collection<BayesNode> nodes = net.getNodes();
        for (final BayesNode node : nodes) {
            final Node noden = new Node(node.getName());
            String[] a = new String[node.getOutcomeCount()];
            for (int i = 0; i < node.getOutcomeCount(); i++) {
				a[i] = node.getOutcomeName(i);
            }
            noden.setStates(a);
            network.addNode(noden);
        }
    }

    /**
     * This method would init the relations in BayesianNetwork from BayesNet.
     * 
     * @param net - the original input.
     * @param network - the BayesianNetwork would get the copy of net.
     */
   private static void initializeArcs(final BayesNet net, final BayesianNetwork network) {
        final Collection<BayesNode> nodes = net.getNodes();
        for (final BayesNode node : nodes) {
            final List<BayesNode> parents = node.getParents();
            final Node children = network.getNode(node.getName());
            final LinkedList<Node> bnParents = new LinkedList<Node>();
            for (int i = 0; i < parents.size(); i++) {
            	bnParents.add(network.getNode(parents.get(i).getName()));
            }
            Node[] b = bnParents.toArray(new Node[0]);
            children.setParents(b);
        }
    }

   /**
    * This method would init the probabilities in BayesianNetwork from BayesNet.
    * 
    * @param net - the original input.
    * @param network - the BayesianNetwork would get the copy of net.
    */
    private static void initializeProbabilities(
    		final BayesNet net, 
    		final BayesianNetwork network) {
        final Collection<BayesNode> nodes = net.getNodes();
        for (final BayesNode node : nodes) {
            final Node noden = network.getNode(node.getName());
            noden.setProbabilities(null); //node.getFactor().getValues());
        }
    }
    
    /**
     * To create a Serializable BayesianNetwork Object from BayesNet.
     * 
     * @param bayesnet - the original input.
     * @return the reference of BayesianNetwork Object.
     */
    public static BayesianNetwork createNetwork(final BayesNet bayesnet) {
    	BayesianNetwork network = new BayesianNetwork();
    	initializeNodes(bayesnet, network);
    	initializeArcs(bayesnet, network);
    	initializeProbabilities(bayesnet, network);
    	return network;
    }
    
    /**
     * To create a BayesNet Object from BayesianNetwork.
     * 
     * @param network - the original input.
     * @return the reference of BayesNet Object.
     */
    public static BayesNet readNetwork(final BayesianNetwork network) {
    	BayesNet bayesnet = new BayesNet();
        readNodes(bayesnet, network);
        readArcs(bayesnet, network);
        readProbabilities(bayesnet, network);
        return bayesnet;
    }

    /**
     * Init nodes in bayesNet from BayesianNetwork.
     * 
     * @param bayesNet - the BayesNet object would get the copy of network.
     * @param network - the original input.
     */
    private static void readNodes(final BayesNet bayesNet, final BayesianNetwork network) {
        final Collection<Node> nodes = network.getNodes();
        for (final Node node : nodes) {
            final BayesNode bayesNode = new BayesNode(node.getIdentifier());
            final String[] states = node.getStates();
            for (int i = 0; i < states.length; i++) {
                bayesNode.addOutcome(states[i]);
            }
            bayesNet.addNode(bayesNode);
        }
    }

    /**
     * Init relations of nodes in bayesNet from BayesianNetwork.
     * 
     * @param bayesNet - the BayesNet object would get the copy of network.
     * @param network - the original input.
     */
    private static void readArcs(final BayesNet bayesNet, final BayesianNetwork network) {
        final Collection<Node> nodes = network.getNodes();
        for (final Node node : nodes) {
            final Node[] parents = node.getParents();
            final BayesNode children = bayesNet.getNode(node.getIdentifier());
            final LinkedList<BayesNode> bnParents = new LinkedList<BayesNode>();
            for (int i = 0; i < parents.length; i++) {
                bnParents.add(bayesNet.getNode(parents[i].getIdentifier()));
            }
            children.setParents(bnParents);
        }
    }

    /**
     * Init the probabilities of nodes in bayesNet from BayesianNetwork.
     * 
     * @param bayesNet - the BayesNet object would get the copy of network.
     * @param network - the original input.
     */
    private static void readProbabilities(final BayesNet bayesNet, final BayesianNetwork network) {
        final Collection<Node> nodes = network.getNodes();
        for (final Node node : nodes) {
            final BayesNode bayesNode = bayesNet.getNode(node.getIdentifier());
            bayesNode.setProbabilities(node.getProbabilities());
        }
    }
}
