/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evors.rs.sim.robot.ctrnn;

import java.util.ArrayList;


/**
 *
 * @author Miles
 */
public class Layer {
        public int layerID = -1;
        //public ArrayList<Integer> IDs;
        public ArrayList<Neuron> neurons = new ArrayList<>();
        //public HashMap<Integer,ArrayList<Integer>> conns; 
        //public CTRNNParamRanges ParamRanges;
        //int n;
        
        
        public Layer() {}
        public Layer(int n,int nextID, CTRNNParamRanges ParamRanges) {
            init(n, nextID);
            //this.ParamRanges = ParamRanges;
        }
        public Layer(int n,int nextID) {
            init(n, nextID);
            //ParamRanges = new CTRNNParamRanges();
        }
        public Layer(ArrayList<Neuron> neurons) {
            if(neurons != null)
                this.neurons = (ArrayList<Neuron>)neurons.clone();
        }
        
        private void init(int n,int nextID) {
            //IDs = new ArrayList<>(n); // this.n = n;
            //conns = new HashMap<>(n);
            for(int i = 0; i < n; i++) {
                //int newID = getNextID();
                //IDs.add(newID);
                //conns.put(newID, new ArrayList<>());
                neurons.add(new Neuron(nextID + i));
            }
        }
        
        public int getTotalGeneLength() {
            int l = 0;
            for(Neuron n : neurons)
                l += 3 + n.conns.size();
            return l;
        }
        
        public void setAllForwardConnections(Layer layer) {
            for(Neuron neur : neurons)
                for(Neuron neur2 : layer.neurons)
                    neur.conns.add(neur2.ID);
        }
        public void setSelfConnections() {
            for(Neuron neur : neurons)
                neur.conns.add(neur.ID);
        }
        
        public boolean isNormalLayer() {
            return this.getClass() == Layer.class;
        }
        
        public void setRecurrentConnections() {
            for(Neuron neur : neurons)
                for(Neuron neur2 : neurons)
                    if(neur.ID != neur2.ID)
                        neur.conns.add(neur2.ID);
        }
        
        public void setAllParams(CTRNNParamRanges params) {
            for(Neuron neur : neurons)
                neur.ParamRanges = params;
        }
        
        
        
        
        
//        public ArrayList<boolean[]> getArrConnsMatrix(int totalN) {
//            ArrayList<boolean[]> r = new ArrayList<>(n);
//            for(int id : IDs)
//                r.add(getConnectionsMatrix(id,totalN));
//            return r;
//        }
        
//        public boolean[] getConnectionsMatrix(int ID, int totalN) {
//            boolean[] connections = new boolean[totalN];
//            ArrayList<Integer> tConns = conns.get(ID);
//            for(int i = 0; i < totalN; i++) {
//                connections[i] = tConns.contains(i) ;
//            }
//            return connections;
//        }

    @Override
    public String toString() {
        return "\nLayer{" + "layerID=" + layerID + ", neurons=" + neurons + '}';
    }
    }
