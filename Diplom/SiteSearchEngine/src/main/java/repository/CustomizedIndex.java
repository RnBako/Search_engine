package repository;


import java.util.List;

public interface CustomizedIndex<Index>{
    List<Index> findByLemmaAndLemmaSize(String lemmaList, Integer lemmasSize);
}
