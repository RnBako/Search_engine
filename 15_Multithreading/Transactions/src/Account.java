public class Account {

    private long money;
    private String accNumber;
    private Boolean isBlocked;

    public Account(String accNumber, long money) {
        this.money = money;
        this.accNumber = accNumber;
        this.isBlocked = false;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public String getAccNumber() {
        return accNumber;
    }

    public void setAccNumber(String accNumber) {
        this.accNumber = accNumber;
    }

    public Boolean getBlocked() {
        return isBlocked;
    }

    public void setBlocked(Boolean blocked) {
        isBlocked = blocked;
    }
}
