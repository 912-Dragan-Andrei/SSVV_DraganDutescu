package service;

import domain.Nota;
import domain.Student;
import domain.Tema;
import org.junit.jupiter.api.Test;
import repository.NotaXMLRepo;
import repository.StudentFileRepository;
import repository.StudentXMLRepo;
import repository.TemaXMLRepo;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.TemaValidator;
import validation.ValidationException;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class IntegrationTestingIncremental {
    private Service service;
    private String studentId;
    private String assignmentId;

    public IntegrationTestingIncremental() {
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

    private String generateUniqueID() {
        return UUID.randomUUID().toString();
    }

    @Test
    void testAddStudentIncremental() {
        String id = generateUniqueID();
        Student student = new Student(id, "Andrei", 1, "email.com");

        assertDoesNotThrow(() -> {
            Student result = service.addStudent(student);
            assertEquals(student, result);
        });

        studentId = id;
    }

    @Test
    void testAddAssignmentIncremental() throws ValidationException {
        testAddStudentIncremental();

        String id = generateUniqueID();
        Tema tema = new Tema(id, "descriere", 1, 2);

        assertDoesNotThrow(() -> {
            Tema result = service.addTema(tema);
            assertEquals(tema, result);
        });

        assignmentId = id;

    }

    @Test
    void testAddGradeIncremental() throws ValidationException {
        testAddAssignmentIncremental();


        String id = generateUniqueID();
        String data = "2024-01-01";
        String[] date = data.split("-");

        LocalDate dataPredare = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        Nota notaCatalog = new Nota(id, studentId, assignmentId, 10, dataPredare);

        assertDoesNotThrow(() -> {
            double result = service.addNota(notaCatalog, "feedback");
            assertEquals(notaCatalog.getNota(), result);
        });
    }


}


class IntegrationTestingBigBang  {
    private Service service;
    public IntegrationTestingBigBang()
    {
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
    private String generateUniqueID() {
        return UUID.randomUUID().toString();
    }



    @Test
    void testAddTemaBigBang() throws ValidationException {
        String id = generateUniqueID();

        Tema tema = new Tema(id, "descriere", 3, 2);

        assertDoesNotThrow(() -> {
            Tema result = service.addTema(tema);
            assertEquals(tema, result);
        });
    }

    @Test
    void addStudentWithValidDataBigBang() {
        String id = generateUniqueID();

        Student student = new Student(id, "Andrei", 1, "email.com");


        assertDoesNotThrow(() -> {
            Student result = service.addStudent(student);
            assertEquals(student, result);
        });
    }

    @Test
    void testAddNotaBigBang() throws ValidationException {

        String idTema = generateUniqueID();

        Tema tema = new Tema(idTema, "descriere", 1, 2);
        service.addTema(tema);

        String idStud = generateUniqueID();
        Student student = new Student(idStud, "Andrei", 1, "email.com");
        service.addStudent(student);

        String id = generateUniqueID();
        String data = "2024-01-01";
        String[] date = data.split("-");


        LocalDate dataPredare = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        Nota notaCatalog = new Nota(id, idStud, idTema, 10, dataPredare);

        assertDoesNotThrow(() -> {
            double result = service.addNota(notaCatalog,"feedback");
            assertEquals(notaCatalog.getNota(), result);
        });
    }

    @Test
    void testBigBangIntegration(){
        addStudentWithValidDataBigBang();
        testAddTemaBigBang();
        testAddNotaBigBang();
    }

}

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
        String id = generateUniqueID();
        Student student = new Student(id, "Andrei", 1, "email.com");


        assertDoesNotThrow(() -> {
            Student result = service.addStudent(student);
            assertEquals(student, result);
        });
    }

    @Test
    void addStudentWithEmptyName() {
        String id = generateUniqueID();
        Student student = new Student(id, "", 1, "email.com");


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
        String id = generateUniqueID();
        Student student = new Student(id, "Andrei", 1, "email.com");
        service.addStudent(student);

        assertNull(service.addStudent(student));
    }

    @Test
    void addStudentWithEmptyEmail() {
        String id = generateUniqueID();
        Student student = new Student(id, "Andrei", 1, "");


        ValidationException exception = assertThrows(ValidationException.class, () -> {
            Student result = service.addStudent(student);
        });
        assertEquals("Email incorect!", exception.getMessage());
    }

    @Test
    void addStudentWithNegativeGroup() {
        String id = generateUniqueID();
        Student student = new Student(id, "Andrei", -1, "email.com");


        ValidationException exception = assertThrows(ValidationException.class, () -> {
            Student result = service.addStudent(student);
        });
        assertEquals("Grupa incorecta!", exception.getMessage());
    }



    @Test
    void addStudentWithMaxIntGroupPlusOne() {
        String id = generateUniqueID();
        long maxIntPlusOneGroup = (long) Integer.MAX_VALUE + 1;
        int invalidGroup = (int) maxIntPlusOneGroup;
        Student student = new Student(id, "Andrei", invalidGroup, "email.com");


        ValidationException exception = assertThrows(ValidationException.class, () -> {
            Student result = service.addStudent(student);
        });
        assertEquals("Grupa incorecta!", exception.getMessage());
    }

    @Test
    void addStudentWithGroupZero() {
        String id = generateUniqueID();
        Student student = new Student(id, "Andrei", 0, "email.com");


        assertDoesNotThrow(() -> {
            Student result = service.addStudent(student);
            assertEquals(student, result);
        });
    }

    @Test
    void addStudentWithMaxIntGroup() {
        String id = generateUniqueID();
        Student student = new Student(id, "Andrei", Integer.MAX_VALUE, "email.com");


        assertDoesNotThrow(() -> {
            Student result = service.addStudent(student);
            assertEquals(student, result);
        });
    }

    @Test
    void addStudentWithMaxIntMinusOneGroup() {
        String id = generateUniqueID();
        Student student = new Student(id, "Andrei", Integer.MAX_VALUE - 1, "email.com");


        assertDoesNotThrow(() -> {
            Student result = service.addStudent(student);
            assertEquals(student, result);
        });
    }

    @Test
    void testAddTemaWithInvalidNrTema() {
        Tema tema = new Tema("", "", 0, 0);
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            service.addTema(tema);
        });
        assertEquals("Numar tema invalid!", exception.getMessage());
    }

    @Test
    void testAddTemaWithInvalidDescriere() {
        String id = generateUniqueID();
        Tema tema = new Tema(id, "", 0, 0);
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            service.addTema(tema);
        });
        assertEquals("Descriere invalida!", exception.getMessage());
    }

    @Test
    void testAddTemaWithInvalidDeadline() {
        String id = generateUniqueID();
        Tema tema = new Tema(id, "descriere", 0, 0);
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            service.addTema(tema);
        });
        assertEquals("Deadlineul trebuie sa fie intre 1-14.", exception.getMessage());
    }

    @Test
    void testAddTemaWithInvalidPrimire() {
        String id = generateUniqueID();
        Tema tema = new Tema(id, "descriere", 3, 0);
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            service.addTema(tema);
        });
        assertEquals("Saptamana primirii trebuie sa fie intre 1-14.", exception.getMessage());
    }

    @Test
    void testAddTema() throws ValidationException {
        String id = generateUniqueID();
        Tema tema = new Tema(id, "descriere", 3, 2);

        assertDoesNotThrow(() -> {
            Tema result = service.addTema(tema);
            assertEquals(tema, result);
        });
    }









    private String generateUniqueID() {
        return UUID.randomUUID().toString();
    }


}

