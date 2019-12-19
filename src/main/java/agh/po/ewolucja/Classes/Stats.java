package agh.po.ewolucja.Classes;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.LinkedList;

public class Stats {
    private LinkedList<StatsEntry> statsEntries = new LinkedList<>();

    public void addNewStat(int day, int grassNum, int animalsNum, Genotype dominatingGene, int avgenergy, int avgLifeTime, double avgKidsNum){
        StatsEntry st = new StatsEntry();
        st.day = day;
        st.grassNum = grassNum;
        st.animalsNum = animalsNum;
        st.dominatingGenotype = dominatingGene;
        st.avgEnergy = avgenergy;
        st.avgLifeTime = avgLifeTime;
        st.avgKidsNum = avgKidsNum;

        statsEntries.add(st);
    }

    public int getLastDay(){
        if(statsEntries.isEmpty()){
            return -1;
        }
        return statsEntries.getLast().day;
    }

    public void dumpHistory(BufferedWriter writer) throws IOException {
        StringBuilder stringBuilder = new StringBuilder(8000);
        StatsEntry temp;
        StatsEntry sum = new StatsEntry();
        int i=0;

        for (StatsEntry statsEntry : statsEntries) {
            temp = statsEntry;
            sum = sum.add(temp);
            i++;
            stringBuilder.append("Dzień: ");
            stringBuilder.append(i);
            stringBuilder.append(System.lineSeparator());

            stringBuilder.append("Średnia liczba zwierząt: ");
            stringBuilder.append(Math.round(sum.animalsNum / (double) i * 100) / 100.0);
            stringBuilder.append(System.lineSeparator());

            stringBuilder.append("Średnia liczba roślin: ");
            stringBuilder.append(Math.round(sum.grassNum / (double) i * 100) / 100);
            stringBuilder.append(System.lineSeparator());

            stringBuilder.append("Dominujący genotyp: ");
            stringBuilder.append(sum.dominatingGenotype);
            stringBuilder.append(System.lineSeparator());

            stringBuilder.append("Średni poziom energii: ");
            stringBuilder.append(Math.round(sum.avgEnergy / (double) i * 100) / 100.0);
            stringBuilder.append(System.lineSeparator());

            stringBuilder.append("Średnia długość życia: ");
            stringBuilder.append(Math.round(sum.avgLifeTime / (double) i * 100) / 100.0);
            stringBuilder.append(System.lineSeparator());

            stringBuilder.append("Średnia ilość dzieci: ");
            stringBuilder.append(Math.round(sum.avgKidsNum / (double) i * 100) / 100.0);
            stringBuilder.append(System.lineSeparator());
            stringBuilder.append("------------------------");

            stringBuilder.append(System.lineSeparator());

            writer.write(stringBuilder.toString());

            stringBuilder.setLength(0);
        }
    }
}
