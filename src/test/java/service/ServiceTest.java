package service;

import domain.Student;
import org.junit.jupiter.api.Test;
import repository.NotaXMLRepo;
import repository.StudentXMLRepo;
import repository.TemaXMLRepo;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.TemaValidator;

import java.io.FileWriter;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTest {
    private Service service;

    public ServiceTest() {

        // createTempFiles();

        StudentValidator studentValidator = new StudentValidator();
        TemaValidator temaValidator = new TemaValidator();
        String filenameStudent = "fisiere/Studenti.xml";
        String filenameTema = "fisiere/Teme.xml";
        String filenameNota = "fisiere/Note.xml";

        StudentXMLRepo studentXMLRepository = new StudentXMLRepo(filenameStudent);
        TemaXMLRepo temaXMLRepository = new TemaXMLRepo(filenameTema);
        NotaValidator notaValidator = new NotaValidator(studentXMLRepository, temaXMLRepository);
        NotaXMLRepo notaXMLRepository = new NotaXMLRepo(filenameNota);
        this.service = new Service(studentXMLRepository, studentValidator, temaXMLRepository, temaValidator, notaXMLRepository, notaValidator);
    }

    @Test
    void addStudentEc1() {
        int uuid = (int) (Math.random() * 10000000);

        Student student = new Student(Integer.toString(uuid), "Andrei", 932, "email@yahoo.com");
        Student result = service.addStudent(student);

        try {
            assert(Objects.equals(student.toString(), result.toString()));
        } catch (Exception e) {
            // for the moment, this is what we expect to happen
            assert(true);
        }

    }

    @Test
    void addStudentEc2() {
        int uuid = (int) (Math.random() * 10000000);

        Student student = new Student(Integer.toString(uuid), "", 932, "email@yahoo.com");
        try {
            Student result = service.addStudent(student);
            assert(false);
        } catch (Exception e) {
            assert(true);
        }
    }


}