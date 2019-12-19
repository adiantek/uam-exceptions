package org.uam.dataload;

public class TestSubject {

    private String name;

    private String type;

    private TestSubject() {

    }

    @Override
    public String toString() {
        return "TestSubject{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    static Builder builder() {
        return new Builder();
    }

    static class Builder {

        private String name;

        private String type;

        Builder withName(String name) {
            this.name = name;
            return this;
        }

        Builder withType(String type) {
            this.type = type;
            return this;
        }

        TestSubject build() {
            TestSubject subject = new TestSubject();
            subject.name = this.name;
            subject.type = this.type;
            return subject;
        }
    }
}
