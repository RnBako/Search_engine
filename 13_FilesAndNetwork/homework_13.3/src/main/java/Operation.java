import java.time.LocalDate;

public class Operation {
    private final String accountType;
    private final String accountNumber;
    private final String accountCurrency;
    private final LocalDate operationDate;
    private final String referenceWiring;
    private final String operationDescription;
    private final double income;
    private final double expense;

    public Operation(String accountType, String accountNumber, String accountCurrency, LocalDate operationDate, String referenceWiring, String operationDescription, double income, double expense) {
        this.accountType = accountType;
        this.accountNumber = accountNumber;
        this.accountCurrency = accountCurrency;
        this.operationDate = operationDate;
        this.referenceWiring = referenceWiring;
        this.operationDescription = operationDescription;
        this.income = income;
        this.expense = expense;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountCurrency() {
        return accountCurrency;
    }

    public LocalDate getOperationDate() {
        return operationDate;
    }

    public String getReferenceWiring() {
        return referenceWiring;
    }

    public String getOperationDescription() {
        return operationDescription;
    }

    public double getIncome() {
        return income;
    }

    public double getExpense() {
        return expense;
    }
}
