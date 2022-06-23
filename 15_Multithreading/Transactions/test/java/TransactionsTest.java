import junit.framework.TestCase;

public class TransactionsTest extends TestCase {

    private static Bank bank = new Bank();

    @Override
    protected void setUp() throws Exception {
        bank.addAccount("232037627122", new Account("232037627122", 627122L));
        bank.addAccount("951146896831", new Account("951146896831", 6831L));
        bank.addAccount("985496323120", new Account("985496323120", 23120L));
        bank.addAccount("962560416154", new Account("962560416154", 16154L));
        bank.addAccount("575333388913", new Account("575333388913", 88913L));
        bank.getAccounts().get("575333388913").setBlocked(true);
    }

    public void testBankTransferIsEmpty() {
        bank.transfer("23203762712", "95114689683", 5000L);
        long actual = bank.getBalance("951146896831");
        long expected = 6831;
        assertEquals(expected, actual);
    }

    public void testBankTransferBlocked() {
        bank.transfer("575333388913", "951146896831", 5000L);
        long actual = bank.getBalance("951146896831");
        long expected = 6831;
        assertEquals(expected, actual);
    }

    public void testBankTransferLess50000() {
        bank.transfer("232037627122", "951146896831", 5000L);
        long actual = bank.getBalance("951146896831");
        long expected = 6831 + 5000;
        assertEquals(expected, actual);
    }

    public void testBankTransferMore50000() {
        bank.transfer("232037627122", "962560416154", 50200L);
        long actual = bank.getBalance("962560416154");
        long expected = 16154 + 50200;
        assertEquals(expected, actual);
    }
}
