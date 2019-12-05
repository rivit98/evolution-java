package agh.po.ewolucja;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Genotype {
    public static final Integer GENOTYPE_SIZE = 32;
    private Integer[] genotype;

    public Genotype(){
        genotype = new Integer[GENOTYPE_SIZE];
        this.randomize();
    }

    public Genotype(Integer[] gen){
        genotype = Arrays.copyOf(gen, GENOTYPE_SIZE);
        Arrays.sort(genotype);
    }

    public void setArray(Integer[] gen){
        genotype = Arrays.copyOf(gen, GENOTYPE_SIZE);
        Arrays.sort(genotype);
    }

    public String toString(){
        return Arrays.toString(this.genotype);
    }

    public Integer getRandom(){
        Random rand = new Random();
        return this.genotype[rand.nextInt(GENOTYPE_SIZE)];
    }

    public void fix(){
        Random rand = new Random();
        List<Integer> genotypeL = new ArrayList<>(Arrays.asList(this.genotype));
        while(!this.verify(genotypeL)){
            for(int i = 0; i < 8; i++){
                while(!genotypeL.contains(i)){
                    genotypeL.set(rand.nextInt(GENOTYPE_SIZE), i);
                }
            }
        }
        this.genotype = genotypeL.toArray(Integer[]::new);
        Arrays.sort(this.genotype);
    }

    public boolean verify(){
        return this.verify(new ArrayList<>(Arrays.asList(this.genotype)));
    }

    private boolean verify(List<Integer> l){
        List<Integer> genotypeL = l;
        for(int i = 0; i < 8; i++){
            if(!genotypeL.contains(i)){
                return false;
            }
        }

        return true;
    }

    public Genotype merge(Genotype g2){
        Random rand = new Random();
        List<Integer> indexesList = IntStream.range(1, GENOTYPE_SIZE).boxed().collect(Collectors.toCollection(LinkedList::new));

        Genotype first = this;
        Genotype second = g2;
        if(rand.nextBoolean()){
            first = g2;
            second = this;
        }

        Collections.shuffle(indexesList);
        Integer splitIndex1 = indexesList.get(0);
        Integer splitIndex2 = indexesList.get(1);

        if(splitIndex1 > splitIndex2){
            int temp = splitIndex1;
            splitIndex1 = splitIndex2;
            splitIndex2 = temp;
        }

        Integer[][] splittedG = new Integer[][]{Arrays.copyOfRange(first.genotype, 0, splitIndex1),
                                                Arrays.copyOfRange(first.genotype, splitIndex1, splitIndex2),
                                                Arrays.copyOfRange(first.genotype, splitIndex2, GENOTYPE_SIZE)};

        Integer[][] splittedG2 = new Integer[][]{Arrays.copyOfRange(second.genotype, 0, splitIndex1),
                Arrays.copyOfRange(second.genotype, splitIndex1, splitIndex2),
                Arrays.copyOfRange(second.genotype, splitIndex2, GENOTYPE_SIZE)};

        Integer from1 = 2;
        List<Integer> newGenotype = new ArrayList<>();
        List<Integer> toChoose = new LinkedList<>(Arrays.asList(0, 1,2));
        Collections.shuffle(toChoose);
        while(!(from1--).equals(0)){
            newGenotype.addAll(Arrays.asList(splittedG[toChoose.get(0)]));
            toChoose.remove(0);
        }

        newGenotype.addAll(Arrays.asList(splittedG2[toChoose.get(0)]));

        Genotype res = new Genotype(newGenotype.toArray(Integer[]::new));
        res.fix();

        return res;
    }

    public void randomize() {
        Random rand = new Random();
        this.genotype = Arrays.stream(this.genotype).map(v -> v = rand.nextInt(8)).toArray(Integer[]::new);
        this.fix();
        Arrays.sort(this.genotype);
    }
}
