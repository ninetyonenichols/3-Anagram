/**
 * AUTHOR: Justin Nichols
 * FILE: PA3Main.java
 * ASSIGNMENT: Programming Assignment 3 - Anagrams
 * COURSE: CSC210; Section D; Spring 2019
 * PURPOSE: Accepts a word and an input file (representing all acceptable words, one per line).
 *              Finds all anagrams of the given word using acceptable words.
 *              Prints results.
 *              
 * 
 * USAGE: 
 * java PA3Main dictFileName originalWord maxAnagramSize
 * 
 * where: dictFileName is the name of a file containing all acceptable words, one per line.
 *        originalWord is a String (the desired word that anagrams will be constructed out of)
 *        maxAnagramSize is an int (the maximum acceptable number of sub-words in an anagram).
 *     
 * 
 *  EXAMPLE INPUT (CREATED BY INSTRUCTORS, NOT BY ME)--
 *      Input File:                       
 *   
 * ---------     
 * | abash |
 * | aura  |
 * | bar   |
 * | barb  |
 * | bee   |
 * | beg   |
 * | blush |
 * ---------
 *  
 * Input-file format must match that shown above.
 * No support exists for any further commands
 */


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class PA3Main {
    public static void main(String[] args) {

        String originalWord = args[1];
        LetterInventory originalWordLI = new LetterInventory(originalWord);


        Scanner dict = obtainDict(args);
        Set<String> containedWords = buildContainedWords(dict,
                originalWordLI);

        List<String> partialAnagram = new ArrayList<String>();
        List<List<String>> anagramList = new ArrayList<List<String>>();
        int maxAnagramSize = Integer.parseInt(args[2]);
        buildAnagramList(originalWordLI, containedWords, partialAnagram,
                anagramList, maxAnagramSize);

        
        printResults(originalWord, originalWordLI, containedWords, anagramList);

    }

    /*
     * Gets the input file and makes sure it opens
     * 
     * @param String[] args, the command-line arguments
     * 
     * @return Scanner dict, the input file (each line is an accepted word)
     */
    public static Scanner obtainDict(String[] args) {
        String fname = args[0];

        Scanner dict = null;
        try {
            dict = new Scanner(new File(fname));
        } catch (FileNotFoundException fileNotFound) {
            fileNotFound.printStackTrace();
            System.exit(1);
        }
        return dict;
    }

    /*
     * builds the set of acceptable words 'contained' in the original word
     * 
     * @param Scanner dict. All acceptable words are contained in dict, one per
     * line
     * 
     * @param LetterInventory originalwordLI. Keeps track of properties of the
     * original word
     * 
     * @return Set<String> containedWords, the set of all acceptable 'words'
     * contained in the original word
     */
    public static Set<String> buildContainedWords (Scanner dict, LetterInventory originalWordLI) {
        
        Set<String> containedWords = new TreeSet<String>();

        String dictEntry = dict.nextLine();
        try {
            while (dictEntry != null) {
                if (originalWordLI.contains(dictEntry)) {
                    containedWords.add(dictEntry);
                }

                dictEntry = dict.nextLine();
            }
        } catch (NoSuchElementException noSuchElement) {
        }
        
        return containedWords;
    }


    /*
     * builds the list of anagrams for the input word
     * 
     * @param LetterInventory originalwordLI. Keeps track of properties of the
     * original word
     * 
     * @param Set<String> containedWords, the set of all acceptable 'words'
     * contained in the original word
     * 
     * @param List<String> partialAnagram, a candidate for being an initial
     * segment of an anagram
     * 
     * @param List<List<String>> anagramList, the list of all anagrams found for
     * the input word
     * 
     * @param int maxAnagramSize, the maximum acceptable size for an anagram. Given as a
     * command-line argument
     * 
     * @return n/a
     */
    public static void buildAnagramList(
            LetterInventory originalWordLI, Set<String> containedWords,
            List<String> partialAnagram, List<List<String>> anagramList, int maxAnagramSize) {

        // base case
        boolean noMaxSize = (maxAnagramSize == 0);
        boolean maxSizeReached = (maxAnagramSize > 0
                && partialAnagram.size() <= maxAnagramSize);
        if (originalWordLI.isEmpty() && (maxSizeReached || noMaxSize)) {
            List<String> anagram = new ArrayList<String>(partialAnagram);
            anagramList.add(anagram);

            return;
        } 
        
        // recursive case
        for (String word : containedWords) {
            if (originalWordLI.contains(word)) {
                // choosing
                originalWordLI.subtract(word);
                partialAnagram.add(word);

                // recursing
                buildAnagramList(originalWordLI, containedWords, partialAnagram,
                        anagramList, maxAnagramSize);

                // un-choosing
                partialAnagram.remove(word);
                originalWordLI.add(word);
            }
        }
    }

    /*
     * prints the results of the inquiry
     * 
     * @param String originalWord, the input word.
     * 
     * @param LetterInventory originalwordLI. Keeps track of properties of the
     * original word
     * 
     * @param Set<String> containedWords, the set of all acceptable 'words'
     * contained in the original word
     * 
     * @param List<List<String>> anagramList, the list of all anagrams found for
     * the input word
     * 
     * @return n/a
     */
    public static void printResults(String originalWord,
            LetterInventory originalWordLI, Set<String> containedWords,
            List<List<String>> anagramList) {
        System.out.printf("Phrase to scramble: %s \n", originalWord);
        System.out.println();

        System.out.printf("All words found in %s:\n", originalWord);
        System.out.println(containedWords);
        System.out.println();

        System.out.printf("Anagrams for %s:\n", originalWord);
        for (List<String> anagram : anagramList) {
            System.out.println(anagram);
        }
    }

}