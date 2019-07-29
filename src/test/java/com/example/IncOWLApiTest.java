package com.example;

import openllet.owlapi.OpenlletReasoner;
import openllet.owlapi.OpenlletReasonerFactory;
import org.apache.jena.ontology.OntClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.fail;

class IncOWLApiTest {

    @BeforeAll
    public static void init() throws IOException, OWLOntologyCreationException {
        IncOwlAPI.init();
    }

    @Test
    public void testWO() throws IOException, OWLOntologyCreationException {
        OWLOntology ont;
        try (InputStream ontIn = IncOwlAPI.fileWO.openStream()) {
            // NOTE: not using IncOwlAPI.manager as that would conflict the IRIs
            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
            ont = manager.loadOntologyFromOntologyDocument(ontIn);

            System.out.println("READ MODEL: " + ont.getOntologyID().getOntologyIRI());
        }

        OpenlletReasoner reasoner = OpenlletReasonerFactory.getInstance().createNonBufferingReasoner(ont);

        if (reasoner.isConsistent())
            System.out.println("consistent");
        else
            fail("inconsistent");

        printDebug(ont, reasoner);
    }

    @Test
    public void testIncU() throws IOException, OWLOntologyCreationException {
        OWLOntology ont = readOntModel(IncOwlAPI.fileU);

        OWLReasoner reasoner = OpenlletReasonerFactory.getInstance().createNonBufferingReasoner(ont);

        if (reasoner.isConsistent())
            System.out.println("consistent");
        else
            fail("inconsistent");

        printDebug(ont, reasoner);
    }

    private static void printDebug(OWLOntology ont, OWLReasoner reasoner) {
        OWLDataFactory factory = ont.getOWLOntologyManager().getOWLDataFactory();

        OWLClass CLASS_CAR = factory.getOWLClass(IRI.create(IncOwlAPI.baseParent + "#Car"));
        OWLClass CLASS_FOSSILEFUEL = factory.getOWLClass(IRI.create(IncOwlAPI.baseParent + "#FossileFuel"));
        OWLClass CLASS_PEDESTRIAN = factory.getOWLClass(IRI.create(IncOwlAPI.baseParent + "#Pedestrian"));

        System.out.println("Cars:");
        reasoner.instances(CLASS_CAR).forEach(System.out::println);

        System.out.println("FossileFuels:");
        reasoner.instances(CLASS_FOSSILEFUEL).forEach(System.out::println);

        System.out.println("Pedestrians:");
        reasoner.instances(CLASS_PEDESTRIAN).forEach(System.out::println);
    }

    public static OWLOntology readOntModel(URL file) throws IOException, OWLOntologyCreationException {
        try (InputStream ontIn = file.openStream()) {
            OWLOntology ont = IncOwlAPI.manager.loadOntologyFromOntologyDocument(ontIn);

            System.out.println("READ MODEL: " + ont.getOntologyID().getOntologyIRI());

            return ont;
        }
    }

}