/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import android.appwidget.AppWidgetProvider;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private ArrayList<String> wordList;
    private HashSet<String> wordSet;
    private HashMap<Integer, ArrayList<String>> size2Words;
    private HashMap<String, ArrayList<String>> letters2words;
    private int wordLength = DEFAULT_WORD_LENGTH;


    public AnagramDictionary(Reader reader) throws IOException {

        BufferedReader in = new BufferedReader(reader);
        String line;
        wordList = new ArrayList<>();
        wordSet = new HashSet<>();
        letters2words = new HashMap<>();
        size2Words = new HashMap<>();

        while ((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);
            String k = sortLetters(word);
            if (letters2words.containsKey(k)) {
                letters2words.get(k).add(word);
            } else {
                ArrayList<String> t = new ArrayList<>();
                t.add(word);
                letters2words.put(k, t);
            }

            int l = k.length();

            if (size2Words.containsKey(l)) {
                size2Words.get(l).add(word);
            } else {
                ArrayList<String> t = new ArrayList<>();
                t.add(word);
                size2Words.put(l, t);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        return wordSet.contains(word) && !word.contains(base);
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        String k = sortLetters(targetWord);
        for (String i : wordList)
            if (k.equals(sortLetters(i)))
                result.add(i);
        return result;
    }

    public String sortLetters(String word) {
        char[] sortedWord = word.toCharArray();
        Arrays.sort(sortedWord);
        return new String(sortedWord);
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        /*
        ArrayList<String> result = new ArrayList<String>();
        String plusOne = word;

        for (String i : wordList){

            if ( i.length() == 1) {
                plusOne += i;
                plusOne = sortLetters(plusOne);
            }

            if (plusOne.equals(sortLetters(i))) {
                result.add(i);
            }
        }
        return result;
        */

        ArrayList<String> result = new ArrayList<>();
        String k;
        for (char i = 'a'; i <= 'z'; i++) {
            k = sortLetters(word.concat("" + i));

            if (letters2words.containsKey(k)) {
                for (String s : letters2words.get(k)) {
                    if (isGoodWord(s, word)){
                        result.add(s);
                    }
                }
            }
        }
        return result;
    }


    public String pickGoodStarterWord() {
        ArrayList<String> txt = size2Words.get(wordLength);

        if (wordLength > MAX_WORD_LENGTH) wordLength++;

        while (true) {
            String word = txt.get(random.nextInt(txt.size()));
            if (getAnagramsWithOneMoreLetter(word).size() >= MIN_NUM_ANAGRAMS)
                return word;
        }
    }

    public List<String> getAnagramsWithTwoLetters(String word) {
        List<String> res = new ArrayList<>();
        String add;

        for (char i = 'a'; i <= 'z'; i++) {
            for (char j = i; j < 'z'; j++) {
                add = sortLetters(word.concat("" + i + j));

                if (letters2words.containsKey(add)) {
                    for (String s : letters2words.get(add)) {
                        if (isGoodWord(s, word)){
                            res.add(s);
                        }
                    }
                }
            }
        }
        return res;
    }
}
