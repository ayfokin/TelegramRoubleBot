import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

public class DataParserTest {
    private final Bot Instance = new Bot();
    private static final Logger logger = LogManager.getLogger();

    @Test
    public void test_getString() {
        Instance.initializeData();
        logger.info("Data initialized successfully");

        Assert.assertEquals("Москва", DataParser.getCity("московский", Instance.cities));
        Assert.assertEquals("Великий Новгород", DataParser.getCity("великий новгород", Instance.cities));
        Assert.assertEquals("Санкт-Петербург", DataParser.getCity("спб", Instance.cities));
        Assert.assertNull(DataParser.getCity("", Instance.cities));
        Assert.assertNull(DataParser.getCity("новгород", Instance.cities));
        logger.debug("getString passed tests with cities");

        Assert.assertEquals("eur", DataParser.getCity("евро в европе", Instance.currencies));
        Assert.assertEquals("jpy", DataParser.getCity("а иены", Instance.currencies));
        Assert.assertEquals("jpy", DataParser.getCity("а что насчет йен", Instance.currencies));
        Assert.assertNull(DataParser.getCity("европейцы", Instance.currencies));
        Assert.assertNull(DataParser.getCity("", Instance.currencies));
        logger.debug("getString passed tests with currencies");
    }

    @Test
    public void test_getDate() {
        Assert.assertEquals("25.05.2019", DataParser.getDate("qwe25.05.2019rty"));
        Assert.assertEquals("01.06.2019", DataParser.getDate("32.05.2019"));
        Assert.assertEquals("30.11.0002", DataParser.getDate("00.00.0000"));
        Assert.assertEquals("0", DataParser.getDate("11/11/2011"));
        Assert.assertEquals("1", DataParser.getDate("покажи курс цб на 25.05.2045"));
        logger.debug("getDate passed tests");
    }
}
