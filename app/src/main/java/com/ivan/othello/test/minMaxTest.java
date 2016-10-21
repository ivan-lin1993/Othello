package com.ivan.othello.test;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivan Lin on 2016/10/19.
 */

public class minMaxTest {
    public Node root, node1, node2, node3, node4, node5, node6, node7, node8, node9, node10, node11;
    ArrayList<Node> tree;

    public class Node {
        public Node(int d, int v, int n, Node p) {
            this.dept = d;
            this.value = v;
            this.parent = p;
            this.num = n;
        }

        public int dept = 0;
        public int value = 0;
        public int num = 0;
        Node parent;
    }

    public minMaxTest() {
        tree = new ArrayList<Node>();
        root = new Node(0, 0, 0, null);
        tree.add(root);
        node1 = new Node(1, 3, 1, root);
        node2 = new Node(1, 5, 2, root);
        node3 = new Node(1, 7, 3, root);

        node4 = new Node(2, 6, 4, node1);
        node5 = new Node(2, 5, 5, node1);
        node6 = new Node(2, 2, 6, node1);

        node7 = new Node(2, 3, 7, node2);
        node8 = new Node(2, 5, 8, node2);

        node9 = new Node(2, 7, 9, node3);
        node10 = new Node(2, 6, 10, node3);
        node11 = new Node(2, 1, 11, node3);

    }

    private List<Node> getChild(Node node) {

        if (node.num == 0) {
            List<Node> arrayList = new ArrayList<Node>() {};
            arrayList.add(node1);
            arrayList.add(node2);
            arrayList.add(node3);
            return arrayList;
        } else if (node.num == 1) {
            List<Node> arrayList = new ArrayList<Node>() {};
            arrayList.add(node4);
            arrayList.add(node5);
            arrayList.add(node6);
            return arrayList;
        } else if (node.num == 2) {
            List<Node> arrayList = new ArrayList<Node>() {};
            arrayList.add(node7);
            arrayList.add(node8);
            return arrayList;
        } else {
            List<Node> arrayList = new ArrayList<Node>() {};
            arrayList.add(node9);
            arrayList.add(node10);
            arrayList.add(node11);
            return arrayList;
        }
    }
    public Node MinMax(Node parent, int alpha, int beta, boolean isMax) {
        Log.e("test","alpha:" +alpha+" beta:"+beta);
        if (parent.dept == 2) {
            //Log.e("test","reach end point");
            return parent;
        } else if (isMax) {
            Log.e("test","isMax");
            Node best = new Node(0,-999,0,null);
            for (Node n : getChild(parent)) {

                Node child = clone(MinMax(n, alpha, beta, false));

                Log.e("test","isMAX  child,d="+child.dept+" v="+child.value+" num="+child.num);
                Log.e("test","isMAX  best,d="+best.dept+" v="+best.value+" num="+best.num);
                if (child.value > best.value) {
                    best = clone(child);
                    Log.e("test","change");
                }
                alpha = (best.value > alpha) ? best.value : alpha;
                //Log.e("test","alpha:"+alpha+" beta:"+beta);
                if (beta <= alpha) {
                    //break;
                }
            }
            Log.e("test","best,d="+best.dept+" v="+best.value+" num="+best.num);
            return best;
        }
        else {
            Log.e("test","ismin");
            Node best = new Node(0,999,0,null);
            for (Node n : getChild(parent)) {
                //Log.e("test","node,d="+n.dept+" v="+n.value+" num="+n.num);
                Node child = clone(MinMax(n, alpha, beta, true));
                //Log.e("test","best child:"+" d:"+child.dept+" v:"+child.value+" n:"+child.num);
                if (child.value < best.value) {
                    best = clone(child);
                }
                Log.e("test","child:"+" d:"+child.dept+" v:"+child.value+" n:"+child.num);
                Log.e("test","best:"+" d:"+best.dept+" v:"+best.value+" n:"+best.num);
                beta = (best.value < beta) ? best.value : beta;
                //Log.e("test","alpha:"+alpha+" beta:"+beta);
                if (beta <= alpha) {
                    //break;
                }
            }
            Log.e("test","best,d="+best.dept+" v="+best.value+" num="+best.num);
            return best;
        }
    }
    public Node clone(Node n){
        Node node=new Node(n.dept,n.value,n.num,n.parent);
        return node;
    }
}
