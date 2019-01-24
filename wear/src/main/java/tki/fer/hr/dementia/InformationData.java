package tki.fer.hr.dementia;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Objects;


    public class InformationData {
    private String name;
    private String surname;
    private String location;

    public InformationData() {
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "InformationData{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", location='" + location + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InformationData that = (InformationData) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(surname, that.surname) &&
                Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, surname, location);
    }

    public void setSurname(String surname) {
        this.surname = surname;

    }

    public void setLocation(String location) {
        this.location = location;
    }

    public InformationData(String name, String surname, String location) {
        this.name = name;
        this.surname = surname;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getLocation() {
        return location;
    }
}
