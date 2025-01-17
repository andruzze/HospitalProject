package test.akulov.hospital.dao;


import com.akulov.hospital.adapters.DatabaseAdapterImpl;
import com.akulov.hospital.configuration.DatabaseConfiguration;
import com.akulov.hospital.dao.entitydao.PatientDAO;
import com.akulov.hospital.model.dto.entity.PatientDTO;
import com.akulov.hospital.model.dto.types.FullName;
import com.akulov.hospital.model.dto.types.Passport;
import com.akulov.hospital.properties.DatabaseProperties;
import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PatientDAOTest {
    private final static String DBPATH = "src/com/akulov/hospital/application.properties";
    private static DatabaseAdapterImpl adapter;
    private static PatientDAO patientDAO;
    private static DatabaseProperties dbProps;
    private static DatabaseConfiguration dbConf;

    @BeforeAll
    public static void setup() {
        dbProps = new DatabaseProperties(DBPATH);
        dbConf = new DatabaseConfiguration(dbProps);
        adapter = new DatabaseAdapterImpl(dbConf.getConnection());
        patientDAO = new PatientDAO(adapter);
    }

    @Test
    @Order(1)
    public void testInsert() {
        patientDAO.delete(1);
        // Arrange
        PatientDTO patient = new PatientDTO(
                1,
                new FullName("Ivanov", "Ivan", "Ivanovich"),
                30,
                new Passport(1234, 567890),
                "+123456789"
        );

        // Act
        patientDAO.insert(patient);

        // Assert
        List<PatientDTO> patients = new ArrayList<>(
                patientDAO.get("*", Map.of("id", 1))
        );
        assertEquals(1, patients.size());
        assertEquals("'Ivan'", patients.get(0).getFullName().getFirstName());
    }

    @Test
    @Order(2)
    public void testGet() {
        // Act
        Collection<PatientDTO> patients = patientDAO.get("*", Map.of("id", 1));

        // Assert
        assertFalse(patients.isEmpty());
        PatientDTO patient = patients.iterator().next();
        assertEquals("'Ivan'", patient.getFullName().getFirstName());
        assertEquals(30, patient.getAge());
    }

    @Test
    @Order(3)
    public void testUpdate() {
        // Arrange
        PatientDTO updatedPatient = new PatientDTO(
                1,
                new FullName("Petrov", "Ivan", "Ivanovich"),
                31,
                new Passport(1234, 567890),
                "+987654321"
        );
        Map<String, Object> conditions = Map.of("id", 1);

        // Act
        patientDAO.update(updatedPatient, conditions);

        // Assert
        List<PatientDTO> patients = new ArrayList<>(
                patientDAO.get("*", Map.of("id", 1))
        );
        assertEquals(1, patients.size());
        assertEquals("'Petrov'", patients.get(0).getFullName().getLastName());
        assertEquals(31, patients.get(0).getAge());
    }

    @Test
    @Order(4)
    public void testDelete() {
        // Act
        patientDAO.delete(1);

        // Assert
        Collection<PatientDTO> patients = patientDAO.get("*", Map.of("id", 1));
        assertTrue(patients.isEmpty());
    }
}
