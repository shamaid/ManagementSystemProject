package AcceptanceTest;

import Domain.*;
import Service.FootballManagementSystem;
import Service.UnionRepresentativeSystem;
import org.junit.Before;
import org.junit.Test;
import java.util.Date;

import static org.junit.Assert.*;

public class ScorePolicyChangeTest {


    private FootballManagementSystem system;
    private UnionRepresentativeSystem repSystem;
    private User unionRep;

    @Before
    public void init()
    {
        system = new FootballManagementSystem();
        system.systemInit(true);
        unionRep = UserFactory.getNewUnionRepresentative("union", "rep", "someLie@a.com");
        repSystem = system.getUnionRepresentativeSystem();
    }


    @Test
    public void changeScorePolicy_68()
    {

        system.dataReboot();

        repSystem.configureNewSeason(unionRep.getID(),2021,new Date(121, 4, 11));
        repSystem.configureNewLeague(unionRep.getID(),"Alufot","level1" );
        String leagueInSeason = repSystem.configureLeagueInSeason(unionRep.getID(),"Alufot","2021","PlayOnceWithEachTeamPolicy","CupScorePolicy",30);

        boolean success = repSystem.changeScorePolicy(unionRep.getID(),leagueInSeason,  "CupScorePolicy");
        assertFalse(success);

    }

    @Test
    public void changeScorePolicy_69()
    {

        repSystem.configureNewSeason(unionRep.getID(),2021,new Date(121, 4, 11));
        repSystem.configureNewLeague(unionRep.getID(),"Alufot","level1" );
        String leagueInSeason = repSystem.configureLeagueInSeason(unionRep.getID(),"Alufot","2021","PlayOnceWithEachTeamPolicy","CupScorePolicy",30);

        boolean success = repSystem.changeScorePolicy(unionRep.getID(),leagueInSeason,  "StandardScorePolicy");
        assertTrue(success);
    }
}
