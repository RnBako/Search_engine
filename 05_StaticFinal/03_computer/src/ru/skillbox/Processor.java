package ru.skillbox;

public class Processor {
    private final Frequency frequency;
    private final Core core;
    private  final ProcessorVendor processorVendor;
    private final int weight;

    public Processor (Frequency frequency, Core core, ProcessorVendor processorVendor, int weight) {
        this.frequency = frequency;
        this.core = core;
        this.processorVendor = processorVendor;
        this.weight = weight;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public Core getCore() {
        return core;
    }

    public ProcessorVendor getProcessorVendor() {
        return processorVendor;
    }

    public int getWeight() {
        return weight;
    }
}
