package ru.skillbox;

public class HardDisk {
    private final DiskType diskType;
    private final DiskSize diskSize;
    private final int weight;

    public HardDisk(DiskType diskType, DiskSize diskSize, int weight) {
        this.diskType = diskType;
        this.diskSize = diskSize;
        this.weight = weight;
    }

    public DiskType getDiskType() {
        return diskType;
    }

    public DiskSize getDiskSize() {
        return diskSize;
    }

    public int getWeight() {
        return weight;
    }
}
