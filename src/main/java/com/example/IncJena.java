package com.example;

import openllet.jena.PelletReasonerFactory;
import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;
import org.apache.jena.util.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class IncJena {

    public static final String lang = FileUtils.langXML;


    public static final String baseParent = "http://www.semanticweb.org/buerger/ontologies/2019/5/untitled-ontology-365";
    public static final URL fileWO = IncJena.class.getClassLoader().getResource("inconsistency_wo_import.owl");
    public static final URL fileParent = IncJena.class.getClassLoader().getResource("inc2.owl");

    public static final String baseU = "http://www.semanticweb.org/buerger/ontologies/2019/5/untitled-ontology-366";
    public static final URL fileU = IncJena.class.getClassLoader().getResource("inc2u.owl");


    private static volatile boolean initialized = false;

    public static final OntDocumentManager dm = OntDocumentManager.getInstance();

    public static synchronized void init() throws IOException {
        if (initialized) return;

        if (fileParent == null) throw new NullPointerException("unable to load fileParent");

        FileManager fm = FileManager.get();
        fm.setModelCaching(true);

        dm.setFileManager(fm);

        loadOntModelToCache(fileParent, baseParent, lang);

        initialized = true;
    }

    private static void loadOntModelToCache(URL file, String base, String lang) throws IOException {
        try (InputStream ontIn = file.openStream()) {
            OntModel ontModel = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC);

            ontModel.read(ontIn, base, lang);

            System.out.println("LOAD to Cache: " + base);

            dm.addModel(base, ontModel);
        }
    }
}
