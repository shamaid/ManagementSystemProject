package AcceptanceTest;

import Service.FootballManagementSystem;
import org.junit.Test;

import static org.junit.Assert.*;

public class SystemInitiationTest {

    @Test
    public void systemInitFail_1()
    {
        FootballManagementSystem system = new FootballManagementSystem();
        system.systemInit(true);

        boolean connect = system.connectToOuterSystems();

        assertFalse(connect);

    }

    @Test
    public void systemInitSuccess_2()
    {
        FootballManagementSystem system = new FootballManagementSystem();
        boolean init = system.systemInit(true);

        assertTrue(init);

    }

}
