package test.akulov.hospital.dao;

import com.akulov.hospital.adapters.DatabaseAdapterImpl;
import com.akulov.hospital.configuration.DatabaseConfiguration;
import com.akulov.hospital.dao.entitydao.DrugDAO;
import com.akulov.hospital.model.dto.entity.DrugDTO;
import com.akulov.hospital.properties.DatabaseProperties;
import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DrugDAOTest {
    private final static String DBPATH = "src/com/akulov/hospital/application.properties";
    private static DatabaseAdapterImpl adapter;
    private static DrugDAO drugDAO;
    private static DatabaseProperties dbProps;
    private static DatabaseConfiguration dbConf;

    @BeforeAll
    public static void setup() {
        dbProps = new DatabaseProperties(DBPATH);
        dbConf = new DatabaseConfiguration(dbProps);
        adapter = new DatabaseAdapterImpl(dbConf.getConnection());
        drugDAO = new DrugDAO(adapter);
    }

    @Test
    @Order(1)
    public void testInsertSingle() {
        drugDAO.delete(); // Очистка тестовых данных
        // Arrange
        DrugDTO drug = new DrugDTO(1, "Paracetamol", "Tablet", "500mg", "Supplier A", 36, "Pain relief medication");

        // Act
        drugDAO.insert(drug);

        // Assert
        List<DrugDTO> drugs = new ArrayList<>(drugDAO.get("*", Map.of("name", "Paracetamol")));

        assertEquals("Paracetamol", drugs.get(0).getName());
        assertEquals("Tablet", drugs.get(0).getReleaseForm());
        assertEquals("500mg", drugs.get(0).getDose());
        assertEquals("Supplier A", drugs.get(0).getSupplier());
        assertEquals(36, drugs.get(0).getShelfLife());
        assertEquals("Pain relief medication", drugs.get(0).getDescription());
    }

    @Test
    @Order(2)
    public void testInsertMultiple() {
        drugDAO.delete(); // Очистка тестовых данных


        // Arrange
        DrugDTO drug1 = new DrugDTO(null, "Ibuprofen", "Capsule", "200mg", "Supplier B", 24, "Anti-inflammatory drug");
        DrugDTO drug2 = new DrugDTO(null, "Amoxicillin", "Syrup", "250mg/5ml", "Supplier C", 12, "Antibiotic");

        // Act
        drugDAO.insert(drug1);
        drugDAO.insert(drug2);

        // Assert
        List<DrugDTO> drugs = new ArrayList<>(drugDAO.get("*", Map.of("name", "Amoxicillin")));
        assertEquals(1, drugs.size());
        assertEquals("Syrup", drugs.get(0).getReleaseForm());
        assertEquals("250mg/5ml", drugs.get(0).getDose());
        assertEquals("Supplier C", drugs.get(0).getSupplier());
    }

    @Test
    @Order(3)
    public void testGetWithComplexCondition() {
        // Arrange
        Map<String, Object> conditions = Map.of("supplier", "Supplier B", "dose", "200mg");

        // Act
        Collection<DrugDTO> drugs = drugDAO.get("*", conditions);

        // Assert
        assertFalse(drugs.isEmpty());
        DrugDTO drug = drugs.iterator().next();
        assertEquals("Ibuprofen", drug.getName());
        assertEquals("Capsule", drug.getReleaseForm());
    }

    @Test
    @Order(4)
    public void testUpdatePartialFields() {
        // Arrange


        Map<String, Object> conditions = Map.of("name", "Ibuprofen");
        DrugDTO updatedDrug = new DrugDTO(1, "Ibuprofen", "Capsule", "750mg", "Supplier B", 24, "Updated description");

        // Act
        drugDAO.update(updatedDrug, conditions);

        // Assert
        List<DrugDTO> drugs = new ArrayList<>(drugDAO.get("*", Map.of("id", 1)));
        assertEquals(1, drugs.size());
        assertEquals("750mg", drugs.get(0).getDose());
        assertEquals("Updated description", drugs.get(0).getDescription());
    }

    @Test
    @Order(5)
    public void testGetEmptyResult() {
        // Act
        Collection<DrugDTO> drugs = drugDAO.get("*", Map.of("id", 999));

        // Assert
        assertTrue(drugs.isEmpty());
    }

    @Test
    @Order(6)
    public void testDeleteNonExistingRecord() {
        // Act & Assert
        assertDoesNotThrow(() -> drugDAO.delete(999));
    }

    @Test
    @Order(7)
    public void testExceedShelfLife() {

        // Arrange
        DrugDTO drug = new DrugDTO(4, "ExpiredDrug", "Powder", "10g", "Supplier D", 0, "Expired medication");

        // Act
        drugDAO.insert(drug);

        // Assert
        List<DrugDTO> drugs = new ArrayList<>(drugDAO.get("*", Map.of("name", "ExpiredDrug")));
        assertEquals(1, drugs.size());
        assertTrue(drugs.get(0).getShelfLife() <= 0);
    }

    @Test
    @Order(8)
    public void testDeleteExistingRecord() {
        // Arrange
        drugDAO.delete(3);

        // Act
        Collection<DrugDTO> drugs = drugDAO.get("*", Map.of("id", 3));

        // Assert
        assertTrue(drugs.isEmpty());
    }

    @Test
    @Order(9)
    public void testGetAll(){
        Collection<DrugDTO> drugs = drugDAO.get("*", Map.of());
        assertTrue(!drugs.isEmpty());
    }
}
