package com.example;

import openllet.jena.PelletReasonerFactory;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.fail;

class IncJenaTest {

    @BeforeAll
    public static void init() throws IOException {
        IncJena.init();
    }

    @Test
    public void testWO() throws IOException {
        OntModel wo = readOntModel(IncJena.fileWO, IncJena.baseParent, IncJena.lang);

        if (wo.validate().isValid())
            System.out.println("consistent");
        else
            fail("inconsistent");

        printDebug(wo);
    }

    @Test
    public void testIncU() throws IOException {
        OntModel u = readOntModel(IncJena.fileU, IncJena.baseU, IncJena.lang);

        if (u.validate().isValid())
            System.out.println("consistent");
        else
            fail("inconsistent");

        printDebug(u);
    }

    private static void printDebug(OntModel m) {
        OntClass car = m.getOntClass(IncJena.baseParent + "#Car");
        OntClass fossileFuel = m.getOntClass(IncJena.baseParent + "#FossileFuel");
        OntClass pedestrian = m.getOntClass(IncJena.baseParent + "#Pedestrian");


        System.out.println("Cars:");
        car.listInstances().forEachRemaining(System.out::println);

        System.out.println("FossileFuels:");
        fossileFuel.listInstances().forEachRemaining(System.out::println);

        System.out.println("Pedestrians:");
        pedestrian.listInstances().forEachRemaining(System.out::println);
    }

    public static OntModel readOntModel(URL file, String base, String lang) throws IOException {
        try (InputStream ontIn = file.openStream()) {

            OntModel ontModel = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC);

            ontModel.read(ontIn, base, lang);

            System.out.println("READ MODEL: " + base);

            return ontModel;
        }
    }

}