package agh.po.ewolucja.Classes;

public class StatsEntry {
    public Integer day = 0;
    public Integer grassNum = 0;
    public Integer animalsNum = 0;
    public Genotype dominatingGenotype;
    public Integer avgEnergy = 0;
    public Integer avgLifeTime = 0;
    public Double avgKidsNum = 0.0;

    public StatsEntry add(StatsEntry other){
        StatsEntry newEntry = new StatsEntry();
        newEntry.dominatingGenotype = other.dominatingGenotype;
        newEntry.grassNum = other.grassNum + grassNum;
        newEntry.animalsNum = other.animalsNum + animalsNum;
        newEntry.avgEnergy = other.avgEnergy + avgEnergy;
        newEntry.avgLifeTime = other.avgLifeTime + avgLifeTime;
        newEntry.avgKidsNum = other.avgKidsNum + avgKidsNum;

        return newEntry;
    }
}
