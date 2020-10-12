package UnitTest;

import Domain.PasswordGenerator;
import org.junit.Test;

import static org.junit.Assert.*;

public class PasswordGeneratorTest {

    @Test
    public void generateRandPassword() {
        assertNotNull(PasswordGenerator.generateRandPassword(6));
    }
}