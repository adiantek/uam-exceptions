package org.uam.dataload;

import org.uam.exceptions.FileLoadingException;
import org.uam.external.ExternalServiceAPI;

import javax.naming.ServiceUnavailableException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class TestDataLoader {

    private static final Logger L = Logger.getLogger(TestDataLoader.class.getSimpleName());

    public static void main(String[] args) {
        TestDataLoader testDataLoader = new TestDataLoader();
        try {
            List<TestSubject> loaded = testDataLoader.load("src/main/resources/textDataFile.csv");
            L.info("Loaded data: " + loaded);
        } catch (FileLoadingException ex) {
            L.log(Level.SEVERE, "Cannot load data", ex);
        }
    }

    private ExternalServiceAPI externalService = new ExternalServiceAPI();

    private List<TestSubject> load(String fileName) throws FileLoadingException {
        List<TestSubject> subjects = new ArrayList<>();
        try {
            Path path = Paths.get(fileName);
            Stream<String> lines = Files.lines(path);
            mapAllToObjects(lines, subjects);
            informExternalService(subjects);
        } catch (ServiceUnavailableException ex) {
            L.log(Level.WARNING, "Cannot inform external service while loading " + fileName, ex);
        } catch (IOException | SubjectCreationException ex) {
            throw new FileLoadingException("Failed to load data from " + fileName, ex);
        } finally {
            L.info("Loading test data is finished at " + LocalDateTime.now());
        }
        return subjects;
    }

    private void mapAllToObjects(Stream<String> lines, List<TestSubject> subjects) throws SubjectCreationException {
        Iterator<String> it = lines.iterator();
        while (it.hasNext()) {
            TestSubject subject = build(it.next());
            subjects.add(subject);
        }
    }

    private TestSubject build(String line) throws SubjectCreationException {
        String[] params = line.split(";");
        if (params.length != 2) {
            throw new SubjectCreationException("Invalid number of semicolons in line: " + line);
        }
        Iterator<String> paramIterator = Arrays.asList(params).iterator();
        return TestSubject.builder().withName(paramIterator.next()).withType(paramIterator.next()).build();
    }

    private void informExternalService(List<TestSubject> subjects) throws ServiceUnavailableException {
        externalService.send(subjects);
    }
}
