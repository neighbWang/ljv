package org.atpfivt.ljv;

import org.approvaltests.Approvals;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

public class LJVTest {

    @Test
    void stringIsNotAPrimitiveType() {
        String actualGraph = new LJV().drawGraph("Hello");
        Approvals.verify(actualGraph);
    }

    @Test
    void objectArraysHoldReferencesPrimitiveArraysHoldValues() {
        String actual_graph = new LJV()
                .setTreatAsPrimitive(String.class)
                .setIgnorePrivateFields(false)
                .drawGraph(
                        new Object[]{new String[]{"a", "b", "c"}, new int[]{1, 2, 3}}
                );
        Approvals.verify(actual_graph);
    }

    @Test
    void assignmentDoesNotCreateANewObject() {
        String x = "Hello";
        String y = x;
        String actual_graph = new LJV().drawGraph(new Object[]{x, y});
        Approvals.verify(actual_graph);
    }

    @Test
    void assignmentWithNewCreateANewObject() {
        String x = "Hello";
        String y = new String(x);
        String actual_graph = new LJV().drawGraph(new Object[]{x, y});
        Approvals.verify(actual_graph);
    }

    @Test
    void stringIntern() {
        String x = "Hello";
        String y = "Hello";
        String actual_graph = new LJV().drawGraph(new Object[]{x, y.intern()});
        Approvals.verify(actual_graph);
    }

    @Test
    void multiDimensionalArrays() {
        String actual_graph = new LJV().drawGraph(new int[4][5]);
        Approvals.verify(actual_graph);
    }

    @Test
    void reversedMultiDimensionalArrays() {
        String actual_graph = new LJV().setDirection(Direction.LR).drawGraph(new int[4][5]);
        Approvals.verify(actual_graph);
    }

    @Test
    void cyclicalStructuresClassesWithAndWithoutAToStringAndWithoutContext() {
        Node n1 = new Node("A");
        n1.level = 1;
        AnotherNode n2 = new AnotherNode("B");
        n2.level = 2;
        AnotherNode n3 = new AnotherNode("C");
        n3.level = 2;

        n1.left = n2;
        n1.right = n3;
        n1.right.left = n1;
        n1.right.right = n1;
        String actual_graph = new LJV()
                .addFieldAttribute("left", "color=red,fontcolor=red")
                .addFieldAttribute("right", "color=blue,fontcolor=blue")
                .addClassAttribute(Node.class, "color=pink,style=filled")
                .addIgnoreField("level")
                .setTreatAsPrimitive(String.class)
                .setShowFieldNamesInLabels(false)
                .drawGraph(n1);
        Approvals.verify(actual_graph);
    }

    @Test
    void paulsExample() {
        ArrayList<Object> a = new ArrayList<>();
        a.add(new Person("Albert", Gender.MALE, 35));
        a.add(new Person("Betty", Gender.FEMALE, 20));
        a.add(new java.awt.Point(100, -100));

        String actual_graph = new LJV()
                .setTreatAsPrimitive(String.class)
                .setTreatAsPrimitive(Gender.class)
                .addIgnoreField("hash")
                .addIgnoreField("count")
                .addIgnoreField("offset")
                .drawGraph(a);
        Approvals.verify(actual_graph);
    }

    @Test
    void testNull() {
        String actualGraph = new LJV().drawGraph(null);
        Approvals.verify(actualGraph);
    }

    @Test
    void treeMap() {
        TreeMap<String, Integer> map = new TreeMap<>();

        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        map.put("four", 4);

        String actualGraph = new LJV()
                .setTreatAsPrimitive(Integer.class)
                .setTreatAsPrimitive(String.class)
                .drawGraph(map);
        Approvals.verify(actualGraph);
    }

    @Test
    void linkedHashMap() {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        map.put("four", 4);

        String actual_graph = new LJV()
                .setTreatAsPrimitive(String.class)
                .setTreatAsPrimitive(Integer.class)
                .setIgnoreNullValuedFields(true)
                .drawGraph(map);

        Approvals.verify(actual_graph);
    }

    @Test
    void hashMap() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        map.put("four", 4);

        String actual_graph = new LJV()
                .setTreatAsPrimitive(String.class)
                .setTreatAsPrimitive(Integer.class)
                .setIgnoreNullValuedFields(true)
                .drawGraph(map);
        Approvals.verify(actual_graph);
    }

    @Test
    void hashMapCollision2() {
        List<String> collisionString = new HashCodeCollision().genCollisionString(3);
        HashMap<String, Integer> map = new HashMap<>();

        for (int i = 0; i < collisionString.size(); i++) {
            map.put(collisionString.get(i), i);
        }

        String actual_graph = new LJV()
                .setTreatAsPrimitive(String.class)
                .setTreatAsPrimitive(Integer.class)
                .drawGraph(map);

        Approvals.verify(actual_graph);
    }


    @Test
    void wrappedObjects() {
        String actual_graph = new LJV().drawGraph(new Example());
        Approvals.verify(actual_graph);
    }

    @Test
    void linkedList() {
        LinkedList<Integer> linkedList = new LinkedList<>();
        linkedList.add(1);
        linkedList.add(42);
        linkedList.add(21);

        String actual_graph = new LJV()
                .setTreatAsPrimitive(Integer.class)
                .addFieldAttribute("next", "color=red,fontcolor=red")
                .addFieldAttribute("prev", "color=blue,fontcolor=blue")
                .addFieldAttribute("first", "color=red,fontcolor=red")
                .addFieldAttribute("last", "color=red,fontcolor=red")
                .drawGraph(linkedList);
        Approvals.verify(actual_graph);
    }

}
