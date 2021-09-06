package ru.skillbox;

public class Computer {
    private Processor processor;
    private Ram ram;
    private HardDisk hardDisk;
    private Monitor monitor;
    private Keyboard keyboard;
    private final String vendor;
    private final String name;

    public Computer(Processor processor, Ram ram, HardDisk hardDisk, Monitor monitor, Keyboard keyboard, String vendor, String name) {
        this.processor = processor;
        this.ram = ram;
        this.hardDisk = hardDisk;
        this.monitor = monitor;
        this.keyboard = keyboard;
        this.vendor = vendor;
        this.name = name;
    }

    public int getComputerWeight() {
        return processor.getWeight() + ram.getWeight() + hardDisk.getWeight() + monitor.getWeight() + keyboard.getWeight();
    }

    public Processor getProcessor() {
        return processor;
    }

    public void setProcessor(Processor processor) {
        this.processor = processor;
    }

    public Ram getRam() {
        return ram;
    }

    public void setRam(Ram ram) {
        this.ram = ram;
    }

    public HardDisk getHardDisk() {
        return hardDisk;
    }

    public void setHardDisk(HardDisk hardDisk) {
        this.hardDisk = hardDisk;
    }

    public Monitor getMonitor() {
        return monitor;
    }

    public void setMonitor(Monitor monitor) {
        this.monitor = monitor;
    }

    public Keyboard getKeyboard() {
        return keyboard;
    }

    public void setKeyboard(Keyboard keyboard) {
        this.keyboard = keyboard;
    }

    public String getVendor() {
        return vendor;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return  "Компьютер " + name + ", от " + vendor + ":\n" +
                "Процессор c частотой " + processor.getFrequency() + ", " +
                                          processor.getCore() + " ядер, от " +
                                          processor.getProcessorVendor() + ";\n" +
                "ОЗУ " + ram.getRamType() + ", объемом - " + ram.getRamSize() + ";\n" +
                "Жёсткий диск " + hardDisk.getDiskType() + ", объемом - " + hardDisk.getDiskSize() + ";\n" +
                "Экран " + monitor.getMonitorType() + ", размером - " + monitor.getDiagonal() + ";\n" +
                "Клавиатура " + keyboard.getKeyboardType() + ((keyboard.getIsBacklight()==IsBacklight.YES)?", с подсветкой":", без подстветки") + ";\n" +
                "Общий вес: " + getComputerWeight() + " грамм.";
    }
}
