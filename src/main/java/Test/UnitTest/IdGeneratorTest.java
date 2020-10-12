package UnitTest;

import Domain.IdGenerator;
import org.junit.Test;

import static org.junit.Assert.*;

public class IdGeneratorTest {

    @Test
    public void getNewId() {
        assertNotEquals(-1,IdGenerator.getNewId());
    }
}