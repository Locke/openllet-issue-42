package com.example;


import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class IncOwlAPI {

    public static final String baseParent = "http://www.semanticweb.org/buerger/ontologies/2019/5/untitled-ontology-365";
    public static final URL fileWO = IncOwlAPI.class.getClassLoader().getResource("inconsistency_wo_import.owl");
    public static final URL fileParent = IncOwlAPI.class.getClassLoader().getResource("inc2.owl");

    public static final String baseU = "http://www.semanticweb.org/buerger/ontologies/2019/5/untitled-ontology-366";
    public static final URL fileU = IncOwlAPI.class.getClassLoader().getResource("inc2u.owl");


    private static volatile boolean initialized = false;
    public static OWLOntologyManager manager = OWLManager.createOWLOntologyManager();


    public static synchronized void init() throws IOException, OWLOntologyCreationException {
        if (initialized) return;

        if (fileParent == null) throw new NullPointerException("unable to load fileParent");

        loadOntModelToCache(fileParent, baseParent);

        initialized = true;
    }

    private static void loadOntModelToCache(URL file, String base) throws OWLOntologyCreationException, IOException {
        try (InputStream ontIn = file.openStream()) {
            OWLOntology ont = manager.loadOntologyFromOntologyDocument(ontIn);

            System.out.println("LOAD to Cache: " + ont.getOntologyID().getOntologyIRI());
        }
    }
}
