package Data;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class DataAccessTest {
    DataAccess dao;
    @Before
    public void init(){
        dao = new DataAccess();

        // Add Cell
        dao.addCell("Complaints", "123456", "5.01.2001", "true", "text text text", "ididid");

        // Add value to existing cell's value
        dao.addToExistingCellValue("Complaints", "Description", "123456", "GG");

        // Get Cell specific value
        String val = dao.getCellValue("Complaints", "Description", "12345");

        // Get Cell values (whole row)
        List<String> res = dao.getAllCellValues("Complaints", "12345");

        // Update cell to new value
        dao.updateCellValue("Complaints", "Description", "12345", "new Value");

        //Get all field values
        List<String> usersIds = dao.getAllFieldValues("Users", "ID");

        //Delete ID from all tables
        boolean success = dao.deleteIdFromAllTables("C240");

    }
    @Test
    public void getAllTableValues() {
    }

    @Test
    public void getAllCellValues() {
    }

    @Test
    public void getCellValue() {
    }

    @Test
    public void addToExistingCellValue() {
    }

    @Test
    public void addCell() {
    }

    @Test
    public void isDouble() {
    }

    @Test
    public void isInt() {
    }

    @Test
    public void isDate() {
    }

    @Test
    public void isBoolean() {
    }

    @Test
    public void updateCellValue() {
    }

    @Test
    public void stringToBoolean() {
    }

    @Test
    public void createDB() {
        dao.createDB();
    }


}