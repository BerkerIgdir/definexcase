package com.example.demo.util;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class QueryWordRectifierUtil {
    private QueryWordRectifierUtil(){}
    private static final Map <String, List <String>> wordToMissSpelledMap = new ConcurrentHashMap <> ();
    static {
        wordToMissSpelledMap.put ("IPHONE", Arrays.asList ("AYFON","APHONE","IPHON"));
    }

    public static Map<String,List<String>> getWordToMissSpelledMap(){
        return Collections.unmodifiableMap (wordToMissSpelledMap);
    }

    public static List<String> get(String key){
        key = key.toUpperCase(Locale.ROOT);
       return Optional.ofNullable (wordToMissSpelledMap.get (key))
               .orElseGet (Collections::emptyList);
    }

    public static void put(String key, String value){
        key = key.toUpperCase(Locale.ROOT);
        value = value.toUpperCase(Locale.ROOT);
        if (Objects.nonNull (wordToMissSpelledMap.get (key))){
            wordToMissSpelledMap.get (key).add (value);
            return;
        }
        wordToMissSpelledMap.put (key,Arrays.asList (value));
    }

    public static Optional<String> checkAndGetIfMissSpelled(String wordToCheck){
       return wordToMissSpelledMap.entrySet ()
                .stream ()
                .filter (entry -> entry.getValue ().contains (wordToCheck))
                .map (Map.Entry::getKey)
                .findFirst ();
    }

}
