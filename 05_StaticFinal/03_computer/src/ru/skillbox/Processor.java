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

    public Processor setFrequency(Frequency frequency) {
        return new Processor(frequency, core, processorVendor, weight);
    }

    public Core getCore() {
        return core;
    }

    public Processor setCore(Core core) {
        return new Processor(frequency, core, processorVendor, weight);
    }

    public ProcessorVendor getProcessorVendor() {
        return processorVendor;
    }

    public Processor setProcessorVendor(ProcessorVendor processorVendor) {
        return new Processor(frequency, core, processorVendor, weight);
    }

    public int getWeight() {
        return weight;
    }

    public Processor setWeight (int weight) {
        return new Processor(frequency, core, processorVendor, weight);
    }
}
