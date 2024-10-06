import java.lang.String;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Part1 {
    public String readFile(String filePath) throws IOException {
        StringBuilder dna = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = reader.readLine()) != null) {
            dna.append(line.trim());
        }
        reader.close();
        return dna.toString();
    }

    public int findStopCodon(String dna, int startIndex, String stopCodon) {
        int index = dna.indexOf(stopCodon, startIndex + 3);
        while (index != -1) {
            if ((index - startIndex) % 3 == 0) {
                return index;
            } else {
                index = dna.indexOf(stopCodon, index + 1);
            }
        }
        return dna.length();
    }

    public String findGene(String dna, int start) {

        int index = dna.indexOf("ATG", start);
        if (index == -1)
            return "";
        else {
            int indexTAA = findStopCodon(dna, index, "TAA");;
            int indexTAG = findStopCodon(dna, index, "TAG");
            int indexTGA = findStopCodon(dna, index, "TGA");
            int min = Math.min(Math.min(indexTAA,indexTAG),indexTGA);
            if(min == dna.length())
            {
                return "";
            }
            return dna.substring(index, min + 3);
            
        }
    }
    public ArrayList<String> getAllGenes(String dna)
    {
        ArrayList<String> genes = new ArrayList<>();
        int index = 0;
        while (true) {
            String gene = findGene(dna,index);
            if (gene.isEmpty()) {
                break;
            }
            genes.add(gene);
           index = dna.indexOf(gene, index) + gene.length();
        }
        return genes;
    }

    public void testGetAllGenes(String dna) {
        System.out.println("Testing getAllGenes");
        ArrayList<String> genes = getAllGenes(dna);
        for (String gene : genes) {
            System.out.println("Found the gene: " + gene);
        }
    }
}

class Part2{
    static float cgRatio(String dna) {
        int count = 0;
        for (int i = 0; i < dna.length(); i++) {
            char c = dna.charAt(i);
            if (c == 'C' || c == 'G') {
                count++;
            }
        }
        return (float) count / dna.length();
    }

    public int countCTG(String dna) {
        int count = 0;
        int index = dna.indexOf("CTG");
        while (index != -1) {
            count++;
            index = dna.indexOf("CTG", index + 3);
        }
        return count;
    }
    public void testCgRatioAndCountCTG(String dna) {
        System.out.println("Testing cgRatio");
        System.out.println("CG Ratio: " + cgRatio(dna));

        System.out.println("Testing countCTG");
        System.out.println("CTG Count: " + countCTG(dna));
    }
}

class Part3 {
    public void processGenes(ArrayList<String> genes) {
        int longer60 = 0;
        int highCGRatio = 0;
        int maxLength = 0;

        for (String gene : genes) {
            if (gene.length() > 60) {
                System.out.println("Gene longer than 60 characters: " + gene);
                longer60++;
            }

            float cgRatio = Part2.cgRatio(gene);
            if (cgRatio > 0.35) {
                System.out.println("Gene with CG ratio higher than 0.35: " + gene);
                highCGRatio++;
            }

            if (gene.length() > maxLength) {
                maxLength = gene.length();
            }
        }

        System.out.println("Number of genes longer than 60 characters: " + longer60);
        System.out.println("Number of genes with CG ratio higher than 0.35: " + highCGRatio);
        System.out.println("Length of the longest gene: " + maxLength);
    }
}


public class Genes {
    public static void main(String[] args) {
        Part1 part1 = new Part1();
        Part2 part2 = new Part2();
        Part3 part3 = new Part3();

        //part1.printAllGenes("ATGAGTTAAATGTGTTATTAA");
        try {
            // Reading DNA from file
            String dna = part1.readFile("Java/Genes/brca1line.fa");
            dna = dna.toUpperCase();

            part1.testGetAllGenes(dna);

            part2.testCgRatioAndCountCTG(dna);

            ArrayList<String> genes = part1.getAllGenes(dna);
            part3.processGenes(genes);

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}
