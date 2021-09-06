package ru.skillbox;

public class Main {

    public static void main(String[] args) {
        Computer computer = new Computer(new Processor(Frequency.FOR_MHZ, Core.FOUR, ProcessorVendor.INTEL,50),
                                         new Ram(RamType.DDR, RamSize.SIXTY_FOR, 100),
                                         new HardDisk(DiskType.SSD, DiskSize.FIVE_TB, 500),
                                         new Monitor(Diagonal.TWENTY_SEVEN,MonitorType.IPS, 2500),
                                         new Keyboard(KeyboardType.MECHANICAL, IsBacklight.NO, 300),
                                         "DNS",
                                         "Домашний компьтер");
        System.out.println(computer.toString());
    }
}
