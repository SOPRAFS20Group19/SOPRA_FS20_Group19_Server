package ch.uzh.ifi.seal.soprafs20.connector;
import ch.uzh.ifi.seal.soprafs20.database.DatabaseConnectorLocation;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.web.WebAppConfiguration;



import static org.junit.jupiter.api.Assertions.*;


@WebAppConfiguration
public class DatabaseConnectorLocationUnitTest {

    @Test
    public void generateFountainIdTest(){
        int id = DatabaseConnectorLocation.generateFountainId();
        assertTrue(id>=10000000 && id<20000000);
    }

    @Test
    public void generateRecyclingStationIdTest(){
        int id = DatabaseConnectorLocation.generateRecyclingId();
        assertTrue(id>=30000000 && id<40000000);
    }

    @Test
    public void generateFireplaceIdTest(){
        int id = DatabaseConnectorLocation.generateFireplaceId();
        assertTrue(id>=20000000 && id<30000000);
    }

    @Test
    public void generateToiletsIdTest(){
        int id = DatabaseConnectorLocation.generateToiletId();
        assertTrue(id>=40000000 && id<50000000);
    }

    @Test
    public void generateBenchIdTest(){
        int id = DatabaseConnectorLocation.generateBenchId();
        assertTrue(id>=60000000 && id<70000000);
    }

    @Test
    public void generateTableTennisIdTest(){
        int id = DatabaseConnectorLocation.generateTableTennisId();
        assertTrue(id>=50000000 && id<60000000);
    }
}

