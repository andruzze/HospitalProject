package test.akulov.hospital.dao;

import com.akulov.hospital.adapters.DatabaseAdapterImpl;
import com.akulov.hospital.configuration.DatabaseConfiguration;
import com.akulov.hospital.dao.entitydao.StoreDAO;
import com.akulov.hospital.model.dto.entity.StoreDTO;
import com.akulov.hospital.properties.DatabaseProperties;
import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StoreDAOTest {
    private final static String DBPATH = "src/com/akulov/hospital/application.properties";
    private static DatabaseAdapterImpl adapter;
    private static StoreDAO storeDAO;
    private static DatabaseProperties dbProps;
    private static DatabaseConfiguration dbConf;

    @BeforeAll
    public static void setup() {
        dbProps = new DatabaseProperties(DBPATH);
        dbConf = new DatabaseConfiguration(dbProps);
        adapter = new DatabaseAdapterImpl(dbConf.getConnection());
        storeDAO = new StoreDAO(adapter);
    }

    @Test
    @Order(1)
    public void testInsertSingle() {
        storeDAO.delete(1); // Очистка тестовых данных
        // Arrange
        StoreDTO store = new StoreDTO(1, 10, 20, 100, 50, 101);

        // Act
        storeDAO.insert(store);

        // Assert
        List<StoreDTO> stores = new ArrayList<>(storeDAO.get("*", Map.of("id", 1)));
        assertEquals(1, stores.size());
        assertEquals(10, stores.get(0).getDepartmentId());
        assertEquals(20, stores.get(0).getAdminostratorId());
        assertEquals(100, stores.get(0).getCapacity());
        assertEquals(50, stores.get(0).getCurrentFill());
    }

    @Test
    @Order(2)
    public void testInsertMultiple() {
        storeDAO.delete(2); // Очистка тестовых данных
        storeDAO.delete(3);

        // Arrange
        StoreDTO store1 = new StoreDTO(2, 15, 25, 200, 150, 202);
        StoreDTO store2 = new StoreDTO(3, 20, 30, 300, 250, 303);

        // Act
        storeDAO.insert(store1);
        storeDAO.insert(store2);

        // Assert
        List<StoreDTO> stores = new ArrayList<>(storeDAO.get("*", Map.of("capacity", 300)));
        assertEquals(1, stores.size());
        assertEquals(20, stores.get(0).getDepartmentId());
        assertEquals(30, stores.get(0).getAdminostratorId());
    }

    @Test
    @Order(3)
    public void testGetWithComplexCondition() {
        // Arrange
        Map<String, Object> conditions = Map.of("department_id", 20, "capacity", 300);

        // Act
        Collection<StoreDTO> stores = storeDAO.get("*", conditions);

        // Assert
        assertFalse(stores.isEmpty());
        StoreDTO store = stores.iterator().next();
        assertEquals(300, store.getCapacity());
        assertEquals(20, store.getDepartmentId());
    }

    @Test
    @Order(4)
    public void testUpdatePartialFields() {
        // Arrange
        Map<String, Object> conditions = Map.of("id", 1);
        StoreDTO updatedStore = new StoreDTO(1, null, 35, null, 70, 101); // Обновляем только adminostratorId и currentFill

        // Act
        storeDAO.update(updatedStore, conditions);

        // Assert
        List<StoreDTO> stores = new ArrayList<>(storeDAO.get("*", Map.of("id", 1)));
        assertEquals(1, stores.size());
        assertEquals(35, stores.get(0).getAdminostratorId());
        assertEquals(70, stores.get(0).getCurrentFill());
    }

    @Test
    @Order(5)
    public void testGetEmptyResult() {
        // Act
        Collection<StoreDTO> stores = storeDAO.get("*", Map.of("id", 999));

        // Assert
        assertTrue(stores.isEmpty());
    }

    @Test
    @Order(6)
    public void testDeleteNonExistingRecord() {
        // Act & Assert
        assertDoesNotThrow(() -> storeDAO.delete(999));
    }

    @Test
    @Order(7)
    public void testExceedCapacity() {
        storeDAO.delete(4);

        // Arrange
        StoreDTO store = new StoreDTO(4, 10, 40, 100, 150, 404); // current_fill > capacity

        // Act
        storeDAO.insert(store);

        // Assert
        List<StoreDTO> stores = new ArrayList<>(storeDAO.get("*", Map.of("id", 4)));
        assertEquals(1, stores.size());
        assertTrue(stores.get(0).getCurrentFill() > stores.get(0).getCapacity());
    }

    @Test
    @Order(8)
    public void testDeleteExistingRecord() {
        // Arrange
        storeDAO.delete(3);

        // Act
        Collection<StoreDTO> stores = storeDAO.get("*", Map.of("id", 3));

        // Assert
        assertTrue(stores.isEmpty());
    }
}
