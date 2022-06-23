public class Printer {
    private String queue = "";
    int pendingPagesCount = 0;
    int totalNumberPagesPrinted = 0;

    //Методы пополнения очереди
    public  void append (String documentText) { append(documentText, "Название отсутствует", 1); }

    public  void append (String documentText, String documentName) { append(documentText, documentName, 1); }

    public void append (String documentText, String documentName, int numberOfPages) {
        queue = queue + "\n" + documentName + " - " +
                               numberOfPages + " стр.: " +
                               documentText;
        pendingPagesCount = pendingPagesCount + numberOfPages;
    }

    //Метод очистки очереди
    public void clear() {
        queue = "";
        pendingPagesCount = 0;
    }

    //Метод, выводящий информацию
    public  void print() {
        System.out.println("Очередь на печать:");
        if (queue.isEmpty()) {
            System.out.println("Очередь пуста");
        } else {
            System.out.println(queue);
            totalNumberPagesPrinted = totalNumberPagesPrinted + pendingPagesCount;
            clear();
        }
    }

    public int getPendingPagesCount() { return pendingPagesCount; }

    public int  getTotalNumberPagesPrinted() { return totalNumberPagesPrinted; }
}
