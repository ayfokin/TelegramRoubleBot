import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

public class DataParserTest {
    private final Bot Instance = new Bot();
    private static final Logger logger = LogManager.getLogger();

    @Test
    public void testCity_getString() {
        Instance.initializeData();

        Assert.assertEquals("Москва", DataParser.getString("московский", Instance.cities));
        logger.debug("testCity_getString passed");
    }

    @Test
    public void testCity2Words_getString() {
        Instance.initializeData();

        Assert.assertEquals("Великий Новгород", DataParser.getString("великий новгород", Instance.cities));
        logger.debug("testCity2Words_getString passed");
    }

    @Test
    public void testCityAbbreviated_getString() {
        Instance.initializeData();

        Assert.assertEquals("Санкт-Петербург", DataParser.getString("спб", Instance.cities));
        logger.debug("testCityAbbreviated_getString passed");
    }

    @Test
    public void testCityEmpty_getString() {
        Instance.initializeData();

        Assert.assertNull(DataParser.getString("", Instance.cities));
        logger.debug("testCityEmpty_getString passed");
    }

    @Test
    public void testCurrencyEur_getString() {
        Instance.initializeData();

        Assert.assertEquals("eur", DataParser.getString("евро в европе", Instance.currencies));
        logger.debug("testCurrencyEur_getString passed");
    }

    @Test
    public void testCurrencyJpy_getString() {
        Instance.initializeData();

        Assert.assertEquals("jpy", DataParser.getString("а что насчет йен", Instance.currencies));
        logger.debug("testCurrencyJpy_getString passed");
    }

    @Test
    public void testCurrencyNo_getString() {
        Instance.initializeData();

        Assert.assertNull(DataParser.getString("европейцы", Instance.currencies));
        logger.debug("testCurrencyNo_getString passed");
    }

    @Test
    public void testCurrencyEmpty_getString() {
        Instance.initializeData();

        Assert.assertNull(DataParser.getString("", Instance.currencies));
        logger.debug("testCurrencyNo_getString passed");
    }

    @Test
    public void test_getDate() {
        Instance.initializeData();

        Assert.assertEquals("25.05.2019", DataParser.getDate("qwe25.05.2019rty"));
        logger.debug("test_getDate passed");
    }

    @Test
    public void testNonexistent_getDate() {
        Instance.initializeData();

        Assert.assertEquals("0", DataParser.getDate("32.05.2019"));
        logger.debug("testNonexistent_getDate passed");
    }

    @Test
    public void testWrongFormat_getDate() {
        Instance.initializeData();

        Assert.assertEquals("0", DataParser.getDate("11/11/2011"));
        logger.debug("testWrongFormat_getDate passed");
    }

    @Test
    public void testFuture_getDate() {
        Instance.initializeData();

        Assert.assertEquals("1", DataParser.getDate("покажи курс цб на 25.05.2045"));
        logger.debug("testFuture_getDate passed");
    }
}
