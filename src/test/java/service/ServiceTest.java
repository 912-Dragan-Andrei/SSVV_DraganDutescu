package service;

import domain.Student;
import org.junit.jupiter.api.Test;
import repository.NotaXMLRepo;
import repository.StudentXMLRepo;
import repository.TemaXMLRepo;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.TemaValidator;
import validation.ValidationException;

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
    void addStudentWithValidData() {

        Student student = new Student("newID1323D", "Andrei", 1, "email.com");


        assertDoesNotThrow(() -> {
            Student result = service.addStudent(student);
            assertEquals(student, result);
        });
    }

    @Test
    void addStudentWithEmptyName() {

        Student student = new Student("anothe33rIDD", "", 1, "email.com");


        ValidationException exception = assertThrows(ValidationException.class, () -> {
            Student result = service.addStudent(student);
        });
        assertEquals("Nume incorect!", exception.getMessage());
    }

    @Test
    void addStudentWithEmptyID() {

        Student student = new Student("", "Andrei", 1, "email.com");


        ValidationException exception = assertThrows(ValidationException.class, () -> {
            Student result = service.addStudent(student);
        });
        assertEquals("Id incorect!", exception.getMessage());
    }

    @Test
    void addStudentWithExistingID() {

        Student student = new Student("uniq12ueID", "Andrei", 1, "email.com");


        assertNull(service.addStudent(student));
    }

    @Test
    void addStudentWithEmptyEmail() {

        Student student = new Student("uniqu332esID", "Andrei", 1, "");


        ValidationException exception = assertThrows(ValidationException.class, () -> {
            Student result = service.addStudent(student);
        });
        assertEquals("Email incorect!", exception.getMessage());
    }

    @Test
    void addStudentWithNegativeGroup() {

        Student student = new Student("uniqu123eIeD", "Andrei", -1, "email.com");


        ValidationException exception = assertThrows(ValidationException.class, () -> {
            Student result = service.addStudent(student);
        });
        assertEquals("Grupa incorecta!", exception.getMessage());
    }



    @Test
    void addStudentWithMaxIntGroupPlusOne() {

        long maxIntPlusOneGroup = (long) Integer.MAX_VALUE + 1;
        int invalidGroup = (int) maxIntPlusOneGroup;
        Student student = new Student("uniq4u3eID", "Andrei", invalidGroup, "email.com");


        ValidationException exception = assertThrows(ValidationException.class, () -> {
            Student result = service.addStudent(student);
        });
        assertEquals("Grupa incorecta!", exception.getMessage());
    }

    @Test
    void addStudentWithGroupZero() {

        Student student = new Student("uniq3uee3eeID", "Andrei", 0, "email.com");


        assertDoesNotThrow(() -> {
            Student result = service.addStudent(student);
            assertEquals(student, result);
        });
    }

    @Test
    void addStudentWithMaxIntGroup() {

        Student student = new Student("unique3233ID", "Andrei", Integer.MAX_VALUE, "email.com");


        assertDoesNotThrow(() -> {
            Student result = service.addStudent(student);
            assertEquals(student, result);
        });
    }

    @Test
    void addStudentWithMaxIntMinusOneGroup() {

        Student student = new Student("uniquw33eeID", "Andrei", Integer.MAX_VALUE - 1, "email.com");


        assertDoesNotThrow(() -> {
            Student result = service.addStudent(student);
            assertEquals(student, result);
        });
    }


}




