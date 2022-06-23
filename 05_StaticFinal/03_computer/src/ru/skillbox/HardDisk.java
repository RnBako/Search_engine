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

    public HardDisk setDiskType(DiskType diskType) {
        return new HardDisk(diskType, diskSize, weight);
    }

    public DiskSize getDiskSize() {
        return diskSize;
    }

    public HardDisk setDiskSize(DiskSize diskSize) {
        return new HardDisk(diskType, diskSize, weight);
    }

    public int getWeight() {
        return weight;
    }

    public HardDisk setWeight(int weight) {
        return new HardDisk(diskType, diskSize, weight);
    }
}
