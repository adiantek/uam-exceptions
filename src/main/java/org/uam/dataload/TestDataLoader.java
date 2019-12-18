package org.uam.dataload;

import org.uam.external.ExternalServiceAPI;

import javax.naming.ServiceUnavailableException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

class TestDataLoader {

    public static void main(String[] args) {
        TestDataLoader testDataLoader = new TestDataLoader();
        List<TestSubject> loaded = testDataLoader.load("src/main/resources/textDataFile.csv");
        System.out.println(loaded);
    }

    private ExternalServiceAPI externalService = new ExternalServiceAPI();

    private List<TestSubject> load(String fileName) {
        final List<TestSubject> subjects = new LinkedList<>();
        try {
            Path path = Paths.get(fileName);
            Stream<String> lines = Files.lines(path);
            mapAllToObjects(subjects, lines);
            informExternalService(subjects);
        } catch (Throwable throwable) {
            throw new RuntimeException("Problem loading test data.");
        } finally {
            System.out.println("Loading test data is finished at " + LocalDateTime.now());
            return subjects;
        }
    }

    private void mapAllToObjects(List<TestSubject> subjects, Stream<String> lines) {
        lines.map(this::build).forEach(subjects::add);
    }

    private TestSubject build(String line) {
        final String[] params = line.split(";");
        final Iterator<String> paramIterator = Arrays.asList(params).iterator();

        if (params.length != 2) {
            throw new RuntimeException("Ups, something went wrong.");
        }        return new TestSubject.Builder().withName(paramIterator.next()).withType(paramIterator.next()).build();
    }

    private void informExternalService(List<TestSubject> subjects) {
        try {
            externalService.send(subjects);
        } catch (ServiceUnavailableException e) {
            throw new RuntimeException(e);
        }
    }
}
